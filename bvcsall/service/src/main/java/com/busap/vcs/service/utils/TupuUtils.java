package com.busap.vcs.service.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by busap on 2016/4/25.
 */
public class TupuUtils {

    //密钥
    private static final String secretId = "5715cd97616106484771b0d7";

    //图普接口请求地址
    private static final String tupuUrl = "http://api.open.tuputech.com/v3/recognition/" + secretId;

    public static String sendScreenshotToTupu(String image){
        String timestamp = Math.round(System.currentTimeMillis() / 1000.0) + "";
        String nonce = Math.random() + "";
        //生成签名
        String signature = TupuSecurityUtil.createSignature(timestamp,nonce,secretId);
        //参数
        Map<String,String> postParams = new HashMap<String, String>();
        postParams.put("image",image);
        postParams.put("timestamp",timestamp);
        postParams.put("nonce",nonce);
        postParams.put("signature",signature);
        String result = HttpPostUtils.doHttpPost(tupuUrl,postParams);
        return result;
    }

}
