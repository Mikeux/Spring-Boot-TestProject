package com.mikeux.testproject;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.OutputCapture;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikeux.testproject.models.Label;
import com.mikeux.testproject.models.LabelDao;
import com.mikeux.testproject.models.Label.LabelType;

import jersey.repackaged.com.google.common.collect.Lists;

import com.mikeux.testproject.models.User;
import com.mikeux.testproject.models.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestProjectApplication.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class LabelServicesTest {
	@Autowired
    private WebApplicationContext wac;
	private MockMvc mockMvc;
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private LabelDao labelDao;
	
	@Before
    public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Rule
	public OutputCapture capture = new OutputCapture();

	@Test
	public void createLabel() throws Exception {
		labelDao.deleteAll();
		User parentuser = userDao.save(new User("CreateLabelTest","Password"));				
				
		for(int i=0; i<5; i++) {
		MvcResult result = this.mockMvc.perform(post("/createNewLabel")
				.contentType(MediaType.APPLICATION_JSON)
                .content(json(parentuser))
                .param("name", "NewLabel"+i)
                .param("type", LabelType.User.toString())
                .param("description", "description"+i)
                .param("icon", json(new byte[0]))
				)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewLabel"+i)))
                .andExpect(jsonPath("$.type", is("User")))
                .andExpect(jsonPath("$.description", is("description"+i)))
                .andReturn();		
		}
		Assert.assertTrue(Lists.newArrayList(labelDao.findAll()).size() == 5);
	}
	
	@Test
	public void FullTest() throws Exception {
		User parentuser = userDao.save(new User("FullLabelTest","Password"));		
		String json;
		Label newLabel;
		
		URL url = new URL("http://www.digitalphotoartistry.com/rose1.jpg");
		BufferedImage  image = ImageIO.read(url); 
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		byte[] imageInByte=baos.toByteArray();
				
		MvcResult result = this.mockMvc.perform(post("/createNewLabel")
				.contentType(MediaType.APPLICATION_JSON)
                .content(json(parentuser))
                .param("name", "NewLabel")
                .param("type", LabelType.User.toString())
                .param("description", "description1")
                .param("icon", json(imageInByte))
				)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewLabel")))
                .andExpect(jsonPath("$.type", is("User")))
                .andExpect(jsonPath("$.description", is("description1")))
                .andReturn();		
		
		json = result.getResponse().getContentAsString();
		newLabel = labelDao.findOne(Long.parseLong(JsonPath.read(json, "$.id").toString()));	
		
		
		url = new URL("http://www.digitalphotoartistry.com/christmasflowerpictures/IMGP1403.jpg");
		image = ImageIO.read(url); 
		baos=new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		imageInByte=baos.toByteArray();
		
		result = this.mockMvc.perform(post("/updateLabel")
				.contentType(MediaType.APPLICATION_JSON)
				.param("id", newLabel.getId().toString())
                .param("name", "UdatedLabel")
                .param("type", LabelType.Favourites.toString())
                .param("description", "description2")
                .param("icon", json(imageInByte))
				)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UdatedLabel")))
                .andExpect(jsonPath("$.type", is("Favourites")))
                .andExpect(jsonPath("$.description", is("description2")))
                .andReturn();	
		
		this.mockMvc.perform(post("/deleteLabel")
				.contentType(MediaType.APPLICATION_JSON)
				.param("id", newLabel.getId().toString())
				)
			.andExpect(status().isOk());
		
		Assert.assertTrue(labelDao.findById(newLabel.getId()) == null);
		
		//System.out.println(result.getResponse().getContentAsString());
		
	}
	
	
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
	
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
