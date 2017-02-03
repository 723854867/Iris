package org.Iris.aliyun.sts;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

public class OssServiceTest {

	public static void main(String[] args) throws IOException {
		String secretToken = "CAIS4QN1q6Ft5B2yfSjIp4eNG8zEoZ5QgaWAYW3Ds0Q+RvhvgoLZlzz2IHpMdHBuAOAfsfswlWpT6v8alqJWRoVORUXCcY4qssyXKphskMmT1fau5Jko1bd6cwr6UmwXta2/SuH9S8ynDZXJQlvYlyh17KLnfDG5JTKMOoGIjpgVAbZ6WRKjP3g8NrUwHAZ5r9IAPnb8LOukNgWQ4lDdF011oAFx+zUdna202Z+b8QGMzg+4mO0Uo5m0PpW/c45nO5J6VNHXquV9bfjGyzUCqUoIpqV3iLEB5TrcopaWBFxU5BPBcZgWm78CBQt9YbdAXswghfPglPp+l/XOnoDssXhkNv1UVCKtOeLAx9DfSsykKpRbFLHgICbp08uTdOPv0WUebGkcKRlBdvc4N3Z0EmZMcDzBLbK98124XGXjQqOegqYtysg3nRe6/9uOOh2TTq6Fli0fP5o7aQQtOgQd3WXnbuhcL24Oelh8VbGOVtcxPlIMq6LsoEjOWzZ8iXpQuvj5YbTTt7oUaYz5QtUcjc1fRuwf7ztyFgmpGuzz1RtLKTQ5e9sMjvmxY63YwaSe3eCebdTBDv06oVhAeVjT1XHXESMJc3Shv4J8NgvCoJiIlLat8ZJkDU4y68tDHdkbhw+bAeQMGoABjTXop89V+EQYTA+jCXXWjT6CBA1qO8q+S+6yiUeypbyqZHepX1kDqXtPsBO1Bbpb35q+1XF5FXtRV1mCBQRHsmCjCzat0pNojHtSSHpVy2QLrm+1gBVZXDDCD6DOXO8jzAgtizAkYUGTl+ueFDyQgNMKtn3ESR5l6MRSdXOWqN0=";
		OSSClient ossClient = new OSSClient("http://oss-cn-shanghai.aliyuncs.com", "STS.FD8PvpLAq6gkcKrWDkJwCmGru",
				"FZx5N65rLKBcaBAKkwKHbVrrQboRY9bL3QpiN6YEdnt3", secretToken);
		ossClient.getObject(new GetObjectRequest("jilu-test", "common/merchant/24/"),new File("C:\\Users\\Administrator\\Desktop\\3.jpg"));
//		InputStream in = object.getObjectContent();
//		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\Administrator\\Desktop\\3.jpg")));
//		System.out.println(bufferedReader.readLine());
//		byte[] b = new byte[1024*100];
//		int temp = 0;
//		int len = 0;
//		while ((temp = in.read()) != -1) {// -1为文件读完的标志
//			writer.write(temp);
//		}
		//System.out.println(new String(b,0,len));
	    test();
	}

	public static void test() throws FileNotFoundException {
		String secretToken = "CAIS4QN1q6Ft5B2yfSjIp4eNG8zEoZ5QgaWAYW3Ds0Q+RvhvgoLZlzz2IHpMdHBuAOAfsfswlWpT6v8alqJWRoVORUXCcY4qssyXKphskMmT1fau5Jko1bd6cwr6UmwXta2/SuH9S8ynDZXJQlvYlyh17KLnfDG5JTKMOoGIjpgVAbZ6WRKjP3g8NrUwHAZ5r9IAPnb8LOukNgWQ4lDdF011oAFx+zUdna202Z+b8QGMzg+4mO0Uo5m0PpW/c45nO5J6VNHXquV9bfjGyzUCqUoIpqV3iLEB5TrcopaWBFxU5BPBcZgWm78CBQt9YbdAXswghfPglPp+l/XOnoDssXhkNv1UVCKtOeLAx9DfSsykKpRbFLHgICbp08uTdOPv0WUebGkcKRlBdvc4N3Z0EmZMcDzBLbK98124XGXjQqOegqYtysg3nRe6/9uOOh2TTq6Fli0fP5o7aQQtOgQd3WXnbuhcL24Oelh8VbGOVtcxPlIMq6LsoEjOWzZ8iXpQuvj5YbTTt7oUaYz5QtUcjc1fRuwf7ztyFgmpGuzz1RtLKTQ5e9sMjvmxY63YwaSe3eCebdTBDv06oVhAeVjT1XHXESMJc3Shv4J8NgvCoJiIlLat8ZJkDU4y68tDHdkbhw+bAeQMGoABjTXop89V+EQYTA+jCXXWjT6CBA1qO8q+S+6yiUeypbyqZHepX1kDqXtPsBO1Bbpb35q+1XF5FXtRV1mCBQRHsmCjCzat0pNojHtSSHpVy2QLrm+1gBVZXDDCD6DOXO8jzAgtizAkYUGTl+ueFDyQgNMKtn3ESR5l6MRSdXOWqN0=";
		OSSClient ossClient = new OSSClient("http://oss-cn-shanghai.aliyuncs.com", "STS.FD8PvpLAq6gkcKrWDkJwCmGru",
				"FZx5N65rLKBcaBAKkwKHbVrrQboRY9bL3QpiN6YEdnt3", secretToken);
		
		InputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\2.jpg");
		
		ossClient.putObject("jilu-test", "common/merchant/24", inputStream);
	}

	public static void testPolicy() {
		String secretToken = "CAISzAN1q6Ft5B2yfSjIrLXgefTBgq5pwqexeB/yjmM2aPdeh6/IpTz2IHpMdHBuAOAfsfswlWpT6v8alq1vRoRZHYds5gyIrcY5yxioRqackQ3fj9Vd+mfMewW6Dxr8w7WXAYHQR8/cffGAck3NkjQJr5LxaTSlWS7YU/yOkoU1StUMQkvaDT1dH4V5Kxcg0v14U0HcLvGwKBXnr3PNBU5zwGpGhHh49L60z7/KiG7Xh0aozfQO9cajYMqmbs1xeYtySMvy1+tzc63HslhZ4AMY87xt3u4Wu2XFv9WARkVc+AnBOO3R99ppa00LgMFYdodAqP/j74oa3sXYi4XwzTtUIOhRSF6xbYu8wczCaozLW9EibuS5QXivtOqCLZ7oqQgpTGsGPQdRAbobJ2R3FAYnR0P6SOau80uYZRy4GerXkvM00J5oiknl5sbNLF+KRrKfljoVIYNeJEojLEwRxnezMP1EIQ5Nd04iG7uOUcJ4axVbrqWvoQDJSmoi7AkO462mPq6I6/5CNtmgA8oa6+dHOsQa6VlNZk/sVrejhn0TcGFYWrtM2MHvQ8TlsO/cnrzDOLGZVaFd4QkDKSqwrSuJVDZMIDbt9gW9n4NuVXy7GoABNFu5iEIGDBuTcvy6TVjeNn+EkV4cEthjHX1L4GpYZ4VWsdjvcm9m/+fnrPuxBpvm05UR/IAfJZgt3fNFOLYwiX/7f11SnDv0/IsaxC58TiQns7Ir6hTFpSiHIZkwrVbTbnioELs/fp9X/dyvZpydq48e9qypypSA2JFBMsYK6Po=";
		OSSClient ossClient = new OSSClient("http://oss-cn-shanghai.aliyuncs.com", "STS.MvU2NuoqHueZz9CjccdxrhjcG",
				"9L5MWRG22AxJf5gb6Wjc6kGpsPHLzq7KfYuy3eyxMLc9", secretToken);
		try {
			ossClient.putObject("jilu-test", "common/test/nams/", new ByteArrayInputStream(new byte[0]));
		} catch (Exception e) {
			System.out.println("put common/test/nams/ failure");
		}
		//
		// try {
		// ossClient.putObject("jilu-test", "common/nams/", new
		// ByteArrayInputStream(new byte[0]));
		// } catch (Exception e) {
		// System.out.println("put common/nams/ failure");
		// }
		//
		OSSObject object = ossClient.getObject("jilu-test", "common/test/nams/");
		System.out.println(object.getKey());
		object = ossClient.getObject("jilu-test", "common/test/nams/");
	}
}
