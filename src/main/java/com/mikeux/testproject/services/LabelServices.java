package com.mikeux.testproject.services;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
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
import com.mikeux.testproject.models.ProtocolDao;
import com.mikeux.testproject.models.User;
import com.mikeux.testproject.models.UserDao;

@RestController
public class LabelServices {
	private static final Logger log = LoggerFactory.getLogger(UserServices.class);
    
	@Autowired
	private LabelDao labelDao;
	
	@Autowired
	private UserDao userDao;
	
	@Transactional(propagation = Propagation.SUPPORTS)
    @RequestMapping("/createNewLabel")
    public Label createNewLabel(@RequestBody User user,
    		@RequestParam(value="name") String name, LabelType type, String description, byte[] icon) {
		
    	Protocol pr = Utils.CreateNewProtocol("LabelServices.createNewLabel",0);
    	try {
    		Label label = new Label(name, type.toString(), description, icon, userDao.findOne(user.getId()));
        	labelDao.save(label);  
        	Utils.ProtocolClose(pr, true, "", null); 
        	return label;
        }
        catch (Exception ex) {
        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
        }    	
    	return null;
    }
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.SUPPORTS)
    @RequestMapping("/updateLabel")
    public Label updateLabel(long id, String name, LabelType type, String description, byte[] icon) {
		Protocol pr = Utils.CreateNewProtocol("LabelServices.updateLabel", id);
		try {
		    boolean locked = Utils.CheckOptimisticLocking("LabelServices.%", id, pr.getId());   	
		    if(locked) {
		    	Utils.ProtocolClose(pr, false, "OptimisticLocking Error", null);
		    } else {
		    	Label label = labelDao.findOne(id);
	        	if(label != null) {
	        		label.setName(name);
	        		label.setType(type.toString());
	        		label.setDescription(description);
	        		label.setIcon(icon);
	        		labelDao.save(label);  
	        		Utils.ProtocolClose(pr, true, "", null); 
	        	} else {
	        		Utils.ProtocolClose(pr, true, "Label Not Exist", null);   
	        	}
	        	return label;
		    }
		}
		catch (Exception ex) {
		    Utils.ProtocolClose(pr, false, ex.toString(), ex);
		}  	
		return null;
    }
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.SUPPORTS)
    @RequestMapping("/deleteLabel")
    public void deleteLabel(long id) {
		Protocol pr = Utils.CreateNewProtocol("LabelServices.deleteLabel", id);
		try {
		    boolean locked = Utils.CheckOptimisticLocking("LabelServices.%", id, pr.getId());   	
		    if(locked) {
		    	Utils.ProtocolClose(pr, false, "OptimisticLocking Error", null);
		    } else {
	        	Label label = labelDao.findOne(id);
	        	if(label != null) {
	        		labelDao.delete(label);   
	        		Utils.ProtocolClose(pr, true, "", null); 
	        	} else {
	        		Utils.ProtocolClose(pr, true, "Label Not Exist", null);   
	        	}         
		    }
		}
		catch (Exception ex) {
			Utils.ProtocolClose(pr, false, ex.toString(), ex);
		}
    	
    }
	
}
