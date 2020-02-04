package ca.bc.gov.educ.api.student.endpoint;

import ca.bc.gov.educ.api.student.struct.Student;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/")
@OpenAPIDefinition(info = @Info(title = "API for Student CRU.", description = "This CRU API is related to student data.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_STUDENT", "WRITE_STUDENT"})})
public interface StudentEndpoint {

  @GetMapping("/{studentID}")
  @PreAuthorize("#oauth2.hasScope('READ_STUDENT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  Student readStudent(@PathVariable String studentID);

  @PostMapping
  @PreAuthorize("#oauth2.hasAnyScope('WRITE_STUDENT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  Student createStudent(@Validated @RequestBody Student student);

  @PutMapping
  @PreAuthorize("#oauth2.hasAnyScope('WRITE_STUDENT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  Student updateStudent(@Validated @RequestBody Student student);

  @GetMapping("/health")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  String health();
}
