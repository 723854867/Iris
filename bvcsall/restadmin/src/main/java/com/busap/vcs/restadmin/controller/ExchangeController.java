package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.Constants;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Exchange;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.ExchangeService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能 兑换/提现 action
 * Created by huoshanwei on 2016/4/6.
 */
@Controller
@RequestMapping("/exchange")
public class ExchangeController {

    @Value("${files.localpath}")
    private String basePath;

    @Resource
    private ExchangeService exchangeService;

    @Resource
    private JedisService jedisService;

    /**
     * 页面跳转
     */
    @RequestMapping("forwardExchangeList")
    public ModelAndView forwardExchangeList(int type) {
        ModelAndView mav = new ModelAndView();
        if(type == 1) {
            mav.addObject("selected","exchange");
            mav.setViewName("exchange/query_exchange");
        }else{
            mav.addObject("selected","withdrawals");
            mav.setViewName("exchange/query_withdrawals");
        }
        mav.addObject("rate",jedisService.get(BicycleConstants.DIVIDE_RATE));
        return mav;
    }

    //查询列表信息
    @RequestMapping("queryExchangeList")
    @ResponseBody
    @EnablePaging
    @EnableFunction("兑换管理,查看兑换列表信息")
    public Map<String, Object> queryExchangeList(@ModelAttribute("queryPage") JQueryPage queryPage,Integer type,
                                                 @RequestParam(value = "name",required = false) String name,
                                                 @RequestParam(value = "state",required = false) Integer state) {
        Exchange exchange = new Exchange();
        exchange.setType(type);
        exchange.setName(name);
        exchange.setState(state);
        List<Exchange> list = exchangeService.selectAll(exchange);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    //添加信息
    @RequestMapping("insertExchange")
    @ResponseBody
    @EnableFunction("兑换管理,添加兑换信息")
    public ResultData insertExchange(@RequestParam(value = "iconUrl", required = false) MultipartFile iconUrl,
                                     @ModelAttribute("exchange") @Valid Exchange exchange, BindingResult results) {
        ResultData resultData = new ResultData();
        if(exchange.getType() == 1){
            Double diamondCount = exchange.getPointCount() * Double.valueOf(jedisService.get(BicycleConstants.DIVIDE_RATE)) * 10;
            if(diamondCount.intValue() != diamondCount){
                resultData.setResultCode("fail");
                resultData.setResultMessage("金币数量不能包含小数点");
                return resultData;
            }
            Double price = exchange.getPointCount() * Double.valueOf(jedisService.get(BicycleConstants.DIVIDE_RATE));
            exchange.setDiamondCount(diamondCount.intValue());
            exchange.setPrice(BigDecimal.valueOf(price));
        } else{
            Double price = exchange.getPointCount() * Double.valueOf(jedisService.get(BicycleConstants.DIVIDE_RATE));
            exchange.setPrice(BigDecimal.valueOf(price));
        }
        if (!iconUrl.isEmpty()) {
            String iconFormat = iconUrl.getOriginalFilename().substring(iconUrl.getOriginalFilename().lastIndexOf("."));
            if (!(Constants.IMAGE_FORMAT.indexOf(iconFormat.toLowerCase()) != -1)) {
                resultData.setResultCode("fail");
                resultData.setResultMessage("请选择jpg，jpeg，png，gif，icon格式的文件");
                return resultData;
            }
        }
        exchange.setCreateDateStr(new Date());
        String iconFile = uploadGiftFile(iconUrl, "exchange", "icon");
        exchange.setIconUrl(iconFile);
        exchange.setCreatorId(U.getUid());
        exchange.setDataFrom(DataFrom.移动麦视后台.getName());
        int ret = exchangeService.insert(exchange);
        if (ret > 0) {
            if (exchange.getType() == 1) {
                jedisService.saveAsMap(BicycleConstants.EXCHANGE + exchange.getId(), exchange);
                jedisService.setValueToSortedSetInShard(BicycleConstants.EXCHANGE_ID, exchange.getWeight(), String.valueOf(exchange.getId()));
            } else {
                jedisService.saveAsMap(BicycleConstants.WITHDRAW_CASH + exchange.getId(), exchange);
                jedisService.setValueToSortedSetInShard(BicycleConstants.WITHDRAW_CASH_ID, exchange.getWeight(), String.valueOf(exchange.getId()));
            }
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败");
        }

        return resultData;
    }
    public static void main (String args[]) {
        double number = 2.56896;
        double decimalPart = number - (int)number;
        System.out.println(decimalPart);
    }
    @RequestMapping("updateWeight")
    @ResponseBody
    @EnableFunction("兑换管理,修改权重")
    public ResultData updateWeight(Long id, Double weight) {
        ResultData resultData = new ResultData();
        Exchange e = exchangeService.selectByPrimaryKey(id);
        if(e == null){
            resultData.setResultCode("fail");
            resultData.setResultMessage("此条数据不存在");
            return resultData;
        }
        Exchange exchange = new Exchange();
        exchange.setWeight(weight);
        exchange.setState(e.getState());
        exchange.setId(id);
        int ret = exchangeService.updateByPrimaryKey(exchange);
        if (ret > 0) {
            Exchange exchangeObject = exchangeService.selectByPrimaryKey(id);
            if (e.getType() == 1) {
                jedisService.saveAsMap(BicycleConstants.EXCHANGE + exchangeObject.getId(), exchangeObject);
                jedisService.setValueToSortedSetInShard(BicycleConstants.EXCHANGE_ID, exchangeObject.getWeight(), String.valueOf(exchangeObject.getId()));
            } else {
                jedisService.saveAsMap(BicycleConstants.WITHDRAW_CASH + exchangeObject.getId(), exchangeObject);
                jedisService.setValueToSortedSetInShard(BicycleConstants.WITHDRAW_CASH_ID, exchangeObject.getWeight(), String.valueOf(exchangeObject.getId()));
            }
            resultData.setResultCode("success");
            resultData.setResultMessage("更改成功");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("更改失败");
        }
        return resultData;
    }

    @RequestMapping("updateState")
    @ResponseBody
    @EnableFunction("兑换管理,设置上下架")
    public ResultData updateState(Long id, String value) {
        ResultData resultData = new ResultData();
        Exchange exchange = new Exchange();
        exchange.setState(Integer.valueOf(value));
        exchange.setId(id);
        int ret = exchangeService.updateByPrimaryKey(exchange);
        if (ret > 0) {
            Exchange exchangeData = exchangeService.selectByPrimaryKey(id);
            if(exchangeData.getType() == 1) {
                jedisService.saveAsMap(BicycleConstants.EXCHANGE + exchangeData.getId(), exchangeData);
                jedisService.setValueToSortedSetInShard(BicycleConstants.EXCHANGE_ID, exchangeData.getWeight(), String.valueOf(exchangeData.getId()));
            }else{
                jedisService.saveAsMap(BicycleConstants.WITHDRAW_CASH + exchangeData.getId(), exchangeData);
                jedisService.setValueToSortedSetInShard(BicycleConstants.WITHDRAW_CASH_ID, exchangeData.getWeight(), String.valueOf(exchangeData.getId()));
            }
            resultData.setResultCode("success");
            resultData.setResultMessage("设置成功");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("设置失败");
        }
        return resultData;
    }

    @RequestMapping("updateExchangeTemplate")
    public ModelAndView updateExchangeTemplate(Long id) {
        ModelAndView mav = new ModelAndView();
        Exchange exchange = exchangeService.selectByPrimaryKey(id);
        mav.addObject("exchange", exchange);
        if(exchange.getType() == 1) {
            mav.setViewName("exchange/update_exchange");
        }else{
            mav.setViewName("exchange/update_withdrawals");
        }
        return mav;
    }

    //更新
    @RequestMapping("updateExchange")
    @ResponseBody
    @EnableFunction("兑换管理,更新兑换信息")
    public ResultData updateExchange(@RequestParam(value = "iconUrl", required = false) MultipartFile iconUrl,
                                 @ModelAttribute("exchange") @Valid Exchange exchange, BindingResult results) {
        ResultData resultData = new ResultData();
        Exchange exchangeObject = exchangeService.selectByPrimaryKey(exchange.getId());
        if (exchangeObject != null) {
            if (!iconUrl.isEmpty()) {
                String format = iconUrl.getOriginalFilename().substring(iconUrl.getOriginalFilename().lastIndexOf("."));
                if (!(Constants.IMAGE_FORMAT.indexOf(format.toLowerCase()) != -1)) {
                    resultData.setResultCode("fail");
                    resultData.setResultMessage("请选择jpg，jpeg，png，gif，icon格式的文件");
                    return resultData;
                }
                String iconFile = uploadGiftFile(iconUrl, "exchange", "icon");
                exchange.setIconUrl(iconFile);
            }
            int ret = exchangeService.updateByPrimaryKey(exchange);
            if (ret > 0) {
                Exchange exchangeData = exchangeService.selectByPrimaryKey(exchange.getId());
                if(exchangeData.getType() == 1) {
                    jedisService.saveAsMap(BicycleConstants.EXCHANGE + exchangeData.getId(), exchangeData);
                    jedisService.setValueToSortedSetInShard(BicycleConstants.EXCHANGE_ID, exchangeData.getWeight(), String.valueOf(exchangeData.getId()));
                }else{
                    jedisService.saveAsMap(BicycleConstants.WITHDRAW_CASH + exchangeData.getId(), exchangeData);
                    jedisService.setValueToSortedSetInShard(BicycleConstants.WITHDRAW_CASH_ID, exchangeData.getWeight(), String.valueOf(exchangeData.getId()));
                }
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
