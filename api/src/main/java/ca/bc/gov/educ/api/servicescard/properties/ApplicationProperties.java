package ca.bc.gov.educ.api.servicescard.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class holds all application properties
 *
 * @author Marco Villeneuve
 */
@Component
@Getter
@Setter
public final class ApplicationProperties {
  public static final String CORRELATION_ID = "correlationID";
  @Value("${nats.server}")
  private String server;

 @Value("${nats.maxReconnect}")
 private int maxReconnect;

 @Value("${nats.connectionName}")
 private String connectionName;
}
