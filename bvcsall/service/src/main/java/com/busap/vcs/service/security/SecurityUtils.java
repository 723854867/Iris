package com.busap.vcs.service.security;

import com.busap.vcs.base.Filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by
 * User: djyin
 * Date: 12/24/13
 * Time: 1:19 PM
 */
@SuppressWarnings("rawtypes")
public class SecurityUtils extends org.apache.shiro.SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    private static Pattern complex = Pattern.compile("(.*?)(#)(F\\()(.*?)(\\))(.*)");

    private static Pattern simple = Pattern.compile("(.*?)(#)(.*)");

    private static Pattern idin = Pattern.compile("(\\d+)(,\\d+)*");
    /**
     * 从Permission里面抽取出一些信息，常见主要针对ID的限制和一些String类型属性的限制，支持的用法有
     * 1. vcs:channel:1,2,3 pos=2 会翻译成 id In 1,2,3
     * 2. vcs:channel:idGt#15  pos=2   会翻译成 id > 15
     * 3. vcs:channel:createBy#djyin  pos=2  会翻译成 createBy =  djyin
     * 4. vcs:channel:*  pos=2 会翻译成 null
     * 5. vcs:channel:createAtGt#F(java.util.Date)2012-02-11 11:11:20 pos=2 会翻译成 createBy > 2012-02-11 11:11:20.
     *
     * @param permission
     *         the permission
     * @param pos
     *         the pos  从0开始
     * @return the filter
     */
    public static Filter getFilter(String permission, Integer pos) {
        if (StringUtils.isBlank(permission)) {
            log.debug("null permission is unsupported.");
            return null;
        }
        // 获取条件
        String condition = null;
        Filter filter = null;
        int lastIndex;
        int index = 0;
        int count = 0;
        while (true) {
            lastIndex = index;
            index = permission.indexOf(':', lastIndex + 1);
            if (index < 0) {
                index = permission.length();
                break;
            }
            count++;
            if (count > pos) {
                index = permission.length();
                break;
            }
        }
        if (count < pos) {
            log.debug("too short permission {} is unsupported.", permission);
            return null;
        }
        condition = permission.substring(lastIndex + 1, index);
        log.debug("condition is {}", condition);
        // 分析条件
        if (StringUtils.isNotBlank(condition)) {
            if (condition.equals("*")) { //通配符模式
                // ignored
                log.debug("permission {} is wildcard.", permission);
                return null;
            } else if (condition.contains("#F(")) { // 复杂模式，带数据转换
                // 分析是否存在强制转换
                Matcher m = complex.matcher(condition);
                if (m.find()) {
                    String property = m.group(1);
                    String type = m.group(4);
                    String value = m.group(6);
                    Primitive primitive = Primitive.fromString(type);
                    Object obj = Primitive.toObject(primitive, value);
                    // 组合Filter
                    filter = new Filter(property, obj);
                }
            } else if (condition.contains("#")) { // 简单模式，处理字符串形式的属性
                // 分析是否存在强制转换
                Matcher m = simple.matcher(condition);
                if (m.find()) {
                    String property = m.group(1);
                    String value = m.group(3);
                    // 组合Filter
                    filter = new Filter(property, value);
                } else {
                    log.debug("permission {} is unsupported.", permission);
                    return null;
                }

            } else if (idin.matcher(condition).find()) {//idIn模式
                String[] sss = condition.split(",");
                Set<Long> idSet = new HashSet<Long>();
                for (String s : sss) {
                    idSet.add(Long.parseLong(s));
                }
                // 组合Filter
                filter = Filter.in("id", idSet);
            } else { // unsupport
                log.debug("permission {} is unsupported.", permission);
            }
        }
        log.debug("Filter is {}", filter);

        // 返回
        return filter;
    }
    /**
     * 从Permission里面抽取出一些信息，常见主要针对ID的限制或者属性的限制，支持的用法有
     * 1. vcs:channel:1,2,3 pos=2 会翻译成 id IN (1,2,3)
     * 2. vcs:channel:name#hello,world  pos=2   会翻译成 name IN (hello,world)
     * 3. vcs:channel:createBy#djyin  pos=2  会翻译成 createBy IN (djyin)
     * 4. vcs:channel:*  pos=2 会翻译成 null
     * 5. vcs:channel:createAt#F(java.util.Date)2012-02-11 11:11:20,2012-02-31 11:11:20 pos=2 会翻译成 createAt IN (2012-02-11 11:11:20,2012-02-31 11:11:20)
     *
     * @param perms
     *         the permission
     * @param pos
     *         the pos  从0开始
     * @return the filter
     */
	@SuppressWarnings("unchecked")
	public static List<Filter> getSelectINFilter(List<String> perms, Integer pos) {
        Map<String, Set> pMap = new HashMap<String, Set>();
        List<Filter> filters = new ArrayList<Filter>();
        boolean wildcardId = false;
        for (String permission : perms) {
            if (StringUtils.isBlank(permission)) {
                log.debug("null permission is unsupported.");
                continue;
            }
            // 获取条件
            String condition = null;

            int lastIndex;
            int index = 0;
            int count = 0;
            while (true) {
                lastIndex = index;
                index = permission.indexOf(':', lastIndex + 1);
                if (index < 0) {
                    index = permission.length();
                    break;
                }
                count++;
                if (count > pos) {
                    index = permission.length();
                    break;
                }

            }
            if (count < pos) {
                log.debug("too short permission {} is unsupported.", permission);
                continue;
            }
            condition = permission.substring(lastIndex + 1, index);
            log.debug("condition is {}", condition);

            // 分析条件
            if (StringUtils.isNotBlank(condition)) {
                if (condition.equals("*")) { //通配符模式
                    // ignored
                    log.debug("permission {} is wildcard.", permission);
                    wildcardId = true;
                    continue;
                } else
                if (condition.contains("#F(")) { // 属性模式 复杂模式，带数据转换
                    // 分析是否存在强制转换
                    Matcher m = complex.matcher(condition);
                    if (m.find()) {
                        String property = m.group(1);
                        String type = m.group(4);
                        Primitive primitive = Primitive.fromString(type);
                        String values = m.group(6);

                        String[] sss = values.split(",");
                        Set pSet = pMap.get(property);
                        if (pSet == null) {
                            pSet = new HashSet();
                            pMap.put(property, pSet);
                        }
                        for (String s : sss) {
                            pSet.add(Primitive.toObject(primitive, s));
                        }
                    } else {
                        log.debug("permission {} is unsupported.", permission);
                        continue;
                    }
                } else if (condition.contains("#")) { // 简单模式，处理字符串形式的属性
                    Matcher m = simple.matcher(condition);
                    if (m.find()) {
                        String property = m.group(1);
                        String values = m.group(3);
                        String[] sss = values.split(",");
                        Set pSet = pMap.get(property);
                        if (pSet == null) {
                            pSet = new HashSet();
                            pMap.put(property, pSet);
                        }
                        for (String s : sss) {
                            pSet.add(s);
                        }
                    } else {
                        log.debug("permission {} is unsupported.", permission);
                        continue;
                    }

                } else if (idin.matcher(condition).find()) {//idIn模式
                    String[] sss = condition.split(",");
                    String property = "id";
                    Set pSet = pMap.get(property);
                    if (pSet == null) {
                        pSet = new HashSet();
                        pMap.put(property, pSet);
                    }
                    for (String s : sss) {
                        pSet.add(Long.parseLong(s));
                    }
                } else { // unsupport
                    log.debug("permission {} is unsupported.", permission);
                    continue;
                }
            }

        }
        if (wildcardId) {
            pMap.remove("id");
        }
        for (Map.Entry<String, Set> entry : pMap.entrySet()) {
            String property = entry.getKey();
            Set value = entry.getValue();
            Filter filter = Filter.in(property, value);
            log.debug("filter is {}", filter);
            filters.add(filter);
        }
        // 返回
        return filters.isEmpty() ? null : filters;
    }

    /**
     * 从Permission里面抽取出一些信息，只处理id和通配符*的情况
     *
     * @param perms the perms
     * @param pos the pos 从0开始
     * @return the filter 如果出现统配符,或者没有任何ID,将返回NULL
     */
    public static Filter getSelectInIdsetFilter(List<String> perms, Integer pos){
        Set<Long> idSet = new HashSet<Long>();
        // 尝试过滤出id来
        for (String permission : perms) {
            if (StringUtils.isBlank(permission)) {
                log.debug("null permission is unsupported.");
                continue;
            }
            int lastIndex;
            int index = 0;
            int count = 0;

            while (true) {
                lastIndex = index;
                index = permission.indexOf(':', lastIndex + 1);
                if (index < 0) {
                    index = permission.length();
                    break;
                }
                count++;
                if (count > pos) {
                    index = permission.length();
                    break;
                }

            }
            if (count < pos) {
                log.debug("too short permission {} is unsupported.", permission);
                continue;
            }
            String condition = permission.substring(lastIndex + 1, index);
            log.debug("condition is {}", condition);

            if (condition.contains("*")) {  //通配符
                log.debug("permission {} is wildcard", permission);
                return null;
            } else if (!idin.matcher(condition).find()) {//idIn模式
                // unsupport
                log.debug("permission {} is unsupported.", permission);
                continue;
            }

            String[] ids = condition.split(",");
            for (String id : ids) {
                if(StringUtils.isNotBlank(id)){
                    idSet.add(Long.parseLong(id));
                }
            }
        }
        return idSet.isEmpty() ? null : new Filter("idIn", idSet);
    }
    @SuppressWarnings("unused")
	public static void main(String[] args) {
        int pos = 2;
        String[] perms = new String[]{
                "vcs:channel",
                "vcs:channel:create",
                "vcs:channel:1,2,3",
                "vcs:channel:idGt#15",
                "vcs:channel:createBy#djyin",
                "vcs:channel:createAt#F(java.util.Date)2012-02-11 11:11:20",
                "vcs:channel:*",
                ":vcs:*"
        };


//        for (String permission : perms) {
//            Filter f = getFilter(permission, pos);
//            log.debug("{}", f);
//
//        }
//        log.debug("*********************************************}");
        perms = new String[]{
                "vcs:channel",
                "vcs:channel:create",
                "vcs:channel:1,2,3",
                "vcs:channel:id#15",
                "vcs:channel:createBy#djyin",
                "vcs:channel:createAt#F(java.util.Date)2012-02-11 11:11:20,2012-02-21 11:11:20",
                "vcs:channel:*",
                ":vcs:*"
        };
        List<String> pList = new ArrayList<String>();
        for (String p : perms) {
            pList.add(p);
        }
        List<Filter> filters = getSelectINFilter(pList, pos);
        log.debug("*********************************************}");
        perms = new String[]{
                "vcs:channel",
                "vcs:channel:create",
                "vcs:channel:1,2,3",
                "vcs:channel:id#15",
                "vcs:channel:createBy#djyin",
                "vcs:channel:createAt#F(java.util.Date)2012-02-11 11:11:20,2012-02-21 11:11:20",
                ":vcs:*"
        };
        pList = new ArrayList<String>();
        for (String p : perms) {
            pList.add(p);
        }
        filters = getSelectINFilter(pList, pos);
//        log.debug("*********************************************}");
//        pList.clear();
//        pList.add("vcs:channel:1,2,3");
//        pList.add("vcs:channel:4,5,3");
//        pList.add("vcs:channel");
//        Filter f = getSelectInIdsetFilter(pList, pos);
//        log.debug("*********************************************}");
//        pList.clear();
//        pList.add("vcs:channel:1,2,3");
//        pList.add("vcs:channel:4,5,3");
//        pList.add("vcs:channel");
//        pList.add("vcs:channel:*");
//        f = getSelectInIdsetFilter(pList, pos);

    }


}
