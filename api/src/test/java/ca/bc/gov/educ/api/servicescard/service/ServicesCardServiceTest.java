package ca.bc.gov.educ.api.servicescard.service;

import ca.bc.gov.educ.api.servicescard.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.servicescard.model.v1.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardRepository;
import ca.bc.gov.educ.api.servicescard.service.v1.ServicesCardService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ServicesCardServiceTest {

  @Autowired
  ServicesCardRepository repository;
  ServicesCardService service;

  @Before
  public void before() {
    this.service = new ServicesCardService(this.repository);
  }

  @Test
  public void testCreateServicesCard_WhenPayloadIsValid_ShouldReturnSavedObject() {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    assertNotNull(this.service.createServicesCard(servicesCard));
    assertNotNull(servicesCard.getServicesCardInfoID());
  }


  @Test
  public void testRetrieveServicesCard_WhenServicesCardExistInDB_ShouldReturnServicesCard() {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    assertNotNull(this.service.createServicesCard(servicesCard));
    assertNotNull(this.service.retrieveServicesCard(servicesCard.getServicesCardInfoID()));
  }

  @Test
  public void testRetrieveServicesCard_WhenServicesCardDoesNotExistInDB_ShouldThrowEntityNotFoundException() {
    final UUID id = UUID.fromString("00000000-0000-0000-0000-f3b2d4f20000");
    assertThrows(EntityNotFoundException.class,
      () -> this.service.retrieveServicesCard(id));
  }

  @Test
  public void testUpdateServicesCard_WhenPayloadIsValid_ShouldReturnTheUpdatedObject() {
    ServicesCardEntity servicesCard = this.getServicesCardEntity();
    servicesCard = this.service.createServicesCard(servicesCard);
    servicesCard.setGivenName("updatedFirstName");
    final ServicesCardEntity updateEntity = this.service.updateServicesCard(servicesCard, servicesCard.getServicesCardInfoID());
    assertNotNull(updateEntity);
    assertNotNull(updateEntity.getUpdateDate());
    assertThat(updateEntity.getGivenName()).isEqualTo("updatedFirstName");
  }

  private ServicesCardEntity getServicesCardEntity() {
    final ServicesCardEntity servicesCardEntity = new ServicesCardEntity();
    servicesCardEntity.setDigitalIdentityID(UUID.randomUUID());
    servicesCardEntity.setBirthDate(LocalDate.parse("1979-06-11"));
    servicesCardEntity.setDid("1234123112321231");
    servicesCardEntity.setEmail("this@sometest.com");
    servicesCardEntity.setGender("Male");
    servicesCardEntity.setIdentityAssuranceLevel("1");
    servicesCardEntity.setGivenName("Mike");
    servicesCardEntity.setGivenNames("John");
    servicesCardEntity.setSurname("Delanis");
    servicesCardEntity.setPostalCode("V0E1W3");
    servicesCardEntity.setUserDisplayName("Mike Delanis");
    return servicesCardEntity;
  }
}
