package com.example.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.TodoEntity;

@Repository
public interface TodoRepo extends JpaRepository<TodoEntity, Integer> {

}
