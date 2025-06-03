package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello3Controller {
  @GetMapping("/hello3/{name}")
  public String sayHello(@PathVariable("name") String name) {
    return "Hello, world! " + "こんにちは " + name + "さん! ";
  }
}
