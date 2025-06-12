package com.example.todolist.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.dao.TodoDaoImpl;
import com.example.todolist.entity.TodoEntity;
import com.example.todolist.form.TodoForm;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepo;
import com.example.todolist.service.TodoService;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TodolistController {
	private final TodoRepo todoRepo;
	private final TodoService todoService;
	private final HttpSession session;
	//Todolist5で追加
	@PersistenceContext
	private EntityManager entityManager;
	TodoDaoImpl todoDaoImpl;
	
	@PostConstruct
	public void init() {
		todoDaoImpl = new TodoDaoImpl(entityManager);
	}
	
	//ToDo一覧を表示
	@GetMapping("/todo")
	public ModelAndView showTodolist(ModelAndView mv,
		@PageableDefault(page = 0, size = 5, sort = "id")  Pageable pageable) {
		mv.setViewName("todoListH");
		
		Page<TodoEntity> todoPage = todoRepo.findAll(pageable);
		mv.addObject("todoQuery", new TodoQuery());
		mv.addObject("todoPage", todoPage);
		mv.addObject("todoList", todoPage.getContent());
		session.setAttribute("todoQuery", new TodoQuery());
		return mv;
	}
	
	//入力フォーム表示
	@GetMapping("/todo/create/form")
	public ModelAndView createForm(ModelAndView mv) {
		mv.setViewName("todoFormH");
		mv.addObject("todoForm" ,new TodoForm());
		session.setAttribute("mode", "create");
		return mv;
	}
	
	//ToDo追加処理
	@PostMapping("/todo/create/do")
	public String addTodoList(@ModelAttribute @Validated TodoForm todoForm,
									BindingResult result, Model model,
									@SessionAttribute(required = false) String mode) {
		
		//エラーチェック
		boolean isValid = todoService.isValid(todoForm, result, mode);
		if(!result.hasErrors() && isValid) {
			//エラーなし
			TodoEntity todoEntity = todoForm.toEntity();
			todoRepo.saveAndFlush(todoEntity);
			//currentPage取得
			Integer currentPage = (Integer) session.getAttribute("currentPage");
			
			return "redirect:/todo/query?page=" + currentPage;
			
		} else {
			//エラーあり
			// model.addAttribute("todoForm", todoForm);
			return "todoFormH";
		}
	}
	
	//ToDo一覧へ戻る
	@PostMapping("/todo/cancel")
	public String cancel() {
		Integer currentPage = (Integer) session.getAttribute("currentPage");
		return "redirect:/todo/query?page=" + currentPage;
	}
	
	//データの更新(件名をクリックされたとき)
	@GetMapping("/todo/{id}")
	public ModelAndView todoById(@PathVariable int id, ModelAndView mv) {
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
							 Model model,
							 @SessionAttribute(required = false) String mode) {
		//エラーチェック
		boolean isValid = todoService.isValid(todoForm, result, mode);
		if(!result.hasErrors() && isValid) {
			TodoEntity todoEntity = todoForm.toEntity();
			todoRepo.saveAndFlush(todoEntity);
			//currentPage取得
			Integer currentPage = (Integer) session.getAttribute("currentPage");
			return "redirect:/todo/query?page=" + currentPage;
			
		} else {
			//エラーあり
			//model.addAttribute("todoForm", todoForm)
			return "todoFormH";
		}
	}
	
	//削除
	@PostMapping("/todo/delete")
	public String deleteTodo(@ModelAttribute TodoForm todoForm) {
		todoRepo.deleteById(todoForm.getId());
		//currentPage取得
		Integer currentPage = (Integer) session.getAttribute("currentPage");
		return "redirect:/todo/query?page=" + currentPage;
	}
	// ToDo検索処理
	@PostMapping("/todo/query")
	public ModelAndView queryTodo(@ModelAttribute TodoQuery todoQuery,
								  BindingResult result,
								  @PageableDefault(page = 0, size = 5) Pageable pageable,
								  ModelAndView mv) {
		mv.setViewName("todoListH");
		
		Page<TodoEntity> todoPage = null;
		if(todoService.isValid(todoQuery, result)) {
			//エラーが無ければ検索
			todoPage = todoDaoImpl.findByCriteria(todoQuery, pageable);
			
			//入力された検索条件をsessionに保存
			session.setAttribute("todoQuery", todoQuery);
			
			mv.addObject("todoPage", todoPage);
			mv.addObject("todoList", todoPage.getContent());
		} else {
			//エラーがあった場合検索
			mv.addObject("todoPage", null);
			mv.addObject("todoList", null);
		}
		return mv;
	}
	@GetMapping("/todo/query")
	public ModelAndView queryTodo(@PageableDefault(page = 0, size = 5) Pageable pageable,
								  ModelAndView mv) {
		mv.setViewName("todoListH");
		
		//sessionに保存されている条件で検索
		TodoQuery todoQuery = (TodoQuery)session.getAttribute("todoQuery");
		Page<TodoEntity> todoPage = todoDaoImpl.findByCriteria(todoQuery, pageable);
		
		mv.addObject("todoQuery", todoQuery);	//検索条件表示用
		mv.addObject("todoPage", todoPage);		//page 情報
		mv.addObject("todoList", todoPage.getContent());	//検索結果
		
		//今のページ数をセッションに格納
		int currentPage = pageable.getPageNumber();
		session.setAttribute("currentPage", currentPage);
		return mv;
	}
}