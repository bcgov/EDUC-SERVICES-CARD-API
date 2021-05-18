package ca.bc.gov.educ.api.servicescard.service.v1;

import ca.bc.gov.educ.api.servicescard.constants.EventOutcome;
import ca.bc.gov.educ.api.servicescard.constants.EventType;
import ca.bc.gov.educ.api.servicescard.mappers.v1.ServicesCardMapper;
import ca.bc.gov.educ.api.servicescard.model.v1.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.model.v1.ServicesCardEvent;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardEventRepository;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardRepository;
import ca.bc.gov.educ.api.servicescard.struct.Event;
import ca.bc.gov.educ.api.servicescard.struct.v1.ServicesCard;
import ca.bc.gov.educ.api.servicescard.utils.JsonUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static ca.bc.gov.educ.api.servicescard.constants.EventStatus.MESSAGE_PUBLISHED;
import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
public class EventHandlerService {

  private static final ServicesCardMapper mapper = ServicesCardMapper.mapper;
  @Getter(PRIVATE)
  private final ServicesCardRepository servicesCardRepository;
  @Getter(PRIVATE)
  private final ServicesCardEventRepository servicesCardEventRepository;

  @Autowired
  public EventHandlerService(final ServicesCardRepository servicesCardRepository, final ServicesCardEventRepository servicesCardEventRepository) {
    this.servicesCardRepository = servicesCardRepository;
    this.servicesCardEventRepository = servicesCardEventRepository;
  }


  public byte[] handleGetServicesCardEvent(final Event event) {
    val optionalServicesCardEntity = this.getServicesCardRepository().findByDid(event.getEventPayload()); // expect did as a string in the payload.
    if (optionalServicesCardEntity.isPresent()) {
      val attachedEntity = optionalServicesCardEntity.get();
      event.setEventPayload(JsonUtil.getJsonStringFromObject(mapper.toStructure(attachedEntity))); //update the event with payload, need to convert to structure MANDATORY otherwise jackson will break.
      event.setEventOutcome(EventOutcome.SERVICES_CARD_FOUND);
    } else {
      event.setEventOutcome(EventOutcome.SERVICES_CARD_NOT_FOUND);
    }
    val servicesCardEvent = this.createServicesCardEvent(event);
    return this.createResponseEvent(servicesCardEvent);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public byte[] handleUpdateServicesCardEvent(final Event event) {
    final ServicesCardEntity entity = mapper.toModel(JsonUtil.getJsonObjectFromString(ServicesCard.class, event.getEventPayload()));
    val optionalServicesCardEntity = this.getServicesCardRepository().findByDid(entity.getDid());
    if (optionalServicesCardEntity.isPresent()) {
      val attachedEntity = optionalServicesCardEntity.get();
      BeanUtils.copyProperties(entity, attachedEntity);
      attachedEntity.setUpdateDate(LocalDateTime.now());
      this.getServicesCardRepository().save(attachedEntity);
      event.setEventPayload(JsonUtil.getJsonStringFromObject(mapper.toStructure(attachedEntity)));// need to convert to structure MANDATORY otherwise jackson will break.
      event.setEventOutcome(EventOutcome.SERVICES_CARD_UPDATED);
    } else {
      event.setEventOutcome(EventOutcome.SERVICES_CARD_NOT_FOUND);
    }
    val servicesCardEvent = this.createServicesCardEvent(event);
    return this.createResponseEvent(servicesCardEvent);
  }


  private ServicesCardEvent createServicesCardEvent(final Event event) {
    return ServicesCardEvent.builder()
      .createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now())
      .createUser(event.getEventType().toString()) //need to discuss what to put here.
      .updateUser(event.getEventType().toString())
      .eventPayload(event.getEventPayload())
      .eventType(event.getEventType().toString())
      .sagaId(event.getSagaId())
      .eventStatus(MESSAGE_PUBLISHED.toString())
      .eventOutcome(event.getEventOutcome().toString())
      .replyChannel(event.getReplyTo())
      .build();
  }

  private byte[] createResponseEvent(final ServicesCardEvent event) {
    val responseEvent = Event.builder()
      .sagaId(event.getSagaId())
      .eventType(EventType.valueOf(event.getEventType()))
      .eventOutcome(EventOutcome.valueOf(event.getEventOutcome()))
      .eventPayload(event.getEventPayload()).build();
    return JsonUtil.getJsonBytesFromObject(responseEvent);
  }
}
