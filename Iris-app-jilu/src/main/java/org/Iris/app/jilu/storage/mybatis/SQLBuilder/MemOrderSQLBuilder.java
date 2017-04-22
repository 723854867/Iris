package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

public class MemOrderSQLBuilder {

	public String getOrderById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER.mark());
				WHERE("order_id = #{orderId}");
				AND();
				WHERE("merchant_id = #{merchantId}");
			}
		}.toString();
	}
	
	public String getOrderByOrderId(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER.mark());
				WHERE("order_id = #{orderId}");
			}
		}.toString();
	}
	
	public String getAllOrderByRootOrderId(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER.mark());
				WHERE("root_order_id = #{orderId}");
				AND();
				WHERE("status!=2");
				ORDER_BY("created");
			}
		}.toString();
	}
	
	public String getChildOrderByOrderId(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER.mark());
				WHERE("super_order_id = #{orderId}");
				AND();
				WHERE("status!=2");
				ORDER_BY("created");
			}
		}.toString();
	}
	
	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_ORDER.mark());
				VALUES("order_id", 				"#{orderId}");
				VALUES("root_order_id", 		"#{rootOrderId}");
				VALUES("super_order_id", 	    "#{superOrderId}");
				VALUES("super_merchant_id", 	"#{superMerchantId}");
				VALUES("super_merchant_name", 	"#{superMerchantName}");
				VALUES("merchant_id", 			"#{merchantId}");
				VALUES("merchant_name", 		"#{merchantName}");
				VALUES("merchant_address", 		"#{merchantAddress}");
				VALUES("customer_id", 			"#{customerId}");
				VALUES("customer_name", 		"#{customerName}");
				VALUES("customer_mobile", 		"#{customerMobile}");
				VALUES("customer_address", 		"#{customerAddress}");
				VALUES("memo", 					"#{memo}");
				VALUES("status", 				"#{status}");
				VALUES("created", 				"#{created}");
				VALUES("updated", 				"#{updated}");
				VALUES("deleted", 				"#{deleted}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_ORDER.mark());
				SET("status=#{status}");
				SET("memo=#{memo}");
				SET("sh_memo=#{shMemo}");
				SET("sh_info=#{shInfo}");
				SET("sh_time=#{shTime}");
				SET("updated=#{updated}");
				WHERE("order_id=#{orderId}");
			}
		}.toString();
	}
	public String getMerchantOrderCountGroupByCustomerBetweenTime() {
		return new SQL() {
			{
				SELECT("customer_id, COUNT(*) count");
				FROM(Table.MEM_ORDER.mark());
				WHERE("merchant_id = #{merchantId}");
				AND();
				WHERE("root_order_id = 0");
				GROUP_BY("customer_id");
			}
		}.toString();
	}
	
	public String getChangeMerchantOrderList(){
		return new SQL() {
			{
				SELECT("order_id,super_merchant_id,super_merchant_name,created");
				FROM(Table.MEM_ORDER.mark());
				WHERE("merchant_id=#{merchantId}");
				AND();
				WHERE("status=2");
			}
		}.toString();
	}
	
	public String getTransferMerchantOrderList(){
		return new SQL() {
			{
				SELECT("order_id,merchant_id,merchant_name,created");
				FROM(Table.MEM_ORDER.mark());
				WHERE("super_merchant_id=#{superMerchantId}");
				AND();
				WHERE("status=2");
			}
		}.toString();
	}
	
	public String getOrderListByMerchantId(){
		
		return "select * from "+Table.MEM_ORDER.mark()+" where merchant_id=#{merchantId} order by created LIMIT #{start},#{pageSize}";
	}
	
	/**
	 * 获取待处理订单（status = 0 或 3）
	 * @return
	 */
	public String getWaitOrderListByMerchantId(){
		
		return "select * from "+Table.MEM_ORDER.mark()+" where merchant_id=#{merchantId} and status < 4 order by created LIMIT #{start},#{pageSize}";
	}
	
	public String getOrderCountByMerchantId(){
		return new SQL(){
			{
				SELECT("count(1)");
				FROM(Table.MEM_ORDER.mark());
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String getWaitOrderCountByMerchantId(){
		return "select count(1) from "+Table.MEM_ORDER.mark()+" where merchant_id=#{merchantId} and status < 4";
	}
	
	public String getTransferMerchantOrderListByOrderId(){
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ORDER.mark());
				WHERE("super_order_id = #{orderId}");
				AND();
				WHERE("status=2");
				ORDER_BY("created");
			}	
		}.toString();
	}
	
	public String delete(){
		return new SQL(){
			{
				DELETE_FROM(Table.MEM_ORDER.mark());
				WHERE("merchant_id=#{merchantId}");
				AND();
				WHERE("order_id=#{orderId}");
			}
		}.toString();
	}
	
	public String batchUpdate(StrictMap<List<MemOrder>> map) {
		List<MemOrder> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		StringBuilder stringBuilder2 = new StringBuilder(256);
		stringBuilder.append("update " + Table.MEM_ORDER.mark() + " set memo = case order_id ");
		for (MemOrder order : list) {
			stringBuilder.append(" when " + order.getOrderId() + " then '" + order.getMemo()+"'");
		}
		stringBuilder.append(" end, updated = case order_id");
		for (MemOrder order : list) {
			stringBuilder.append(" when " + order.getOrderId() + " then " + order.getUpdated());
			stringBuilder2.append(order.getOrderId() + ",");
		}
		stringBuilder.append(" end");
		stringBuilder.append(" where order_id in(");
		stringBuilder.append(stringBuilder2.substring(0, stringBuilder2.length() - 1));
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
}
