package com.busap.vcs.data.model;

/**
 * Created by busap on 2016/5/31.
 */
public class SingerDisplay {

    private Long id; //歌曲id

    private String name; //歌曲名称

    private String singerName; //歌手名称

    private Long singerType;

    private String singerTypeName; //歌手分类名称

    private Long state; //状态 1上架 0下架

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

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public Long getSingerType() {
        return singerType;
    }

    public void setSingerType(Long singerType) {
        this.singerType = singerType;
    }
}
