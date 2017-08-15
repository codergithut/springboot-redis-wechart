package wechart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/11
 * @description
 */
@NodeEntity
public class Masses {

    @GraphId
    private Long id;

    private String redisKey;

    /**
     * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
     * @description 有可能是群有可能是兴趣小组啥的
     */
    private String type;

    @Relationship(type = "contains", direction = Relationship.INCOMING)
    @JsonIgnore
    private Set<User> users;

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }


    public void addMasses(User user) {

        if(users == null) {

            users = new HashSet<User>();

        }

        users.add(user);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
