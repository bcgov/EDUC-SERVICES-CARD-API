package ca.bc.gov.educ.api.servicescard.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ca.bc.gov.educ.api.servicescard.struct.ServicesCard;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping("/")
@OpenAPIDefinition(info = @Info(title = "API for Services Card CRU.", description = "This CRU API is related to services card data.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_SERVICES_CARD", "WRITE_SERVICES_CARD"})})
public interface ServicesCardEndpoint {

  @GetMapping("/{servicesCardID}")
  @PreAuthorize("#oauth2.hasScope('READ_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  ServicesCard readServicesCard(@PathVariable String servicesCardID);

  @GetMapping
  @PreAuthorize("#oauth2.hasScope('READ_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND.")})
  ServicesCard searchServicesCard(@RequestParam("did") String did);

  @ResponseStatus(code = CREATED)
  @PostMapping
  @PreAuthorize("#oauth2.hasAnyScope('WRITE_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  ServicesCard createServicesCard(@Validated @RequestBody ServicesCard servicesCard);

  @PutMapping
  @PreAuthorize("#oauth2.hasAnyScope('WRITE_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  ServicesCard updateServicesCard(@Validated @RequestBody ServicesCard servicesCard);

  @DeleteMapping
  @PreAuthorize("#oauth2.hasScope('DELETE_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  ResponseEntity<Void> deleteAll();

  @DeleteMapping("/{id}")
  @PreAuthorize("#oauth2.hasScope('DELETE_SERVICES_CARD')")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"),  @ApiResponse(responseCode = "404", description = "NOT FOUND."), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  ResponseEntity<Void> deleteById(@PathVariable UUID id);
}
