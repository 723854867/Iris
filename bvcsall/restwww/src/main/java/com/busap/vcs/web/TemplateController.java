package com.busap.vcs.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.mapper.TemplateMapper;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
 
@Controller
@RequestMapping("/template")
public class TemplateController{

    private Logger logger = LoggerFactory.getLogger(TemplateController.class); 

    @Autowired
    TemplateMapper template;
    
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    //获取模板/滤镜列表
    @RequestMapping("/findAll")
    @ResponseBody
    public RespBody findAll(int type){   
    	return respBodyWriter.toSuccess(template.getAll(type)); 
    }  
}

