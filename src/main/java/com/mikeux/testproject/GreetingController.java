package com.mikeux.testproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.log4j.Logger;
//import org.apache.log4j.LogManager;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RestController
public class GreetingController {

	private static final Logger log = LoggerFactory.getLogger(GreetingController.class);
	//private static Logger log = Logger.getLogger(GreetingController.class.getName());
	
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    protected JdbcTemplate jdbc;
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	log.info("Jeeee! "+ counter);
    	
        String sql = "INSERT into user(name, password) VALUES (?, ?)";
        //jdbc.update( sql, new Object[] { name, "motnaf87"});
    	
    	//SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    	//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbc.update( sql, new Object[] { name, "motnaf87"});
    	
    	
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}