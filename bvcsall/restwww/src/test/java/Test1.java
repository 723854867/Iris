import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.busap.vcs.utils.AES128Common;

public class Test1 {
	public static void main(String s[]){
		try {   
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost =  new HttpPost("http://localhost/restwww/favorite/findFavoriteList");
			//填入各个表单域的值
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("page","1"));
			params.add(new BasicNameValuePair("size", "10"));
			httppost.setHeader("authKey", AES128Common.encrypt("18601239224|e8ed21b0a055ae57"));
			httppost.setHeader("uid", "100006");
			//添加参数
			httppost.setEntity(new UrlEncodedFormEntity(params));
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
