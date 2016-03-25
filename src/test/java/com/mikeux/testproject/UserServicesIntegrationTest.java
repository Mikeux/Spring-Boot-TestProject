package com.mikeux.testproject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;

import com.mikeux.testproject.models.UserDao;
import com.mikeux.testproject.services.UserServices;

import jersey.repackaged.com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestProjectApplication.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class UserServicesIntegrationTest {

	@Autowired
    private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Autowired
	private UserDao userDao;
	
	@Before
    public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Rule
	public OutputCapture capture = new OutputCapture();

	//@Test
	public void testName() throws Exception {
		System.out.println("Hello World!");
		assertThat(capture.toString(), containsString("World"));		
	}
	
	@Test
	public void createUser() throws Exception {
		int userCount = 5;
		this.userDao.deleteAll();
		for(int i=0; i < userCount; i++) {
			mockMvc.perform(get("/createNewUser")
					.param("name", "TesUser"+i)
					.param("password", "secret"+i));
		}
		
		Assert.assertTrue(Lists.newArrayList(userDao.findAll()).size() == 5);
	}	
	
}







