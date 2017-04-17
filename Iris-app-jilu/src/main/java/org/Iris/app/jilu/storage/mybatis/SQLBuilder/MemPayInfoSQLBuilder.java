package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemPayInfoSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.MEM_PAY_INFO.mark());
				VALUES("merchant_id", "#{merchantId}");
				VALUES("out_trade_no", "#{outTradeNo}");
				VALUES("pay_type", "#{payType}");
				VALUES("`subject`", "#{subject}");
				VALUES("body", "#{body}");
				VALUES("total_amount", "#{totalAmount}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_PAY_INFO.mark());
				SET("status=#{status}");
				SET("updated=#{updated}");
				WHERE("out_trade_no=#{outTradeNo}");
			}
		}.toString();
	}
	
	public String findByOutRradeNo(){
		return new SQL(){
			{
				SELECT(Table.MEM_PAY_INFO.mark());
				WHERE("out_trade_no=#{outTradeNo}");
			}
		}.toString();
	}
}
