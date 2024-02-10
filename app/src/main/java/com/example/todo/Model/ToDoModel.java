package com.example.todo.Model;

import java.util.Date;

public class ToDoModel {
    private int id, status;
    private String task;
    private String date;

    public ToDoModel(String task, String date) {
        this.task = task;
        this.date = date;
        this.status = 0;
    }
    public ToDoModel(String task, String date, int status) {
        this.task = task;
        this.date = date;
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
