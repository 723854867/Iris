package com.busap.vcs.restadmin.interceptor;


import com.busap.vcs.data.entity.OperationLog;
import com.busap.vcs.data.entity.Permission;
import com.busap.vcs.data.enums.ModuleType;
import com.busap.vcs.data.mapper.OperationLogDao;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Spring 方法拦截器
 * User: huoshanwei
 * Date: 15-10-23
 * Time: 下午5:23
 */
@Service("pageMethodInterceptor")
public class PageMethodInterceptor implements MethodInterceptor {

    @Resource
    private OperationLogDao operationLogDao;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        boolean isFunction = false;
        // 判断是否是带有功能注解
        if (invocation.getMethod().isAnnotationPresent(EnableFunction.class)) {
            isFunction = true;
        }
        // 判断是否是带有翻页注解
        if (invocation.getMethod().isAnnotationPresent(EnablePaging.class)) {
            checkEnablePaging(invocation);
        }
        // 方法处理
        Object result;
        boolean isSuccess = true;
        try {
            result = invocation.proceed();
        } catch (Throwable t) {
            isSuccess = false;
            throw t;
        } finally {
            // 清理翻页参数
            clearContextHolder();
            // 记录日志
            if (isFunction) {
                saveOperationLog(invocation);
            }
        }
        return result;
    }

    private void checkEnablePaging(MethodInvocation invocation) throws Throwable {
        // 查找方法中是否有翻页参数
        JQueryPage jqueryPage = null;
        for (Object obj : invocation.getArguments()) {
            if (JQueryPage.class.isInstance(obj)) {
                jqueryPage = (JQueryPage) obj;
                break;
            }
        }
        if (jqueryPage != null) {
            // 设置翻页参数
            addContextHolder(getPage(jqueryPage));
        }
    }

    /**
     * 获得翻页参数对象
     *
     * @param jqueryPage JQuery翻页参数
     * @return
     */
    protected Page getPage(JQueryPage jqueryPage) {
        Page rollPage = new Page();
        //当前页
        Integer page = jqueryPage.getPage();
        int currentPage = (page != null && page > 0 ? page : 1);
        rollPage.setCurrentPage(currentPage);
        //logger.debug("当前页数：[" + currentPage + "]");
        //每页显示条数
        Integer rows = jqueryPage.getRows();
        int pageSize = (rows != null && rows > 0 ? rows : 10);
        rollPage.setPageSize(pageSize);
        //logger.debug("当前页显示条数：[" + pageSize + "]");
        return rollPage;
    }

    public void addContextHolder(Page rollPage) {
        // 设置翻页参数
        PagingContextHolder.setPage(rollPage);
    }

    public void clearContextHolder() {
        // 清理过期的翻页参数
        PagingContextHolder.removePage();
    }

    //获取request对象
    private HttpServletRequest getHttpServletRequest(MethodInvocation invocation) {
        //获取request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    private void saveOperationLog(MethodInvocation invocation) {
        EnableFunction ef = invocation.getMethod().getAnnotation(EnableFunction.class);
        HttpServletRequest request = getHttpServletRequest(invocation);
        String url = request.getRequestURI();
        String referer = request.getHeader("Referer");
        String objectid = U.nvl(request.getParameter("id"));
        String method = request.getMethod();

        List<String> strList = new ArrayList<String>();
        Enumeration pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
            Object object = (Object) pNames.nextElement();
            String value = request.getParameter(object.toString());
            strList.add(object + "=" + value);
        }
        String params = "{" + StringUtils.join(strList, ",") + "}";

        String methodFunction = ef.value();
        String[] functionArray = methodFunction.split(",");
        OperationLog operationLog = new OperationLog();
        operationLog.setCreatorId(U.getUid());
        operationLog.setDescription("动作：" + functionArray[1] + "；参数：" + params);
        operationLog.setModuleType(ModuleType.移动麦视后台.getName());
        operationLog.setOperType(functionArray[1]);
        operationLog.setReqUrl(url);
        operationLog.setReferer(referer);
        operationLog.setObjectid(objectid);
        operationLog.setReqType(method);
        Permission permission = operationLogDao.selectPermissionByName(functionArray[0]);
        if(permission != null){
            operationLog.setPermissionId(permission.getId());
        }
        //operationLog.setPermissionId(Long.valueOf(functionArray[0]));
        operationLog.setIp(CommonUtils.getRemoteIp());
        operationLog.setUid(U.getUid());
        operationLog.setUname(U.getUname());
        operationLogDao.insert(operationLog);
    }

}
