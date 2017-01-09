package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.AutoChatType;

@Resource(name = "autoChatTypeRepository")
public interface AutoChatTypeRepository extends	BaseRepository<AutoChatType, Long> {

}
