package org.Iris.app.jilu.common.bean.form;

import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.storage.domain.CfgGoods;

/**
 * 商品表单
 * @author 樊水东
 * 2017年3月13日
 */
public class CfgGoodsForm {
	private long goodsId;
	private String goodsCode;
	private String zhName;
	private float unitPrice;
	private String classification;
	private String source;//目前暂时只有商户 如果是商户则存储商户id
	private String merchantName;//来源名字
	
	public CfgGoodsForm(){
		
	}
	public CfgGoodsForm(CfgGoods goods){
		this.goodsId = goods.getGoodsId();
		this.goodsCode = goods.getGoodsCode();
		this.zhName = goods.getZhName();
		this.unitPrice = goods.getUnitPrice();
		this.source = goods.getSource();
		this.classification = goods.getClassification();
		this.merchantName = goods.getMerchantName();
	}
	
	public static List<CfgGoodsForm> getCfgGoodsFormList(List<CfgGoods> list){
		List<CfgGoodsForm> cfgGoodsForms = new ArrayList<CfgGoodsForm>();
		for(CfgGoods cfgGoods:list){
			CfgGoodsForm form = new CfgGoodsForm(cfgGoods);
			cfgGoodsForms.add(form);
		}
		return cfgGoodsForms;
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
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	
}
