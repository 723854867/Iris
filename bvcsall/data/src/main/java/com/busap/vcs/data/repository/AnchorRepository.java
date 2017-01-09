package com.busap.vcs.data.repository;


import javax.annotation.Resource;

import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.LiveActivity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Resource(name = "anchorRepository")
public interface AnchorRepository extends BaseRepository<Anchor, Long> {

    /**
     * 扣除主播金豆
     */
    @Modifying
    @Transactional
    @Query("update Anchor set pointCount = (pointCount - ?1)  where creatorId =?2 and pointCount >= ?1")
    public int exchangePoint(int point, long uid);


    /**
     * 用于回滚主播金豆
     */
    @Modifying
    @Transactional
    @Query("update Anchor set pointCount = (pointCount + ?1)  where creatorId =?2")
    public int addPoint(int point, long uid);
}
