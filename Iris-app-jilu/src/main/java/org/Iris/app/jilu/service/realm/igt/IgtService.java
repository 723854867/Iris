package org.Iris.app.jilu.service.realm.igt;
import java.util.List;

import javax.annotation.Resource;

import org.Iris.app.jilu.service.realm.igt.domain.TransmissionInfo;
import org.Iris.app.jilu.storage.domain.MemCid;
import org.Iris.igt.push.AppPushManager;
import org.Iris.util.common.JsonAppender;
import org.springframework.stereotype.Service;
/**
 * 个推服务类
 * @author 樊水东
 * 2017年2月8日
 */
@Service
public class IgtService {
	
	@Resource
	private AppPushManager appPushManager;

//	/**
//	 * 推送给单个目标
//	 * @param cid
//	 * @param title
//	 * @param text
//	 */
//	public void pushToSingle(String cid,String title,String content,String text){
//		appPushManager.pushToSingle(cid, title,content, text);
//	}
//	/**
//	 * 推送给一个列表
//	 * @param cids
//	 * @param title
//	 * @param text
//	 */
//	public void pushToList(List<String> cids,String title,String content,String text){
//		appPushManager.pushToList(cids, title,content, text);
//	}
//	/**
//	 * 推送给整个应用
//	 * @param title
//	 * @param text
//	 */
//	public void pushToApp(String title,String content,String text){
//		appPushManager.pushToApp(title,content, text);
//	}
	
	/**
	 * 推送给单个目标
	 * @param cid
	 * @param info
	 */
	public void pushToSingle(MemCid memCid,TransmissionInfo info){
		if(null != memCid)
			appPushManager.pushToSingle(memCid.getClientId(), info.getParam().getTitle(),info.getParam().getContent(), JsonAppender.toJson(info));
	}
	/**
	 * 推送给一个列表
	 * @param cids
	 * @param info
	 */
	public void pushToList(List<String> cids,TransmissionInfo info){
		appPushManager.pushToList(cids, info.getParam().getTitle(),info.getParam().getContent(), JsonAppender.toJson(info));
	}
	/**
	 * 推送给整个应用
	 * @param info
	 */
	public void pushToApp(TransmissionInfo info){
		appPushManager.pushToApp(info.getParam().getTitle(),info.getParam().getContent(), JsonAppender.toJson(info));
	}
	
}
