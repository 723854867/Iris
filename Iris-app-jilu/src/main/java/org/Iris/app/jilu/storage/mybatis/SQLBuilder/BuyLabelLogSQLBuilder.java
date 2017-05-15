package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.Map;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class BuyLabelLogSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.LOG_BUY_LABEL.mark());
				VALUES("merchant_id", "#{merchantId}");
				VALUES("count", "#{count}");
				VALUES("price", "#{price}");
				VALUES("created", "#{created}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.LOG_BUY_LABEL.mark());
				SET("status=#{status}");
				SET("send_time=#{sendTime}");
				WHERE("id=#{id}");
			}
		}.toString();
	}
	
	public String findById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.LOG_BUY_LABEL.mark());
				WHERE("id=#{id}");
			}
		}.toString();
	}
	
	public String list(Map<String, Object> map){
		return "select * from "+Table.LOG_BUY_LABEL.mark()+" where status = "+map.get("status")+" order by created desc limit "+map.get("start")+","+map.get("pageSize")+"";
	}
	
	public String count(Map<String, Object> map){
		return "select count(1) from "+Table.LOG_BUY_LABEL.mark()+" where status = "+map.get("status");
	}
}

