package com.busap.vcs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Label;

@Service
public class SolrUserService {
	
	@Value("${solr_zookeeper}")
	private String zkHost;
	
	
	SolrClient server = null;  
	
	public SolrUserService(){
		
	}
	
	
	@PostConstruct
	public void init(){
		CloudSolrClient solr = new CloudSolrClient(zkHost);
		solr.setParallelUpdates(true);
		solr.setDefaultCollection("user");
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
	
	public void addUser(long id,String name) throws SolrServerException, IOException{  
		//创建doc文档
	    SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("id", id);
	    doc.addField("name", name);
		server.add(doc);
		server.commit();
		
	}
	
	public void addUser(long id,String name,String phone) throws SolrServerException, IOException{  
		//创建doc文档
	    SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("id", id);
	    doc.addField("name", name);
	    doc.addField("phone", phone);
		server.add(doc);
		server.commit();
		
	}
	
	
	public int deleteDocByid(long docId) throws SolrServerException, IOException{
		UpdateResponse updateResponse = server.deleteById(String.valueOf(docId));
		int status = updateResponse.getStatus();
		return status;
	}
	
	
	public int updateDocNum(long id,String name) throws SolrServerException, IOException{
		SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("id", id);
	    doc.addField("name", name);
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
		
		
		params.set("q", "name_phone:"+name);
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
	
	public List<String> findSuggestLabel(String name){
		 QueryResponse queryResponse = new QueryResponse();  
		    SolrQuery query = new SolrQuery();  
		    // fl=id,name&rows=0&q=*:*&facet=true&facet.field=searchText&facet.  
		    // mincount=1&facet.prefix=sony  
		    //facet=true&rows=0&fl=id%2Cname&facet.prefix=sony&facet.field=searchText  
		    try {  
		        query.setFacet(true);  
		        query.setRows(20);  
		        query.setQuery("name:"+name);  
		        query.setFacetPrefix(name);  
		        query.addFacetField("name");  
		        System.out.println(query.toString());  
		        queryResponse = server.query(query, METHOD.POST);  
		    } catch (Exception e) {  
		        // TODO: handle exception  
		       e.printStackTrace();  
		    }  
		    NamedList<Object> nl = queryResponse.getResponse();  
		    
		    NamedList<Object> nl2 = (NamedList<Object>) nl.get("facet_counts");  
		    NamedList<Object> nl3 = (NamedList<Object>) nl2.get("facet_fields");  
		    NamedList<Object> nl4 = (NamedList<Object>) nl3.get("name");
//		    NamedList<Object> nl5 = (NamedList<Object>) nl.get("response");
		    SolrDocumentList sdl6 = (SolrDocumentList) nl.get("response");
		    Iterator<SolrDocument> sdIt = sdl6.iterator(); 
		    
		    List<String> nameList =new ArrayList<String>();
		    while (sdIt.hasNext()) {  
		    	SolrDocument entry = sdIt.next();  
		        //System.out.println(entry.getFieldValue("name"));  
		        nameList.add(entry.getFieldValue("name").toString());
		    }
		    return nameList;
		    
//		    System.out.println(nl4.size());  
//		    Iterator<Entry<String, Object>> it = nl4.iterator();  
//		    while (it.hasNext()) {  
//		        Entry<String, Object> entry = it.next();  
//		        System.out.println(entry.getKey() + "____" + entry.getValue());  
//		    }  
		    
//		    SolrDocumentList results = queryResponse.getResults(); 
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
			SolrUserService server = new SolrUserService();
			server.addUser(1000,"梵蒂冈我拍阿萨德" );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//s.search(); 
	}

}
