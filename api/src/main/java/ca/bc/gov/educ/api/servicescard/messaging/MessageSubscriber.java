package ca.bc.gov.educ.api.servicescard.messaging;

import ca.bc.gov.educ.api.servicescard.service.v1.EventHandlerService;
import ca.bc.gov.educ.api.servicescard.struct.Event;
import ca.bc.gov.educ.api.servicescard.utils.JsonUtil;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static ca.bc.gov.educ.api.servicescard.constants.Topics.SERVICES_CARD_API_TOPIC;


@Component
@Slf4j
public class MessageSubscriber {

  private final EventHandlerService eventHandlerService;
  private final Connection connection;

  @Autowired
  public MessageSubscriber(final Connection con, EventHandlerService eventHandlerService) {
    this.eventHandlerService = eventHandlerService;
    this.connection = con;
  }

  /**
   * This subscription will makes sure the messages are required to acknowledge manually to STAN.
   * Subscribe.
   */
  @PostConstruct
  public void subscribe() {
    String queue = SERVICES_CARD_API_TOPIC.toString().replace("_", "-");
    var dispatcher = connection.createDispatcher(onMessage());
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
          var eventString = new String(message.getData());
          var event = JsonUtil.getJsonObjectFromString(Event.class, eventString);
          eventHandlerService.handleEvent(event);
          log.debug("Event is :: {}", event);
        } catch (final Exception e) {
          log.error("Exception ", e);
        }
      }
    };
  }


}
