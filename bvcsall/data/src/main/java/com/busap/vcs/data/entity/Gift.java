package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by busap on 2015/12/22.
 */
@Entity
@Table(name = "gift")
public class Gift extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2574574944756658905L;

	@Column(name = "name",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String name;

    @Column(name = "gift_icon_url",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String giftIconUrl;//礼物icon url

    @Column(name = "gift_purpose",columnDefinition = "tinyint(1) NOT NULL",nullable=false)
    private Integer giftPurpose;//礼物type/礼物用途 1收视榜 2人气榜 3直播 4其他

    @Column(name = "effect_type",columnDefinition = "int(4) NOT NULL DEFAULT 0",nullable=false)
    private Integer effectType; //效果类型 0无动画 1雪花 2心跳方法 3位移 5雪花停留 6抛物线 7跑车 8城堡岛屿 9游轮

    @Column(name = "effect_file_url",columnDefinition = "varchar(255) NULL",nullable=true)
    private String effectFileUrl;//效果文件url

    @Column(name = "price",columnDefinition = "int(8) NOT NULL",nullable=false)
    private Integer price;//价格（拍币数量）

    @Column(name = "point",columnDefinition = "int(8) NULL",nullable=false)
    private Integer point;//点数 对应礼物类型 直播称为点数，其他暂为效果值 拍豆

    @Column(name = "state",columnDefinition = "tinyint(1) NOT NULL DEFAULT 1",nullable=false)
    private Integer state;//状态 1上架 0下架 默认为1

    @Column(name = "weight",columnDefinition = "double(6,2) NOT NULL DEFAULT 0.00",nullable=false)
    private Double weight;//权重

    @Column(name = "marker_state",columnDefinition = "enum('normal','new') DEFAULT 'normal'",nullable=true)
    private String markerState;//标记状态 如是否new 是否促销等

    @Column(name = "is_free",columnDefinition = "tinyint(1) NOT NULL DEFAULT 0",nullable=false)
    private Integer isFree; //1代表免费 0代表不免费

    @Column(name = "free_count",columnDefinition = "int(8) NOT NULL DEFAULT 0",nullable=false)
    private Integer freeCount;//免费次数
    
    @Column(name = "loop_support",columnDefinition = "tinyint(1) NOT NULL DEFAULT 0",nullable=false)
    private Integer loopSupport;//支持连续发送，1 支持   0 不支持
    
    @Column(name = "is_exclusive",columnDefinition = "tinyint(1) NOT NULL DEFAULT 0",nullable=false)
    private Integer isExclusive;//是否是专属礼物，1 是   0 不是
    
    @Column(name = "screenshot_support",columnDefinition = "tinyint(1) NOT NULL DEFAULT 0",nullable=false)
    private Integer screenshotSupport;//是否支持截屏，1 支持   0 不支持
    
    @Column(name = "screenshot_number",columnDefinition = "int(8) NOT NULL DEFAULT 0",nullable=false)
    private Integer screenshotNumber = 0;//截屏需要的赠送礼物的个数

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGiftIconUrl() {
        return giftIconUrl;
    }

    public void setGiftIconUrl(String giftIconUrl) {
        this.giftIconUrl = giftIconUrl;
    }

    public Integer getGiftPurpose() {
        return giftPurpose;
    }

    public void setGiftPurpose(Integer giftPurpose) {
        this.giftPurpose = giftPurpose;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    public String getEffectFileUrl() {
        return effectFileUrl;
    }

    public void setEffectFileUrl(String effectFileUrl) {
        this.effectFileUrl = effectFileUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getMarkerState() {
        return markerState;
    }

    public void setMarkerState(String markerState) {
        this.markerState = markerState;
    }

    public Integer getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(Integer freeCount) {
        this.freeCount = freeCount;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

	public Integer getLoopSupport() {
		return loopSupport;
	}

	public void setLoopSupport(Integer loopSupport) {
		this.loopSupport = loopSupport;
	}

	public Integer getIsExclusive() {
		return isExclusive;
	}

	public void setIsExclusive(Integer isExclusive) {
		this.isExclusive = isExclusive;
	}

	public Integer getScreenshotSupport() {
		return screenshotSupport;
	}

	public void setScreenshotSupport(Integer screenshotSupport) {
		this.screenshotSupport = screenshotSupport;
	}

	public Integer getScreenshotNumber() {
		return screenshotNumber;
	}

	public void setScreenshotNumber(Integer screenshotNumber) {
		this.screenshotNumber = screenshotNumber;
	}
	
	
}
