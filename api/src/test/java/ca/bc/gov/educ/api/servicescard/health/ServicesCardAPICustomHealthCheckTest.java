package ca.bc.gov.educ.api.servicescard.health;

import io.nats.client.Connection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ServicesCardAPICustomHealthCheckTest {

  @Autowired
  Connection natsConnection;

  @Autowired
  private ServicesCardAPICustomHealthCheck servicesCardAPICustomHealthCheck;

  @Test
  public void testGetHealth_givenClosedNatsConnection_shouldReturnStatusDown() {
    when(natsConnection.getStatus()).thenReturn(Connection.Status.CLOSED);
    assertThat(servicesCardAPICustomHealthCheck.getHealth(true)).isNotNull();
    assertThat(servicesCardAPICustomHealthCheck.getHealth(true).getStatus()).isEqualTo(Status.DOWN);
  }

  @Test
  public void testGetHealth_givenOpenNatsConnection_shouldReturnStatusUp() {
    when(natsConnection.getStatus()).thenReturn(Connection.Status.CONNECTED);
    assertThat(servicesCardAPICustomHealthCheck.getHealth(true)).isNotNull();
    assertThat(servicesCardAPICustomHealthCheck.getHealth(true).getStatus()).isEqualTo(Status.UP);
  }


  @Test
  public void testHealth_givenClosedNatsConnection_shouldReturnStatusDown() {
    when(natsConnection.getStatus()).thenReturn(Connection.Status.CLOSED);
    assertThat(servicesCardAPICustomHealthCheck.health()).isNotNull();
    assertThat(servicesCardAPICustomHealthCheck.health().getStatus()).isEqualTo(Status.DOWN);
  }

  @Test
  public void testHealth_givenOpenNatsConnection_shouldReturnStatusUp() {
    when(natsConnection.getStatus()).thenReturn(Connection.Status.CONNECTED);
    assertThat(servicesCardAPICustomHealthCheck.health()).isNotNull();
    assertThat(servicesCardAPICustomHealthCheck.health().getStatus()).isEqualTo(Status.UP);
  }
}
