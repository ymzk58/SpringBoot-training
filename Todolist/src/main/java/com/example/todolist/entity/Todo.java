package com.example.todolist.entity;

import java.sql.Date;

import jakarta.persistence.Column;	//カラムの制約をカスタマイズ
import jakarta.persistence.Entity;	//クラスをDBと見なす
import jakarta.persistence.GeneratedValue;	//主キーの自動生成
import jakarta.persistence.GenerationType;	//生成方法
import jakarta.persistence.Id;		//主キーを表す
import jakarta.persistence.Table;	//テーブル名
import lombok.Data;

//PostgreSQLから受け取るには対応したクラスが必要

@Entity
@Table(name = "todo")
@Data
public class Todo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "title")
  private String title;

  @Column(name = "importance")
  private Integer importance;

  @Column(name = "urgency")
  private Integer urgency;

  @Column(name = "deadline")
  private Date deadline;

  @Column(name = "done")
  private String done;
}
