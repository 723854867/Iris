package com.busap.vcs.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.mapper.SensitiveWordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.SensitiveWord;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SensitiveWordRepository;
import com.busap.vcs.service.SensitiveWordService;
import com.busap.vcs.service.SensitivewordFilter;
import com.github.zkclient.ZkClient;
import org.springframework.transaction.annotation.Transactional;

@Service("sensitiveWordService")
public class SensitiveWordServiceImpl extends BaseServiceImpl<SensitiveWord, Long> implements SensitiveWordService {
	
	private static final Logger logger = LoggerFactory.getLogger(SensitiveWordServiceImpl.class);
	 
	@Resource(name = "sensitivewordFilter")
	SensitivewordFilter sensitivewordFilter;
	
	//敏感词监听path
	private static final String SENSITIVE_WORD_PATH = "/main/word/keepword";
	@Autowired
	private ZkClient zkClient;
	
	@Resource(name = "sensitiveWordRepository")
    private SensitiveWordRepository sensitiveWordRepository;

	@Resource
	private SensitiveWordDao sensitiveWordDao;

    @Resource(name = "sensitiveWordRepository")
    @Override
    public void setBaseRepository(BaseRepository<SensitiveWord, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }  
	
	public void checkAndReplaceSensitiveWord(Object obj){ 
		try {     
	        Class clazz = obj.getClass();  
	        Field[] fields = obj.getClass().getDeclaredFields();//获得属性  
	        for (Field field : fields) { 
	        	String type = field.getType().getName();
	        	if(!"java.lang.String".equals(type)){
	        		continue;
	        	} 
	            PropertyDescriptor pd = new PropertyDescriptor(field.getName(),  clazz);  
	            Method getMethod = pd.getReadMethod();//获得get方法  
	            Method setMethod = pd.getWriteMethod();//获得get方法  
	            String o = (String)getMethod.invoke(obj);//执行get方法返回一个Object 
	            if(o==null||o.trim().equals(""))
	            	continue;
	            String s = sensitivewordFilter.replaceSensitiveWord(o, SensitivewordFilter.maxMatchType, "*"); 
	            setMethod.invoke(obj, s);
	        }  
	    } catch (Exception e) {
	        logger.error("敏感词过滤错误："+e.getMessage(), e);
	    }
	}  
	
	public void sendWordToZookeeper(String word){ 
		if(!zkClient.exists(SENSITIVE_WORD_PATH)){
			zkClient.createPersistent(SENSITIVE_WORD_PATH,true);
		}
		zkClient.writeData(SENSITIVE_WORD_PATH, word.getBytes());
	} 
	
	public static void main(String s[]){
		ZkClient zkClient = new ZkClient("192.168.108.160:2181",86400000,50000);
		if(!zkClient.exists(SENSITIVE_WORD_PATH)){  
			zkClient.createPersistent("/main/word/keepword",true);
		}  
		zkClient.writeData(SENSITIVE_WORD_PATH, "dsfdsf".getBytes());
	}

	@Override
	public List<SensitiveWord> querySensitiveWords(Map<String,Object> params){
		return sensitiveWordDao.select(params);
	}

	@Override
	public int insert(SensitiveWord sensitiveWord){
		return sensitiveWordDao.insert(sensitiveWord);
	}

	@Override
	public int updateSensitiveWord(SensitiveWord sensitiveWord){
		return sensitiveWordDao.update(sensitiveWord);
	}

	@Override
	@Transactional(readOnly = true,rollbackFor = Exception.class)
	public int batchInsertSensitiveWord(List<SensitiveWord> list){
		return sensitiveWordDao.batchInsertSensitiveWord(list);
	}

/*
	@Override
	public Integer querySensitiveWordCount(Map<String,Object> params){
		return sensitiveWordDao.selectCount(params);
	}
*/

	@Override
	public int deleteBySensitiveWordId(Long id){
		return sensitiveWordDao.deleteByPrimaryKey(id);
	}

	@Override
	public SensitiveWord querySensitiveWord(Long id){
		return sensitiveWordDao.selectByPrimaryKey(id);
	}

}
