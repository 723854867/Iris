package org.Iris.igt;

import org.Iris.igt.push.AppPushManager;

import com.gexin.rp.sdk.base.IPushResult;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	
	public void testPush() {
		AppPushManager appPush = new AppPushManager();
		appPush.setAppId("c5VCMF5JOK8gSqD6tbXYe1");
		appPush.setAppKey("uHtRPpbwtP9nFhPFb3iG53");
		appPush.setMasterSecret("lDqk89YoFX5ytzaQeyE9l7");
		appPush.setUrl("http://sdk.open.api.igexin.com/apiex.htm");
		
		appPush.init();
		
		IPushResult result = appPush.pushToApp("title", "服务端test");
	}
}
