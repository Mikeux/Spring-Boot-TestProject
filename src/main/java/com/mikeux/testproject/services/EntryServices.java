package com.mikeux.testproject.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
import com.mikeux.testproject.models.Entry;
import com.mikeux.testproject.models.EntryDao;
import com.mikeux.testproject.models.Folder;
import com.mikeux.testproject.models.FolderDao;
import com.mikeux.testproject.models.Label;
import com.mikeux.testproject.models.LabelDao;
import com.mikeux.testproject.models.Protocol;

@RestController
public class EntryServices {
private static final Logger log = LoggerFactory.getLogger(UserServices.class);
    
	@Autowired
	private EntryDao entryDao;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private LabelDao labelDao;
	
	@Transactional(propagation = Propagation.SUPPORTS)
    @RequestMapping("/createNewEntry")
    public Entry createNewEntry(@RequestBody Folder folder, String name) {
    	Protocol pr = Utils.CreateNewProtocol("EntryServices.createNewEntry",0);
    	try {
        	Entry entry = new Entry(name, folderDao.findOne(folder.getId()));
        	entryDao.save(entry);        
        	Utils.ProtocolClose(pr, true, "", null);   
        	return entry;
        }
        catch (Exception ex) {
        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
        }
    	return null;    	
    }
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.SUPPORTS)
    @RequestMapping("/attach")
    public void attach(long entryId, @RequestBody Set<Long> labelIds) {
		Protocol pr = Utils.CreateNewProtocol("EntryServices.attach", entryId);
		try {
		    boolean locked = Utils.CheckOptimisticLocking("EntryServices.%", entryId, pr.getId());   	
		    if(locked) {
		    	Utils.ProtocolClose(pr, false, "OptimisticLocking Error", null);
		    } else {
		    	Entry entry = entryDao.findOne(entryId);
	        	if(entry != null) {
	        		Iterable<Label> labels = labelDao.findAll(labelIds);
	        		for (Label label : labels) {
	        			entry.addLabel(label);
	        		}
	        		entryDao.save(entry);
	        		Utils.ProtocolClose(pr, true, "", null); 
	        	} else {
	        		Utils.ProtocolClose(pr, true, "Entry Not Exist", null);   
	        	}       	    	
		    }
		}
		catch (Exception ex) {
		    Utils.ProtocolClose(pr, false, ex.toString(), ex);
		}     	
    }
	
}
