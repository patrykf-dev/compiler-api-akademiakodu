package pl.akademiakodu.repositories;
import org.springframework.data.repository.CrudRepository;
import pl.akademiakodu.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
