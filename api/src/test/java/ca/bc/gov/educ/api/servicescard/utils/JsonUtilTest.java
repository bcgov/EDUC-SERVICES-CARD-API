package ca.bc.gov.educ.api.servicescard.utils;

import ca.bc.gov.educ.api.servicescard.struct.v1.ServicesCard;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilTest {

  @Test
  public void getJsonStringFromObject() {
    ServicesCard servicesCard = new ServicesCard();
    servicesCard.setCity("test");
    assertThat(JsonUtil.getJsonStringFromObject(servicesCard)).isNotEmpty();
  }

  @Test
  public void getJsonObjectFromString() {
    ServicesCard servicesCard = new ServicesCard();
    servicesCard.setCity("test");
    assertThat(JsonUtil.getJsonObjectFromString(ServicesCard.class, JsonUtil.getJsonStringFromObject(servicesCard))).isNotNull();
  }

  @Test
  public void getJsonBytesFromObject() {
    ServicesCard servicesCard = new ServicesCard();
    servicesCard.setCity("test");
    assertThat(JsonUtil.getJsonBytesFromObject(servicesCard)).isNotEmpty();
  }

  @Test
  public void getJsonBytesFromObjectThrowJsonProcessingException() {
    ServicesCard servicesCard = new ServicesCard();
    servicesCard.setCity("test");
    assertThat(JsonUtil.getJsonBytesFromObject(servicesCard)).isNotEmpty();
  }
}
