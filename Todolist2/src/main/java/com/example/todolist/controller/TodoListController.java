package com.example.todolist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
  private final TodoRepository todoRepository;
  private final TodoService todoService;

  // ToDo一覧を表示する
  @GetMapping("/todo")
  public ModelAndView showTodoList(ModelAndView mv) {
    mv.setViewName("todoList");
    List<Todo> todoList = todoRepository.findAll();
    mv.addObject("todoList", todoList);
    return mv;
  }

  // ToDo入力フォーム表示
  @GetMapping("/todo/create")
  public ModelAndView createTodo(ModelAndView mv) {
    mv.setViewName("todoForm");
    mv.addObject("todoData", new TodoData());	//空のインスタンスを渡す
    return mv;
  }

  // ToDo追加処理
  @PostMapping("/todo/create")
  public ModelAndView createTodo(@ModelAttribute @Validated TodoData todoData,
                                 BindingResult result, ModelAndView mv) {
    // エラーチェック
    boolean isValid = todoService.isValid(todoData, result);
    if (!result.hasErrors() && isValid) {
      // エラーなし
      Todo todo = todoData.toEntity();
      todoRepository.saveAndFlush(todo);
      return showTodoList(mv);

    } else {
      // エラーあり
      mv.setViewName("todoForm");
      // mv.addObject("todoData", todoData);
      return mv;
    }
  }

  // ToDo一覧へ戻る
  @PostMapping("/todo/cancel")
  public String cancel() {
    return "redirect:/todo";
  }
}
