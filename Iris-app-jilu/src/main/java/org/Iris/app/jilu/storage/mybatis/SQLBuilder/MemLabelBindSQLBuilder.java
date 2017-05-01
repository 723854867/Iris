package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemLabelBind;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

public class MemLabelBindSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.MEM_LABEL_BIND.mark());
				VALUES("label_id", "#{labelId}");
				VALUES("merchant_id", "#{merchantId}");
				VALUES("buy_id", "#{buyId}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}
	
	public String batchInsert(StrictMap<List<MemLabelBind>> map) {
		List<MemLabelBind> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("insert into " + Table.MEM_LABEL_BIND.mark()
				+ " (label_id,merchant_id,buy_id,created,updated) values ");
		for (MemLabelBind memLabelBind : list) {
			stringBuilder.append("('" + memLabelBind.getLabelId() + "','" + memLabelBind.getMerchantId() + "','"
					+ memLabelBind.getBuyId() + "','" 
					+ memLabelBind.getCreated() + "','" + memLabelBind.getUpdated() + "'),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_LABEL_BIND.mark());
				SET("status=#{status}");
				SET("bind_id=#{bindId}");
				SET("bind_type=#{bindType}");
				SET("updated=#{updated}");
				SET("memo=#{memo}");
				SET("longitude=#{longitude}");
				SET("latitude=#{latitude}");
				SET("bind_time=#{bindTime}");
				SET("updated=#{updated}");
				WHERE("label_id=#{labelId}");
			}
		}.toString();
	}
	
	public String findById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_LABEL_BIND.mark());
				WHERE("label_id=#{labelId}");
			}
		}.toString();
	}
	
}
