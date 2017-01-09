package com.spnetty.server.util;

import java.util.*;

/**
 * Created by
 * User: djyin
 * Date: 3/6/14
 * Time: 5:54 PM
 * 和Spring的EL表达式配合使用，达到能在spirng中,一个属性中注入set,list,map的目的
 */
public class ELUtils {

    public static Set<String> toSet(String raws) {
        Set<String> cSet = new HashSet<String>();
        String[] spliteds = raws.split(",");
        for (String splited : spliteds) {
            cSet.add(splited);
        }
        return cSet;
    }

    public static List<String> toList(String raws) {
        List<String> cList = new ArrayList<String>();
        String[] spliteds = raws.split(",");
        for (String splited : spliteds) {
            cList.add(splited);
        }
        return cList;
    }

    public static Map<String, String> toMap(String raws) {
        Map<String, String> cMap = new HashMap<String, String>();
        String[] spliteds = raws.split(",");
        for (String splited : spliteds) {
            String[] kv = splited.split(":");
            if (kv != null && kv.length > 1) {
                cMap.put(kv[0], kv[1]);
            }
        }
        return cMap;
    }

}
