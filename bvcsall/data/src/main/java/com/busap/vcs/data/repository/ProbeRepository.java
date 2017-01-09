package com.busap.vcs.data.repository;

import javax.annotation.Resource;
import com.busap.vcs.data.entity.Probe;

/**
 * Created by zx.
 */
@Resource(name = "probeRepository")
public interface ProbeRepository extends BaseRepository<Probe, Long> { 
	 
}
