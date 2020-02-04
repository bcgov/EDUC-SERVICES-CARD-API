package ca.bc.gov.educ.api.student.service;

import ca.bc.gov.educ.api.student.properties.ApplicationProperties;
import ca.bc.gov.educ.api.student.rest.RestUtils;
import ca.bc.gov.educ.api.student.struct.DataSourceCode;
import ca.bc.gov.educ.api.student.struct.GenderCode;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ca.bc.gov.educ.api.student.constant.CodeTableConstants.DATA_SOURCE_API_BASE_PATH;
import static ca.bc.gov.educ.api.student.constant.CodeTableConstants.GENDER_CODE_API_BASE_PATH;

@Service
public class CodeTableService {

  public static final String PARAMETERS = "parameters";
  @Getter(AccessLevel.PRIVATE)
  private final RestUtils restUtils;
  private static Map<String, DataSourceCode> dataSourceCodeMap = new ConcurrentHashMap<>();
  private static Map<String, GenderCode> genderCodeMap = new ConcurrentHashMap<>();
  private final ApplicationProperties props;

  @PreDestroy
  public void close() {
    dataSourceCodeMap.clear();
    genderCodeMap.clear();
  }

  @Autowired
  public CodeTableService(final RestUtils restUtils, ApplicationProperties props) {
    this.restUtils = restUtils;
    this.props = props;
  }

  @PostConstruct
  public void loadCodeTableDataToMemory() {
    loadDataSourceCodes();
    loadGenderCodes();
  }

  public void loadDataSourceCodes() {
    RestTemplate restTemplate = restUtils.getRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    ResponseEntity<DataSourceCode[]> dataSourceCodeResponse;
    dataSourceCodeResponse = restTemplate.exchange(props.getCodetableApiURL() + DATA_SOURCE_API_BASE_PATH.getValue(), HttpMethod.GET, new HttpEntity<>(PARAMETERS, headers), DataSourceCode[].class);
    if (dataSourceCodeResponse.getBody() != null) {
      dataSourceCodeMap.putAll(Arrays.stream(dataSourceCodeResponse.getBody()).collect(Collectors.toMap(DataSourceCode::getDataSourceCode, dataSource -> dataSource)));
    }
  }

  private void loadGenderCodes() {
    RestTemplate restTemplate = restUtils.getRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    ResponseEntity<GenderCode[]> genderCodeResponse;
    genderCodeResponse = restTemplate.exchange(props.getCodetableApiURL() + GENDER_CODE_API_BASE_PATH.getValue(), HttpMethod.GET, new HttpEntity<>(PARAMETERS, headers), GenderCode[].class);
    if (genderCodeResponse.getBody() != null) {
      genderCodeMap.putAll(Arrays.stream(genderCodeResponse.getBody()).collect(Collectors.toMap(GenderCode::getGenderCode, genderCode -> genderCode)));
    }
  }

  /**
   * This method will look first at the cache, which spring boot internally handles,
   * then it will go to the the cache which loaded data during the boot, if it is not found then it will make a rest call
   * and retrieve latest data from API.
   *
   * @param genderCode the code based on which the Data will be retrieved from MAP.
   * @return GENDER CODE or null.
   */
  @Cacheable("genderCodeCache")
  public GenderCode findGenderCode(String genderCode) {
    if (genderCodeMap.containsKey(genderCode)) {
      return genderCodeMap.get(genderCode);
    }
    loadGenderCodes();
    return genderCodeMap.get(genderCode);
  }

  @Cacheable("datasourceCodeCache")
  public DataSourceCode findDataSourceCode(String datasourceCode) {
    if (dataSourceCodeMap.containsKey(datasourceCode)) {
      return dataSourceCodeMap.get(datasourceCode);
    }
    loadDataSourceCodes();
    return dataSourceCodeMap.get(datasourceCode);
  }
}
