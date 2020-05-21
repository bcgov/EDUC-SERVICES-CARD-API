package ca.bc.gov.educ.api.servicescard.service;

import ca.bc.gov.educ.api.servicescard.constants.EventOutcome;
import ca.bc.gov.educ.api.servicescard.constants.EventStatus;
import ca.bc.gov.educ.api.servicescard.constants.EventType;
import ca.bc.gov.educ.api.servicescard.mappers.ServicesCardMapper;
import ca.bc.gov.educ.api.servicescard.model.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardEventRepository;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardRepository;
import ca.bc.gov.educ.api.servicescard.struct.Event;
import ca.bc.gov.educ.api.servicescard.struct.ServicesCard;
import ca.bc.gov.educ.api.servicescard.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EventHandlerServiceTest {
  private static final String did = "1234123112321231";
  @Autowired
  private ServicesCardEventRepository servicesCardEventRepository;
  @Autowired
  private ServicesCardRepository servicesCardRepository;
  private EventHandlerService eventHandlerService;

  @Before
  public void before() {
    eventHandlerService = new EventHandlerService(servicesCardRepository, servicesCardEventRepository);
  }

  @After
  public void after() {
    servicesCardEventRepository.deleteAll();
    servicesCardRepository.deleteAll();
  }

  @Test
  public void testGetServicesCardEvent_WhenEventPayloadContainsValidDID_ShouldCreateADBRecordWithSERVICES_CARD_FOUND() {
    ServicesCardEntity servicesCard = getServicesCardEntity();
    servicesCardRepository.save(servicesCard);
    Event getEvent = createDummyGetServiceCardEvent(did);
    eventHandlerService.handleEvent(getEvent);
    val serviceCardEvents = servicesCardEventRepository.findAll();

    assertThat(serviceCardEvents.size() == 1).isTrue();
    val serviceCardEvent = serviceCardEvents.get(0);
    assertThat(serviceCardEvent.getEventStatus().equalsIgnoreCase(EventStatus.DB_COMMITTED.toString())).isTrue();
    assertThat(serviceCardEvent.getEventOutcome().equalsIgnoreCase(EventOutcome.SERVICES_CARD_FOUND.toString())).isTrue();
  }

  @Test
  public void testGetServicesCardEvent_WhenEventPayloadContainsInvalidDID_ShouldCreateADBRecordWithSERVICES_CARD_NOT_FOUND() {
    ServicesCardEntity servicesCard = getServicesCardEntity();
    servicesCardRepository.save(servicesCard);
    Event getEvent = createDummyGetServiceCardEvent("1212213");
    eventHandlerService.handleEvent(getEvent);
    val serviceCardEvents = servicesCardEventRepository.findAll();

    assertThat(serviceCardEvents.size() == 1).isTrue();
    val serviceCardEvent = serviceCardEvents.get(0);
    assertThat(serviceCardEvent.getEventStatus().equalsIgnoreCase(EventStatus.DB_COMMITTED.toString())).isTrue();
    assertThat(serviceCardEvent.getEventOutcome().equalsIgnoreCase(EventOutcome.SERVICES_CARD_NOT_FOUND.toString())).isTrue();
  }

  @Test
  public void testUpdateServicesCardEvent_WhenEventPayloadContainsInvalidDID_ShouldCreateADBRecordWithSERVICES_CARD_NOT_FOUND() throws JsonProcessingException {
    ServicesCardEntity servicesCard = getServicesCardEntity();
    servicesCardRepository.save(servicesCard);
    ServicesCard servicesCardStruct = ServicesCardMapper.mapper.toStructure(servicesCard);
    servicesCardStruct.setDid("23213123123");
    Event getEvent = createDummyUpdateServiceCardEvent(JsonUtil.getJsonStringFromObject(servicesCardStruct));
    eventHandlerService.handleEvent(getEvent);
    val serviceCardEvents = servicesCardEventRepository.findAll();

    assertThat(serviceCardEvents.size() == 1).isTrue();
    val serviceCardEvent = serviceCardEvents.get(0);
    assertThat(serviceCardEvent.getEventStatus().equalsIgnoreCase(EventStatus.DB_COMMITTED.toString())).isTrue();
    assertThat(serviceCardEvent.getEventOutcome().equalsIgnoreCase(EventOutcome.SERVICES_CARD_NOT_FOUND.toString())).isTrue();
  }

  @Test
  public void testUpdateServicesCardEvent_WhenEventPayloadContainsValidDID_ShouldCreateADBRecordWithSERVICES_CARD_UPDATED() throws JsonProcessingException {
    ServicesCardEntity servicesCard = getServicesCardEntity();
    servicesCardRepository.save(servicesCard);
    ServicesCard servicesCardStruct = ServicesCardMapper.mapper.toStructure(servicesCard);
    servicesCardStruct.setCity("VICTORIA");
    Event getEvent = createDummyUpdateServiceCardEvent(JsonUtil.getJsonStringFromObject(servicesCardStruct));
    eventHandlerService.handleEvent(getEvent);
    val serviceCardEvents = servicesCardEventRepository.findAll();

    assertThat(serviceCardEvents.size() == 1).isTrue();
    val serviceCardEvent = serviceCardEvents.get(0);
    assertThat(serviceCardEvent.getEventStatus().equalsIgnoreCase(EventStatus.DB_COMMITTED.toString())).isTrue();
    assertThat(serviceCardEvent.getEventOutcome().equalsIgnoreCase(EventOutcome.SERVICES_CARD_UPDATED.toString())).isTrue();
    val optionalServicesCardEntity = servicesCardRepository.findByDid(did);
    assertThat(optionalServicesCardEntity.isPresent()).isTrue();
    assertThat(optionalServicesCardEntity.get().getCity().equals("VICTORIA")).isTrue();
  }
  public Event createDummyOutBoxEvent(UUID eventId) {
    return Event.builder().eventType(EventType.SERVICES_CARD_EVENT_OUTBOX_PROCESSED).eventPayload(eventId.toString()).build();
  }

  public Event createDummyGetServiceCardEvent(String did) {
    return Event.builder()
        .sagaId(UUID.randomUUID())
        .eventType(EventType.valueOf("GET_SERVICES_CARD"))
        .eventPayload(did).build();
  }

  public Event createDummyUpdateServiceCardEvent(String payload) {
    return Event.builder()
        .sagaId(UUID.randomUUID())
        .eventType(EventType.valueOf("UPDATE_SERVICES_CARD"))
        .eventPayload(payload).build();
  }

  private ServicesCardEntity getServicesCardEntity() {
    ServicesCardEntity servicesCardEntity = new ServicesCardEntity();
    servicesCardEntity.setDigitalIdentityID(UUID.randomUUID());
    servicesCardEntity.setBirthDate(LocalDate.parse("1979-06-11"));
    servicesCardEntity.setCity("Compton");
    servicesCardEntity.setCountry("Canada");
    servicesCardEntity.setDid(did);
    servicesCardEntity.setEmail("this@sometest.com");
    servicesCardEntity.setGender("Male");
    servicesCardEntity.setIdentityAssuranceLevel("1");
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
