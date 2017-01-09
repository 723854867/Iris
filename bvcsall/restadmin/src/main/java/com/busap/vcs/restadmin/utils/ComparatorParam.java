package com.busap.vcs.restadmin.utils;

import com.busap.vcs.data.entity.LiveParam;

import java.util.Comparator;

/**
 * 直播参数比较器
 * Created by Knight on 15/11/13.
 */
public class ComparatorParam implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        LiveParam param1 = (LiveParam) o1;
        LiveParam param2 = (LiveParam) o2;
        int typeFlag = param1.getType().compareTo(param2.getType());
        if (typeFlag == 0) {
            return param1.getCount().compareTo(param2.getCount());
        } else {
            return typeFlag;
        }
    }
}
