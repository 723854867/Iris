package com.busap.vcs.srv.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Message;
import com.busap.vcs.srv.IHandlerMsgService;
import com.busap.vcs.srv.JedisService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BnoticeService implements IHandlerMsgService {
	private static final Logger logger = LoggerFactory.getLogger(BnoticeService.class);
	@Autowired
	JedisService jedisService;
	@Autowired
	ObjectMapper mapper;

	@Value("#{configProperties['user_msg_key_prefix']}")
	private String user_push_msg_key;
	@Value("#{configProperties['all_user_notice']}")
	private String user_notice;

	private static final String NOTICEKEY = "notice";

	@Value("#{configProperties['user_info_key']}")
	private String user_info_key = "USER_INFO_";

	public void dealMsg(Message msg) {
		try {
			switch (msg.getAction()) {
			case INSERT:
				Map<String, Object> dataMap = msg.getDataMap();
				// 发送范围，指定用户，所有用户
				String scope = (String) dataMap.get("scope");
				// 操作,打开应用，启动到视频、活动详情页
				String op = (String) dataMap.get("op");
				String targetId = (String) dataMap.get("targetid");
				String title = (String) dataMap.get("title");
				if ("all".equals(scope)) {
					Map<String, Object> vMap = new HashMap<String, Object>();
					vMap.put("title", title);
					// type:1评论，2赞，3关注，4系统消息
					vMap.put("type", 4);
					// tag:1视频，2活动，3列表
					if ("video".equals(op)) {
						vMap.put("tag", 1);
						vMap.put("targetid", Long.parseLong(targetId));
					} else if ("activity".equals(op)) {
						vMap.put("tag", 2);
						vMap.put("targetid", Long.parseLong(targetId));
					} else if ("app".equals(op)) {
						vMap.put("tag", 0);
						vMap.put("targetid", 0);
					} else {
						vMap.put("tag", 3);
						vMap.put("targetid", 0);
					}
					String content = mapper.writeValueAsString(vMap);
					jedisService.setValueToSortedSetInShard(user_notice, System.currentTimeMillis(), content);
				} else {
					String uids = (String) dataMap.get("uids");
					if (StringUtils.isNotBlank(uids)) {
						Map<String, Object> vMap = new HashMap<String, Object>();
						vMap.put("title", title);
						// type:1评论，2赞，3关注，4系统消息
						vMap.put("type", 4);
						// tag:1视频，2活动，3列表
						if ("video".equals(op)) {
							vMap.put("tag", 1);
							vMap.put("targetid", Long.parseLong(targetId));
						} else if ("activity".equals(op)) {
							vMap.put("tag", 2);
							vMap.put("targetid", Long.parseLong(targetId));
						} else if ("app".equals(op)) {
							vMap.put("tag", 0);
							vMap.put("targetid", 0);
						} else {
							vMap.put("tag", 3);
							vMap.put("targetid", 0);
						}
						String content = mapper.writeValueAsString(vMap);
						String[] uidArray = uids.split(";");
						for (String uid : uidArray) {
							if (StringUtils.isNotBlank(uid)) {
								jedisService.setValueToMap(user_push_msg_key + uid, NOTICEKEY, content);
							}
						}
					}
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("error msg {}|{}|{}|{}|{}", e.getMessage(), msg.getModule(), msg.getAction(), msg.getDataMap(), msg.getConditionMap());
		}

	}
}
