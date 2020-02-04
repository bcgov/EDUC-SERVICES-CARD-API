package ca.bc.gov.educ.api.servicescard.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import ca.bc.gov.educ.api.servicescard.service.ServicesCardService;
import ca.bc.gov.educ.api.servicescard.struct.ServicesCard;
import lombok.AccessLevel;
import lombok.Getter;

@Component
public class ServicesCardPayloadValidator {

  @Getter(AccessLevel.PRIVATE)
  private final ServicesCardService studentService;

  @Autowired
  public ServicesCardPayloadValidator(final ServicesCardService studentService) {
    this.studentService = studentService;
  }

  public List<FieldError> validatePayload(ServicesCard servicesCard, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
//    validatePEN(student, isCreateOperation, apiValidationErrors);
//    validateDataSourceCode(student, apiValidationErrors);
//    validateGenderCode(student, apiValidationErrors);
//    validateEmail(student, isCreateOperation, apiValidationErrors);
    return apiValidationErrors;
  }

//  protected void validateGenderCode(Student student, List<FieldError> apiValidationErrors) {
//    final GenderCode genderCode = codeTableService.findGenderCode(student.getGenderCode());
//    if (genderCode == null) {
//      apiValidationErrors.add(createFieldError("genderCode", student.getGenderCode(), "Invalid Gender Code."));
//    } else if (genderCode.getEffectiveDate() != null && new Date().before(genderCode.getEffectiveDate())) {
//      apiValidationErrors.add(createFieldError("genderCode", student.getDataSourceCode(), "Gender Code provided is not yet effective."));
//    } else if (genderCode.getExpiryDate() != null && new Date().after(genderCode.getExpiryDate())) {
//      apiValidationErrors.add(createFieldError("genderCode", student.getDataSourceCode(), "Gender Code provided has expired."));
//    }
//  }
//
//  protected void validateDataSourceCode(Student student, List<FieldError> apiValidationErrors) {
//    final DataSourceCode dataSourceCode = codeTableService.findDataSourceCode(student.getDataSourceCode());
//    if (dataSourceCode == null) {
//      apiValidationErrors.add(createFieldError("dataSourceCode", student.getDataSourceCode(), "Invalid Data Source Code."));
//    } else if (dataSourceCode.getEffectiveDate() != null && new Date().before(dataSourceCode.getEffectiveDate())) {
//      apiValidationErrors.add(createFieldError("dataSourceCode", student.getDataSourceCode(), "Data Source Code provided is not yet effective."));
//    } else if (dataSourceCode.getExpiryDate() != null && new Date().after(dataSourceCode.getExpiryDate())) {
//      apiValidationErrors.add(createFieldError("dataSourceCode", student.getDataSourceCode(), "Data Source Code provided has expired."));
//    }
//  }

//  protected void validatePEN(Student student, boolean isCreateOperation, List<FieldError> apiValidationErrors) {
//    Optional<StudentEntity> studentEntity = getStudentService().retrieveStudentByPen(student.getPen());
//    if (isCreateOperation && studentEntity.isPresent()) {
//      apiValidationErrors.add(createFieldError("pen", student.getPen(), "PEN is already associated to a student."));
//    } else if (studentEntity.isPresent() && !studentEntity.get().getStudentID().equals(UUID.fromString(student.getStudentID()))) {
//      apiValidationErrors.add(createFieldError("pen", student.getPen(), "Updated PEN number is already associated to a different student."));
//    }
//  }
//
//  protected void validateEmail(Student student, boolean isCreateOperation, List<FieldError> apiValidationErrors) {
//    if (StringUtils.isNotBlank(student.getEmail())) {
//      Optional<StudentEntity> studentEntityByEmail = getStudentService().retrieveStudentByEmail(student.getEmail());
//      if (isCreateOperation && studentEntityByEmail.isPresent()) {
//        apiValidationErrors.add(createFieldError("pen", student.getEmail(), "Email is already associated to a student."));
//      } else if (studentEntityByEmail.isPresent() && !studentEntityByEmail.get().getStudentID().equals(UUID.fromString(student.getStudentID()))) {
//        apiValidationErrors.add(createFieldError("pen", student.getEmail(), "Updated Email is already associated to a different student."));
//      }
//    }
//  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("student", fieldName, rejectedValue, false, null, null, message);
  }

}
