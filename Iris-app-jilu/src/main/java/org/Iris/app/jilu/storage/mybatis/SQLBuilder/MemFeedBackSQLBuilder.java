package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.Map;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemFeedBackSQLBuilder {
	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.MEM_FEEDBACK.mark());
				VALUES("created", "#{created}");
				VALUES("merchant_id", "#{merchantId}");
				VALUES("content", "#{content}");
				VALUES("contact", "#{contact}");
			}
		}.toString();
	}
	/**
	 * 获取所有的反馈信息
	 */
	public String getList(Map<String, Object> map){
		StringBuilder builder = new StringBuilder();
		builder.append("select mf.*,mu.nick_name as nick_name, mu.mobile as mobile from "+Table.MEM_FEEDBACK.mark()+" mf left join "+Table.MEM_MERCHANT.mark()+" mu on mu.user_id=mf.user_id where 1=1");
		if(map.get("nickName")!=""&&map.get("nickName")!=null)
			builder.append(" and mu.nick_name like '%"+map.get("nickName")+"%'");
		if(map.get("mobile")!=""&&map.get("nickName")!=null)
			builder.append(" and mu.mobile like '%"+map.get("mobile")+"%'");
		builder.append(" order by mf.created desc limit "+map.get("start")+","+map.get("pageSize"));
		return builder.toString();
	}
	/**
	 * 获取反馈信息的总数
	 */
	public String getCount(Map<String, Object> map){
		StringBuilder builder = new StringBuilder();
		builder.append("select count(*) from "+Table.MEM_FEEDBACK.mark()+" mf left join "+Table.MEM_MERCHANT.mark()+" mu on mu.user_id=mf.user_id where 1=1");
		if(map.get("nickName")!=""&&map.get("nickName")!=null)
			builder.append(" and mu.nick_name like '%"+map.get("nickName")+"%'");
		if(map.get("mobile")!=""&&map.get("nickName")!=null)
			builder.append(" and mu.mobile like '%"+map.get("mobile")+"%'");
		return builder.toString();
	}
}
