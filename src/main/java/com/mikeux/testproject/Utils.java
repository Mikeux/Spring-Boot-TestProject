package com.mikeux.testproject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
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
	
	private static ProtocolDao protocolDao;
	
	@Autowired
	private ProtocolDao dao0;
	
	@PostConstruct     
	public void initStaticDao () {
		protocolDao = this.dao0;
	}
		
	//@Autowired
	//private ProtocolDao protocolDao;
	
	//@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public static Protocol ProtocolValidator(String methodName) {	
		log.info("Protocol - "+methodName);
		String ClassName = "";
		if(methodName.indexOf(".") > -1) {
			ClassName = methodName.split("\\.")[0];
		} else {
			ClassName = methodName;
		}

		//List<Protocol> protList = protocolDao.findByMethodNameNullErrorMessage(methodName);
		List<Protocol> protList = protocolDao.findByMethodNameNullErrorMessage(ClassName+".%");
		Protocol newProt = null;
		//Protocol lastProt = null;
		newProt = new Protocol();
		newProt.setMethodName(methodName);
		newProt.setExecutionTime(new Timestamp(new java.util.Date().getTime()));	
		protocolDao.save(newProt);
		
		//log.error("newProt = "+(newProt == null ? "null" : newProt.getMethodName()));
			
		log.error("List Size:"+protList.size());
		/*for(Protocol prot : protList){
			log.error(prot.getExecutionTime()+"");
		}*/
		if(protList.size() > 0) {
			Utils.ProtocolClose(newProt, false, "Optimistic Locking Exception", null);
			newProt = null;
		}
				
		/*if(protList.size() == 0) {
			//ret = false;			
			protocolDao.save(newProt);
		} else {
			log.error("Utolso ("+methodName+"): "+protList.get(0).getExecutionTime().toString());
			Utils.ProtocolClose(newProt, false, "Optimistic Locking Exception", null);
			newProt = null;
		}*/
		return newProt;
	}
	
	@Transactional
	public static void ProtocolClose(Protocol protocol, boolean successFul, String errorMessage, Throwable throwable) {
		
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
