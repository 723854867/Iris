package com.busap.vcs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.busap.vcs.data.entity.Song;
import com.busap.vcs.data.vo.SongVo;
import com.busap.vcs.service.SongService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.mapper.MusicDAO;
import com.busap.vcs.data.vo.MusicVO;
//import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import scala.reflect.New;
import scala.util.parsing.combinator.testing.Str;

@Controller
@RequestMapping("/music")
public class MusicController {

	private Logger logger = LoggerFactory.getLogger(MusicController.class);

	@Autowired
	MusicDAO music;

	@Resource
	private SongService songService;

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

	// 获得全部音乐
	@RequestMapping("/getAllMusic")
	@ResponseBody
	public RespBody getAllMusic() {
		String uid = (String) this.request.getHeader("uid");
		logger.info("uid={},getAllMusic", uid);
		List<MusicVO> list = music.selectAllMusic();
		return respBodyWriter.toSuccess(list);

	}

	//搜索音乐
	@RequestMapping("getMusicByKeyword")
	@ResponseBody
	public RespBody getMusicByKeyword(String keyword,Integer page,Integer rows){
		if(StringUtils.isBlank(keyword) || page == null || rows == null){
			return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
		}
		if (page <= 0) {
			page = 1;
		}
		if (rows <= 0) {
			rows = 10;
		}
		Map<String,Object> params = new HashMap<String, Object>(3);
		params.put("keyword",keyword);
		params.put("pageStart",(page-1)*rows);
		params.put("pageSize",rows);
		List<SongVo> list = songService.querySongList(params);
		if(list.isEmpty()){
			Map<String,Object> recommendParams = new HashMap<String, Object>(2);
			recommendParams.put("pageStart",0);
			recommendParams.put("pageSize",100);
			list = songService.querySongList(recommendParams);
			return respBodyWriter.toSuccess("success",list,"recommend");
		}
		return respBodyWriter.toSuccess("success",list,"normal");

	}

	//记录下载量
	@RequestMapping("recordMusicDownloads")
	@ResponseBody
	public RespBody recordMusicDownloads(Long id){
		if(id > 0){
			Song song = songService.selectByPrimaryKey(id);
			if(song != null && song.getState() == 1){
				song.setDownloadCount(song.getDownloadCount()+1);
				int result = songService.updateByPrimaryKey(song);
				if(result > 0){
					return respBodyWriter.toSuccess();
				}else{
					return respBodyWriter.toError(ResponseCode.CODE_500.toString(), ResponseCode.CODE_500.toCode());
				}
			}
		}
		//参数错误
		return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());

	}


}
