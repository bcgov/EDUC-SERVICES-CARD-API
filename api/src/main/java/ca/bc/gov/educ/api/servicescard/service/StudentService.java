package ca.bc.gov.educ.api.student.service;

import ca.bc.gov.educ.api.student.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.student.exception.InvalidParameterException;
import ca.bc.gov.educ.api.student.model.StudentEntity;
import ca.bc.gov.educ.api.student.repository.StudentRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * StudentService
 *
 * @author John Cox
 */

@Service
public class StudentService {
  private static final String STUDENT_ID_ATTRIBUTE = "studentID";
  public static final String STUDENT_API = "STUDENT_API";

  @Getter(AccessLevel.PRIVATE)
  private final StudentRepository repository;

  public StudentService(@Autowired final StudentRepository repository) {
    this.repository = repository;
  }

  /**
   * Search for StudentEntity by id
   *
   * @param studentID the unique GUID for a given student.
   * @return the Student entity if found.
   * @throws EntityNotFoundException if the entity is not found in the  database by its GUID.
   */
  public StudentEntity retrieveStudent(UUID studentID) {
    Optional<StudentEntity> result = repository.findById(studentID);
    if (result.isPresent()) {
      return result.get();
    } else {
      throw new EntityNotFoundException(StudentEntity.class, STUDENT_ID_ATTRIBUTE, studentID.toString());
    }
  }

  /**
   * Search for StudentEntity by PEN
   *
   * @param pen the unique PEN for a given student.
   * @return the Student entity if found.
   */
  public Optional<StudentEntity> retrieveStudentByPen(String pen) {
    return repository.findStudentEntityByPen(pen);
  }

  /**
   * Creates a StudentEntity
   *
   * @param student the payload which will create the student record.
   * @return the saved instance.
   * @throws InvalidParameterException if Student GUID is passed in the payload for the post operation it will throw this exception.
   */
  public StudentEntity createStudent(StudentEntity student) {

    if (student.getStudentID() != null)
      throw new InvalidParameterException(STUDENT_ID_ATTRIBUTE);
    student.setCreateUser(STUDENT_API);
    student.setUpdateUser(STUDENT_API);
    student.setUpdateDate(new Date());
    student.setCreateDate(new Date());

    return repository.save(student);
  }

  /**
   * Updates a StudentEntity
   *
   * @param student the payload which will update the DB record for the given student.
   * @return the updated entity.
   * @throws EntityNotFoundException if the entity does not exist in the DB.
   */
  public StudentEntity updateStudent(StudentEntity student) {

    Optional<StudentEntity> curStudentEntity = repository.findById(student.getStudentID());

    if (curStudentEntity.isPresent()) {
      final StudentEntity newStudentEntity = curStudentEntity.get();
      val createUser = newStudentEntity.getCreateUser();
      val createDate = newStudentEntity.getCreateDate();
      BeanUtils.copyProperties(student, newStudentEntity);
      newStudentEntity.setUpdateUser(STUDENT_API);
      newStudentEntity.setUpdateDate(new Date());
      newStudentEntity.setCreateUser(createUser);
      newStudentEntity.setCreateDate(createDate);
      return repository.save(newStudentEntity);
    } else {
      throw new EntityNotFoundException(StudentEntity.class, STUDENT_ID_ATTRIBUTE, student.getStudentID().toString());
    }
  }

  public Optional<StudentEntity> retrieveStudentByEmail(String email) {
    return repository.findStudentEntityByEmail(email);
  }
}
