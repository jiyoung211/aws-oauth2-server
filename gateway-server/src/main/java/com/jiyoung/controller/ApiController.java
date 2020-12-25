package com.jiyoung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(path = "/api")
public class ApiController
{

	@GetMapping
	public String main(Model model)
	{
		return "pub/api";
	}

}
