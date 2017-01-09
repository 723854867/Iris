package com.busap.vcs.data;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.User;
import com.busap.vcs.data.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

/**
 * Created by djyin on 7/19/2014.
 */
public class Usage {
    public static void main(String[] args) {
        // 最默认使用JAR中带的配置文件
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
        UserRepository userRepository;
        userRepository = ctx.getBean(UserRepository.class);
        String r = RandomStringUtils.random(5);
        Filter filter = Filter.eq("name", r);
        User nUser = new User();
        nUser.setName(r);
        nUser.setUsername(r);
        nUser.setPassword(r);
        nUser.setIsEnabled(true);
        nUser.setIsLocked(false);
        nUser.setType("test");
        userRepository.save(nUser);
        Page<User> page = userRepository.findAll(new PageRequest(0, 10), Collections.singletonList(filter));
        System.out.println(page);

        User u = userRepository.findByUsername(r);
        //Iterable<User> users =  userRepository.findAll(UserRepository.UserRepositoryExpressions.isLocked());

    }


}
