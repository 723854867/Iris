package com.busap.vcs.data.repository;

import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.entity.LiveActivity;
import org.springframework.data.jpa.repository.Query;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Resource(name = "giftRepository")
public interface GiftRepository extends BaseRepository<Gift, Long> {

	@Query(nativeQuery=true,value="select * from gift where id in ?1")
	public List<Gift> findGiftsByIds (Collection ids);

	@Query(nativeQuery=true,value="select * from gift where is_exclusive=1")
	public List<Gift> findExclusiveGifts ();

	@Query(nativeQuery=true,value="select * from gift where is_exclusive=1 and state = ?1")
	public List<Gift> findExclusiveGiftsByState (Integer state);

}
