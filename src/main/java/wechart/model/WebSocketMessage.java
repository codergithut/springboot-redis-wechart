package wechart.model;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/8/8
 * @description
 */
public class WebSocketMessage {

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 发送方id
     */
    private String sendId;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 接受方id
     */
    private String receivedId;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 消息类型
     */
    private String type;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 消息发送时间
     */
    private Date date;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 消息发送的内容
     */
    private String content;

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReceivedId() {
        return receivedId;
    }

    public void setReceivedId(String receivedId) {
        this.receivedId = receivedId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
