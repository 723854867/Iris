package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.AutoChat;

@Resource(name = "autoChatRepository")
public interface AutoChatRepository extends BaseRepository<AutoChat, Long> {

}
