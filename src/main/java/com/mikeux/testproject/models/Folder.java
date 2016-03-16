package com.mikeux.testproject.models;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="folder")
public class Folder implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    @Column(nullable = false)
	private String name;
    
    public String getName() {
        return this.name;
    }
    
    public String setName(String name) {
        return this.name = name;
    }
    
    //@Column(nullable = true)
	//private String parentFolder;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentFolder")
    private Folder parentFolder;
    
    /*
    public Folder getParentFolder() {
    	return this.parentFolder;
    }

    public void setParentFolder(Folder folder) {
    	this.parentFolder = folder;
    }*/
    
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
	private User user;
    
    /*public User getUser() {
    	return this.user;
    }

    public void setUserr(User user) {
    	this.user = user;
    }*/
    
}
