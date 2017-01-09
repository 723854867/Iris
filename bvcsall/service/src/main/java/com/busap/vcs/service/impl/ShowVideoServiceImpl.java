package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.ShowVideo;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.mapper.ShowDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.ShowVideoRepository;
import com.busap.vcs.data.repository.VideoRepository;
import com.busap.vcs.data.vo.ShowVO;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.ShowVideoService;

@Service("showVideoService")
public class ShowVideoServiceImpl extends BaseServiceImpl<ShowVideo, Long> implements
		ShowVideoService {
	
	private Logger logger = LoggerFactory.getLogger(ShowVideoServiceImpl.class);

	@Resource(name="showVideoRepository")
	private ShowVideoRepository showVideoRepository;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="videoRepository")
	private VideoRepository videoRepository;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Autowired
	private ShowDAO showDAO;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;

	@Resource(name="showVideoRepository")
	@Override
	public void setBaseRepository(BaseRepository<ShowVideo, Long> baseRepository) {
		// TODO Auto-generated method stub
		super.setBaseRepository(showVideoRepository);
	}
	
	@Override
	public List<ShowVideo> getShowList(String uid,Long timestamp, Integer count) {
		Map<String,Object> params = new HashMap<String,Object>(); 
		params.put("timestamp", (timestamp==null||timestamp.longValue()<=0)?null:new Date(timestamp));
		params.put("count", count); 
		List<ShowVideo> list = showDAO.getAllShow(params);
		for (ShowVideo show:list) {
			Video video = videoRepository.findOne(show.getVideoId());
			show.setPlayCount(video.getShowPlayCount());
			String userStr = show.getRefUserId();
			if (userStr != null && !"".equals(userStr)){
				String[] userIds = userStr.split(",");
				List<Map<String,String>> userList = new ArrayList<Map<String,String>>();
				for (int i=0;i<userIds.length;i++){
					Map<String,String> user = ruserService.getUserFromRedis(Long.valueOf(userIds[i]));
					if (user != null && !user.isEmpty()) {
						if ( uid != null && !"".equals(uid)){
							Long userId = Long.valueOf(user.get("id"));
							user.put("isAttention", String.valueOf(attentionService.isAttention(Long.valueOf(uid),userId )));
						}
						userList.add(user);
					}
				}
				show.setUserList(userList);
			}
			
		}
		return list;
	}

	@Override
	public ShowVideo getShowByVideoId(Long videoId) {
		ShowVideo show = showDAO.getShowByVideoId(videoId);
		return show;
	}

	@Override
	public Page findShowVideos(Integer pageNo, Integer pageSize) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pageSize", pageSize);
		params.put("pageStart", (pageNo-1)*pageSize);
		List<ShowVO> fbList = showDAO.findAllShows(params);
		Integer totalCount = showDAO.findAllShowsCount();
		Page pinfo = new PageImpl(fbList,new PageRequest(pageNo-1, pageSize, null),totalCount);
		return pinfo;
	}
}
