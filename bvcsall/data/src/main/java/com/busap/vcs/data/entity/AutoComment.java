package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "auto_comment")
public class AutoComment extends BaseEntity {

    private static final long serialVersionUID = -696415787873128188L;
    // 评论内容
    @Column(name = "comment",columnDefinition = "varchar(500)  NULL ",nullable=true)
    private String comment;

    // 是否可用状态
    @Column(name = "available",columnDefinition = "int(2)  NULL ",nullable=true)
    private Integer available;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }
}