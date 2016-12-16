package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.common.bean.form.AssumeRoleForm;
import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.lang.DateUtils;

import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

public class ALIYUN_ASSUME_ROLE extends UnitAction<MerchantSession> {
	
	public static final ALIYUN_ASSUME_ROLE INSTANCE					= new ALIYUN_ASSUME_ROLE();

	@Override
	protected String execute0(MerchantSession session) {
		Merchant merchant = session.getUnit();
		String key = RedisKeyGenerator.getAliyunStsDataKey(merchant.uid());
		AssumeRoleForm form = redisOperate.hgetAll(key, new AssumeRoleForm());
		if (null == form) {
			AssumeRoleResponse response = aliyunService.getStsToken(session.getUnit());
			form = new AssumeRoleForm(response);
			redisOperate.hmset(key, form);
			long expire = DateUtils.getTimeGap(form.getExpiration(), DateUtils.getUTCDate(), DateUtils.ISO8601_UTC, DateUtils.TIMEZONE_UTC);
			// 提前 5 分钟失效
			expire -= 60 * 5;
			redisOperate.expire(key, (int) (expire / 1000));
		}
		return Result.jsonSuccess(form);
	}
}
