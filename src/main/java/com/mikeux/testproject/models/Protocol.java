package com.mikeux.testproject.models;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name="protocol")
public class Protocol {
		
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	public Long getId() {
		return id;
	}
	
	@Column(length = 255)
	private String methodName;
	public String getMethodName() {
		return methodName;
	}	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	@Column
    private Long fkId;
	public Long getFkId() {
		return fkId;
	}
	public void setFkId(Long id){
		this.fkId = id;
	}
	
	@Column
	//@Temporal(TemporalType.TIMESTAMP)
	private Timestamp executionTime;
	public Timestamp getExecutionTime() {
	    return this.executionTime;
	}
	public void setExecutionTime(Timestamp executionTime) {
		this.executionTime = executionTime;
	}
	
	@Column
	private boolean successful ;
	public boolean getSuccessful() {
	    return this.successful;
	}	
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	
	@Column(length = 255)
	private String errorMessage;
	public String getErrorMessage() {
	    return this.errorMessage;
	}	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Lob
	@Column
	private String stacktrace;
	public String getStacktrace() {
	    return this.stacktrace;
	}
	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}
}

