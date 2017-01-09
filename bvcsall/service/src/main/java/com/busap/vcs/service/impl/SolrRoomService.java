package com.busap.vcs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SolrRoomService {
	
	@Value("${solr_zookeeper}")
	private String zkHost;
	
	
	SolrClient server = null;  
	
	public SolrRoomService(){
		
	}
	
	
	@PostConstruct
	public void init(){
		CloudSolrClient solr = new CloudSolrClient(zkHost);
		solr.setParallelUpdates(true);
		solr.setDefaultCollection("room");
		solr.setZkConnectTimeout(10*10000);
		solr.setZkClientTimeout(10*10000);
		solr.connect();
		server = solr;
	}
	
	@PreDestroy
	public void release(){
		if (server != null) {
			server.shutdown();
		}
	}
	
	public void addRoom(long id,String title,String anchorName,String anchorPic,String playbackUrl,String roomPic) throws SolrServerException, IOException{  
		
		//创建doc文档
	    SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("id", id);
	    doc.addField("title", title);
	    doc.addField("anchorName", anchorName);
	    doc.addField("anchorPic", anchorPic);
	    doc.addField("playbackUrl", playbackUrl);
	    doc.addField("roomPic", roomPic);
		server.add(doc);
		server.commit();
		
	}
	
	
   public void addRoom(long videoId,String publishTime,String playKey,String duration,String description,String videoPic,String createDate,long anchorId,Long maxAccessNumber ) throws SolrServerException, IOException{  
	 //创建doc文档
	    SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("videoId", videoId);
	    doc.addField("publishTime", publishTime);
	    doc.addField("playKey", playKey);
	    doc.addField("duration", duration);
	    doc.addField("description", description);
	    doc.addField("videoPic", videoPic);
	    doc.addField("createDate", createDate);
	    doc.addField("anchorId", anchorId);
	    doc.addField("maxAccessNumber", maxAccessNumber);
		server.add(doc);
		server.commit();
		
	}
		
	
	public void addRoom(long id,String title,String phone) throws SolrServerException, IOException{  
		//创建doc文档
	    SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("id", id);
	    doc.addField("title", title);
//	    doc.addField("title", title);
		server.add(doc);
		server.commit();
		
	}
	
	
	public int deleteDocByid(long docId) throws SolrServerException, IOException{
		UpdateResponse updateResponse = server.deleteById(String.valueOf(docId));
		int status = updateResponse.getStatus();
		return status;
	}
	
	
	public int updateDocNum(long id,String title) throws SolrServerException, IOException{
		SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("id", id);
	    doc.addField("title", title);
		server.add(doc);
		server.commit();
		return 0;
	}
	
	
	public List<Long> search(String name,Integer page,Integer rows) throws SolrServerException{
		ModifiableSolrParams  params =new ModifiableSolrParams();
		//将用户输入的关键字符转义
		/*Pattern pattern = Pattern.compile("[\\+\\-!\\(\\)\\:\\^\\[\\]\\{\\}~\\*\\?\"\\ ]");
		Matcher matcher = pattern.matcher(name);
		StringBuffer bs = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(bs, "\\\\"+matcher.group());
		}
		matcher.appendTail(bs);
		
		name = bs.toString();*/
		name = ClientUtils.escapeQueryChars(name);
		
		// 查询关键词，*:*代表所有属性、所有值，即所有index
		
//		boolean dataFlg=false;
		boolean chineseFlg=false;
		boolean englishFlg=false;
		

		name=SolrUtil.convertSearchName(name);

		
//		if(name!=null&&!name.equals("")&&name.length()>=1) {
//			name=name+"*";
//		}
		
		
		params.set("q", "title:"+name);
		//name:实地了解 AND videoid:{* TO 22194}
		// 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
		if(rows==null) {
			rows=10;
		}
		Integer start=(page-1)*rows;
		params.set("start", start);
		
		params.set("rows",rows); 
		// 排序，，如果按照id 排序，，那么将score desc 改成 id desc(or asc)
		params.set("sort", "score desc"); 
		// 返回信息 * 为全部 这里是全部加上score，如果不加下面就不能使用score
		params.set("fl", "*,score"); 
		QueryResponse response = server.query(params);
	    SolrDocumentList list = response.getResults();
	    
	    
	    if (CollectionUtils.isNotEmpty(list)) {
	    	List<Long> retList = new ArrayList<Long>(list.size());
	    	for (int i = 0; i < list.size(); i++) {
	    		//System.out.println(list.get(i));
	    		SolrDocument document = list.get(i);
	    		retList.add((Long)document.get("id"));
	    	}
	    	return retList;
		}else {
			return new ArrayList<Long>(0);
		}
	    
	    
	    
	}
	
	public List<Map> searchByStart(String name,Integer start,Integer rows) throws SolrServerException{
		ModifiableSolrParams  params =new ModifiableSolrParams();
		//将用户输入的关键字符转义
		/*Pattern pattern = Pattern.compile("[\\+\\-!\\(\\)\\:\\^\\[\\]\\{\\}~\\*\\?\"\\ ]");
		Matcher matcher = pattern.matcher(name);
		StringBuffer bs = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(bs, "\\\\"+matcher.group());
		}
		matcher.appendTail(bs);
		
		name = bs.toString();*/
		name = ClientUtils.escapeQueryChars(name);
		
		// 查询关键词，*:*代表所有属性、所有值，即所有index
		
//		boolean dataFlg=false;
		boolean chineseFlg=false;
		boolean englishFlg=false;
		

		name=SolrUtil.convertSearchName(name);

		
//		if(name!=null&&!name.equals("")&&name.length()>=1) {
//			name=name+"*";
//		}
		
		
		params.set("q", "description:"+name);
		
	//	params.set("q", "videoId:[455561 TO *]");
		
	//	params.set("q", "publishTime:['2016-07-28 00:00:00' TO *]");
//		params.set("q", "*:*");
		//name:实地了解 AND videoid:{* TO 22194}
		// 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
		if(rows==null) {
			rows=10;
		}
//		Integer start=(page-1)*rows;
		params.set("start", start);
		
		params.set("rows",rows); 
		// 排序，，如果按照id 排序，，那么将score desc 改成 id desc(or asc)
		params.set("sort", "score desc"); 
		// 返回信息 * 为全部 这里是全部加上score，如果不加下面就不能使用score
		params.set("fl", "*,score"); 
		QueryResponse response = server.query(params);
	    SolrDocumentList list = response.getResults();
	    
	    
	    if (CollectionUtils.isNotEmpty(list)) {
	    	List<Map> retList = new ArrayList<Map>(list.size());
	    	for (int i = 0; i < list.size(); i++) {
	    		//System.out.println(list.get(i));
	    		SolrDocument document = list.get(i);
	    		Map roomMap=new HashMap();
//	    		roomMap.put("id", document.get("id"));
//	    		roomMap.put("anchorName", document.get("anchorName"));
//	    		roomMap.put("anchorPic", document.get("anchorPic"));
//	    		roomMap.put("description", document.get("title"));
//	    		roomMap.put("playKey", document.get("playbackUrl"));
//	    		roomMap.put("videoPic", document.get("roomPic"));
	    		
	    		
	    		roomMap.put("videoId", document.get("videoId"));
	    		roomMap.put("publishTime", document.get("publishTime"));
	    		roomMap.put("playKey", document.get("playKey"));
	    		roomMap.put("duration", document.get("duration"));
	    		roomMap.put("description", document.get("description"));
	    		roomMap.put("videoPic", document.get("videoPic"));
	    		roomMap.put("createDate", document.get("createDate"));
	    		roomMap.put("anchorId", document.get("anchorId"));
	    		roomMap.put("maxAccessNumber", document.get("maxAccessNumber"));
	    		
	    		
	    		retList.add(roomMap);
	    	}
	    	return retList;
		}else {
			return new ArrayList<Map>(0);
		}
	    
	    
	    
	}
	
	
	public static void main(String []str){
		//用户输入的内容需要转义
		/*SolrWoPaiTagService s = new SolrWoPaiTagService();
		Pattern  pattern = Pattern.compile("[\\+\\-!\\(\\)\\:\\^\\[\\]\\{\\}~\\*\\?\"\\||]");
		Matcher matcher = pattern.matcher("adasf+*++~++?\"");
		StringBuffer bs = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(bs, "\\\\\\\\"+matcher.group());
			System.out.println("1111");
			System.out.println(matcher.group());
		}
		matcher.appendTail(bs);
		System.out.println(bs.toString());*/
		
		try {
			//s.index();
			//s.updateDocNum(1000,"梵蒂冈我拍阿萨德" ,1);
			//s.search("我拍");
			//s.deleteDocByid(1000);
			//s.search("\\?");
			SolrRoomService server = new SolrRoomService();
//			server.addRoom(1000,"梵蒂冈我拍阿萨德" );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//s.search(); 
	}

}
