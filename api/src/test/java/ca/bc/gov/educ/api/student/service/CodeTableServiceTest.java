package ca.bc.gov.educ.api.student.service;

import ca.bc.gov.educ.api.student.properties.ApplicationProperties;
import ca.bc.gov.educ.api.student.repository.StudentRepository;
import ca.bc.gov.educ.api.student.rest.RestUtils;
import ca.bc.gov.educ.api.student.struct.DataSourceCode;
import ca.bc.gov.educ.api.student.struct.GenderCode;
import ca.bc.gov.educ.api.student.validator.StudentPayloadValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static ca.bc.gov.educ.api.student.constant.CodeTableConstants.DATA_SOURCE_API_BASE_PATH;
import static ca.bc.gov.educ.api.student.constant.CodeTableConstants.GENDER_CODE_API_BASE_PATH;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CodeTableServiceTest {
  public static final String PARAMETERS = "parameters";
  @Mock
  RestTemplate template;

  @Mock
  RestUtils restUtils;

  @Mock
  ApplicationProperties applicationProperties;

  @InjectMocks
  CodeTableService codeTableService;


  @Test
  public void testLoadCodeTableDataToMemory_OnClassLoad_ShouldPopulateTheCodeMaps() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    when(restUtils.getRestTemplate()).thenReturn(template);
    when(applicationProperties.getCodetableApiURL()).thenReturn("http://localhost:0000");
    when(template.exchange(applicationProperties.getCodetableApiURL() + DATA_SOURCE_API_BASE_PATH.getValue(), HttpMethod.GET, new HttpEntity<>(PARAMETERS, headers), DataSourceCode[].class)).thenReturn(createDataSourceResponse());
    when(template.exchange(applicationProperties.getCodetableApiURL() + GENDER_CODE_API_BASE_PATH.getValue(), HttpMethod.GET, new HttpEntity<>(PARAMETERS, headers), GenderCode[].class)).thenReturn(createGenderCodeResponse());
    codeTableService = new CodeTableService(restUtils, applicationProperties);
    assertNotNull(codeTableService.findDataSourceCode("MYED"));
    assertNotNull(codeTableService.findDataSourceCode("MYED1"));
    assertNotNull(codeTableService.findGenderCode("M"));
    assertNotNull(codeTableService.findGenderCode("F"));
  }

  private ResponseEntity<DataSourceCode[]> createDataSourceResponse() {
    return ResponseEntity.ok(createDataSourceArray());
  }

  private DataSourceCode[] createDataSourceArray() {
    DataSourceCode[] dataSourceCodes = new DataSourceCode[2];
    dataSourceCodes[0] = DataSourceCode.builder().dataSourceCode("MYED").effectiveDate(new Date()).expiryDate(new Date()).build();
    dataSourceCodes[1] = DataSourceCode.builder().dataSourceCode("MYED1").effectiveDate(new Date()).expiryDate(new Date()).build();
    return dataSourceCodes;
  }

  private ResponseEntity<GenderCode[]> createGenderCodeResponse() {
    return ResponseEntity.ok(createGenderCodeArray());
  }

  private GenderCode[] createGenderCodeArray() {
    GenderCode[] genderCodes = new GenderCode[2];
    genderCodes[0] = GenderCode.builder().genderCode("M").effectiveDate(new Date()).expiryDate(new Date()).build();
    genderCodes[1] = GenderCode.builder().genderCode("F").effectiveDate(new Date()).expiryDate(new Date()).build();
    return genderCodes;
  }

}
