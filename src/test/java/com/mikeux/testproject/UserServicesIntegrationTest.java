package com.mikeux.testproject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;

import com.mikeux.testproject.models.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestProjectApplication.class)
@WebIntegrationTest({"server.port=0", "management.port=0"}) //User Random ports
@TestPropertySource(locations="classpath:test.properties")
//@ContextConfiguration(classes = Config.class, initializers = ConfigFileApplicationContextInitializer.class)
public class UserServicesIntegrationTest {
	
	//https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
	//http://www.jayway.com/2014/07/04/integration-testing-a-spring-boot-application/
	
	RestTemplate restTemplate = new TestRestTemplate();
	
	@Autowired
	private UserDao userDao;
	
	@Before
    public void setUp() {
	
	}
	
	@Rule
	public OutputCapture capture = new OutputCapture();

	@Test
	public void testName() throws Exception {
		System.out.println("Hello World!");
		assertThat(capture.toString(), containsString("World"));		
	}
	
}
