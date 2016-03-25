package com.mikeux.testproject.models;


import java.io.Serializable;
import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "user")
public class User implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    // ... additional members, often include @OneToMany mappings
    public User() { }
    
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

}