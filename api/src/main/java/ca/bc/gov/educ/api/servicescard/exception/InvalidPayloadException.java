package ca.bc.gov.educ.api.student.exception;

import ca.bc.gov.educ.api.student.exception.errors.ApiError;
import lombok.Getter;

public class InvalidPayloadException extends RuntimeException {

  @Getter
  private final ApiError error;

  public InvalidPayloadException(final ApiError error) {
    super(error.getMessage());
    this.error = error;
  }
}
