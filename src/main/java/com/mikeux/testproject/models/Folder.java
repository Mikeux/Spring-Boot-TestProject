package com.mikeux.testproject.models;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="folder")
public class Folder implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    public Folder(String name, User user, Folder parentFolder) {
    	this.name = name;
    	this.user = user;
    	this.parentFolder = parentFolder;
    }    
    
    @Column(nullable = false)
	private String name;
    
    public String getName() {
        return this.name;
    }
    
    public String setName(String name) {
        return this.name = name;
    }
        
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentFolder")
    private Folder parentFolder;
    
    
    public Folder getParentFolder() {
    	return this.parentFolder;
    }

    public void setParentFolder(Folder folder) {
    	this.parentFolder = folder;
    }
    
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
	private User user;
    
    public User getUser() {
    	return this.user;
    }

    public void setUser(User user) {
    	this.user = user;
    }
    
}
