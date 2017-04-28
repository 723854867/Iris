package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;

/**
 * 
 * @author liusiyuan
 * 2017年4月7日
 */
public class CmsVersion implements RedisHashBean{
	/**
	 * 版本id
	 */
	private long versionId;
	/**
	 * 版本号
	 */
	private String versionNum;
	/**
	 * 0-不强制升级1-强制升级
	 */
	private int status;
	/**
	 * 0启用状态1-删除状态
	 */
	private int delFlag;
	/**
	 * 创建时间
	 */
	private int created;
	/**
	 * 更新时间
	 */
	private int updated;
	/**
	 * 版本更新内容
	 */
	private String content;
	/**
	 * 下载地址
	 */
	private String downloadUrl;
	/**
	 * 操作系统，0-安卓 1-ios
	 */
	private int operatSys;
	public CmsVersion( String versionNum, int status, String content, String downloadUrl, int operatSys) {
		super();
		int time = DateUtils.currentTime();
		this.versionNum = versionNum;
		this.status = status;
		this.created = time;
		this.updated = time;
		this.content = content;
		this.downloadUrl = downloadUrl;
		this.operatSys = operatSys;
	}
	public CmsVersion(long versionId, String versionNum, int status, int delFlag, String content, String downloadUrl, int operatSys) {
		super();
		this.versionId = versionId;
		this.versionNum = versionNum;
		this.status = status;
		this.updated = DateUtils.currentTime();
		this.delFlag = delFlag;
		this.content = content;
		this.downloadUrl = downloadUrl;
		this.operatSys = operatSys;
	}
	
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public CmsVersion() {
		super();
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getVersionId() {
		return versionId;
	}
	public void setVersionId(long versionId) {
		this.versionId = versionId;
	}
	public String getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public int getOperatSys() {
		return operatSys;
	}
	public void setOperatSys(int operatSys) {
		this.operatSys = operatSys;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	public int getUpdated() {
		return updated;
	}
	public void setUpdated(int updated) {
		this.updated = updated;
	}
	@Override
	public String redisKey() {
		return CommonKeyGenerator.getVersion(operatSys);
	}
	
}
