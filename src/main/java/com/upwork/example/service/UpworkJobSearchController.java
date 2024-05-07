package com.upwork.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.upwork.example.model.UpworkJobSearchData;
import com.upwork.example.scheduler.UpworkDataPollingService;

@RestController
public class UpworkJobSearchController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UpworkDataPollingService pollingService;
	
	@RequestMapping(value = "/upworksearchquery", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public boolean getdata(@RequestBody UpworkJobSearchData query) {
		pollingService.updataobj = query;
		return true;
	}

}
