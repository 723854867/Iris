package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Set;

import org.Iris.app.jilu.storage.domain.MerchantCustomer;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MerchantCustomerSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import redis.clients.jedis.Tuple;

public interface MerchantCustomerMapper {

	@InsertProvider(type = MerchantCustomerSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "customer_id", keyProperty = "customerId")
	void insert(MerchantCustomer customer);
	
	/**
	 * 获取指定商户的指定客户
	 * 
	 * @param merchantId
	 * @param customerById
	 * @return
	 */
	@SelectProvider(type = MerchantCustomerSQLBuilder.class, method = "getMerchantCustomerById")
	MerchantCustomer getMerchantCustomerById(@Param("merchantId") long merchantId, @Param("customerId") long customerById);
	
	/**
	 * 获取商户的客户列表 
	 * 
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MerchantCustomerSQLBuilder.class, method = "getMerchantCustomers")
	List<MerchantCustomer> getMerchantCustomers(long merchantId); 
	
	/**
	 * 获取指定的客户列表
	 * 
	 * @param ids
	 * @return
	 */
	@SelectProvider(type = MerchantCustomerSQLBuilder.class, method = "getCustomersByIds")
	List<MerchantCustomer> getCustomersByIds(Set<Tuple> set);
}
