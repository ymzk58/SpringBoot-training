package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Hello5Controller {
  @GetMapping("/hello5")
  public ModelAndView sayHello(@RequestParam("name") String name, ModelAndView mv) {

    mv.setViewName("hello");
    mv.addObject("name", name);
    return mv;
  }
}
