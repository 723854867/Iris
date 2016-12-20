package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.common.bean.model.CustomerListModel;
import org.apache.ibatis.annotations.Param;

public interface MemOrderMapper {

	/**
	 * 获取在 [start, end] 之间的商户的客户订单统计数
	 * 
	 * @param merchantId
	 * @param start
	 * @param end
	 * @return
	 */
	List<CustomerListModel.CustomerListPurchaseFrequencyModel> getMerchantOrderCountGroupByCustomerBetweenTime(@Param("merchantId") long merchantId, @Param("start") int start, @Param("end") int end);
}
