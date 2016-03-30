package com.mikeux.testproject;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
@EnableAsync
public class TestProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestProjectApplication.class, args);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.BAD_REQUEST.value(), "Please try again and with a non empty string as 'name'");
	}
	
	@ExceptionHandler(Exception.class)
	public String handleError(HttpServletRequest req, Exception exception) {
		//logger.error("Request: " + req.getRequestURL() + " raised " + exception);
		return "Request: " + req.getRequestURL() + " raised " + exception;
	}
	
}
