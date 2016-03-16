package com.mikeux.testproject.models;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name="protocol")
public class Protocol {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(length = 255)
	private String methodName;

	public String getmethodName() {
	    return this.methodName;
	}
	
	@Column
	//@Temporal(TemporalType.TIMESTAMP)
	private Date executionTime;
	
	@Column
	private boolean successful ;
	
	@Column(length = 255)
	private String errorMessage;
	
	@Lob
	@Column
	private String stacktrace;
	

}

