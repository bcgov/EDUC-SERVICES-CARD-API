package ca.bc.gov.educ.api.servicescard.health;

import io.nats.client.Connection;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ServicesCardAPICustomHealthCheck implements HealthIndicator {
  private final Connection natsConnection;

  public ServicesCardAPICustomHealthCheck(Connection natsConnection) {
    this.natsConnection = natsConnection;
  }

  @Override
  public Health getHealth(boolean includeDetails) {
    return healthCheck();
  }


  @Override
  public Health health() {
    return healthCheck();
  }

  private Health healthCheck() {
    if (this.natsConnection.getStatus() == Connection.Status.CLOSED) {
      return Health.down().withDetail("NATS", " Connection is Closed.").build();
    }
    return Health.up().build();
  }
}
