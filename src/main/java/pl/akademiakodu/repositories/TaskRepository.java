package pl.akademiakodu.repositories;
import org.springframework.data.repository.CrudRepository;
import pl.akademiakodu.models.Task;

public interface TaskRepository extends CrudRepository<Task, Integer> {

}