package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.redis.RedisHashBean;

public class CfgGoods implements RedisHashBean {

	private long goodsId;
	private String goodsCode;
	private String zhName;
	private String usName;
	private String goodsFormat;
	private String classification;
	private String zhBrand;
	private String usBrand;
	private String unit;
	private float weight;
	private String alias;
	private String barcode;
	private String sku;
	private float unitPrice;
	private String source;//目前暂时只有商户 如果是商户则存储商户id
	private String merchantName;
	private int created;
	private int updated;
	//供后台时间转换
	private String updateTime;

	public CfgGoods() {
	}
	public CfgGoods(long goodsId) {
		super();
		this.goodsId = goodsId;
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

	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
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

	public float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String redisKey() {
		return CommonKeyGenerator.getMemGoodsKey(goodsId);
	}

}