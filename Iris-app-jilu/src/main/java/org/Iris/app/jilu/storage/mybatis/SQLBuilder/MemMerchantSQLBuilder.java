package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.Map;

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
				SET("send_name = #{sendName}");
				SET("send_address = #{sendAddress}");
				SET("send_mobile = #{sendMobile}");
				SET("status_mod = #{statusMod}");
				SET("last_login_time = #{lastLoginTime}");
				SET("last_purchase_time = #{lastPurchaseTime}");
				SET("`money` = #{money}");
				SET("`del_flag` = #{delFlag}");
				WHERE("merchant_id = #{merchantId}");
			}
		}.toString();
	}
	
	/**
	 * 条件搜索 后台使用
	 * @param map
	 * @return
	 */
	public String list(Map<String, Object> map){
		StringBuilder builder = new StringBuilder();
		builder.append("select * from "+Table.MEM_MERCHANT.mark()+" where 1=1 ");
		if(!map.get("name").equals(""))
			builder.append("and name like '%"+map.get("name")+"%' ");
		builder.append("order by updated desc LIMIT "+map.get("start")+","+map.get("pageSize")+"");
		return builder.toString();
	}
	
	public String count(Map<String, Object> map){
		StringBuilder builder = new StringBuilder();
		builder.append("select count(1) from "+Table.MEM_MERCHANT.mark()+" where 1=1 ");
		if(!map.get("name").equals(""))
			builder.append("and name like '%"+map.get("name")+"%' ");
		return builder.toString();
	}
}
