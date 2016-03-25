package com.mikeux.testproject;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mikeux.testproject.models.Entry;
import com.mikeux.testproject.models.EntryDao;
import com.mikeux.testproject.models.Folder;
import com.mikeux.testproject.models.FolderDao;
import com.mikeux.testproject.models.Label;
import com.mikeux.testproject.models.Label.LabelType;
import com.mikeux.testproject.models.LabelDao;
import com.mikeux.testproject.models.User;
import com.mikeux.testproject.models.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestProjectApplication.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class EntryServicesTest {
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
	private FolderDao folderDao;
	
	@Autowired
	private LabelDao labelDao;
	
	@Autowired
	private EntryDao entryDao;
	
	@Before
    public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Rule
	public OutputCapture capture = new OutputCapture();
	
	
	@Test
	public void FullTest() throws Exception {
		labelDao.deleteAll();
		User parentuser = userDao.save(new User("createEntryTest","Password"));				
		String json;
		Folder parentFolder;
		Entry newEntry;
		List<Long> labelIds = new LinkedList<Long>();
		
		MvcResult result = this.mockMvc.perform(post("/createNewFolder")
				.contentType(MediaType.APPLICATION_JSON)
                .content(json(parentuser))
                .param("name", "ParentFolder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("ParentFolder")))
                .andReturn();
		
		json = result.getResponse().getContentAsString();
		parentFolder = folderDao.findOne(Long.parseLong(JsonPath.read(json, "$.id").toString()));	
		
		result = this.mockMvc.perform(post("/createNewEntry")
				.contentType(MediaType.APPLICATION_JSON)
                .content(json(parentFolder))
                .param("name", "NewEntry")
				)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewEntry")))
                .andReturn();	
		json = result.getResponse().getContentAsString();
		newEntry = entryDao.findOne(Long.parseLong(JsonPath.read(json, "$.id").toString()));	
		
		for(int i=0; i<5; i++) {
			result = this.mockMvc.perform(post("/createNewLabel")
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
			json = result.getResponse().getContentAsString();
			labelIds.add(Long.parseLong(JsonPath.read(json, "$.id").toString()));	
		}
		
		this.mockMvc.perform(post("/attach")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(labelIds))
				.param("entryId", newEntry.getId().toString())
				)
                .andExpect(status().isOk())
                .andReturn();		
	}
	
	
	
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
