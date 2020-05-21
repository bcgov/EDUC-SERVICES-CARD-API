package ca.bc.gov.educ.api.servicescard.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Class holds all application properties
 *
 * @author Marco Villeneuve
 */
@Component
@Getter
@Setter
public class ApplicationProperties {

  @Value("${nats.streaming.server.url}")
  @Getter
  private String natsUrl;

  @Value("${nats.streaming.server.clusterId}")
  @Getter
  private String natsClusterId;
}
