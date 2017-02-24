package org.Iris.app.jilu.service.realm.relation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.JiLuPushUtil;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.bean.enums.RelationMod;
import org.Iris.app.jilu.common.bean.form.FriendApplyForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.common.bean.model.FriendApplyModel;
import org.Iris.app.jilu.common.bean.model.FriendListModel;
import org.Iris.app.jilu.service.realm.igt.IgtService;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.PubRelation;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.app.jilu.storage.redis.RedisCache;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.core.exception.IrisRuntimeException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.redis.operate.lock.DistributeLock;
import org.Iris.redis.operate.lua.LuaOperate;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.lang.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Tuple;

@Service
public class RelationService extends RedisCache {
	
	private static final Logger logger = LoggerFactory.getLogger(RelationService.class);
	
	@Resource
	private LuaOperate luaOperate;
	@Resource
	private RedisOperate redisOperate;
	@Resource
	private DistributeLock distributeLock;
	@Resource
	private RelationManager relationManager;
	@Resource
	private IgtService igtService;
	
	public Relation getRelation(String relationId) {
		String key = CommonKeyGenerator.relationMapKey();
		String serial = redisOperate.hget(key, relationId);
		if (null == serial) {
			PubRelation relation = relationManager.getById(relationId);
			if (null == relation)
				return null;
			if (!redisOperate.hsetnx(key, relationId, SerializeUtil.JsonUtil.GSON.toJson(relation)))
				serial = redisOperate.hget(key, relationId);
		}
		return new Relation(SerializeUtil.JsonUtil.GSON.fromJson(serial, PubRelation.class));
	}
	
	/**
	 * 好友申请
	 * 
	 * @param applier
	 * @param acceptor
	 * @param memo
	 * @return
	 */
	public String apply(Merchant applier, Merchant acceptor, String memo) { 
		String id = _relationId(applier.getMemMerchant().getMerchantId(), acceptor.getMemMerchant().getMerchantId());
		String lockKey = CommonKeyGenerator.relationLockKey(id);
		String lockId = distributeLock.tryLock(lockKey);
		if (null == lockId)
			return Result.jsonError(ICode.Code.REQUEST_FREQUENTLY);
		
		try {
			Relation relation = getRelation(id);
			if (null != relation)
				return Result.jsonError(JiLuCode.RELATION_EXIST);
			long flag = redisOperate.zadd(
					MerchantKeyGenerator.friendApplyListKey(acceptor.getMemMerchant().getMerchantId()), 
					System.currentTimeMillis(), String.valueOf(applier.getMemMerchant().getMerchantId()));
			if (1 == flag) 
				redisOperate.hset(MerchantKeyGenerator.friendApplyDataKey(
						acceptor.getMemMerchant().getMerchantId()), 
						String.valueOf(applier.getMemMerchant().getMerchantId()),
						SerializeUtil.JsonUtil.GSON.toJson(new FriendApplyModel(applier, memo)));
		} finally {
			if (!distributeLock.unLock(lockKey, lockId))
				logger.warn("Relation lock {} release failure for lockId {}!", lockKey, lockId);
		}
		//推送好友申请  参数：id,名字
		MemMerchant applierMerchant = applier.getMemMerchant();
		JiLuPushUtil.FriendApplyPush(acceptor.getMemCid(acceptor.getMemMerchant().getMerchantId()),applierMerchant.getMerchantId(),applierMerchant.getName(),memo);
		return Result.jsonSuccess();
	}
	
	/**
	 * 好友申请列表
	 * 
	 * @param merchant
	 * @return
	 */
	public String applyList(Merchant merchant, int page, int pageSize) { 
		String key = MerchantKeyGenerator.friendApplyListKey(merchant.getMemMerchant().getMerchantId());
		long count = redisOperate.zcount(key);
		if (0 == count)
			return Result.jsonSuccess(Pager.EMPTY);
		long total = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
		if (total < page || page < 1)
			return Result.jsonSuccess(Pager.EMPTY);
		int start = (page - 1) * pageSize;
		int end = start + pageSize - 1;
		Set<Tuple> set = redisOperate.zrangeWithScores(key, start, end);
		if (null == set || set.isEmpty())
			return Result.jsonSuccess(Pager.EMPTY);
		String[] members = new String[set.size()];
		double[] scores = new double[set.size()];
		int i = 0;
		Iterator<Tuple> iterator = set.iterator();
		while (iterator.hasNext()) {
			Tuple tuple = iterator.next();
			members[i] = tuple.getElement();
			scores[i] = tuple.getScore();
			i++;
		}
		List<String> list = redisOperate.hmget(MerchantKeyGenerator.friendApplyDataKey(merchant.getMemMerchant().getMerchantId()), members);
		List<FriendApplyForm> data = new ArrayList<FriendApplyForm>();
		for (i = 0; i < members.length; i++) {
			String res = list.get(i);
			if (null == res)
				continue;
			FriendApplyForm form = SerializeUtil.JsonUtil.GSON.fromJson(res, FriendApplyForm.class);
			form.setTime(DateUtils.getUTCDate((long) scores[i]));
			data.add(form);
		}
		return Result.jsonSuccess(new Pager<FriendApplyForm>(count, data));
	}
	
	/**
	 * 处理好友申请
	 * 
	 * @return
	 */
	public String applyReply(Merchant merchant, long targetId, int type) { 
		String res = luaOperate.evalLua(JiLuLuaCommand.REMOVE_FRIEND_APPLY.name(), 2, 
				MerchantKeyGenerator.friendApplyListKey(merchant.getMemMerchant().getMerchantId()),
				MerchantKeyGenerator.friendApplyDataKey(merchant.getMemMerchant().getMerchantId()),
				String.valueOf(targetId));
		if (null == res)
			return Result.jsonError(JiLuCode.FRIEND_APPLY_NOT_EXIST);
	    FriendApplyModel model =  SerializeUtil.JsonUtil.GSON.fromJson(res, FriendApplyModel.class);
		switch (type) {
		case 1:		// 拒绝
			return _rejectApply(merchant, model);
		default:
			return _aggreeApply(merchant, model);
		}
	}
	
	/**
	 * 删除好友
	 * 
	 * @param merchant
	 * @param targetId
	 * @return
	 */
	public String deleteFriend(Merchant merchant, long targetId) {
		String id = _relationId(merchant.getMemMerchant().getMerchantId(), targetId);
		if (!relationManager.delete(id))
			return Result.jsonError(JiLuCode.RELATION_NOT_EXIST);
		redisOperate.hdel(CommonKeyGenerator.relationMapKey(), id);
		return Result.jsonSuccess();
	}
	
	private String _rejectApply(Merchant merchant, FriendApplyModel applyModel) {
		//推送好友申请处理  参数：id,名字，是否接受
		JiLuPushUtil.FriendApplyReplyPush(merchant.getMemCid(applyModel.getApplier()), merchant.getMemMerchant().getMerchantId(), merchant.getMemMerchant().getName(), 1);
		return Result.jsonSuccess();
	}
	
	private String _aggreeApply(Merchant merchant, FriendApplyModel applyModel) {
		String id = _relationId(merchant.getMemMerchant().getMerchantId(), applyModel.getApplier());
		String lockKey = CommonKeyGenerator.relationLockKey(id);
		String lockId = distributeLock.tryLock(lockKey);
		if (null == lockId)
			return Result.jsonError(ICode.Code.REQUEST_FREQUENTLY);
		
		try {
			Relation relation = getRelation(id);
			if (null != relation)
				return Result.jsonError(JiLuCode.RELATION_EXIST);
			PubRelation pubRelation = BeanCreator.newRelation(id, applyModel.getApplier(), merchant.getMemMerchant().getMerchantId(), RelationMod.FRIEDN.mark());
			relationManager.insert(pubRelation);
			redisOperate.hset(CommonKeyGenerator.relationMapKey(), id, SerializeUtil.JsonUtil.GSON.toJson(pubRelation));
			//推送好友申请处理  参数：id,名字，是否接受
			JiLuPushUtil.FriendApplyReplyPush(merchant.getMemCid(applyModel.getApplier()), merchant.getMemMerchant().getMerchantId(), merchant.getMemMerchant().getName(), 0);
			return Result.jsonSuccess();
		} finally {
			if (!distributeLock.unLock(lockKey, lockId))
				logger.warn("Relation lock {} release failure for lockId {}!", lockKey, lockId);
		}
	}
	
	public Pager<FriendListModel> friendList(Merchant merchant, int page, int pageSize) { 
		return relationManager.friendList(merchant.getMemMerchant().getMerchantId(), page, pageSize);
	}
	
	private String _relationId(long merchantId1, long merchantId2) { 
		if (merchantId1 > merchantId2)
			return merchantId2 + "_" + merchantId1;
		if (merchantId1 < merchantId2)
			return merchantId1 + "_" + merchantId2;
		throw new IrisRuntimeException("relation lock a unique merchantId!");
	}
}
