package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.Constants;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.enums.Platform;
import com.busap.vcs.data.model.GiftDisplay;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.GiftService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.*;

/**
 * Created by busap on 2015/12/23.
 */
@Controller
@RequestMapping("gift")
public class GiftController extends CRUDController<Gift, Long> {

    @Value("${files.localpath}")
    private String basePath;

    @Resource(name = "giftService")
    private GiftService giftService;

    @Resource(name = "jedisService")
    private JedisService jedisService;

    @Resource(name = "giftService")
    @Override
    public void setBaseService(BaseService<Gift, Long> baseService) {
        this.baseService = baseService;
    }


    //礼物列表页跳转
    @RequestMapping("forwardGiftList")
    public ModelAndView forwardGiftList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("gift/query_gift");
        return mav;
    }

    //查询礼物列表信息
    @RequestMapping("queryGiftList")
    @ResponseBody
    @EnablePaging
    @EnableFunction("礼物管理,查看礼物列表信息")
    public Map<String, Object> queryGiftList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                             @ModelAttribute("giftDisplay") @Valid GiftDisplay giftDisplay, BindingResult results) {
        List<Gift> list = giftService.selectAll(giftDisplay);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    //添加礼物信息
    @RequestMapping("insertGift")
    @ResponseBody
    @EnableFunction("礼物管理,添加礼物信息")
    public ResultData insertGift(@RequestParam("giftIconUrl") MultipartFile giftIconUrl,
                                 @RequestParam(value = "effectFileUrl", required = false) MultipartFile effectFileUrl,
                                 @ModelAttribute("gift") @Valid Gift gift, BindingResult results) {
        ResultData resultData = new ResultData();
        if (giftIconUrl.isEmpty()) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("请至少选择一个jpg，jpeg，png，gif，icon格式的文件");
            return resultData;
        }
        String format = giftIconUrl.getOriginalFilename().substring(giftIconUrl.getOriginalFilename().lastIndexOf("."));
        if (!(Constants.IMAGE_FORMAT.indexOf(format.toLowerCase()) != -1)) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("请选择jpg，jpeg，png，gif，icon格式的文件");
            return resultData;
        }
        if (!effectFileUrl.isEmpty()) {
            String effectFormat = effectFileUrl.getOriginalFilename().substring(effectFileUrl.getOriginalFilename().lastIndexOf("."));
            if (!(Constants.IMAGE_FORMAT.indexOf(effectFormat.toLowerCase()) != -1)) {
                resultData.setResultCode("fail");
                resultData.setResultMessage("请选择jpg，jpeg，png，gif，icon格式的文件");
                return resultData;
            }
        }
        if (gift.getIsFree() == 0) { //0代表不免费 免费次数设置为0
            gift.setFreeCount(0);
        } else { //其它代表免费 价格及对应拍豆数量设置为0
            gift.setPrice(0);
            gift.setPoint(0);
        }
        if(gift.getScreenshotSupport() == 0){
            gift.setScreenshotNumber(0);//如果该礼物不支持截屏，则截屏次数为0
        }
        gift.setCreateDateStr(new Date());
        String giftIcon = uploadGiftFile(giftIconUrl, "gift", "icon");
        String effectFile = uploadGiftFile(effectFileUrl, "gift", "effect");
        gift.setGiftIconUrl(giftIcon);
        gift.setEffectFileUrl(effectFile);
        gift.setCreatorId(U.getUid());
        gift.setDataFrom(DataFrom.移动麦视后台.getName());
        int ret = giftService.insert(gift);
        if (ret > 0) {
            jedisService.saveAsMap(BicycleConstants.GIFT + gift.getId(), gift);
            jedisService.setValueToSortedSetInShard(BicycleConstants.GIFT_ID, gift.getWeight(), String.valueOf(gift.getId()));
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败");
        }

        return resultData;
    }

    @RequestMapping("updateGiftTemplate")
    public ModelAndView updateGiftTemplate(Long id) {
        ModelAndView mav = new ModelAndView();
        Gift gift = giftService.selectByPrimaryKey(id);
        mav.addObject("gift", gift);
        mav.setViewName("gift/update_gift");
        return mav;
    }

    //更新礼物信息
    @RequestMapping("updateGift")
    @ResponseBody
    @EnableFunction("礼物管理,更新礼物信息")
    public ResultData updateGift(@RequestParam("giftIconUrl") MultipartFile giftIconUrl,
                                 @RequestParam(value = "effectFileUrl", required = false) MultipartFile effectFileUrl,
                                 @ModelAttribute("gift") @Valid Gift gift, BindingResult results) {
        ResultData resultData = new ResultData();
        Gift giftObject = giftService.selectByPrimaryKey(gift.getId());
        if (giftObject != null) {
            if (!giftIconUrl.isEmpty()) {
                String format = giftIconUrl.getOriginalFilename().substring(giftIconUrl.getOriginalFilename().lastIndexOf("."));
                if (!(Constants.IMAGE_FORMAT.indexOf(format.toLowerCase()) != -1)) {
                    resultData.setResultCode("fail");
                    resultData.setResultMessage("请选择jpg，jpeg，png，gif，icon格式的文件");
                    return resultData;
                }
                String giftIcon = uploadGiftFile(giftIconUrl, "gift", "icon");
                gift.setGiftIconUrl(giftIcon);
            }
            if (!effectFileUrl.isEmpty()) {
                String format = effectFileUrl.getOriginalFilename().substring(effectFileUrl.getOriginalFilename().lastIndexOf("."));
                if (!(Constants.IMAGE_FORMAT.indexOf(format.toLowerCase()) != -1)) {
                    resultData.setResultCode("fail");
                    resultData.setResultMessage("请选择jpg，jpeg，png，gif，icon格式的文件");
                    return resultData;
                }
                String effectFile = uploadGiftFile(effectFileUrl, "gift", "effect");
                gift.setEffectFileUrl(effectFile);
            }
            //礼物价格 是否赠送 赠送数量不可修改
 /*           gift.setPrice(giftObject.getPrice());
            gift.setIsFree(giftObject.getIsFree());
            gift.setFreeCount(giftObject.getFreeCount());*/
            /*if (gift.getIsFree() == 0) { //0代表不免费 免费次数设置为0
                gift.setFreeCount(0);
            } else { //其它代表免费 价格及对应拍豆数量设置为0
                gift.setPrice(0);
                gift.setPoint(0);
            }*/
            //礼物价格 拍豆 是否免费 免费次数不允许修改 20160122 19:04
            /*gift.setPrice(giftObject.getPrice());
            gift.setPoint(giftObject.getPoint());
            gift.setIsFree(giftObject.getIsFree());
            gift.setFreeCount(giftObject.getFreeCount());*/
            int ret = giftService.updateByPrimaryKey(gift);
            if (ret > 0) {
                Gift giftData = giftService.selectByPrimaryKey(gift.getId());
                jedisService.saveAsMap(BicycleConstants.GIFT + giftData.getId(), giftData);
                jedisService.setValueToSortedSetInShard(BicycleConstants.GIFT_ID, giftData.getWeight(), String.valueOf(giftData.getId()));
                resultData.setResultCode("success");
                resultData.setResultMessage("更新成功");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("更新失败");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("数据不存在");
        }
        return resultData;
    }

    @RequestMapping("updateState")
    @ResponseBody
    @EnableFunction("礼物管理,设置上下架或new等")
    public ResultData updateState(Long id, Integer type, String value) {
        ResultData resultData = new ResultData();
        Gift gift = new Gift();
        if (type == 1) {
            gift.setState(Integer.valueOf(value));
        } else {
            gift.setMarkerState(value);
        }
        gift.setId(id);
        int ret = giftService.updateByPrimaryKey(gift);
        if (ret > 0) {
            Gift giftObject = giftService.selectByPrimaryKey(id);
            jedisService.saveAsMap(BicycleConstants.GIFT + giftObject.getId(), giftObject);
            resultData.setResultCode("success");
            resultData.setResultMessage("设置成功");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("设置失败");
        }
        return resultData;
    }

    @RequestMapping("updateWeight")
    @ResponseBody
    @EnableFunction("礼物管理,修改权重")
    public ResultData updateWeight(Long id, Double weight) {
        ResultData resultData = new ResultData();
        Gift gift = new Gift();
        gift.setWeight(weight);
        gift.setId(id);
        int ret = giftService.updateByPrimaryKey(gift);
        if (ret > 0) {
            Gift giftObject = giftService.selectByPrimaryKey(id);
            jedisService.saveAsMap(BicycleConstants.GIFT + giftObject.getId(), giftObject);
            jedisService.setValueToSortedSetInShard(BicycleConstants.GIFT_ID, giftObject.getWeight(), String.valueOf(giftObject.getId()));
            resultData.setResultCode("success");
            resultData.setResultMessage("更改成功");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("更改失败");
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

}
