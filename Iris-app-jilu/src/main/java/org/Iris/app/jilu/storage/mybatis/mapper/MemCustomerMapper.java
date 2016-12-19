package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemCustomerSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

public interface MemCustomerMapper {

	@InsertProvider(type = MemCustomerSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "customer_id", keyProperty = "customerId")
	void insert(MemCustomer memCustomer);
}