package ca.bc.gov.educ.api.servicescard.controller.v1;

import ca.bc.gov.educ.api.servicescard.endpoint.v1.ServicesCardEndpoint;
import ca.bc.gov.educ.api.servicescard.exception.InvalidParameterException;
import ca.bc.gov.educ.api.servicescard.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.servicescard.exception.errors.ApiError;
import ca.bc.gov.educ.api.servicescard.mappers.v1.ServicesCardMapper;
import ca.bc.gov.educ.api.servicescard.service.v1.ServicesCardService;
import ca.bc.gov.educ.api.servicescard.struct.v1.ServicesCard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


/**
 * Student controller
 *
 * @author John Cox
 */

@RestController
@Slf4j
public class ServicesCardController implements ServicesCardEndpoint {

  @Getter(AccessLevel.PRIVATE)
  private final ServicesCardService service;

  private static final ServicesCardMapper mapper = ServicesCardMapper.mapper;

  @Autowired
  ServicesCardController(final ServicesCardService servicesCardService) {
    this.service = servicesCardService;
  }

  @Override
  public ServicesCard readServicesCard(final String servicesCardID) {
    return mapper.toStructure(this.service.retrieveServicesCard(UUID.fromString(servicesCardID)));
  }

  @Override
  public ServicesCard searchServicesCard(final String did) {
    return mapper.toStructure(this.service.searchServicesCard(did));
  }

  @Override
  public ServicesCard createServicesCard(final ServicesCard servicesCard) {
    if (servicesCard.getServicesCardInfoID() != null) {
      throw new InvalidParameterException("servicesCardInfoID");
    }
    if (LocalDate.parse(servicesCard.getBirthDate()).isAfter(LocalDate.now())) {
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Payload contains invalid data.").status(BAD_REQUEST).build();
      final List<FieldError> errors = new ArrayList<>();
      errors.add(new FieldError("servicesCard", "birthDate", servicesCard.getBirthDate(), false, null, null, "Birth date should be in past."));
      error.addValidationErrors(errors);
      throw new InvalidPayloadException(error);
    }
    return mapper.toStructure(this.service.createServicesCard(mapper.toModel(servicesCard)));
  }

  @Override
  public ServicesCard updateServicesCard(final ServicesCard servicesCard, final UUID servicesCardInfoID) {
    return mapper.toStructure(this.service.updateServicesCard(mapper.toModel(servicesCard), servicesCardInfoID));
  }

  @Override
  @Transactional
  public ResponseEntity<Void> deleteById(final UUID id) {
    this.getService().deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
