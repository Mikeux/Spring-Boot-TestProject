package com.mikeux.testproject.models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Entity
@Table(name="entry")
public class Entry {
	
	
	@PreUpdate
	@PrePersist
	public void updateTimeStamps() {
	    updateTime = new Timestamp(new Date().getTime());
	}
	
	public void addLabel(Label label) {
		if(!this.entyLabels.contains(label)) {
			this.entyLabels.add(label);
		}
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    @Column(nullable = false)
	private String name;
    
	@Column(name="updateTime", nullable = false,
    columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updateTime;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "folder_id")
	private Folder folder;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public Set<Label> getEntyLabels() {
		return entyLabels;
	}

	public void setEntyLabels(Set<Label> entyLabels) {
		this.entyLabels = entyLabels;
	}

	public Long getId() {
		return id;
	}

	public Entry(String name, Folder folder) {
		super();
		this.name = name;
		this.folder = folder;
	}

	public Entry() {
		super();
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="entylabels", 
    	joinColumns={@JoinColumn(name="entry_id")}, 
    	inverseJoinColumns={@JoinColumn(name="label_id")})
    private Set<Label> entyLabels = new HashSet<Label>();
    
    
}
