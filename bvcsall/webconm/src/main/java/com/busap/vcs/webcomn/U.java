package com.busap.vcs.webcomn;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.service.security.SecurityUtils;

/**
 * 
 * @author meizhiwen
 *
 */
public class U {

	private static final Logger logger = LoggerFactory.getLogger(U.class);
	/**
	 * 将数据库查出来的List<BaseEntity>>数据结构转换成List
	 * @param list
	 * @param keyName
	 * @return
	 */
	public static <T> List<String> parseListMapToList(List list,Class<T> classes,String keyName) {
		List<String> strList = new ArrayList<String>();
		if(list!=null&&!list.isEmpty()&&list.size()>0){
			for (Object o : list) {
				T map =(T)o;
				try {
					String value=BeanUtils.getProperty(map, keyName);
					strList.add(value);
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		}
		return strList;
	}
	
	public static String nvl(Object o) {
		if(o==null){
			return "";
		}
		if(StringUtils.isNotBlank(o.toString())){
			return o.toString();
		}
		return "";
	}
	
	public static String nvl(String o) {
		if (!StringUtils.isBlank(o)) {
			return o.trim();
		}
		return "";
	}
	
	public static int nvl(String o,int defaultValue) {
		if (!StringUtils.isBlank(o)) {
			return Integer.parseInt(o);
		}
		return defaultValue;
	}
	
	public static Session getSession(){
		return SecurityUtils.getSubject().getSession();
	}
	
	public static Object getAttribute(String key){
		return getSession().getAttribute(key);
	}
	
	public static void setAttribute(String key,Object value){
		getSession().setAttribute(key,value);
	}
	
	public static Long getUid(){
		Long suid = getAttribute(Constants.SESSION_UID)==null?0L:(Long)getAttribute(Constants.SESSION_UID);
		return suid;
	}
	
	public static String getUname(){
		String sname = getAttribute(Constants.SESSION_UNAME)==null?"":getAttribute(Constants.SESSION_UNAME).toString(); 
		return sname;
	}
	 /**
     * 获取单个字符串的MD5值
     * 
     * @param String
     * @return
     */
    public static String getStringMD5(String text) {
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(text.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
