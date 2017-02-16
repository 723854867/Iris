package org.Iris.app.jilu.service.realm.relation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.common.bean.model.FriendListModel;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.service.realm.merchant.MerchantService;
import org.Iris.app.jilu.storage.domain.MemAccid;
import org.Iris.app.jilu.storage.domain.PubRelation;
import org.Iris.app.jilu.storage.mybatis.mapper.RelationMapper;
import org.springframework.stereotype.Component;

@Component
public class RelationManager {

	@Resource
	private RelationMapper relationMapper;
	@Resource
	private MerchantService merchantService;
	
	public PubRelation getById(String relationId) { 
		return relationMapper.getById(relationId);
	}
	
	public void insert(PubRelation relation) { 
		relationMapper.insert(relation);
	}
	
	@SuppressWarnings("unchecked")
	public Pager<FriendListModel> friendList(long merchantId, int page, int pageSize) {
		long count = relationMapper.count(merchantId);
		if (0 == count)
			return null;
		long total = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
		if (total < page || page < 1)
			return Pager.EMPTY;
		long start = (page - 1) * pageSize;
		List<PubRelation> list = relationMapper.getPager(merchantId, start, pageSize);
		List<FriendListModel> friendListModels = new ArrayList<FriendListModel>();
		for(PubRelation pubRelation : list){
			FriendListModel friendListModel = new FriendListModel();
			long friendId = pubRelation.getApplier() == merchantId?pubRelation.getAcceptor():pubRelation.getApplier();
			friendListModel.setFriendId(friendId);
			friendListModel.setCreated(pubRelation.getCreated());
			Merchant merchant = merchantService.getMerchantById(friendId);
			MemAccid memAccid = merchant.getMemAccid();
			friendListModel.setFriendName(merchant.getMemMerchant().getName());
			if(memAccid!=null){
				friendListModel.setAccid(memAccid.getAccid());
				friendListModel.setToken(memAccid.getToken());
			}
			friendListModels.add(friendListModel);
		}
		return new Pager<FriendListModel>(total, friendListModels);
	}
	
	public boolean delete(String id) {
		return 1 == relationMapper.delete(id);
	}
}
