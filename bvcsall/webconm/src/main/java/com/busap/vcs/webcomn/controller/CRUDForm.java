package com.busap.vcs.webcomn.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by djyin on 3/27/2014.
 */
public class CRUDForm {
    /**
     *
     */
    protected Map<String, String> filters = new HashMap<String, String>();


    /**
     * 排序条件,比如
     * IdAscUsernameDesc
     */
    protected String orders;

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }


    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }

}
