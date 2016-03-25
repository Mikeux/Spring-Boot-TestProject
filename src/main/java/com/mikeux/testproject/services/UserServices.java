package com.mikeux.testproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
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
    
	@Autowired
	private UserDao userDao;
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@RequestMapping("/createNewUser")
    public void createNewUser(@RequestParam(value="name") String name, @RequestParam(value="password") String password) {
    	Protocol pr = Utils.CreateNewProtocol("UserServices.createNewUser",0);
    	try {
            User user = new User(name, password);
            userDao.save(user);
            Utils.ProtocolClose(pr, true, "", null);             
        }
        catch (Exception ex) {      
        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
        }  
    }
}
