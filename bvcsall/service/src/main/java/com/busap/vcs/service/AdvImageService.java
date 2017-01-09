package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.AdvImage;


public interface AdvImageService extends BaseService<AdvImage, Long> {
	AdvImage findOne(long id);
	
	List<AdvImage> findAdvImages();
}
