package wechart.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
public class Manage implements Serializable {

    private String redisKey;//redis中的key

    private String username;

    private String password;

    private String powder;

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

    public String getPowder() {
        return powder;
    }

    public void setPowder(String powder) {
        this.powder = powder;
    }
}
