package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemOrderPacket;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemOrderPacketSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemOrderPacketMapper {

	@InsertProvider(type = MemOrderPacketSQLBuilder.class, method = "insert")
	void insert(MemOrderPacket packet);
	
	@UpdateProvider(type = MemOrderPacketSQLBuilder.class, method = "update")
	void update(MemOrderPacket packet);
	
	@InsertProvider(type = MemOrderPacketSQLBuilder.class, method = "batchInsert")
	void batchInsert(List<MemOrderPacket> list);
	
	@SelectProvider(type = MemOrderPacketSQLBuilder.class, method = "getMemOrderPacketById")
	MemOrderPacket getMemOrderPacketById(@Param("packetId") String packetId,@Param("merchantId") long merchantId);
}
