package com.busap.vcs.data.entity;

public class IdentifyInfo {
    private Long id;

    private Long userId;

    private String custCertNo;

    private String custName;

    private String gender;

    private String nation;

    private String birthday;

    private String address;

    private String issuingAuthority;

    private String certValiddate;

    private String certExpdate;

    private Integer status;

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

    public String getCustCertNo() {
        return custCertNo;
    }

    public void setCustCertNo(String custCertNo) {
        this.custCertNo = custCertNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getCertValiddate() {
        return certValiddate;
    }

    public void setCertValiddate(String certValiddate) {
        this.certValiddate = certValiddate;
    }

    public String getCertExpdate() {
        return certExpdate;
    }

    public void setCertExpdate(String certExpdate) {
        this.certExpdate = certExpdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}