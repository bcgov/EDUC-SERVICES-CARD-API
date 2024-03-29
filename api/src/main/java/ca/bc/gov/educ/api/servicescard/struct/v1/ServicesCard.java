package ca.bc.gov.educ.api.servicescard.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServicesCard implements Serializable {
  private static final long serialVersionUID = 1L;

  String servicesCardInfoID;
  @NotNull(message = "Digital identity ID cannot be null")
  String digitalIdentityID;
  @Size(max = 255)
  @NotNull(message = "did cannot be null")
  String did;
  @Size(max = 255)
  String userDisplayName;
  @Size(max = 255)
  String givenName;
  @Size(max = 255)
  String givenNames;
  @Size(max = 255)
  @NotNull(message = "Surname cannot be null")
  String surname;
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of Birth must be in 'yyyy-mm-dd' format")
  @NotNull(message = "Birth date cannot be null")
  String birthDate;
  @Size(max = 7)
  @NotNull(message = "Gender cannot be null")
  String gender;
  @Size(max = 1)
  @NotNull(message = "Identity assurance level cannot be null")
  String identityAssuranceLevel;
  @Size(max = 255)
  @Email(message = "Email must be valid email address")
  String email;
  @Size(max = 7)
  @NotNull(message = "Postal code cannot be null")
  String postalCode;
  @Null(message = "createDate should be null.")
  String createDate;
  @Null(message = "updateDate should be null.")
  String updateDate;
  @Size(max = 32)
  String createUser;
  @Size(max = 32)
  String updateUser;
  @Size(max = 50)
  String subscriptionId;
}
