package wechart.controller;

import com.mongodb.BasicDBList;
import com.mongodb.Bytes;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import mongodb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wechart.model.TalkHistoryContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/8
 * @description
 */
@RestController
public class MongodbController {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * save use before findName
     * @return
     */
    @GetMapping("/save")
    public TalkHistoryContent save() {

        TalkHistoryContent talkHistoryContent = new TalkHistoryContent();

        List<String> members = new ArrayList<String>();

        List<String> contents = new ArrayList<String>();

        members.add("1");

        members.add("2");

        contents.add("test");

        contents.add("haha");

        talkHistoryContent.setKey("test");
        talkHistoryContent.setContents(contents);
        talkHistoryContent.setMembers(members);



        IndexOperations io = mongoTemplate.indexOps("talkHistoryContent");

        Index index = new Index();

        index.on("members",Order.ASCENDING); //为name属性加上 索引

        io.ensureIndex(index);

        mongoTemplate.save(talkHistoryContent);

        //也可以使用Repository插入数据，userService.save(user);
        return talkHistoryContent;
    }

    @GetMapping("/find")
    public List<User> find() {
        Query query = new Query();

        Criteria criteria =new Criteria();
        criteria.and("members").is("1");

        query.addCriteria(criteria);

        List<TalkHistoryContent> list = mongoTemplate.find(query, TalkHistoryContent.class);

        for(TalkHistoryContent s : list) {
            System.out.println(s.toString());
        }

//        mongoTemplate.
//        List<User> userList = mongoTemplate.findAll(User.class);
//        return userList;



        return null;
    }

}