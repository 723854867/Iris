package com.busap.vcs.service.utils;

import com.pingplusplus.model.Charge;

/**
 * Charge Transfer
 * Created by Knight on 15/12/23.
 */
public class ChargeVO  extends Charge {

    private Long time_paid;

    private Long time_expire;

    private Long time_settle;

    private String transaction_no;

    private Integer amount_refunded;

    private String failure_code;

    private String failure_msg;

    private String order_no;

    private String client_ip;

    private Integer amount_settle;

    public Long getTime_paid() {
        return time_paid;
    }

    public void setTime_paid(Long time_paid) {
        this.time_paid = time_paid;
        super.setTimePaid(time_paid);
    }

    public Long getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(Long time_expire) {
        this.time_expire = time_expire;
        super.setTimeExpire(time_expire);
    }

    public Long getTime_settle() {
        return time_settle;
    }

    public void setTime_settle(Long time_settle) {
        this.time_settle = time_settle;
        super.setTimeSettle(time_settle);
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
        super.setTransactionNo(transaction_no);
    }

    public Integer getAmount_refunded() {
        return amount_refunded;
    }

    public void setAmount_refunded(Integer amount_refunded) {
        this.amount_refunded = amount_refunded;
        super.setAmountRefunded(amount_refunded);
    }

    public String getFailure_code() {
        return failure_code;
    }

    public void setFailure_code(String failure_code) {
        this.failure_code = failure_code;
        super.setFailureCode(failure_code);
    }

    public String getFailure_msg() {
        return failure_msg;
    }

    public void setFailure_msg(String failure_msg) {
        this.failure_msg = failure_msg;
        super.setFailureMsg(failure_msg);
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
        super.setOrderNo(order_no);
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
        super.setClientIp(client_ip);
    }

    public Integer getAmount_settle() {
        return amount_settle;
    }

    public void setAmount_settle(Integer amount_settle) {
        this.amount_settle = amount_settle;
        super.setAmountSettle(amount_settle);
    }

    @Override
    public String toString() {
        return "ChargeVO{" +
                "time_paid=" + time_paid +
                ", time_expire=" + time_expire +
                ", time_settle=" + time_settle +
                ", transaction_no='" + transaction_no + '\'' +
                ", amount_refunded=" + amount_refunded +
                ", failure_code='" + failure_code + '\'' +
                ", failure_msg='" + failure_msg + '\'' +
                ", order_no='" + order_no + '\'' +
                ", client_ip='" + client_ip + '\'' +
                ", amount_settle=" + amount_settle +
                '}';
    }

}
