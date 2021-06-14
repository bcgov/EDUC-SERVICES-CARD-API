package ca.bc.gov.educ.api.servicescard.messaging;

import ca.bc.gov.educ.api.servicescard.helpers.LogHelper;
import ca.bc.gov.educ.api.servicescard.service.v1.EventHandlerDelegatorService;
import ca.bc.gov.educ.api.servicescard.struct.Event;
import ca.bc.gov.educ.api.servicescard.utils.JsonUtil;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static ca.bc.gov.educ.api.servicescard.constants.Topics.SERVICES_CARD_API_TOPIC;


@Component
@Slf4j
public class MessageSubscriber {

  private final EventHandlerDelegatorService eventHandlerDelegatorService;
  private final Connection connection;

  @Autowired
  public MessageSubscriber(final Connection con, final EventHandlerDelegatorService eventHandlerService) {
    this.eventHandlerDelegatorService = eventHandlerService;
    this.connection = con;
  }

  /**
   * This subscription will makes sure the messages are required to acknowledge manually to STAN.
   * Subscribe.
   */
  @PostConstruct
  public void subscribe() {
    final String queue = SERVICES_CARD_API_TOPIC.toString().replace("_", "-");
    final var dispatcher = this.connection.createDispatcher(this.onMessage());
    dispatcher.subscribe(SERVICES_CARD_API_TOPIC.toString(), queue);
  }

  /**
   * On message message handler.
   *
   * @return the message handler
   */
  private MessageHandler onMessage() {
    return (Message message) -> {
      if (message != null) {
        log.info("Message received is :: {} ", message);
        try {
          val eventString = new String(message.getData());
          LogHelper.logMessagingEventDetails(eventString);
          val event = JsonUtil.getJsonObjectFromString(Event.class, eventString);
          this.eventHandlerDelegatorService.handleEvent(message, event);
          log.debug("Event is :: {}", event);
        } catch (final Exception e) {
          log.error("Exception ", e);
        }
      }
    };
  }


}
