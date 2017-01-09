package com.busap.vcs.data.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.busap.vcs.data.enums.Sex;
import com.busap.vcs.data.enums.UserStatus;
import com.busap.vcs.data.enums.UserType;
import com.busap.vcs.data.enums.VipStatus;
import com.busap.vcs.operate.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * restwww 前端用户使用
 * @author meizhiwen
 *
 */
@Entity
@Table(name = "ruser")
@JsonIgnoreProperties(value = {"password", "activationCode", "roles","salt"})

public class Ruser extends BaseEntity {
	
	@NoRedis
    private static final long serialVersionUID = -7519486823153844426L;
    
    public enum ThirdUser{
    	
    	self("busonline","巴士在线"),sina("sina","新浪"),qq("qq","QQ"),wechat("wechat","微信");
    	
    	private ThirdUser(String c,String description){
    		this.value=c;
    		this.description=description;
    	}
    	
    	private String value; 
    	private String description;  
    	
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
    	
    }

    @NotEmpty(groups = {Save.class,Update.class})
    @Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5 -]+$")
    @Length(min = 2, max = 20)
    @Column(nullable = false, updatable = true, unique = true)
    private String username;

    @NoRedis
    @NotEmpty(groups = {Save.class,Update.class})
    @Pattern(regexp = "^[^\\s&\"<>]+$")
    @Length(min = 4, max = 200)
    @Column(nullable = false)
    private String password;

    @Email
    @Length(max = 200)
    @Column(length = 200)
    private String email;


    @Column(nullable = true, updatable = true, name = "open_id", length = 100)
    private String openId;

    @Column(nullable = false)
    private String type=UserType.普通用户.getName();

    @Column(nullable = true)
    private String phone;
    
    @Column(nullable = true)
    private String bandPhone; //绑定的手机号码，可以用来登陆，不能重复
    
    @Transient
    private Integer canBand; //是否能绑定手机号，1：是，0：否

    @Length(max = 200)
    @Column(nullable = true, length = 200)
    private String name;//名字
    @Length(max = 255)
    @Column(nullable = true, length = 255)
    private String department;

    @Column(length = 255)
    private String activationCode;

    @NotNull
    @Column(nullable = false)
    private Boolean isEnabled=true;

    @NotNull
    @Column(nullable = false)
    private Boolean isLocked=false;

    @Column
    private Integer loginFailureCount;

    @Column
    private Date lockedDate;

    @Column
    private Date loginDate;


    @Column(length = 128)
    private String loginIp;
    
    @Column
    private Integer fansCount = 0;
    
    @Column
    private Integer attentionCount = 0;
    
    @Column(name = "sign_sum")
    private Integer signSum=0;//积分数
    
    /*密码盐*/
    @Column
    private String salt;
    //性别
    private String sex=Sex.不男不女.getName();
    //所在地
    private String addr;
    //个性签名
    private String signature;
    
    private String pic;//用户头像

    private String homePic;//个人首页背景图 

    private Integer videoCount=0;//个人上传并通过审核的视频数 

    private Integer allVideoCount=0;//个人上传的视频总数 
    
    @Transient
    private Integer allPlaybackCount=0;//个人直播回放总数
    
    @Transient
    private Integer allPicCount=0;//个人上传图片总数
    
    private String thirdFrom=ThirdUser.self.getValue();//第三方联合登录的用户来源   
    
    private String thirdUserame;//第三方账户 
    
    private String wechatUnionid; //微信unionid
    
    @Transient
    private int isAttention = 0;//是否关注该用户

    /**
     * 用户状态，默认激活 0,禁言 1,封号 2
     */
    @Column(name = "stat",columnDefinition = "tinyint NOT NULL DEFAULT 0",nullable=true)
    private Integer stat = UserStatus.active.getStat();
    
    /**
     * 用户vip等级状态，暂时支持（普通 0 默认，蓝V 1，黄V 2，绿V 3）
     */
    @Column(name = "vstat",columnDefinition = "tinyint NOT NULL DEFAULT 0",nullable=true)
    private Integer vipStat = VipStatus.normal.getStat();
    
    /**
     * vip权重，推荐时按照权重排序后推荐
     */
    @Column(name = "vip_weight",columnDefinition = "int NOT NULL DEFAULT 0",nullable=true)
    private Integer vipWeight = 0;
    
    /**
     * 直播权重
     */
    @Column(name = "live_weight",columnDefinition = "int NOT NULL DEFAULT 0",nullable=true)
    private Integer liveWeight = 0;
    
    @Transient
    private int age;//年龄
    
    private Date birthday;//生日
	//人气值
    @Column(name = "day_popularity",columnDefinition = "double(32,2)  NULL DEFAULT '0.00'",nullable=true)
	private Double dayPopularity = 0.00;
    
  //真的马甲用户是否可进入排行榜，0或者null不限制，1不能上榜
    @Column(name = "rank_able",columnDefinition = "int  NULL DEFAULT 0",nullable=true)
	private Integer rankAble = 0;
    
  //人气排名
  	@Transient
  	private int hotRank = 0;

    //24小时人气
    @Column(name = "24hour_popularity",columnDefinition = "double(6,2)  NULL DEFAULT '0.00'",nullable=true)
    private Double twentyFourHourPopularity = 0.00;

    //周人气
    @Column(name = "week_popularity",columnDefinition = "double(6,2)  NULL DEFAULT '0.00'",nullable=true)
    private Double weekPopularity = 0.00;

    //月人气
    @Column(name = "month_popularity",columnDefinition = "double(8,2)  NULL DEFAULT '0.00'",nullable=true)
    private Double monthPopularity = 0.00;

    //年人气
    @Column(name = "year_popularity",columnDefinition = "double(10,2)  NULL DEFAULT '0.00'",nullable=true)
    private Double yearPopularity = 0.00;
    
    private String regPlatform; //注册平台:ion/android/h5

    @Column(name = "allow_publish",columnDefinition = "int(4) not NULL DEFAULT 1",nullable=true)
    private Integer allowPublish;//是否允许后台发布视频 0允许 1不允许

    @Column(name = "diamond_count", columnDefinition = "int(10) not NULL DEFAULT 0", nullable = true)
    private Integer diamondCount = 0; // 钻石总数
    
    @Column(nullable = true,columnDefinition = "int(8) DEFAULT 0")
    private Integer isAnchor = 0;//是否是主播，1：是，0：不是，默认不是
    
    //是否允许评论，默认1
  	@Column(nullable = true,columnDefinition = "int(8) DEFAULT 1")
  	private int allowEvaluation = 1;
  	
  	@Transient
  	private String existIdentify; //认证信息是否存在，1：存在，0：不存在
  	
  	@Transient
  	private String identifyStatus;//认证状态，0：认证中，1：认证通过，-1：认证失败

    @Column(name = "organization_id",columnDefinition = "bigint(8) NULL",nullable=true)
    private Long organizationId;

    @Column(name = "is_blacklist",columnDefinition = "int(4) NULL DEFAULT 1",nullable=true)
    private Integer isBlacklist; //0:加入黑名单 1:未加入黑名单

    @Column(name = "live_type",columnDefinition = "int(4) NULL DEFAULT 0",nullable=true)
    private Integer liveType; //直播类型 0：手机 1：摄像机
    
    private Long liveSetting;//直播马甲设置
    
    //推荐位 0：可以被推荐 1：不可被推荐
  	@Column(nullable = true,columnDefinition = "int(8) DEFAULT 0")
  	private int recommendBit = 0;

    @Transient
    private String  streamUrl; //播放地址
    
    @Transient
    private Integer canLinkMic = 1; //是否可以连麦，1：可以，0：不可以
    
    private String appVersion;
    
    private Integer berryCount = 0; // 草莓数量
    
    private String ifa;

    public Integer getAllVideoCount() {
		return allVideoCount;
	}

	public void setAllVideoCount(Integer allVideoCount) {
		this.allVideoCount = allVideoCount;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getAge() {
		if(birthday==null)
			return 0;
		else{
			Calendar old = Calendar.getInstance();
			old.setTime(birthday); 
			Calendar now = Calendar.getInstance(); 
			return (now.get(Calendar.YEAR)-old.get(Calendar.YEAR));
		} 
	} 

	public String getThirdFrom() {
		return thirdFrom;
	}

	public void setThirdFrom(String thirdFrom) {
		this.thirdFrom = thirdFrom;
	}

	public String getThirdUserame() {
		return thirdUserame;
	}

	public void setThirdUserame(String thirdUserame) {
		this.thirdUserame = thirdUserame;
	}

	public Integer getVideoCount() {
		return videoCount;
	}

	public void setVideoCount(Integer videoCount) {
		this.videoCount = videoCount;
	}

	public String getHomePic() {
		return homePic;
	}

	public void setHomePic(String homePic) {
		this.homePic = homePic;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

//	@NoRedis
//	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Wrole.class)
//    @JoinTable(name = "ruser_rrole")
//    private List<Wrole> roles = new ArrayList<Wrole>();

    public String getCredentialsSalt() {
		return username + salt;
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
    	if(StringUtils.isBlank(name)){
    		if(username.trim().matches("\\d{11}")){ //是否电话号码
    			return username.substring(0,3)+"****"+username.substring(8,10);
    		}else{
    			return username;
    		}
    	}
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }


    public Boolean getIsLocked() {
        return isLocked;
    }

    public int getHotRank() {
		return hotRank;
	}

	public void setHotRank(int hotRank) {
		this.hotRank = hotRank;
	}

	public Double getDayPopularity() {
		return dayPopularity;
	}

	public void setDayPopularity(Double popularity) {
		this.dayPopularity = popularity;
	}

	public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }


    public Integer getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    public Date getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


//    public List<Wrole> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(List<Wrole> roles) {
//        this.roles = roles;
//    }
    
    public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ruser)) return false;
        if (!super.equals(o)) return false;

        Ruser user = (Ruser) o;

        if (department != null ? !department.equals(user.department) : user.department != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (!type.equals(user.type)) return false;
        if (!username.equals(user.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
        return result;
    }

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return the addr
	 */
	public String getAddr() {
		return addr;
	}

	/**
	 * @param addr the addr to set
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public Integer getVipStat() {
		return vipStat;
	}

	public void setVipStat(Integer vipStat) {
		this.vipStat = vipStat;
	}

	public Integer getFansCount() {
		return fansCount;
	}

	public void setFansCount(Integer fansCount) {
		this.fansCount = fansCount;
	}

	public Integer getAttentionCount() {
		return attentionCount;
	}

	public void setAttentionCount(Integer attentionCount) {
		this.attentionCount = attentionCount;
	}

	public int getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(int isAttention) {
		this.isAttention = isAttention;
	}

	public Integer getSignSum() {
		return signSum;
	}

	public void setSignSum(Integer signSum) {
		this.signSum = signSum;
	}

	public Integer getVipWeight() {
		return vipWeight;
	}

	public void setVipWeight(Integer vipWeight) {
		this.vipWeight = vipWeight;
	}

	public Integer getRankAble() {
		return rankAble;
	}

	public void setRankAble(Integer rankAble) {
		this.rankAble = rankAble;
	}

    public Double getTwentyFourHourPopularity() {
        return twentyFourHourPopularity;
    }

    public void setTwentyFourHourPopularity(Double twentyFourHourPopularity) {
        this.twentyFourHourPopularity = twentyFourHourPopularity;
    }

    public Double getWeekPopularity() {
        return weekPopularity;
    }

    public void setWeekPopularity(Double weekPopularity) {
        this.weekPopularity = weekPopularity;
    }

    public Double getMonthPopularity() {
        return monthPopularity;
    }

    public void setMonthPopularity(Double monthPopularity) {
        this.monthPopularity = monthPopularity;
    }

    public Double getYearPopularity() {
        return yearPopularity;
    }

    public void setYearPopularity(Double yearPopularity) {
        this.yearPopularity = yearPopularity;
    }

	public String getRegPlatform() {
		return regPlatform;
	}

	public void setRegPlatform(String regPlatform) {
		this.regPlatform = regPlatform;
	}

	public String getWechatUnionid() {
		return wechatUnionid;
	}

	public void setWechatUnionid(String wechatUnionid) {
		this.wechatUnionid = wechatUnionid;
	}


	public Integer getLiveWeight() {
		return liveWeight;
	}

	public void setLiveWeight(Integer liveWeight) {
		this.liveWeight = liveWeight;
	}
    
    

    public Integer getAllowPublish() {
        return allowPublish;
    }

    public void setAllowPublish(Integer allowPublish) {
        this.allowPublish = allowPublish;
    }

    public Integer getDiamondCount() {
        return diamondCount;
    }

    public void setDiamondCount(Integer diamondCount) {
        this.diamondCount = diamondCount;
    }

	public Integer getIsAnchor() {
		return isAnchor;
	}

	public void setIsAnchor(Integer isAnchor) {
		this.isAnchor = isAnchor;
	}

	public int getAllowEvaluation() {
		return allowEvaluation;
	}

	public void setAllowEvaluation(int allowEvaluation) {
		this.allowEvaluation = allowEvaluation;
	}

	public String getExistIdentify() {
		return existIdentify;
	}

	public void setExistIdentify(String existIdentify) {
		this.existIdentify = existIdentify;
	}

	public String getIdentifyStatus() {
		return identifyStatus;
	}

	public void setIdentifyStatus(String identifyStatus) {
		this.identifyStatus = identifyStatus;
	}

	public String getBandPhone() {
		return bandPhone;
	}

	public void setBandPhone(String bandPhone) {
		this.bandPhone = bandPhone;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Integer getCanBand() {
		//如果用户是手机号注册，返回0
		if (CommonUtil.isPhone(username) || (bandPhone != null && CommonUtil.isPhone(bandPhone)) ){
			return 0;
		}
		return 1;
	}

	public void setCanBand(Integer canBand) {
		this.canBand = canBand;
	}

	public Integer getAllPlaybackCount() {
		return allPlaybackCount;
	}

	public void setAllPlaybackCount(Integer allPlaybackCount) {
		this.allPlaybackCount = allPlaybackCount;
	}

	public Integer getAllPicCount() {
		return allPicCount;
	}

	public void setAllPicCount(Integer allPicCount) {
		this.allPicCount = allPicCount;
	}

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getIsBlacklist() {
        return isBlacklist;
    }

    public void setIsBlacklist(Integer isBlacklist) {
        this.isBlacklist = isBlacklist;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getLiveType() {
        return liveType;
    }

    public void setLiveType(Integer liveType) {
        this.liveType = liveType;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

	public int getRecommendBit() {
		return recommendBit;
	}

	public void setRecommendBit(int recommendBit) {
		this.recommendBit = recommendBit;
	}

	public Long getLiveSetting() {
		return liveSetting;
	}

	public void setLiveSetting(Long liveSetting) {
		this.liveSetting = liveSetting;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Integer getBerryCount() {
		return berryCount;
	}

	public void setBerryCount(Integer berryCount) {
		this.berryCount = berryCount;
	}

	public String getIfa() {
		return ifa;
	}

	public void setIfa(String ifa) {
		this.ifa = ifa;
	}

	public Integer getCanLinkMic() {
		return canLinkMic;
	}

	public void setCanLinkMic(Integer canLinkMic) {
		this.canLinkMic = canLinkMic;
	}
	

}
