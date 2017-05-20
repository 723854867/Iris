package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.ArrayList;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.Iris.util.lang.DateUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

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
	public String batchInsert(StrictMap<ArrayList<ArrayList<Object>>> map){
		int time = DateUtils.currentTime();
		ArrayList<ArrayList<Object>> rowList = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("insert into " + Table.CFG_GOODS.mark()
				+ " (goods_code,zh_name,goods_format,us_name,classification,zh_brand,us_brand,unit,weight,alias,sku,barcode,unit_price,source,merchant_name,created,updated) values ");
		for (int i = 2;i<rowList.size();i++) {
			stringBuilder.append("(");
			for(int j = 0;j<13;j++){
				if(j < rowList.get(i).size()){
					stringBuilder.append("'"+rowList.get(i).get(j)+"',");
				}
				else{
					if(j==8)//weight
						stringBuilder.append("0,");
					else if(j==12)//unit_price
						stringBuilder.append("1,");
					else
						stringBuilder.append("'',");
				}
					
			}
			stringBuilder.append("0,'公共库',"+time+","+time+"),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
	
	public String batchInsertByMerchant(Map<String, Object> map){
		int time = DateUtils.currentTime();
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<Object>> rowList = (ArrayList<ArrayList<Object>>)map.get("list");
		MemMerchant memMerchant = (MemMerchant)map.get("memMerchant");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("insert into " + Table.CFG_GOODS.mark()
				+ " (goods_code,zh_name,goods_format,us_name,classification,zh_brand,us_brand,unit,weight,alias,sku,barcode,unit_price,source,merchant_name,created,updated) values ");
		for (int i = 2;i<rowList.size();i++) {
			stringBuilder.append("(");
			for(int j = 0;j<13;j++){
				if(j < rowList.get(i).size()){
					stringBuilder.append("'"+rowList.get(i).get(j)+"',");
				}
				else{
					if(j==8)//weight
						stringBuilder.append("0,");
					else if(j==12)//unit_price
						stringBuilder.append("1,");
					else
						stringBuilder.append("'',");
				}
					
			}
			stringBuilder.append(""+memMerchant.getMerchantId()+",'"+memMerchant.getName()+"',"+time+","+time+"),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
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
	/**
	 * 条件搜索 后台使用
	 * @param map
	 * @return
	 */
	public String getGoodsList(Map<String, Object> map){
		StringBuilder builder = new StringBuilder();
		builder.append("select * from "+Table.CFG_GOODS.mark()+" where 1=1 ");
		if(!map.get("zhName").equals(""))
			builder.append("and zh_name like '%"+map.get("zhName")+"%' ");
		if(!map.get("alias").equals(""))
			builder.append("and alias like '%"+map.get("alias")+"%' ");
		if(!map.get("goodsCode").equals(""))
			builder.append("and goods_code like '%"+map.get("goodsCode")+"%' ");
		if(map.get("source")!=null && !map.get("source").equals(""))
			builder.append("and source = "+map.get("source")+" ");
		builder.append("order by updated desc LIMIT "+map.get("start")+","+map.get("pageSize")+"");
		return builder.toString();
	}
	
	public String getCount(Map<String, Object> map){
		StringBuilder builder = new StringBuilder();
		builder.append("select count(1) from "+Table.CFG_GOODS.mark()+" where 1=1 ");
		if(!map.get("zhName").equals(""))
			builder.append("and zh_name like '%"+map.get("zhName")+"%' ");
		if(!map.get("alias").equals(""))
			builder.append("and alias like '%"+map.get("alias")+"%' ");
		if(!map.get("goodsCode").equals(""))
			builder.append("and goods_code like '%"+map.get("goodsCode")+"%' ");
		if(map.get("source")!=null && !map.get("source").equals(""))
			builder.append("and source = "+map.get("source")+" ");
		return builder.toString();
	}
}
