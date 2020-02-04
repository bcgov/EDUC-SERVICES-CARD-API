package ca.bc.gov.educ.api.student.repository;

import ca.bc.gov.educ.api.student.model.StudentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends CrudRepository<StudentEntity, UUID> {
  Optional<StudentEntity> findStudentEntityByPen(String pen);

  Optional<StudentEntity> findStudentEntityByEmail(String email);

}
