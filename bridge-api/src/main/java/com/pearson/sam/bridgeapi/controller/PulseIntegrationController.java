package com.pearson.sam.bridgeapi.controller;

import com.pearson.sam.bridgeapi.util.CourseSectionUtil;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pulse")
public class PulseIntegrationController {

	@RequestMapping(value = "/courseList", method = RequestMethod.GET)
	public void findTitles(HttpServletResponse response) {
		String assetDetail = CourseSectionUtil.getAssetDetailsForCourse("5ba232b5ab78ee3c0080cb07", "teststudentbh54");
		response.setContentType("application/json");
		addHeaders(response);
		try {
			response.getWriter().print(assetDetail);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 /**
	   * addHeaders.
	   * 
	   */
	  private void addHeaders(HttpServletResponse res) {
	    res.setHeader("Access-Control-Allow-Origin", "*");
	    res.setHeader("Access-Control-Allow-Credentials", "true");
	    res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    res.setHeader("Access-Control-Max-Age", "3600");
	    res.setHeader("Access-Control-Allow-Headers",
	        "Content-Type, Accept, X-Requested-With, remember-me, authorization");
	  }
	
}
