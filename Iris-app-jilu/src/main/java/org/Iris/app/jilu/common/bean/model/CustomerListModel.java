package org.Iris.app.jilu.common.bean.model;

import org.Iris.app.jilu.common.bean.enums.CustomerListType;

public interface CustomerListModel {

	long getCustomerId();
	
	double getScore(CustomerListType type);
}
