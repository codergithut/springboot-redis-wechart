package SDN.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.Set;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/14
 * @description
 */
@RelationshipEntity(type = "观看")
public class Show {

    @GraphId
    private Long id;

    @StartNode
    @JsonBackReference
    private Set<Person> persons;

    @EndNode
    @JsonBackReference
    private Movie movie;

    public Show(Long id, Set<Person> persons, Movie movie) {
        this.id = id;
        this.persons = persons;
        this.movie = movie;
    }

    public Show() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
