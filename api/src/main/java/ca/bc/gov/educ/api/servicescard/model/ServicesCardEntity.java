package ca.bc.gov.educ.api.servicescard.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "services_card_info")
@Data
public class ServicesCardEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
          @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "services_card_info_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  UUID servicesCardInfoID;
  @NotNull(message = "did cannot be null")
  @Column(name = "did", unique = true)
  String did;
  @Column(name = "user_display_name")
  String userDisplayName;
  @Column(name = "given_name")
  String givenName;
  @Column(name = "given_names")
  String givenNames;
  @Column(name = "surname")
  String surname;
  @Column(name = "birthdate")
  @PastOrPresent
  Date birthDate;
  @Column(name = "gender")
  String gender;
  @NotNull(message = "Identity assurance level cannot be null")
  @Column(name = "identity_assurance_level")
  String identityAssuranceLevel;
  @Email(message = "Email must be valid email address")
  @Column(name = "email")
  String email;
  @NotNull(message = "Street address cannot be null")
  @Column(name = "street_address")
  String streetAddress;
  @NotNull(message = "City cannot be null")
  @Column(name = "city")
  String city;
  @NotNull(message = "Province cannot be null")
  @Column(name = "province")
  String province;  
  @NotNull(message = "Country cannot be null")
  @Column(name = "country")
  String country; 
  @NotNull(message = "Postal code cannot be null")
  @Column(name = "postal_code")
  String postalCode;   
  @Column(name = "create_user")
  String createUser;
  @Column(name = "create_date")
  @PastOrPresent
  Date createDate;
  @Column(name = "update_user")
  String updateUser;
  @Column(name = "update_date")
  @PastOrPresent
  Date updateDate;

}
