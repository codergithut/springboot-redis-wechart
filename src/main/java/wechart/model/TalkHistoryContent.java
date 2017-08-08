package wechart.model;

import com.alibaba.fastjson.JSON;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/8
 * @description
 */
public class TalkHistoryContent {
    @Id
    private String id;

    private String key;

    private String[] members;

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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
