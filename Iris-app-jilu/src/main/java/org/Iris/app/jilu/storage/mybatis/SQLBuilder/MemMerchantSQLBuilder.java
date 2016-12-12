package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemMerchantSQLBuilder {
	
	public String getByMerchantId() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_MERCHANT.mark());
<<<<<<< HEAD
				WHERE("uid=${uid}");
			}
		}.toString();
	}
	
	public String getByAccount(){
		return "select a.* from "+Table.MEM_MERCHANT.mark()+" a,"+Table.MEM_ACCOUNT.mark()+" b "
				+ "where a.merchant_id = b.merchant_id and b.account='${account}' and b.type='${type}'";
	}
	
	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.MEM_MERCHANT.mark());
				VALUES("merchant_id","${merchantId}");
				VALUES("statusMod","${statusMod}");
				VALUES("name","${name}");
				VALUES("mobile","${mobile}");
				VALUES("address","${address}");
				VALUES("avatar","${avatar}");
				VALUES("QRCode","${QRCode}");
				VALUES("lastLoginTime","${lastLoginTime}");
				VALUES("lastPurchaseTime","${lastPurchaseTime}");
				VALUES("created","${created}");
				VALUES("updated","${updated}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_MERCHANT.mark());
				SET("merchant_id=${merchantId}");
				SET("statusMod=${statusMod}");
				SET("name=${name}");
				SET("mobile=${mobile}");
				SET("address=${address}");
				SET("avatar=${avatar}");
				SET("QRCode=${QRCode}");
				SET("lastLoginTime=${lastLoginTime}");
				SET("lastPurchaseTime=${lastPurchaseTime}");
				SET("updated=${updated}");
				WHERE("merchant_id=${merchantId}");
			}
		}.toString();
	}
=======
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}

	public String insert() { 
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_MERCHANT.mark());
				VALUES("name", 				"#{name}");
				VALUES("address", 			"#{address}");
				VALUES("avatar", 			"#{avatar}");
				VALUES("last_login_time", 	"#{lastLoginTime}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
>>>>>>> 9e0c873049238851849e3e9d27e689741acc69ba
}
