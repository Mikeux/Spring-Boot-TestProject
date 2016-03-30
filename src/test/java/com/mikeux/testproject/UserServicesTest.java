package com.mikeux.testproject;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.OutputCapture;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.mikeux.testproject.models.UserDao;

import jersey.repackaged.com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestProjectApplication.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@TestPropertySource(locations="classpath:test.properties")
public class UserServicesTest {

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







