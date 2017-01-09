package com.busap.vcs.service;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.Notification;

public interface NotificationService extends BaseService<Notification,Long>{

	Page<Notification> findMyNotifications(Integer page, Integer size, String uid);
}
