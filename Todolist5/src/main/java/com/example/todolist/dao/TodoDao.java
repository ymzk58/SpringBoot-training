package com.example.todolist.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.todolist.entity.TodoEntity;
import com.example.todolist.form.TodoQuery;

public interface TodoDao {
	// JPQLによる検索
	List<TodoEntity> findByJPQL(TodoQuery todoQuery);
	
	//Criteria APIによる検索
	Page<TodoEntity> findByCriteria(TodoQuery todoQuery, Pageable pageable);
}