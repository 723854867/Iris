package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.common.bean.model.CustomerListPurchaseFrequencyModel;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemOrderSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemOrderMapper {

	@SelectProvider(type = MemOrderSQLBuilder.class, method="getOrderById")
	MemOrder getOrderById(String orderId);
	
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
}
