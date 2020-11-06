package com.yxt.crud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 13:07
 */
@RestController("helloController")
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "Hello, Web World";
	}
}
