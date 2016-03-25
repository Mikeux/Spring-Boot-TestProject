package com.mikeux.testproject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.mikeux.testproject.models.Protocol;
import com.mikeux.testproject.models.ProtocolDao;
import com.mikeux.testproject.services.UserServices;

@Component
public class Utils {
	private static final Logger log = LoggerFactory.getLogger(UserServices.class);
	
	public static ProtocolDao protocolDao;
	
	@Autowired
	private ProtocolDao dao0;
	
	@PostConstruct     
	public void initStaticDao () {
		protocolDao = this.dao0;
	}
		
	//@Autowired
	//private ProtocolDao protocolDao;
	
	@Transactional
	public static Protocol CreateNewProtocol(String methodName, long Id) {
		Protocol newProt = null;
		newProt = new Protocol();
		newProt.setMethodName(methodName);
		newProt.setExecutionTime(new Timestamp(new java.util.Date().getTime()));	
		newProt.setFkId(Id);
		protocolDao.save(newProt);
		return newProt;
	}
	
	public static boolean CheckOptimisticLocking(String methodExpression, long Id, Long prId) {
		List<Long> list = new LinkedList();
		list.add(Id);
		return 	CheckOptimisticLocking(methodExpression, list, prId);
	}
	
	public static boolean CheckOptimisticLocking(String methodExpression, List<Long> CheckIds, Long prId) {
		List<Protocol> protList = protocolDao.findByMethodNameNullErrorMessageAndById(methodExpression, CheckIds, prId);
		return protList.size() > 0;
	}
			
	@Transactional
	public static void ProtocolClose(Protocol protocol, boolean successFul, String errorMessage, Throwable throwable) {
		if(errorMessage.length() > 254) errorMessage = errorMessage.substring(0,254);
		
		protocol.setSuccessful(successFul);
		protocol.setErrorMessage(errorMessage);
		/*if(!successFul) {
			String Stack = "";
			StackTraceElement[] stes = Thread.currentThread().getStackTrace();
	        for (StackTraceElement element : stes) {
	        	Stack += element+"\r\n";
	        }	        
			protocol.setStacktrace(Stack);
		}	*/
		if(throwable != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			throwable.printStackTrace(pw);
			protocol.setStacktrace(sw.getBuffer().toString());
		} else {
			protocol.setStacktrace("");
		}
		protocolDao.save(protocol);		
	}
	//http://programmers.stackexchange.com/questions/260877/issues-about-static-injection-in-spring
		
}
