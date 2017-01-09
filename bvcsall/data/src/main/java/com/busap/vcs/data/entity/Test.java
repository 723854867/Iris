package com.busap.vcs.data.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by
 * User: djyin
 * Date: 12/3/13
 * Time: 9:33 AM
 */
@Entity
@Table(name = "test")
public class Test extends BaseEntity {

    private static final long serialVersionUID = -7519486823153844426L;

    /**
     * The Key.变量名
     */
    @Column(nullable = false, length = 255)
    private String syskey;

    /**
     * The Value.变量值
     */
    @Column(length = 4096)
    private String sysvalue;


    /**
     * The Type.变量类型
     */
    @Column(length = 255)
    private String systype;


    /**
     * The Description.描述
     */
    @Length(max = 1024)
    @Column(length = 1024, nullable = true)

    private String description;

    public Test() {
        super();
    }

    public Test(String syskey, String sysvalue, String systype, String description) {
        this.syskey = syskey;
        this.sysvalue = sysvalue;
        this.systype = systype;
        this.description = description;
    }

    public Test(String syskey, String sysvalue, String systype) {
        this.syskey = syskey;
        this.sysvalue = sysvalue;
        this.systype = systype;
    }

    public String getSystype() {
        return systype;
    }

    public void setSystype(String systype) {
        this.systype = systype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSysvalue() {
        return sysvalue;
    }

    public void setSysvalue(String sysvalue) {
        this.sysvalue = sysvalue;
    }

    public String getSyskey() {
        return syskey;
    }

    public void setSyskey(String syskey) {
        this.syskey = syskey;
    }


}

