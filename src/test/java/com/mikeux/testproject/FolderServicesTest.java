package com.mikeux.testproject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import javax.ws.rs.*;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.OutputCapture;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mikeux.testproject.models.Folder;
import com.mikeux.testproject.models.FolderDao;
import com.mikeux.testproject.models.User;
import com.mikeux.testproject.models.UserDao;

import jersey.repackaged.com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestProjectApplication.class)
@WebIntegrationTest({"server.port=0"})
@TestPropertySource(locations="classpath:test.properties")
public class FolderServicesTest {
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	private List<User> userList = new ArrayList<>();

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
    private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Rule
	public OutputCapture capture = new OutputCapture();
	
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

	@Before
    public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	//@Test
	public void deleteNotExistingFolder() throws Exception {
		this.mockMvc.perform(post("/deleteFolderRecursively")
				.contentType(MediaType.APPLICATION_JSON)
                .param("id", "6969"))
                .andExpect(status().isOk());
		
		Assert.assertTrue(folderDao.findOne((long) 6969) == null);
	}
	
	@Test
	@Async
	public void FullTest() throws Exception {
		List<Folder> subfolders = new LinkedList<Folder>();
		Folder parentFolder;
		String json;
		
		this.userDao.deleteAll();
		userList.add(userDao.save(new User("FolderTest1","Password")));
		userList.add(userDao.save(new User("FolderTest2","Password")));
		userList.add(userDao.save(new User("FolderTest3","Password")));
		userList.add(userDao.save(new User("FolderTest4","Password")));
	
		
        String userJson = json(userList.get(1));
		MvcResult result = this.mockMvc.perform(post("/createNewFolder")
					.contentType(MediaType.APPLICATION_JSON)
	                .content(userJson)
	                .param("name", "ParentFolder"))
	                .andExpect(status().isOk())
	                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                //.andExpect(jsonPath("$.id", is(1)))
	                .andExpect(jsonPath("$.name", is("ParentFolder")))
	                .andReturn();
		
		System.out.println(result.getResponse().getContentAsString());
		json = result.getResponse().getContentAsString();
		parentFolder = folderDao.findOne(Long.parseLong(JsonPath.read(json, "$.id").toString()));		
		System.out.println("New Folder id: "+parentFolder.getId());
		
		for(int i=0; i<5; i++) {
			result = this.mockMvc.perform(post("/createNewSubFolder")
				.contentType(MediaType.APPLICATION_JSON)
                .content(json(parentFolder))
                .param("name", "SubFolder"+i))
                .andExpect(status().isOk())
                .andReturn();
			json = result.getResponse().getContentAsString();
			subfolders.add(folderDao.findOne(Long.parseLong(JsonPath.read(json, "$.id").toString())));
		}
		
		for(int i=0; i<2; i++) {
			this.mockMvc.perform(post("/createNewSubFolder")
				.contentType(MediaType.APPLICATION_JSON)
                .content(json(subfolders.get(0)))
                .param("name", "SubSubFolder0_"+i))
                .andExpect(status().isOk());
		}
		
		for(int i=0; i<1; i++) {
			this.mockMvc.perform(post("/createNewSubFolder")
				.contentType(MediaType.APPLICATION_JSON)
                .content(json(subfolders.get(1)))
                .param("name", "SubSubFolder1_"+i))
                .andExpect(status().isOk());
		}
		
		for(int i=0; i<3; i++) {
			this.mockMvc.perform(post("/createNewSubFolder")
				.contentType(MediaType.APPLICATION_JSON)
                .content(json(subfolders.get(3)))
                .param("name", "SubSubFolder3_"+i))
                .andExpect(status().isOk());
		}
		
		this.mockMvc.perform(post("/deleteFolderRecursively")
				.contentType(MediaType.APPLICATION_JSON)
                .param("id", "1"))
                .andExpect(status().isOk());
		
		Assert.assertTrue(Lists.newArrayList((folderDao.findAll())).size() == 0);
				
		//--------------------------------------------------------------
		
		//Client client = ClientBuilder.newClient();
		//WebTarget target = client.target("http://localhost:8080/createNewFolder");
        
        //target = target.path("createNewFolder/{user}");        
        //target = target.resolveTemplate("user",userList.get(3));
        ///target = target.resolveTemplate("name","asdasd");
        //target = target.queryParam("name","Röffff");
        
        //Syn
        //Invocation.Builder builder = target.request();
        //Response response = builder.post(Entity.json(userList.get(3)));
        /*response = builder.post(Entity.json(userList.get(3)));
        response = builder.post(Entity.json(userList.get(3)));*/
        //target.request().post(Entity.entity(userList.get(3), MediaType.APPLICATION_JSON));
        
        //Asyn
        //AsyncInvoker builder = target.request().async();
        //Entity<User> valaki = Entity.json(userList.get(3));
        /*for(int i=0; i<5; i++) {
        	builder.post(valaki); 
        }*/
        
        //Response post = builder.post(Entity.entity(pojo, MediaType.APPLICATION_JSON));
        //System.out.println("--------------------------");
        
        /*String userJson = json(userList.get(0));
        for(int i=0; i<5; i++) {
	        new Thread(new Runnable() { public void run() { 
	        	try {
					mockMvc.perform(post("/createNewFolder")
							.contentType(MediaType.APPLICATION_JSON)
					        .content(userJson)
					        .param("name", "Böffff"))
					        .andExpect(status().isOk());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }}).start();	
        }*/

	}
	
	public void deleteExistingFolder() throws Exception {
		this.FullTest();
		
		this.mockMvc.perform(post("/deleteFolderRecursively")
				.contentType(MediaType.APPLICATION_JSON)
                .param("id", "1"))
                .andExpect(status().isOk());
		//Folder valamio = folderDao.findOne((long) 1987);
		//System.out.println("NULL??? => "+(valamio == null));
		Assert.assertTrue(folderDao.findOne((long) 1) == null);
	}
	
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
