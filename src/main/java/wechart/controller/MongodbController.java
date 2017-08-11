package wechart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<TalkHistoryContent> save() {

        IndexOperations io = mongoTemplate.indexOps("talkHistoryContent");

        Index index = new Index();

        index.on("members",Order.ASCENDING); //为name属性加上 索引

        io.ensureIndex(index);

        List<TalkHistoryContent> talkHistoryContents = getTalkHistoryContent();

        //也可以使用Repository插入数据，userService.save(user);
        return talkHistoryContents;
    }

    private List<TalkHistoryContent> getTalkHistoryContent() {
        int i = 0;
        List<TalkHistoryContent> talkHistoryContents = new ArrayList<TalkHistoryContent>();

        while(i < 10) {
            TalkHistoryContent talkHistoryContent = new TalkHistoryContent();

            String[] members = new String[]{i + "", (i + 1) +""};

            List<String> contents = new ArrayList<String>();


            contents.add("test");

            contents.add("haha");

            talkHistoryContent.setKey("test");
            talkHistoryContent.setContents(contents);
            talkHistoryContent.setMembers(members);

            talkHistoryContents.add(talkHistoryContent);

            mongoTemplate.save(talkHistoryContent);

            i++;
        }

        return talkHistoryContents;



    }

    @GetMapping("/find")
    public Object find() {
        Query query = new Query();

        Criteria criteria =new Criteria();
        criteria.and("members").is("1");

        query.addCriteria(criteria);

        List<TalkHistoryContent> list = mongoTemplate.find(query, TalkHistoryContent.class);

        for(TalkHistoryContent s : list) {

            System.out.println(s.toString());

        }

        return list;

    }

}