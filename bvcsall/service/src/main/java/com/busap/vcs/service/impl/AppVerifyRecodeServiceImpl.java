package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.AppVerifyRecodeWithBLOBs;
import com.busap.vcs.data.mapper.AppVerifyRecodeMapper;
import com.busap.vcs.service.AppVerifyRecodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Knight on 16/5/27.
 */
@Service
public class AppVerifyRecodeServiceImpl implements AppVerifyRecodeService {

    @Autowired
    private AppVerifyRecodeMapper appVerifyRecodeMapper;

    @Override
    public boolean saveVerifyRecode(Long userId,
                                    String receipt,
                                    String param,
                                    String clientIp,
                                    String transactionNo,
                                    String status,
                                    String result) {
        AppVerifyRecodeWithBLOBs recode = new AppVerifyRecodeWithBLOBs();
        recode.setUserId(userId);
        recode.setCreateTime(new Date());
        recode.setReceipt(receipt);
        recode.setParam(param);
        recode.setClientIp(clientIp);
        recode.setTransactionNo(transactionNo);
        recode.setStatus(status);
        recode.setResult(result);
        return appVerifyRecodeMapper.insert(recode) > 0;
    }
}
