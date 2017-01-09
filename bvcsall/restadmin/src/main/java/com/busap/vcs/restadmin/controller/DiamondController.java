package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.Constants;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Diamond;
import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.model.DiamondDisplay;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.DiamondService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2015/12/23.
 */
@Controller
@RequestMapping("/diamond")
public class DiamondController extends CRUDController<Diamond, Long> {

    @Value("${files.localpath}")
    private String basePath;

    @Resource(name = "jedisService")
    private JedisService jedisService;

    @Resource
    private DiamondService diamondService;

    @Override
    public void setBaseService(BaseService<Diamond, Long> baseService) {
        this.baseService = baseService;
    }

    //拍币列表页跳转
    @RequestMapping("forwardDiamondList")
    public ModelAndView forwardDiamondList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("diamond/query_diamond");
        mav.addObject("selected","diamond");
        return mav;
    }

    //查询拍币列表信息
    @RequestMapping("queryDiamondList")
    @ResponseBody
    @EnablePaging
    @EnableFunction("拍币管理,查看拍币列表信息")
    public Map<String, Object> queryDiamondList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                @ModelAttribute("diamond") @Valid DiamondDisplay diamond,
                                                BindingResult results) {
        List<Diamond> list = diamondService.selectAll(diamond);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    //添加拍币信息
    @RequestMapping("insertDiamond")
    @ResponseBody
    @EnableFunction("拍币管理,添加拍币信息")
    public ResultData insertDiamond(MultipartFile diamondIconUrl, @ModelAttribute("diamond") @Valid Diamond diamond,
                                    BindingResult results) {
        ResultData resultData = new ResultData();
        if (diamondIconUrl.isEmpty()) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("请至少选择一个jpg，jpeg，png，gif，icon格式的文件");
            return resultData;
        }
        String format = diamondIconUrl.getOriginalFilename().substring(diamondIconUrl.getOriginalFilename().lastIndexOf("."));
        if (!(Constants.IMAGE_FORMAT.indexOf(format.toLowerCase()) != -1)) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("请选择jpg，jpeg，png，gif，icon格式的文件");
            return resultData;
        }
        String diamondIcon = uploadGiftFile(diamondIconUrl, "diamond", "icon");
        diamond.setDiamondIconUrl(diamondIcon);
        diamond.setCreateDateStr(new Date());
        diamond.setCreatorId(U.getUid());
        diamond.setDataFrom(DataFrom.移动麦视后台.getName());
        //如果为不赠送，设置赠送数量为0
        if (diamond.getIsGive() == 0) {
            diamond.setGiveCount(0);
        }
        int result = diamondService.insert(diamond);
        if (result > 0) {
            jedisService.saveAsMap(BicycleConstants.DIAMOND + diamond.getId(), diamond);
            if (diamond.getPayMethod() == 1) {
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_OTHER_ID, diamond.getWeight(), String.valueOf(diamond.getId()));
            }else if(diamond.getPayMethod() == 2) { 
            	jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_BIGCHARGE_ID, diamond.getWeight(), String.valueOf(diamond.getId()));
            }else if(diamond.getPayMethod() == 3){
            	jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_YINGYONGBAO_ID, diamond.getWeight(), String.valueOf(diamond.getId()));
            }else {
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_APPLE_ID, diamond.getWeight(), String.valueOf(diamond.getId()));
            }
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功！");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败！");
        }

        return resultData;
    }

    @RequestMapping("updateDiamondTemplate")
    public ModelAndView updateDiamondTemplate(Long id) {
        ModelAndView mav = new ModelAndView();
        Diamond diamond = diamondService.selectByPrimaryKey(id);
        mav.addObject("diamond", diamond);
        mav.setViewName("diamond/update_diamond");
        return mav;
    }

    @RequestMapping("update")
    public String updateDiamond(Long id) {
        diamondService.deleteByPrimaryKey(id);
        return "";
    }


    //更新拍币信息
    @RequestMapping("updateDiamond")
    @ResponseBody
    @EnableFunction("拍币管理,更新拍币信息")
    public ResultData updateDiamond(@RequestParam(value = "diamondIconUrl", required = false) MultipartFile diamondIconUrl,
                                    @ModelAttribute("diamond") @Valid Diamond diamond, BindingResult results) {
        Diamond d = diamondService.selectByPrimaryKey(diamond.getId());
        ResultData resultData = new ResultData();
        if(d == null){
            //如果拍币信息不存在 返回失败信息
            resultData.setResultCode("fail");
            resultData.setResultMessage("更新失败");
            return resultData;
        }

        if (!diamondIconUrl.isEmpty()) {
            String format = diamondIconUrl.getOriginalFilename().substring(diamondIconUrl.getOriginalFilename().lastIndexOf("."));
            if (!(Constants.IMAGE_FORMAT.indexOf(format.toLowerCase()) != -1)) {
                resultData.setResultCode("fail");
                resultData.setResultMessage("请选择jpg，jpeg，png，gif，icon格式的文件");
                return resultData;
            }
            String diamondIcon = uploadGiftFile(diamondIconUrl, "diamond", "icon");
            diamond.setDiamondIconUrl(diamondIcon);
        }
        //如果为不赠送，设置赠送数量为0
        if (diamond.getIsGive() == 0) {
            diamond.setGiveCount(0);
        }

        int result = diamondService.updateByPrimaryKey(diamond);
        if (result > 0) {
            Diamond diamondObj = diamondService.selectByPrimaryKey(diamond.getId());//查询拍币最新信息 写入redis
            jedisService.saveAsMap(BicycleConstants.DIAMOND + diamondObj.getId(), diamondObj);
            if (diamond.getPayMethod() == 1) {
                //其它支付方式
                if (d.getPayMethod() == 0) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_APPLE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 2) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_BIGCHARGE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 3) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_YINGYONGBAO_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                //更新redis
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_OTHER_ID, diamondObj.getWeight(), String.valueOf(diamondObj.getId()));
            }else if(diamond.getPayMethod() == 2) { 
            	if (d.getPayMethod() == 0) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_APPLE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 1) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_OTHER_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 3) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_YINGYONGBAO_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                //更新redis
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_BIGCHARGE_ID, diamondObj.getWeight(), String.valueOf(diamondObj.getId()));
            }else if(diamond.getPayMethod() == 3) { 
            	if (d.getPayMethod() == 0) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_APPLE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 1) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_OTHER_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 2) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_BIGCHARGE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                //更新redis
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_YINGYONGBAO_ID, diamondObj.getWeight(), String.valueOf(diamondObj.getId()));
            }else {
                if (d.getPayMethod() == 1) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_OTHER_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 2) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_BIGCHARGE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 3) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_YINGYONGBAO_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_APPLE_ID, diamondObj.getWeight(), String.valueOf(diamondObj.getId()));
            }
            resultData.setResultCode("success");
            resultData.setResultMessage("更新成功");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("更新失败");
        }
        return resultData;
    }

    @RequestMapping("updateState")
    @ResponseBody
    @EnableFunction("拍币管理,设置拍币上下架")
    public ResultData updateState(Long id, Integer value) {
        Diamond d = diamondService.selectByPrimaryKey(id);
        ResultData resultData = new ResultData();
        if(d == null){
            //如果拍币信息不存在 返回失败信息
            resultData.setResultCode("fail");
            resultData.setResultMessage("设置失败，不存在此条记录！");
            return resultData;
        }

        Diamond diamond = new Diamond();
        diamond.setId(id);
        diamond.setState(value);
        int ret = diamondService.updateByPrimaryKey(diamond);
        if (ret > 0) {
            Diamond diamondObj = diamondService.selectByPrimaryKey(id);
            jedisService.saveAsMap(BicycleConstants.DIAMOND + diamondObj.getId(), diamondObj);
            resultData.setResultCode("success");
            resultData.setResultMessage("设置成功！");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("设置失败！");
        }
        return resultData;
    }

    /**
     * 上传file
     *
     * @param file
     */
    private String uploadGiftFile(@RequestParam MultipartFile file, String filePath, String filePathName) {
        if (file.isEmpty()) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        String sufix = fileName.substring(fileName.lastIndexOf("."));
        String zipPath = File.separator + filePath + File.separator + filePathName + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
        String zipFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss") + sufix;
        String zipUrl = "";
        try {
            File mFile = new File(basePath + zipPath, zipFilename);
            FileUtils.copyInputStreamToFile(file.getInputStream(), mFile);
            zipUrl = zipPath + zipFilename;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipUrl;

    }

    @RequestMapping("updateWeight")
    @ResponseBody
    @EnableFunction("拍币管理,修改拍币权重")
    public ResultData updateWeight(Long id, Double weight) {
        ResultData resultData = new ResultData();
        Diamond d = diamondService.selectByPrimaryKey(id);
        if(d == null){
            //如果拍币信息不存在 返回失败信息
            resultData.setResultCode("fail");
            resultData.setResultMessage("更新失败，不存在此条记录信息！");
            return resultData;
        }
        Diamond diamond = new Diamond();
        diamond.setId(id);
        diamond.setWeight(weight);
        int ret = diamondService.updateByPrimaryKey(diamond);
        if (ret > 0) {
            Diamond diamondObj = diamondService.selectByPrimaryKey(diamond.getId());
            jedisService.saveAsMap(BicycleConstants.DIAMOND + diamondObj.getId(), diamondObj);
            
            
            if (diamondObj.getPayMethod() == 1) {
                if (d.getPayMethod() == 0) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_APPLE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 2) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_BIGCHARGE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 3) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_YINGYONGBAO_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_OTHER_ID, diamondObj.getWeight(), String.valueOf(diamondObj.getId()));
            } else if(diamond.getPayMethod() == 2) { 
            	if (d.getPayMethod() == 0) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_APPLE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 1) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_OTHER_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 3) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_YINGYONGBAO_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                //更新redis
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_BIGCHARGE_ID, diamondObj.getWeight(), String.valueOf(diamondObj.getId()));
            }else if(diamond.getPayMethod() == 3) { 
            	if (d.getPayMethod() == 0) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_APPLE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 1) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_OTHER_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 2) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_BIGCHARGE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                //更新redis
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_YINGYONGBAO_ID, diamondObj.getWeight(), String.valueOf(diamondObj.getId()));
            }else {
                if (d.getPayMethod() == 1) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_OTHER_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 2) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_BIGCHARGE_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                if (d.getPayMethod() == 3) {
                    //删除之前redis中已存在的信息
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.DIAMOND_YINGYONGBAO_ID, String.valueOf(d.getWeight()), String.valueOf(d.getId()));
                }
                jedisService.setValueToSortedSetInShard(BicycleConstants.DIAMOND_APPLE_ID, diamondObj.getWeight(), String.valueOf(diamondObj.getId()));
            }
            resultData.setResultCode("success");
            resultData.setResultMessage("更新成功！");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("更新失败！");
        }
        return resultData;
    }

}
