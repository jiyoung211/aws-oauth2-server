package com.jiyoung.apigw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(path = "/api")
public class ApiController
{

	@GetMapping
	public String main(Model model)
	{
		System.out.println("################### call ApiController ###################");
		return "pub/api";
	}

	@GetMapping("/test")
	public @ResponseBody String test()
	{
		System.out.println("################### call test ###################");
		return "test";
	}

}
