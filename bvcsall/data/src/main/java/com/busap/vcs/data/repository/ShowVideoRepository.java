package com.busap.vcs.data.repository;


import javax.annotation.Resource;

import com.busap.vcs.data.entity.ShowVideo;


/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "showVideoRepository")
public interface ShowVideoRepository extends BaseRepository<ShowVideo, Long> {
}
