package com.busap.vcs.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pili.Hub;
import com.pili.PiliException;
import com.pili.Stream;
import com.pili.Stream.SaveAsResponse;
import com.pili.Stream.Segment;
import com.pili.Stream.SegmentList;
import com.qiniu.Credentials;

public class QiniuUtil {
	
	private Logger logger = LoggerFactory.getLogger(QiniuUtil.class);
	
    private static final String ACCESS_KEY = "SgrUcAclf2MIBfPJ5zyhXirv79gUJadojQnV3fN3";
    private static final String SECRET_KEY = "nlHcueuQcXTOeuMctILW_Gu4bzAcjfTKr3KlzPc9";

    private static final String HUB_NAME = "busappstream";
    
    public static void main(String[] args) {
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QiniuUtil util = new QiniuUtil();
		
		String startTimeStr = "2016-04-07 17:36:19";
		String endTimeStr = "2016-04-07 17:44:13";
		
		Date startTime = null;
		Date endTime = null;
		
		try {
			startTime = format.parse(startTimeStr);
			endTime = format.parse(endTimeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Stream stream = util.getStream("z1.busappstream.f3a05325cf5ed0bd");
		
		
//		util.getSegment(stream, startTime.getTime()/1000, endTime.getTime()/1000);
//		System.out.println(util.getFileUrl(stream, String.valueOf(System.currentTimeMillis()), "mp4", startTime.getTime()/1000, endTime.getTime()/1000));
//		System.out.println(util.getHlsPlaybackUrl(stream,startTime.getTime()/1000, endTime.getTime()/1000));
		System.out.println(util.getDuration(stream,startTime.getTime()/1000, endTime.getTime()/1000));
//		System.out.println(util.getHlsPlaybackUrls(stream));
//		util.getRtmpPublishUrl(stream);
//		util.getLiveUrl(stream, "hls");
//		util.getLiveUrl(stream, "rtmp");
	}
    
    
    /**
     * 根据主播用户id生成直播流
     * @param anchorId
     * @return
     */
    public Stream createStream(String title) {
    	logger.info("createStream,title={}",title);
    	
    	Credentials credentials = new Credentials(ACCESS_KEY, SECRET_KEY); 
        Hub hub = new Hub(credentials, HUB_NAME);
        
        String publishKey = "wopai";   
        String publishSecurity = "static";     
    	Stream stream = null;
    	try {
			stream = hub.createStream(title, publishKey, publishSecurity);
		} catch (PiliException e) {
			e.printStackTrace();
		}
    	
    	logger.info("stream json is {}",stream == null ? "null" : stream.toJsonString());
    	
    	return stream;
    }
    
    /**
     * 根据直播流id查询直播流
     * @param streamId
     * @return
     */
    public Stream getStream(String streamId) {
    	logger.info("getStrem,streamId={}",streamId);
    	
    	Credentials credentials = new Credentials(ACCESS_KEY, SECRET_KEY); 
        Hub hub = new Hub(credentials, HUB_NAME);
        
        Stream stream = null;
        try {
			stream = hub.getStream(streamId);
		} catch (PiliException e) {
			e.printStackTrace();
		}
        
        logger.info("stream json is {}",stream == null ? "null" : stream.toJsonString());
        
    	return stream;
    }
    
    /**
     * 根据直播流查询rtmp推送地址
     * @param stream
     * @return
     */
    public String getRtmpPublishUrl(Stream stream){
    	String originUrl = null;
    	if (stream != null) {
    		try {
				originUrl = stream.rtmpPublishUrl();
			} catch (PiliException e) {
				e.printStackTrace();
			}
    	}
        logger.info("getRtmpPublishUrl,url is {}",originUrl == null ? "null" : originUrl);
        return originUrl;
    }
    
    /**
     * 根据直播流查询不同方式的直播播放地址
     * @param stream
     * @param type
     * @return
     */
    public String getLiveUrl(Stream stream,String type){
    	logger.info("getLiveUrl,type is {}",type);
    	String liveUrl = null;
    	
    	if (stream != null) {
    		if (type.equals("rtmp")){
    			liveUrl = stream.rtmpLiveUrls().get(Stream.ORIGIN);
    		} else if (type.equals("flv")) {
    			liveUrl = stream.httpFlvLiveUrls().get(Stream.ORIGIN);
    		} else if (type.equals("hls")) {
    			liveUrl = stream.hlsLiveUrls().get(Stream.ORIGIN);
    		}
    	}
    	logger.info("liveUrl is {}",liveUrl);
    	
    	return liveUrl;
    }
    
    /**
     *  获得直播视频下载地址
     * @param stream
     * @param videoName  保存的视频名称
     * @param suffix   视频文件后缀
     * @param start  开始时间
     * @param end  结束时间
     * @return
     */
    public String getFileUrl(Stream stream,String videoName,String suffix,Long start,Long end) {
    	String fileUrl = "";
    	String saveAsFormat    = suffix;                            // required
    	String saveAsName      = videoName + "." + saveAsFormat; // required
    	long saveAsStart       = start;                       // required, in second, unix timestamp
    	long saveAsEnd         = end;                       // required, in second, unix timestamp
    	String saveAsNotifyUrl = null;                             // optional
    	try {
    	    SaveAsResponse response = stream.saveAs(saveAsName, saveAsFormat, saveAsStart, saveAsEnd, saveAsNotifyUrl);
    	    fileUrl = response.toString();
    	} catch (PiliException e) {
    	    e.printStackTrace();
    	}
    	return fileUrl;
    }
    
    public String getHlsPlaybackUrl(Stream stream,Long start,Long end) {
    	String fileUrl = "";
    	String saveAsFormat    = "mp4";                            // required
    	String saveAsName      = String.valueOf(System.currentTimeMillis()) + "." + saveAsFormat; // required
    	long saveAsStart       = start;                       // required, in second, unix timestamp
    	long saveAsEnd         = end;                       // required, in second, unix timestamp
    	String saveAsNotifyUrl = null;                             // optional
    	try {
    	    SaveAsResponse response = stream.saveAs(saveAsName, saveAsFormat, saveAsStart, saveAsEnd, saveAsNotifyUrl);
    	    fileUrl = response.toString();
    	    JSONObject json = JSONObject.fromObject(fileUrl);
    	    System.out.println(fileUrl);
    	    fileUrl = json.getString("url");
    	} catch (PiliException e) {
    	    e.printStackTrace();
    	}
    	return fileUrl;
    }
    
    public String getDuration(Stream stream,Long start,Long end) {
    	String duration = "";
    	String saveAsFormat    = "mp4";                            // required
    	String saveAsName      = String.valueOf(System.currentTimeMillis()) + "." + saveAsFormat; // required
    	long saveAsStart       = start;                       // required, in second, unix timestamp
    	long saveAsEnd         = end;                       // required, in second, unix timestamp
    	String saveAsNotifyUrl = null;                             // optional
    	try {
    	    SaveAsResponse response = stream.saveAs(saveAsName, saveAsFormat, saveAsStart, saveAsEnd, saveAsNotifyUrl);
    	    String fileUrl = response.toString();
    	    JSONObject json = JSONObject.fromObject(fileUrl);
    	    fileUrl = json.getString("targetUrl");
    	    String reqUrl = fileUrl+"?avinfo";
    	    logger.info("getDuration,reqUrl is {}",reqUrl);
    	    String resContent = httpGet(reqUrl);
    	    json = JSONObject.fromObject(resContent);
    	    System.out.println(json.getString("duration"));
    	} catch (PiliException e) {
    	    e.printStackTrace();
    	}
    	return duration;
    }
    
    public String httpGet(String url) {
		logger.info("httpclient,request url is :{}",url);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		String respContent = "";
		try {
			int statusCode = client.executeMethod(method);
			respContent = new String(method.getResponseBody(),"utf-8");
//			if (statusCode == HttpStatus.SC_OK) {
//				respContent = new String(method.getResponseBody(),"utf-8");
//			} else {
//				logger.error("Response Code: " + statusCode);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		System.out.println(respContent);
		return respContent;
	}
    
    public void getSegment(Stream stream,Long start,Long end) {
    	try {
			SegmentList sl = stream.segments(start, end);
			System.out.println(sl.getDuration());
			for (Segment s:sl.getSegmentList()){
				System.out.println(s.getEnd()-s.getStart());
			}
		} catch (PiliException e) {
			e.printStackTrace();
		}
    }
    
    public String getHlsPlaybackUrls(Stream stream) {
    	String response = "";
    	try {
    		response = stream.hlsPlaybackUrls(-1, -1).get(stream.ORIGIN);
    	} catch (PiliException e) {
    		e.printStackTrace();
    	}
    	return response;
    }
}
