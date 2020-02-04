package ca.bc.gov.educ.api.servicescard.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.educ.api.servicescard.endpoint.ServicesCardEndpoint;
import ca.bc.gov.educ.api.servicescard.mappers.ServicesCardMapper;
import ca.bc.gov.educ.api.servicescard.service.ServicesCardService;
import ca.bc.gov.educ.api.servicescard.struct.ServicesCard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * Student controller
 *
 * @author John Cox
 */

@RestController
@EnableResourceServer
@Slf4j
public class ServicesCardController implements ServicesCardEndpoint {

  @Getter(AccessLevel.PRIVATE)
  private final ServicesCardService service;

  @Getter(AccessLevel.PRIVATE)
  private final ServicesCardMapper mapper = ServicesCardMapper.mapper;

  @Autowired
  ServicesCardController(final ServicesCardService servicesCardService) {
    this.service = servicesCardService;
  }

  public ServicesCard readServicesCard(String servicesCardID) {
    return mapper.toStructure(service.retrieveServicesCard(UUID.fromString(servicesCardID)));
  }

  @ResponseStatus(code = CREATED)
  public ServicesCard createServicesCard(ServicesCard servicesCard) {
    return mapper.toStructure(service.createServicesCard(mapper.toModel(servicesCard)));
  }

  public ServicesCard updateServicesCard(ServicesCard servicesCard) {
    return mapper.toStructure(service.updateServicesCard(mapper.toModel(servicesCard)));
  }

  @Override
  public String health() {
    log.info("Health Check OK, returning OK");
    return "OK";
  }
}
