package pl.akademiakodu.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.akademiakodu.models.UploadedProject;

public interface UploadedProjectRepository extends CrudRepository<UploadedProject, Integer> {

}
