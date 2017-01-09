package com.busap.vcs.service.utils;

import com.pingplusplus.model.Transfer;

import java.util.Map;

/**
 * Transfer Transfer
 * Created by Knight on 16/4/6.
 */
public class TransferVO extends Transfer {

    private Long time_transferred;

    private String order_no;

    private String batch_no;

    private String failure_msg;

    private Map metadata;

    public Long getTime_transferred() {
        return time_transferred;
    }

    public void setTime_transferred(Long time_transferred) {
        this.time_transferred = time_transferred;
        super.setTimeTransferred(time_transferred);
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
        super.setOrderNo(order_no);
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getFailure_msg() {
        return failure_msg;
    }

    public void setFailure_msg(String failure_msg) {
        this.failure_msg = failure_msg;
    }

    public Map getMetadata() {
        return metadata;
    }

    public void setMetadata(Map metadata) {
        this.metadata = metadata;
    }
}
