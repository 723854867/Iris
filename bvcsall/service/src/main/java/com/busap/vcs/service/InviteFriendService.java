package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.vo.InviteFriendVO;

public interface InviteFriendService {
	
	public List<InviteFriendVO> inviteFriendFromContacts(Long uid,String phoneNumbers);
	
	public List<InviteFriendVO> inviteFriendFormSian(Long uid,List<InviteFriendVO> users);

}
