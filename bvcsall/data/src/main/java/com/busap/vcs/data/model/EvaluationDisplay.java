package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.Evaluation;

/**
 * Created by huoshanwei on 2015/9/29.
 */
public class EvaluationDisplay extends Evaluation {

    private String name;

    private String username;

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
}
