package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.OrderPay;
import com.busap.vcs.data.model.UserChargeDetail;
import com.busap.vcs.data.vo.ConsumePayVO;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.ExcelUtils;
import com.busap.vcs.util.page.EnablePaging;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 充值明细
 * Created by Knight on 16/1/4.
 */
@Controller
@RequestMapping("payConsume")
public class PayConsumeController {

    @Value("${payment_app_id}")
    private String APP_ID;

    @Value("${weixin_app_id}")
    private String WX_APP_ID;

    @Value("${weixin_secret}")
    private String WX_SECRET;

    @Value("${files.localpath}")
    private String basePath;

    @Autowired
    private PingPayService pingPayService;

    @Autowired
    private RuserService ruserService;

    @Autowired
    JedisService jedisService;

    @Resource(name="giftService")
    private GiftService giftService;

    private Logger logger = LoggerFactory.getLogger(PayConsumeController.class);

    @RequestMapping("index")
    public String findListHandler(HttpServletRequest req) {
        List<String> platformList = ruserService.selectAllRegPlatform();
        req.setAttribute("platformList", platformList);
        StringBuffer jpqlApp = new StringBuffer();
        List<ParameterBean> paramListApp=new ArrayList<ParameterBean>();
        jpqlApp.append(" select  distinct cr.appVersion FROM AppVersion cr  ");
        List<OrderByBean> orderByListApp = new ArrayList<OrderByBean>();
        OrderByBean orderByObjectApp = new OrderByBean("createDate",1,"cr");
        orderByListApp.add(orderByObjectApp);
        try {
            List appVersionList = ruserService.getObjectByJpql(jpqlApp, 0, 1000, "cr", paramListApp, null, orderByListApp);
            req.setAttribute("appVersionList", appVersionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "payConsume/recordList";
    }

    @RequestMapping("recordList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> recordList(Integer paramType, String param, String start, String end, String chargeType, Integer page,
                                          Integer rows, String channel, String isGive, String appVersion, String platform, String source) {
        List<ConsumePayVO> list;
        
        List<OrderPay> listSum;
        long startTime = StringUtils.isBlank(start) ? 0l : transDate(start);
        long endTime = StringUtils.isBlank(end) ? 0l : transDate(end);

        int total;
        if (paramType == null && param == null && start == null && end == null && chargeType == null&&(channel==null||channel.equals(""))&&(isGive==null||isGive.equals("")) ) {
            // 第一次进主页
        	if(page==null) {
        		page=1;
        	}
        	if(rows==null) {
        		rows=20;
        	}
            list = pingPayService.getByCondition(0, "", 0l, 0l, 0,  page, rows,null,null,null, null, null);
            total = pingPayService.countByCondition(0, "", 0l, 0l, 0,null,null);
            listSum = pingPayService.getSumByCondition(0, "", 0l, 0l, 0,  1, 20,null,null,null, null, null);
        } else {

            int chargeTypeInt = StringUtils.isBlank(chargeType) ? 0 : Integer.parseInt(chargeType);
            list = pingPayService.getByCondition(paramType, param, startTime, endTime, chargeTypeInt, page, rows, channel, isGive, appVersion, platform, source);
            total = pingPayService.countByCondition(paramType, param, startTime, endTime, chargeTypeInt, channel, isGive);
            listSum = pingPayService.getSumByCondition(paramType, param, startTime, endTime, chargeTypeInt, 1, 20, channel, isGive, appVersion, platform, source);
        }
        for (ConsumePayVO consumePayVO : list) {
        	consumePayVO.setExtraMoney(consumePayVO.getExtra() / 10d);
		}
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);

        if(listSum!=null&&listSum.size()>0) {
        	OrderPay op=(OrderPay) listSum.get(0);
        	if(op!=null) {
        		if(op.getAmount()!=null) {
            		map.put("amountSum", op.getAmount()/100d);
            	}else {
            		map.put("amountSum", 0);
            	}
                if (op.getReceived() != null) {
                    map.put("receivedSum", op.getReceived());
                } else {
                    map.put("receivedSum", 0);
                }
            	if(op.getExtra()!=null) {
            		map.put("extraSum", op.getExtra());
            		map.put("extraMSum", op.getExtra()/10d);
            	}else {
            		map.put("extraSum", 0);
            		map.put("extraMSum", 0);
            	}
        	}
        }
        Integer totalUsers = pingPayService.findTotalChargeUsers(startTime, endTime);
        if (totalUsers != null) {
            map.put("usersSum", totalUsers);
        } else {
            map.put("usersSum", 0);
        }

        return map;
    }

    @RequestMapping("exportToExcel")
    public void exportActivityVideosToExcel(@RequestParam(value = "paramType", required = false) Integer paramType,
                                            @RequestParam(value = "param",required = false) String param,
                                            @RequestParam(value = "start",required = false) String start,
                                            @RequestParam(value = "end",required = false) String end,
                                            @RequestParam(value = "chargeType",required = false) String chargeType,
                                            @RequestParam(value = "channel",required = false) String channel,
                                            @RequestParam(value = "isGive",required = false) String isGive,
                                            @RequestParam(value = "appVersion",required = false) String appVersion,
                                            @RequestParam(value = "platform",required = false) String platform,
                                            @RequestParam(value = "source",required = false) String source,
                                            HttpServletResponse response)throws IOException {
        long startTime = StringUtils.isBlank(start) ? 0l : transDate(start + " 00:00:00");
        long endTime = StringUtils.isBlank(end) ? 0l : transDate(end + " 23:59:59");
        int chargeTypeInt = StringUtils.isBlank(chargeType) ? 0 : Integer.parseInt(chargeType);

        List<UserChargeDetail> list = pingPayService.exportByCondition(paramType, param, startTime, endTime, chargeTypeInt, channel, isGive, appVersion, platform, source);

        ExcelUtils<UserChargeDetail> excelUtils = new ExcelUtils<UserChargeDetail>();
        String[] headers = {"订单时间", "订单流水号", "用户ID", "用户昵称", "用户手机号", "支付渠道", "金额（元）","赠送金币","赠送现金（元）","支付状态"};
        OutputStream outputStream = response.getOutputStream();// 取得输出流
        response.reset();
        // 设定输出文件头
        response.setHeader("Content-disposition", "attachment; filename=UserChargeDetails.xls");
        // 定义输出类型
        response.setContentType("application/msexcel");

        excelUtils.exportExcel("用户充值明细", headers, list, outputStream);
    }

    private long transDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateString);
            return date.getTime() / 1000l;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0l;
        }
    }

}
