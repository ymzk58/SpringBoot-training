package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameController {

  @Autowired
  HttpSession session;

  @GetMapping("/")
  public String index() {
    session.invalidate();
    // 答えを作ってSessionへ格納
    Random rnd = new Random();
    int answer = rnd.nextInt(100) + 1;
    session.setAttribute("answer", answer);
    System.out.println("answer=" + answer);// コンソールへ正解を出力する
    return "game";
  }

  @PostMapping("/challenge")
  public ModelAndView challenge(@RequestParam("number") int number, ModelAndView mv) { 
    // セッションから答えを取得
    int answer = (Integer)session.getAttribute("answer");

    // ユーザーの回答履歴を取得
    @SuppressWarnings("unchecked")
    List<History> histories = (List<History>)session.getAttribute("histories");
    if (histories == null) {
      histories = new ArrayList<>();
      session.setAttribute("histories", histories);
    }
    // 判定→回答履歴追加
    if (answer < number) {
      histories.add(new History(histories.size() + 1, number, "もっと小さいです"));

    } else if (answer == number) {
      histories.add(new History(histories.size() + 1, number, "正解です！"));

    } else {
      histories.add(new History(histories.size() + 1, number, "もっと大きいです"));
    }

    mv.setViewName("game");
    mv.addObject("histories", histories);
    return mv;
  }
}