package ca.bc.gov.educ.api.student.controller;

import ca.bc.gov.educ.api.student.endpoint.StudentEndpoint;
import ca.bc.gov.educ.api.student.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.student.exception.errors.ApiError;
import ca.bc.gov.educ.api.student.mappers.StudentMapper;
import ca.bc.gov.educ.api.student.service.StudentService;
import ca.bc.gov.educ.api.student.struct.Student;
import ca.bc.gov.educ.api.student.validator.StudentPayloadValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;


/**
 * Student controller
 *
 * @author John Cox
 */

@RestController
@EnableResourceServer
@Slf4j
public class StudentController implements StudentEndpoint {

  @Getter(AccessLevel.PRIVATE)
  private final StudentService service;

  @Getter(AccessLevel.PRIVATE)
  private final StudentPayloadValidator payloadValidator;
  private final StudentMapper mapper = StudentMapper.mapper;

  @Autowired
  StudentController(final StudentService studentService, StudentPayloadValidator payloadValidator) {
    this.service = studentService;
    this.payloadValidator = payloadValidator;
  }

  public Student readStudent(String studentID) {
    return mapper.toStructure(service.retrieveStudent(UUID.fromString(studentID)));
  }

  @ResponseStatus(code = CREATED)
  public Student createStudent(Student student) {
    val validationResult = getPayloadValidator().validatePayload(student, true);
    if (!validationResult.isEmpty()) {
      ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Payload contains invalid data.").status(BAD_REQUEST).build();
      error.addValidationErrors(validationResult);
      throw new InvalidPayloadException(error);
    }
    return mapper.toStructure(service.createStudent(mapper.toModel(student)));
  }

  public Student updateStudent(Student student) {
    val validationResult = getPayloadValidator().validatePayload(student, false);
    if (!validationResult.isEmpty()) {
      ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Payload contains invalid data.").status(BAD_REQUEST).build();
      error.addValidationErrors(validationResult);
      throw new InvalidPayloadException(error);
    }
    return mapper.toStructure(service.updateStudent(mapper.toModel(student)));
  }

  @Override
  public String health() {
    log.info("Health Check OK, returning OK");
    return "OK";
  }
}
