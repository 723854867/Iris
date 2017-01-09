package com.busap.vcs.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Favorite;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.FavoriteService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/favorite")
public class FavoriteController extends CRUDController<Favorite, Long> {

    private Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Resource(name="favoriteService")
    FavoriteService favoriteService; 
    
    @Resource(name="videoService") 
    VideoService videoService;

    @Resource(name="favoriteService")
    @Override
    public void setBaseService(BaseService<Favorite, Long> baseService) {
        this.baseService = baseService;
    } 
    
    //按页查询我的收藏夹
    @RequestMapping("/findFavoriteList")
    @ResponseBody
    public RespBody findFavoriteList(Integer page, Integer size){
    	String uid = (String)this.request.getHeader("uid"); 
    	return respBodyWriter.toSuccess(favoriteService.findFavoriteList(page, size, uid)); 
    }
    
    @RequestMapping("/findFavoriteListByLastId")
    @ResponseBody
    public RespBody findFavoriteListByLastId(Long date, Integer count){
    	String uid = (String)this.request.getHeader("uid"); 
    	List<Video> ls = favoriteService.findFavoriteListByLastId((date==null||date==0)?null:new Date(date), count, uid);
    	return  this.respBodyWriter.toSuccess(ls);
    } 
    
    @RequestMapping("/saveFavorite")
    @ResponseBody
    public RespBody saveFavorite(Favorite f){
    	String uid = (String)this.request.getHeader("uid"); 
    	f.setCreatorId(Long.parseLong(uid));
    	f.setDataFrom(DataFrom.麦视rest接口.getName());
    	
    	Video v = videoService.find(f.getVideoId()); 
		if(v==null||VideoStatus.已删除.getName().equals(v.getFlowStat())){
			return this.respBodyWriter.toError(ResponseCode.CODE_332.toString(), ResponseCode.CODE_332.toCode());
		}
		
		boolean isf =favoriteService.isFavorited(f.getVideoId(),Long.parseLong(uid));
		if(isf){
			return this.respBodyWriter.toError(ResponseCode.CODE_202.toString(), ResponseCode.CODE_202.toCode());
		}
		
		try{
			favoriteService.saveFavorite(f);
		}catch(DataIntegrityViolationException e){
			//同一视频重复收藏，即便爆出错误，也算收藏成功，数据库有唯一键限制， 所以在此捕获错误。
			return this.respBodyWriter.toError(ResponseCode.CODE_202.toString(), ResponseCode.CODE_202.toCode());
		}
    	return this.respBodyWriter.toSuccess(); 
    }
    
    @RequestMapping("/deleteFavorite")
    @ResponseBody
    public RespBody deleteFavorite(Long videoId){ 
    	String uid = (String)this.request.getHeader("uid"); 
    	boolean isf =favoriteService.isFavorited(videoId,Long.parseLong(uid));
		if(!isf){
			return this.respBodyWriter.toError(ResponseCode.CODE_338.toString(), ResponseCode.CODE_338.toCode());
		}
    	favoriteService.deleteFavorite(videoId,Long.parseLong(uid));
    	return this.respBodyWriter.toSuccess(); 
    } 
    
    @RequestMapping("/multiDeleteFavorite")
    @ResponseBody
    public RespBody multiDeleteFavorite(String videoIds){ 
    	String uid = (String)this.request.getHeader("uid"); 
    	String[] videoIdArray = videoIds.split(",");
    	for(int i=0;i<videoIdArray.length;i++){
    		favoriteService.deleteFavorite(Long.parseLong(videoIdArray[i]),Long.parseLong(uid));
    	}
    	return this.respBodyWriter.toSuccess(); 
    } 
}

