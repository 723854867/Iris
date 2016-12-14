package org.Iris.app.jilu.storage.domain;

import org.Iris.redis.RedisHashBean;

public class Goods implements RedisHashBean {

	private long goodsId;
	private String goodsCode;
	private String zhName;
	private String usName;
	private String goodsFormat;
	private String sort;
	private String zhBrand;
	private String usBrand;
	private String unit;
	private float weight;
	private String alias;
	private String barcode;
	private String sku;
	private int created;
	private int updated;

	public Goods() {
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getZhName() {
		return zhName;
	}

	public void setZhName(String zhName) {
		this.zhName = zhName;
	}

	public String getUsName() {
		return usName;
	}

	public void setUsName(String usName) {
		this.usName = usName;
	}

	public String getGoodsFormat() {
		return goodsFormat;
	}

	public void setGoodsFormat(String goodsFormat) {
		this.goodsFormat = goodsFormat;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getZhBrand() {
		return zhBrand;
	}

	public void setZhBrand(String zhBrand) {
		this.zhBrand = zhBrand;
	}

	public String getUsBrand() {
		return usBrand;
	}

	public void setUsBrand(String usBrand) {
		this.usBrand = usBrand;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	@Override
	public String redisKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
