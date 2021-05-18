package ca.bc.gov.educ.api.servicescard.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ServicesCardRequestInterceptor implements AsyncHandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(ServicesCardRequestInterceptor.class);

  /**
   * Pre handle boolean.
   *
   * @param request  the request
   * @param response the response
   * @param handler  the handler
   * @return the boolean
   */
  @Override
  public boolean preHandle(final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final Object handler) {
    if (request.getMethod() != null && request.getRequestURL() != null) {
      log.info("{} {}", request.getMethod(), request.getRequestURL());
    }
    if (request.getQueryString() != null) {
      log.debug("Query string     : {}", request.getQueryString());
    }
    return true;
  }

  /**
   * After completion.
   *
   * @param request  the request
   * @param response the response
   * @param handler  the handler
   * @param ex       the ex
   */
  @Override
  public void afterCompletion(@NonNull final HttpServletRequest request, final HttpServletResponse response, @NonNull final Object handler, final Exception ex) {
    final int status = response.getStatus();
    if (status >= 200 && status < 300) {
      log.info("RESPONSE STATUS: {}", status);
    } else {
      log.error("RESPONSE STATUS: {}", status);
    }
  }
}
