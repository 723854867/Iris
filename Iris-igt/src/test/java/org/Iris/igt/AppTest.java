package org.Iris.igt;

import org.Iris.igt.push.AppPushManager;

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
		
		//appPush.pushToApp("", "服务端test");
		appPush.pushToSingle("25e75e1ee094be59c32cb3fe066f046e","", "服务端单推");
	}
}
