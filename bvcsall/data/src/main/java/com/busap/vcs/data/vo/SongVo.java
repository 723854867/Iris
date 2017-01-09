package com.busap.vcs.data.vo;

/**
 * Created by busap on 2016/5/30.
 */
public class SongVo {

    private Long id; //歌曲id

    private String name; //歌曲名称

    private String singerName; //歌手名称

    private String singerTypeName; //歌手分类名称

    private String packageUrl; //伴奏文件

    private Long packageSize; //伴奏文件大小

    private String albumName; //专辑名称

    private String albumCover; //专辑封面地址

    private Long type; //类型 1伴奏 2原唱

    private Long downloadCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerTypeName() {
        return singerTypeName;
    }

    public void setSingerTypeName(String singerTypeName) {
        this.singerTypeName = singerTypeName;
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

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }
}
