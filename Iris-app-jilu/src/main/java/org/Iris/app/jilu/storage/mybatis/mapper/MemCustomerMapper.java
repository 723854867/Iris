package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemCustomerSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

public interface MemCustomerMapper {

	@InsertProvider(type = MemCustomerSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "customer_id", keyProperty = "customerId")
	void insert(MemCustomer memCustomer);
	
	@SelectProvider(type = MemCustomerSQLBuilder.class, method = "getMemCustomerById")
	MemCustomer getMemCustomerById(long customerById);
	/**
	 * 获取商户的客户列表 
	 * 
	 * @param merchantId
	 * @return
	 */
	List<MemCustomer> getMerchantCustomers(long merchantId); 
}
