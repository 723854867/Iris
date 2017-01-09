package com.busap.vcs.service;

import com.busap.vcs.data.entity.CustomizeActivity;

/**
 * Created by
 * User: zx
 */
public interface CustomizeActivityService extends BaseService<CustomizeActivity, Long> {

	

	public CustomizeActivity findCurrentCustomizeActivity();


}
