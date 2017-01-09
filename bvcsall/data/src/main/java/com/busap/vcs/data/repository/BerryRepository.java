package com.busap.vcs.data.repository;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.Berry;


@Resource(name = "berryRepository")
public interface BerryRepository extends BaseRepository<Berry, Long> {
	
	@Query(nativeQuery=true,value="select * from berry where dest_id=?1 order by create_at desc limit ?2,?3")
	public List<Berry> getPlantBerryList(Long userId,Integer start,Integer count);

}
