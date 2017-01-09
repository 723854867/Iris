package com.busap.vcs.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.SensitiveWord;
import com.busap.vcs.data.repository.SensitiveWordRepository;
import com.github.zkclient.IZkDataListener;
import com.github.zkclient.ZkClient;

/**
 * @Description: 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 * @Project：test
 * @Author : chenming
 * @Date ： 2014年4月20日 下午2:27:06
 * @version 1.0
 */  
public class SensitiveWordInit {  

	private static final Logger logger = LoggerFactory.getLogger(SensitiveWordInit.class);
	
	private String ENCODING = "UTF-8";    //字符编码
	@SuppressWarnings("rawtypes")
	public HashMap sensitiveWordMap;
	
	@Resource(name = "sensitiveWordRepository")
    private SensitiveWordRepository sensitiveWordRepository;
	
	//敏感词监听path
	private static final String SENSITIVE_WORD_PATH = "/main/word/keepword";
	
	@Autowired
	private ZkClient zkClient;
	
	public SensitiveWordInit(){
		super();
	}
	
	/**
	 * @author chenming 
	 * @date 2014年4月20日 下午2:28:32
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public Map initKeyWord(){
		try {
			//读取敏感词库 
			Set<String> keyWordSet = this.getWords(); 
			//初始化敏感词
			addSensitiveWordToHashMap(keyWordSet);
			logger.info("set zookeeper listener start.............................");
			zkClient.subscribeDataChanges(SENSITIVE_WORD_PATH, new IZkDataListener() {
				public void handleDataDeleted(String dataPath) throws Exception {
					
				} 
				public void handleDataChange(String dataPath, byte[] data) throws Exception {
					String word = new String(data);
					logger.info("sensitive word changed,word="+word+"............"); 
					if(word!=null&&!word.trim().equals("")){ 
						word = word.trim();
						if(word.startsWith("insert_")){
							addOneSensitiveWord(word.substring(word.indexOf("_")+1));
						}else if(word.startsWith("update_")){
							String []s = word.split("_");
							updateSensitiveWord(s[1],s[2]);
						}else if(word.startsWith("delete_")){
							removeSensitiveWord(word.substring(word.indexOf("_")+1)); 
						}else if(word.equals("reload")){
							//重新加载敏感词
							addSensitiveWordToHashMap(getWords());
						}
					}
					logger.info("sensitive word reload end.......................");
				}
			});
		} catch (Exception e) {
			logger.error("敏感词初始化错误",e);
		}
		return sensitiveWordMap;
	}
	
	private Set<String> getWords(){
    	List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("status", ""+0));
		List<SensitiveWord> ls =sensitiveWordRepository.findAll(filters,null);
		Set<String> set = new HashSet<String>();
		if(ls!=null&&ls.size()>0){
			for(SensitiveWord one:ls)
				set.add(one.getWord());
		} 
		return set;
    }
	
	@SuppressWarnings("rawtypes")
	public Map initKeyWord(Set<String> keyWordSet){
		try { 
			//将敏感词库加入到HashMap中
			addSensitiveWordToHashMap(keyWordSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	/**
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
	 * 中 = {
	 *      isEnd = 0
	 *      国 = {<br>
	 *      	 isEnd = 1
	 *           人 = {isEnd = 0
	 *                民 = {isEnd = 1}
	 *                }
	 *           男  = {
	 *           	   isEnd = 0
	 *           		人 = {
	 *           			 isEnd = 1
	 *           			}
	 *           	}
	 *           }
	 *      }
	 *  五 = {
	 *      isEnd = 0
	 *      星 = {
	 *      	isEnd = 0
	 *      	红 = {
	 *              isEnd = 0
	 *              旗 = {
	 *                   isEnd = 1
	 *                  }
	 *              }
	 *      	}
	 *      }
	 * @author chenming 
	 * @date 2014年4月20日 下午3:04:20
	 * @param keyWordSet  敏感词库
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
		String key = null;  
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		//迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while(iterator.hasNext()){
			key = iterator.next();    //关键字
			nowMap = sensitiveWordMap;
			for(int i = 0 ; i < key.length() ; i++){
				char keyChar = key.charAt(i);       //转换成char型
				Object wordMap = nowMap.get(keyChar);       //获取
				
				if(wordMap != null){        //如果存在该key，直接赋值
					nowMap = (Map) wordMap;
				}
				else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String,String>();
					newWorMap.put("isEnd", "0");     //不是最后一个
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				
				if(i == key.length() - 1){
					nowMap.put("isEnd", "1");    //最后一个
				}
			}
		}
	}
	
	private void updateSensitiveWord(String oldkey,String newkey){
		this.removeSensitiveWord(oldkey);
		this.addOneSensitiveWord(newkey);
	}
	
	private void addOneSensitiveWord(String key) { 
		Map nowMap = sensitiveWordMap;
		Map<String, String> newWorMap = null;  
		for(int i = 0 ; i < key.length() ; i++){
			char keyChar = key.charAt(i);       //转换成char型
			Object wordMap = nowMap.get(keyChar);//获取 
			if(wordMap != null){//如果存在该key，直接赋值
				nowMap = (Map) wordMap;
			}
			else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
				newWorMap = new HashMap<String,String>();
				newWorMap.put("isEnd", "0");     //不是最后一个
				nowMap.put(keyChar, newWorMap);
				nowMap = newWorMap;
			} 
			if(i == key.length() - 1){
				nowMap.put("isEnd", "1");    //最后一个
			}
		} 
	}
	
	private void removeSensitiveWord(String key) { 
		Map nowMap = null;  
		nowMap = sensitiveWordMap; 
		Map [] allMap = new HashMap[key.length()+1];
		allMap[0]=sensitiveWordMap;
		for(int i = 0 ; i < key.length() ; i++){
			char keyChar = key.charAt(i);//转换成char型
			allMap[i+1] = (Map)nowMap.get(keyChar);//获取 
			if(allMap[i+1]!= null){//如果存在该key，直接赋值 
				allMap[i+1].put("curkey", keyChar);
				if(i==key.length()-1){ 
					for(int j=i+1;j>0;j--){
						if(allMap[j].size()==2){
							allMap[j-1].remove(allMap[j].get("curkey")); 
						} 
					} 
				}else{
					nowMap = (Map) nowMap.get(keyChar);
				}
			}  
		} 
	}

	/**
	 * 读取敏感词库中的内容，将内容添加到set集合中
	 * @author chenming 
	 * @date 2014年4月20日 下午2:31:18
	 * @return
	 * @version 1.0
	 * @throws Exception 
	 */
	@SuppressWarnings("resource")
	private Set<String> readSensitiveWordFile() throws Exception{
		Set<String> set = null;
		
		File file = new File("D:\\SensitiveWord.txt");    //读取文件
		InputStreamReader read = new InputStreamReader(new FileInputStream(file),ENCODING);
		try {
			if(file.isFile() && file.exists()){      //文件流是否存在
				set = new HashSet<String>();
				BufferedReader bufferedReader = new BufferedReader(read);
				String txt = null;
				while((txt = bufferedReader.readLine()) != null){    //读取文件，将文件内容放入到set中
					set.add(txt);
			    }
			}
			else{         //不存在抛出异常信息
				throw new Exception("敏感词库文件不存在");
			}
		} catch (Exception e) {
			throw e;
		}finally{
			read.close();     //关闭文件流
		}
		return set;
	} 
}
