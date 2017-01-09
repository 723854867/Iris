package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource; 

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; 

import com.busap.vcs.data.entity.HotLabel;
import com.busap.vcs.data.entity.Label;
import com.busap.vcs.data.entity.Tag;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.HotLabelService;
import com.busap.vcs.service.LabelService;
import com.busap.vcs.service.SensitiveWordService;
import com.busap.vcs.service.TagService;
import com.busap.vcs.service.impl.SolrWoPaiTagService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/tag")
public class TagController extends CRUDController<Tag, Long> {

    private Logger logger = LoggerFactory.getLogger(TagController.class);

    @Resource(name="tagService")
    TagService tagService; 
    
    @Resource(name="sensitiveWordService") 
    SensitiveWordService sensitiveWordService;
    
    @Resource(name="hotLabelService")
    HotLabelService hotLabelService;
    
    @Resource(name="labelService")
    LabelService labelService;
    
    @Autowired
    private SolrWoPaiTagService solrWoPaiTagService;

    @Resource(name="tagService")
    @Override
    public void setBaseService(BaseService<Tag, Long> baseService) {
        this.baseService = baseService;
    }  
    
    @RequestMapping("/tagList")
    @ResponseBody
    public RespBody tagList(){ 
    	return this.respBodyWriter.toSuccess(tagService.findAll()); 
    } 
    
    
    /**
     * 返回默认的最热或者运营推荐的tag，上限为20个
     * @return
     */
    @RequestMapping("hottag")
    @ResponseBody
    public RespBody getHottag(){
    	List<HotLabel> hotLabelList = hotLabelService.findAllOrderByDisplayorder();
    	return this.respBodyWriter.toSuccess(hotLabelList);
    }
    
    /**
     * 返回用户搜索的tag列表
     * 暂时用数据库like 'xxx%'实现，后期要做全文检索
     * @param tag
     * @return
     */
    @RequestMapping("getmatchtag")
    @ResponseBody
    public RespBody getMatchTag(@RequestParam String tag){
    	//List<Label> labelList = labelService.findMatchTag(tag);
		try {
			List<Label> labelList = solrWoPaiTagService.search(tag);
			return this.respBodyWriter.toSuccess(labelList);
		} catch (SolrServerException e) {
			e.printStackTrace();
			logger.error("getmatchtag method error,error msg is {}",e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.respBodyWriter.toSuccess(new ArrayList<Label>(0));
    }
    
    
    /**
     * 
     * 发现页展示4个不知道是什么狗P玩意的标签
     * @return
     */
    @RequestMapping("discoverytag")
    @ResponseBody
    public RespBody getDiscoveryTag(){
    	List<HotLabel> hotLabelList = hotLabelService.find4ByShowOrder();
    	
    	return this.respBodyWriter.toSuccess(hotLabelList);
    }
     
}

