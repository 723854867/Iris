package org.Iris.app.jilu.common;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;
import org.Iris.app.jilu.storage.domain.MemCid;
import org.Iris.util.common.JsonAppender;

/**
 * 吉录推送类
 * 
 * @author Administrator
 *
 */
public class JiLuPushUtil {

	/**
	 * 推送转单信息 参数：转单方名字，转单单号，转单时间
	 * 
	 * @param memCid
	 * @param name
	 * @param orderId
	 * @param created
	 */
	public static void OrderTransformPush(MemCid memCid, String name, String orderId, int created) {
		if (memCid != null) {
			String msg = JsonAppender.newAppender().append("type", IgtPushType.ORDER_TRANSFORM.mark())
					.append("name", name).append("orderId", orderId).append("created", created).toString();
			Beans.igtService.pushToSingle(memCid.getClientId(), IgtPushType.ORDER_TRANSFORM.name(), msg);
		}
	}

	/**
	 * 推送转单取消信息 参数：转单单号，转单父订单号
	 * 
	 * @param memCid
	 * @param orderId
	 * @param superOrderId
	 */
	public static void OrderCancelPush(MemCid memCid, String orderId, String superOrderId) {
		if (memCid != null) {
			String msg = JsonAppender.newAppender().append("type", IgtPushType.ORDER_CANCEL.mark())
					.append("orderId", orderId).append("superOrderId", superOrderId).toString();
			Beans.igtService.pushToSingle(memCid.getClientId(), IgtPushType.ORDER_CANCEL.name(), msg);
		}
	}

	/**
	 * 推送转单接收信息 参数：转单单号，转单父订单号
	 * 
	 * @param memCid
	 * @param orderId
	 * @param superOrderId
	 */
	public static void OrderReceivePush(MemCid memCid, String orderId, String superOrderId) {
		if (memCid != null) {
			String msg = JsonAppender.newAppender().append("type", IgtPushType.ORDER_RECEIVE.mark())
					.append("orderId", orderId).append("superOrderId", superOrderId).toString();
			Beans.igtService.pushToSingle(memCid.getClientId(), IgtPushType.ORDER_RECEIVE.name(), msg);
		}
	}

	/**
	 * 推送转单拒绝信息 参数：转单单号，转单父订单号
	 * 
	 * @param memCid
	 * @param orderId
	 * @param superOrderId
	 */
	public static void OrderRefusePush(MemCid memCid, String orderId, String superOrderId) {
		if (memCid != null) {
			String msg = JsonAppender.newAppender().append("type", IgtPushType.ORDER_REFUSE.mark())
					.append("orderId", orderId).append("superOrderId", superOrderId).toString();
			Beans.igtService.pushToSingle(memCid.getClientId(), IgtPushType.ORDER_REFUSE.name(), msg);
		}
	}

	/**
	 * 推送订单状态改变信息 参数 订单号，子订单号，订单状态
	 * 
	 * @param memCid
	 * @param orderId
	 * @param childOrderId
	 * @param status
	 */
	public static void OrderStatusChangePush(MemCid memCid, String orderId, String childOrderId, int status) {
		if (memCid != null) {
			String msg = JsonAppender.newAppender().append("type", IgtPushType.ORDER_STATUS_CHANGE.mark())
					.append("orderId", orderId).append("childOrderId", childOrderId).append("status", status + "")
					.toString();
			Beans.igtService.pushToSingle(memCid.getClientId(), IgtPushType.ORDER_STATUS_CHANGE.name(), msg);
		}
	}
}
