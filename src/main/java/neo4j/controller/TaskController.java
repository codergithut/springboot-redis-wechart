package neo4j.controller;

import neo4j.model.Task;
import neo4j.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/11
 * @description
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;


    @Autowired
    Neo4jTemplate neo4jTemplate;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @Transactional
    public Task saveTask() {
        Task taskInfo = new Task();
        taskInfo.setTaskName("test");
        Task task = taskRepository.save(taskInfo);
        return task;
    }



    @RequestMapping(value = "/serarch", method = RequestMethod.GET)
    @ResponseBody
    public Object create() {
        Task task = taskRepository.findByTaskName("test");
        Object o = neo4jTemplate.queryForObjects(Task.class, "", null);
        return o;
    }
}
