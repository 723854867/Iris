package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Set;

import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MerchantCustomerSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import redis.clients.jedis.Tuple;

public interface MerchantCustomerMapper {

	@InsertProvider(type = MerchantCustomerSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "customer_id", keyProperty = "customerId")
	void insert(MemCustomer customer);
	
	@UpdateProvider(type = MerchantCustomerSQLBuilder.class, method = "update")
	void update(MemCustomer customer);
	
	/**
	 * 获取指定商户的指定客户
	 * 
	 * @param merchantId
	 * @param customerById
	 * @return
	 */
	@SelectProvider(type = MerchantCustomerSQLBuilder.class, method = "getMerchantCustomerById")
	MemCustomer getMerchantCustomerById(@Param("merchantId") long merchantId, @Param("customerId") long customerById);
	
	/**
	 * 获取商户的客户列表 
	 * 
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MerchantCustomerSQLBuilder.class, method = "getMerchantCustomers")
	List<MemCustomer> getMerchantCustomers(long merchantId); 
	
	/**
	 * 获取指定的客户列表
	 * 
	 * @param ids
	 * @return
	 */
	@SelectProvider(type = MerchantCustomerSQLBuilder.class, method = "getCustomersByIds")
	List<MemCustomer> getCustomersByIds(Set<Tuple> set);
}
