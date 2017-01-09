package com.busap.vcs.service.impl;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;

public class SolrServiceImpl {
	
	String zkHost = "192.168.75.128:2181,192.168.75.129:2181,192.168.75.130:2181";
	SolrClient server = null;  
	
	public SolrServiceImpl(){
		CloudSolrClient solr = new CloudSolrClient(zkHost);
		solr.setDefaultCollection("corevideo");
		solr.setZkConnectTimeout(10*1000);
		solr.setZkClientTimeout(10*1000); 
		server = solr;
	}
	
	public void index(){  
		//创建doc文档
	    SolrInputDocument doc = new SolrInputDocument();
	    doc.addField("id", 1);
	    doc.addField("name", "hegaoxiang test");
	    doc.addField("manu", "sdfdsf");
		try{
			server.add(doc);
			server.commit(); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void search(){  
		try{
			ModifiableSolrParams  params =new ModifiableSolrParams();
			// 查询关键词，*:*代表所有属性、所有值，即所有index
		    params.set("q", "*:*");
		    // 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
		    params.set("start", 0);
		    params.set("rows",4); 
		    // 排序，，如果按照id 排序，，那么将score desc 改成 id desc(or asc)
		    params.set("sort", "score desc"); 
		    // 返回信息 * 为全部 这里是全部加上score，如果不加下面就不能使用score
		    params.set("fl", "*,score"); 
			QueryResponse response = server.query(params); 
	        SolrDocumentList list = response.getResults();
	        for (int i = 0; i < list.size(); i++) {
	            System.out.println(list.get(i));
	        }  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String []str){
		SolrServiceImpl s = new SolrServiceImpl();
		s.index();
		s.search(); 
	}

}
