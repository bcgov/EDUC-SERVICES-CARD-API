package ca.bc.gov.educ.api.student.validator;

import ca.bc.gov.educ.api.student.model.StudentEntity;
import ca.bc.gov.educ.api.student.service.CodeTableService;
import ca.bc.gov.educ.api.student.service.StudentService;
import ca.bc.gov.educ.api.student.struct.DataSourceCode;
import ca.bc.gov.educ.api.student.struct.GenderCode;
import ca.bc.gov.educ.api.student.struct.Student;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.*;

@Component
public class StudentPayloadValidator {

  @Getter(AccessLevel.PRIVATE)
  private final StudentService studentService;
  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  @Autowired
  public StudentPayloadValidator(final StudentService studentService, final CodeTableService codeTableService) {
    this.studentService = studentService;
    this.codeTableService = codeTableService;
  }

  public List<FieldError> validatePayload(Student student, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    validatePEN(student, isCreateOperation, apiValidationErrors);
    validateDataSourceCode(student, apiValidationErrors);
    validateGenderCode(student, apiValidationErrors);
    validateEmail(student, isCreateOperation, apiValidationErrors);
    return apiValidationErrors;
  }

  protected void validateGenderCode(Student student, List<FieldError> apiValidationErrors) {
    final GenderCode genderCode = codeTableService.findGenderCode(student.getGenderCode());
    if (genderCode == null) {
      apiValidationErrors.add(createFieldError("genderCode", student.getGenderCode(), "Invalid Gender Code."));
    } else if (genderCode.getEffectiveDate() != null && new Date().before(genderCode.getEffectiveDate())) {
      apiValidationErrors.add(createFieldError("genderCode", student.getDataSourceCode(), "Gender Code provided is not yet effective."));
    } else if (genderCode.getExpiryDate() != null && new Date().after(genderCode.getExpiryDate())) {
      apiValidationErrors.add(createFieldError("genderCode", student.getDataSourceCode(), "Gender Code provided has expired."));
    }
  }

  protected void validateDataSourceCode(Student student, List<FieldError> apiValidationErrors) {
    final DataSourceCode dataSourceCode = codeTableService.findDataSourceCode(student.getDataSourceCode());
    if (dataSourceCode == null) {
      apiValidationErrors.add(createFieldError("dataSourceCode", student.getDataSourceCode(), "Invalid Data Source Code."));
    } else if (dataSourceCode.getEffectiveDate() != null && new Date().before(dataSourceCode.getEffectiveDate())) {
      apiValidationErrors.add(createFieldError("dataSourceCode", student.getDataSourceCode(), "Data Source Code provided is not yet effective."));
    } else if (dataSourceCode.getExpiryDate() != null && new Date().after(dataSourceCode.getExpiryDate())) {
      apiValidationErrors.add(createFieldError("dataSourceCode", student.getDataSourceCode(), "Data Source Code provided has expired."));
    }
  }

  protected void validatePEN(Student student, boolean isCreateOperation, List<FieldError> apiValidationErrors) {
    Optional<StudentEntity> studentEntity = getStudentService().retrieveStudentByPen(student.getPen());
    if (isCreateOperation && studentEntity.isPresent()) {
      apiValidationErrors.add(createFieldError("pen", student.getPen(), "PEN is already associated to a student."));
    } else if (studentEntity.isPresent() && !studentEntity.get().getStudentID().equals(UUID.fromString(student.getStudentID()))) {
      apiValidationErrors.add(createFieldError("pen", student.getPen(), "Updated PEN number is already associated to a different student."));
    }
  }

  protected void validateEmail(Student student, boolean isCreateOperation, List<FieldError> apiValidationErrors) {
    if (StringUtils.isNotBlank(student.getEmail())) {
      Optional<StudentEntity> studentEntityByEmail = getStudentService().retrieveStudentByEmail(student.getEmail());
      if (isCreateOperation && studentEntityByEmail.isPresent()) {
        apiValidationErrors.add(createFieldError("pen", student.getEmail(), "Email is already associated to a student."));
      } else if (studentEntityByEmail.isPresent() && !studentEntityByEmail.get().getStudentID().equals(UUID.fromString(student.getStudentID()))) {
        apiValidationErrors.add(createFieldError("pen", student.getEmail(), "Updated Email is already associated to a different student."));
      }
    }
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("student", fieldName, rejectedValue, false, null, null, message);
  }

}
