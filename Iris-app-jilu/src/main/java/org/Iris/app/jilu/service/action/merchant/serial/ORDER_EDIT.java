package org.Iris.app.jilu.service.action.merchant.serial;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
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
		Merchant merchant = session.getMerchant();
		MemOrder order = merchant.getMerchantOrderById(merchant.getMemMerchant().getMerchantId(), orderId);
		if(order == null)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		if(order.getSuperMerchantId()!=0)
			return Result.jsonError(JiLuCode.TRANSFORM_ORDER_CONNOT_UPDATED);
		if (order.getStatus() != 0) 
			return Result.jsonError(JiLuCode.ORDER_IS_LOCK);
		// 对传递的需要变更的产品列表进行验证
		List<MemOrderGoods> addList = null;
		List<MemOrderGoods> updateList = null;
		List<MemOrderGoods> deleteList = null;
		if (addGoodsList != null && !"".equals(addGoodsList)) {
			addList = new ArrayList<MemOrderGoods>(
					Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(addGoodsList, MemOrderGoods[].class)));
			if (null == addList || addList.size() == 0)
				throw IllegalConstException.errorException(JiLuParams.ADDGOODSLIST);
			for (MemOrderGoods ogs : addList) {
				MemOrderGoods mGoods = merchant.getMerchantOrderGoodsById(orderId, ogs.getGoodsId());
				if (mGoods != null)
					return Result.jsonError(JiLuCode.ORDER_GOODS_IS_EXIST.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_EXIST.defaultValue(), ogs.getGoodsId()));
				CfgGoods goods = merchant.getGoodsById(ogs.getGoodsId());
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
		List<MemOrderGoods> tempUpdateList = new ArrayList<MemOrderGoods>();
		if (updateGoodsList != null && !"".equals(updateGoodsList)) {
			updateList = new ArrayList<MemOrderGoods>(
					Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(updateGoodsList, MemOrderGoods[].class)));
			if (null == updateList || updateList.size() == 0)
				throw IllegalConstException.errorException(JiLuParams.UPDATEGOODSLIST);
			for (MemOrderGoods ogs : updateList) {
				MemOrderGoods mGood = merchant.getMerchantOrderGoodsById(orderId, ogs.getGoodsId());
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
			deleteList = new ArrayList<MemOrderGoods>(
					Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(deleteGoodsList, MemOrderGoods[].class)));
			if (null == deleteList || deleteList.size() == 0)
				throw IllegalConstException.errorException(JiLuParams.DELETEGOODSLIST);
			for (MemOrderGoods ogs : deleteList) {
				MemOrderGoods mGood = merchant.getMerchantOrderGoodsById(orderId, ogs.getGoodsId());
				if (mGood == null)
					return Result.jsonError(JiLuCode.ORDER_GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_NOT_EXIST.defaultValue(), ogs.getGoodsId()));
				if (mGood.getStatus() != 0)
					return Result.jsonError(JiLuCode.ORDER_GOODS_IS_LOCK.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_LOCK.defaultValue(), ogs.getGoodsId()));
				ogs.setId(mGood.getId());
				ogs.setOrderId(orderId);
			}
		}

		merchantService.updateOrder(order, addList, tempUpdateList, deleteList,merchant);
		return Result.jsonSuccess();
	}
}