package com.busap.vcs.web;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.data.enums.IntegralConsumeTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.data.entity.Integral;
import com.busap.vcs.data.entity.IntegralConsume;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.IntegralConsumeService;
import com.busap.vcs.service.IntegralService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.utils.DuibaUtil;

@Controller
@RequestMapping("/duiba")
public class DuibaController {

	private Logger logger = LoggerFactory.getLogger(DuibaController.class);

	@Autowired
	protected HttpServletRequest request;
	
	@Autowired
    private IntegralConsumeService integralConsumeService;

	@Resource(name = "ruserService")
	private RuserService ruserService;

	@RequestMapping("/autoLogin")
	public String autoLogin(String redirect) {
		String uid = this.request.getHeader("uid");
		if (uid == null || "".equals(uid)) {
			uid = this.request.getParameter("uid");
		}
		logger.info("duiba,autoLogin,uid={}");

		Ruser ruser = ruserService.find(Long.parseLong(uid));
		int credits = ruser.getSignSum(); // 获得用户当前积分

		Map<String, String> map = new TreeMap<String, String>();
		map.put("uid", uid);
		map.put("credits", String.valueOf(credits));
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		if (redirect != null && !"".equals(redirect)){
			map.put("redirect",redirect);
		}

		String autoLoginUrl = DuibaUtil.createRequestUrl(map);
		
		logger.info("duiba autologin url is :{}",autoLoginUrl);

		return "redirect:" + autoLoginUrl;
	}
	
	@RequestMapping("/deductIntegral")
	public void deductIntegral(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		
		String uid=request.getParameter("uid");//用户id
		String credits=request.getParameter("credits");//本次兑换消耗的积分
		String appKey=request.getParameter("appKey");//接口appKey，应用的唯一标识
		String timestamp=request.getParameter("timestamp");//1970-01-01开始的时间戳，毫秒为单位。
		String description=request.getParameter("description");//本次积分消耗的描述(带中文，请用utf-8进行url解码)
		String orderNum=request.getParameter("orderNum");//兑吧订单号(请记录到数据库中)
		String type=request.getParameter("type");//兑换类型：alipay(支付宝), qb(Q币), coupon(优惠券), object(实物), phonebill(话费), phoneFlow(流量), virtual(虚拟商品), turntable(大转盘), singleLottery(单品抽奖) 所有类型不区分大小写
		String facePrice=request.getParameter("facePrice");//兑换商品的市场价值，单位是分，请自行转换单位
		String actualPrice=request.getParameter("actualPrice");//此次兑换实际扣除开发者账户费用，单位为分
		String ip=request.getParameter("ip");//用户ip，不保证获取到
		String waitAudit=request.getParameter("waitAudit");//是否需要审核(如需在自身系统进行审核处理，请记录下此信息)
		String params=request.getParameter("params");//详情参数，不同的类型，返回不同的内容，中间用英文冒号分隔。(支付宝类型带中文，请用utf-8进行解码) 实物商品：返回收货信息(姓名:手机号:省份:城市:区域:详细地址)、支付宝：返回账号信息(支付宝账号:实名)、话费：返回手机号、QB：返回QQ号
		String sign=request.getParameter("sign");//MD5签名
		
		
		IntegralConsume integralConsume=new IntegralConsume();
		
//		integralConsume.setConsumeId(uid);
		integralConsume.setConsumeIntegral(Integer.valueOf(credits));
		integralConsume.setConsumeType(type);
		integralConsume.setCreateTime(timestamp!=null?new Date(Long.valueOf(timestamp)):new java.util.Date());
		integralConsume.setDescription(description);
		integralConsume.setStatus(0);
		integralConsume.setUpdateTime(timestamp!=null?new Date(Long.valueOf(timestamp)):new java.util.Date());
		integralConsume.setUserId(Long.valueOf(uid));
		integralConsume.setOuderNum(orderNum);
		integralConsume.setType(IntegralConsumeTypeEnum.getValueByTypeString(type));
		
		Ruser ruser = ruserService.find(Long.parseLong(uid));
		
		if(credits!=null&&!credits.equals("")) {
			if(ruser.getSignSum()-Integer.valueOf(credits)<0) {
				response.getWriter().write(
						"{'status': 'fail','errorMessage': '积分不足','credits': '"+ruser.getSignSum()+"'}"
				);
			}else {
				
				try {
					IntegralConsume  integralConsumeNew=integralConsumeService.insertIntegralConsume(integralConsume);
					
					ruser.setSignSum(ruser.getSignSum()-Integer.valueOf(credits));
					
					ruserService.save(ruser);
					
					response.getWriter().write(
						"{'status': 'ok','errorMessage': '','bizId': '"+integralConsumeNew.getId()+"','credits': '"+ruser.getSignSum()+"'}"
					);
				} catch (Exception e) {
					response.getWriter().write(
							"{'status': 'fail','errorMessage': '同步积分失败','credits': '"+ruser.getSignSum()+"'}"
					);
				}
				
				
			}
			
		}
		
		
	}
	
	@RequestMapping("/exchangeResult")
	public void exchangeResult(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String appKey=request.getParameter("appKey");//接口appKey，应用的唯一标识
		
		
		String uid=request.getParameter("uid");//用户id
		String timestamp=request.getParameter("timestamp");//1970-01-01开始的时间戳，毫秒为单位。
		String success=request.getParameter("success");//是否成功
		String errorMessage=request.getParameter("errorMessage");//错误原因
		String orderNum=request.getParameter("orderNum");//兑吧订单号(请记录到数据库中)
		String bizId=request.getParameter("bizId");//integral id
		
		String sign=request.getParameter("sign");//MD5签名
		
		if(success!=null&&success.equals("true")) {
			if(bizId!=null&&!bizId.equals("")) {
				
			}else if(orderNum!=null&&!orderNum.equals("")) {
				
			}
			response.getWriter().write("ok");
		}else if(success!=null&&success.equals("false")){
			IntegralConsume integralConsume=null;
			if(bizId!=null&&!bizId.equals("")) {
				integralConsume=integralConsumeService.getIntegralConsumeById(Long.valueOf(bizId));
			}else if(orderNum!=null&&!orderNum.equals("")) {
				integralConsume=integralConsumeService.getIntegralConsumeByOrderNum(orderNum);
			}
			
			if(integralConsume!=null) {
				
				if(integralConsume.getStatus().intValue()==0) {
					IntegralConsume ic=new IntegralConsume();
					ic.setId(integralConsume.getId());
					ic.setUpdateTime(timestamp!=null?new Date(Long.valueOf(timestamp)):new java.util.Date());
					ic.setStatus(1);
					
					integralConsumeService.updateByPrimaryKeySelective(ic);
					if(uid!=null&&!uid.equals("")) {
						
					}else {
						uid=integralConsume.getUserId().toString();
					}
					Ruser ruser = ruserService.find(Long.parseLong(uid));
					ruser.setSignSum(ruser.getSignSum()+integralConsume.getConsumeIntegral());
					ruserService.save(ruser);
					
					response.getWriter().write("ok");
				}else {
					response.getWriter().write("ok");
				}
				
			}else {
				
			}
			
		}
		
	}

}
