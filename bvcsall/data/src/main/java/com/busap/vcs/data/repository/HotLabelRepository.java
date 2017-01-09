package com.busap.vcs.data.repository;

import javax.annotation.Resource;
import com.busap.vcs.data.entity.HotLabel;

@Resource(name = "hotLabelRepository")
public interface HotLabelRepository extends BaseRepository<HotLabel, Long> {
	
	
}
