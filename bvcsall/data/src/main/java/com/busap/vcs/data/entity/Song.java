package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 歌曲
 * Created by busap on 2016/5/30.
 */
@Entity
@Table(name = "song")
public class Song extends BaseEntity {

    private static final long serialVersionUID = 5049424224527421484L;

    @Column(name = "name",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String name; //歌曲名称

    @Column(name = "package_url",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String packageUrl; //文件压缩包地址

    @Column(name = "package_size",columnDefinition = "bigint(8) NOT NULL",nullable=false)
    private Long packageSize; //文件压缩包大小

    @Column(name = "album_id",columnDefinition = "bigint(8) NULL",nullable=true)
    private Long albumId; //专辑ID

    @Column(name = "singer_id",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String singerId; //歌手ID

    @Column(name = "type",columnDefinition = "int(4) NOT NULL",nullable=false)
    private Long type; //类型 1伴奏 2原唱

    @Column(name = "state",columnDefinition = "int(4) NOT NULL",nullable=false)
    private Long state; //状态 1上线 0下线

    @Column(name = "download_count",columnDefinition = "bigint(8) NOT NULL",nullable=false)
    private Long downloadCount; //下载量

    @Column(name = "language",columnDefinition = "int(4) NULL",nullable=true)
    private Long language;//语种

    @Column(name = "is_hd",columnDefinition = "int(4) NULL",nullable=true)
    private Long isHD;//是否高清 0否 1是

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public void setPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
    }

    public Long getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(Long packageSize) {
        this.packageSize = packageSize;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getSingerId() {
        return singerId;
    }

    public void setSingerId(String singerId) {
        this.singerId = singerId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Long getLanguage() {
        return language;
    }

    public void setLanguage(Long language) {
        this.language = language;
    }

    public Long getIsHD() {
        return isHD;
    }

    public void setIsHD(Long isHD) {
        this.isHD = isHD;
    }
}
