package org.Iris.app.jilu.service.action.merchant.serial;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.lang.DateUtils;

public class ORDER_EDIT extends SerialMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		String addGoodsList = session.getKVParamOptional(JiLuParams.ADDGOODSLIST);
		String updateGoodsList = session.getKVParamOptional(JiLuParams.UPDATEGOODSLIST);
		String deleteGoodsList = session.getKVParamOptional(JiLuParams.DELETEGOODSLIST);
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		MerchantOrder order = orderCache.getMerchantOrderById(session.getUnit().getUnit().getMerchantId(), orderId);
		int status = order.getStatus();
		if (status != 0) {
			// 订单不能修改
			return Result.jsonError(JiLuCode.ORDER_IS_LOCK);
		}
		// 对传递的需要变更的产品列表进行验证
		List<MerchantOrderGoods> addList = null;
		List<MerchantOrderGoods> updateList = null;
		List<MerchantOrderGoods> deleteList = null;
		if (addGoodsList != null && !"".equals(addGoodsList)) {
			addList = new ArrayList<MerchantOrderGoods>(
					Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(addGoodsList, MerchantOrderGoods[].class)));
			if (null == addList || addList.size() == 0)
				throw IllegalConstException.errorException(JiLuParams.ADDGOODSLIST);
			for (MerchantOrderGoods ogs : addList) {
				MerchantOrderGoods mGoods = orderCache.getMerchantOrderGoodsById(orderId, ogs.getGoodsId());
				if (mGoods != null)
					return Result.jsonError(JiLuCode.ORDER_GOODS_IS_EXIST.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_EXIST.defaultValue(), ogs.getGoodsId()));
				CfgGoods goods = orderCache.getGoodsById(ogs.getGoodsId());
				if (goods == null)
					return Result.jsonError(JiLuCode.GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.GOODS_NOT_EXIST.defaultValue(), ogs.getGoodsId()));
				ogs.setOrderId(orderId);
				ogs.setGoodsName(goods.getZhName());
				ogs.setStatus(0);
				int time = DateUtils.currentTime();
				ogs.setCreated(time);
				ogs.setUpdated(time);
			}
		}
		List<MerchantOrderGoods> tempUpdateList = new ArrayList<MerchantOrderGoods>();
		if (updateGoodsList != null && !"".equals(updateGoodsList)) {
			updateList = new ArrayList<MerchantOrderGoods>(
					Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(updateGoodsList, MerchantOrderGoods[].class)));
			if (null == updateList || updateList.size() == 0)
				throw IllegalConstException.errorException(JiLuParams.UPDATEGOODSLIST);
			for (MerchantOrderGoods ogs : updateList) {
				MerchantOrderGoods mGood = orderCache.getMerchantOrderGoodsById(orderId, ogs.getGoodsId());
				if (mGood == null)
					return Result.jsonError(JiLuCode.ORDER_GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_NOT_EXIST.defaultValue(), ogs.getGoodsId()));
				if (mGood.getStatus() != 0)
					return Result.jsonError(JiLuCode.ORDER_GOODS_IS_LOCK.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_LOCK.defaultValue(), ogs.getGoodsId()));
				mGood.setCount(ogs.getCount());
				mGood.setUnitPrice(ogs.getUnitPrice());
				mGood.setUpdated(DateUtils.currentTime());
				tempUpdateList.add(mGood);
			}
		}else
			tempUpdateList = null;
		
		if (deleteGoodsList != null && !"".equals(deleteGoodsList)) {
			deleteList = new ArrayList<MerchantOrderGoods>(
					Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(deleteGoodsList, MerchantOrderGoods[].class)));
			if (null == deleteList || deleteList.size() == 0)
				throw IllegalConstException.errorException(JiLuParams.DELETEGOODSLIST);
			for (MerchantOrderGoods ogs : deleteList) {
				MerchantOrderGoods mGood = orderCache.getMerchantOrderGoodsById(orderId, ogs.getGoodsId());
				if (mGood == null)
					return Result.jsonError(JiLuCode.ORDER_GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_NOT_EXIST.defaultValue(), ogs.getGoodsId()));
				if (mGood.getStatus() != 0)
					return Result.jsonError(JiLuCode.ORDER_GOODS_IS_LOCK.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_LOCK.defaultValue(), ogs.getGoodsId()));
				ogs.setId(mGood.getId());
			}
		}

		orderCache.updateOrder(order, addList, tempUpdateList, deleteList);
		return Result.jsonSuccess();
	}
}