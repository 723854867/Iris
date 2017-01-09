package com.busap.vcs.data.entity;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.busap.vcs.data.enums.PermissionStatus;
import com.busap.vcs.data.enums.SystemType;

/**
 * Created by
 * User: djyin
 * Date: 12/16/13
 * Time: 7:37 PM
 * 代表系统中对象的访问许可
 */
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity implements Comparable<Permission> {

    /**
     * The Name.
     */
    @Column(nullable = true)
    private String name;//名称

    /**
     * The Value.
     */
    @Column(length = 255)
    private String value;//url

    /**
     * The Description.
     */
    @Column(length = 1024, nullable = true)
    private String description;
    
    private Long pid=0L;//父结点id
    
    private int sort;//排序字段
    
    private String status=PermissionStatus.可用.getName();

    public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Permission() {
    }

    public Permission(String value, PermissionType type, String systemType) {
        this.value = value;
        this.type = type;
        this.systemType = systemType;
    }

    public Permission(String name, String value, String description, PermissionType type, String systemType) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.type = type;
        this.systemType = systemType;
    }

    /**
     * The Type.实现类型,比如URI,BIObject
     */
    @Column(length = 255, nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionType type=PermissionType.URI;

    /**
     * The enum Permission type.目前有两种类型,URI和基于业务对象的
     */
    public enum PermissionType {
        URI, BIObject
    }
    /**
     * The System type.系统类型,比如VCS,Security
     * 默认是菜单的权限，Security是指shiro的权限
     */
    @Column(length = 255, nullable = false)
    String systemType=SystemType.系统菜单.getName();
    
    @Transient
    private List<Permission> children=new ArrayList<Permission>();
    
    @Transient
    private String text;

    public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Permission> getChildren() {
		return children;
	}

	public void setChildren(List<Permission> children) {
		this.children = children;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        if (!super.equals(o)) return false;

        Permission that = (Permission) o;

        if (!systemType.equals(that.systemType)) return false;
        if (type != that.type) return false;
//        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
//        result = 31 * result + value.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + systemType.hashCode();
        return result;
    }

	@Override
	public int compareTo(Permission o) {
		if(this.sort>o.getSort()){
			return 1;
		}else if(this.sort<o.getSort()){
			return -1;
		}
		return 0;
	}
}
