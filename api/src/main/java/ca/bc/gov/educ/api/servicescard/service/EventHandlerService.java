package ca.bc.gov.educ.api.servicescard.service;

import ca.bc.gov.educ.api.servicescard.constants.EventOutcome;
import ca.bc.gov.educ.api.servicescard.mappers.ServicesCardMapper;
import ca.bc.gov.educ.api.servicescard.model.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.model.ServicesCardEvent;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardEventRepository;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardRepository;
import ca.bc.gov.educ.api.servicescard.struct.Event;
import ca.bc.gov.educ.api.servicescard.struct.ServicesCard;
import ca.bc.gov.educ.api.servicescard.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static ca.bc.gov.educ.api.servicescard.constants.EventStatus.DB_COMMITTED;
import static ca.bc.gov.educ.api.servicescard.constants.EventStatus.MESSAGE_PUBLISHED;
import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
public class EventHandlerService {

  public static final String NO_RECORD_SAGA_ID_EVENT_TYPE = "no record found for the saga id and event type combination, processing.";
  public static final String RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE = "record found for the saga id and event type combination, might be a duplicate or replay," +
          " just updating the db status so that it will be polled and sent back again.";
  public static final String EVENT_LOG = "event is :: {}";
  public static final String PAYLOAD_LOG = "Payload is :: ";
  @Getter(PRIVATE)
  private final ServicesCardRepository servicesCardRepository;
  private static final ServicesCardMapper mapper = ServicesCardMapper.mapper;
  @Getter(PRIVATE)
  private final ServicesCardEventRepository servicesCardEventRepository;

  @Autowired
  public EventHandlerService(final ServicesCardRepository servicesCardRepository, final ServicesCardEventRepository servicesCardEventRepository) {
    this.servicesCardRepository = servicesCardRepository;
    this.servicesCardEventRepository = servicesCardEventRepository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleEvent(Event event) {
    try {
      switch (event.getEventType()) {
        case SERVICES_CARD_EVENT_OUTBOX_PROCESSED:
          log.info("received SERVICES_CARD_EVENT_OUTBOX_PROCESSED event :: ");
          log.trace(PAYLOAD_LOG + event.getEventPayload());
          handleServicesCardOutboxProcessedEvent(event.getEventPayload());
          break;
        case UPDATE_SERVICES_CARD:
          log.info("received UPDATE_SERVICES_CARD event :: ");
          log.trace(PAYLOAD_LOG + event.getEventPayload());
          handleUpdateServicesCardEvent(event);
          break;
        case GET_SERVICES_CARD:
          log.info("received GET_SERVICES_CARD event :: ");
          log.trace(PAYLOAD_LOG + event.getEventPayload());
          handleGetServicesCardEvent(event);
          break;
        default:
          log.info("silently ignoring other events.");
          break;
      }
    } catch (final Exception e) {
      log.error("Exception", e);
    }
  }

  private void handleGetServicesCardEvent(Event event) throws JsonProcessingException {
    val optionalServicesCardEvent = getServicesCardEventRepository().findBySagaIdAndEventType(event.getSagaId(), event.getEventType().toString());
    ServicesCardEvent servicesCardEvent;
    if (!optionalServicesCardEvent.isPresent()) {
      log.info(NO_RECORD_SAGA_ID_EVENT_TYPE);
      log.trace("Event is {}", event);
      val optionalServicesCardEntity = getServicesCardRepository().findByDid(event.getEventPayload()); // expect did as a string in the payload.
      if (optionalServicesCardEntity.isPresent()) {
        val attachedEntity = optionalServicesCardEntity.get();
        event.setEventPayload(JsonUtil.getJsonStringFromObject(mapper.toStructure(attachedEntity))); //update the event with payload, need to convert to structure MANDATORY otherwise jackson will break.
        event.setEventOutcome(EventOutcome.SERVICES_CARD_FOUND);
      } else {
        event.setEventOutcome(EventOutcome.SERVICES_CARD_NOT_FOUND);
      }
      servicesCardEvent = createServicesCardEvent(event);
    } else {
      log.info(RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE);
      log.trace(EVENT_LOG, event);
      servicesCardEvent = optionalServicesCardEvent.get();
      servicesCardEvent.setEventStatus(DB_COMMITTED.toString());
    }

    getServicesCardEventRepository().save(servicesCardEvent);
  }

  private void handleUpdateServicesCardEvent(Event event) throws JsonProcessingException {
    val optionalServicesCardEvent = getServicesCardEventRepository().findBySagaIdAndEventType(event.getSagaId(), event.getEventType().toString());
    ServicesCardEvent servicesCardEvent;
    if (!optionalServicesCardEvent.isPresent()) {
      log.info(NO_RECORD_SAGA_ID_EVENT_TYPE);
      log.trace(EVENT_LOG, event);
      ServicesCardEntity entity = mapper.toModel(JsonUtil.getJsonObjectFromString(ServicesCard.class, event.getEventPayload()));
      val optionalServicesCardEntity = getServicesCardRepository().findByDid(entity.getDid());
      if (optionalServicesCardEntity.isPresent()) {
        val attachedEntity = optionalServicesCardEntity.get();
        BeanUtils.copyProperties(entity, attachedEntity);
        attachedEntity.setUpdateDate(LocalDateTime.now());
        getServicesCardRepository().save(attachedEntity);
        event.setEventPayload(JsonUtil.getJsonStringFromObject(mapper.toStructure(attachedEntity)));// need to convert to structure MANDATORY otherwise jackson will break.
        event.setEventOutcome(EventOutcome.SERVICES_CARD_UPDATED);
      } else {
        event.setEventOutcome(EventOutcome.SERVICES_CARD_NOT_FOUND);
      }
      servicesCardEvent = createServicesCardEvent(event);
    } else {
      log.info(RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE);
      log.trace(EVENT_LOG, event);
      servicesCardEvent = optionalServicesCardEvent.get();
      servicesCardEvent.setEventStatus(DB_COMMITTED.toString());
    }

    getServicesCardEventRepository().save(servicesCardEvent);
  }

  private void handleServicesCardOutboxProcessedEvent(String servicesCardEventId) {
    val optionalServicesCardEvent = getServicesCardEventRepository().findById(UUID.fromString(servicesCardEventId));
    if (optionalServicesCardEvent.isPresent()) {
      val servicesCardEvent = optionalServicesCardEvent.get();
      servicesCardEvent.setEventStatus(MESSAGE_PUBLISHED.toString());
      getServicesCardEventRepository().save(servicesCardEvent);
    }
  }


  private ServicesCardEvent createServicesCardEvent(Event event) {
    return ServicesCardEvent.builder()
            .createDate(LocalDateTime.now())
            .updateDate(LocalDateTime.now())
            .createUser(event.getEventType().toString()) //need to discuss what to put here.
            .updateUser(event.getEventType().toString())
            .eventPayload(event.getEventPayload())
            .eventType(event.getEventType().toString())
            .sagaId(event.getSagaId())
            .eventStatus(DB_COMMITTED.toString())
            .eventOutcome(event.getEventOutcome().toString())
            .replyChannel(event.getReplyTo())
            .build();
  }
}
