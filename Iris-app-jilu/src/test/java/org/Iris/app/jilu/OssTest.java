package org.Iris.app.jilu;

import java.io.ByteArrayInputStream;
import java.io.File;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;

public class OssTest {

	public static void main(String[] args) {
		// endpoint以杭州为例，其它region请按实际情况填写
		String endpoint = "http://oss-cn-shanghai.aliyuncs.com/";
		// 临时账号accessKeyId/accessKeySecret/securityToken
		String accessKeyId = "STS.ENRYvGq74ao28bLaeRSWSqQuC";
		String accessKeySecret = "2qaPiFLJQbFozK3qLyFgmyCQgKV8r2oWNoDSiLQ48eAM";
		String token = "CAIS8QF1q6Ft5B2yfSjIpI3nEszznOgV1q3ZOkT9hWUHX9h/npTeoTz2IHpMdHBuAOAfsfswlWpT6v8alqB6T55OSAmcNZIocVW/CfviMeT7oMWQweEuqv/MQBq+aXPS2MvVfJ+KLrf0ceusbFbpjzJ6xaCAGxypQ12iN+/i6/clFKN1ODO1dj1bHtxbCxJ/ocsBTxvrOO2qLwThjxi7biMqmHIl1TMnsPnhk5PBtUaB0QWm8IJP+dSteKrDRtJ3IZJyX+2y2OFLbafb2EZSkUMQrPcv0vUUoGiY4YDHWAUIuw/nMevQ75hzIRRldnCk7XeyAkCWGoABVCjUykLT4jH6Su1aFpwqcAe7EGu08fajpPi65/AHpcxVc9MseADB4rjH2Fu/k3fAnfF5RC+GgLK9ZlflM3c/jJojyvXTdvNcBN/wztO2PiTrOx0lmHFSBq1rFrs1V/FhbGs0hcRctaO+oJS7vR4PYprLgnZ+Y6sCca2raDiZ5To=";
		// 创建OSSClient实例
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret, token);
		// 使用访问OSS
		client.putObject("jilu-test", "common/test/sd/", new ByteArrayInputStream(new byte[0]));
		// 关闭client
		client.shutdown();
	}
}
