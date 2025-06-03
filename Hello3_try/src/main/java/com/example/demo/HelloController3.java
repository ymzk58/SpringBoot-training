package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController3 {
	@GetMapping("hello3/{name}")
	public String sayHello(@PathVariable("name")String name) {
		return "こんにちは" + name;
	}
}