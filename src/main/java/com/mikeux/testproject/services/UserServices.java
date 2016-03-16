package com.mikeux.testproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mikeux.testproject.models.User;
import com.mikeux.testproject.models.UserDao;
import com.mikeux.testproject.models.UserDao2;

@RestController
public class UserServices {
	private static final Logger log = LoggerFactory.getLogger(UserServices.class);
	
	/*private UserDao userDao;

    @Autowired
    public UserServices(UserDao userDao)
    {
        this.userDao = userDao;
    }*/
    
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserDao2 userDao2;

	//@Autowired
    //protected JdbcTemplate jdbc;
    
    @RequestMapping("/createNewUser")
    public void createNewUser(@RequestParam(value="name") String name, @RequestParam(value="password") String password) {
    	
    	String userId = "";
        try {
          User user = new User(name, password);
          userDao.save(user);
          //userId = String.valueOf(user.getId());
        }
        catch (Exception ex) {
          //return "Error creating the user: " + ex.toString();
        }
    	
    }
	
    @RequestMapping("/createNewUser2")
    public void createNewUser2(@RequestParam(value="name") String name, @RequestParam(value="password") String password) {
    	
    	String userId = "";
        try {
          User user = new User(name, password);
          userDao2.create(user);
          //userDao.save(user);
          //userId = String.valueOf(user.getId());
        }
        catch (Exception ex) {
          //return "Error creating the user: " + ex.toString();
        }
    	
    }
}
