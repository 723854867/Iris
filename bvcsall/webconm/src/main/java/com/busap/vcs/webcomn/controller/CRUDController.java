package com.busap.vcs.webcomn.controller;


import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.parser.OrderBySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.base.Filter;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.BaseEntity;
import com.busap.vcs.data.entity.OperationLog;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.OperationLogService;
import com.busap.vcs.webcomn.Constants;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;

/**
 * 默认实现的CRUD的SpringMVC Controller
 */
@Controller
public abstract class CRUDController<T extends BaseEntity, ID extends Serializable> extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(CRUDController.class);

    /**
     * The Base service. 这玩意没法autowire啊..
     */
    protected BaseService<T, ID> baseService;
    
    @Autowired
    protected OperationLogService operationLogService;
    
    @Value("${log_meduleType}")
    protected String log_meduleType;


    public CRUDController() {
        super();

    }
    
    @Autowired  
    protected  HttpServletRequest request;  

    /**
     * Sets base service.子类必须实现这个方法,而且必须注入一个BaseService<T, ID>对象进来
     *
     * @param baseService the base service
     */
    public abstract void setBaseService(BaseService<T, ID> baseService);


    protected RespBody list(Integer start, Integer limit, List<Filter> filters, Sort sort) {
        List<T> entities = baseService.findAll(start, limit, filters, sort);
        return respBodyWriter.toSuccess(entities);
    }
    protected List<T> listAndNotReturn(Integer start, Integer limit, List<Filter> filters, Sort sort) {
    	List<T> entities = baseService.findAll(start, limit, filters, sort);
    	return entities;
    }

    // 支持分页
//    protected RespBody listPage(Integer start, Integer limit, List<Filter> filters, Sort sort) {
//        Pageable pr = new PageRequest(start, limit, sort);
//        Page<T> page = baseService.findAll(pr, filters);
//        return respBodyWriter.toSuccess(page);
//    }
    // 支持分页
    protected RespBody listPage(Integer page, Integer size, List<Filter> filters, Sort sort) {
        Pageable pr = new PageRequest(page, size, sort);
        Page<T> p = baseService.findAll(pr, filters);
        return respBodyWriter.toSuccess(p);
    }


    /**
     * Create.
     *
     * @param entity the entity
     * @return the request result
     */
    @RequestMapping(value = {"/create"},
            method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody create(T entity) {
        if (!validator(entity, BaseEntity.Save.class)) {
            return respBodyWriter.toError(ResponseCode.CODE_455.toString(),ResponseCode.CODE_455.toCode());
        }
        baseService.save(entity);
        this.operationLogService.save(new OperationLog(log_meduleType, "添加", entity.getId().toString(), entity.getClass().getSimpleName(), U.getUid(), U.getUname(), "添加"+entity.getClass().getSimpleName()));
        return respBodyWriter.toSuccess(entity);
    }

    /**
     * Get request.
     *
     * @param id the id
     * @return the request result
     */
    @RequestMapping(value = {"/get"},
            method = {RequestMethod.GET})
    @ResponseBody
    public RespBody get(@RequestParam ID id) {
        T entity = baseService.find(id);
        return respBodyWriter.toSuccess(entity);
    }

    /**
     * Update request.
     *
     * @param entity the entity
     * @return the request result
     */
    @RequestMapping(value = {"/update"},
            method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody update(T entity) {
        if (!validator(entity, BaseEntity.Update.class)) {
            return respBodyWriter.toError("",ResponseCode.CODE_455.toCode());
        }

        baseService.update(entity);
        this.operationLogService.save(new OperationLog(log_meduleType, "更新", entity.getId().toString(), entity.getClass().getSimpleName(), U.getUid(), U.getUname(), "更新"+entity.getClass().getSimpleName()));
        return respBodyWriter.toSuccess(entity);
    }

    /**
     * Delete request
     *
     * @param id the id
     * @return the request result
     */
    @RequestMapping(value = {"/delete"},
            method = {RequestMethod.GET, RequestMethod.DELETE,RequestMethod.POST})
    @ResponseBody
    public RespBody delete(@RequestParam ID id) {
        T entity = baseService.find(id);
        if (entity != null) {
            baseService.delete(id);
        }
        RespBody reps = new RespBody(Boolean.TRUE, RespBody.MESSAGE_OK, id);
        this.operationLogService.save(new OperationLog(log_meduleType, "删除", entity.getId().toString(), entity.getClass().getSimpleName(), U.getUid(), U.getUname(), "删除"+entity.getClass().getSimpleName()));
        return reps;

    }

    /**
     * Delete resp body.
     *
     * @param ids the ids
     * @return the resp body
     */
    @RequestMapping(value = {"/deleteall"}, method = {RequestMethod.POST})
    @ResponseBody
    public RespBody deleteAll(@RequestParam  ID[] ids) {
        if (ids != null) {
            baseService.delete(ids);
        }
        RespBody reps = new RespBody(Boolean.TRUE, RespBody.MESSAGE_OK, ids);
        this.operationLogService.save(new OperationLog(log_meduleType, "删除全部", StringUtils.join(ids,","), baseService.getEntityClass().getSimpleName(), U.getUid(), U.getUname(), "删除全部"+baseService.getEntityClass().getSimpleName()));
        return reps;
    }

    /**
     * List request result.
     *
     * @param start 元素开始的index下标,注意不是页码
     * @param limit 返回集合的长度,相当于分页大小
     * @return the request result
     */
    @RequestMapping(value = {"/list"},
            method = {RequestMethod.GET})
    @ResponseBody
    public RespBody list(@RequestParam Integer start, @RequestParam Integer limit) {
        return list(start, limit, null, null);
    }

    /**
     * curdForm是动态查询条件
     * JSP写法：
     * <input name="curdForm.filters['nameLike']" value="ccc" />
     * <input name="curdForm.orders['idDesc']" value="ccc" />
     */
    @RequestMapping(value = {"/find"},
            method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RespBody find(@RequestParam Integer start, @RequestParam Integer limit, @RequestBody CRUDForm curdForm) {
        // 从request中获取通用查询条件
        List<Filter> filters = new ArrayList<Filter>();
        for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
            filters.add(condToFilter(entry.getKey(), entry.getValue()));
        }
        Sort sort = null;
        if (StringUtils.isNotBlank(curdForm.getOrders())) {
            Class<T> clz = baseService.getEntityClass();
            OrderBySource source = new OrderBySource(curdForm.getOrders(), clz);
            sort = source.toSort();
        }
        return list(start, limit, filters, sort);
    }

    /**
     * 通用的单实体(Entity)的查找方法
     * filters key=createDateLt,value=201203232
     * orders createDateDesc idAsc
     *
     * @param page 开始页码
     * @param size 分页大小
     * @return the resp body
     */
    @RequestMapping(value = {"/page"},
            method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RespBody listPage(@RequestParam Integer page, @RequestParam Integer size) {
        return listPage(page, size, null, null);
    }

    /**
     * curdForm是动态查询条件
     * JSP写法：
     * <input name="curdForm.filters['nameLike']" value="ccc" />
     * <input name="curdForm.orders['idDesc']" value="ccc" />
     * Ajax写法:
     *
     * @param page 开始页码
     * @param size 分页大小
     */
    @RequestMapping(value = {"/findpage"},
            method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RespBody findPage(@RequestParam Integer page, @RequestParam Integer size, @RequestBody CRUDForm curdForm) {
        // 从request中获取通用查询条件
        List<Filter> filters = new ArrayList<Filter>();
        for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
            filters.add(condToFilter(entry.getKey(), entry.getValue()));
        }
        Sort sort = null;
        if (StringUtils.isNotBlank(curdForm.getOrders())) {
            Class<T> clz = baseService.getEntityClass();
            OrderBySource source = new OrderBySource(curdForm.getOrders(), clz);
            sort = source.toSort();
        }
        return listPage(page, size, filters, sort);
    }


    @SuppressWarnings("rawtypes")
    protected Filter condToFilter(String cond, String condValue) {
        Filter filter = new Filter(cond, condValue);
        Object obj = null;
        PropertyDescriptor pd = this.baseService.getEntityPropertyDescriptor(filter.getProperty().toLowerCase());
        Class<?> cls = pd.getPropertyType();
        // 减少反射调用
        if (cls.equals(String.class)) { // String 型
            return filter;
        } else if (cls.equals(Integer.class)) {
            obj = Integer.valueOf(condValue);
        } else if (cls.equals(Long.class)) {
            obj = Long.valueOf(condValue);
        } else if (cls.equals(Date.class)) {
            obj = Double.valueOf(condValue);
        } else if (cls.equals(Double.class)) {
            obj = Double.valueOf(condValue);
        } else {
            // 调用构造函数
            try {
                Constructor c = pd.getPropertyType().getConstructor(String.class);
                obj = c.newInstance(condValue);
            } catch (NoSuchMethodException e) {
                log.error("通用查找自动转换Number型参数失败:", e);
            } catch (InvocationTargetException e) {
                log.error("通用查找自动转换Number型参数失败:", e);
            } catch (InstantiationException e) {
                log.error("通用查找自动转换Number型参数失败:", e);
            } catch (IllegalAccessException e) {
                log.error("通用查找自动转换Number型参数失败:", e);
            }
        }

        if (obj != null) {
            filter.setValue(obj);
        } else { // 尝试使用BigDecimal
            BigDecimal num = new BigDecimal(condValue);
            filter.setValue(num);
        }
        return filter;
    }

    @SuppressWarnings("rawtypes")
	@RequestMapping(value = {"/listpage"},
            method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map listpage(Integer page, Integer rows, CRUDForm curdForm) {
        // 从request中获取通用查询条件
    	if(page==0){
    		page=1;
    	}
        List<Filter> filters = new ArrayList<Filter>();
        for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
        	if(StringUtils.isNotBlank(entry.getValue())){
        		filters.add(condToFilter(entry.getKey(), entry.getValue()));
        	}
        }
        Sort sort = null;
        if (StringUtils.isNotBlank(curdForm.getOrders())) {
            Class<T> clz = baseService.getEntityClass();
            OrderBySource source = new OrderBySource(curdForm.getOrders(), clz);
            sort = source.toSort();
        }
        RespBody res=listPage(page, rows, filters, sort);
        Page pinfo=(Page)res.getResult();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
    }
    
    @SuppressWarnings("rawtypes")
	@RequestMapping("upload")
	public @ResponseBody Map upload(@RequestParam MultipartFile[] file,HttpServletRequest req,HttpServletResponse resp){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.RESULT, "success");
		
		String uploadupath=setUploadPath(req);
		
		log.info(uploadupath);
		for(MultipartFile myfile : file){
            if(myfile.isEmpty()){
            	log.error("上传文件为空xxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                map.put(Constants.MESSAGE, "上传失败");
            }else{
                String originalFilename = myfile.getOriginalFilename();
                log.info("文件原名: " + originalFilename);
                log.info("上传文件input name: " + myfile.getName());
                log.info("文件长度: " + myfile.getSize());
                log.info("文件类型: " + myfile.getContentType());
                log.info("========================================");
                try {
                    FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(uploadupath, DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_")+originalFilename));
                } catch (IOException e) {
                	log.error("文件[" + originalFilename + "]上传失败,堆栈轨迹如下",e);
                    map.put(Constants.MESSAGE, "上传失败，请重试");
                }
            }
		}
		if(map.containsKey(Constants.MESSAGE)){
			map.put(Constants.RESULT, "error");
		}
		return map;
	}
    
    /**
     * 可供继承，设置上传文件的路径
     * @param req
     * @return
     */
    protected String setUploadPath(HttpServletRequest req){
    	File devFile=FileUtils.getFile(req.getSession().getServletContext().getRealPath(""));
		String uploadupath=devFile.getParentFile().getAbsolutePath()+File.separator+"upload"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		return uploadupath;
    }
}
