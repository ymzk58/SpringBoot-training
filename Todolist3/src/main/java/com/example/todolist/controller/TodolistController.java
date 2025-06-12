package com.example.todolist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.entity.TodoEntity;
import com.example.todolist.form.TodoForm;
import com.example.todolist.repository.TodoRepo;
import com.example.todolist.service.TodoService;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodolistController {
	private final TodoRepo todoRepo;
	private final TodoService todoService;
	private final HttpSession session;
	
	//ToDo一覧を表示
	@GetMapping("/todo")
	public ModelAndView showTodolist(ModelAndView mv) {
		List<TodoEntity> todoList = todoRepo.findAll();
		mv.setViewName("todoListH");
		mv.addObject("todoList", todoList);
		return mv;
	}
	
	//入力フォーム表示
	@GetMapping("/todo/create")
	public ModelAndView createForm(ModelAndView mv) {
		mv.setViewName("todoFormH");
		mv.addObject("todoForm" ,new TodoForm());
		session.setAttribute("mode", "create");
		return mv;
	}
	
	//ToDo追加処理
	@PostMapping("/todo/create")
	public String addTodoList(@ModelAttribute @Validated TodoForm todoForm,
									BindingResult result, Model model) {
		
		//エラーチェック
		boolean isValid = todoService.isValid(todoForm,result);
		if(!result.hasErrors() && isValid) {
			//エラーなし
			TodoEntity todoEntity = todoForm.toEntity();
			todoRepo.saveAndFlush(todoEntity);
			return "redirect:/todo";
			
		} else {
			//エラーあり
			// model.addAttribute("todoForm", todoForm);
			return "todoFormH";
		}
	}
	
	//ToDo一覧へ戻る
	@PostMapping("/todo/cancel")
	public String cancel() {
		return "redirect:/todo";
	}
	
	//データの更新(件名をクリックされたとき)
	@GetMapping("/todo/{id}")
	public ModelAndView todoById(@PathVariable(name = "id")int id, ModelAndView mv) {
		mv.setViewName("todoFormH");
		TodoEntity todoEntity = todoRepo.findById(id).get();
		mv.addObject("todoForm", todoEntity);
		session.setAttribute("mode", "update");
		return mv;
	}
	
	//更新ボタンが押されたとき
	@PostMapping("/todo/update")
	public String updateTodo(@ModelAttribute @Validated TodoForm todoForm,
							 BindingResult result,
							 Model model) {
		//エラーチェック
		boolean isValid = todoService.isValid(todoForm, result);
		if(!result.hasErrors() && isValid) {
			TodoEntity todoEntity = todoForm.toEntity();
			todoRepo.saveAndFlush(todoEntity);
			return "redirect:/todo";
			
		} else {
			//エラーあり
			//model.addAttribute("todoForm", todoForm)
			return "todoFormH";
		}
	}
	
	@PostMapping("/todo/delete")
	public String deleteTodo(@ModelAttribute TodoForm todoForm) {
		todoRepo.deleteById(todoForm.getId());
		return "redirect:/todo";
	}
}
