package ca.bc.gov.educ.api.servicescard.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.educ.api.servicescard.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.servicescard.exception.InvalidParameterException;
import ca.bc.gov.educ.api.servicescard.model.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;

/**
 * Services Card Service
 *
 * @author Marco Villeneuve
 */

@Service
public class ServicesCardService {
  private static final String STUDENT_ID_ATTRIBUTE = "studentID";
  public static final String STUDENT_API = "STUDENT_API";

  @Getter(AccessLevel.PRIVATE)
  private final ServicesCardRepository repository;

  public ServicesCardService(@Autowired final ServicesCardRepository repository) {
    this.repository = repository;
  }

  /**
   * Search for StudentEntity by id
   *
   * @param studentID the unique GUID for a given student.
   * @return the Student entity if found.
   * @throws EntityNotFoundException if the entity is not found in the  database by its GUID.
   */
  public ServicesCardEntity retrieveServicesCard(UUID studentID) {
    Optional<ServicesCardEntity> result = repository.findById(studentID);
    if (result.isPresent()) {
      return result.get();
    } else {
      throw new EntityNotFoundException(ServicesCardEntity.class, STUDENT_ID_ATTRIBUTE, studentID.toString());
    }
  }


  /**
   * Creates a ServicesCardEntity
   *
   * @param servicesCardEntity the payload which will create the student record.
   * @return the saved instance.
   * @throws InvalidParameterException if Student GUID is passed in the payload for the post operation it will throw this exception.
   */
  public ServicesCardEntity createServicesCard(ServicesCardEntity servicesCardEntity) {

    if (servicesCardEntity.getStudentID() != null)
      throw new InvalidParameterException(STUDENT_ID_ATTRIBUTE);
    servicesCardEntity.setCreateUser(STUDENT_API);
    servicesCardEntity.setUpdateUser(STUDENT_API);
    servicesCardEntity.setUpdateDate(new Date());
    servicesCardEntity.setCreateDate(new Date());

    return repository.save(servicesCardEntity);
  }

  /**
   * Updates a ServicesCardEntity
   *
   * @param student the payload which will update the DB record for the given student.
   * @return the updated entity.
   * @throws EntityNotFoundException if the entity does not exist in the DB.
   */
  public ServicesCardEntity updateServicesCard(ServicesCardEntity student) {

    Optional<ServicesCardEntity> curServicesCardEntity = repository.findById(student.getStudentID());

    if (curServicesCardEntity.isPresent()) {
      final ServicesCardEntity newServicesCardEntity = curServicesCardEntity.get();
      val createUser = newServicesCardEntity.getCreateUser();
      val createDate = newServicesCardEntity.getCreateDate();
      BeanUtils.copyProperties(student, newServicesCardEntity);
      newServicesCardEntity.setUpdateUser(STUDENT_API);
      newServicesCardEntity.setUpdateDate(new Date());
      newServicesCardEntity.setCreateUser(createUser);
      newServicesCardEntity.setCreateDate(createDate);
      return repository.save(newServicesCardEntity);
    } else {
      throw new EntityNotFoundException(ServicesCardEntity.class, STUDENT_ID_ATTRIBUTE, student.getStudentID().toString());
    }
  }

}
