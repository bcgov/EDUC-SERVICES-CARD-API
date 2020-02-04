package ca.bc.gov.educ.api.servicescard.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.bc.gov.educ.api.servicescard.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.servicescard.exception.InvalidParameterException;
import ca.bc.gov.educ.api.servicescard.model.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ServicesCardServiceTest {

	@Autowired
	ServicesCardRepository repository;
	ServicesCardService service;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

	@Before
	public void before() {
		service = new ServicesCardService(repository);
	}

	@Test
	public void testCreateServicesCard_WhenPayloadIsValid_ShouldReturnSavedObject() throws ParseException {
		ServicesCardEntity servicesCard = getServicesCardEntity();
		assertNotNull(service.createServicesCard(servicesCard));
		assertNotNull(servicesCard.getServicesCardInfoID());
	}

	@Test
	public void testCreateServicesCard_WhenPayloadContainsServicesCardID_ShouldThrowInvalidParameterException()
			throws ParseException {
		ServicesCardEntity servicesCard = getServicesCardEntity();
		servicesCard.setServicesCardInfoID(UUID.fromString("00000000-8000-0000-000e-000000000000"));
		assertThrows(InvalidParameterException.class, () -> service.createServicesCard(servicesCard));
	}

	@Test
	public void testRetrieveServicesCard_WhenServicesCardExistInDB_ShouldReturnServicesCard() throws ParseException {
		ServicesCardEntity servicesCard = getServicesCardEntity();
		assertNotNull(service.createServicesCard(servicesCard));
		assertNotNull(service.retrieveServicesCard(servicesCard.getServicesCardInfoID()));
	}

	@Test
	public void testRetrieveServicesCard_WhenServicesCardDoesNotExistInDB_ShouldThrowEntityNotFoundException() {
		assertThrows(EntityNotFoundException.class,
				() -> service.retrieveServicesCard(UUID.fromString("00000000-0000-0000-0000-f3b2d4f20000")));
	}

	@Test
	public void testUpdateServicesCard_WhenPayloadIsValid_ShouldReturnTheUpdatedObject() throws ParseException {
		ServicesCardEntity servicesCard = getServicesCardEntity();
		servicesCard = service.createServicesCard(servicesCard);
		servicesCard.setGivenName("updatedFirstName");
		ServicesCardEntity updateEntity = service.updateServicesCard(servicesCard);
		assertNotNull(updateEntity);
		assertNotNull(updateEntity.getUpdateDate());
		assertThat(updateEntity.getGivenName().equals("updatedFirstName")).isTrue();
	}

	private ServicesCardEntity getServicesCardEntity() throws ParseException {
		ServicesCardEntity servicesCardEntity = new ServicesCardEntity();
		servicesCardEntity.setBirthDate(formatter.parse("1979-06-11"));
		servicesCardEntity.setCity("Compton");
		servicesCardEntity.setCountry("Canada");
		servicesCardEntity.setDid("1234123112321231");
		servicesCardEntity.setEmail("this@sometest.com");
		servicesCardEntity.setGender("Male");
		servicesCardEntity.setGivenName("Mike");
		servicesCardEntity.setGivenNames("John");
		servicesCardEntity.setSurname("Delanis");
		servicesCardEntity.setPostalCode("V0E1W3");
		servicesCardEntity.setProvince("British Columbia");
		servicesCardEntity.setStreetAddress("123 SomeAddress");
		servicesCardEntity.setUserDisplayName("Mike Delanis");
		return servicesCardEntity;
	}
}
