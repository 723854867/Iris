package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.IdentifyInfo;
import com.busap.vcs.data.vo.CustPicResponse;
import com.busap.vcs.service.RealAuthService;
import com.busap.vcs.service.bean.ResInObject;
import com.busap.vcs.service.utils.Base64;
import com.busap.vcs.service.utils.HttpsUtil;
import com.busap.vcs.service.utils.JsonUtil;
import com.busap.vcs.service.utils.encrypt.EncryptUtil;
import com.busap.vcs.service.utils.encrypt.RealNameMsDesPlus;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * 实名查验Api
 * Created by Knight on 16/5/30.
 */
@Service("realAuthService")
public class RealAuthServiceImpl implements RealAuthService {

    private Logger logger = LoggerFactory.getLogger(RealAuthServiceImpl.class);
    
    //正式地址
//    private final String custPicIdentifyURL = "https://211.138.24.191:24200/smz-resapi/restservice/custPicIdentify/";
//    private final String custInfoPicVerifyURL = "https://211.138.24.191:24200/smz-resapi/restservice/custInfoPicVerify/";
//    private final String cusPicCompareURL = "https://211.138.24.191:24200/smz-resapi/restservice/cusPicCompare/";
    
    //测试地址
    private final String custPicIdentifyURL = "https://211.138.24.191:20138/smz-resapi/restservice/custPicIdentify/";
    private final String custInfoPicVerifyURL = "https://211.138.24.191:20138/smz-resapi/restservice/custInfoPicVerify/";
    private final String cusPicCompareURL = "https://211.138.24.191:20138/smz-resapi/restservice/cusPicCompare/";
    // HTTP连接超时的时间 秒
    private String connection_timeout = "30";
    // 从响应中读取数据超时的时间
    private String so_timeout = "30";
    
   //正式
    // 提交方式
//    private String method = "POST";
//
//    private String userId = "kliokj";
//
//    private String userPwd = "o5Ty@sHdg";
//
//    private String requestSource = "791215";
//
//    private String signatureKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCnrC+GomfS54VNWKD2XbVboFPzGti6ES0jGjo4hdEdoaniuOv9iArrRDPdNKB6Zt4FiWFsj90i3a5uIc5bhRV9rTHFki5W5honpjyyH/ZVpWaEa02UerftS9EfA+b/QI0Q1zBS5nAzsgzydcQ7Bs8loKY4+7sWwfbRvjQKOg6bst6XsIxx1/2wcujOsWisRgHtkvF1TdxM92ZnsUJSq40QE6DcSWiMAVCu0xD/yIoTfZw7GerodS5BTb7jRmaVrs/7DrP/At+nYQDmee2nfDZApfKqeZy/fBc4ld+gsddHPV63Ttlwr0p5SVLscMZ6MgBFbrYEaw288r3CcdFrBnpPAgMBAAECggEAZ5IioUV8YHiGjOpsMwJbXkfA03AH45dsjDxNLlDqhSUxLU0Q6MC6De1JJCC36CPWjRD5TvfmnvzUHE9hioLAbasoDWRon2DvUwKKh3Ta0riihLVLbKYJKtkkScY9T/ptqLNjtDGHqUcYXESGe/eOyS1cv+IvBLW3YoPc3VxTXbS1bohhIGP+pJBuyhbyw7AUjmmbxlIokRVr5ANkQwjWpjYCEh0+JNE4mKiGD/w4gRXltIvQVYMgfacfRoF29kpGfS5whRlCF4lMLUiZFYkpRwUhLVtmGQ+03UTzILXVdt0PMpofKCZsMnlN0s73nIUyKijlIZZhZoKHFMa6NuO/kQKBgQDTG5aaXgShOntvDaMEoYV76nvSEp5gMcwsAWZgD685hZ7cqXN1zvfTzG1VDLVLeCU5nV/gdbuXAcPOAFGrjkMk8GXA1xt+escW4WAcjJCvfN2QJAE0LYMeTlx55Y+qpgpuTKIA2puAdtrL2hpxJWZfWVQ2Ry6IASfGJVh6YpX0owKBgQDLVAwv61iikBo8O6Av12KDKcp6G23p/kTow0623XTY5zJgnFIvFGkh9iiUF/3hMjDLEd9lcHBsqKfLyyLIqiY9lINMwR5DO3qJilafx1fZEv34syNU3/8uyZtzogJgjmn7KBo/0G+bw6O781U+M7eDvFboi/okPYHNsPg1q8iSZQKBgQCSUQnF/in5UV5h7zKwr/6jjza4uDza8UjQqzu4ksZg+PLNgAjNe2CbhJG2o/M31pL+Zswf95nIJOoAaoowfI6fGJt919lvhUtd2WFTCD3z03iBQeMvteet5qQDvI8huKl8H1MdgmbK2BX3ujgfecuFhJVwdU+T5jEaDaJQWOoUPwKBgQC4ZAMdXdemrPotz4DogfJDsp19eOyHvGBvmdcDfPffUGNeO8VIDsF83AhWrR8+n06iihXGn15FG/n3lcN4qN0/ETS1YZj5LbJlkwAVrCjdohyR8prC+NQpcQm5pp1zRROVgfu0DWl5n8UXyIdwfmQZYq6J+48gWGsjxy6BNcUSBQKBgG0WIHFhRXv8y+Ssp3DQJcjjBJ4PjD4WYfbWcmTeujTB55pj5TN5d9ZV+BK+disBikemgAJosn7LMB7/ybkARMCKMQazZw+h5Bm46Ers8RAhMIisSzy4/ahNPgvYSDvQZytk3iAEUDlKcaD3MV817DSLHp45tF3G5nf8v73xUWb1";
//
//    private String sKey = "NAHTJ";

    //测试
    private String method = "POST";

    private String userId = "obulsi";

    private String userPwd = "oj2@#s5g";

    private String requestSource = "791215";

    private String signatureKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCqq+L2agcEFKs+SL/5A0ffikOLV0oQCylNDBwZqF8wMsbd0hN7tjShYi5H3gvnxSLjb4PZfElN6nxXNRyJAyvqDqAVKo474gtrQUF23duBm/49lQw+tEYKSPpcdyGXAOqfR3x86Jfsw3eMh9yC3X5KNjAJyqK76zCWvlS3x6AdX/2VETGrIeYW0xsZKnhrLUbg194c2Pd3b+TmoMEKeICcaFQfeUS4L2JGXWDES5CLuwpmjlydatkvfR9Mi1yObqTmcN7qjlFeIuMWxoqqKDnjapwTEX8lCxvYZJxEgQCo5cj6pfQiqwTUK5A5WmHp6rv+Q9V1zamIGpGQBvtHtKx1AgMBAAECggEBAJon1UHTNPEXpjEEzvkhYHrlzOp/9nKVUkG4bW0zan8DbiKPjG+SfOlD4B+jzUcUBdY+NG5t11xcLuEqJNMQnUq02R0lKgGmw7+l2nZ+H7Vq6/I9vSip9hV2I0T/icR13HXvwN6lzAvb5hB16tP9MgPf+/XsJJ7t6R5PXf/9fH7qafcRLCs8vcf1sUOUxq5E4Z19VKZVH9/tA9g+w3nbucoWKnrGFpaaZPwIiRelNvAOVimDc+GSNQOhdzM66l/2JCyPOVDFK6jbd2k3hx6eM3U15j8fVrYlInLTtC3kN3LRAE075RJhDzG739+EOWzw3duXBEn0d9VWPPR1mUMj/SUCgYEA25ISPQz00DuOWxJJEdeVEEzhS1bqy6hshiaY8aUe7xZqlsys3qTExbOCl0NnpHvmI7PqV5YdeM1ymyRJvR87TML0REjt7S6OHyBkH2rtONMaJwKdo2Lx8V5izsAvY5mRhUFkGDRyE2OQwOirMzZHleMikq71ag4On4eaJP1Ocg8CgYEAxvzlw2EntsrgPTDcLOKOgThz9njAv34EO1I2BmUFNDTAh56kczPedpM4UQ9iGMPWRoVGwG4hSKQhAEOhkVc7HGCE9J6Y3m1rV6o4/Kjj0W4IeIlyOo7Afobv7Kx2kz4hBm3HER5lA3hrTOyRO/THYcjjSd+pLLRAmgJtwU7wbTsCgYAi3sfWukAG8e5huD2yf7JErHD7LgqL3WuewnJ3remDvuOen1K06SX239NVgnbrQGIEOYZ18nkrvjT9Pn4XcuZQAoYYchyXFlWry9EazDhwVdZJa7jtQcF6u6qlexALmU/rAb+LuIiFW8KZYFoQAAbOXwwWj4KaHTFlVHu+dBwJMQKBgQC8UkwBBMnZLVfbkYpO0HudwBe/g7inglPu8HdAxgqOxf4QXAu3G7fG8L2zuaNVXMJGK4wyQggt/2dcIKWgL7Y5vP8Xo6F5oISeivMz64LwifpqWWpKESAMnxgiHXZ7E6mds/daNDHVfvnN63XoEFlCKiUxnQM208DwAKxu7o6MUQKBgQCYdp8QVtEk1+QKfIOmSI7htL9maBcHVqAvf5NAwYq5vpoKCPTddWbGmT8w84eyjG8D4RZcJ/yU0l+fTU+A0z2O5/c/i+HmwfnlDm3o0+4JFJOPbX932/JkF4OWoBSurKoT/MmGTAr098hIGxQp3lNBxxFBIasEaCtyDdU+Dxks3g==";

    private String sKey = "ERTQW";

    @Value("${files.localpath}")
    private String fileLocalPath="";


//    public static void main(String[] args) {
//        RealAuthServiceImpl real = new RealAuthServiceImpl();
//////////1
//        String zpath="/Users/Knight/Downloads/4973AE1CC1AA.jpg";
//        String fpath="/Users/Knight/Downloads/4DCAC832CEB7.jpg";
//        CustPicResponse responseZ = real.custPicIdentify("Z", zpath);
//        CustPicResponse responseF = real.custPicIdentify("F", fpath);
//
//
//        System.out.println(responseZ);
//        System.out.println(responseF);
//
//////////2
////        IdentifyInfo info = new IdentifyInfo();
////        info.setAddress("云南省昆明市寻甸回族彝族自治县七星乡必寨村委会老长地村26号");
////        info.setBirthday("1991-11-09");
////        info.setCustCertNo("310225199111091219");
////        info.setCustName("张麒麟");
////        info.setGender("男");
////        info.setNation("汉");
////        info.setCertExpdate("2016-06-22");
////        info.setCertValiddate("2006-06-22");
////        info.setIssuingAuthority("寻甸回族彝族自治县公安局");
////
////        CustPicResponse response = real.custInfoPicVerify("1", "0", info);
////        System.out.println(response.getReturnCode());
//
//////////3
////        String picPath = "/Users/Knight/Downloads/2F4DF370E0E6.jpg";
////        IdentifyInfo info = new IdentifyInfo();
////        info.setAddress("云南省昆明市寻甸回族彝族自治县七星乡必寨村委会老长地村26号");
////        info.setBirthday("1991-11-09");
////        info.setCustCertNo("310225199111091219");
////        info.setCustName("张麒麟");
////        info.setGender("男");
////        info.setNation("汉");
////        info.setCertExpdate("2016-06-22");
////        info.setCertValiddate("2006-06-22");
////        info.setIssuingAuthority("寻甸回族彝族自治县公安局");
////
////        CustPicResponse response1 = real.cusPicCompare("1", "0", info, "", picPath, "1");
////        System.out.println(response1.getReturnCode());
//    }


    /**
     *
     * @param picType 身份证图片 类型:“Z” 代表正面 “F” 代表反面
     * @param picPath 图片路径
     * @return
     */
    @Override
    public CustPicResponse custPicIdentify(String picType, String picPath) {
        try {

            ResInObject in = new ResInObject(); // 业务参数
            Map<String, String> params = new HashMap<String, String>();

			/*
			 * 请求服务的用户名、密码等账号信息
			 */
            params.put("userId", userId);
            params.put("userPwd", userPwd);
            params.put("picType", picType);

			/*
			 * yyyyMMddHHmmssSSS+6位随机数
			 */
            String sn = EncryptUtil.getSn();
            params.put("sn", sn);

            String reqJson = JsonUtil.convertObject2Json(params);
            // 整体加密报文元素
            reqJson = new RealNameMsDesPlus(sKey).encrypt(reqJson);
            in.setParamsJson(reqJson);

			String picPathName = IOUtils.toString(new FileInputStream(fileLocalPath + picPath), "ISO-8859-1");
            logger.info("picPathName length=" + picPathName.length());
            in.getImages().put("picPathName", picPathName);

            in.setBusiCode("custPicIdentify");
            in.setRequestSource(requestSource);
            in.setTransactionID(requestSource + sn);
            String signature = EncryptUtil.getSignature(signatureKey,requestSource, requestSource + sn, userId, userPwd, sn);
            // 验签数据
            in.setSignature(signature);
            logger.info("transactionId=" + requestSource + sn + " & RequestSource=" + requestSource);
            // 建议使用以下方式发起https请求
            HttpsUtil httpUtil = new HttpsUtil();
            String res = httpUtil.request(custPicIdentifyURL, in);

            logger.info("response:" + res);
            return (CustPicResponse) JSONObject.toBean(JSONObject.fromObject(res), CustPicResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 此接口 供身份证信息真实性查验及身份证照片真实性查验服务。
     * 使用时需要将身份证 文字信息和图片路径传给接口,进行身份证信息公安部联网比对,查验身份信息真实性。
     * @param isInterfaceCombination 是否联合使用接口 0否1是。取值为1时,“身份证图文信息验证接口”与“人像比对接口” 联合使用
     * @param needStaffCheck 是否需要人 工审核 0 不需要 1 需要。取值为1时, 需要携带身份证正反 面路径
     * @param identifyInfo 身份证信息
     * @return returnCode=0000:代表成功 1001:系统异常 2999:操作失败
     */
    public CustPicResponse custInfoPicVerify(String isInterfaceCombination, String needStaffCheck, IdentifyInfo identifyInfo) {
        try {
            ResInObject in = new ResInObject(); // 业务参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("custCertNo", identifyInfo.getCustCertNo());
            params.put("custName", identifyInfo.getCustName());
            params.put("gender", identifyInfo.getGender());
            params.put("nation", identifyInfo.getNation());
            params.put("birthday", identifyInfo.getBirthday());
            params.put("address", identifyInfo.getAddress());
            params.put("issuingAuthority", identifyInfo.getIssuingAuthority());
            params.put("certValiddate", identifyInfo.getCertValiddate());
            params.put("certExpdate", identifyInfo.getCertExpdate());
            params.put("needStaffCheck", needStaffCheck);
            params.put("isInterfaceCombination", isInterfaceCombination);
            params.put("userId", userId);
            params.put("userPwd", userPwd);
			/*
			 * yyyyMMddHHmmssSSS+6位随机数
			 */
            String sn = EncryptUtil.getSn();
            params.put("sn", sn);

            String reqJson = JsonUtil.convertObject2Json(params);
            // 整体加密报文元素
            reqJson = new RealNameMsDesPlus(sKey).encrypt(reqJson);
            in.setParamsJson(reqJson);
            in.setBusiCode("custInfoPicVerify");
            in.setRequestSource(requestSource);
            in.setTransactionID(requestSource + sn);

            String signature = EncryptUtil.getSignature(signatureKey,requestSource, requestSource + sn, userId, userPwd, sn);
            // 验签数据
            in.setSignature(signature);
            // 建议使用以下方式发起https请求

            logger.info("custInfoPicVerify params:" + JSONObject.fromObject(in).toString());
            HttpsUtil httpUtil = new HttpsUtil();
            String res = httpUtil.request(custInfoPicVerifyURL, in);

            System.out.println(res);
            return (CustPicResponse) JSONObject.toBean(JSONObject.fromObject(res), CustPicResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 此接口 供手持身份证图片或者活体图片路径与公安部数据库头像数据比对服务,返回 比对结果。
     * @param isInterfaceCombination 是否联合使用接口 0否1是。取值为1时,“身份证图文信息验证接口”与“人像比对接口” 联合使用
     * @param needStaffCheck 是否需要人 工审核 0 不需要 1 需要。取值为1时, 需要携带身份证正反面路径
     * @param identifyInfo 身份证信息
     * @param token “身份证信息验证接口”与“人像比对接口” 联合使用时,即 isInterfaceCombination取值为1时,必填, 该值为身份证信息验证接口返回的token
     * @param picPath 图片路径
     * @param isHandleCard 上传图片是否为手持身份证,其中:0代表人像图片, 1代表上传手持身份证图片
     * @return returnCode=0000:代表成功 1001:系统异常 2999:操作失败
     */
    public CustPicResponse cusPicCompare(String isInterfaceCombination, String needStaffCheck, IdentifyInfo identifyInfo, String token, String picPath, String isHandleCard) {
        try {
            ResInObject in = new ResInObject(); // 业务参数
            Map<String, String> params = new HashMap<String, String>();
            String sn = EncryptUtil.getSn();
            params.put("sn", sn);
            params.put("custCertNo", identifyInfo.getCustCertNo());
            params.put("custName", identifyInfo.getCustName());
            params.put("needStaffCheck", needStaffCheck);
            params.put("isInterfaceCombination", isInterfaceCombination);
            if (StringUtils.isNotBlank(token)) {
                params.put("token", token);
            }
            params.put("userId", userId);
            params.put("userPwd", userPwd);
            if (StringUtils.isNotBlank(isHandleCard)) {
                params.put("isHandleCard", isHandleCard);
            }


            String reqJson = JsonUtil.convertObject2Json(params);
            // 整体加密报文元素
            reqJson = new RealNameMsDesPlus(sKey).encrypt(reqJson);
            in.setBusiCode("cusPicCompare");
            in.setParamsJson(reqJson);
            in.setRequestSource(requestSource);
            in.setTransactionID(requestSource + sn);
            String picPathName = IOUtils.toString(new FileInputStream(fileLocalPath + picPath), "ISO-8859-1");
            in.getImages().put("picNameR", picPathName);

            String signature = EncryptUtil.getSignature(signatureKey,requestSource, requestSource + sn, userId, userPwd, sn);
            // 验签数据
            in.setSignature(signature);
            logger.info("cusPicCompare - TransactionID: " + requestSource + sn);
            logger.info("cusPicCompare - paramsJson:" + reqJson);
            logger.info("cusPicCompare - signature:" + signature);
            logger.info("cusPicCompare - BusiCode: cusPicCompare");
            logger.info("cusPicCompare - RequestSource: " + requestSource);
            logger.info("cusPicCompare - picNameR: " + picPathName);
            logger.info(JSONObject.fromObject(in).toString());

            HttpsUtil httpUtil = new HttpsUtil();
            String res = httpUtil.request(cusPicCompareURL, in);

            logger.info(res);
            return (CustPicResponse) JSONObject.toBean(JSONObject.fromObject(res), CustPicResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置连接超时时间，单位秒，默认为30秒
     *
     * @param connection_timeout
     */
    public void setConnection_timeout(String connection_timeout) {
        this.connection_timeout = connection_timeout;
    }

    /**
     * 设置读取超时时间，单位秒，默认为30秒
     *
     * @param so_timeout
     */
    public void setSo_timeout(String so_timeout) {
        this.so_timeout = so_timeout;
    }

    /**
     * 设置提交方式为GET，默认为POST
     */
    public void setMethodIsGet() {
        this.method = "GET";
    }


    /**
     * 忽视证书HostName
     */
    private HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
            return true;
        }
    };

    /**
     * Ignore Certification
     */
    private TrustManager ignoreCertificationTrustManger = new X509TrustManager() {

        private X509Certificate[] certificates;

        public void checkClientTrusted(X509Certificate certificates[],
                                       String authType) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
            }
        }

        public void checkServerTrusted(X509Certificate[] ax509certificate,
                                       String s) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
            }

        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    };

    /**
     * 发送JSON请求方法 默认为POST方式
     *
     * @param urlStr
     *            请求地址
     * @return
     * @throws IOException
     * @throws Exception
     */
    public String request(String urlStr,ResInObject in) throws Exception {
        String jsonParam= JsonUtil.convertObject2Json(in);
        String res = "";
        HttpsURLConnection conn = null;
        if (urlStr == null || urlStr.trim().length() == 0
                || !urlStr.toLowerCase().startsWith("https")) {
            return "";
        }
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            conn = (HttpsURLConnection) url.openConnection();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("TLSv1");;
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            conn.setSSLSocketFactory(ssf);
            conn.setConnectTimeout(Integer.valueOf(connection_timeout) * 1000);
            conn.setReadTimeout(Integer.valueOf(so_timeout) * 1000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            String encoding = Base64.encode("userName:passWord".getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            if (in.getRequestSource() != null && in.getRequestSource().trim().length() > 0) {
                conn.setRequestProperty("requestSource", in.getRequestSource());
            }

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (jsonParam != null && jsonParam.trim().length() > 0) {
                out.write(jsonParam.getBytes("UTF-8"));
            }

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }
}
