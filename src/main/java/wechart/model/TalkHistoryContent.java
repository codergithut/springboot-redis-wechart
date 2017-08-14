package wechart.model;

import com.alibaba.fastjson.JSON;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/8
 * @description 消息记录
 */
public class TalkHistoryContent {

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description mongodb使用的id
     */
    @Id
    private String id;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 存放消息相关人员ID信息
     */
    private String key;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 需要通知的对象ID
     */
    private String[] members;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 消息发送时间
     */
    private Date date;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 将消息记录通过json转换为String存放该字段中
     */
    private List<String> contents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
