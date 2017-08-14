package wechart.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
public class User implements Serializable {

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 存放redis的消息ID
     */
    private String redisKey;//redis中的key

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 用户名
     */
    private String username;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 用户密码
     */
    private String password;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 用户名称拼音
      */
    private String pinying;

    public String getPinying() {
        return pinying;
    }

    public void setPinying(String pinying) {
        this.pinying = pinying;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    private String binding;

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
