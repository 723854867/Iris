package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.Tag;
import com.busap.vcs.data.enums.TagStatus;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.TagRepository;
import com.busap.vcs.service.TagService;

@Service("tagService")
public class TagServiceImpl extends BaseServiceImpl<Tag, Long> implements
TagService { 
	@Resource(name = "tagRepository")
	private TagRepository tagRepository; 

	@Resource(name = "tagRepository")
	@Override
	public void setBaseRepository(BaseRepository<Tag, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}

	@Override
	public List findByStatus(String status) {
		return this.tagRepository.findByStatus(status);
	}

	@Override
	public List<Tag> findMyTags(Long uid,boolean communal) {
		
		if(uid==null){
			return null;
		}
		Sort sort=new Sort(Sort.Direction.ASC,"createDate");
		List<Filter> filters=new ArrayList<Filter>();
		Filter f=Filter.eq("creatorId", uid);
		filters.add(f);
		
		List<Tag> tags=this.tagRepository.findAll(filters, sort);
		
		if(communal){
			filters.clear();
			filters.add(Filter.isNull("creatorId"));
			tags.addAll(this.tagRepository.findAll(filters, sort));
		}
		
		return tags;
	}  
	
}
