package com.busap.vcs.data.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 遗漏回放信息补查
 * Created by busap on 2016/7/20.
 */
@Entity
@Table(name = "missing_playback_info")
public class MissingPlaybackInfo{

    @Id
    @Column(name = "persistent_id",columnDefinition = "varchar(100) NOT NULL",nullable=false)
    private String persistentId; //网宿回调唯一ID

    @Column(name = "room_id",columnDefinition = "bigint(8) NULL",nullable=true)
    private Long roomId;//房间ID

    @Column(name = "playkey",columnDefinition = "varchar(255) NULL",nullable=true)
    private String playkey;//回放url

    @Column(name = "stream_id",columnDefinition = "varchar(100) NULL",nullable=true)
    private String streamId;

    @Column(name = "create_at",columnDefinition = "timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'",nullable=true)
    private Date createDate;

    @Column(name = "modify_at",columnDefinition = "timestamp NULL DEFAULT CURRENT_TIMESTAMP",nullable=true)
    private Date modifyDate;

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getPlaykey() {
        return playkey;
    }

    public void setPlaykey(String playkey) {
        this.playkey = playkey;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
