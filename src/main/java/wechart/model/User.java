package wechart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
@NodeEntity
public class User implements Serializable {

    @GraphId
    private Long id;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 存放redis的消息ID
     */
    private String redisKey;//redis中的key


    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    @Relationship(type = "friends", direction = Relationship.OUTGOING)
    @JsonIgnore
    private Set<User> friends;

    @Relationship(type = "contains", direction = Relationship.OUTGOING)
    @JsonIgnore
    private Set<Masses> masseses;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @param friend 需要添加的好友信息
     * @return void
     * @description 添加好友信息，双方互相添加为好友。
     */
    public void addFriends(User friend) {

        if(friends == null) {

            friends = new HashSet<User>();

        }

        friends.add(friend);

    }


    public void addMasses(Masses masses) {

        if(masseses == null) {

            masseses = new HashSet<Masses>();

        }

        masseses.add(masses);

    }

    public Set<Masses> getMasseses() {
        return masseses;
    }

    public void setMasseses(Set<Masses> masseses) {
        this.masseses = masseses;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }
}
