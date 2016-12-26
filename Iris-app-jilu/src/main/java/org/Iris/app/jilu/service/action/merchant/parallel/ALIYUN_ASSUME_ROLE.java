package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.common.bean.form.AssumeRoleForm;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.unit.merchant.MerchantOperator;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.lang.DateUtils;

import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

public class ALIYUN_ASSUME_ROLE extends ParallelMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		MerchantOperator merchant = session.getUnit();
		String key = MerchantKeyGenerator.aliyunStsTokenDataKey(merchant.uid());
		AssumeRoleForm form = redisOperate.hgetAll(key, new AssumeRoleForm());
		if (null == form) {
			AssumeRoleResponse response = aliyunService.getStsToken(merchant.getUnit().getMerchantId());
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
