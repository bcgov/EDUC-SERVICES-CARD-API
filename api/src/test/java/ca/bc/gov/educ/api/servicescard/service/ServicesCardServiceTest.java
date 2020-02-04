package ca.bc.gov.educ.api.servicescard.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
	public void testCreateStudent_WhenPayloadIsValid_ShouldReturnSavedObject() throws ParseException {
		ServicesCardEntity servicesCard = getServicesCardEntity();
		assertNotNull(service.createServicesCard(servicesCard));
		assertNotNull(servicesCard.getServicesCardInfoID());
	}

//
//
//  @Test
//  public void testCreateStudent_WhenPayloadContainsStudentID_ShouldThrowInvalidParameterException() throws ParseException {
//    StudentEntity student = getStudentEntity();
//    student.setStudentID(UUID.fromString("00000000-8000-0000-000e-000000000000"));
//    assertThrows(InvalidParameterException.class, () -> service.createStudent(student));
//  }
//
//
//  @Test
//  public void testRetrieveStudent_WhenStudentExistInDB_ShouldReturnStudent() throws ParseException {
//    StudentEntity student = getStudentEntity();
//    assertNotNull(service.createStudent(student));
//    assertNotNull(service.retrieveStudent(student.getStudentID()));
//  }
//
//  @Test
//  public void testRetrieveStudent_WhenStudentDoesNotExistInDB_ShouldThrowEntityNotFoundException() {
//    assertThrows(EntityNotFoundException.class, () -> service.retrieveStudent(UUID.fromString("00000000-0000-0000-0000-f3b2d4f20000")));
//  }
//
//  @Test
//  public void testUpdateStudent_WhenPayloadIsValid_ShouldReturnTheUpdatedObject() throws ParseException {
//
//    StudentEntity student = getStudentEntity();
//    student = service.createStudent(student);
//    student.setLegalFirstName("updatedFirstName");
//    StudentEntity updateEntity = service.updateStudent(student);
//    assertNotNull(updateEntity);
//    assertNotNull(updateEntity.getUpdateDate());
//    assertThat(updateEntity.getLegalFirstName().equals("updatedFirstName")).isTrue();
//  }
//
//
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
