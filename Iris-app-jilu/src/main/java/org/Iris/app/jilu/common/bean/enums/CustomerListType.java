package org.Iris.app.jilu.common.bean.enums;

/**
 * 客户列表类型
 * 
 * @author ahab
 */
public enum CustomerListType {

	NAME(0),
	PURCHASE_SUM(1),
	PURCHASE_RECENT(2),
	PURCHASE_FREQUENCY(3);
	
	private int mark;
	
	private CustomerListType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public static final CustomerListType match(int type) {
		for (CustomerListType listType : CustomerListType.values()) {
			if (listType.mark != type)
				continue;
			return listType;
		}
		return null;
	}
}
