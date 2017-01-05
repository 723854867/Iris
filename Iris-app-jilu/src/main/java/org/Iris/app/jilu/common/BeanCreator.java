package org.Iris.app.jilu.common;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.PubRelation;
import org.Iris.app.jilu.storage.domain.MemOrderPacket;
import org.Iris.util.common.CnToSpell;
import org.Iris.util.lang.DateUtils;

/**
 * 转么负责domain对象创建
 * 
 * @author ahab
 */
public class BeanCreator {

	public static MemMerchant newMemMerchant(String name, String address) { 
		MemMerchant memMerchant = new MemMerchant();
		memMerchant.setAddress(address);
		memMerchant.setName(name);
		int time = DateUtils.currentTime();
		memMerchant.setCreated(time);
		memMerchant.setUpdated(time);
		memMerchant.setLastLoginTime(time);
		return memMerchant;
	}
	
	public static MemAccount newMemAccount(AccountType type, String accout, int time, long merchantId) {
		MemAccount memAccount = new MemAccount();
		memAccount.setMerchantId(merchantId);
		memAccount.setAccount(accout);
		memAccount.setType(type.mark());
		memAccount.setUpdated(time);
		memAccount.setCreated(time);
		return memAccount;
	}
	
	public static MemCustomer newMemCustomer(long merchantId, String name, String mobile, String address, String memo, String IDNumber) { 
		MemCustomer memCustomer = new MemCustomer();
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
			String usBrand, String unit, float weight, String alias, String barcode, String sku,String unitPrice){
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
		memGoods.setUnitPrice(unitPrice);
		return memGoods;
	}
	
	public static MemOrder newMemOrder(String orderId, long merchantId, String merchantName,String memchantAddress, long customerId, String customerName,
			String customerMobile, String customerAddress, int status){
		MemOrder memOrder = new MemOrder();
		memOrder.setOrderId(orderId);
		memOrder.setMerchantId(merchantId);
		memOrder.setMerchantName(merchantName);
		memOrder.setMerchantAddress(memchantAddress);
		memOrder.setCustomerId(customerId);
		memOrder.setCustomerName(customerName);
		memOrder.setCustomerMobile(customerMobile);
		memOrder.setCustomerAddress(customerAddress);
		memOrder.setStatus(status);
		memOrder.setRootOrderId(orderId);
		int time = DateUtils.currentTime();
		memOrder.setCreated(time);
		memOrder.setUpdated(time);
		return memOrder;
	}
	
	public static final PubRelation newRelation(String id, long applier, long acceptor, int mod) {
		PubRelation relation = new PubRelation();
		relation.setId(id);
		relation.setApplier(applier);
		relation.setAcceptor(acceptor);
		relation.setMod(mod);
		int time = DateUtils.currentTime();
		relation.setCreated(time);
		relation.setUpdated(time);
		return relation;
	}
	
	public static MemOrderPacket newMemOrderPacket(String packetId,String orderId,long merchantId) {
		MemOrderPacket packet = new MemOrderPacket();
		packet.setPacketId(packetId);;
		packet.setOrderId(orderId);
		packet.setMerchantId(merchantId);
		int time = DateUtils.currentTime();
		packet.setCreated(time);
		packet.setUpdated(time);
		return packet;
	}
}
