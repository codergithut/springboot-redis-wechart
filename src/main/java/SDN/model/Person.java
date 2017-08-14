package SDN.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/14
 * @description
 */
@NodeEntity
public class Person {

    @GraphId
    private Long id;

    private String name;

    private int sex;

    @DateLong
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:sss")
    private Date create;

    @Relationship(type = "朋友", direction = Relationship.OUTGOING)
    @JsonIgnore
    private Set<Person> friends = new HashSet<Person>();

    @Relationship(type = "评分")
    private Set<Rating> ratings =new HashSet<Rating>();

    @Relationship(type = "观看", direction = Relationship.OUTGOING)
    private Set<Show> visiters = new HashSet<Show>();

    public void beFriend(Person person) {
        friends.add(person);
    }

    public void addVisiter(Show show) {
        visiters.add(show);
    }

    public Rating rate(Movie movie, int starts, String comment) {
        Rating rating = new Rating(this, movie, starts, comment);
        ratings.add(rating);
        return rating;
    }

    public Person() {
    }

    public Person(Long id, String name, int sex, Date create, Set<Person> friends, Set<Rating> ratings, Set<Show> visiters) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.create = create;
        this.friends = friends;
        this.ratings = ratings;
        this.visiters = visiters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Set<Person> getFriends() {
        return friends;
    }

    public void setFriends(Set<Person> friends) {
        this.friends = friends;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Set<Show> getVisiters() {
        return visiters;
    }

    public void setVisiters(Set<Show> visiters) {
        this.visiters = visiters;
    }
}
