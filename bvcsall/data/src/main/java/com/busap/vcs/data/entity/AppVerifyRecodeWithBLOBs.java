package com.busap.vcs.data.entity;

public class AppVerifyRecodeWithBLOBs extends AppVerifyRecode {
    private String receipt;

    private String result;

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}