package ca.bc.gov.educ.api.servicescard.poll;

import ca.bc.gov.educ.api.servicescard.constants.EventOutcome;
import ca.bc.gov.educ.api.servicescard.constants.EventType;
import ca.bc.gov.educ.api.servicescard.messaging.MessagePublisher;
import ca.bc.gov.educ.api.servicescard.model.v1.ServicesCardEvent;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardEventRepository;
import ca.bc.gov.educ.api.servicescard.struct.Event;
import ca.bc.gov.educ.api.servicescard.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static ca.bc.gov.educ.api.servicescard.constants.EventStatus.DB_COMMITTED;
import static ca.bc.gov.educ.api.servicescard.constants.Topics.SERVICES_CARD_API_TOPIC;
import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
public class EventTaskScheduler {

  @Getter(PRIVATE)
  private final MessagePublisher messagePublisher;
  @Getter(PRIVATE)
  private final ServicesCardEventRepository servicesCardEventRepository;

  @Autowired
  public EventTaskScheduler(MessagePublisher messagePublisher, ServicesCardEventRepository servicesCardEventRepository) {
    this.messagePublisher = messagePublisher;
    this.servicesCardEventRepository = servicesCardEventRepository;
  }

 // @Scheduled(cron = "0/1 * * * * *")
  @SchedulerLock(name = "EventTablePoller",
          lockAtLeastFor = "900ms", lockAtMostFor = "950ms")
  public void pollEventTableAndPublish() throws IOException {
    List<ServicesCardEvent> events = getServicesCardEventRepository().findByEventStatus(DB_COMMITTED.toString());
    if (!events.isEmpty()) {
      for (ServicesCardEvent event : events) {
        try {
          if (event.getReplyChannel() != null) {
            getMessagePublisher().dispatchMessage(event.getReplyChannel(), servicesCardEventProcessed(event));
          }
          getMessagePublisher().dispatchMessage(SERVICES_CARD_API_TOPIC.toString(), createOutboxEvent(event));
        } catch (IOException e) {
          log.error("exception occurred", e);
          throw e;
        }
      }
    } else {
      log.trace("no unprocessed records.");
    }
  }

  private byte[] servicesCardEventProcessed(ServicesCardEvent servicesCardEvent) throws JsonProcessingException {
    val event = Event.builder()
      .sagaId(servicesCardEvent.getSagaId())
      .eventType(EventType.valueOf(servicesCardEvent.getEventType()))
      .eventOutcome(EventOutcome.valueOf(servicesCardEvent.getEventOutcome()))
      .eventPayload(servicesCardEvent.getEventPayload()).build();
    return JsonUtil.getJsonStringFromObject(event).getBytes();
  }

  private byte[] createOutboxEvent(ServicesCardEvent servicesCardEvent) throws JsonProcessingException {
    val event = Event.builder().eventType(EventType.SERVICES_CARD_EVENT_OUTBOX_PROCESSED).eventPayload(servicesCardEvent.getEventId().toString()).build();
    return JsonUtil.getJsonStringFromObject(event).getBytes();
  }
}
