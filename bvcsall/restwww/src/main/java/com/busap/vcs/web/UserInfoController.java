package com.busap.vcs.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.OAuthService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.utils.QiniuUtil;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.pili.Stream;

@RestController
public class UserInfoController extends CRUDController<Ruser, Long> {
	@Autowired
	private RuserService ruserService;

	@Autowired
	private OAuthService oAuthService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "anchorService")
	private AnchorService anchorService;

	@RequestMapping(value = { "/userInfo" }, method = RequestMethod.GET)
	public RespBody userInfo(HttpServletRequest request)
			throws OAuthSystemException, OAuthProblemException {

		String access_tokne= request.getHeader((OAuth.OAUTH_ACCESS_TOKEN));
		
		if(StringUtils.isBlank(access_tokne)){
			access_tokne=request.getParameter(OAuth.OAUTH_ACCESS_TOKEN);
		}
		
		
		
		String username = oAuthService.getUsernameByAccessToken(access_tokne);
		Ruser ruser = ruserService.findByUsername(username);
		ruser.setPassword("");
		
        Anchor anchor = anchorService.getAnchorByUserid(ruser.getId());
        
        ruser.setIdentifyStatus(anchor == null ?"":String.valueOf(anchor.getStatus()));
        ruser.setExistIdentify(anchor == null || anchor.getStatus() == -2 ? "0":"1");
        
        String type = jedisService.get(BicycleConstants.CDN_NAME);
        
        if ("qiniu".equals(type)) {
        	 if (anchor != null && anchor.getQiniuStreamId() !=null && !"".equals(anchor.getQiniuStreamId())){
             	QiniuUtil util = new QiniuUtil();
             	Stream stream = util.getStream(anchor.getQiniuStreamId());
             	ruser.setStreamUrl(util.getLiveUrl(stream, "rtmp"));
             } else {
             	ruser.setStreamUrl("");
             }
        } else if ("ucloud".equals(type)) {
        	 if (anchor != null && anchor.getStreamId() !=null && !"".equals(anchor.getStreamId())){
             	ruser.setStreamUrl("http://ucloud-rtmp.wopaitv.com/ucloud-test/"+anchor.getStreamId()+".flv");
             } else {
             	ruser.setStreamUrl("");
             }
        } else if ("wangsu".equals(type)) {
			if (anchor != null && anchor.getStreamId() != null
					&& !"".equals(anchor.getStreamId())) {
				ruser.setStreamUrl("http://wsflv.wopaitv.com/blive/"
						+ anchor.getStreamId() + ".flv");
			} else {
				ruser.setStreamUrl("");
			}
		}
        
		return this.respBodyWriter.toSuccess(ruser);

	}

	@Override
	public void setBaseService(BaseService<Ruser, Long> baseService) {
		this.baseService = baseService;

	}
}
