package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Video;

public interface AdminVideoMapper {
	public List<Video> searchAdminVideo(Map<String, Object> map);
	
	public List<Video> searchAdminVideoActivity(Map<String, Object> map);
	
	public long searchAdminVideoCount(Map<String, Object> map);
	
	public long searchAdminVideoCountActivity(Map<String, Object> map);

	List<Video> selectAdministratorVideos(Map<String, Object> map);

	Integer selectAdministratorVideoCount(Map<String, Object> map);
}
