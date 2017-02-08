package org.Iris.app.jilu.service.realm.igt;
import java.util.List;

import javax.annotation.Resource;

import org.Iris.igt.push.AppPushManager;
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

	/**
	 * 推送给单个目标
	 * @param cid
	 * @param title
	 * @param text
	 */
	public void pushToSingle(String cid,String title,String text){
		appPushManager.pushToSingle(cid, title, text);
	}
	/**
	 * 推送给一个列表
	 * @param cids
	 * @param title
	 * @param text
	 */
	public void pushToList(List<String> cids,String title,String text){
		appPushManager.pushToList(cids, title, text);
	}
	/**
	 * 推送给整个应用
	 * @param title
	 * @param text
	 */
	public void pushToApp(String title,String text){
		appPushManager.pushToApp(title, text);
	}
}
