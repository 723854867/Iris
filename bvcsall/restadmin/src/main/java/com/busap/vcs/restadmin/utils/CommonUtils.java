package com.busap.vcs.restadmin.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by huoshanwei on 2015/10/20.
 */
public class CommonUtils {

    /**
     * 获取远程访问IP
     *
     * @return
     */
    public static String getRemoteIp() {
        // 获取请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String remoteIp = request.getHeader("x-forwarded-for");
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("X-Real-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteAddr();
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteHost();
        }
        return remoteIp;
    }

    public static HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            //System.out.println(new String(filename.getBytes()));
            String contentType = "application/txt";
            if ("xls".equals(ext) || "xlsx".equals(ext)) {
                contentType = "application/msexcel";
            }
            //System.out.println(filename);
            // 以流的形式下载文件
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType(contentType);
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    //获取几天前时间
    public static String getDateBefore(int day) {
        Date dNow = new Date();   //当前时间
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -day);  //设置为前一天
        Date dBefore = calendar.getTime();   //得到前一天的时间


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前一天
        String defaultEndDate = sdf.format(dNow); //格式化当前时间


        System.out.println("前一天的时间是：" + defaultStartDate);

        System.out.println("生成的时间是：" + defaultEndDate);
        return defaultEndDate;
    }

    /**
     * 读取文件
     *
     * @param readPath 文件路径.
     * @return
     */
    public static String readTxt(String readPath, String sign) {
        String readTxt = "";
        try {
            File f = new File(readPath);
            if (f.isFile() && f.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(f), "UTF-8");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    readTxt += line + sign;
                }
                read.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readTxt;
    }

    public static String formatDuring(long mss, int type) {
        if (type == 1) {
            long days = mss / (1000 * 60 * 60 * 24);
            long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (mss % (1000 * 60)) / 1000;
            return days + "天" + hours + "时" + minutes + "分" + seconds + "秒";
        } else {
            long hours = mss / (1000 * 60 * 60);
            long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (mss % (1000 * 60)) / 1000;
            return hours + "时" + minutes + "分" + seconds + "秒";
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static String dateFormatUnixTime(String date){
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(date);
            long l = d.getTime();
            String str = String.valueOf(l);
            time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    /**
     * HMAC-SHA1加密方案<br>
     * @param content-待加密内容
     * @param secretKey-密钥
     * @return HMAC_SHA1加密后的字符串
     */
    public static String HMACSHA1(String content, String secretKey) {
        try {
            byte[] secretKeyBytes = secretKey.getBytes();
            SecretKey secretKeyObj = new SecretKeySpec(secretKeyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeyObj);
            byte[] text = content.getBytes("UTF-8");
            byte[] encryptContentBytes = mac.doFinal(text);
            //SHA1算法得到的签名长度，都是160位二进制码，换算成十六进制编码字符串表示
            String encryptContent = bytesToHexString(encryptContentBytes);
            return encryptContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
    /**
     * 获取字节数组的16进制字符串表示形式<br>
     * 范例：0xff->'ff'
     * @param bytes 字节数组
     * @return string-16进制的字符串表示形式
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder("");
        for(byte ib : bytes) {
            char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            char[] ob = new char[2];
            ob[0] = Digit[(ib >>> 4) & 0X0f];
            ob[1] = Digit[ib & 0X0F];
            hexString.append(ob);
        }
        return hexString.toString();
    }

}
