package com.busap.vcs.data.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "customize_activity")
public class CustomizeActivity extends BaseEntity{  
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8320033412611739655L;

	private String  title;//标题       
	
	private Integer  type; //1：url跳转 2：端内活动跳转 3：定制拍摄
	
	private String  url;//URL链接,类型为url跳转时填写
	
	private String  activityId;//URL链接,类型为端内活动跳转 和定制拍摄 时填写
	
	@Column(name = "activity_des",columnDefinition = "varchar(1024)  NULL ",nullable=true)
	private String  activityDes;  //活动描述,类型为定制拍摄时填写
	
	@Column(name = "share_des",columnDefinition = "varchar(1024)  NULL ",nullable=true)
	private String  shareDes;  //分享描述,类型为定制拍摄时填写
	
	private String  headId;//指定片头,类型为定制拍摄 时填写
	
	private String  mvId;//指定MV,类型为定制拍摄 时填写
	
	private Integer  status; //1：上线 2：下线
	
    private Date timeStart;//生效开始时间
	
    private Date timeEnd;//生效结束时间
	
	@Transient
	private String timeStartStr;//临时生效开始时间
	
	@Transient
    private String timeEndStr;//临时生效结束时间
	
	private String  actPicIos;//活动大图 地址Ios
	
	private String  actIconIos;//活动缩略图 地址Ios
	
	private String  loadPicIos;//素材加载图 地址Ios
	
	private String  loadFailPicIos;//素材加载失败图 地址Ios
	
	private String  bagIos;//拍摄素材包 地址Ios
	
	private String  buttonIconIos;//活动button 地址Ios
	
	
	private String  actPicAndroid;//活动大图 地址Android
	
	private String  actIconAndroid;//活动缩略图 地址Android
	
	private String  loadPicAndroid;//素材加载图 地址Android
	
	private String  loadFailPicAndroid;//素材加载失败图 地址Android
	
	private String  bagAndroid;//拍摄素材包 地址Android
	
	private String  buttonIconAndroid;//活动button 地址Android
	
	@Transient
	private Activity activity;//接口调用选择的活动对象
	
	@Transient
	private Template head;//接口调用选择的片头对象
	
	@Transient
	private Template mv;//接口调用选择的MV对象

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityDes() {
		return activityDes;
	}

	public void setActivityDes(String activityDes) {
		this.activityDes = activityDes;
	}

	public String getShareDes() {
		return shareDes;
	}

	public void setShareDes(String shareDes) {
		this.shareDes = shareDes;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getMvId() {
		return mvId;
	}

	public void setMvId(String mvId) {
		this.mvId = mvId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getActPicIos() {
		return actPicIos;
	}

	public void setActPicIos(String actPicIos) {
		this.actPicIos = actPicIos;
	}

	public String getActIconIos() {
		return actIconIos;
	}

	public void setActIconIos(String actIconIos) {
		this.actIconIos = actIconIos;
	}

	public String getLoadPicIos() {
		return loadPicIos;
	}

	public void setLoadPicIos(String loadPicIos) {
		this.loadPicIos = loadPicIos;
	}

	public String getLoadFailPicIos() {
		return loadFailPicIos;
	}

	public void setLoadFailPicIos(String loadFailPicIos) {
		this.loadFailPicIos = loadFailPicIos;
	}

	public String getBagIos() {
		return bagIos;
	}

	public void setBagIos(String bagIos) {
		this.bagIos = bagIos;
	}

	public String getActPicAndroid() {
		return actPicAndroid;
	}

	public void setActPicAndroid(String actPicAndroid) {
		this.actPicAndroid = actPicAndroid;
	}

	public String getActIconAndroid() {
		return actIconAndroid;
	}

	public void setActIconAndroid(String actIconAndroid) {
		this.actIconAndroid = actIconAndroid;
	}

	public String getLoadPicAndroid() {
		return loadPicAndroid;
	}

	public void setLoadPicAndroid(String loadPicAndroid) {
		this.loadPicAndroid = loadPicAndroid;
	}

	public String getLoadFailPicAndroid() {
		return loadFailPicAndroid;
	}

	public void setLoadFailPicAndroid(String loadFailPicAndroid) {
		this.loadFailPicAndroid = loadFailPicAndroid;
	}

	public String getBagAndroid() {
		return bagAndroid;
	}

	public void setBagAndroid(String bagAndroid) {
		this.bagAndroid = bagAndroid;
	}

	public String getTimeStartStr() {
		return timeStartStr;
	}

	public void setTimeStartStr(String timeStartStr) {
		this.timeStartStr = timeStartStr;
	}

	public String getTimeEndStr() {
		return timeEndStr;
	}

	public void setTimeEndStr(String timeEndStr) {
		this.timeEndStr = timeEndStr;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Template getHead() {
		return head;
	}

	public void setHead(Template head) {
		this.head = head;
	}

	public Template getMv() {
		return mv;
	}

	public void setMv(Template mv) {
		this.mv = mv;
	}

	public String getButtonIconIos() {
		return buttonIconIos;
	}

	public void setButtonIconIos(String buttonIconIos) {
		this.buttonIconIos = buttonIconIos;
	}

	public String getButtonIconAndroid() {
		return buttonIconAndroid;
	}

	public void setButtonIconAndroid(String buttonIconAndroid) {
		this.buttonIconAndroid = buttonIconAndroid;
	}
	
	
	
	
	
}
