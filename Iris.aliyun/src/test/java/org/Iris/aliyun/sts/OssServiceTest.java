package org.Iris.aliyun.sts;

import java.io.ByteArrayInputStream;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;

public class OssServiceTest {

	public static void main(String[] args) {
//		OssService ossService = new OssService();
//		ossService.setAccessKeyId("LTAIx81L9s0GzPtA");
//		ossService.setAccessKeySecret("1CeO2BG6ackYKosN5efi8bjR7JgUsr");
//		ossService.setEndpoint("http://oss-cn-shanghai.aliyuncs.com");
//		ossService.init();
//		ossService.createFolder("jilu-test", "test/name/");
		
//		test();
//		testPolicy();
	}
	
	public static void test() { 
		String secretToken = "CAIS7AF1q6Ft5B2yfSjIqonHGsz+lO5A0vbTd2XHgEYwSOt4jLbvrzz2IHpMdHBuAOAfsfswlWpT6v8alq1vRoRZHdZhqwyIrcY5yxioRqacke7XhOV2pf/IMGyXDAGBr622Su7lTdTbV+6wYlTf7EFayqf7cjPQND7Mc+f+6/hdY88QQxOzYBdfGd5SPXECksIBMmbLPvvfWXyDwEioVRY44lMh1zIhtPvjnZDEtiCz1gOqlrUnwK3qOYWhYsVWO5Nybsy4xuQedNCainYAskYTpP4p0/MVo2yb54mHYltQ5wjDL+fP7s23svmgkAaCcRqAAZPrC2ZEXIL+b75Zr6rDCx7CVXiJwFjSzM6y4r9Nuj2+z0+5nVa1Flbqf5mohDSt1phmVJ0TL/GElZjjYpEYOIWMDMfCBohxwZV7479GdHiwxll39ahBOa5OLmU2Hd8OhZ1taAYt+skmGyZfUZzuVVoUzn2hnvyugLxWPFgqDYAp";
		OSSClient ossClient = new OSSClient("http://oss-cn-shanghai.aliyuncs.com", "STS.KJrQvJy1ae48uCvdFeDdTcsDM", "ZZmKMJg7ducprZEwaT3YTvWBGZLFGAg157HkL67SPxr", secretToken);
		ossClient.putObject("jilu-test", "common/nams/", new ByteArrayInputStream(new byte[0]));
	}
	
	public static void testPolicy() {
		String secretToken = "CAISzAN1q6Ft5B2yfSjIrLXgefTBgq5pwqexeB/yjmM2aPdeh6/IpTz2IHpMdHBuAOAfsfswlWpT6v8alq1vRoRZHYds5gyIrcY5yxioRqackQ3fj9Vd+mfMewW6Dxr8w7WXAYHQR8/cffGAck3NkjQJr5LxaTSlWS7YU/yOkoU1StUMQkvaDT1dH4V5Kxcg0v14U0HcLvGwKBXnr3PNBU5zwGpGhHh49L60z7/KiG7Xh0aozfQO9cajYMqmbs1xeYtySMvy1+tzc63HslhZ4AMY87xt3u4Wu2XFv9WARkVc+AnBOO3R99ppa00LgMFYdodAqP/j74oa3sXYi4XwzTtUIOhRSF6xbYu8wczCaozLW9EibuS5QXivtOqCLZ7oqQgpTGsGPQdRAbobJ2R3FAYnR0P6SOau80uYZRy4GerXkvM00J5oiknl5sbNLF+KRrKfljoVIYNeJEojLEwRxnezMP1EIQ5Nd04iG7uOUcJ4axVbrqWvoQDJSmoi7AkO462mPq6I6/5CNtmgA8oa6+dHOsQa6VlNZk/sVrejhn0TcGFYWrtM2MHvQ8TlsO/cnrzDOLGZVaFd4QkDKSqwrSuJVDZMIDbt9gW9n4NuVXy7GoABNFu5iEIGDBuTcvy6TVjeNn+EkV4cEthjHX1L4GpYZ4VWsdjvcm9m/+fnrPuxBpvm05UR/IAfJZgt3fNFOLYwiX/7f11SnDv0/IsaxC58TiQns7Ir6hTFpSiHIZkwrVbTbnioELs/fp9X/dyvZpydq48e9qypypSA2JFBMsYK6Po=";
		OSSClient ossClient = new OSSClient("http://oss-cn-shanghai.aliyuncs.com", "STS.MvU2NuoqHueZz9CjccdxrhjcG", "9L5MWRG22AxJf5gb6Wjc6kGpsPHLzq7KfYuy3eyxMLc9", secretToken);
		try {
			ossClient.putObject("jilu-test", "common/test/nams/", new ByteArrayInputStream(new byte[0]));
		} catch (Exception e) {
			System.out.println("put common/test/nams/ failure");
		}
//		
//		try {
//			ossClient.putObject("jilu-test", "common/nams/", new ByteArrayInputStream(new byte[0]));
//		} catch (Exception e) {
//			System.out.println("put common/nams/ failure");
//		}
//		
		OSSObject object = ossClient.getObject("jilu-test", "common/test/nams/");
		System.out.println(object.getKey());
		object = ossClient.getObject("jilu-test", "common/test/nams/");
	}
}
