package neo4j.controller;

import neo4j.model.Task;
import neo4j.model.User;
import neo4j.repository.TaskRepository;
import neo4j.repository.UserRepository;
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
    UserRepository userRepository;


    @Autowired
    Neo4jTemplate neo4jTemplate;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @Transactional
    public Object saveTask() {
        User user = new User();
        user.setName("你好地球人");
        Task taskInfo = new Task();
        taskInfo.setTaskName("test");
        Task task = taskRepository.save(taskInfo);
        User user1 = userRepository.save(user);
        return user1;
    }



    @RequestMapping(value = "/serarch", method = RequestMethod.GET)
    @ResponseBody
    public Object create() {
        Task task = taskRepository.findByTaskName("测试任务");
        long count = neo4jTemplate.count(Task.class);
        taskRepository.delete(task);
        return count;
    }
}
