package com.busap.vcs.service.impl;

import java.text.SimpleDateFormat;
import java.util.*; 

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.Favorite;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.mapper.VideoDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.FavoriteRepository;
import com.busap.vcs.data.repository.VideoRepository;
import com.busap.vcs.service.FavoriteService;
import com.busap.vcs.service.VideoService;

@Service("favoriteService")
public class FavoriteServiceImpl extends BaseServiceImpl<Favorite, Long> implements
FavoriteService {
 
	
	@Resource(name = "favoriteRepository")
	private FavoriteRepository favoriteRepository;
	
	@Resource(name="videoRepository") 
	private VideoRepository videoRepository;
	
	@Resource(name="videoService")
	private VideoService videoService;
	
	@Autowired
	VideoDAO videoDAO;
	 
	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

	@Resource(name = "favoriteRepository")
	@Override
	public void setBaseRepository(BaseRepository<Favorite, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}  

	//获取一个用户的收藏 
	public Page<Video> findFavoriteList(Integer page, Integer size, String uid){
		Sort s = new Sort(Direction.DESC,"createDate");
		Pageable p = new PageRequest(page, size,s);
		List<Filter> filters = new ArrayList<Filter>(1);
		filters.add(new Filter("creatorId", uid)); 
		Page<Favorite> fls = this.findAll(p, filters);  
		List<Video> vls = new ArrayList<Video>();
		if(fls.getContent()!=null&&fls.getContent().size()>0){ 
			for(Favorite f:fls.getContent()){ 
				Video v = videoRepository.findOne(f.getVideoId());  
				if(v!=null){  
					v.setFavorite(true);
					if(uid!=null&&uid.trim().equals(""))
						v.setPraise(videoService.praised(Long.parseLong(uid), v.getId()));
					v.setShowDate(f.getCreateDateStr());
					vls.add(v);  
				}
			}
		}
		Page<Video> ret = new PageImpl<Video>(vls, p, fls.getTotalElements()); 
		return ret;
	} 
	
	//获取一个用户的收藏 
	public List<Video> findFavoriteListByLastId(Date date, Integer count,String uid){
//    	List<Filter> filters = new ArrayList<Filter>();
//    	filters.add(new Filter("creatorId",uid)); 
//    	//startId==0表示最新，startId>0表示从startId起的老数据
//    	if(date!=null){
//        	filters.add(Filter.lt("createDate", date));  
//    	}
//    	Sort s = new Sort(Sort.Direction.DESC,"createDate");  
//    	List<Favorite> ls = this.findAll(0, count, filters, s);  
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("date", date);
    	map.put("count", count);
    	map.put("uid", uid);
    	List<Video> ls=videoDAO.getFavoriteList(map);
//    	List<Video> vls = new ArrayList<Video>();
//		if(ls!=null&&ls.size()>0){ 
//			for(Video v:ls){ 
////				Video v = videoRepository.findOne(f.getVideoId());  
//				if(v!=null){  
//					v.setFavorite(true);
//					if(uid!=null&&!uid.trim().equals(""))
//						v.setPraise(videoService.praised(Long.parseLong(uid), v.getId()));
////					v.setShowDate(f.getCreateDateStr());
////					vls.add(v);  
//				}
//			}
//		}
    	return  ls; 
	} 
	
	/**
	 * 条件查询收藏视频
	 * @param page
	 * @param size
	 * @param filters
	 * @return
	 */
	public Page<Video> findFavoriteList(Integer page, Integer size,Long uid,List<Filter> filters){
		Sort s = new Sort(Direction.DESC,"createDate");
		Pageable p = new PageRequest(page, size,s);
		
		if(filters==null){
			filters=new ArrayList<Filter>();
		}
		
		Filter creatorF=new Filter("creatorId", uid);
		filters.add(creatorF);
		
		Page<Favorite> fls = this.findAll(p, filters);

		List<Long> ids=null;
		if(fls.getContent()!=null&&fls.getContent().size()>0){
			ids = new ArrayList<Long>();
			for(Favorite f:fls.getContent()){ 
				ids.add(f.getVideoId());
			}
		}
		if(ids==null||ids.isEmpty()){
			return new PageImpl<Video>(new ArrayList<Video>());
		}
		filters.clear();
		//视频ID 列表
		filters.add(Filter.in("id", ids));
		
		Page<Video> ret = videoRepository.findAll(p, filters);
		return ret;
	}
	
	public void saveFavorite(Favorite f){
		Favorite has = favoriteRepository.findByCreatorIdAndVideoId(f.getCreatorId(), f.getVideoId());
		if(has==null){ 
			this.save(f);
			videoRepository.incFavoriteCount(f.getVideoId()); 
		}
	}

	public void deleteFavorite(Long videoId,Long creator){
		Favorite has = favoriteRepository.findByCreatorIdAndVideoId(creator, videoId);
		if(has!=null){
			favoriteRepository.deleteByCreatorIdAndVideoId(creator, videoId);  
			videoRepository.decFavoriteCount(videoId);
		}
	}
		
	public void deleteFavorite(Long creator){
		List<Favorite> favs=favoriteRepository.findByCreatorId(creator);
		for (Favorite favorite : favs) {
			videoRepository.decFavoriteCount(favorite.getVideoId());
		}
		favoriteRepository.deleteInBatch(favs);
	}
	
	public boolean isFavorited(Long videoId,Long creator){
		List<Filter> filters = new ArrayList<Filter>(2); 
		filters.add(new Filter("creatorId", creator));
		filters.add(new Filter("videoId", videoId));
		long c =favoriteRepository.count(filters);
		if(c>0)
			return true;
		else
			return false;
	} 
}
