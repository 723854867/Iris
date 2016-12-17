package org.Iris.app.jilu.common;

import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;

public class JiLuResourceUtil {

	private static final String MERCHANT_FOLDER 			= "/common/merchant/";
	private static final String CUSTOMER_FOLDER	 			= "/customer/";

	private static final String AVATAR 						= "/avatar.jpg";
	private static final String CUSTOMER_ID_FRONTAGE 		= "/IDFrontage.jpg";
	private static final String CUSTOMER_ID_BEHIND	 		= "/IDBehind.jpg";

	/**
	 * 商户头像地址
	 * 
	 * @param merchant
	 * @return
	 */
	public static final String merchantAvatarUri(MemMerchant merchant) {
		StringBuilder builder = new StringBuilder(AppConfig.getAliyunOssFolderPrefix());
		return builder.append(MERCHANT_FOLDER).append(merchant.getMerchantId()).append(AVATAR).toString();
	}

	/**
	 * 客户身份证正面
	 * 
	 * @param memCustomer
	 * @return
	 */
	public static final String customerIDFrontageUri(MemCustomer memCustomer) {
		StringBuilder builder = new StringBuilder(AppConfig.getAliyunOssFolderPrefix());
		return builder.append(MERCHANT_FOLDER).append(memCustomer.getMerchantId()).append(CUSTOMER_FOLDER)
				.append(memCustomer.getCustomerId()).append(CUSTOMER_ID_FRONTAGE).toString();
	}
	
	/**
	 * 客户身份证翻面
	 * 
	 * @param memCustomer
	 * @return
	 */
	public static final String customerIDBehindUri(MemCustomer memCustomer) {
		StringBuilder builder = new StringBuilder(AppConfig.getAliyunOssFolderPrefix());
		return builder.append(MERCHANT_FOLDER).append(memCustomer.getMerchantId()).append(CUSTOMER_FOLDER)
				.append(memCustomer.getCustomerId()).append(CUSTOMER_ID_BEHIND).toString();
	}
}
