package ca.bc.gov.educ.api.servicescard.model.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SERVICES_CARD_INFO")
@Data
@DynamicUpdate
public class ServicesCardEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
          @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "SERVICES_CARD_INFO_ID", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  UUID servicesCardInfoID;

  @Column(name = "DIGITAL_IDENTITY_ID", columnDefinition = "BINARY(16)")
  UUID digitalIdentityID;

  @Column(name = "DID", unique = true)
  String did;

  @Column(name = "USER_DISPLAY_NAME")
  String userDisplayName;

  @Column(name = "GIVEN_NAME")
  String givenName;

  @Column(name = "GIVEN_NAMES")
  String givenNames;

  @Column(name = "SURNAME")
  String surname;

  @Column(name = "BIRTHDATE")
  @PastOrPresent
  LocalDate birthDate;

  @Column(name = "GENDER")
  String gender;

  @Column(name = "IDENTITY_ASSURANCE_LEVEL")
  String identityAssuranceLevel;

  @Email(message = "Email must be valid email address")
  @Column(name = "EMAIL")
  String email;

  @Column(name = "POSTAL_CODE")
  String postalCode;

  @Column(name = "CREATE_USER", updatable = false)
  String createUser;

  @Column(name = "CREATE_DATE", updatable = false)
  @PastOrPresent
  LocalDateTime createDate;

  @Column(name = "UPDATE_USER")
  String updateUser;

  @Column(name = "UPDATE_DATE")
  @PastOrPresent
  LocalDateTime updateDate;

  @Column(name = "SUBSCRIPTION_ID")
  String subscriptionId;
}
