package com.example.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController4 {
	@PostMapping("/hello4")
	public String sayHello(@RequestParam("name")String name) {
		return "こんちゃ！" + name;
	}
}
