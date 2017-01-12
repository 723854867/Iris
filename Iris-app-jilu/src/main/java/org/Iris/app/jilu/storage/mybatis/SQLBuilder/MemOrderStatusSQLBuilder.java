package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemOrderStatusSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.MEM_ORDER_STATUS.mark());
				VALUES("order_id", 				  "#{orderId}");
				VALUES("goods_count",			  "#{goodsCount}");
				VALUES("transform_success_count", "#{transformSuccessCount}");
				VALUES("transform_count", 		  "#{transformCount}");
				VALUES("packet_count", 			  "#{packetCount}");
				VALUES("transport_count",		  "#{transportCount}");
				VALUES("finished_count", 		  "#{finishedCount}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_ORDER_STATUS.mark());
				SET("goods_count=#{goodsCount}");
				SET("transform_success_count=#{transformSuccessCount}");
				SET("transform_count=#{transformCount}");
				SET("packet_count=#{packetCount}");
				SET("transport_count=#{transportCount}");
				SET("finished_count=#{finishedCount}");
				WHERE("order_id=#{orderId}");
			}
		}.toString();
	}
	
	public String getMemOrderStatusByOrderId(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_STATUS.mark());
				WHERE("order_id=#{orderId}");
			}
		}.toString();
	}
}
