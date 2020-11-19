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
    private final ServicesCardRequestInterceptor servicesCardRequestInterceptor;

    @Autowired
    public ServicesCardMVCConfig(final ServicesCardRequestInterceptor servicesCardRequestInterceptor){
        this.servicesCardRequestInterceptor = servicesCardRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(servicesCardRequestInterceptor).addPathPatterns("/**");
    }
}
