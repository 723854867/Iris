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

public class OssServiceTest {

	public static void main(String[] args) throws IOException {
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
//		System.out.println(new String(b,0,len));
	    get();
	}

	public static void test() throws FileNotFoundException {
		String secretToken = "CAIS4QN1q6Ft5B2yfSjIqrDEIMKAuLZx5POncXLwgUwhes5CjYud1jz2IHpMdHBuAOAfsfswlWpT6v8alqJWRoVORUXCcY4qssyAKLUUqMmT1fau5Jko1bd6cwr6UmwXta2/SuH9S8ynDZXJQlvYlyh17KLnfDG5JTKMOoGIjpgVAbZ6WRKjP3g8NrUwHAZ5r9IAPnb8LOukNgWQ4lDdF011oAFx+zUdna202Z+b8QGMzg+4mO0Uo5m0PpW/c45nO5J6VNHXquV9bfjGyzUCqUoIpqV3iLEB5TrcopaWBFxU5BPBcZgWm78CBQt9YbdAXswghfPglPp+l/XOnoDssXhkNv1UVCKtOeLAx9DfSsykKpRbFLHgICbp08uTdOPv0WUebGkcKRlBdvc4N3Z0EmZMcDzBLbK98124XGXjQqOegqYtysg3nRe6/9uOOh2TTq6Fli0fP5o7aQQtOgQd3WXnbuhcL24Oelh8VbGOVtcxPlIMq6LsoEjOWzZ8iXpQuvj5YbTTt7oUaYz5QtUcjc1fRuwf7ztyFgmpGuzz1RtLKTQ5e9sMjvmxY63YwaSe3eCebdTBDv06oVhAeVjT1XHXESMJc3Shv4J8NgvCoJiIlLat8ZJkDU4y68tDHdkbhw+bAeQMGoABAj8Rh7F4pXNhJsdRricy+AS1jhNYEmzrGzHKu4a3BviBjUfSKL2vxEZl05Epz92lf8e+8X0o04QgW1bu4hBN5/9Vrwa5GX1beUjHasqXeo4YB2jfa0RJ9RNVOBOG9NJ/6QKzyFNBtV6FkhKO3v8SPLL/WsD3o5WrzbJraReLfV0=";
		OSSClient ossClient = new OSSClient("http://oss-cn-shanghai.aliyuncs.com", "STS.Ksqkx4UiPS1LsTAeLtvAnbN64",
				"GMiJBgAm9puvu7hkDEmpt5Qvd3ii8ULx7uiXhWucs4UP", secretToken);
		
		InputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\2.jpg");
		
		ossClient.putObject("jilu-test", "common/merchant/test/fsd.jpg", inputStream);
	}
	
	public static void get(){
		String secretToken = "CAIS4QN1q6Ft5B2yfSjIqrDEIMKAuLZx5POncXLwgUwhes5CjYud1jz2IHpMdHBuAOAfsfswlWpT6v8alqJWRoVORUXCcY4qssyAKLUUqMmT1fau5Jko1bd6cwr6UmwXta2/SuH9S8ynDZXJQlvYlyh17KLnfDG5JTKMOoGIjpgVAbZ6WRKjP3g8NrUwHAZ5r9IAPnb8LOukNgWQ4lDdF011oAFx+zUdna202Z+b8QGMzg+4mO0Uo5m0PpW/c45nO5J6VNHXquV9bfjGyzUCqUoIpqV3iLEB5TrcopaWBFxU5BPBcZgWm78CBQt9YbdAXswghfPglPp+l/XOnoDssXhkNv1UVCKtOeLAx9DfSsykKpRbFLHgICbp08uTdOPv0WUebGkcKRlBdvc4N3Z0EmZMcDzBLbK98124XGXjQqOegqYtysg3nRe6/9uOOh2TTq6Fli0fP5o7aQQtOgQd3WXnbuhcL24Oelh8VbGOVtcxPlIMq6LsoEjOWzZ8iXpQuvj5YbTTt7oUaYz5QtUcjc1fRuwf7ztyFgmpGuzz1RtLKTQ5e9sMjvmxY63YwaSe3eCebdTBDv06oVhAeVjT1XHXESMJc3Shv4J8NgvCoJiIlLat8ZJkDU4y68tDHdkbhw+bAeQMGoABAj8Rh7F4pXNhJsdRricy+AS1jhNYEmzrGzHKu4a3BviBjUfSKL2vxEZl05Epz92lf8e+8X0o04QgW1bu4hBN5/9Vrwa5GX1beUjHasqXeo4YB2jfa0RJ9RNVOBOG9NJ/6QKzyFNBtV6FkhKO3v8SPLL/WsD3o5WrzbJraReLfV0=";
		OSSClient ossClient = new OSSClient("http://oss-cn-shanghai.aliyuncs.com", "STS.Ksqkx4UiPS1LsTAeLtvAnbN64",
				"GMiJBgAm9puvu7hkDEmpt5Qvd3ii8ULx7uiXhWucs4UP", secretToken);
		ossClient.getObject(new GetObjectRequest("jilu-test", "common/merchant/test/fsd.jpg"),new File("C:\\Users\\Administrator\\Desktop\\4.jpg"));
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
