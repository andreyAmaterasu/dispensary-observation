package ru.kostromin.dispensaryobservation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import ru.kostromin.dispensaryobservation.configuration.AppConfiguration;
import ru.kostromin.dispensaryobservation.dto.DispensaryObservationResponse;
import ru.kostromin.dispensaryobservation.service.DispensaryObservationService;
import ru.kostromin.dispensaryobservation.util.PatientDataExtractor;
import ru.kostromin.dispensaryobservation.util.RequestParametersConverter;
import ru.kostromin.dispensaryobservation.dto.DispensaryObservationSqlFields;

@ExtendWith(SpringExtension.class)
class DispensaryObservationControllerTest {

  @Mock
  private AppConfiguration appConfiguration;
  @Mock
  private NamedParameterJdbcTemplate jdbcTemplate;
  @Mock
  private RequestParametersConverter requestParametersConverter;
  @Mock
  private Map<String, String> map;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {

    DispensaryObservationService dispensaryObservationService = new DispensaryObservationService(
        appConfiguration, jdbcTemplate, new PatientDataExtractor());

    FormattingConversionService conversionService = new FormattingConversionService();
    conversionService.addConverter(requestParametersConverter);

    mockMvc = MockMvcBuilders.standaloneSetup(
        new DispensaryObservationController(dispensaryObservationService, appConfiguration))
        .setConversionService(conversionService).build();
  }

  @Test
  @DisplayName("Пациент по переданным параметрам в фильтре найден, отправлен ответ OK (200)")
  void getDispensaryObservationResponse_ok200() throws Exception {

    String sqlQuery = "someSqlQuery";
    Mockito.when(appConfiguration.getSql()).thenReturn(sqlQuery);

    Mockito.doAnswer(invocationOnMock -> {
      ResultSetExtractor<List<DispensaryObservationResponse>> resultSetExtractor =
          invocationOnMock.getArgument(2);
      return resultSetExtractor.extractData(getDispensaryObservationResponseWithOneClinicCodeAndOneDiagnosisMock());
    }).when(jdbcTemplate).query(
        Mockito.anyString(),
        Mockito.any(MapSqlParameterSource.class),
        Mockito.any(PatientDataExtractor.class));

    String filter = "anyFilter";
    Mockito.when(requestParametersConverter.convert(filter)).thenReturn(map);
    Mockito.when(CollectionUtils.isEmpty(map)).thenReturn(false);
    mockMvc.perform(MockMvcRequestBuilders.get("/patients?filter={filter}", filter))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpectAll(jsonPath("$[0].lastName").value("lastName"),
            jsonPath("$[0].firstName").value("firstName"),
            jsonPath("$[0].middleName").value("middleName"),
            jsonPath("$[0].birthDate").value(Date.valueOf("2023-01-01").getTime()),
            jsonPath("$[0].SNILS").value("snils"),
            jsonPath("$[0].OMS").value("omsNumber"),
            jsonPath("$[0].dispensaryObservation[0].clinicCode").value("clinicCode_1"),
            jsonPath("$[0].dispensaryObservation[0].diagnosis[0]").value("diagnosis_1"));
  }

  @Test
  @DisplayName("Пациент по переданным параметрам в фильтре не найден, отправлен ответ "
      + "NOT FOUND (404) { error: Не найден пациент }")
  void getDispensaryObservationResponse_notFound404() throws Exception {

    String sqlQuery = "someSqlQuery";
    Mockito.when(appConfiguration.getSql()).thenReturn(sqlQuery);
    Mockito.when(appConfiguration.getPatientNotFoundResponseBody())
        .thenReturn(Map.of("error", "Не найден пациент"));

    Mockito.doAnswer(invocationOnMock -> {
      ResultSetExtractor<List<DispensaryObservationResponse>> resultSetExtractor =
          invocationOnMock.getArgument(2);
      return resultSetExtractor.extractData(getEmptyDispensaryObservationResponseMock());
    }).when(jdbcTemplate).query(
        Mockito.anyString(),
        Mockito.any(MapSqlParameterSource.class),
        Mockito.any(PatientDataExtractor.class));

    String filter = "anyFilter";
    Mockito.when(requestParametersConverter.convert(filter)).thenReturn(map);
    Mockito.when(CollectionUtils.isEmpty(map)).thenReturn(false);
    mockMvc.perform(MockMvcRequestBuilders.get("/patients?filter={filter}", filter))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Не найден пациент"));
  }

  @Test
  @DisplayName("Json, полученный в параметре запроса не был прочитан, отправлен ответ "
      + "NOT FOUND (404) { error: Невозможно прочитать параметры запроса }")
  void getDispensaryObservationResponse_barRequest400() throws Exception {

    String sqlQuery = "someSqlQuery";
    Mockito.when(appConfiguration.getSql()).thenReturn(sqlQuery);
    Mockito.when(appConfiguration.getBadRequestResponseBody())
        .thenReturn(Map.of("error", "Невозможно прочитать параметры запроса"));

    Mockito.doAnswer(invocationOnMock -> {
      ResultSetExtractor<List<DispensaryObservationResponse>> resultSetExtractor =
          invocationOnMock.getArgument(2);
      return resultSetExtractor.extractData(getEmptyDispensaryObservationResponseMock());
    }).when(jdbcTemplate).query(
        Mockito.anyString(),
        Mockito.any(MapSqlParameterSource.class),
        Mockito.any(PatientDataExtractor.class));

    String filter = "anyFilter";
    Mockito.when(requestParametersConverter.convert(filter)).thenReturn(map);
    Mockito.when(CollectionUtils.isEmpty(map)).thenReturn(true);
    mockMvc.perform(MockMvcRequestBuilders.get("/patients?filter={filter}", filter))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("Невозможно прочитать параметры запроса"));
  }

  private ResultSet getDispensaryObservationResponseWithOneClinicCodeAndOneDiagnosisMock()
      throws SQLException {

    ResultSet resultSetMock = Mockito.mock(ResultSet.class);
    Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.LAST_NAME.getColumnName())).thenReturn("lastName");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.FIRST_NAME.getColumnName())).thenReturn("firstName");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.MIDDLE_NAME.getColumnName())).thenReturn("middleName");
    Mockito.when(resultSetMock.getDate(DispensaryObservationSqlFields.BIRTH_DATE.getColumnName())).thenReturn(Date.valueOf("2023-01-01"));
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.SNILS.getColumnName())).thenReturn("snils");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.OMS_NUMBER.getColumnName())).thenReturn("omsNumber");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.CLINIC_CODE.getColumnName())).thenReturn("clinicCode_1");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.DIAGNOSIS.getColumnName())).thenReturn("diagnosis_1");
    return resultSetMock;
  }

  private ResultSet getEmptyDispensaryObservationResponseMock() throws SQLException {

    ResultSet resultSetMock = Mockito.mock(ResultSet.class);
    Mockito.when(resultSetMock.next()).thenReturn(false);
    return resultSetMock;
  }
}
