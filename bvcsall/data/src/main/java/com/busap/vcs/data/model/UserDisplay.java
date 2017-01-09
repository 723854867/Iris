package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.User;

/**
 * Created by huoshanwei on 2015/10/12.
 */
public class UserDisplay extends User {

    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
