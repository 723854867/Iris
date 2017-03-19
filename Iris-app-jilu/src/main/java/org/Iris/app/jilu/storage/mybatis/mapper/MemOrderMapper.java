package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.common.bean.model.CustomerListPurchaseFrequencyModel;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemOrderGoodsSQLBuilder;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemOrderSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemOrderMapper {

	@SelectProvider(type = MemOrderSQLBuilder.class, method="getOrderById")
	MemOrder getOrderById(@Param("merchantId") long merchantId, @Param("orderId") String orderId);
	
	@SelectProvider(type = MemOrderSQLBuilder.class, method="getOrderByOrderId")
	MemOrder getOrderByOrderId(@Param("orderId") String orderId);
	
	/**
	 * 根据订单号获取所有关联的订单
	 * @param orderId
	 * @return
	 */
	@SelectProvider(type = MemOrderSQLBuilder.class, method="getAllOrderByRootOrderId")
	List<MemOrder> getAllOrderByRootOrderId(@Param("orderId") String orderId);
	
	/**
	 * 根据订单号获取所有子订单
	 * @param orderId
	 * @param created
	 * @return
	 */
	@SelectProvider(type = MemOrderSQLBuilder.class, method="getChildOrderByOrderId")
	List<MemOrder> getChildOrderByOrderId(@Param("orderId") String orderId);
	
	@InsertProvider(type = MemOrderSQLBuilder.class, method="insert")
	void insert(MemOrder order);
	
	@UpdateProvider(type = MemOrderSQLBuilder.class, method="update")
	void update(MemOrder order);
	
	/**
	 * 获取在 [start, end] 之间的商户的客户订单统计数
	 * 
	 * @param merchantId
	 * @param start
	 * @param end
	 * @return
	 */
	@SelectProvider(type = MemOrderSQLBuilder.class, method="getMerchantOrderCountGroupByCustomerBetweenTime")
	List<CustomerListPurchaseFrequencyModel> getMerchantOrderCountGroupByCustomerBetweenTime(@Param("merchantId") long merchantId, @Param("start") int start, @Param("end") int end);

	/**
	 * 通过被转单商户id获取转单申请列表
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MemOrderSQLBuilder.class, method="getChangeMerchantOrderList")
	List<MemOrder> getChangeMerchantOrderList(long merchantId);
	
	/**
	 * 通过商户id获取正在转单中的订单列表
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MemOrderSQLBuilder.class, method="getTransferMerchantOrderList")
	List<MemOrder> getTransferMerchantOrderList(long merchantId);
	
	/**
	 * 查找当前商户的所有订单（创建者为该商户）
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MemOrderSQLBuilder.class, method="getOrderListByMerchantId")
	List<MemOrder> getOrderListByMerchantId(@Param("merchantId") long merchantId, @Param("start") int start, @Param("pageSize") int pageSize);
	
	/**
	 * 查找当前商户的所有订单总数
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MemOrderSQLBuilder.class, method="getOrderCountByMerchantId")
	long getOrderCountByMerchantId(@Param("merchantId") long merchantId);
	
	/**
	 * 通过订单号获取正在转单中的订单列表
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MemOrderSQLBuilder.class, method="getTransferMerchantOrderListByOrderId")
	List<MemOrder> getTransferMerchantOrderListByOrderId(String orderId);
	
	@DeleteProvider(type = MemOrderSQLBuilder.class, method="delete")
	void delete(@Param("merchantId") long merchantId,@Param("orderId") String orderId);
	
	/**
	 * 批量更新订单备注
	 * @param list
	 */
	@UpdateProvider(type = MemOrderSQLBuilder.class, method = "batchUpdate")
	void batchUpdate(List<MemOrder> list);
}
