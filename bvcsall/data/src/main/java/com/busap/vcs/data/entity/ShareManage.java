package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 分享管理
 * Created by Knight on 15/11/16.
 */
@Entity
@Table(name = "share_manage")
public class ShareManage extends BaseEntity {

    private static final long serialVersionUID = 4137345974167893891L;

    @Column(name = "share_type",columnDefinition = "int(4)  NULL ", nullable=true)
    private String shareType;

    @Column(name = "share_title",columnDefinition = "varchar(255) NULL ", nullable=true)
    private String shareTitle;

    @Column(name = "share_text",columnDefinition = "varchar(500)  NULL ", nullable=true)
    private String shareText;

    @Column(name = "share_img",columnDefinition = "varchar(500)  NULL ", nullable=true)
    private String shareImg;

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }
}
