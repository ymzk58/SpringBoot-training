package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class GameController {
	@Autowired
	HttpSession session;
	
	@GetMapping("/")
	public String initialize() {
		session.invalidate();
		Random rnd = new Random();
		int answer = rnd.nextInt(100)+1;
		session.setAttribute("answer", answer);
		System.out.println(answer);
		return "game";
	}
	
	@PostMapping("/challenge")
	public ModelAndView challenge(@RequestParam("number")int number, ModelAndView mv) {
		//セッションから答えを取得
		int answer = (Integer)session.getAttribute("answer");
		
		//ユーザーの回答履歴を取得
		@SuppressWarnings("unchecked")
		List<History> histories = (List<History>)session.getAttribute("histories");
		if(histories==null) {
			histories = new ArrayList<>();
			session.setAttribute("histories", histories);
		}
		
		if(number > answer) {
			histories.add(new History(histories.size(), number, "もっと小さいです"));
		} else if(number == answer) {
			histories.add(new History(histories.size(), number, "正解です！"));
		} else {
			histories.add(new History(histories.size(), number, "もっと大きいです"));
		}
		mv.setViewName("game");
		mv.addObject("histories", histories);
		return mv;
	}
}