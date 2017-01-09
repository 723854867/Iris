package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.data.vo.InviteFriendVO;
import com.busap.vcs.data.vo.InviteInfoVO;
import com.busap.vcs.oauth.third.weibo.weibo4j.Account;
import com.busap.vcs.oauth.third.weibo.weibo4j.Friendships;
import com.busap.vcs.oauth.third.weibo.weibo4j.model.User;
import com.busap.vcs.oauth.third.weibo.weibo4j.model.UserWapper;
import com.busap.vcs.oauth.third.weibo.weibo4j.model.WeiboException;
import com.busap.vcs.oauth.third.weibo.weibo4j.org.json.JSONException;
import com.busap.vcs.oauth.third.weibo.weibo4j.org.json.JSONObject;
import com.busap.vcs.service.InviteFriendService;
import com.busap.vcs.service.InviteInfoService;
import com.busap.vcs.service.JedisService;
//import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/inviteFriend")
public class InviteFriendController {

	private Logger logger = LoggerFactory.getLogger(InviteFriendController.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Resource(name="inviteService")
	private InviteFriendService inviteService;
	
	@Resource(name="inviteInfoService")
	private InviteInfoService inviteInfoService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	/**
	 * 邀请好友
	 * @param type(1:邀请通讯录好友，2：邀请第三方好友(目前仅有新浪微博))
	 * @param phoneNumbers(通讯录手机号码，多个号码之间用逗号分隔，当type=1时有效)
	 * @param accessToken(第三方accessToken,当type=2时有效)
	 * @return
	 */
	@RequestMapping("/invite")
	@ResponseBody
	public RespBody invite(int type,String phoneNumbers,String accessToken) {
		String uid = (String)this.request.getHeader("uid");
		logger.info("invite,uid={},type={},phoneNumbers={},accessToken={}",uid,type,phoneNumbers,accessToken);
		
		List<InviteFriendVO> list = new ArrayList<InviteFriendVO>();
		if (type==1) {
			list = inviteService.inviteFriendFromContacts(Long.parseLong(uid), phoneNumbers);
		} else {
			//如果有accessToken，将accessToken保存到redis用户信息中
			if (accessToken != null && !"".equals(accessToken)){
				jedisService.setValueToMap(BicycleConstants.USER_INFO+uid, BicycleConstants.SINA_TOKEN, accessToken);
			} else {
				accessToken = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, BicycleConstants.SINA_TOKEN);
			}
			
			if (accessToken == null || "".equals(accessToken))
				return respBodyWriter.toError(ResponseCode.CODE_456.toString(),ResponseCode.CODE_456.toCode());
			List<InviteFriendVO> friends = null;
			try {
				friends = getInviteFriendFromSina(accessToken);
			} catch (WeiboException e) {
				e.printStackTrace();
				return respBodyWriter.toError(e.getError(), e.getErrorCode());
			} catch (JSONException e) {
				e.printStackTrace();
				return respBodyWriter.toError(ResponseCode.CODE_500.toString(),ResponseCode.CODE_500.toCode());
			}
			list = inviteService.inviteFriendFormSian(Long.parseLong(uid), friends);
		}
		
		return respBodyWriter.toSuccess(list);
		
	}
	
	//根据新浪微博accessToken获得用户的新浪微博好友
	private List<InviteFriendVO> getInviteFriendFromSina(String accessToken) throws WeiboException, JSONException {
		List<InviteFriendVO> friends = new ArrayList<InviteFriendVO>();
		Account am = new Account(accessToken);
		
		//根据accessToken获得新浪用户uid
		JSONObject uIdInfo = am.getUid();
		String sinaUid = uIdInfo.getString("uid");
		
		//根据accessToken,uid获得新浪用户好友列表
		Friendships fs = new Friendships(accessToken);
		UserWapper uw = fs.getFriendsByID(sinaUid);
		List<User> list = uw.getUsers();
		
		//将新浪好友列表封装成我拍推荐用户模型
		for (User user:list){
			InviteFriendVO friend = new InviteFriendVO();
			friend.setDataFrom(ThirdUser.sina.getValue());
			friend.setThirdPartNickname(user.getScreenName().trim());
			friend.setThirdPartPic(user.getProfileImageUrl().trim());
			friend.setThirdUsername(user.getId().trim());
			friends.add(friend);
		}
		return friends;
	}
	
	@RequestMapping("/getInviteInfo")
	@ResponseBody
	public RespBody getInviteInfo(){
		List<InviteInfoVO> list = inviteInfoService.findAvaliblelInviteInfo();
		return respBodyWriter.toSuccess(list == null || list.size()==0?new ArrayList<InviteInfoVO>():list);
	}
}
