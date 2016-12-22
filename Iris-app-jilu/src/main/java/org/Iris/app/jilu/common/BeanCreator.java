package org.Iris.app.jilu.common;

import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.storage.domain.MerchantAccount;
import org.Iris.app.jilu.storage.domain.MerchantCustomer;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.util.common.CnToSpell;
import org.Iris.util.lang.DateUtils;

/**
 * 转么负责domain对象创建
 * 
 * @author ahab
 */
public class BeanCreator {

	public static Merchant newMemMerchant(String name, String address) { 
		Merchant memMerchant = new Merchant();
		memMerchant.setAddress(address);
		memMerchant.setName(name);
		int time = DateUtils.currentTime();
		memMerchant.setCreated(time);
		memMerchant.setUpdated(time);
		memMerchant.setLastLoginTime(time);
		return memMerchant;
	}
	
	public static MerchantAccount newMemAccount(AccountModel am, int time, long merchantId) {
		MerchantAccount memAccount = new MerchantAccount();
		memAccount.setMerchantId(merchantId);
		memAccount.setAccount(am.getAccount());
		memAccount.setType(am.getType());
		memAccount.setUpdated(time);
		memAccount.setCreated(time);
		return memAccount;
	}
	
	public static MerchantCustomer newMemCustomer(long merchantId, String name, String mobile, String address, String memo, String IDNumber) { 
		MerchantCustomer memCustomer = new MerchantCustomer();
		memCustomer.setMerchantId(merchantId);
		memCustomer.setName(name);
		memCustomer.setMobile(mobile);
		memCustomer.setAddress(address);
		memCustomer.setMemo(memo);
		memCustomer.setIDNumber(IDNumber);
		memCustomer.setNamePrefixLetter(CnToSpell.getFirstChar(name));
		memCustomer.setPurchaseSum("0");//设置初始值
		memCustomer.setLastPurchaseTime(0);//设置初始值
		int time = DateUtils.currentTime();
		memCustomer.setCreated(time);
		memCustomer.setUpdated(time);
		return memCustomer;
	}
	
	public static CfgGoods newMemGoods(String goodsCode, String zhName, String usName, String goodsFormat, String classification, String zhBrand,
			String usBrand, String unit, float weight, String alias, String barcode, String sku){
		CfgGoods memGoods = new CfgGoods();
		memGoods.setGoodsCode(goodsCode);
		memGoods.setZhName(zhName);
		memGoods.setUsName(usName);
		memGoods.setGoodsFormat(goodsFormat);
		memGoods.setClassification(classification);
		memGoods.setZhBrand(zhBrand);
		memGoods.setUsBrand(usBrand);
		memGoods.setUnit(unit);
		memGoods.setWeight(weight);
		memGoods.setAlias(alias);
		memGoods.setBarcode(barcode);
		memGoods.setSku(sku);
		int time = DateUtils.currentTime();
		memGoods.setCreated(time);
		memGoods.setUpdated(time);
		return memGoods;
	}
	
	public static MerchantOrder newMemOrder(String orderId, long merchantId, String merchantName,String memchantAddress, long customerId, String customerName,
			String customerMobile, String customerAddress, int status){
		MerchantOrder memOrder = new MerchantOrder();
		memOrder.setOrderId(orderId);
		memOrder.setMerchantId(merchantId);
		memOrder.setMerchantName(merchantName);
		memOrder.setMerchantAddress(memchantAddress);
		memOrder.setCustomerId(customerId);
		memOrder.setCustomerName(customerName);
		memOrder.setCustomerMobile(customerMobile);
		memOrder.setCustomerAddress(customerAddress);
		memOrder.setStatus(status);
		int time = DateUtils.currentTime();
		memOrder.setCreated(time);
		memOrder.setUpdated(time);
		return memOrder;
	}
}
