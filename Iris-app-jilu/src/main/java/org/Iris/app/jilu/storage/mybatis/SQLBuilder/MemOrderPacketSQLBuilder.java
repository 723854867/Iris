package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemOrderPacket;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

public class MemOrderPacketSQLBuilder {

	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_ORDER_PACKET.mark());
				VALUES("packet_id", 		"#{packetId}");
				VALUES("order_id", 		    "#{orderId}");
				VALUES("merchant_id", 		"#{merchantId}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
	
	public String batchInsert(StrictMap<List<MemOrderPacket>> map) {
		List<MemOrderPacket> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("insert into "+Table.MEM_ORDER_PACKET.mark()+" (packet_id,order_id,merchant_id,`status`,created,updated) values ");
		for(MemOrderPacket packet:list){
			stringBuilder.append("('"+packet.getPacketId()+"','"+packet.getOrderId()+"','"+packet.getMerchantId()+"','"+packet.getStatus()+"','"+packet.getCreated()+"','"+packet.getUpdated()+"'),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_ORDER_PACKET.mark());
				SET("express=#{express}");
				SET("memo=#{memo}");
				SET("express_code=#{expressCode}");
				SET("postage=#{postage}");
				SET("label=#{label}");
				SET("latitude=#{latitude}");
				SET("longitude=#{longitude}");
				SET("status=#{status}");
				SET("updated=#{updated}");
				WHERE("packet_id=#{packetId}");
			}
		}.toString();
	}
	
	public String delete(){
		return new SQL(){
			{
				DELETE_FROM(Table.MEM_ORDER_PACKET.mark());
				WHERE("packet_id=#{packetId}");
			}
		}.toString();
	}
	
	public String getMemOrderPacketById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_PACKET.mark());
				WHERE("packet_id=#{packetId}");
				AND();
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String getMemOrderPacketByExpressCode(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_PACKET.mark());
				WHERE("express_code=#{expressCode}");
			}
		}.toString();
	}
	
	public String getMemOrderPacketByOrderId(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_PACKET.mark());
				WHERE("order_id=#{orderId}");
			}
		}.toString();
	}
	
}
