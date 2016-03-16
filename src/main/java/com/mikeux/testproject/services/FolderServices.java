package com.mikeux.testproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mikeux.testproject.models.Folder;
import com.mikeux.testproject.models.User;

public class FolderServices {
	private static final Logger log = LoggerFactory.getLogger(UserServices.class);
    
    @RequestMapping("/createNewFolder")
    public void createNewFolder(@RequestBody User user,
    		@RequestParam(value="name") String name) {
    	
    }
    
    @RequestMapping("/createNewSubFolder")
    public void createNewSubFolder(@RequestBody Folder parentFolder,
    		@RequestParam(value="name") String name) {
    	
    }
    
    @RequestMapping("/deleteFolderRecursively")
    public void deleteFolderRecursively(@RequestParam(value="id") Long id) {
    	
    }
}
