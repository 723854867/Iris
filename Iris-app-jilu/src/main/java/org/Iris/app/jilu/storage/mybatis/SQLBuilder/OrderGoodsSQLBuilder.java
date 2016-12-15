package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.Iterator;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.OrderGoods;

public class OrderGoodsSQLBuilder {

	public String batchInsert(Map<String, OrderGoods> map) {
		Iterator<OrderGoods> iterator = map.values().iterator();
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("insert into order_goods (order_id,goods_id,goods_count,goods_price) values ");
		while (iterator.hasNext()) {
			OrderGoods orderGoods = iterator.next();
			stringBuilder.append("('"+orderGoods.getOrderId()+"','"+orderGoods.getGoodsId()+"','"+orderGoods.getGoodsCount()+"','"+orderGoods.getGoodsPrice()+"'),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
	

}
