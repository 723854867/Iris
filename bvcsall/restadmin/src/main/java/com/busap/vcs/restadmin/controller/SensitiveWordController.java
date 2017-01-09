package com.busap.vcs.restadmin.controller;

import java.io.*;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.BaseEntity;
import com.busap.vcs.data.entity.SensitiveWord;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.SensitiveWordService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 敏感词增删改查
 *
 * @author klh
 */
@Controller()
@RequestMapping("/sensitiveWord")
public class SensitiveWordController extends CRUDController<SensitiveWord, Long> {
    @Resource(name = "sensitiveWordService")
    private SensitiveWordService sensitiveWordService;

    @Value("${files.localpath}")
    private String basePath;

    @Resource(name="jedisService")
    private JedisService jedisService;

    private static final Logger logger = LoggerFactory.getLogger(SensitiveWordController.class);

    @Resource(name = "sensitiveWordService")
    @Override
    public void setBaseService(BaseService<SensitiveWord, Long> baseService) {
        this.baseService = baseService;
    }

    @EnableFunction("敏感词管理,查看敏感词列表")
    @RequestMapping("sensitiveWordlist")
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sensitiveWord/sensitiveWord");
        return mav;
    }

    @RequestMapping("querySensitiveWordList")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> querySensitiveWordList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                     @RequestParam(value = "word", required = false) String word){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("word",word);
        List<SensitiveWord> list = sensitiveWordService.querySensitiveWords(params);
        Page page = PagingContextHolder.getPage();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",page.getTotalResult());
        map.put("rows",list);
        return map;
    }

    @EnableFunction("敏感词管理,更新敏感词信息")
    @RequestMapping(value = {"/updatepage"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody updatepage(SensitiveWord entity, HttpServletRequest req) {
        if (!validator(entity, BaseEntity.Update.class)) {
            return respBodyWriter.toError(entity);
        }
        SensitiveWord dbEntity = this.sensitiveWordService.find(entity.getId());
        if (entity.getStatus() == 1) {//暂停屏蔽
            sensitiveWordService.sendWordToZookeeper("delete_" + dbEntity.getWord() + "_" + entity.getWord());//发送zookeeper
        } else {//屏蔽
            sensitiveWordService.sendWordToZookeeper("update_" + dbEntity.getWord() + "_" + entity.getWord());//发送zookeeper
        }
        String[] ps = new String[]{"word", "status"};
        try {
            for (String s : ps) {
                BeanUtils.setProperty(dbEntity, s, BeanUtils.getProperty(entity, s));
            }
        } catch (Exception e) {
            this.logger.error("copy properties error", e);
            return this.respBodyWriter.toError("该词已存在");
        }
        List<Filter> filters = new ArrayList<Filter>();
        Filter filter1 = Filter.eq("word", dbEntity.getWord());
        Filter filter2 = Filter.ne("id", dbEntity.getId());
        filters.add(filter1);
        filters.add(filter2);
        boolean bool = baseService.exists(filters);
        if (bool) {
            return this.respBodyWriter.toError("该词已存在");
        }
        baseService.update(dbEntity);
        jedisService.incr(BicycleConstants.SENSITIVE_WORD_UPDATE_COUNT);
        return respBodyWriter.toSuccess(dbEntity);
    }

    @EnableFunction("敏感词管理,删除敏感词信息")
    @RequestMapping("/deleteWord")
    @ResponseBody
    public RespBody deleteWord(Long id) {
        SensitiveWord word = sensitiveWordService.find(id);
        baseService.delete(id);
        sensitiveWordService.sendWordToZookeeper("delete_" + word.getWord());//发送zookeeper
        jedisService.incr(BicycleConstants.SENSITIVE_WORD_UPDATE_COUNT);
        return respBodyWriter.toSuccess();
    }

    @RequestMapping("/createWord")
    @ResponseBody
    public RespBody createWord(SensitiveWord word) {
        Filter filter = Filter.eq("word", word.getWord());
        boolean bool = baseService.exists(filter);
        if (bool) {
            return respBodyWriter.toError("该词已存在");
        }
        baseService.save(word);
        sensitiveWordService.sendWordToZookeeper("insert_" + word.getWord());//发送zookeeper
        jedisService.incr(BicycleConstants.SENSITIVE_WORD_UPDATE_COUNT);
        return respBodyWriter.toSuccess();
    }

    @EnableFunction("敏感词管理,添加敏感词信息")
    @RequestMapping("doInsert")
    @ResponseBody
    public Map<String, Object> doInsert(String word) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("keyword",word);
        List<SensitiveWord> list = sensitiveWordService.querySensitiveWords(params);
        Map<String, Object> map = new HashMap<String, Object>();
        if(list.isEmpty()){
            SensitiveWord sensitiveWord = new SensitiveWord();
            sensitiveWord.setWord(word);
            sensitiveWord.setCreateDateStr(new Date());
            sensitiveWord.setStatus(0);
            sensitiveWord.setCreatorId(U.getUid());
            int result = sensitiveWordService.insert(sensitiveWord);
            if (result > 0) {
                jedisService.setValueToSetInShard(BicycleConstants.SENSITIVE_WORDS, word);
                map.put("resultCode", "ok");
                map.put("resultMessage", "添加成功！");
            } else {
                map.put("resultCode", "error");
                map.put("resultMessage", "添加失败！");
            }
        }else {
            map.put("resultCode", "error");
            map.put("resultMessage", "敏感词已存在！");
        }
        jedisService.incr(BicycleConstants.SENSITIVE_WORD_UPDATE_COUNT);
        return map;
    }

    @EnableFunction("敏感词管理,删除敏感词信息")
    @RequestMapping("deleteSensitiveWord")
    @ResponseBody
    public Map<String, Object> deleteSensitiveWord(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        SensitiveWord sensitiveWord = sensitiveWordService.querySensitiveWord(id);
        if(sensitiveWord == null){
            map.put("resultCode", "error");
            map.put("resultMessage", "删除失败，敏感词不存在！");
        }
        int result = sensitiveWordService.deleteBySensitiveWordId(id);
        if (result > 0) {
            jedisService.deleteSetItemFromShard(BicycleConstants.SENSITIVE_WORDS, sensitiveWord.getWord());
            map.put("resultCode", "ok");
            map.put("resultMessage", "删除成功！");
        } else {
            map.put("resultCode", "error");
            map.put("resultMessage", "删除失败！");
        }
        jedisService.incr(BicycleConstants.SENSITIVE_WORD_UPDATE_COUNT);
        return map;
    }

    @EnableFunction("敏感词管理,批量导入敏感词信息")
    @RequestMapping("doBatchImport")
    @ResponseBody
    public Map<String, Object> doBatchImport(@RequestParam("wordFile") MultipartFile wordFile) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!wordFile.isEmpty()) {
            String path = File.separator + "sensitiveWord" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
            String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmm_") + wordFile.getOriginalFilename();
            try {
                FileUtils.copyInputStreamToFile(wordFile.getInputStream(), new File(basePath + path, originalFilename));
                String txtStr = readTxt(basePath + path + originalFilename);
                List<SensitiveWord> list = new ArrayList<SensitiveWord>();
                String txt = txtStr.substring(0, txtStr.length() - 1);
                String[] txtArray = txt.split(",");
                List<String> listArray = Arrays.asList(txtArray);
                String wordStr = "";
                for (String word : listArray) {
                    SensitiveWord sensitiveWord = new SensitiveWord();
                    sensitiveWord.setWord(word.trim());
                    sensitiveWord.setStatus(0);
                    sensitiveWord.setCreateDateStr(new Date());
                    sensitiveWord.setCreatorId(U.getUid());
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("keyword", word);
                    List<SensitiveWord> sensitiveWordList = sensitiveWordService.querySensitiveWords(params);
                    if (sensitiveWordList.isEmpty()) {
                        list.add(sensitiveWord);
                        jedisService.setValueToSetInShard(BicycleConstants.SENSITIVE_WORDS, word);
                    } else {
                        wordStr += word + ",";
                    }

                }
                if (!list.isEmpty()) {
                    int result = sensitiveWordService.batchInsertSensitiveWord(list);
                    if (result == list.size() && result > 0) {
                        map.put("resultCode", "ok");
                        map.put("resultMessage", wordStr.substring(0, wordStr.length() - 1) + "已存在，其它敏感词导入成功！");
                    } else {
                        map.put("resultCode", "error");
                        map.put("resultMessage", "导入失败！");
                    }
                } else {
                    map.put("resultCode", "error");
                    map.put("resultMessage", "导入失败，请检查文件中的敏感词！");
                }
                return map;
            } catch (IOException e) {
                map.put("resultCode", "error");
                map.put("resultMessage", "导入异常，请重试！");
                e.printStackTrace();
            }
        } else {
            map.put("resultCode", "error");
            map.put("resultMessage", "文件不能为空！");
        }
        jedisService.incr(BicycleConstants.SENSITIVE_WORD_UPDATE_COUNT);
        return map;
    }

    /**
     * 读取文件
     *
     * @param readPath 文件路径.
     * @return
     */
    public String readTxt(String readPath) {
        String readTxt = "";
        try {
            File f = new File(readPath);
            if (f.isFile() && f.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(f), "UTF-8");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    readTxt += line + ",";
                }
                read.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readTxt;
    }


}
