package com.busap.vcs.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.busap.vcs.data.model.SingVoteDisplay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.constants.SingConstants;
import com.busap.vcs.data.entity.SingVote;
import com.busap.vcs.data.entity.VoiceList;
import com.busap.vcs.data.mapper.SingVoteDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SingVoteRepository;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.SingVoteService;
import com.busap.vcs.service.VoiceListService;

@Service("singVoteService")
public class SingVoteServiceImpl extends BaseServiceImpl<SingVote, Long> implements
SingVoteService {
	
	private Logger logger = LoggerFactory.getLogger(SingVoteServiceImpl.class);
	
	@Autowired
    JedisService jedisService;
	
	@Resource(name="singVoteRepository")
	private SingVoteRepository singVoteRepository;
	
	@Resource(name="voiceListService")
	private VoiceListService voiceListService;
	
	
	@Resource(name="singVoteRepository")
	@Override
	public void setBaseRepository(BaseRepository<SingVote, Long> baseRepository) {
		super.setBaseRepository(singVoteRepository);
	}
	
	@Resource
	private SingVoteDAO singVoteDAO;


	@Override
	public List<Map<String, Object>> getRank(Integer type,Integer count,Date startTime,Date endTime,Long uid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("count", count);
		params.put("uid", uid);
		if (type == 1) { //学员榜
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			return singVoteDAO.getMemberRank(params);
		} else if (type == 2) { //网综榜
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			return singVoteDAO.getNormalRank(params);
		} else if (type == 3) { //主播榜
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			return singVoteDAO.getAnchorRank(params);
		} else if (type == 4) { //贡献榜
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			return singVoteDAO.getContributeRank(params);
		} else if (type == 5) { //贡献榜总榜
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			return singVoteDAO.getContributeRankTotal(params);
		}
		return null;
	}


	@Override
	public void manualVote(Date createTime, Long destId,Long popularity,Long operationId) {
		SingVote sv = new SingVote();
		sv.setCreatorId(0l); //人工干预投票，投票人id默认为0
		sv.setCreateDate(createTime);
		sv.setDestId(destId);
		sv.setType(4);
		sv.setValue(popularity);
		sv.setOperationId(operationId);
		if (jedisService.isSetMemberInShard(SingConstants.SING_MEMBER, String.valueOf(destId))) {  //判断是否是新歌声学员
			sv.setUserType(1);
		} else {
			sv.setUserType(2);
		}
		sv.setPopularity(popularity);
		save(sv);
	}


	@Override
	public Long getPopularity(Long destId) {
		return singVoteRepository.getPopularity(destId);
	}


	@Override
	public Long getContributePopularity(Long creatorId) {
		return singVoteRepository.getContributePopularity(creatorId);
	}


	@Override
	public void createRank(Integer type) {
		Date currentTime = new Date();
		if (type == 1){
			//获得学员榜的配置
			VoiceList memberRankConifg = voiceListService.getRankConfig(currentTime, 1);
			if (memberRankConifg != null) {
				try {
					Integer count = memberRankConifg.getPersonNumber();
					Date startTime = memberRankConifg.getStartTime();
					Date endTime = memberRankConifg.getEndTime();
					List<Map<String,Object>> rank = getRank(1, count, startTime, endTime,null);
					if (rank != null && rank.size()>0) {
						jedisService.delete(SingConstants.MEMBER_RANK);
						//将排行信息存入redis
						for (int i=0;i<rank.size();i++) {
							Map<String,Object> map = rank.get(i);
							jedisService.setValueToSortedSetInShard(SingConstants.MEMBER_RANK, Double.parseDouble(String.valueOf(map.get("total"))), String.valueOf(map.get("destId")));
						}
					}
					//存入倒计时时间
					Long dateline = endTime.getTime()-currentTime.getTime()<0?0:endTime.getTime()-currentTime.getTime();
					jedisService.set(SingConstants.MEMBER_DATELINE, String.valueOf(dateline));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (type == 2) {
			//获得网综榜的配置
			VoiceList normalRankConifg = voiceListService.getRankConfig(currentTime, 2);
			if (normalRankConifg != null) {
				try {
					Integer count = normalRankConifg.getPersonNumber();
					Date startTime = normalRankConifg.getStartTime();
					Date endTime = normalRankConifg.getEndTime();
					List<Map<String,Object>> rank = getRank(2, count, startTime, endTime,null);
					if (rank != null && rank.size()>0) {
						jedisService.delete(SingConstants.NORMAL_RANK);
						//将排行信息存入redis
						for (int i=0;i<rank.size();i++) {
							Map<String,Object> map = rank.get(i);
							jedisService.setValueToSortedSetInShard(SingConstants.NORMAL_RANK, Double.parseDouble(String.valueOf(map.get("total"))), String.valueOf(map.get("destId")));
						}
					}
					//存入倒计时时间
					Long dateline = endTime.getTime()-currentTime.getTime()<0?0:endTime.getTime()-currentTime.getTime();
					jedisService.set(SingConstants.NORMAL_DATELINE, String.valueOf(dateline));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (type == 3) {
			//获得主播榜的配置
			VoiceList anchorRankConifg = voiceListService.getRankConfig(currentTime, 3);
			if (anchorRankConifg != null) {
				try {
					Integer count = anchorRankConifg.getPersonNumber();
					Date startTime = anchorRankConifg.getStartTime();
					Date endTime = anchorRankConifg.getEndTime();
					String url = anchorRankConifg.getUrl();
					List<Map<String,Object>> rank = getRank(3, count, startTime, endTime,null);
					if (rank != null && rank.size()>0) {
						jedisService.delete(SingConstants.ANCHOR_RANK);
						//将排行信息存入redis
						for (int i=0;i<rank.size();i++) {
							Map<String,Object> map = rank.get(i);
							jedisService.setValueToSortedSetInShard(SingConstants.ANCHOR_RANK, Double.parseDouble(String.valueOf(map.get("total"))), String.valueOf(map.get("destId")));
						}
					}
					//存入倒计时时间
					Long dateline = endTime.getTime()-currentTime.getTime()<0?0:endTime.getTime()-currentTime.getTime();
					jedisService.set(SingConstants.ANCHOR_DATELINE, String.valueOf(dateline));
					jedisService.set(SingConstants.ANCHOR_RANK_URL, url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (type == 4) {
			//获得贡献榜的配置
			VoiceList contributeRankConifg = voiceListService.getRankConfig(currentTime, 4);
			if (contributeRankConifg != null) {
				try {
					Integer count = contributeRankConifg.getPersonNumber();
					Date startTime = contributeRankConifg.getStartTime();
					Date endTime = contributeRankConifg.getEndTime();
					String url = contributeRankConifg.getUrl();
					List<Map<String,Object>> rank = getRank(4, count, startTime, endTime,null);
					if (rank != null && rank.size()>0) {
						jedisService.delete(SingConstants.CONTRIBUTE_RANK);
						//将排行信息存入redis
						for (int i=0;i<rank.size();i++) {
							Map<String,Object> map = rank.get(i);
							jedisService.setValueToSortedSetInShard(SingConstants.CONTRIBUTE_RANK, Double.parseDouble(String.valueOf(map.get("total"))), String.valueOf(map.get("creatorId")));
						}
					}
					//存入倒计时时间
					Long dateline = endTime.getTime()-currentTime.getTime()<0?0:endTime.getTime()-currentTime.getTime();
					jedisService.set(SingConstants.CONTRIBUTE_DATELINE, String.valueOf(dateline));
					jedisService.set(SingConstants.CONTRIBUTE_RANK_URL, url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (type == 5){
			//获得贡献榜总榜的配置
			VoiceList contributeRankTotalConifg = voiceListService.getRankConfig(currentTime, 5);
			if (contributeRankTotalConifg != null) {
				try {
					Integer count = contributeRankTotalConifg.getPersonNumber();
					Date startTime = contributeRankTotalConifg.getStartTime();
					Date endTime = contributeRankTotalConifg.getEndTime();
					List<Map<String,Object>> rank = getRank(5, count, startTime, endTime,null);
					if (rank != null && rank.size()>0) {
						jedisService.delete(SingConstants.CONTRIBUTE_RANK_TOTAL);
						//将排行信息存入redis
						for (int i=0;i<rank.size();i++) {
							Map<String,Object> map = rank.get(i);
							jedisService.setValueToSortedSetInShard(SingConstants.CONTRIBUTE_RANK_TOTAL, Double.parseDouble(String.valueOf(map.get("total"))), String.valueOf(map.get("creatorId")));
						}
					}
					//存入倒计时时间
					Long dateline = endTime.getTime()-currentTime.getTime()<0?0:endTime.getTime()-currentTime.getTime();
					jedisService.set(SingConstants.CONTRIBUTE_DATELINE_TOTAL, String.valueOf(dateline));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public List<SingVoteDisplay> querySingVote(Map<String, Object> params) {
		return singVoteDAO.selectSingVote(params);
	}


	@Override
	public Double getUserPopularity(Long uid, Integer type) {
		Date currentTime = new Date();
		if (type == 1) {
//			VoiceList memberRankConifg = voiceListService.getRankConfig(currentTime, 1);
//			if (memberRankConifg != null) {
//				try {
//					Date startTime = memberRankConifg.getStartTime();
//					Date endTime = memberRankConifg.getEndTime();
//					List<Map<String,Object>> rank = getRank(1, 1, startTime, endTime,uid);
//					if (rank != null && rank.size() >0){
//						return Double.parseDouble(String.valueOf(rank.get(0).get("total")));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			Double popularity = jedisService.zscore(SingConstants.MEMBER_RANK, String.valueOf(uid));
			return popularity == null ?0d:popularity;
		} else if (type == 2) {
//			VoiceList normalRankConifg = voiceListService.getRankConfig(currentTime, 2);
//			if (normalRankConifg != null) {
//				try {
//					Date startTime = normalRankConifg.getStartTime();
//					Date endTime = normalRankConifg.getEndTime();
//					List<Map<String,Object>> rank = getRank(2, 1, startTime, endTime,uid);
//					if (rank != null && rank.size() >0){
//						return Double.parseDouble(String.valueOf(rank.get(0).get("total")));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			Double popularity = jedisService.zscore(SingConstants.NORMAL_RANK, String.valueOf(uid));
			return popularity == null ?0d:popularity;
		} else if (type == 3) {
//			VoiceList anchorRankConifg = voiceListService.getRankConfig(currentTime, 3);
//			if (anchorRankConifg != null) {
//				try {
//					Date startTime = anchorRankConifg.getStartTime();
//					Date endTime = anchorRankConifg.getEndTime();
//					String url = anchorRankConifg.getUrl();
//					List<Map<String,Object>> rank = getRank(3, 1, startTime, endTime,uid);
//					if (rank != null && rank.size() >0){
//						return Double.parseDouble(String.valueOf(rank.get(0).get("total")));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			Double popularity = jedisService.zscore(SingConstants.ANCHOR_RANK, String.valueOf(uid));
			return popularity == null ?0d:popularity;
		} else if (type == 4) {
//			VoiceList contributeRankConifg = voiceListService.getRankConfig(currentTime, 4);
//			if (contributeRankConifg != null) {
//				try {
//					Date startTime = contributeRankConifg.getStartTime();
//					Date endTime = contributeRankConifg.getEndTime();
//					String url = contributeRankConifg.getUrl();
//					List<Map<String,Object>> rank = getRank(4, 1, startTime, endTime,uid);
//					if (rank != null && rank.size() >0){
//						return Double.parseDouble(String.valueOf(rank.get(0).get("total")));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			Double popularity = jedisService.zscore(SingConstants.CONTRIBUTE_RANK, String.valueOf(uid));
			return popularity == null ?0d:popularity;
		} else if (type == 5) {
//			VoiceList contributeRankTotalConifg = voiceListService.getRankConfig(currentTime, 5);
//			if (contributeRankTotalConifg != null) {
//				try {
//					Date startTime = contributeRankTotalConifg.getStartTime();
//					Date endTime = contributeRankTotalConifg.getEndTime();
//					List<Map<String,Object>> rank = getRank(5, 1, startTime, endTime,uid);
//					if (rank != null && rank.size() >0){
//						return Double.parseDouble(String.valueOf(rank.get(0).get("total")));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			Double popularity = jedisService.zscore(SingConstants.CONTRIBUTE_RANK_TOTAL, String.valueOf(uid));
			return popularity == null ?0d:popularity;
		}
		return 0d;
	}

}
