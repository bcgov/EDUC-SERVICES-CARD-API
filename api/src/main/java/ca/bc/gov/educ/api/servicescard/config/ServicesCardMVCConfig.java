package ca.bc.gov.educ.api.servicescard.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ServicesCardMVCConfig implements WebMvcConfigurer {

  @Getter(AccessLevel.PRIVATE)
  private final RequestResponseInterceptor requestResponseInterceptor;

  @Autowired
  public ServicesCardMVCConfig(final RequestResponseInterceptor requestResponseInterceptor) {
    this.requestResponseInterceptor = requestResponseInterceptor;
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(this.requestResponseInterceptor).addPathPatterns("/**");
  }
}
