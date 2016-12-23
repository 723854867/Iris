package org.Iris.app.jilu.service.realm.relation;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.bean.model.FriendApplyModel;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.storage.redis.cache.RelationCache;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IrisRuntimeException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.redis.operate.lock.DistributeLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RelationService {
	
	private static final Logger logger = LoggerFactory.getLogger(RelationService.class);

	@Resource
	private RelationCache relationCache;
	@Resource
	private DistributeLock distributeLock;
	
	public String apply(Merchant applier, Merchant target, String memo) { 
		String key = _getKey(applier.getMerchantId(), target.getMerchantId());
		String lock = CommonKeyGenerator.relationLockKey(key);
		String lockId = distributeLock.tryLock(lock);
		if (null == lockId) 
			return Result.jsonError(ICode.Code.DATA_CHANGED);
		
		try {
			FriendApplyModel model = new FriendApplyModel(applier, memo);
			return Result.jsonSuccess();
		} finally {
			if (!distributeLock.unLock(lock, lockId))
				logger.warn("Relation lock {} release failure for lockId {}!", lock, lockId);
		}
	}
	
	private String _getKey(long merchantId1, long merchantId2) { 
		if (merchantId1 > merchantId2)
			return merchantId2 + "_" + merchantId1;
		if (merchantId1 < merchantId2)
			return merchantId1 + "_" + merchantId2;
		throw new IrisRuntimeException("Relation key only allow for different merchantId");
	}
}
