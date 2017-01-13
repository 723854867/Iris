package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Set;

import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemCustomerSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import redis.clients.jedis.Tuple;

public interface MemCustomerMapper {

	@InsertProvider(type = MemCustomerSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "customer_id", keyProperty = "customerId")
	void insert(MemCustomer customer);
	
	@UpdateProvider(type = MemCustomerSQLBuilder.class, method = "update")
	void update(MemCustomer customer);
	
	/**
	 * 获取指定商户的指定客户
	 * 
	 * @param merchantId
	 * @param customerById
	 * @return
	 */
	@SelectProvider(type = MemCustomerSQLBuilder.class, method = "getMerchantCustomerById")
	MemCustomer getMerchantCustomerById(@Param("merchantId") long merchantId, @Param("customerId") long customerId);
	
	/**
	 * 获取商户的客户列表 
	 * 
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MemCustomerSQLBuilder.class, method = "getMerchantCustomers")
	List<MemCustomer> getMerchantCustomers(long merchantId); 
	
	/**
	 * 获取指定的客户列表
	 * 
	 * @param ids
	 * @return
	 */
	@SelectProvider(type = MemCustomerSQLBuilder.class, method = "getCustomersByIds")
	List<MemCustomer> getCustomersByIds(Set<Tuple> set);
	
	@DeleteProvider(type = MemCustomerSQLBuilder.class, method = "delete")
	void delete(MemCustomer customer);
	
	/**
	 * 获取商户的客户列表 
	 * 
	 * @param merchantId
	 * @return
	 */
	@SelectProvider(type = MemCustomerSQLBuilder.class, method = "getMerchantCustomersByNameOrPhone")
	List<MemCustomer> getMerchantCustomersByNameOrPhone(@Param("merchantId") long merchantId,@Param("value") String value,@Param("start") int start,@Param("pageSize") int pageSize); 

	@SelectProvider(type = MemCustomerSQLBuilder.class, method = "getCountByNameOrPhone")
	long getCountByNameOrPhone(@Param("merchantId") long merchantId,@Param("value") String value); 

}
