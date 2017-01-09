package com.busap.vcs.data.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.busap.vcs.serializer.LabelJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "label")
@JsonSerialize(using=LabelJsonSerializer.class)
public class Label  implements Serializable {  
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4404842992391391526L;
	
	@Column(nullable=false,unique=true,length=40)
	private String  name;
	
	@Column(columnDefinition="bigint not null DEFAULT '0'")
	private Long  num;
	
	
	
	@Column(name = "timeStamp",columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",nullable=true)
	private Timestamp  timeStamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    @Column(name = "create_at",columnDefinition = "timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'",nullable=true)

    Date createDate;
 
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
