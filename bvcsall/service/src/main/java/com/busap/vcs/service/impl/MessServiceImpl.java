package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.busap.vcs.data.entity.SystemMess;
import com.busap.vcs.data.mapper.SysmessDAO;
import com.busap.vcs.data.vo.MessVO;
import com.busap.vcs.service.MessService;

@Service("messService")
public class MessServiceImpl extends BaseServiceImpl<SystemMess, Long> implements MessService {
	@Autowired
	private SysmessDAO sysmessDAO;
	@Override
	public List<MessVO> findMess(Integer page, Integer size, String creator,Integer type) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uid", creator);
		params.put("pageStart", (page-1)*size);
		params.put("pageSize", size);
		if (type != null && type.intValue() == 1) { //获得用户赞消息
			return sysmessDAO.findUserPraiseMessByUid(params);
		} else if (type != null && type.intValue() == 2) { //获得用户关注消息
			return sysmessDAO.findUserAttentionMessByUid(params);
		} else if (type != null && type.intValue() == 3) { //获得用户评论消息
			return sysmessDAO.findUserEvaluationMessByUid(params);
		} else if (type != null && type.intValue() == 4) { //获得用户转发消息
			return sysmessDAO.findUserForwardMessByUid(params);
		}
//		return sysmessDAO.findUserMessByUid(params);
		return null;
	}
}