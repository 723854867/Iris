package com.busap.vcs.service;

/**
 * Created by Knight on 16/5/27.
 */
public interface AppVerifyRecodeService {
    boolean saveVerifyRecode(Long userId,
                             String receipt,
                             String param,
                             String clientIp,
                             String transactionNo,
                             String status,
                             String result);
}
