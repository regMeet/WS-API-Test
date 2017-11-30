package com.pivotus.bff.restservices;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pivotus.bff.common.VO.TaskVO;
import com.pivotus.bff.common.entities.Task;

@RestController
@RequestMapping(value = "/api/test")
public class TestRestServices {

    private List<Task> taskList;

    public TestRestServices() {
        taskList = new ArrayList<>();
        taskList.add(Task.builder().id(1L).name("Office").description("Description n1").build());
        taskList.add(Task.builder().id(2L).name("Super").description("Description n2").build());
        taskList.add(Task.builder().id(3L).name("Personal").description("Description n3").build());
        taskList.add(Task.builder().id(4L).name("Office").description("Description n4").build());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Task>> getTaskList(@RequestHeader(value = "Authorization", required = true) String accessToken,
            @RequestParam(value = "name", required = false) String name) {

        if (name != null) {
            List<Task> filteredTaskList = taskList.stream().filter(task -> {
                boolean equalTask = task.getName().contains(name);
                boolean notDeleted = task.isDeleted() == false;
                return equalTask && notDeleted;
            }).collect(Collectors.toList());
            return new ResponseEntity<List<Task>>(filteredTaskList, HttpStatus.OK);
        }

        List<Task> filteredTaskList = taskList.stream().filter(task -> task.isDeleted() == false).collect(Collectors.toList());

        return new ResponseEntity<List<Task>>(filteredTaskList, HttpStatus.OK);
    }

    @RequestMapping(value = "/{todoTaskId}", method = RequestMethod.PUT)
    public ResponseEntity<Task> updateTask(@RequestHeader(value = "Authorization", required = true) String accessToken, @PathVariable("todoTaskId") Long todoTaskId,
            @RequestBody @Valid TaskVO task) {

        Task taskToBeUpdated = findTaskById(todoTaskId);
        if (taskToBeUpdated != null) {
            taskToBeUpdated.setName(task.getName());
            taskToBeUpdated.setDescription(task.getDescription());
            return new ResponseEntity<Task>(taskToBeUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Task> createTask(@RequestHeader(value = "Authorization", required = true) String accessToken, @RequestBody @Valid TaskVO task) {

        Long lastId = taskList.get(taskList.size() - 1).getId();
        Long nextId = lastId + 1;

        Task newTask = Task.builder().id(nextId).name(task.getName()).description(task.getDescription()).build();
        taskList.add(newTask);

        return new ResponseEntity<Task>(newTask, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{todoTaskId}", method = RequestMethod.DELETE)
    public ResponseEntity<List<Task>> deleteTask(@RequestHeader(value = "Authorization", required = true) String accessToken,
            @PathVariable("todoTaskId") Long todoTaskId) {

        Task taskToBeDeleted = findTaskById(todoTaskId);
        if (taskToBeDeleted != null) {
            taskToBeDeleted.setDeleted(true);
            return getTaskList(accessToken, null);
        } else {
            return new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{todoTaskId}/done", method = RequestMethod.POST)
    public ResponseEntity<Task> markTaskAsDone(@RequestHeader(value = "Authorization", required = true) String accessToken, @PathVariable("todoTaskId") Long todoTaskId) {

        Task taskToBeMarked = findTaskById(todoTaskId);
        if (taskToBeMarked != null) {
            taskToBeMarked.setDone(true);
            return new ResponseEntity<Task>(taskToBeMarked, HttpStatus.OK);
        } else {
            return new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value = "/expireToken", method = RequestMethod.POST)
    public ResponseEntity<Task> expireToken(@RequestHeader(value = "Authorization", required = true) String accessToken) {
        return new ResponseEntity<Task>(HttpStatus.UNAUTHORIZED);
    }

    private Task findTaskById(Long todoTaskId) {
        return taskList.stream().filter(t -> {
            boolean equals = t.getId().equals(todoTaskId);
            boolean notDeleted = t.isDeleted() == false;
            return equals && notDeleted;
        }).findAny().orElse(null);
    }
}
