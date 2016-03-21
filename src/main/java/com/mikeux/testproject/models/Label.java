package com.mikeux.testproject.models;

import java.sql.Clob;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="label")
public class Label {	

	public static enum LabelType {
        Default,
        Type1, 
        Type2
    }
	
	public Label(String name, String type, String description, byte[] icon, User user) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.icon = icon;
		this.user = user;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
	private String name;
    
    @Column(length = 50)
    private String type;
    
    @Lob
    @Type(type="text")
    @Column
    private String description;
    //String _description = clob.getSubString(0, clob.length());
    
    @Type(type="blob")
    @Column
    private byte[] icon;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
	private User user;
    
   
    @ManyToMany(mappedBy = "entyLabels")
    private Set<Entry> users = new HashSet<Entry>();

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}
}
