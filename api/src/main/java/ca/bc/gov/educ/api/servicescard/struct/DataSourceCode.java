package ca.bc.gov.educ.api.student.struct;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceCode {
  String dataSourceCode;
  String label;
  String description;
  Integer displayOrder;
  Date effectiveDate;
  Date expiryDate;
}
