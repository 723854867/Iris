package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemMerchantSQLBuilder {
	
	public String getByMerchantId() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_MERCHANT.mark());
				WHERE("merchant_id = #{merchantId}");
			}
		}.toString();
	}

	public String insert() { 
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_MERCHANT.mark());
				VALUES("last_login_time", 	"#{lastLoginTime}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_MERCHANT.mark());
				SET("name = #{name}");
				SET("QR_code = #{QRCode}");
				SET("updated = #{updated}");
				SET("address = #{address}");
				SET("status_mod = #{statusMod}");
				SET("last_login_time = #{lastLoginTime}");
				SET("last_purchase_time = #{lastPurchaseTime}");
				SET("`money` = #{money}");
				WHERE("merchant_id = #{merchantId}");
			}
		}.toString();
	}
}
