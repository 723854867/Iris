package com.busap.vcs.base;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用的过滤器实现,用来配合DAO的findList,count函数实现动态查询
 */
@SuppressWarnings("unchecked")
public class Filter implements Serializable {
    public enum Operator {
        eq, ne, gt, lt, ge, le, like, in, isnull, isnotnull;

        public static Operator fromString(String value) {
            return valueOf(value.toLowerCase());
        };
        public String toString() {
            switch (this) {
                case eq:
                    return "=";
                case ne:
                    return "!=";
                case gt:
                    return ">";
                case lt:
                    return "<";
                case ge:
                    return ">=";
                case le:
                    return "<=";
                case like:
                    return "LIKE";
                case in:
                    return "IN";
                case isnull:
                    return "IS NULL";
                case isnotnull:
                    return "IS NOT NULL";
                default:
                    return "=";
            }
        };
    }

    private static final Logger log = LoggerFactory.getLogger(Filter.class);
    /**
     * 用来解析 xxxEq 这种类型的字符串,解析出xxx属性和Eq操作符
     */
    private static Pattern opPatten = Pattern.compile("(.*)(Eq|Ne|Gt|Lt|Ge|Le|Like|In|IsNull|IsNotNull)$");

    private static final long serialVersionUID = -8712382358441065075L;
    private static final boolean DefaultIgnoreCase = false;
    private String property;
    private Operator operator;
    private Object value;
    private Boolean ignoreCase = Boolean.valueOf(DefaultIgnoreCase);
    
    private String root;
    
    

    public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}
	
	private String join;
	
	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}
	
	private String queryProperty;
	
	

	public String getQueryProperty() {
		return queryProperty;
	}

	public void setQueryProperty(String queryProperty) {
		this.queryProperty = queryProperty;
	}

	@SuppressWarnings("rawtypes")
    private static Object toClzNewInstance(Class clz){
        //判断 clz 是不是一个集合类
        if (clz.isAssignableFrom(List.class)) {
            // 尝试获取 泛型
            //Type t = ((ParameterizedType) clz.getGenericSuperclass()).getActualTypeArguments()[0];
            // 根据泛型，创建新List
            List<?> pl = new ArrayList();
            return pl;

        } else if (clz.isAssignableFrom(Map.class)) {//判断 clz 是不是一个MAP类
            // 尝试获取 泛型
            //Type[] types = ((ParameterizedType) clz.getGenericSuperclass()).getActualTypeArguments();
            //Type tKey = types[0];
            //Type tValue = types[1];
            // 根据泛型，创建新List
            Map<?,?> pm = new HashMap();
            return pm;

        } else if (clz.isAssignableFrom(Set.class)) {//判断 clz 是不是一个MAP类
            // 尝试获取 泛型
            // Type t = clz.getGenericSuperclass();
            // 根据泛型，创建新List
			Set<?> ps = new HashSet();
            return ps;

        } else {
            try {
                return clz.newInstance();
            } catch (InstantiationException e) {
                log.error("LOG00210:", e);
            } catch (IllegalAccessException e) {
                log.error("LOG00230:", e);
            }
        }
        return null;
    }

    /**
     * To val.
     * 通过一个类的String类型的构造函数创建一个实例,支持集合类
     *
     * @param clz
     *         the clz
     * @param value
     *         the value
     * @return the object
     */
    @SuppressWarnings("rawtypes")
    private static Object toClzNewInstance(Class clz, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        //判断 clz 是不是一个集合类
        if (clz.isAssignableFrom(List.class)) {
            // 尝试获取 泛型
            Type t = ((ParameterizedType) clz.getGenericSuperclass()).getActualTypeArguments()[0];
            // 根据泛型，创建新List
            List pl = new ArrayList();
            for (String v : value.split(",")) {
                Object o = toClzNewInstance(t.getClass(), v);
                pl.add(o);
            }
            return pl;

        } else if (clz.isAssignableFrom(Map.class)) {//判断 clz 是不是一个MAP类
            // 尝试获取 泛型
            Type[] types = ((ParameterizedType) clz.getGenericSuperclass()).getActualTypeArguments();
            Type tKey = types[0];
            Type tValue = types[1];
            // 根据泛型，创建新List
            Map pm = new HashMap();
            for (String v : value.split(",")) {
                String[] kv = v.split("=");// key=value 形式的字符串
                if (kv.length > 1) {
                    Object key = toClzNewInstance(tKey.getClass(), kv[0]);
                    Object va = toClzNewInstance(tValue.getClass(), kv[1]);
                    pm.put(key, va);
                }
            }
            return pm;

        } else if (clz.isAssignableFrom(Set.class)) {//判断 clz 是不是一个MAP类
            // 尝试获取 泛型
            Type t = clz.getGenericSuperclass();
            // 根据泛型，创建新List
            Set ps = new HashSet();
            for (String v : value.split(",")) {
                Object o = toClzNewInstance(t.getClass(), v);
                ps.add(o);
            }
            return ps;

        } else {
            return newClzInstance(clz, value);
        }
    }
    /**
     * 通过一个类的String类型的构造函数创建一个实例
     *
     * @param clz
     *         the clz
     * @param value
     *         the value
     * @return the object
     */
    @SuppressWarnings("rawtypes")
    private static Object newClzInstance(Class clz, String value) {
        try {
            Constructor c = clz.getConstructor(String.class);
            return c.newInstance(value);
        } catch (IllegalAccessException e) {
            // ignored
            throw new IllegalArgumentException("class:" + clz.getName() + " can't new a instance, for String Constructor can't be access.", e);
        } catch (InvocationTargetException e) {
            // ignored
            throw new IllegalArgumentException("class:" + clz.getName() + " can't new a instance, for String Constructor can't be access.", e);
        } catch (NoSuchMethodException e) {
            // ignored
            throw new IllegalArgumentException("class:" + clz.getName() + " can't new a instance, for String Constructor is not exist.", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("class:" + clz.getName() + " can't new a instance, for instance can't be init.", e);
        }
    }
    public Filter() {

    }
    /**
     * 这个方法主要是提供给在security.permission分析查询条件时使用，其他代码中尽量使用 public Filter(String propertyAndOperator, Object value)。
     * 这个构造函数会分析字符串，效率会低一些。
     * 创建一个过滤条件,例如：
     * 输入 new Filter(idIn,"1,2,3,4") 相当于 new Filter("id",Filter.Operator.in,new HastSet<Long>())
     * 输入 new Filter(idIn,"1,2,3,4") 相当于 new Filter("id",Filter.Operator.in,new HastSet<Long>())
     *
     * @param propertyAndOperator 属性和操作符的合体,风格是xxxEq,xxxLt
     * @param value the value
     */
    @SuppressWarnings("rawtypes")
    public Filter(String propertyAndOperator, String value, Class propertyClz) {
        this(propertyAndOperator, (Object) value);
        this.value = toClzNewInstance(propertyClz, value);
    }

    /**
     * Instantiates a new Filter.
     *
     * @param propertyAndOperator the property and operator
     * @param value the value
     */
    public Filter(String propertyAndOperator, String value) {
        this(propertyAndOperator, (Object) value);
        // 特殊处理in ,Id
        if(StringUtils.isBlank(value)){
            return;
        }
        if (this.operator.equals(Operator.in)) {
            if (this.property.equalsIgnoreCase("id")) {
                Set<Long> idset = new HashSet<Long>();
                String[] ids = value.split(",");
                for (String id : ids) {
                    if(StringUtils.isNotBlank(id)){
                        idset.add(Long.parseLong(id));
                    }
                }
                this.value = idset;
            } else {
                String[] ids = value.split(",");
                Set<String> idset = new HashSet<String>();
                for (String id : ids) {
                    if (StringUtils.isNotBlank(id)) {
                        idset.add(id);
                    }
                }
                this.value = idset;
            }
        }else if (this.property.equalsIgnoreCase("id")) {
            this.value = Long.parseLong(value);
        }

    }
    /**
     * 创建一个过滤条件
     *
     * @param propertyAndOperator 属性和操作符的合体,风格是xxxEq,xxxLt
     * @param value the value
     */
    public Filter(String propertyAndOperator, Object value) {
        Matcher m = opPatten.matcher(propertyAndOperator);
        if(m.find()){
            this.property = m.group(1);
            this.operator = Operator.fromString(m.group(2));
        }else{
            this.property = propertyAndOperator;
            this.operator = Operator.eq;
        }
        this.value = value;

    }
    /**
     * 创建一个过滤条件
     *
     * @param property the property
     * @param operator the operator
     * @param value the value
     */
    public Filter(String property,String operator, Object value) {
        this.property = property;
        this.operator = Operator.fromString(operator);
        this.value = value;

    }
    
    /**
     * 创建一个过滤条件
     *
     * @param property the property
     * @param operator the operator
     * @param value the value
     * @param root 
     */
    public Filter(String root,String join,String property,String operator, Object value) {
    	this.root = root;
    	this.join = join;
    	this.property = property;
        this.operator = Operator.fromString(operator);
        this.value = value;

    }
    
    public Filter(String root,String join,String property,String operator, String queryProperty,Object value) {
    	this.root = root;
    	this.join = join;
    	this.property = property;
    	this.queryProperty = queryProperty;
        this.operator = Operator.fromString(operator);
        this.value = value;

    }

    /**
     * 创建一个过滤条件
     *
     * @param property the property
     * @param operator the operator
     * @param value the value
     */
    public Filter(String property, Operator operator, Object value) {
        this.property = property;
        this.operator = operator;
        this.value = value;
    }
    
    /**
     * 创建一个过滤条件
     *
     * @param root
     * @param property the property
     * @param operator the operator
     * @param value the value
     */
    public Filter(String root,String join,String property, Operator operator, Object value) {
    	this.root = root;
    	this.join = join;
    	this.property = property;
        this.operator = operator;
        this.value = value;
    }
    
    public Filter(String root,String join,String property, Operator operator,String queryProperty, Object value) {
    	this.root = root;
    	this.join = join;
    	this.property = property;
    	this.queryProperty = queryProperty;
        this.operator = operator;
        this.value = value;
    }

    /**
     * 创建一个过滤条件
     *
     * @param property the property
     * @param operator the operator
     * @param value the value
     * @param ignoreCase the ignore case
     */
    public Filter(String property, Operator operator, Object value,
                  boolean ignoreCase) {
        this.property = property;
        this.operator = operator;
        this.value = value;
        this.ignoreCase = Boolean.valueOf(ignoreCase);
    }
    
    /**
     * 创建一个过滤条件
     *
     * @param root
     * @param property the property
     * @param operator the operator
     * @param value the value
     * @param ignoreCase the ignore case
     */
    public Filter(String root,String join,String property, Operator operator, Object value,boolean ignoreCase) {
    	this.root = root;
    	this.join = join;
    	this.property = property;
        this.operator = operator;
        this.value = value;
        this.ignoreCase = Boolean.valueOf(ignoreCase);
    }
    
    public Filter(String root,String join,String property, Operator operator, String queryProperty,Object value,boolean ignoreCase) {
    	this.root = root;
    	this.join = join;
    	this.property = property;
    	this.queryProperty = queryProperty;
        this.operator = operator;
        this.value = value;
        this.ignoreCase = Boolean.valueOf(ignoreCase);
    }

    /**
     * 转换 Map<String, Object> filters 成 Filter[]
     *
     * @param filters the filters
     * @return the filter [ ]
     */
    public static List<Filter> map2Array(Map<String, Object> filters){
        if (filters == null || filters.isEmpty()) {
            return null;
        }
        List<Filter> rets = new ArrayList<Filter>();
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            Filter f = new Filter(entry.getKey(),entry.getValue());
            rets.add(f);
        }
        return rets;
        //return rets.toArray(new Filter[]{});
    }

    public static Filter eq(String property, Object value) {
        return new Filter(property, Operator.eq, value);
    }

    public static Filter eq(String property, Object value, boolean ignoreCase) {
        return new Filter(property, Operator.eq, value, ignoreCase);
    }

    public static Filter ne(String property, Object value) {
        return new Filter(property, Operator.ne, value);
    }

    public static Filter ne(String property, Object value, boolean ignoreCase) {
        return new Filter(property, Operator.ne, value, ignoreCase);
    }

    public static Filter gt(String property, Object value) {
        return new Filter(property, Operator.gt, value);
    }

    public static Filter lt(String property, Object value) {
        return new Filter(property, Operator.lt, value);
    }

    public static Filter ge(String property, Object value) {
        return new Filter(property, Operator.ge, value);
    }

    public static Filter le(String property, Object value) {
        return new Filter(property, Operator.le, value);
    }

    public static Filter like(String property, Object value) {
        return new Filter(property, Operator.like, value);
    }

    public static Filter in(String property, Object value) {
        return new Filter(property, Operator.in, value);
    }

    public static Filter isNull(String property) {
        return new Filter(property, Operator.isnull, null);
    }

    public static Filter isNotNull(String property) {
        return new Filter(property, Operator.isnotnull, null);
    }
    
    public static Filter eq(String root,String join, String property, Object value) {
        return new Filter(root,join, property, Operator.eq, value);
    }

    public static Filter eq(String root,String join, String property, Object value, boolean ignoreCase) {
        return new Filter(root,join, property, Operator.eq, value, ignoreCase);
    }

    public static Filter ne(String root,String join, String property, Object value) {
        return new Filter(root,join, property, Operator.ne, value);
    }

    public static Filter ne(String root,String join, String property, Object value, boolean ignoreCase) {
        return new Filter(root,join, property, Operator.ne, value, ignoreCase);
    }

    public static Filter gt(String root,String join, String property, Object value) {
        return new Filter(root,join, property, Operator.gt, value);
    }

    public static Filter lt(String root,String join, String property, Object value) {
        return new Filter(root,join, property, Operator.lt, value);
    }

    public static Filter ge(String root,String join, String property, Object value) {
        return new Filter(root,join, property, Operator.ge, value);
    }

    public static Filter le(String root,String join, String property, Object value) {
        return new Filter(root,join, property, Operator.le, value);
    }

    public static Filter like(String root,String join, String property, Object value) {
        return new Filter(root,join, property, Operator.like, value);
    }

    public static Filter in(String root,String join, String property, Object value) {
        return new Filter(root,join, property, Operator.in, value);
    }

    public static Filter isNull(String root,String join, String property) {
        return new Filter(root,join, property, Operator.isnull, null);
    }

    public static Filter isNotNull(String root,String join, String property) {
        return new Filter(root, join, property, Operator.isnotnull, null);
    }
    
    
    public static Filter eq(String root,String join, String property,String queryProperty, Object value) {
        return new Filter(root,join, property, Operator.eq, queryProperty,value);
    }

    public static Filter eq(String root,String join, String property,String queryProperty, Object value, boolean ignoreCase) {
        return new Filter(root,join, property, Operator.eq,queryProperty, value, ignoreCase);
    }

    public static Filter ne(String root,String join, String property,String queryProperty, Object value) {
        return new Filter(root,join, property, Operator.ne, queryProperty,value);
    }

    public static Filter ne(String root,String join, String property,String queryProperty, Object value, boolean ignoreCase) {
        return new Filter(root,join, property, Operator.ne,queryProperty, value, ignoreCase);
    }

    public static Filter gt(String root,String join, String property,String queryProperty, Object value) {
        return new Filter(root,join, property, Operator.gt,queryProperty, value);
    }

    public static Filter lt(String root,String join, String property,String queryProperty, Object value) {
        return new Filter(root,join, property, Operator.lt,queryProperty, value);
    }

    public static Filter ge(String root,String join, String property,String queryProperty, Object value) {
        return new Filter(root,join, property, Operator.ge,queryProperty, value);
    }

    public static Filter le(String root,String join, String property,String queryProperty, Object value) {
        return new Filter(root,join, property, Operator.le,queryProperty, value);
    }

    public static Filter like(String root,String join, String property,String queryProperty, Object value) {
        return new Filter(root,join, property, Operator.like,queryProperty, value);
    }

    public static Filter in(String root,String join, String property,String queryProperty, Object value) {
        return new Filter(root,join, property, Operator.in,queryProperty, value);
    }

    public static Filter isNull(String root,String join, String property,String queryProperty) {
        return new Filter(root,join, property, Operator.isnull, null,queryProperty);
    }

    public static Filter isNotNull(String root,String join, String property,String queryProperty) {
        return new Filter(root, join, property, Operator.isnotnull, null,queryProperty);
    }
    

    public Filter ignoreCase() {
        this.ignoreCase = Boolean.valueOf(true);
        return this;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Boolean getIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Filter localFilter = (Filter) obj;
        return new EqualsBuilder()
                .append(getProperty(), localFilter.getProperty())
                .append(getOperator(), localFilter.getOperator())
                .append(getValue(), localFilter.getValue()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getProperty())
                .append(getOperator()).append(getValue()).toHashCode();
    }
    public String toString(){
        return this.property+" "+this.operator+" "+this.value;
    }
}
