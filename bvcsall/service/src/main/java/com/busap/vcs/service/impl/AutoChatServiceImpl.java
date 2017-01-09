package com.busap.vcs.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.AutoChat;
import com.busap.vcs.data.entity.AutoChatType;
import com.busap.vcs.data.mapper.AutoChatDAO;
import com.busap.vcs.data.repository.AutoChatRepository;
import com.busap.vcs.data.repository.AutoChatTypeRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.service.AutoChatService;
import com.busap.vcs.service.JedisService;

@Service("autoChatService")
public class AutoChatServiceImpl extends BaseServiceImpl<AutoChat, Long> implements AutoChatService {

	@Resource(name = "autoChatRepository")
	private AutoChatRepository autoChatRepository;
	
	@Resource(name = "autoChatTypeRepository")
	private AutoChatTypeRepository autoChatTypeRepository;
	
	@Autowired
    JedisService jedisService;
	
	@Autowired
	private AutoChatDAO autoChatDAO;
	
	@Resource(name = "autoChatRepository")
    @Override
    public void setBaseRepository(BaseRepository<AutoChat, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }
	
	@Override
	public void addAutoChat(AutoChat chat) {
		this.save(chat);
		Long id = chat.getId();
		if(id>0){
			if(chat.getUid()!=null){
				jedisService.setValueToSortedSetInShard(BicycleConstants.AUTOCHAT_WORDS+chat.getUid(), id, chat.getWords());
			} else {
				Long typeId = chat.getTypeId();
				jedisService.setValueToSortedSetInShard(BicycleConstants.AUTOCHAT_WORDS+typeId, id, chat.getWords());
			}
		}
	}

	@Override
	public void updateStatus(Long id,Integer status) {
		AutoChat chat = this.find(id);
		if(chat != null && chat.getStatus().intValue() != status.intValue()){
			chat.setStatus(status);
			this.update(chat);
			if(status.intValue() == 0){
				if(chat.getUid()!=null && chat.getUid()>0){
					jedisService.setValueToSortedSetInShard(BicycleConstants.AUTOCHAT_WORDS+chat.getUid(), id, chat.getWords());
				} else {
					jedisService.setValueToSortedSetInShard(BicycleConstants.AUTOCHAT_WORDS+chat.getTypeId(), id, chat.getWords());
				}
			} else if(status.intValue() == 1){
				if(chat.getUid()!=null && chat.getUid()>0){
					jedisService.deleteSortedSetItemFromShard(BicycleConstants.AUTOCHAT_WORDS+chat.getUid(), chat.getWords());
				}else{
					jedisService.deleteSortedSetItemFromShard(BicycleConstants.AUTOCHAT_WORDS+chat.getTypeId(), chat.getWords());
				}
			}
		}
	}

	@Override
	public Page<AutoChat> searchList(Integer pageNo, Integer pageSize,Map<String, Object> params) {
		List<AutoChat> ls = autoChatDAO.searchList(params);
		Integer count = autoChatDAO.searchCount(params);
		
		Page<AutoChat> pinfo = new PageImpl<AutoChat>(ls,new PageRequest(pageNo-1, pageSize, null),count);
		return pinfo;
	}

	@Override
	public void addAutoChatType(String name) {
		AutoChatType chatType = new AutoChatType();
		chatType.setName(name);
		chatType.setStatus(0);
		autoChatTypeRepository.save(chatType);
	}

	@Override
	public void updateAutoChatTypeStatus(Long id, Integer status) {
		AutoChatType chatType = autoChatTypeRepository.findOne(id);
		if(chatType != null && chatType.getStatus().intValue()!=status.intValue()){
			chatType.setStatus(status);
			autoChatTypeRepository.save(chatType);
		}
	}

	@Override
	public List<AutoChatType> findTypes() {
		// TODO Auto-generated method stub
		return autoChatTypeRepository.findAll();
	}

}
