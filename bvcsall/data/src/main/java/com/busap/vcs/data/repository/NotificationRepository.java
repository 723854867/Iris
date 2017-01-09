package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Notification;

@Resource(name = "notificationRepository")
public interface NotificationRepository extends BaseRepository<Notification, Long>{
	
}
