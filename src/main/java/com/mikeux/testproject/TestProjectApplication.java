package com.mikeux.testproject;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
@EnableAsync
public class TestProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestProjectApplication.class, args);
	}

	//@ExceptionHandler(IllegalArgumentException.class)
	/*@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.BAD_REQUEST.value(), "Please try again and with a non empty string as 'name'");
	}*/
	
	@ExceptionHandler(Exception.class)
	//@ExceptionHandler
	public String handleError(HttpServletRequest req, Exception exception) {
		//logger.error("Request: " + req.getRequestURL() + " raised " + exception);
		return "Request: " + req.getRequestURL() + " raised " + exception;
	}
	
	/*@Bean
	public SimpleUrlHandlerMapping sampleServletMapping() {
	    SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
	    mapping.setOrder(Integer.MAX_VALUE - 2);

	    Properties urlProperties = new Properties();
	    urlProperties.put("/index", "myController");
	    mapping.setMappings(urlProperties);

	    return mapping;
	}*/
	
}
