package org.Iris.util.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.Iris.util.domain.User;

import junit.framework.TestCase;

public class SerilizeUtilTest extends TestCase {

	public void testProtostuff() throws Exception {
		User user = new User();
		user.setAge(12);
		user.setHigh(2.35);
		user.setMale(true);
		user.setMoney(2.312);
		user.setName("凡事都");
		user.setTeenage(false);
		user.setSex(5);
		byte[] data = SerializeUtil.ProtostuffUtil.serial(user);
		System.out.println(Arrays.toString(data));
		
		User user2 = SerializeUtil.ProtostuffUtil.deserial(data, User.class);
		assertEquals(user2.getAge(), user.getAge());
		assertEquals(user2.getHigh(), user.getHigh());
		assertEquals(user2.isMale(), user.isMale());
		assertEquals(user2.getMoney(), user.getMoney());
		assertEquals(user2.getName(), user.getName());
		assertEquals(user2.getTeenage(), user.getTeenage());
		assertEquals(user2.getSex(), user.getSex());
		
		int ad = 100;
		data = SerializeUtil.ProtostuffUtil.serial(ad);
		System.out.println(Arrays.toString(data));
		int s = SerializeUtil.ProtostuffUtil.deserial(data, Integer.class);
		System.out.println(s);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SerializeUtil.ProtostuffUtil.serial(user, out);
		data = out.toByteArray();
		System.out.println(Arrays.toString(data));
	}
}
