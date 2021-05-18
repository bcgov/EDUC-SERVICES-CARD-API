package ca.bc.gov.educ.api.servicescard.service.v1;

import ca.bc.gov.educ.api.servicescard.messaging.MessagePublisher;
import ca.bc.gov.educ.api.servicescard.struct.Event;
import io.nats.client.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventHandlerDelegatorService {

  public static final String PAYLOAD_LOG = "Payload is :: ";
  public static final String RESPONDING_BACK_TO_NATS_ON_CHANNEL = "responding back to NATS on {} channel ";
  private final EventHandlerService eventHandlerService;
  private final MessagePublisher messagePublisher;

  @Autowired
  public EventHandlerDelegatorService(final EventHandlerService eventHandlerService, final MessagePublisher messagePublisher) {
    this.eventHandlerService = eventHandlerService;
    this.messagePublisher = messagePublisher;
  }

  @Async("subscriberExecutor")
  public void handleEvent(final Message message, final Event event) {
    final byte[] response;
    final boolean isSynchronous = message.getReplyTo() != null;
    try {
      switch (event.getEventType()) {
        case UPDATE_SERVICES_CARD:
          log.info("received UPDATE_SERVICES_CARD event :: ");
          log.trace(PAYLOAD_LOG + event.getEventPayload());
          response = this.eventHandlerService.handleUpdateServicesCardEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, isSynchronous ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, response);
          break;
        case GET_SERVICES_CARD:
          log.info("received GET_SERVICES_CARD event :: ");
          log.trace(PAYLOAD_LOG + event.getEventPayload());
          response = this.eventHandlerService.handleGetServicesCardEvent(event);
          log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, isSynchronous ? message.getReplyTo() : event.getReplyTo());
          publishToNATS(event, message, isSynchronous, response);
          break;
        default:
          log.info("silently ignoring other events.");
          break;
      }
    } catch (final Exception e) {
      log.error("Exception", e);
    }
  }

  private void publishToNATS(Event event, Message message, boolean isSynchronous, byte[] response) {
    if (isSynchronous) { // sync, req/reply pattern of nats
      messagePublisher.dispatchMessage(message.getReplyTo(), response);
    } else { // async, pub/sub
      messagePublisher.dispatchMessage(event.getReplyTo(), response);
    }
  }

}
