package ca.bc.gov.educ.api.student.struct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderCode {
  String genderCode;
  String label;
  String description;
  Integer displayOrder;
  Date effectiveDate;
  Date expiryDate;

}
