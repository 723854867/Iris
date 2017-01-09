package com.busap.vcs.web.h5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.BaseEntity;
import com.busap.vcs.data.entity.Evaluation;
import com.busap.vcs.data.entity.Favorite;
import com.busap.vcs.data.entity.Praise;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.service.RuserService;

public class BaseH5Controller {

	@Resource(name = "ruserService")
	private RuserService ruserService;
	
    public void parseUserInfo(Object obj) {
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
}
