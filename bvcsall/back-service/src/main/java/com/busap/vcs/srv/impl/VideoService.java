package com.busap.vcs.srv.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Message;
import com.busap.vcs.dao.LabelDao;
import com.busap.vcs.data.entity.Label;
import com.busap.vcs.data.entity.LabelVideo;
import com.busap.vcs.srv.IHandlerMsgService;
import com.busap.vcs.srv.JedisService;
import com.busap.vcs.srv.SolrWoPaiTagService;
import com.busap.vcs.srv.SolrWoPaiVideoTagService;
import com.busap.vcs.srv.wsclient.WSUtil;

@Service
public class VideoService implements IHandlerMsgService {
	private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
	@Autowired
	JedisService jedisService;

	@Autowired
	private SolrWoPaiTagService woPaiTagService;

	@Autowired
	private SolrWoPaiVideoTagService solrWoPaiVideoTagService;

	@Autowired
	private LabelDao labelDao;

	private static final String VIDEO_CHECK = "Video_Check";

	public void dealMsg(Message msg) {
		try {
			switch (msg.getAction()) {
			case INSERT:
				Map<String, Object> dataMap = msg.getDataMap();
				long uid = Long.parseLong((String) dataMap.get("uid"));
				Set<String> tags = (Set<String>) dataMap.get("tags");
				// Object tags = dataMap.get("tags");
				String svid = (String) dataMap.get("vid");
				String isadmin = (String) dataMap.get("isadmin");
				logger.info("insert video,video id is {},isadmin flag is {}", svid, isadmin);
				if (StringUtils.isBlank(isadmin)) {
					jedisService.setValueToSortedSetInShard(VIDEO_CHECK, System.currentTimeMillis(), svid);
					//发送通知审核的消息
					WSUtil.sendMessage(svid);
				}

				if (CollectionUtils.isNotEmpty(tags)) {
					for (String tag : tags) {
						try {
							labelDao.insert(tag);
						} catch (Exception e) {
							logger.error("tag name {} is exists", tag);
						}
					}

					String[] tagStrs = new String[tags.size()];
					tags.toArray(tagStrs);
					labelDao.batchUpdateLabelNumByName(tagStrs);

					for (String tag : tags) {
						Label label = labelDao.getLabel(tag);
						if (label != null) {
							woPaiTagService.index(label.getId(), label.getName(), label.getNum());
						}
					}
					List<LabelVideo> lvList = labelDao.getLabelVideoList(Long.parseLong(svid));
					if (CollectionUtils.isNotEmpty(lvList)) {
						for (LabelVideo lv : lvList) {
							solrWoPaiVideoTagService.index(lv.getId(), lv.getLabelName(), lv.getVideoId());
						}
					}
				}
				break;
			case DELETE:
				// 异步处理wopaivideotag这个集合下的索引，主要是根据id删除document
				// 根据视频id，取关联表数据然后删除
				Map<String, Object> delDataMap = msg.getDataMap();
				String[] deledVids = (String[]) delDataMap.get("vids");
				if (deledVids != null && deledVids.length > 0) {
					for (String deledVid : deledVids) {
						long vid = Long.parseLong(deledVid);
						List<LabelVideo> lvList = labelDao.getLabelVideoList(vid);
						if (CollectionUtils.isNotEmpty(lvList)) {
							List<String> list = new ArrayList<String>(lvList.size());
							for (int i = 0; i < lvList.size(); i++) {
								list.add(lvList.get(i).getId().toString());
							}
							solrWoPaiVideoTagService.deleteDocsByIds(list);
							logger.debug("delete solr index by label video ids {}",list);
						}
					}
				}
				break;
			case UPDATE:
				//目前视频只能是管理控制台修改
				Map<String, Object> updateDataMap = msg.getDataMap();
				Set<String> updateTags = (Set<String>) updateDataMap.get("tags");
				String updateVid = (String) updateDataMap.get("vid");
				//删除索引
				try {
					solrWoPaiVideoTagService.deleteDocByQuery(updateVid);
				} catch (Exception e) {
					logger.error("delete video index error,video id is {},error msg is {}",updateVid,e.getMessage());
				}
				
				if (CollectionUtils.isNotEmpty(updateTags)) {
					for (String tag : updateTags) {
						try {
							labelDao.insert(tag);
						} catch (Exception e) {
							logger.error("tag name {} is exists", tag);
						}
					}

					String[] tagStrs = new String[updateTags.size()];
					updateTags.toArray(tagStrs);
					labelDao.batchUpdateLabelNumByName(tagStrs);

					for (String tag : updateTags) {
						Label label = labelDao.getLabel(tag);
						if (label != null) {
							woPaiTagService.index(label.getId(), label.getName(), label.getNum());
						}
					}
					List<LabelVideo> lvList = labelDao.getLabelVideoList(Long.parseLong(updateVid));
					if (CollectionUtils.isNotEmpty(lvList)) {
						for (LabelVideo lv : lvList) {
							solrWoPaiVideoTagService.index(lv.getId(), lv.getLabelName(), lv.getVideoId());
						}
					}
				}
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("error {}|{}|{}|{}|{}", msg.getModule(), msg.getAction(), msg.getDataMap(), msg.getConditionMap(), e.getMessage());
		}
	}
}
