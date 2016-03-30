package com.mikeux.testproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mikeux.testproject.services.UserServices;

@ControllerAdvice
public class GlobalControllerAdvice {
	private static final Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);
	
  @ModelAttribute
  public void myMethod(Model model) {
	  //log.error(model.toString());
      //Object myValues = // obtain your data from DB here...
	  //model.addAttribute("myDbValues", myValues);
  }
}
