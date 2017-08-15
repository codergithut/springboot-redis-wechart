package wechart.neo4j.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import wechart.model.Masses;
import wechart.model.User;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/11
 * @description
 */
@Repository
public interface MassesRepository extends GraphRepository<Masses> {

//    Task findByTaskName(@Param("taskName") String taskName);
//
//    @Query("MATCH (t:Task) WHERE t.taskName =~ ('(?i).*'+{taskName}+'.*') RETURN t")
//    Collection<Task> findByNameContaining(@Param("taskName") String taskName);

}

