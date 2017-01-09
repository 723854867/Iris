package com.busap.vcs.data.entity;

import java.util.ArrayList;
import java.util.List;

public class IdentifyInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public IdentifyInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andCustCertNoIsNull() {
            addCriterion("cust_cert_no is null");
            return (Criteria) this;
        }

        public Criteria andCustCertNoIsNotNull() {
            addCriterion("cust_cert_no is not null");
            return (Criteria) this;
        }

        public Criteria andCustCertNoEqualTo(String value) {
            addCriterion("cust_cert_no =", value, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoNotEqualTo(String value) {
            addCriterion("cust_cert_no <>", value, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoGreaterThan(String value) {
            addCriterion("cust_cert_no >", value, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoGreaterThanOrEqualTo(String value) {
            addCriterion("cust_cert_no >=", value, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoLessThan(String value) {
            addCriterion("cust_cert_no <", value, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoLessThanOrEqualTo(String value) {
            addCriterion("cust_cert_no <=", value, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoLike(String value) {
            addCriterion("cust_cert_no like", value, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoNotLike(String value) {
            addCriterion("cust_cert_no not like", value, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoIn(List<String> values) {
            addCriterion("cust_cert_no in", values, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoNotIn(List<String> values) {
            addCriterion("cust_cert_no not in", values, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoBetween(String value1, String value2) {
            addCriterion("cust_cert_no between", value1, value2, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustCertNoNotBetween(String value1, String value2) {
            addCriterion("cust_cert_no not between", value1, value2, "custCertNo");
            return (Criteria) this;
        }

        public Criteria andCustNameIsNull() {
            addCriterion("cust_name is null");
            return (Criteria) this;
        }

        public Criteria andCustNameIsNotNull() {
            addCriterion("cust_name is not null");
            return (Criteria) this;
        }

        public Criteria andCustNameEqualTo(String value) {
            addCriterion("cust_name =", value, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameNotEqualTo(String value) {
            addCriterion("cust_name <>", value, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameGreaterThan(String value) {
            addCriterion("cust_name >", value, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameGreaterThanOrEqualTo(String value) {
            addCriterion("cust_name >=", value, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameLessThan(String value) {
            addCriterion("cust_name <", value, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameLessThanOrEqualTo(String value) {
            addCriterion("cust_name <=", value, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameLike(String value) {
            addCriterion("cust_name like", value, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameNotLike(String value) {
            addCriterion("cust_name not like", value, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameIn(List<String> values) {
            addCriterion("cust_name in", values, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameNotIn(List<String> values) {
            addCriterion("cust_name not in", values, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameBetween(String value1, String value2) {
            addCriterion("cust_name between", value1, value2, "custName");
            return (Criteria) this;
        }

        public Criteria andCustNameNotBetween(String value1, String value2) {
            addCriterion("cust_name not between", value1, value2, "custName");
            return (Criteria) this;
        }

        public Criteria andGenderIsNull() {
            addCriterion("gender is null");
            return (Criteria) this;
        }

        public Criteria andGenderIsNotNull() {
            addCriterion("gender is not null");
            return (Criteria) this;
        }

        public Criteria andGenderEqualTo(String value) {
            addCriterion("gender =", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotEqualTo(String value) {
            addCriterion("gender <>", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderGreaterThan(String value) {
            addCriterion("gender >", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderGreaterThanOrEqualTo(String value) {
            addCriterion("gender >=", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderLessThan(String value) {
            addCriterion("gender <", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderLessThanOrEqualTo(String value) {
            addCriterion("gender <=", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderLike(String value) {
            addCriterion("gender like", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotLike(String value) {
            addCriterion("gender not like", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderIn(List<String> values) {
            addCriterion("gender in", values, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotIn(List<String> values) {
            addCriterion("gender not in", values, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderBetween(String value1, String value2) {
            addCriterion("gender between", value1, value2, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotBetween(String value1, String value2) {
            addCriterion("gender not between", value1, value2, "gender");
            return (Criteria) this;
        }

        public Criteria andNationIsNull() {
            addCriterion("nation is null");
            return (Criteria) this;
        }

        public Criteria andNationIsNotNull() {
            addCriterion("nation is not null");
            return (Criteria) this;
        }

        public Criteria andNationEqualTo(String value) {
            addCriterion("nation =", value, "nation");
            return (Criteria) this;
        }

        public Criteria andNationNotEqualTo(String value) {
            addCriterion("nation <>", value, "nation");
            return (Criteria) this;
        }

        public Criteria andNationGreaterThan(String value) {
            addCriterion("nation >", value, "nation");
            return (Criteria) this;
        }

        public Criteria andNationGreaterThanOrEqualTo(String value) {
            addCriterion("nation >=", value, "nation");
            return (Criteria) this;
        }

        public Criteria andNationLessThan(String value) {
            addCriterion("nation <", value, "nation");
            return (Criteria) this;
        }

        public Criteria andNationLessThanOrEqualTo(String value) {
            addCriterion("nation <=", value, "nation");
            return (Criteria) this;
        }

        public Criteria andNationLike(String value) {
            addCriterion("nation like", value, "nation");
            return (Criteria) this;
        }

        public Criteria andNationNotLike(String value) {
            addCriterion("nation not like", value, "nation");
            return (Criteria) this;
        }

        public Criteria andNationIn(List<String> values) {
            addCriterion("nation in", values, "nation");
            return (Criteria) this;
        }

        public Criteria andNationNotIn(List<String> values) {
            addCriterion("nation not in", values, "nation");
            return (Criteria) this;
        }

        public Criteria andNationBetween(String value1, String value2) {
            addCriterion("nation between", value1, value2, "nation");
            return (Criteria) this;
        }

        public Criteria andNationNotBetween(String value1, String value2) {
            addCriterion("nation not between", value1, value2, "nation");
            return (Criteria) this;
        }

        public Criteria andBirthdayIsNull() {
            addCriterion("birthday is null");
            return (Criteria) this;
        }

        public Criteria andBirthdayIsNotNull() {
            addCriterion("birthday is not null");
            return (Criteria) this;
        }

        public Criteria andBirthdayEqualTo(String value) {
            addCriterion("birthday =", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayNotEqualTo(String value) {
            addCriterion("birthday <>", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayGreaterThan(String value) {
            addCriterion("birthday >", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayGreaterThanOrEqualTo(String value) {
            addCriterion("birthday >=", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayLessThan(String value) {
            addCriterion("birthday <", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayLessThanOrEqualTo(String value) {
            addCriterion("birthday <=", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayLike(String value) {
            addCriterion("birthday like", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayNotLike(String value) {
            addCriterion("birthday not like", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayIn(List<String> values) {
            addCriterion("birthday in", values, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayNotIn(List<String> values) {
            addCriterion("birthday not in", values, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayBetween(String value1, String value2) {
            addCriterion("birthday between", value1, value2, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayNotBetween(String value1, String value2) {
            addCriterion("birthday not between", value1, value2, "birthday");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityIsNull() {
            addCriterion("issuing_authority is null");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityIsNotNull() {
            addCriterion("issuing_authority is not null");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityEqualTo(String value) {
            addCriterion("issuing_authority =", value, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityNotEqualTo(String value) {
            addCriterion("issuing_authority <>", value, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityGreaterThan(String value) {
            addCriterion("issuing_authority >", value, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityGreaterThanOrEqualTo(String value) {
            addCriterion("issuing_authority >=", value, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityLessThan(String value) {
            addCriterion("issuing_authority <", value, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityLessThanOrEqualTo(String value) {
            addCriterion("issuing_authority <=", value, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityLike(String value) {
            addCriterion("issuing_authority like", value, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityNotLike(String value) {
            addCriterion("issuing_authority not like", value, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityIn(List<String> values) {
            addCriterion("issuing_authority in", values, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityNotIn(List<String> values) {
            addCriterion("issuing_authority not in", values, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityBetween(String value1, String value2) {
            addCriterion("issuing_authority between", value1, value2, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andIssuingAuthorityNotBetween(String value1, String value2) {
            addCriterion("issuing_authority not between", value1, value2, "issuingAuthority");
            return (Criteria) this;
        }

        public Criteria andCertValiddateIsNull() {
            addCriterion("cert_validdate is null");
            return (Criteria) this;
        }

        public Criteria andCertValiddateIsNotNull() {
            addCriterion("cert_validdate is not null");
            return (Criteria) this;
        }

        public Criteria andCertValiddateEqualTo(String value) {
            addCriterion("cert_validdate =", value, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateNotEqualTo(String value) {
            addCriterion("cert_validdate <>", value, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateGreaterThan(String value) {
            addCriterion("cert_validdate >", value, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateGreaterThanOrEqualTo(String value) {
            addCriterion("cert_validdate >=", value, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateLessThan(String value) {
            addCriterion("cert_validdate <", value, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateLessThanOrEqualTo(String value) {
            addCriterion("cert_validdate <=", value, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateLike(String value) {
            addCriterion("cert_validdate like", value, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateNotLike(String value) {
            addCriterion("cert_validdate not like", value, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateIn(List<String> values) {
            addCriterion("cert_validdate in", values, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateNotIn(List<String> values) {
            addCriterion("cert_validdate not in", values, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateBetween(String value1, String value2) {
            addCriterion("cert_validdate between", value1, value2, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertValiddateNotBetween(String value1, String value2) {
            addCriterion("cert_validdate not between", value1, value2, "certValiddate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateIsNull() {
            addCriterion("cert_expdate is null");
            return (Criteria) this;
        }

        public Criteria andCertExpdateIsNotNull() {
            addCriterion("cert_expdate is not null");
            return (Criteria) this;
        }

        public Criteria andCertExpdateEqualTo(String value) {
            addCriterion("cert_expdate =", value, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateNotEqualTo(String value) {
            addCriterion("cert_expdate <>", value, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateGreaterThan(String value) {
            addCriterion("cert_expdate >", value, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateGreaterThanOrEqualTo(String value) {
            addCriterion("cert_expdate >=", value, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateLessThan(String value) {
            addCriterion("cert_expdate <", value, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateLessThanOrEqualTo(String value) {
            addCriterion("cert_expdate <=", value, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateLike(String value) {
            addCriterion("cert_expdate like", value, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateNotLike(String value) {
            addCriterion("cert_expdate not like", value, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateIn(List<String> values) {
            addCriterion("cert_expdate in", values, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateNotIn(List<String> values) {
            addCriterion("cert_expdate not in", values, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateBetween(String value1, String value2) {
            addCriterion("cert_expdate between", value1, value2, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andCertExpdateNotBetween(String value1, String value2) {
            addCriterion("cert_expdate not between", value1, value2, "certExpdate");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}