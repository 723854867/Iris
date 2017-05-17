package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemJbDetailSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.MEM_JB_DETAIL.mark());
				VALUES("merchant_id", "#{merchantId}");
				VALUES("jb", "#{jb}");
				VALUES("time", "#{time}");
				VALUES("type", "#{type}");
				VALUES("order_id", "#{orderId}");
			}
		}.toString();
	}
	
	/**
	 * 获取收支明细
	 * @return
	 */
	public String getJbDetail(){
		return "select * from "+Table.MEM_JB_DETAIL.mark()+" where merchant_id=#{merchantId} order by time desc limit #{start},#{pageSize}";
	}
	
	public String getJbDetailCount(){
		return "select count(1) from "+Table.MEM_JB_DETAIL.mark()+" where merchant_id=#{merchantId}"; 
	}
	/**
	 * 获取收入总金额
	 * @return
	 */
	public String getRevenue(){
		return "select sum(jb) jb from "+Table.MEM_JB_DETAIL.mark()+" where merchant_id=#{merchantId} and type=0";
	}
	
	/**
	 * 获取支出总金额
	 * @return
	 */
	public String getExpenses(){
		return "select sum(jb) jb from "+Table.MEM_JB_DETAIL.mark()+" where merchant_id=#{merchantId} and type!=0";
	}
}
