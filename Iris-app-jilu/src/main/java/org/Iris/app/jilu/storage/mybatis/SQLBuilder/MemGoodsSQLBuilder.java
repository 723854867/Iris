package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemGoodsSQLBuilder {

	public String insert() { 
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_GOODS.mark());
				VALUES("goods_code", 		    "#{goodsCode}");
				VALUES("zh_name", 				"#{zhName}");
				VALUES("us_name", 			    "#{usName}");
				VALUES("goods_format", 			"#{goodsFormat}");
				VALUES("classification", 		"#{classification}");
				VALUES("zh_brand", 				"#{zhBrand}");
				VALUES("us_brand", 				"#{usBrand}");
				VALUES("unit", 					"#{unit}");
				VALUES("weight", 				"#{weight}");
				VALUES("alias", 				"#{alias}");
				VALUES("barcode", 				"#{barcode}");
				VALUES("sku", 					"#{sku}");
				VALUES("created", 				"#{created}");
				VALUES("updated", 				"#{updated}");
			}
		}.toString();
	}
	
	public String getGoodsById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_GOODS.mark());
				WHERE("goods_id=#{goods_id}");
				
			}
		}.toString();
	}
}
