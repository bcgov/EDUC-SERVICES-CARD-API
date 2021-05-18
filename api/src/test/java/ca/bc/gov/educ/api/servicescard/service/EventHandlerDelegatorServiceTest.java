package ca.bc.gov.educ.api.servicescard.service;

import ca.bc.gov.educ.api.servicescard.constants.EventOutcome;
import ca.bc.gov.educ.api.servicescard.constants.EventType;
import ca.bc.gov.educ.api.servicescard.mappers.v1.ServicesCardMapper;
import ca.bc.gov.educ.api.servicescard.messaging.MessagePublisher;
import ca.bc.gov.educ.api.servicescard.model.v1.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardEventRepository;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardRepository;
import ca.bc.gov.educ.api.servicescard.service.v1.EventHandlerDelegatorService;
import ca.bc.gov.educ.api.servicescard.struct.Event;
import ca.bc.gov.educ.api.servicescard.struct.v1.ServicesCard;
import ca.bc.gov.educ.api.servicescard.support.NatsMessageImpl;
import ca.bc.gov.educ.api.servicescard.utils.JsonUtil;
import lombok.val;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventHandlerDelegatorServiceTest {
  private static final String did = "1234123112321231";
  public static final String REPLY_CHANNEL = "replyChannel";
  @Autowired
  private ServicesCardEventRepository servicesCardEventRepository;
  @Autowired
  private ServicesCardRepository servicesCardRepository;

  @Autowired
  MessagePublisher messagePublisher;
  @Captor
  ArgumentCaptor<byte[]> responseEventCaptor;
  @Captor
  ArgumentCaptor<String> subjectCaptor;
  @Autowired
  private EventHandlerDelegatorService eventHandlerDelegatorService;

  @After
  public void after() {
    Mockito.reset(messagePublisher);
    this.servicesCardEventRepository.deleteAll();
    this.servicesCardRepository.deleteAll();
  }

  @Test
  public void testGetServicesCardEvent_WhenEventPayloadContainsValidDID_ShouldCreateADBRecordWithSERVICES_CARD_FOUND() {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    this.servicesCardRepository.save(servicesCard);
    final Event getEvent = this.createDummyGetServiceCardEvent(did);
    this.eventHandlerDelegatorService.handleEvent(NatsMessageImpl.builder().build(), getEvent);
    Mockito.verify(messagePublisher).dispatchMessage(subjectCaptor.capture(), responseEventCaptor.capture());
    final String subject = subjectCaptor.getValue();
    assertThat(subject).isEqualTo(REPLY_CHANNEL);
    val responseEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(responseEventCaptor.getValue()));
    assertThat(responseEvent.getEventPayload()).isNotBlank();
    assertThat(responseEvent.getEventOutcome()).isEqualTo(EventOutcome.SERVICES_CARD_FOUND);
  }

  @Test
  public void testGetServicesCardEvent_whenEventPayloadContainsValidDID_ShouldCreateADBRecordWithSERVICES_CARD_FOUND() {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    this.servicesCardRepository.save(servicesCard);
    final Event getEvent = this.createDummyGetServiceCardEvent(did);
    this.eventHandlerDelegatorService.handleEvent(NatsMessageImpl.builder().replyTo("synChannel").build(), getEvent);
    Mockito.verify(messagePublisher).dispatchMessage(subjectCaptor.capture(), responseEventCaptor.capture());
    final String subject = subjectCaptor.getValue();
    assertThat(subject).isEqualTo("synChannel");
    val responseEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(responseEventCaptor.getValue()));
    assertThat(responseEvent.getEventPayload()).isNotBlank();
    assertThat(responseEvent.getEventOutcome()).isEqualTo(EventOutcome.SERVICES_CARD_FOUND);
  }

  @Test
  public void testGetServicesCardEvent_WhenEventPayloadContainsInvalidDID_ShouldCreateADBRecordWithSERVICES_CARD_NOT_FOUND() {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    this.servicesCardRepository.save(servicesCard);
    final Event getEvent = this.createDummyGetServiceCardEvent("1212213");
    this.eventHandlerDelegatorService.handleEvent(NatsMessageImpl.builder().build(), getEvent);

    Mockito.verify(messagePublisher).dispatchMessage(subjectCaptor.capture(), responseEventCaptor.capture());
    final String subject = subjectCaptor.getValue();
    assertThat(subject).isEqualTo(REPLY_CHANNEL);
    val responseEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(responseEventCaptor.getValue()));
    assertThat(responseEvent.getEventPayload()).isNotBlank();
    assertThat(responseEvent.getEventOutcome()).isEqualTo(EventOutcome.SERVICES_CARD_NOT_FOUND);
  }

  @Test
  public void testUpdateServicesCardEvent_WhenEventPayloadContainsInvalidDID_ShouldCreateADBRecordWithSERVICES_CARD_NOT_FOUND() {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    this.servicesCardRepository.save(servicesCard);
    final ServicesCard servicesCardStruct = ServicesCardMapper.mapper.toStructure(servicesCard);
    servicesCardStruct.setDid("23213123123");
    final Event getEvent = this.createDummyUpdateServiceCardEvent(JsonUtil.getJsonStringFromObject(servicesCardStruct));
    this.eventHandlerDelegatorService.handleEvent(NatsMessageImpl.builder().build(), getEvent);
    Mockito.verify(messagePublisher).dispatchMessage(subjectCaptor.capture(), responseEventCaptor.capture());
    final String subject = subjectCaptor.getValue();
    assertThat(subject).isEqualTo(REPLY_CHANNEL);
    val responseEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(responseEventCaptor.getValue()));
    assertThat(responseEvent.getEventPayload()).isNotBlank();
    assertThat(responseEvent.getEventOutcome()).isEqualTo(EventOutcome.SERVICES_CARD_NOT_FOUND);
  }

  @Test
  public void testUpdateServicesCardEvent_WhenEventPayloadContainsInvalidDIDSyncCall_ShouldCreateADBRecordWithSERVICES_CARD_NOT_FOUND() {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    this.servicesCardRepository.save(servicesCard);
    final ServicesCard servicesCardStruct = ServicesCardMapper.mapper.toStructure(servicesCard);
    servicesCardStruct.setDid("23213123123");
    final Event getEvent = this.createDummyUpdateServiceCardEvent(JsonUtil.getJsonStringFromObject(servicesCardStruct));
    this.eventHandlerDelegatorService.handleEvent(NatsMessageImpl.builder().replyTo(REPLY_CHANNEL).build(), getEvent);
    Mockito.verify(messagePublisher).dispatchMessage(subjectCaptor.capture(), responseEventCaptor.capture());
    final String subject = subjectCaptor.getValue();
    assertThat(subject).isEqualTo(REPLY_CHANNEL);
    val responseEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(responseEventCaptor.getValue()));
    assertThat(responseEvent.getEventPayload()).isNotBlank();
    assertThat(responseEvent.getEventOutcome()).isEqualTo(EventOutcome.SERVICES_CARD_NOT_FOUND);
  }

  @Test
  public void testUpdateServicesCardEvent_WhenEventPayloadContainsValidDID_ShouldCreateADBRecordWithSERVICES_CARD_UPDATED() {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    this.servicesCardRepository.save(servicesCard);
    final ServicesCard servicesCardStruct = ServicesCardMapper.mapper.toStructure(servicesCard);
    servicesCardStruct.setCity("VICTORIA");
    final Event getEvent = this.createDummyUpdateServiceCardEvent(JsonUtil.getJsonStringFromObject(servicesCardStruct));
    this.eventHandlerDelegatorService.handleEvent(NatsMessageImpl.builder().build(), getEvent);

    Mockito.verify(messagePublisher).dispatchMessage(subjectCaptor.capture(), responseEventCaptor.capture());
    final String subject = subjectCaptor.getValue();
    assertThat(subject).isEqualTo(REPLY_CHANNEL);
    val responseEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(responseEventCaptor.getValue()));
    assertThat(responseEvent.getEventPayload()).isNotBlank();
    assertThat(responseEvent.getEventOutcome()).isEqualTo(EventOutcome.SERVICES_CARD_UPDATED);
  }

  public Event createDummyOutBoxEvent(final UUID eventId) {
    return Event.builder().eventType(EventType.SERVICES_CARD_EVENT_OUTBOX_PROCESSED).eventPayload(eventId.toString()).build();
  }

  public Event createDummyGetServiceCardEvent(final String did) {
    return Event.builder()
      .sagaId(UUID.randomUUID())
      .replyTo(REPLY_CHANNEL)
      .eventType(EventType.valueOf("GET_SERVICES_CARD"))
      .eventPayload(did).build();
  }

  public Event createDummyUpdateServiceCardEvent(final String payload) {
    return Event.builder()
      .sagaId(UUID.randomUUID())
      .replyTo(REPLY_CHANNEL)
      .eventType(EventType.valueOf("UPDATE_SERVICES_CARD"))
      .eventPayload(payload).build();
  }

  private ServicesCardEntity getServicesCardEntity() {
    final ServicesCardEntity servicesCardEntity = new ServicesCardEntity();
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
