package com.busap.vcs.service;

import com.busap.vcs.data.entity.SensitiveWord;

import java.util.List;
import java.util.Map;


public interface SensitiveWordService extends BaseService<SensitiveWord, Long> {

	public void checkAndReplaceSensitiveWord(Object obj);
	
	public void sendWordToZookeeper(String word);

	List<SensitiveWord> querySensitiveWords(Map<String,Object> params);

	int insert(SensitiveWord sensitiveWord);

	int updateSensitiveWord(SensitiveWord sensitiveWord);

	int batchInsertSensitiveWord(List<SensitiveWord> list);

/*	Integer querySensitiveWordCount(Map<String,Object> params);*/

	int deleteBySensitiveWordId(Long id);

	SensitiveWord querySensitiveWord(Long id);

}
