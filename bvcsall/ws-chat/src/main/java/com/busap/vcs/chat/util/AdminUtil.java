package com.busap.vcs.chat.util;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.chat.bean.Base;

public class AdminUtil {
	private static Logger logger = LoggerFactory.getLogger(AdminUtil.class);
	//后台管理链接
	public static Map<Channel,String> adminChannel = new ConcurrentHashMap<Channel, String>();
	
	/**
	 * 管理员链接服务
	 * @param chn
	 * @param base
	 * @return
	 */
	public static boolean addAdmin(Channel chn,Base base){
		if(chn == null || !chn.isOpen()){
			logger.info("admin长连接用户加入时已关闭!");
			return false;
		}
		String uid = base.getParams().get("uid");
		if(StringUtils.isNotBlank(uid)){
			adminChannel.put(chn, uid);
			logger.info("管理员 {} 链接成功.",uid);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 管理员退出服务
	 * @param chn
	 * @return
	 */
	public static boolean removeAdmin(Channel chn){
		adminChannel.remove(chn);
		if(chn.isOpen()){
			chn.close();
		}
		logger.info("admin user closed.");
		return true;
	}
}
