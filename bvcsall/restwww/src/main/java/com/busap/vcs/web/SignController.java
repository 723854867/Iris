package com.busap.vcs.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.busap.vcs.base.IntegralType;
import com.busap.vcs.data.entity.Integral;
import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.service.*;
import com.busap.vcs.util.DateUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.SignUser;
import com.busap.vcs.data.vo.SignVO;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.SignService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.SignUserService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/sign")
public class SignController {

	private Logger logger = LoggerFactory.getLogger(SignController.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

	@Resource(name="signService")
	private SignService signService;

	@Resource(name="signUserService")
	private SignUserService signUserService;

	@Resource(name="ruserService")
	private RuserService ruserService;

	@Resource(name="jedisService")
	private JedisService jedisService;

	@Resource(name = "integralService")
	private IntegralService integralService;

	private final int QIAN_DAO_GET=1;//签到获取
	private final int FEN_XIANG_GET=2;//分享获取
	private final int DATE_SUB_SIGN=4;//添加分值与签到时间差值
	private final int MAX_DATE_SUB_SIGN=30;//添加分值与签到时间差值最大签到天数
	private final int FEN_XIANG_GET_SIGN=100;//添加分值与签到时间差值最大签到天数

	private final int IS_GET_TODAY_SIGN=1;//今天已经签到
	private final int IS_NOT_GET_TODAY_SIGN=0;//尚未签到
	/**
	 * 用户签到或者分享添加积分
	 * @return 积分
	 */
	@RequestMapping("/add")
	@ResponseBody
	public RespBody addSign(String type) {
		if (type == null || !StringUtils.isNumeric(type)) {
			return respBodyWriter.toError(ResponseCode.CODE_450.toString(), ResponseCode.CODE_450.toCode());
		}
		String uid = this.request.getHeader("uid");
		if(StringUtils.isBlank(uid)) {
			return respBodyWriter.toError(ResponseCode.CODE_453.toString(), ResponseCode.CODE_453.toCode());
		} else if (Integer.parseInt(type) == 1) {
			logger.info("addSign : userId=" + uid + " is signing...");
			if (StringUtils.isBlank(uid)){
				return respBodyWriter.toError(ResponseCode.CODE_453.toString(), ResponseCode.CODE_453.toCode());
			}
			Long userId = Long.parseLong(uid);
			Ruser user = ruserService.find(userId);
			if (user == null) {
				return respBodyWriter.toError(ResponseCode.CODE_451.toString(), ResponseCode.CODE_451.toCode());
			}
			// 写入积分系统
			SignVO returnSign = new SignVO();
			Integral integral = integralService.getIntegral(userId, null, TaskTypeSecondEnum.sign);
			if (integral != null && DateUtils.compareDate(integral.getCreateTime(), new Date())) {
				// 签到前先判断签到状态
				returnSign.setIsgetSign(IS_GET_TODAY_SIGN);
			} else {
				returnSign.setIsgetSign(IS_NOT_GET_TODAY_SIGN);
			}
			Map<String, Integer> map = integralService.getIntegralFromSign(userId);

			List<SignUser> signUserList = signUserService.findSignUserByuid(uid);
			if (signUserList != null && signUserList.size() > 0) {
				// 如果 SignUser有记录
				SignUser signUser = signUserList.get(0);
				switch (map.get("continueSign")) {
					case Constants.alreadySign:
						returnSign.setContinueSign(signUser.getContinueSign());
						returnSign.setUserAllSignNum(user.getSignSum());
						break;
					case Constants.firstSign:
						returnSign.setContinueSign(1);
						returnSign.setUserAllSignNum(user.getSignSum() + map.get("signNum"));
						break;
					case Constants.continueSign:
						returnSign.setContinueSign(signUser.getContinueSign());
						returnSign.setUserAllSignNum(user.getSignSum() + map.get("signNum"));
						break;
					default:
						break;
				}
			} else {
				// 如果 SignUser没有记录
				returnSign.setContinueSign(1);
				returnSign.setUserAllSignNum(user.getSignSum());
			}
			returnSign.setSignNum(map.get("signNum"));
			return respBodyWriter.toSuccess(returnSign);
		} else {
			return respBodyWriter.toSuccess();
		}
	}

	/**
	 *用户查看积分
	 * @return
	 */
	@RequestMapping("/loadall")
	@ResponseBody
	public RespBody loadAllSign(Integer page,Integer size) {
		String uid = this.request.getHeader("uid");
		if(page==null||page<=0){
			page=1;
		}
		if(size==null||size<=0){
			size=10;
		}
		if(uid==null||uid.isEmpty()){
			return respBodyWriter.toError(ResponseCode.CODE_453.toString(), ResponseCode.CODE_453.toCode());
		}else{
			List<SignVO> allSign = signService.findUserAllSgin(uid,(page-1)*size,size);
			if(allSign!=null && allSign.size()>0){
				return respBodyWriter.toSuccess(allSign);

			}else{
				return respBodyWriter.toSuccess(allSign);
			}
		}
	}

	/**
	 * 用户查看当前积分状态
	 *
	 * @return
	 */
	@RequestMapping("/loadusersign")
	@ResponseBody
	public RespBody loadUserSignHandler() {
		String uid = this.request.getHeader("uid");
		if (StringUtils.isBlank(uid)){
			return respBodyWriter.toError(ResponseCode.CODE_453.toString(), ResponseCode.CODE_453.toCode());
		}
		logger.info("loadUserSignHandler userID=" + uid);
		Long userId = Long.parseLong(uid);
		SignVO signVO = null;
		// 查询最后一次签到日期
		Integral integral = integralService.getIntegral(userId, null, TaskTypeSecondEnum.sign);
		boolean isTodaySign = integral != null && DateUtils.compareDate(new Date(), integral.getCreateTime());
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);

		SignUser signUser;
		List<SignUser> signUsers=signUserService.findSignUserByuid(uid);
		if (signUsers != null && signUsers.size() > 0) {
            signUser = signUsers.get(0);
        } else {
			signUser = new SignUser();
			signUser.setCreatorId(userId);
			signUser.setCreateDate(new Date());
			signUser.setModifyDate(new Date());
			if (integral != null) {
				signUser.setContinueSign(1);
				signUser.setSumSignNum(5);      // 第一次签到积分默认为5
			} else {
				signUser.setContinueSign(0);
				signUser.setSumSignNum(0);      // 第一次签到积分默认为5
			}
			signUserService.save(signUser);
		}

		logger.info("loadUserSignHandler isTodaySign=" + isTodaySign + "    signUser=" + JSONObject.fromObject(signUser).toString());
		if (isTodaySign && signUser != null) {
            // 如果今天已经签到了
            signVO = new SignVO();
            signVO.setIsgetSign(IS_GET_TODAY_SIGN);
            signVO.setContinueSign(signUser.getContinueSign());
            if (signUser.getContinueSign() >= MAX_DATE_SUB_SIGN) {
                signVO.setNextSignNum(MAX_DATE_SUB_SIGN + DATE_SUB_SIGN);
            } else {
                signVO.setNextSignNum(signUser.getContinueSign() + DATE_SUB_SIGN + 1);
            }
        } else if (!isTodaySign && signUser != null) {
            signVO = new SignVO();
            if (integral != null && DateUtils.compareDate(yesterday.getTime(), integral.getCreateTime())) {
                // 如果今天没签到，昨天签到了
                signVO.setIsgetSign(IS_NOT_GET_TODAY_SIGN);
                signVO.setContinueSign(signUser.getContinueSign());
                if (signVO.getContinueSign() >= MAX_DATE_SUB_SIGN) {
                    signVO.setNextSignNum(MAX_DATE_SUB_SIGN + DATE_SUB_SIGN);
                } else {
                    signVO.setNextSignNum(signUser.getContinueSign() + DATE_SUB_SIGN + 1);
                }
            } else {
                // 如果今天和昨天都没签到
                signVO.setIsgetSign(IS_NOT_GET_TODAY_SIGN);
                signVO.setContinueSign(0);
                signVO.setNextSignNum(1 + DATE_SUB_SIGN);
            }
        }
		if (signVO != null) {
            Ruser user = ruserService.find(userId);
            signVO.setUserAllSignNum(user.getSignSum());
			logger.info("loadUserSignHandler  signVO=" + JSONObject.fromObject(signVO).toString());
            return respBodyWriter.toSuccess(signVO);
        } else {
            return respBodyWriter.toError("sign user is null! Data Error!", -1);
        }

	}

}
