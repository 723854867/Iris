package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class CfgGoodsSQLBuilder {

	public String insert() { 
		return new SQL() {
			{
				INSERT_INTO(Table.CFG_GOODS.mark());
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
				VALUES("unit_price", 			"#{unitPrice}");
				VALUES("created", 				"#{created}");
				VALUES("updated", 				"#{updated}");
			}
		}.toString();
	}
	
	public String getGoodsById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.CFG_GOODS.mark());
				WHERE("goods_id=#{goods_id}");
				
			}
		}.toString();
	}
}
