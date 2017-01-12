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
		test();
	}
	
	public static void test() { 
		String secretToken = "CAIS0AN1q6Ft5B2yfSjIpK6GecDk3L5M44+baEXfoUoFZrlhoqryqDz2IHpMdHBuAOAfsfswlWpT6v8alqNWRoVORUXCcY4ptmW+c/BQINivgde8yJBZolLPewHKefKSvqL7Z+H+U6mqGJOEYEzFkSle2KbzcS7YMXWuLZyOj+wIDLkQRRLqL0B0ZrFsKxBltdUROFbIKP+pKWSKuGfLC1dysQcO3wEL4K+kkMqH8Uic3h+oyO4RusHtL4O4KMJrOZJ4VbXD1edtJK3ay3wSuVoY9qZykelY9Czb+dqaBlxW5VLEQRi9kLBGKAt+edJ/ecptp+T9kvtVo/Hak5+NpT9GIeBSVVaiJuCn29CWN+61dLRYcq3gaVDMyczNTJOX1TkpenUGLghHVsM8I3trc3wWRyrdN7Ws9SLkAE+qQLPX17otg9gnjQfq/N6XYkSCWKne1CsCMZ81aV9vbnxc1Gf6IKgdaF4OIRE1BbeRUMBvYQtC7qblpwbSXyt8iSgQ/d+GPq+G4vlCN9ShA80ajtdFOK4r6TV6Ew7FLJu1kVoReWBfRrJb7bLgI5fX6sXemr3DOLCfWqlY5QoLK2uN9nKXKHFRKTC0+tA4dUpAZTIIOgJ0mRqAAX8qkPNHORRNcPT5xyR1amPvohV4CZFFq0LmcHUfTgQLckltDRd1LK/Y9hlOdeKZYmTKCItVrCVEJpVdEoBZOvsjR9Xonbu0Ce1DwMc9BF4tFjSFMGlSLQ7D7DxmQPRpKm2HckWRahTF6a4hL0wzjD/5cWv1rd5iXMA7S98AgzcO";
		OSSClient ossClient = new OSSClient("http://oss-cn-shanghai.aliyuncs.com", "STS.Em32zP1amTMpjcnEJPj6MMoYJ", "FrzbxDkqPFTzRexZVNaZfpVWEaPdj6qfPuiKAaFp264h", secretToken);
		ossClient.putObject("jilu-test", "common/9", new ByteArrayInputStream(new byte[0]));
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
