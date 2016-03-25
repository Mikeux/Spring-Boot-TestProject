package com.mikeux.testproject.services;

import java.util.Arrays;
import java.util.LinkedList;
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
import com.mikeux.testproject.models.Folder;
import com.mikeux.testproject.models.FolderDao;
import com.mikeux.testproject.models.Label;
import com.mikeux.testproject.models.Protocol;
import com.mikeux.testproject.models.ProtocolDao;
import com.mikeux.testproject.models.User;
import com.mikeux.testproject.models.UserDao;

@RestController
public class FolderServices {
	private static final Logger log = LoggerFactory.getLogger(UserServices.class);
    
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FolderDao folderDao;
		
	@Transactional(isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.SUPPORTS)
    @RequestMapping("/createNewFolder")
    public Folder createNewFolder(@RequestBody User user, @RequestParam(value="name") String name) {
		
    	Protocol pr = Utils.CreateNewProtocol("FolderServices.createNewFolder", 0);
    	try {
    		/*List<Protocol> protList = Utils.protocolDao.findByMethodNameNullErrorMessage("FolderServices.createNewFolder");
    		if(protList.size() > 0){
    			throw new Exception("+++++++++");
    		} */   		
    		//Thread.sleep(5000);    		
        	Folder folder = new Folder(name, userDao.findOne(user.getId()), null);
        	folderDao.save(folder);   
        	Utils.ProtocolClose(pr, true, "", null);        
        	return folder;
        }
        catch (Exception ex) {
        	Utils.ProtocolClose(pr, false, ex.toString(), ex);
        }
    	return null;
    }
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.SUPPORTS)
    @RequestMapping("/createNewSubFolder")
    public Folder createNewSubFolder(@RequestBody Folder parentFolder,
    		@RequestParam(value="name") String name) {    	
    	
    	Protocol pr = Utils.CreateNewProtocol("FolderServices.createNewSubFolder", parentFolder.getId());
		try {
			boolean locked = false;
			List<Protocol> protList = Utils.protocolDao.findByMethodNameNullErrorMessage("FolderServices.deleteFolderRecursively");
			if(protList.size() > 0){
				List<Long> deletingFolders = this.getSubFoldersRecursive(protList.get(0).getFkId());
				if(deletingFolders.contains(parentFolder.getId())) {
					locked = true;
				}				
			}	
			if(locked) {
		    	Utils.ProtocolClose(pr, false, "OptimisticLocking Error", null);
		    } else {
		    	Folder _parentFolder = folderDao.findOne(parentFolder.getId());
	        	Folder folder = new Folder(
	        			name, 
	        			_parentFolder.getUser(),
	        			_parentFolder);
	        	folderDao.save(folder);     
	        	Utils.ProtocolClose(pr, true, "", null);  
	        	return folder;
		    }
		}
		catch (Exception ex) {
		    Utils.ProtocolClose(pr, false, ex.toString(), ex);
		}
		return null;
    }
    
	@Transactional(isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.SUPPORTS)
    @RequestMapping("/deleteFolderRecursively")
    public void deleteFolderRecursively(@RequestParam(value="id") Long id) {   
    	
    	Protocol pr = Utils.CreateNewProtocol("FolderServices.deleteFolderRecursively", id);
		try {
			if(folderDao.findOne(id) == null) {
				Utils.ProtocolClose(pr, false, "Not found any Folder", null);
			} else {
				//List<Long> subFolders = this.getSubFoldersRecursive(folderDao.findOne(id));
				List<Long> subFolders = this.getSubFoldersRecursive(id);
				
				boolean locked = false;
				List<Protocol> protList = Utils.protocolDao.findByMethodNameNullErrorMessage("FolderServices.createNewSubFolder");
				for(Protocol prot : protList) { 
					if(subFolders.contains(prot.getFkId())) { 
						locked = true;
						break;
					}
				}
				
				if(!locked) {
					locked = Utils.CheckOptimisticLocking("EntryServices.%", subFolders, pr.getId());
				}
	
			    if(locked) {
			    	Utils.ProtocolClose(pr, false, "OptimisticLocking Error", null);
			    } else {
			    	folderDao.deleteByIdIn(subFolders);
	    			Utils.ProtocolClose(pr, true, "", null);  
			    }
			}
		}
		catch (Exception ex) {
		    Utils.ProtocolClose(pr, false, ex.toString(), ex);
		}
    }
    
    private List<Long> getSubFoldersRecursive(Long folderId) {
    	List<Long> results = new LinkedList<Long>();
    	results.add(folderId);
    	List<Folder> subFolderList = folderDao.findByParentFolderId(folderId);
    	for(Folder f : subFolderList) {
    		results.addAll(getSubFoldersRecursive(f.getId()));
    	}
    	return results;
    }    
    
    private List<Long> getSubFoldersRecursive(Folder folder) {
    	List<Long> results = new LinkedList();
    	results.add(folder.getId());
    	List<Folder> subFolderList = folderDao.findByParentFolder(folder);
    	for(Folder f : subFolderList) {
    		results.addAll(getSubFoldersRecursive(f));
    	}
    	return results;
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



