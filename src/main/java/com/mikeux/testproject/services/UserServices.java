package com.mikeux.testproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mikeux.testproject.Utils;
import com.mikeux.testproject.models.Protocol;
import com.mikeux.testproject.models.User;
import com.mikeux.testproject.models.UserDao;

@RestController
public class UserServices {
	private static final Logger log = LoggerFactory.getLogger(UserServices.class);
	
	/*@ExceptionHandler
	public String allInComingServices() {
		return "valami";
	}  	
	
	@RequestMapping
	public String forwardRequest(final HttpServletRequest request) {
		log.error("forwardRequest");
	    return "forward:/legacy" + request.getRequestURI();
	}*/
		
	 /*private RequestMappingHandlerMapping handlerMapping;
	 @Autowired
	 public void EndpointDocController(RequestMappingHandlerMapping handlerMapping) {
		 this.handlerMapping = handlerMapping;
	 }*/
	 
	 /*private UserDao userDao;

    @Autowired
    public UserServices(UserDao userDao)
    {
        this.userDao = userDao;
    }*/
    
	@Autowired
	private UserDao userDao;
	
	//@Autowired
    //protected JdbcTemplate jdbc;
    	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	@RequestMapping("/createNewUser")
    public void createNewUser(@RequestParam(value="name") String name, @RequestParam(value="password") String password) {
    	Protocol pr = Utils.ProtocolValidator("UserServices.createNewUser");   	
    	if(pr != null) {
	        try {
	        	//Thread.sleep(5000);
	            User user = new User(name, password);
	            userDao.save(user);
	            Utils.ProtocolClose(pr, true, "", null);       
	        }
	        catch (Exception ex) {
	        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
	        }        
    	}
    }
}
