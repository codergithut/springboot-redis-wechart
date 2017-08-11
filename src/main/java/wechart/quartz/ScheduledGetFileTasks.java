package wechart.quartz;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import wechart.config.CommonValue;
import wechart.model.TalkHistoryContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/2/13
 * @description 定时器启动到指定文件夹下获取数据并上传到消费接受中心
 */
@Component
@Configurable
public class ScheduledGetFileTasks implements CommonValue{

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SetOperations setOperations;

    public void saveHistoryContent() throws Exception {

        IndexOperations io = mongoTemplate.indexOps("talkHistoryContent");

        Index index = new Index();

        index.on("members", Order.ASCENDING); //为name属性加上 索引

        io.ensureIndex(index);

        Set<String> keys = setOperations.members(HISTORYCONTENT);

        for(String key : keys) {

            Set<String> contents = setOperations.members(key);

            String[] members = key.split("_");

            TalkHistoryContent talkHistoryContent = new TalkHistoryContent();

            talkHistoryContent.setMembers(members);

            List<String> files = new ArrayList<String>();

            for(String content : contents) {

                files.add(content);

            }

            mongoTemplate.save(talkHistoryContent);
        }

    }


}

