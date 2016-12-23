package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.common.bean.model.CustomerListPurchaseFrequencyModel;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MerchantOrderSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MerchantOrderMapper {

	@SelectProvider(type = MerchantOrderSQLBuilder.class, method="getOrderById")
	MerchantOrder getOrderById(@Param("merchantId") long merchantId, @Param("orderId") String orderId);
	
	@InsertProvider(type = MerchantOrderSQLBuilder.class, method="insert")
	void insert(MerchantOrder order);
	
	@UpdateProvider(type = MerchantOrderSQLBuilder.class, method="update")
	void update(MerchantOrder order);
	
	/**
	 * 获取在 [start, end] 之间的商户的客户订单统计数
	 * 
	 * @param merchantId
	 * @param start
	 * @param end
	 * @return
	 */
	@SelectProvider(type = MerchantOrderSQLBuilder.class, method="getMerchantOrderCountGroupByCustomerBetweenTime")
	List<CustomerListPurchaseFrequencyModel> getMerchantOrderCountGroupByCustomerBetweenTime(@Param("merchantId") long merchantId, @Param("start") int start, @Param("end") int end);

	/**
	 * 通过被转单商户id获取转单申请列表
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MerchantOrderSQLBuilder.class, method="getChangeMerchantOrderList")
	List<MerchantOrder> getChangeMerchantOrderList(long merchantId);
}
