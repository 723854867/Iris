package org.Iris.app.web.template.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.web.template.service.persistence.domain.User;
import org.Iris.app.web.template.service.persistence.mapper.UserMapper;
import org.Iris.app.web.template.web.IrisServlet;
import org.springframework.beans.factory.annotation.Autowired;

public class HelloWorld extends IrisServlet {

	private static final long serialVersionUID = -1633606111581327433L;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<User> list = userMapper.selectAll();
		System.out.println(list);
		System.out.println("here");
		try {
			TimeUnit.HOURS.sleep(6);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
