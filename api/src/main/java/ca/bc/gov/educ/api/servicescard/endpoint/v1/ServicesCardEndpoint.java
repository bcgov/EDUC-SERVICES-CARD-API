package ca.bc.gov.educ.api.servicescard.endpoint.v1;

import ca.bc.gov.educ.api.servicescard.struct.v1.ServicesCard;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static ca.bc.gov.educ.api.servicescard.constants.v1.URL.BASE_URL;
import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping(BASE_URL)
@OpenAPIDefinition(info = @Info(title = "API for Services Card CRU.", description = "This CRU API is related to services card data.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_SERVICES_CARD", "WRITE_SERVICES_CARD"})})
public interface ServicesCardEndpoint {

  @GetMapping("/{servicesCardID}")
  @PreAuthorize("hasAuthority('SCOPE_READ_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  ServicesCard readServicesCard(@PathVariable String servicesCardID);

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND.")})
  ServicesCard searchServicesCard(@RequestParam("did") String did);

  @ResponseStatus(code = CREATED)
  @PostMapping
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  ServicesCard createServicesCard(@Validated @RequestBody ServicesCard servicesCard);

  @PutMapping("/{servicesCardInfoID}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  ServicesCard updateServicesCard(@Validated @RequestBody ServicesCard servicesCard, @PathVariable UUID servicesCardInfoID);

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('DELETE_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND."), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  ResponseEntity<Void> deleteById(@PathVariable UUID id);
}
