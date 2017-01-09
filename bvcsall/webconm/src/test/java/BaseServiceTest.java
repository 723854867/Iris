//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.lang.RandomStringUtils;
//import org.apache.commons.lang.builder.ToStringBuilder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.busap.vcs.data.entity.User;
//import com.busap.vcs.service.BaseService;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath*:/applicationContext.xml","classpath*:/applicationContext-service.xml","classpath*:/applicationContext-data.xml"})
//public class BaseServiceTest {
//
//	@Resource(name="userServiceImpl")
//	private BaseService<User, Long> baseService;
//	@Test
//	public void find() {
//		User u=baseService.find(5L);
//		System.out.println(u.getUsername()+"===========================================================================");
//	}
//	
//	@Test
//	public void create(){
//		User u=new User();
//		u.setPassword("meizhiwen"+RandomStringUtils.randomAlphanumeric(6));
//		u.setUsername("password"+RandomStringUtils.randomAlphanumeric(6));
//		u.setIsEnabled(true);
//		u.setIsLocked(true);
//		u.setType("adminuser");
//		this.baseService.save(u);
//	}
//	
//	@Test
//	public void update() {
//		User user=this.baseService.find(8L);
//		user.setUsername("朱华绒");
//		user.setPassword("哈:"+RandomStringUtils.randomAlphanumeric(6));
//		this.baseService.update(user);
//	}
//	
//	@Test
//	public void findall() {
//		List<User> list=this.baseService.findAll();
//		for (User user : list) {
//			System.out.println(ToStringBuilder.reflectionToString(user)+"###################################");
//		}
//	}
//}
