package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.Map;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.activemq.filter.function.splitFunction;
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
				VALUES("total_jb", "#{totalJb}");
				VALUES("status", "#{status}");
				VALUES("cz_time", "#{czTime}");
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
				SET("cz_time=#{czTime}");
				WHERE("out_trade_no=#{outTradeNo}");
			}
		}.toString();
	}
	
	public String findByOutRradeNo(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_PAY_INFO.mark());
				WHERE("out_trade_no=#{outTradeNo}");
			}
		}.toString();
	}
	
	public String getJbCzLogCount(Map<String, Object> map){
		StringBuilder builder = new StringBuilder();
		builder.append("select count(1) from "+Table.MEM_PAY_INFO.mark());
		builder.append(" where status=1 ");
		if((int)map.get("startTime")!=0)
			builder.append(" and cz_time>="+map.get("beginTime"));
		if((int)map.get("endTime")!=0)
			builder.append(" and cz_time<="+map.get("endTime"));
		return builder.toString();
	}
	
	public String getJbCzLog(Map<String, Object> map){
		StringBuilder builder = new StringBuilder();
		builder.append("select * from "+Table.MEM_PAY_INFO.mark());
		builder.append(" where status=1 ");
		if((int)map.get("startTime")!=0)
			builder.append(" and cz_time>="+map.get("beginTime"));
		if((int)map.get("endTime")!=0)
			builder.append(" and cz_time<="+map.get("endTime"));
		builder.append(" limit "+map.get("start")+", "+map.get("pageSize"));
		return builder.toString();
	}
}
