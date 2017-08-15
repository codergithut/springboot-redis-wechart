package neo4j.repository;

import neo4j.model.User;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/11
 * @description
 */
@Repository
public interface UserRepository extends GraphRepository<User> {

//    Task findByTaskName(@Param("taskName") String taskName);
//
//    @Query("MATCH (t:Task) WHERE t.taskName =~ ('(?i).*'+{taskName}+'.*') RETURN t")
//    Collection<Task> findByNameContaining(@Param("taskName") String taskName);

}

