package com.jiyoung.apigw.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorContoller implements ErrorController
{
	private final String PATH = "/error";

	@RequestMapping(value = PATH)
	public String error(HttpServletRequest request, Model model)
	{
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String statusCode = String.valueOf(status);

		HttpStatus httpStatus = HttpStatus.resolve(Integer.parseInt(statusCode));
		if (httpStatus == HttpStatus.UNAUTHORIZED)
		{
			return "error/401";
		} else if (httpStatus == HttpStatus.NOT_FOUND)
		{
			return "error/404";
		} else if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR)
		{
			return "error/500";
		}

		model.addAttribute("httpStatus", httpStatus.value());
		model.addAttribute("reasonPhrase", httpStatus.getReasonPhrase());

		return "error/other";
	}

	@Override
	public String getErrorPath()
	{
		return PATH;
	}

}
