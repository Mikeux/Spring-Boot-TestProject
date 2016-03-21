package com.mikeux.testproject.services;

import java.util.List;

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
import com.mikeux.testproject.models.Folder;
import com.mikeux.testproject.models.FolderDao;
import com.mikeux.testproject.models.Protocol;
import com.mikeux.testproject.models.User;
import com.mikeux.testproject.models.UserDao;

@RestController
public class FolderServices {
	private static final Logger log = LoggerFactory.getLogger(UserServices.class);
    
	@Autowired
	private FolderDao folderDao;
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @RequestMapping("/createNewFolder")
    public void createNewFolder(@RequestBody User user,
    		@RequestParam(value="name") String name) {
    	
		Protocol pr = Utils.ProtocolValidator("FolderServices.createNewFolder");   	
		if(pr != null) {
			try {
	        	Folder folder = new Folder(name, user, null);
	        	folderDao.save(folder);   
	        	Utils.ProtocolClose(pr, true, "", null);  
	        }
	        catch (Exception ex) {
	        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
	        }
		}
    }
    
    @RequestMapping("/createNewSubFolder")
    public void createNewSubFolder(@RequestBody Folder parentFolder,
    		@RequestParam(value="name") String name) {
    	
    	Protocol pr = Utils.ProtocolValidator("FolderServices.createNewSubFolder");   	
    	if(pr != null) {
	        try {
	        	Folder folder = new Folder(
	        			name, 
	        			parentFolder.getUser(),
	        			parentFolder);
	        	folderDao.save(folder);     
	        	Utils.ProtocolClose(pr, true, "", null);  
	        }
	        catch (Exception ex) {
	        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
	        }
    	}
    }
    
    @RequestMapping("/deleteFolderRecursively")
    public void deleteFolderRecursively(@RequestParam(value="id") Long id) {
    	
    	Protocol pr = Utils.ProtocolValidator("FolderServices.deleteFolderRecursively");   	
    	if(pr != null) {
	        try {
	        	Folder folder = folderDao.findOne(id);
	        	if(folder != null) {
	        		deleteFolderRecursive(folder);
	        		folderDao.delete(folder); 
	        	}
	        	Utils.ProtocolClose(pr, true, "", null);  
	        }
	        catch (Exception ex) {
	        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
	        }    
    	}
    }
    
    private void deleteFolderRecursive(Folder folder) {
    	List<Folder> list = folderDao.findByParentFolder(folder);
    	for(Folder f : list) {
    		if(f.getParentFolder() != null) {
    			deleteFolderRecursive(f);
    		} else {
    			folderDao.delete(f);
    		}
    	}
    }
    
}



