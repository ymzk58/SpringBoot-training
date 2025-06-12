package com.example.todolist.form;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.example.todolist.entity.TodoEntity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TodoForm {
	private Integer id;
	
	@NotBlank(message = "件名を入力してください")
	private String title;
	
	@NotNull(message = "重要度を選択してください")
	private Integer importance;
	
	@Min(value = 0, message = "緊急度を選択してください")
	private Integer urgency;
	
	@NotBlank(message = "期限を記入してください")
	private String deadline;
	
	private String done;
	
	//入力データからEntityを生成して返す
	public TodoEntity toEntity() {
		TodoEntity todoEntity = new TodoEntity();
		todoEntity.setId(id);
		todoEntity.setTitle(title);
		todoEntity.setImportance(importance);
		todoEntity.setUrgency(urgency);
		todoEntity.setDone(done);
		
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		long ms;
		try {
			ms = sdFormat.parse(deadline).getTime();
			todoEntity.setDeadline(new Date(ms));
		} catch (ParseException e) {
			todoEntity.setDeadline(null);
		}
		return todoEntity;
	}
}