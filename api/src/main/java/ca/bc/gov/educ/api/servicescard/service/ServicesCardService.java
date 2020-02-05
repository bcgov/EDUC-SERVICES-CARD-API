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

/**
 * Services Card Service
 *
 * @author Marco Villeneuve
 */

@Service
public class ServicesCardService {
  private static final String SERVICES_CARD_ID_ATTRIBUTE = "servicesCardInfoID";
  public static final String SERVICES_CARD_API = "SERVICES_CARD_API";

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
      throw new EntityNotFoundException(ServicesCardEntity.class, SERVICES_CARD_ID_ATTRIBUTE, studentID.toString());
    }
  }
  
  /**
   * Search for DigitalIDEntity by identity value and identity type code (BCeID or BCSC)
   *
   * @param typeValue
   * @param typeCode
   * @return
   * @throws EntityNotFoundException
   */
  public ServicesCardEntity searchServicesCard(String did) {
    Optional<ServicesCardEntity> result = repository.findByDid(did.toUpperCase());

    if (result.isPresent()) {
      return result.get();
    } else {
      throw new EntityNotFoundException(ServicesCardEntity.class, "did", did);
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

    if (servicesCardEntity.getServicesCardInfoID() != null)
      throw new InvalidParameterException(SERVICES_CARD_ID_ATTRIBUTE);
    servicesCardEntity.setCreateUser(SERVICES_CARD_API);
    servicesCardEntity.setUpdateUser(SERVICES_CARD_API);
    servicesCardEntity.setUpdateDate(new Date());
    servicesCardEntity.setCreateDate(new Date());

    return repository.save(servicesCardEntity);
  }

  /**
   * Updates a ServicesCardEntity
   *
   * @param serviceCard the payload which will update the DB record for the given student.
   * @return the updated entity.
   * @throws EntityNotFoundException if the entity does not exist in the DB.
   */
  public ServicesCardEntity updateServicesCard(ServicesCardEntity serviceCard) {

    Optional<ServicesCardEntity> curServicesCardEntity = repository.findById(serviceCard.getServicesCardInfoID());

    if (curServicesCardEntity.isPresent()) {
      final ServicesCardEntity newServicesCardEntity = curServicesCardEntity.get();
      String createUser = newServicesCardEntity.getCreateUser();
      Date createDate = newServicesCardEntity.getCreateDate();
      BeanUtils.copyProperties(serviceCard, newServicesCardEntity);
      newServicesCardEntity.setUpdateUser(SERVICES_CARD_API);
      newServicesCardEntity.setUpdateDate(new Date());
      newServicesCardEntity.setCreateUser(createUser);
      newServicesCardEntity.setCreateDate(createDate);
      return repository.save(newServicesCardEntity);
    } else {
      throw new EntityNotFoundException(ServicesCardEntity.class, SERVICES_CARD_ID_ATTRIBUTE, serviceCard.getServicesCardInfoID().toString());
    }
  }

}
