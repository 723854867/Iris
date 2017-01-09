package com.busap.vcs.webcomn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.busap.vcs.base.Constants;
import com.busap.vcs.data.entity.BaseEntity;
import com.busap.vcs.data.entity.Evaluation;
import com.busap.vcs.data.entity.Favorite;
import com.busap.vcs.data.entity.Praise;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.util.encode.JsonBinder;

/**
 * Created by
 * User: djyin
 * Date: 12/2/13
 * Time: 1:48 PM
 * 默认的RespBody对象创建器,自动识别数据校验错误
 */
@SuppressWarnings("unchecked")
@Component("respBodyBuilder")
public class RespBodyBuilder {

    private static final Logger logger = LoggerFactory.getLogger(RespBodyBuilder.class);

    private static JsonBinder jsonBinder = JsonBinder.nonEmptyMapper();

    CloseableHttpClient httpclient = HttpClients.createDefault(); 
    
    int c=0;
    Date start=null;
    
    @Resource(name = "ruserService")
	RuserService ruserService;

    public void parseUserInfo(Object obj) {
        //从麦克获取用户信息
        List ls = null;
        if (obj != null && (obj instanceof Page)) {
            ls = ((Page) obj).getContent();
        } else if (obj != null && (obj instanceof List)) {
            ls = (List) obj;
        } else if (obj instanceof Favorite || obj instanceof Evaluation || obj instanceof Video || obj instanceof Praise) {
            ls = new ArrayList();
            ls.add(obj);
        }
        if (ls == null || ls.size() <= 0)
            return;
        if (ls.get(0) instanceof Favorite || ls.get(0) instanceof Evaluation || ls.get(0) instanceof Video || ls.get(0) instanceof Praise) {
            List<Long> ids = new ArrayList<Long>(); 
//        	for (Object one : ls) {
//            	Long id = ((BaseEntity) one).getCreatorId();
//            	if(id==null)
//            		continue;
//            	Ruser user = ruserService.find(id);
//            	((BaseEntity) one).setUser(user); 
//            	
//            	//评论需要查出被回复人的信息，其他实体没有这项
//            	if(ls.get(0) instanceof Evaluation){
//            		Long pid = ((Evaluation)one).getPid();
//            		if(pid!=null){
//            			((Evaluation)one).setParent(ruserService.find(pid));
//            		}
//            	}
//            }  
        	for (Object one : ls) {
            	Long id = ((BaseEntity) one).getCreatorId();
            	if(id!=null)
            		ids.add(id);   
            	//评论需要查出被回复人的信息，其他实体没有这项
            	if(ls.get(0) instanceof Evaluation){
            		Long pid = ((Evaluation)one).getPid();
            		if(pid!=null){ 
            			ids.add(pid); 
            		}
            	}
            } 
        	//一次性获取所有的ruser信息
        	Map<Long,Ruser> users = ruserService.getRusers(ids);
        	for (Object one : ls) {
            	Long id = ((BaseEntity) one).getCreatorId();
            	if(id!=null)  
            	((BaseEntity)one).setUser(users.get(id)); 
            	//评论需要查出被回复人的信息，其他实体没有这项
            	if(ls.get(0) instanceof Evaluation){
            		Long pid = ((Evaluation)one).getPid();
            		if(pid!=null){
            			((Evaluation)one).setParent(users.get(pid));
            		}
            	}
            }  
        }
    } 

    //TODO 应该增加一些配置项，比如i18N消息

    /**
     * 从spring的环境中获取数据格式校验错误
     *
     * @return the violations
     */
    public static Set<Violation> getViolations() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Set<ConstraintViolation<BaseEntity>> set = (Set<ConstraintViolation<BaseEntity>>) requestAttributes.getAttribute(Constants.ENTITY_CONSTRAINT_VIOLATIONS, RequestAttributes.SCOPE_REQUEST);
        Set<Violation> violations = new HashSet<Violation>();

        if (set != null && !set.isEmpty()) {
            Iterator<ConstraintViolation<BaseEntity>> iter = set.iterator();
            while (iter.hasNext()) {
                Violation v = new Violation(iter.next());
                violations.add(v);
            }
        }
        Set<Violation> customs = (Set<Violation>) requestAttributes.getAttribute(Constants.ENTITY_CUSTOM_VIOLATIONS, RequestAttributes.SCOPE_REQUEST);
        if (customs != null && !customs.isEmpty()) {
            violations.addAll(customs);
        }
        return violations;

    }

    public RespBody toSuccess() {
        RespBody reps = new RespBody(Boolean.TRUE, RespBody.MESSAGE_OK, null);
        return reps;
    }


    /**
     * To success. 默认成功的输出
     *
     * @return the request result
     */
    public RespBody toSuccess(Object result) { 
        this.parseUserInfo(result);
        RespBody reps = new RespBody(Boolean.TRUE, RespBody.MESSAGE_OK, result); 
        return reps;
    }
    
    
    public RespBody toSuccess(Object result,Long time) { 
    	c++;
//    	if(start==null)
//    		start=new Date();
        this.parseUserInfo(result);
        RespBody reps = new RespBody(Boolean.TRUE, RespBody.MESSAGE_OK, result);
        if(logger.isInfoEnabled()&&(c%1000==0)){
			logger.info("查询最新视频，web单次总耗时，time="+(new Date().getTime()-time)+"毫秒");
//			logger.info("查询最新视频，web平均耗时，time="+(new Date().getTime()-start.getTime())/c+"毫秒");
		}
        return reps;
    }


    /**
     * To success.默认成功的输出
     *
     * @param message the message 成功消息
     * @param result  the result 成功的结果对象
     * @return the response 用于直接json输出的对象
     */
    public RespBody toSuccess(String message, Object result) {
        this.parseUserInfo(result);
        RespBody reps = new RespBody(Boolean.TRUE, message == null ? RespBody.MESSAGE_OK : message, result);
        return reps;
    }
    
    public RespBody toSuccess(String message, Object result,String extraData) {
    	this.parseUserInfo(result);
    	RespBody reps = new RespBody(Boolean.TRUE, message == null ? RespBody.MESSAGE_OK : message, result,extraData);
    	return reps;
    }


    /**
     * To error. 默认的失败结果输出
     *
     * @param message 错误消息
     * @param result  the result
     * @return the response
     */
    public RespBody toError(String message, Object result, Integer code) {
        Set<Violation> violations = getViolations();
        RespBody reps;
        if (violations.isEmpty()) {
            reps = new RespBody(Boolean.FALSE, message == null ? RespBody.MESSAGE_INTERNAL_ERROR : message, result, code);
        } else {
            reps = new RespBody(Boolean.FALSE, message == null ? RespBody.MESSAGE_VIOLATION_ERROR : message, result, code);
            reps.setViolations(violations);
        }
        return reps;
    }


    /**
     * To error. 默认的失败结果输出
     *
     * @param message 错误消息
     * @return the response
     */
    public RespBody toError(String message, Integer code) {
        Set<Violation> violations = getViolations();
        RespBody reps;
        if (violations.isEmpty()) {
            reps = new RespBody(Boolean.FALSE, message == null ? RespBody.MESSAGE_INTERNAL_ERROR : message, null, code);
        } else {
            reps = new RespBody(Boolean.FALSE, message == null ? RespBody.MESSAGE_VIOLATION_ERROR : message, null, code);
            reps.setViolations(violations);
        }
        return reps;
    }


    /**
     * To error. 默认的失败结果输出
     *
     * @param result 错误消息
     * @return the response
     */
    public RespBody toError(Object result, Integer code) {
        Set<Violation> violations = getViolations();
        RespBody reps;
        if (violations.isEmpty()) {
            reps = new RespBody(Boolean.FALSE, RespBody.MESSAGE_INTERNAL_ERROR, result, code);
        } else {
            reps = new RespBody(Boolean.FALSE, RespBody.MESSAGE_VIOLATION_ERROR, result, code);
            reps.setViolations(violations);
        }
        return reps;

    }

    public RespBody toError(Object result) {
        Set<Violation> violations = getViolations();
        RespBody reps;
        if (violations.isEmpty()) {
            reps = new RespBody(Boolean.FALSE, RespBody.MESSAGE_INTERNAL_ERROR, result);
        } else {
            reps = new RespBody(Boolean.FALSE, RespBody.MESSAGE_VIOLATION_ERROR, result);
            reps.setViolations(violations);
        }
        return reps;

    }


}
