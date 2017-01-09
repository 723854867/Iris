package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.data.mapper.RuserDAO;
import com.busap.vcs.data.vo.InviteFriendVO;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.InviteFriendService;

@Service("inviteService")
public class InviteFriendServiceImpl implements InviteFriendService {

	@Autowired
	RuserDAO ruserDao;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;
	
	@Override
	public List<InviteFriendVO> inviteFriendFromContacts(Long uid,
			String phoneNumbers) {
		List<InviteFriendVO> result = new ArrayList<InviteFriendVO>();
		List<InviteFriendVO> friends = null;
		if (phoneNumbers != null && !"".equals(phoneNumbers.trim())){
			if (phoneNumbers.endsWith(","))
				phoneNumbers = phoneNumbers.substring(0,phoneNumbers.length()-1);
			String[] array = phoneNumbers.split(",");
			//从通讯录中查找我拍用户
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("uid", uid);
			params.put("phoneNumbers", array);
			friends = ruserDao.getRusersFromContacts(params);
			
			if (friends != null && friends.size() > 0){
				result.addAll(friends);
				//将通讯录中非我拍用户的记录插入到返回列表的末尾
				for (int i=0;i<array.length;i++){
					boolean isWopaiUser = false;
					for (InviteFriendVO friend:friends){
						if (array[i].trim().equals(friend.getUsername().trim())){
							isWopaiUser = true;
							break;
						}
					}
					
					if (!isWopaiUser){
						InviteFriendVO temp = new InviteFriendVO();
						temp.setThirdUsername(array[i]);
						temp.setDataFrom("contacts");
						result.add(temp);
					}
				}
			} else {
				for (int i=0;i<array.length;i++){
					InviteFriendVO temp = new InviteFriendVO();
					temp.setThirdUsername(array[i]);
					temp.setDataFrom("contacts");
					result.add(temp);
				}
			}
			
		}
		
		return result;
	}

	@Override
	public List<InviteFriendVO> inviteFriendFormSian(Long uid,
			List<InviteFriendVO> thirdFriends) {
		List<InviteFriendVO> tempList = new ArrayList<InviteFriendVO>();
		List<InviteFriendVO> friends = null;
		if (thirdFriends != null && thirdFriends.size()>0){
			//获得所有好友的新浪微博唯一标识
			List<String> sinaUids = new ArrayList<String>();
			for (InviteFriendVO friend:thirdFriends){
				sinaUids.add(friend.getThirdUsername());
			}
			
			//从新浪微博好友中查找我拍用户
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("uid", uid);
			params.put("thirdUsernames", sinaUids);
			params.put("thirdPart", ThirdUser.sina.getValue());
			friends = ruserDao.getRusersFromThirdPart(params);
			
			for(InviteFriendVO friend:friends) {
				if (attentionService.isAttention(uid, friend.getId()) == 1) {
					friend.setIsAttention(1 + attentionService.isAttention(friend.getId(), uid));
				}
			}
			
			
			//如果新浪微博好友是我拍用户，则将微博信息加入到对应的bean中，如果不在，将微博信息加入到返回列表的末尾
			if (friends != null && friends.size() > 0){
				for (InviteFriendVO thirdFriend:thirdFriends){
					boolean isWopaiUser = false;
					for (InviteFriendVO friend:friends){
						if (thirdFriend.getThirdUsername().equals(friend.getThirdUsername())){
							isWopaiUser = true;
							friend.setThirdPartNickname(thirdFriend.getThirdPartNickname());
							friend.setThirdPartPic(thirdFriend.getThirdPartPic());
							break;
						}
					}
					
					if (!isWopaiUser){
						thirdFriend.setDataFrom(ThirdUser.sina.getValue());
						tempList.add(thirdFriend);
					}
				}
				friends.addAll(tempList);
			} else {
				return thirdFriends;
			}
		}
		
		return friends;
	}
	
}
