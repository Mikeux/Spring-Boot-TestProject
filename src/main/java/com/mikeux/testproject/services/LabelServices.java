package com.mikeux.testproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mikeux.testproject.Utils;
import com.mikeux.testproject.models.Label;
import com.mikeux.testproject.models.Label.LabelType;
import com.mikeux.testproject.models.LabelDao;
import com.mikeux.testproject.models.Protocol;
import com.mikeux.testproject.models.User;

@RestController
public class LabelServices {
	private static final Logger log = LoggerFactory.getLogger(UserServices.class);
    
	@Autowired
	private LabelDao labelDao;
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @RequestMapping("/createNewLabel")
    public void createNewLabel(@RequestBody User user,
    		@RequestParam(value="name") String name, LabelType type, String description, byte[] icon) {
    	
		Protocol pr = Utils.ProtocolValidator("LabelServices.createNewLabel");   	
			if(pr != null) {
	        try {
	        	Label label = new Label(name, type.toString(), description, icon, user);
	        	labelDao.save(label);  
	        	Utils.ProtocolClose(pr, true, "", null);    
	        }
	        catch (Exception ex) {
	        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
	        }
		}
    	
    }
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @RequestMapping("/updateLabel")
    public void updateLabel(long id, String name, LabelType type, String description, byte[] icon) {
    	
		Protocol pr = Utils.ProtocolValidator("LabelServices.updateLabel");   	
		if(pr != null) {
	        try {
	        	Label label = labelDao.findOne(id);
	        	if(label != null) {
	        		label.setType(type.toString());
	        		label.setDescription(description);
	        		label.setIcon(icon);
	        		labelDao.save(label);  
	        		Utils.ProtocolClose(pr, true, "", null);    
	        	}        	         	
	        }
	        catch (Exception ex) {
	        	Utils.ProtocolClose(pr, false, ex.toString(), ex);;
	        }
		}    	
    }
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @RequestMapping("/deleteLabel")
    public void deleteLabel(long id, String name, LabelType type, String description, byte[] icon) {
    	
		Protocol pr = Utils.ProtocolValidator("LabelServices.deleteLabel");   	
		if(pr != null) {
	        try {
	        	Label label = labelDao.findOne(id);
	        	if(label != null) {
	        		labelDao.delete(label);   
	        	}
	        	Utils.ProtocolClose(pr, true, "", null);    
	        }
	        catch (Exception ex) {
	        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
	        }
		}
    	
    }
	
}
