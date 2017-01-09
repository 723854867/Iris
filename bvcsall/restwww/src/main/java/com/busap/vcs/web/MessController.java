package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.busap.vcs.service.*;
import com.busap.vcs.webcomn.util.DelMsgStructure;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.vo.MessVO;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/mes")
public class MessController {

	private Logger logger = LoggerFactory.getLogger(MessController.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

	@Resource(name="messService")
	private MessService messService;

	@Resource(name="attentionService")
	private AttentionService attentionService;

	@Resource(name="praiseService")
	private	PraiseService praiseService;

	@Resource(name="forwardService")
	private ForwardService forwardService;

	@Resource(name="evaluationService")
	private	EvaluationService evaluationService;

	// 添加或取消关注
	@RequestMapping("/getMess")
	@ResponseBody
	@Transactional
	public RespBody getMess(Integer page, Integer size,Integer type) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		if(null==uid)return respBodyWriter.toError("当前登陆用户id为空,可能登陆已失效", ResponseCode.CODE_304.toCode());
		List<JSONObject> list = new ArrayList<JSONObject>();
		List<MessVO>  mess = messService.findMess(page, size, uid,type);
		for(MessVO mv: mess){
			JSONObject mesJson=new JSONObject();
			if(mv.getType().equals("2")||mv.getType().equals("1")){
				int isAttention = 0;
				if (attentionService.isAttention(Long.parseLong(uid), Long.valueOf(mv.getOid())) == 1) {
					isAttention = 1 + attentionService.isAttention(Long.valueOf(mv.getOid()), Long.parseLong(uid));
				}
				mesJson.put("isAttend", isAttention);
			} else {
				mesJson.put("isAttend", "");
			}
			mesJson.put("vstat", mv.getVstat());
			mesJson.put("type", mv.getType());
			mesJson.put("oid", mv.getOid());//操作人ID
			mesJson.put("name", mv.getName());
			mesJson.put("avatar", mv.getAvatar());
			mesJson.put("create_at", mv.getCreate_at().substring(0, mv.getCreate_at().length() - 2));
			mesJson.put("uid", mv.getUid());
			mesJson.put("videoId", mv.getVideoId());
			mesJson.put("videoPic", mv.getVideoPic());
			mesJson.put("id", mv.getId());
			list.add(mesJson);
		}
		return respBodyWriter.toSuccess(list);
	}

	@RequestMapping("/deleteMess")
	@ResponseBody
	@Transactional
	public RespBody deleteMess(String jsonParam) {
		logger.info("delete message: jsonParam=" + jsonParam);
		JSONArray jsonArray = JSONArray.fromObject(jsonParam);
		logger.info("delete message: jsonArray=" + jsonArray.toString());
		List<DelMsgStructure> msgList = (List<DelMsgStructure>) JSONArray.toCollection(jsonArray, DelMsgStructure.class);
		List<Long> attentionList = new ArrayList<Long>();
		List<Long> praiseList = new ArrayList<Long>();
		List<Long> evaluationList = new ArrayList<Long>();
		List<Long> forwardList = new ArrayList<Long>();
		for (DelMsgStructure delMsg : msgList) {
			int type = Integer.parseInt(delMsg.getType());
			Long id = Long.parseLong(delMsg.getId());
			if (id > 0) {
				switch (type) {
				/* 1=赞 */
					case 1 :
						praiseList.add(id);
						break;
				/* 2=关注 */
					case 2 :
						attentionList.add(id);
						break;
				/* 3=评价 */
					case 3 :
						evaluationList.add(id);
						break;
				/* 4=转发 */
					case 4 :
						forwardList.add(id);
						break;
					default:
						break;
				}
			}
		}
		if (attentionList.size() > 0) {
			attentionService.deleteAttentionByIds(attentionList);
		}
		if (praiseList.size() > 0) {
			praiseService.deletePraiseByIds(praiseList);
		}
		if (evaluationList.size() > 0) {
			evaluationService.deleteEvaluationByIds(evaluationList);
		}
		if (forwardList.size() > 0) {
			forwardService.deleteForwardByIds(forwardList);
		}

		return respBodyWriter.toSuccess(1);
	}


}
