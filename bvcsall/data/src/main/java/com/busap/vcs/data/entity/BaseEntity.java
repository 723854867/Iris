package com.busap.vcs.data.entity;

import com.busap.vcs.data.listener.EntityListener;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
@EntityListeners({EntityListener.class})
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    /**
     * 数据格式校验时使用的分组:创建数据,不验证id,创建时间,更新时间
     */
    public abstract interface Save extends Default {

    }

    /**
     * 数据格式校验时使用的分组:更新数据,不验证  创建时间,更新时间
     */
    public abstract interface Update extends Default {

    }
    
    @NoRedis
    private static final long serialVersionUID = -67188388306700736L;
    @NoRedis
    public static final String ID_PROPERTY_NAME = "id";
    @NoRedis
    public static final String CREATE_DATE_PROPERTY_NAME = "createDate";
    @NoRedis
    public static final String MODIFY_DATE_PROPERTY_NAME = "modifyDate";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    @Column(name = "create_at",columnDefinition = "timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'",nullable=true)
    
    Date createDate;
 
    @Column(name = "modify_at",columnDefinition = "timestamp NULL DEFAULT CURRENT_TIMESTAMP",nullable=true)
    private Date modifyDate;

    private Long creatorId; //创建人id 
    
    @Column(name = "data_from")
	public String dataFrom;
    
    @Transient Object user;

    
    public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
 

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Long getCreateDate() {
    	if(createDate!=null)
        return createDate.getTime();
    	else
    		return null;
    }

    public Date getCreateDateStr() {
        return createDate;
    }

    public void setCreateDateStr(Date createDate){
        this.createDate = createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }



    public Long getModifyDate() {
    	if(modifyDate!=null)
        return modifyDate.getTime();
    	else
    		return null;
    }
    
    public Date getModifyDateStr() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!BaseEntity.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        BaseEntity localBaseEntity = (BaseEntity) obj;
        return getId() != null ? getId().equals(localBaseEntity.getId())
                : false;
    }

    public int hashCode() {
        int i = 17;
        i += (getId() == null ? 0 : getId().hashCode() * 31);
        return i;
    }
}
