package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.MemFeedBack;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemFeedBackSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

public interface MemFeedBackMapper {

	@Options(useGeneratedKeys = true, keyColumn = "feedback_id", keyProperty = "feedbackId")
	@InsertProvider(type = MemFeedBackSQLBuilder.class, method = "insert")
	int insert(MemFeedBack mfb);
	/**
	 * 获取所有反馈信息
	 */
	@SelectProvider(type = MemFeedBackSQLBuilder.class, method = "getList")
	List<MemFeedBack> getList(Map<String, Object> map);
	/**
	 * 获取反馈信息总数
	 */
	@SelectProvider(type = MemFeedBackSQLBuilder.class, method = "getCount")
	int getCount(Map<String, Object> map);
}
