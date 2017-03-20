package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.mybatis.MybatisUtil;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

public class MemOrderGoodsSQLBuilder {

	public String insert() {
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_ORDER_GOODS.mark());
				VALUES("order_id", "#{orderId}");
				VALUES("packet_id", "#{packetId}");
				VALUES("goods_id", "#{goodsId}");
				VALUES("goods_name", "#{goodsName}");
				VALUES("goods_image", "#{goodsImage}");
				VALUES("count", "#{count}");
				VALUES("unit_price", "#{unitPrice}");
				VALUES("status", "#{status}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}

	public String batchInsert(StrictMap<List<MemOrderGoods>> map) {
		List<MemOrderGoods> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("insert into " + Table.MEM_ORDER_GOODS.mark()
				+ " (order_id,goods_id,packet_id,goods_name,count,unit_price,status,created,updated) values ");
		for (MemOrderGoods orderGoods : list) {
			stringBuilder.append("('" + orderGoods.getOrderId() + "','" + orderGoods.getGoodsId() + "','"
					+ orderGoods.getPacketId() + "','" + orderGoods.getGoodsName() + "'" + ",'" + orderGoods.getCount()
					+ "','" + orderGoods.getUnitPrice() + "'" + ",'" + orderGoods.getStatus() + "','"
					+ orderGoods.getCreated() + "','" + orderGoods.getUpdated() + "'),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}

	public String update() {
		return new SQL() {
			{
				UPDATE(Table.MEM_ORDER_GOODS.mark());
				SET("packet_id=#{packetId}");
				SET("goods_id=#{goodsId}");
				SET("goods_name=#{goodsName}");
				SET("goods_image=#{goodsImage}");
				SET("count=#{count}");
				SET("unit_price=#{unitPrice}");
				SET("status=#{status}");
				SET("updated=#{updated}");
				WHERE("id=#{id}");
			}
		}.toString();
	}

	public String batchUpdate(StrictMap<List<MemOrderGoods>> map) {
		List<MemOrderGoods> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		StringBuilder stringBuilder2 = new StringBuilder(256);
		stringBuilder.append("update " + Table.MEM_ORDER_GOODS.mark() + " set count = case id ");
		for (MemOrderGoods orderGoods : list) {
			stringBuilder.append(" when " + orderGoods.getId() + " then " + orderGoods.getCount());
		}
		stringBuilder.append(" end, goods_id = case id");
		for (MemOrderGoods orderGoods : list) {
			stringBuilder.append(" when " + orderGoods.getId() + " then " + orderGoods.getGoodsId());
		}
		stringBuilder.append(" end, change_order_id = case id");
		for (MemOrderGoods orderGoods : list) {
			stringBuilder.append(" when " + orderGoods.getId() + " then " + orderGoods.getChangeOrderId());
		}
		stringBuilder.append(" end, unit_price = case id");
		for (MemOrderGoods orderGoods : list) {
			stringBuilder.append(" when " + orderGoods.getId() + " then " + orderGoods.getUnitPrice());
		}
		stringBuilder.append(" end, packet_id = case id");
		for (MemOrderGoods orderGoods : list) {
			stringBuilder.append(
					" when " + orderGoods.getId() + " then '" + MybatisUtil.checkNull(orderGoods.getPacketId()) + "'");
		}
		stringBuilder.append(" end, status = case id");
		for (MemOrderGoods orderGoods : list) {
			stringBuilder.append(" when " + orderGoods.getId() + " then " + orderGoods.getStatus());
		}
		stringBuilder.append(" end, updated = case id");
		for (MemOrderGoods orderGoods : list) {
			stringBuilder.append(" when " + orderGoods.getId() + " then " + orderGoods.getUpdated());
			stringBuilder2.append(orderGoods.getId() + ",");
		}
		stringBuilder.append(" end");
		stringBuilder.append(" where id in(");
		stringBuilder.append(stringBuilder2.substring(0, stringBuilder2.length() - 1));
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	public String delete() {
		return new SQL() {
			{
				DELETE_FROM(Table.MEM_ORDER_GOODS.mark());
				WHERE("id=#{id}");
			}
		}.toString();
	}

	public String batchDelete(StrictMap<List<MemOrderGoods>> map) {
		List<MemOrderGoods> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("delete from " + Table.MEM_ORDER_GOODS.mark() + " where id in(");
		for (MemOrderGoods goods : list) {
			stringBuilder.append(goods.getId() + ",");
		}

		stringBuilder.setLength(stringBuilder.length() - 1);
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	public String getMerchantOrderGoodsById() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_GOODS.mark());
				WHERE("id=#{id}");
			}
		}.toString();
	}

	public String getMerchantOrderGoodsByOrderId() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_GOODS.mark());
				WHERE("order_id=#{orderId}");
				AND();
				WHERE("goods_id=#{goodsId}");
			}
		}.toString();
	}

	public String getMerchantOrderGoodsByList(StrictMap<List<MemOrderGoods>> map) {
		List<MemOrderGoods> ogs = map.get("collection");
		StringBuilder builder = new StringBuilder("SELECT * FROM " + Table.MEM_ORDER_GOODS.mark() + " WHERE id IN(");
		for (MemOrderGoods goods : ogs)
			builder.append(goods.getId()).append(",");
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		return builder.toString();
	}

	public String getChangeMerchantOrderGoodsByOrderId() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_GOODS.mark());
				WHERE("order_id=#{orderId}");
				AND();
				WHERE("status=2");
			}
		}.toString();
	}

	//// 找到父订单单个产品未转传出去的部分
	public String getSuperNotChangeOrderGoods() {
		return "select * from " + Table.MEM_ORDER_GOODS.mark()
				+ " where order_id=#{orderId} and goods_id = #{goodsId} and status=0";
	}

	// 找到父订单单个产品转出去的部分
	public String getSuperChangeOrderGoods() {
		return "select * from " + Table.MEM_ORDER_GOODS.mark()
				+ " where order_id=#{orderId} and goods_id = #{goodsId} and count=#{count} and status=1 limit 0,1";
	}

	public String getNotFinishMerchantOrderGoodsByOrderId() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_GOODS.mark());
				WHERE("order_id=#{orderId}");
				AND();
				WHERE("status=0");
			}
		}.toString();
	}
	
	public String getNotFinishMerchantOrderGoodsByMerchantId(){
		return "SELECT mog.`goods_id`,SUM(COUNT) count FROM "+Table.MEM_ORDER.mark()+" mo JOIN "+Table.MEM_ORDER_GOODS.mark()+" mog ON mo.order_id = mog.order_id  "
				+ "WHERE mo.merchant_id = #{merchantId} AND mog.`status` =0 and mog.created BETWEEN #{start} AND #{end} GROUP BY mog.`goods_id`";
	}

	public String getDelMerchantOrderGoodsByOrderId() {
		return "select * from " + Table.MEM_ORDER_GOODS.mark()
				+ " where order_id = #{orderId} and status != 7 ";
	}

	public String getPacketMerchantOrderGoodsByPacketId() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_GOODS.mark());
				WHERE("packet_id=#{packetId}");
				AND();
				WHERE("status=3");
			}
		}.toString();
	}
	
	public String getPacketMerchantOrderGoodsCountByPacketId(){
		return new SQL() {
			{
				SELECT("count(1)");
				FROM(Table.MEM_ORDER_GOODS.mark());
				WHERE("packet_id=#{packetId}");
				AND();
				WHERE("status=3");
			}
		}.toString();
	}
	
	public String getMerchantOrderGoodsByPacketId(){
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_GOODS.mark());
				WHERE("packet_id=#{packetId}");
				AND();
				WHERE("goods_id=#{goodsId}");
			}
		}.toString();
	}

}
