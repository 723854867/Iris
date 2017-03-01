package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.Map;

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
				VALUES("source", 				"#{source}");
				VALUES("merchant_name", 		"#{merchantName}");
				VALUES("created", 				"#{created}");
				VALUES("updated", 				"#{updated}");
			}
		}.toString();
	}
	
	public String update() { 
		return new SQL() {
			{
				UPDATE(Table.CFG_GOODS.mark());
				SET("zh_name=#{zhName}");
				SET("us_name=#{usName}");
				SET("goods_format=#{goodsFormat}");
				SET("classification=#{classification}");
				SET("zh_brand=#{zhBrand}");
				SET("us_brand=#{usBrand}");
				SET("unit=#{unit}");
				SET("weight=#{weight}");
				SET("alias=#{alias}");
				SET("barcode=#{barcode}");
				SET("sku=#{sku}");
				SET("unit_price=#{unitPrice}");
				SET("unit_price=#{unitPrice}");
				SET("updated=#{updated}");
				WHERE("goods_id=#{goodsId}");
			}
		}.toString();
	}
	
	public String delete(){
		return new SQL(){
			{
				DELETE_FROM(Table.CFG_GOODS.mark());
				WHERE("goods_id=#{goodsId}");
				AND();
				WHERE("source=#{source}");
			}
		}.toString();
	}
	
	public String getGoodsById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.CFG_GOODS.mark());
				WHERE("goods_id=#{goodsId}");
				
			}
		}.toString();
	}
	
	public String getCountByGoodsName(Map<String, Object> para){
		return new SQL(){
			{
				SELECT("count(1)");
				FROM(Table.CFG_GOODS.mark());
				WHERE("zh_name like '%"+para.get("zhName")+"%'");
			}
		}.toString();
	}
	
	public String getCountByCode(){
		return new SQL(){
			{
				SELECT("count(1)");
				FROM(Table.CFG_GOODS.mark());
				WHERE("goods_code=#{goodsCode}");
			}
		}.toString();
	}
	
	public String getCountByMerchantId(){
		return new SQL(){
			{
				SELECT("count(1)");
				FROM(Table.CFG_GOODS.mark());
				WHERE("source=#{merhcantId}");
			}
		}.toString();
	}
	public String getGoodsListByGoodsName(Map<String, Object> para){
		return "select * from "+Table.CFG_GOODS.mark()+" where zh_name like '%"+para.get("zhName")+"%' LIMIT #{start},#{pageSize}";
	}
	
	public String getGoodsListByCode(){
		return "select * from "+Table.CFG_GOODS.mark()+" where goods_code = #{goodsCode} LIMIT #{start},#{pageSize}";
	}
	
	public String getGoodsListByMerchantId(){
		return "select * from "+Table.CFG_GOODS.mark()+" where source=#{merchantId} LIMIT #{start},#{pageSize}";
	}
}
