package com.busap.vcs.service;

import com.busap.vcs.data.entity.IdentifyInfo;
import com.busap.vcs.data.vo.CustPicRequest;
import com.busap.vcs.data.vo.CustPicResponse;

/**
 * 实名查验Api
 * Created by Knight on 16/5/30.
 */
public interface RealAuthService {

    /**
     *
     * @param picType 身份证图片 类型:“Z” 代表正面 “F” 代表反面
     * @param picPath 图片类型
     * @return
     */
    public CustPicResponse custPicIdentify(String picType, String picPath);

    /**
     * 此接口 供身份证信息真实性查验及身份证照片真实性查验服务。
     * 使用时需要将身份证 文字信息和图片路径传给接口,进行身份证信息公安部联网比对,查验身份信息真实性。
     * @param isInterfaceCombination 是否联合使用接口 0否1是。取值为1时,“身份证图文信息验证接口”与“人像比对接口” 联合使用
     * @param needStaffCheck 是否需要人 工审核 0 不需要 1 需要。取值为1时, 需要携带身份证正反 面路径
     * @param identifyInfo 身份证信息
     * @return
     */
    public CustPicResponse custInfoPicVerify(String isInterfaceCombination, String needStaffCheck, IdentifyInfo identifyInfo);

    public CustPicResponse cusPicCompare(String isInterfaceCombination, String needStaffCheck, IdentifyInfo identifyInfo, String token, String picPath, String isHandleCard);
}
