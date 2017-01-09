package com.busap.vcs.data.entity;

public class CollectInfo {
    private Long id;

    private Long userId;

    private Long create;

    private Long roomId;

    private Long firstDelayTime;

    private Integer blockTimes;

    private String type;
    
    private String localOutIP;		//本地出口IP
    
    private String publishHost;
    
    private String piliHost;
    
    private String clientType;		//客户端类型
    
    private String errorCode;		//错误码      PULL 133  音视频全没有 5次           135 有音频没视频                 137 音视频不同步               138 有视频没音频                      0 150次网路不好
	
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCreate() {
        return create;
    }

    public void setCreate(Long create) {
        this.create = create;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getFirstDelayTime() {
        return firstDelayTime;
    }

    public void setFirstDelayTime(Long firstDelayTime) {
        this.firstDelayTime = firstDelayTime;
    }

    public Integer getBlockTimes() {
        return blockTimes;
    }

    public void setBlockTimes(Integer blockTimes) {
        this.blockTimes = blockTimes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public void setLocalOutIP(String localOutIP) {
		this.localOutIP = localOutIP;
	}

	public String getLocalOutIP() {
		return localOutIP;
	}

	public String getPublishHost() {
		return publishHost;
	}

	public void setPublishHost(String publishHost) {
		this.publishHost = publishHost;
	}

	public String getPiliHost() {
		return piliHost;
	}

	public void setPiliHost(String piliHost) {
		this.piliHost = piliHost;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
    
    
}