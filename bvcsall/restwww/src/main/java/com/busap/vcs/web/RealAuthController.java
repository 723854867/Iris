package com.busap.vcs.web;

import com.busap.vcs.data.entity.IdentifyInfo;
import com.busap.vcs.data.vo.CustPicResponse;
import com.busap.vcs.service.RealAuthService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Test Auth
 * Created by Knight on 16/6/3.
 */
@Controller
@RequestMapping("/realAuth")
public class RealAuthController {

    @Autowired
    private RealAuthService realAuthService;

    @Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    private Logger logger = LoggerFactory.getLogger(RealAuthController.class);

    @RequestMapping("/custPicIdentify")
    @ResponseBody
    public RespBody custPicIdentify(String picType, String path) {
        logger.info("custPicIdentify = picType:" + picType + " & path:" + path);
        CustPicResponse response = realAuthService.custPicIdentify(picType, path);
        return respBodyWriter.toSuccess(response);
    }

    @RequestMapping("/custInfoPicVerify")
    @ResponseBody
    public RespBody custInfoPicVerify(String isInterfaceCombination, String needStaffCheck, String CustCertNo) {

        IdentifyInfo info = new IdentifyInfo();
        info.setAddress("云南省昆明市寻甸回族彝族自治县七星乡必寨村委会老长地村26号");
        info.setIssuingAuthority("寻甸回族彝族自治县公安局");
        info.setCertValiddate("2006-06-22");
        info.setNation("汉");
        info.setGender( "男");
        info.setBirthday("1991-11-09");
        info.setCertExpdate("2016-06-22");
        info.setCustCertNo(CustCertNo);
        info.setCustName("张麒麟");
        CustPicResponse response = realAuthService.custInfoPicVerify(isInterfaceCombination, needStaffCheck, info);
        return respBodyWriter.toSuccess(response);
    }


    @RequestMapping("/custUnion")
    @ResponseBody
    public RespBody custUnion(String isInterfaceCombination, String needStaffCheck, String picPath, String CustCertNo) {
        IdentifyInfo info = new IdentifyInfo();
        info.setAddress("云南省昆明市寻甸回族彝族自治县七星乡必寨村委会老长地村26号");
        info.setIssuingAuthority("寻甸回族彝族自治县公安局");
        info.setCertValiddate("2006-06-22");
        info.setNation("汉");
        info.setGender("男");
        info.setBirthday("1991-11-09");
        info.setCertExpdate("2016-06-22");
        if (StringUtils.isNotBlank(CustCertNo)) {
            info.setCustCertNo(CustCertNo);
        } else {
            info.setCustCertNo("310225199111091219");
        }
        info.setCustName("张麒麟");
        CustPicResponse response = realAuthService.custInfoPicVerify(isInterfaceCombination, needStaffCheck, info);

        CustPicResponse response1 = realAuthService.cusPicCompare(isInterfaceCombination, needStaffCheck, info, response.getBean().get("token"), picPath, "1");

        return respBodyWriter.toSuccess(response1);
    }

    @RequestMapping("/cusPicCompare")
    @ResponseBody
    public RespBody cusPicCompare(String isInterfaceCombination, String needStaffCheck, String picPath, String CustCertNo) {
        IdentifyInfo info = new IdentifyInfo();
        info.setAddress("云南省昆明市寻甸回族彝族自治县七星乡必寨村委会老长地村26号");
        info.setIssuingAuthority("寻甸回族彝族自治县公安局");
        info.setCertValiddate("2006-06-22");
        info.setNation("汉");
        info.setGender("男");
        info.setBirthday("1991-11-09");
        info.setCertExpdate("2016-06-22");
        if (StringUtils.isNotBlank(CustCertNo)) {
            info.setCustCertNo(CustCertNo);
        } else {
            info.setCustCertNo("310225199111091219");
        }
        info.setCustName("张麒麟");

        CustPicResponse response = realAuthService.cusPicCompare(isInterfaceCombination, needStaffCheck, info, "", picPath, "1");

        return respBodyWriter.toSuccess(response);
    }
}
