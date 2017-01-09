package com.busap.vcs.data.model;

/**
 * Created by huoshanwei on 2015/11/4.
 */
public class ActivityVideoUser {

    private Long id;

    private String type;

    private String name;

    private String username;

    private Long vipStat;

    private String phone;

    private String pic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getVipStat() {
        return vipStat;
    }

    public void setVipStat(Long vipStat) {
        this.vipStat = vipStat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
