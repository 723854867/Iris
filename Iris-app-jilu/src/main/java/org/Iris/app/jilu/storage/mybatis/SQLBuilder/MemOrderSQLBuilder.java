package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemOrderSQLBuilder {

	public String getMerchantOrderCountGroupByCustomerBetweenTime() {
		return new SQL() {
			{
				SELECT("customer_id, COUNT(*) count");
				FROM(Table.MEM_ORDER.mark());
				WHERE("merchant_id=#{merchantId}");
				GROUP_BY("customer_id");
			}
		}.toString();
	}
}
