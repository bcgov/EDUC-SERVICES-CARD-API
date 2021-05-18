package ca.bc.gov.educ.api.servicescard.controller.v1;

import ca.bc.gov.educ.api.servicescard.ServicesCardApiApplication;
import ca.bc.gov.educ.api.servicescard.mappers.v1.ServicesCardMapper;
import ca.bc.gov.educ.api.servicescard.model.v1.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.repository.ServicesCardRepository;
import ca.bc.gov.educ.api.servicescard.service.v1.ServicesCardService;
import ca.bc.gov.educ.api.servicescard.utils.JsonUtil;
import lombok.val;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static ca.bc.gov.educ.api.servicescard.constants.v1.URL.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServicesCardApiApplication.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ServicesCardControllerTest {

  @Autowired
  ServicesCardService service;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ServicesCardRepository repository;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @After
  public void tearDown() {
    this.repository.deleteAll();
  }

  @Test
  public void readServicesCard_givenRandomID_shouldReturnNotFound() throws Exception {
    this.mockMvc.perform(get(BASE_URL + "/" + UUID.randomUUID())
      .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_SERVICES_CARD"))))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void readServicesCard_givenValidID_shouldReturnOK() throws Exception {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    val savedEntity = this.service.createServicesCard(servicesCard);
    assertNotNull(savedEntity);
    assertNotNull(savedEntity.getServicesCardInfoID());
    this.mockMvc.perform(get(BASE_URL + "/" + savedEntity.getServicesCardInfoID())
      .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_SERVICES_CARD"))))
      .andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void searchServicesCard_givenRandomID_shouldReturnNotFound() throws Exception {
    this.mockMvc.perform(get(BASE_URL + "?did=" + UUID.randomUUID())
      .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_SERVICES_CARD"))))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void searchServicesCard_givenValidID_shouldReturnOK() throws Exception {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    val savedEntity = this.service.createServicesCard(servicesCard);
    assertNotNull(savedEntity);
    assertNotNull(savedEntity.getServicesCardInfoID());
    this.mockMvc.perform(get(BASE_URL + "?did=" + savedEntity.getDid())
      .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_SERVICES_CARD"))))
      .andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void createServicesCard() throws Exception {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    val savedEntity = this.service.createServicesCard(servicesCard);
    assertNotNull(savedEntity);
    assertNotNull(savedEntity.getServicesCardInfoID());
    val payload = ServicesCardMapper.mapper.toStructure(savedEntity);
    payload.setCreateDate(null);
    payload.setCreateUser(null);
    payload.setUpdateDate(null);
    payload.setServicesCardInfoID(null);
    payload.setDid("1236456");
    this.mockMvc
      .perform(post(BASE_URL)
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SERVICES_CARD")))
        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON).content(JsonUtil.getJsonStringFromObject(payload)))
      .andDo(print()).andExpect(status().isCreated());
  }

  @Test
  public void createServicesCard_givenInvalidPayload_shouldReturn400() throws Exception {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    val savedEntity = this.service.createServicesCard(servicesCard);
    assertNotNull(savedEntity);
    assertNotNull(savedEntity.getServicesCardInfoID());
    val payload = ServicesCardMapper.mapper.toStructure(savedEntity);
    payload.setCreateDate(null);
    payload.setCreateUser(null);
    payload.setUpdateDate(null);
    payload.setServicesCardInfoID(null);
    payload.setBirthDate("2300-12-31");
    payload.setDid("1236456");
    this.mockMvc
      .perform(post(BASE_URL)
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SERVICES_CARD")))
        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON).content(JsonUtil.getJsonStringFromObject(payload)))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void createServicesCard_givenInvalidBirthDate_shouldReturn400() throws Exception {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    val savedEntity = this.service.createServicesCard(servicesCard);
    assertNotNull(savedEntity);
    assertNotNull(savedEntity.getServicesCardInfoID());
    val payload = ServicesCardMapper.mapper.toStructure(savedEntity);
    payload.setCreateDate(null);
    payload.setCreateUser(null);
    payload.setUpdateDate(null);
    payload.setDid("1236456");
    this.mockMvc
      .perform(post(BASE_URL)
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SERVICES_CARD")))
        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON).content(JsonUtil.getJsonStringFromObject(payload)))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void updateServicesCard() throws Exception {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    val savedEntity = this.service.createServicesCard(servicesCard);
    assertNotNull(savedEntity);
    assertNotNull(savedEntity.getServicesCardInfoID());
    val payload = ServicesCardMapper.mapper.toStructure(savedEntity);
    payload.setCreateDate(null);
    payload.setCreateUser(null);
    payload.setUpdateDate(null);
    this.mockMvc
      .perform(put(BASE_URL + "/" + savedEntity.getServicesCardInfoID())
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SERVICES_CARD")))
        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON).content(JsonUtil.getJsonStringFromObject(payload)))
      .andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void deleteById() throws Exception {
    final ServicesCardEntity servicesCard = this.getServicesCardEntity();
    val savedEntity = this.service.createServicesCard(servicesCard);
    assertNotNull(savedEntity);
    assertNotNull(savedEntity.getServicesCardInfoID());
    this.mockMvc
      .perform(delete(BASE_URL + "/" + savedEntity.getServicesCardInfoID())
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_SERVICES_CARD"))))
      .andDo(print()).andExpect(status().isNoContent());
  }


  private ServicesCardEntity getServicesCardEntity() {
    final ServicesCardEntity servicesCardEntity = new ServicesCardEntity();
    servicesCardEntity.setDigitalIdentityID(UUID.randomUUID());
    servicesCardEntity.setBirthDate(LocalDate.parse("1979-06-11"));
    servicesCardEntity.setCity("Compton");
    servicesCardEntity.setCountry("Canada");
    servicesCardEntity.setDid("1234123112321231");
    servicesCardEntity.setEmail("this@sometest.com");
    servicesCardEntity.setGender("Male");
    servicesCardEntity.setIdentityAssuranceLevel("1");
    servicesCardEntity.setGivenName("Mike");
    servicesCardEntity.setGivenNames("John");
    servicesCardEntity.setSurname("Delanis");
    servicesCardEntity.setPostalCode("V0E1W3");
    servicesCardEntity.setProvince("British Columbia");
    servicesCardEntity.setStreetAddress("123 SomeAddress");
    servicesCardEntity.setUserDisplayName("Mike Delanis");
    return servicesCardEntity;
  }
}
