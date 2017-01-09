package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.Gift;

/**
 * Created by busap on 2015/12/25.
 */
public class GiftDisplay extends Gift {

    private String sort;

    private String order;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
