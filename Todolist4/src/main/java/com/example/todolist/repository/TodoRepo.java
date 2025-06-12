package com.example.todolist.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.TodoEntity;

@Repository
public interface TodoRepo extends JpaRepository<TodoEntity, Integer> {
	List<TodoEntity> findByTitleLike(String title);
	List<TodoEntity> findByImportance(Integer importance);
	List<TodoEntity> findByUrgency(Integer urgency);
	List<TodoEntity> findByDeadlineBetweenOrderByDeadlineAsc(Date from, Date to);
	List<TodoEntity> findByDeadlineGreaterThanEqualOrderByDeadlineAsc(Date from);
	List<TodoEntity> findByDeadlineLessThanEqualOrderByDeadlineAsc(Date to);
	List<TodoEntity> findByDone(String done);
}