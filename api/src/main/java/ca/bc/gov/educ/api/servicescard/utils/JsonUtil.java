package ca.bc.gov.educ.api.servicescard.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonUtil {
  private static final ObjectMapper mapper = new ObjectMapper();

  private JsonUtil() {
  }

  @SneakyThrows(JsonProcessingException.class)
  public static String getJsonStringFromObject(Object payload) {
    return mapper.writeValueAsString(payload);
  }

  @SneakyThrows(JsonProcessingException.class)
  public static <T> T getJsonObjectFromString(Class<T> clazz, String payload) {
    return mapper.readValue(payload, clazz);
  }

  @SneakyThrows(JsonProcessingException.class)
  public static byte[] getJsonBytesFromObject(Object payload) {
    return mapper.writeValueAsBytes(payload);
  }
}
