import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.busap.vcs.utils.AES128Common;

public class Test {
	public static void main(String s[]){
		try {  
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost =  new HttpPost("http://www.wopaitv.com/restwww/user/uploadHeaderPic");
			//填入各个表单域的值
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("authKey",AES128Common.encrypt("18010050057|7f73ccb2229cec8e")));
			params.add(new BasicNameValuePair("uid", "12"));
			httppost.setHeader("uid", "11");
			//添加参数
			httppost.setEntity(new UrlEncodedFormEntity(params));
			httppost.addHeader("access_token", "");
			//设置编码
			HttpResponse httpresponse=httpclient.execute(httppost); 
			// 获取返回数据
			int statusCode = httpresponse.getStatusLine().getStatusCode() ;
			HttpEntity entity = httpresponse.getEntity();
            String result = EntityUtils.toString(entity); 
            System.out.println("请求返回值:" + result); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
