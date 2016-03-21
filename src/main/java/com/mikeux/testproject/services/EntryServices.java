package com.mikeux.testproject.services;

import java.util.ArrayList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mikeux.testproject.Utils;
import com.mikeux.testproject.models.Entry;
import com.mikeux.testproject.models.EntryDao;
import com.mikeux.testproject.models.Folder;
import com.mikeux.testproject.models.Label;
import com.mikeux.testproject.models.LabelDao;
import com.mikeux.testproject.models.Protocol;

@RestController
public class EntryServices {
private static final Logger log = LoggerFactory.getLogger(UserServices.class);
    
	@Autowired
	private EntryDao entryDao;
	
	@Autowired
	private LabelDao labelDao;
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @RequestMapping("/createNewEntry")
    public void createNewEntry(@RequestBody Folder folder, String name) {
    	
		Protocol pr = Utils.ProtocolValidator("EntryServices.createNewEntry");   	
		if(pr != null) {
	        try {
	        	Entry entry = new Entry(name, folder);
	        	entryDao.save(entry);        
	        	Utils.ProtocolClose(pr, true, "", null); 
	        }
	        catch (Exception ex) {
	        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
	        }
		}
    	
    }
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @RequestMapping("/attach")
    public void attach(long entryId, Set<Long> labelIds) {
    	
		Protocol pr = Utils.ProtocolValidator("EntryServices.attach");   	
		if(pr != null) {
	        try {
	        	Entry entry = entryDao.findOne(entryId);
	        	if(entry != null) {
	        		Iterable<Label> labels = labelDao.findAll(labelIds);
	        		for (Label label : labels) {
	        			entry.addLabel(label);
	        		}
	        		entryDao.save(entry);    
	        	}        	    	
        		Utils.ProtocolClose(pr, true, "", null); 
	        }
	        catch (Exception ex) {
	        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
	        }
		}
    	
    }
	
}
