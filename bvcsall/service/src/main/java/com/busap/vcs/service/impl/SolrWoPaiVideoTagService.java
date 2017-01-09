package com.busap.vcs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class SolrWoPaiVideoTagService {
	
	@Value("${solr_zookeeper}")
	//192.168.108.151:2181/solr
	private String zkHost = "192.168.108.151/solr";
	
	
	SolrClient server = null;  
	
	public SolrWoPaiVideoTagService(){
		
	}
	
	@PostConstruct
	public void init(){
		CloudSolrClient solr = new CloudSolrClient(zkHost);
		solr.setParallelUpdates(true);
		solr.setDefaultCollection("wopaivideotag");
		solr.setZkConnectTimeout(10*10000);
		solr.setZkClientTimeout(10*1000);
		solr.connect();
		server = solr;
	}
	
	
	@PreDestroy
	public void release(){
		if (server != null) {
			server.shutdown();
		}
	}
	
	public void index(long id,String name,long videoId) throws SolrServerException, IOException{  
		//创建doc文档
	    SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("id", id);
	    doc.addField("name", name);
	    doc.addField("videoid", videoId);
		server.add(doc);
		server.commit();
		
	}
	
	
	public int deleteDocByid(long docId) throws SolrServerException, IOException{
		UpdateResponse updateResponse = server.deleteById(String.valueOf(docId));
		int status = updateResponse.getStatus();
		return status;
	}
	
	
	public int deleteDocsByIds(List<String> ids) throws SolrServerException, IOException{
		UpdateResponse updateResponse = server.deleteById(ids);
		int status = updateResponse.getStatus();
		return status;
	}
	
	public int updateDocNum(long id,String name,long num) throws SolrServerException, IOException{
		SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("id", id);
	    doc.addField("name", name);
	    doc.addField("num", num);
		server.add(doc);
		server.commit();
		return 0;
	}
	
	
	public List<Long> search(String name,long id,int rows) throws SolrServerException{
		ModifiableSolrParams  params =new ModifiableSolrParams();
		//关键字符转义
		/*Pattern  pattern = Pattern.compile("[\\+\\-!\\(\\)\\:\\^\\[\\]\\{\\}~\\*\\?\"\\ ]");
		Matcher matcher = pattern.matcher(name);
		StringBuffer bs = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(bs, "\\\\"+matcher.group());
		}
		matcher.appendTail(bs);
		name = bs.toString();*/
		name = ClientUtils.escapeQueryChars(name);
		
		name=SolrUtil.convertSearchName(name);
		
		// 查询关键词，*:*代表所有属性、所有值，即所有index
		if (id == 0) {
			params.set("q", "name:"+name);
		}else {
			params.set("q", "name:"+name+" AND videoid:{* TO "+id+"}");
		}
		
		//name:实地了解 AND videoid:{* TO 22194}
		// 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
		params.set("start", 0);
		params.set("rows",rows);
		// 排序，，如果按照id 排序，，那么将score desc 改成 id desc(or asc)
		params.set("sort", "videoid desc");
		// 返回信息 * 为全部 这里是全部加上score，如果不加下面就不能使用score
		params.set("fl", "*,score"); 
		QueryResponse response = server.query(params);
	    SolrDocumentList list = response.getResults();
	    if (CollectionUtils.isNotEmpty(list)) {
	    	List<Long> retList = new ArrayList<Long>(list.size());
	    	for (int i = 0; i < list.size(); i++) {
	    		//System.out.println(list.get(i));
	    		SolrDocument document = list.get(i);
	    		retList.add((Long)document.get("videoid"));
	    	}
	    	return retList;
		}else {
			return new ArrayList<Long>(0);
		}
	}
	
	public static void main(String []str){
		//用户输入的内容需要转义
		/*SolrWoPaiTagService s = new SolrWoPaiTagService();
		Pattern  pattern = Pattern.compile("[\\+\\-!\\(\\)\\:\\^\\[\\]\\{\\}~\\*\\?\"]");
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
			SolrWoPaiVideoTagService server = new SolrWoPaiVideoTagService();
			//server.index(1000,"梵蒂冈我拍阿萨德" ,5);
			//server.search("?", 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//s.search(); 
	}

}
