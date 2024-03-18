/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 *
 */

// Write your code here

package com.example.todo.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

//import javax.validation.OverridesAttribute;

import com.example.todo.repository.TodoRepository;
import com.example.todo.model.Todo;
import com.example.todo.model.TodoRowMapper;

@Service
public class TodoH2Service implements TodoRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Todo> getTodos() {
        List<Todo> list = db.query("SELECT * FROM TODOLIST", new TodoRowMapper());
        ArrayList<Todo> todos = new ArrayList<>(list);
        return todos;

    }

    @Override
    public Todo getTodoById(int id) {
        try {
            Todo todos = db.queryForObject("SELECT * FROM TODOLIST where id = ?", new TodoRowMapper(), id);
            return todos;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Todo addTodo(Todo todo) {
        db.update("insert into TODOLIST(todo, status, priority) values(?, ?, ?)", todo.getTodo(), todo.getStatus(),
                todo.getPriority());

        Todo todo1 = db.queryForObject("SELECT * FROM TODOLIST where todo = ? and status = ? and priority = ?",
                new TodoRowMapper(), todo.getTodo(), todo.getStatus(), todo.getPriority());

        return todo1;

    }

    @Override
    public Todo updateTodo(int id, Todo todo) {
            if (todo.getTodo() != null) {
                db.update("update TODOLIST set todo = ? where id = ?", todo.getTodo(), todo);
            }
            if (todo.getStatus() != null) {
                db.update("update TODOLIST set status = ? where id = ?", todo.getStatus(), todo);
            }
            if (todo.getPriority() != null) {
                db.update("update TODOLIST set priority = ? where id = ?", todo.getPriority(), todo);
            }
            return getTodoById(id);

    }

    @Override
    public void deleteTodo(int id) {
        db.update("delete from TODOLIST where id = ?", id);
    }
}