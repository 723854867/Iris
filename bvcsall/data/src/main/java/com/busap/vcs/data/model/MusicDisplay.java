package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.Music;

/**
 * Created by huoshanwei on 2015/9/22.
 */
public class MusicDisplay extends Music {

    private String createPerson;

    private String typeName;

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
