package wechart.quartz;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import wechart.config.CommonValue;

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
    @Qualifier(JEDIS)
    Jedis read;

    public void saveHistoryContent() throws Exception {
        Set<String> keys = read.smembers(HISTORYCONTENT);

        for(String key : keys) {
            Set<String> contents = read.smembers(key);
            //todo 将数据塞到mongodb中
        }


        System.out.println("test1");

    }


}

