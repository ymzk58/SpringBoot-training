package com.example.todolist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
  private final TodoRepository todoRepository;
  private final TodoService todoService;
  private final HttpSession session;

  // ToDo一覧を表示する
  @GetMapping("/todo")
  public ModelAndView showTodoList(ModelAndView mv) {
    mv.setViewName("todoList");
    List<Todo> todoList = todoRepository.findAll();
    mv.addObject("todoList", todoList);
    mv.addObject("todoQuery", new TodoQuery());
    return mv;
  }

  // ToDo表示
  @GetMapping("/todo/{id}")
  public ModelAndView todoById(@PathVariable(name = "id") int id, ModelAndView mv) {
    mv.setViewName("todoForm");
    Todo todo = todoRepository.findById(id).get();
    mv.addObject("todoData", todo);
    session.setAttribute("mode", "update");
    return mv;
  }

  // ToDo入力フォーム表示
  @GetMapping("/todo/create/form")
  public ModelAndView createTodo(ModelAndView mv) {
    mv.setViewName("todoForm");
    mv.addObject("todoData", new TodoData());
    session.setAttribute("mode", "create"); 
    return mv;
  }

  // ToDo追加処理
  @PostMapping("/todo/create/do")
  public String createTodo(@ModelAttribute @Validated TodoData todoData, 
                           BindingResult result,
                           Model model) {
    // エラーチェック
    boolean isValid = todoService.isValid(todoData, result);
    if (!result.hasErrors() && isValid) {
      // エラーなし
      Todo todo = todoData.toEntity();
      todoRepository.saveAndFlush(todo);
      return "redirect:/todo";

    } else {
      // エラーあり
      // model.addAttribute("todoData", todoData);
      return "todoForm";
    }
  }

  // ToDo更新処理
  @PostMapping("/todo/update")
  public String updateTodo(@ModelAttribute @Validated TodoData todoData, 
                           BindingResult result,
                           Model model) {
    // エラーチェック
    boolean isValid = todoService.isValid(todoData, result);
    if (!result.hasErrors() && isValid) {
      // エラーなし
      Todo todo = todoData.toEntity();
      todoRepository.saveAndFlush(todo);
      return "redirect:/todo";

    } else {
      // エラーあり
      // model.addAttribute("todoData", todoData);
      return "todoForm";
    }
  }

  // ToDo削除処理
  @PostMapping("/todo/delete")
  public String deleteTodo(@ModelAttribute TodoData todoData) {
    todoRepository.deleteById(todoData.getId());
    return "redirect:/todo";
  }

  // ToDo検索処理
  @PostMapping("/todo/query")
  public ModelAndView queryTodo(@ModelAttribute TodoQuery todoQuery, 
                                BindingResult result,
                                ModelAndView mv) {
    mv.setViewName("todoList");

    List<Todo> todoList = null;
    if (todoService.isValid(todoQuery, result)) {
      // エラーがなければ検索
      todoList = todoService.doQuery(todoQuery);
    }
    // mv.addObject("todoQuery", todoQuery);
    mv.addObject("todoList", todoList);

    return mv;
  }

  // ToDo一覧へ戻る
  @PostMapping("/todo/cancel")
  public String cancel() {
    return "redirect:/todo";
  }
}
