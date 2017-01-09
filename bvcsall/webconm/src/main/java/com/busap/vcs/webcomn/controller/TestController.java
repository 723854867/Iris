package com.busap.vcs.webcomn.controller;

import com.busap.vcs.data.entity.Test;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.TestService;
import com.busap.vcs.webcomn.RespBody;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created with IntelliJ IDEA and by
 * User: djyin
 * DateTime: 11/13/13 5:07 PM
 */
//@Controller("testController")
//@RequestMapping({"/test"})
@Lazy(false)
public class TestController extends CRUDController<Test, Long> {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource(name = "testServiceImpl")
    TestService testService;

    @PostConstruct
    void init() {
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");
    }

    @Resource(name = "testServiceImpl")
    @Override
    public void setBaseService(BaseService<Test, Long> baseService) {
        this.baseService = baseService;
    }

    /**
     * 进入测试页面
     *
     * @return
     */
//    @RequestMapping(value = "/example", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.OK)
    public String example() {
        return "example";
    }

    /**
     * spring base system summary
     *
     * @param request the request
     * @return the request result
     */
//    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RespBody index(HttpServletRequest request) {

        List<Test> systemSummary = new ArrayList<Test>();

        // web容器
        ServletContext sc = request.getSession().getServletContext();
        systemSummary.add(new Test("serverInfo", sc.getServerInfo(), "ServletContext"));
        systemSummary.add(new Test("servletVersion", sc.getMajorVersion()
                + "." + sc.getMinorVersion(), "ServletContext"));
        systemSummary.add(new Test("majorVersion", String.valueOf(sc.getMajorVersion()), "ServletContext"));
        systemSummary.add(new Test("minorVersion", String.valueOf(sc.getMinorVersion()), "ServletContext"));

        /*  servlet 3.0
        systemSummary.add(new Test("cookie.domain", sc.getSessionCookieConfig().getDomain(),"ServletContext"));
        systemSummary.add(new Test("cookie.name", sc.getSessionCookieConfig().getName(),"ServletContext"));
        systemSummary.add(new Test("cookie.maxAge", String.valueOf(sc.getSessionCookieConfig().getMaxAge()),"ServletContext"));
        */

        systemSummary.add(new Test("contextPath", sc.getContextPath(), "ServletContext"));

        //spring配置

        WebApplicationContext wsc = WebApplicationContextUtils.getWebApplicationContext(sc);

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        systemSummary.add(new Test("startupDate", sf.format(new Date(wsc.getStartupDate())), "ServletContext"));

        //数据库连接池状态
        String[] dsNames = wsc.getBeanNamesForType(DataSource.class);
        if (dsNames != null) {
            for (int i = 0; i < dsNames.length; i++) {
                String dsName = dsNames[i];
                DataSource ds = wsc.getBean(dsName, DataSource.class);
                try {
                    Properties props = ds.getConnection().getClientInfo();
                    for (Map.Entry p : props.entrySet()) {
                        systemSummary.add(new Test(dsName + "." + p.getKey().toString(), p.getValue().toString(), "DataSource"));
                    }

                } catch (SQLException e) {
                    // ignored
                    systemSummary.add(new Test(dsName + ".status", e.getMessage(), "DataSource"));
                }
                systemSummary.add(new Test(dsName + ".status", "ok", "DataSource"));

            }
        }
        //缓存连接池状态

        //JVM

        int mb = 1024 * 1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();


        //Print used memory
        systemSummary.add(new Test("java.vm.usedMemory", String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / mb), "JVM"));

        //Print free memory
        systemSummary.add(new Test("java.vm.freeMemory",
                String.valueOf((runtime.freeMemory() / mb)), "JVM"));

        //Print total available memory
        systemSummary.add(new Test("java.vm.totalMemory", String.valueOf((runtime.totalMemory() / mb)), "JVM"));

        //Print Maximum available memory
        systemSummary.add(new Test("java.vm.maxMemory", String.valueOf((runtime.maxMemory() / mb)), "JVM"));


        //java.version
        systemSummary.add(new Test("java.version", System.getProperty("java.version"), "JVM"));

        //java.vendor
        systemSummary.add(new Test("java.vendor", System.getProperty("java.vendor"), "JVM"));

        //java.vm.name
        systemSummary.add(new Test("java.vm.name", System.getProperty("java.vm.name"), "JVM"));

        //java.class.path
        systemSummary.add(new Test("java.class.path", System.getProperty("java.class.path"), "JVM"));

        //java.library.path
        systemSummary.add(new Test("java.library.path", System.getProperty("java.library.path"), "JVM"));

        //os.name
        systemSummary.add(new Test("os.name", System.getProperty("os.name"), "JVM"));

        //os.arch
        systemSummary.add(new Test("os.arch", System.getProperty("os.arch"), "JVM"));

        //os.version
        systemSummary.add(new Test("os.version", System.getProperty("os.version"), "JVM"));

        //user.dir
        systemSummary.add(new Test("user.dir", System.getProperty("user.dir"), "JVM"));

        //user.language
        systemSummary.add(new Test("user.language", System.getProperty("user.language"), "JVM"));

        //file.encoding
        systemSummary.add(new Test("file.encoding", System.getProperty("file.encoding"), "JVM"));

        List<Test> all = testService.findAll();
        if (all != null && !all.isEmpty()) {
            Long[] ids = new Long[all.size()];
            int i = -1;
            for (Test t : all) {
                ids[i++] = t.getId();
            }
            testService.delete(ids);
        }
        for (Test t : systemSummary) {
            testService.save(t);
        }
        return this.respBodyWriter.toSuccess(systemSummary);
    }

    /**
     * 以下是测试CRUD用的命令
     *
     curl -i -L -H "Accept: application/json" -H "Content-type: application/json;charset=UTF-8" http://127.0.0.1:8080/webconm/test/ping?id=100&message=ping

     curl -i -L -H "Accept: application/xml" -H "Content-type: application/json;charset=UTF-8" http://127.0.0.1:8080/webconm/test/ping?id=100&message=ping

     curl -i -L -H "Accept: application/xml" http://127.0.0.1:8080/webconm/test/list?start=0&limit=5

     curl -i -L -H "Accept: application/json" http://127.0.0.1:8080/webconm/test/curdform

     curl -i -L -H "Accept: application/json" -H "Content-type: application/json;charset=UTF-8" -d '{ "filters" : {"idEq" : "44","sysvalueIsNotNull" : ""}, "orders" : "idDesc"}' http://127.0.0.1:8080/webconm/test/find?start=0&limit=5

     curl -i -L -H "Accept: application/xml" -H "Content-type: application/json;charset=UTF-8" -d '{ "filters" : {"idEq" : "44","sysvalueIsNotNull" : ""}, "orders" : "idDesc"}' http://127.0.0.1:8080/webconm/test/find?start=0&limit=5
     */


    /**
     * Json ping. 测试jackson2的提交和返回
     * 测试用的curl命令是:
     * curl -i -L -H "Accept: application/json" -H "Content-type: application/json;charset=UTF-8"  -X PUT -d '{"id":100,"message":"PING"}' http://127.0.0.1:8080/bvcs/test/jsontest
     *
     * @return the response
     */
//    @RequestMapping(value = "/ping", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RespBody jsontest(Long id, String message) {
        RespBody pong = null;
//        if (StringUtils.isNotBlank(message) && message.equalsIgnoreCase("PING")) {
//            pong = this.respBodyWriter.toSuccess("PONG", id);
//        } else {
//            pong = this.respBodyWriter.toSuccess(message, id);
//        }
        return pong;
    }

    /**
     * curdform. 返回一个curdform的例子
     *
     * @return the response
     */
//    @RequestMapping(value = "/curdform", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RespBody curdform() {
        CRUDForm form = new CRUDForm();
        form.getFilters().put("idEq", "44");
        form.getFilters().put("sysvalueIsNotNull", "");
        form.setOrders("idDescSysvalueAsc");
        RespBody pong = this.respBodyWriter.toSuccess("PONG", form);
        return pong;
    }


}

