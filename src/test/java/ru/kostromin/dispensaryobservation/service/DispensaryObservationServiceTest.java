package ru.kostromin.dispensaryobservation.service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostromin.dispensaryobservation.configuration.AppConfiguration;
import ru.kostromin.dispensaryobservation.dto.DispensaryObservationResponse;
import ru.kostromin.dispensaryobservation.util.PatientDataExtractor;
import ru.kostromin.dispensaryobservation.dto.DispensaryObservationSqlFields;

@ExtendWith(SpringExtension.class)
class DispensaryObservationServiceTest {

  @Mock
  private AppConfiguration appConfiguration;
  @Mock
  private NamedParameterJdbcTemplate jdbcTemplate;

  private DispensaryObservationService dispensaryObservationService;

  @BeforeEach
  void setup() {
    dispensaryObservationService = new DispensaryObservationService(
        appConfiguration, jdbcTemplate, new PatientDataExtractor());
  }

  @Test
  @DisplayName("SQL запрос найден, данные по переданным параметрам в запросе найдены "
      + "(одна клиника, один диагноз), ответ отправлен")
  void sqlQueryFoundDataInRequestFoundWithOneClinicCodeAndOneDiagnosisResponseSent() {

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

    List<DispensaryObservationResponse> responseList =
        dispensaryObservationService.getDispensaryObservations(new HashMap<>());
    System.out.println(responseList);
    Assertions.assertEquals(1, responseList.size());
    Assertions.assertEquals(1, responseList.get(0).dispensaryObservations().size());
    Assertions.assertEquals(1, responseList.get(0).dispensaryObservations().get(0).diagnoses().size());

    Assertions.assertEquals("lastName", responseList.get(0).lastName());
    Assertions.assertEquals("firstName", responseList.get(0).firstName());
    Assertions.assertEquals("middleName", responseList.get(0).middleName());
    Assertions.assertEquals(Date.valueOf("2023-01-01"), responseList.get(0).birthDate());
    Assertions.assertEquals("snils", responseList.get(0).snils());
    Assertions.assertEquals("omsNumber", responseList.get(0).omsNumber());
    Assertions.assertEquals("clinicCode_1", responseList.get(0).dispensaryObservations().get(0).clinicCode());
    Assertions.assertEquals("diagnosis_1", responseList.get(0).dispensaryObservations().get(0).diagnoses().get(0));
  }

  @Test
  @DisplayName("SQL запрос найден, данные по переданным параметрам в запросе найдены "
      + "(одна клиника, два диагноза), ответ отправлен")
  void sqlQueryFoundDataInRequestFoundWithOneClinicCodeAndMoreThatOneDiagnosesResponseSent() {

    String sqlQuery = "someSqlQuery";
    Mockito.when(appConfiguration.getSql()).thenReturn(sqlQuery);

    Mockito.doAnswer(invocationOnMock -> {
      ResultSetExtractor<List<DispensaryObservationResponse>> resultSetExtractor =
          invocationOnMock.getArgument(2);
      return resultSetExtractor.extractData(getDispensaryObservationResponseWithOneClinicCodeAndMoreThatOneDiagnosesMock());
    }).when(jdbcTemplate).query(
        Mockito.anyString(),
        Mockito.any(MapSqlParameterSource.class),
        Mockito.any(PatientDataExtractor.class));

    List<DispensaryObservationResponse> responseList =
        dispensaryObservationService.getDispensaryObservations(new HashMap<>());
    Assertions.assertEquals(1, responseList.size());
    Assertions.assertEquals(1, responseList.get(0).dispensaryObservations().size());
    Assertions.assertEquals(2, responseList.get(0).dispensaryObservations().get(0).diagnoses().size());

    Assertions.assertEquals("lastName", responseList.get(0).lastName());
    Assertions.assertEquals("firstName", responseList.get(0).firstName());
    Assertions.assertEquals("middleName", responseList.get(0).middleName());
    Assertions.assertEquals(Date.valueOf("2023-01-01"), responseList.get(0).birthDate());
    Assertions.assertEquals("snils", responseList.get(0).snils());
    Assertions.assertEquals("omsNumber", responseList.get(0).omsNumber());
    Assertions.assertEquals("clinicCode_1", responseList.get(0).dispensaryObservations().get(0).clinicCode());
    Assertions.assertEquals("diagnosis_1", responseList.get(0).dispensaryObservations().get(0).diagnoses().get(0));
    Assertions.assertEquals("diagnosis_2", responseList.get(0).dispensaryObservations().get(0).diagnoses().get(1));
  }

  @Test
  @DisplayName("SQL запрос найден, данные по переданным параметрам в запросе найдены "
      + "(больше, чем одна клиника и больше, чем один диагноз), ответ отправлен")
  void sqlQueryFoundDataInRequestFoundWithMoreThatOneClinicCodeAndMoreThatOneDiagnosesResponseSent() {

    String sqlQuery = "someSqlQuery";
    Mockito.when(appConfiguration.getSql()).thenReturn(sqlQuery);

    Mockito.doAnswer(invocationOnMock -> {
      ResultSetExtractor<List<DispensaryObservationResponse>> resultSetExtractor =
          invocationOnMock.getArgument(2);
      return resultSetExtractor.extractData(getDispensaryObservationResponseWithMoreThatOneClinicCodeAndMoreThatOneDiagnosesMock());
    }).when(jdbcTemplate).query(
        Mockito.anyString(),
        Mockito.any(MapSqlParameterSource.class),
        Mockito.any(PatientDataExtractor.class));

    List<DispensaryObservationResponse> responseList =
        dispensaryObservationService.getDispensaryObservations(new HashMap<>());
    Assertions.assertEquals(1, responseList.size());
    Assertions.assertEquals(2, responseList.get(0).dispensaryObservations().size());
    Assertions.assertEquals(3, responseList.get(0).dispensaryObservations().get(0).diagnoses().size());
    Assertions.assertEquals(2, responseList.get(0).dispensaryObservations().get(1).diagnoses().size());

    Assertions.assertEquals("lastName", responseList.get(0).lastName());
    Assertions.assertEquals("firstName", responseList.get(0).firstName());
    Assertions.assertEquals("middleName", responseList.get(0).middleName());
    Assertions.assertEquals(Date.valueOf("2023-01-01"), responseList.get(0).birthDate());
    Assertions.assertEquals("snils", responseList.get(0).snils());
    Assertions.assertEquals("omsNumber", responseList.get(0).omsNumber());
    Assertions.assertEquals("clinicCode_1", responseList.get(0).dispensaryObservations().get(0).clinicCode());
    Assertions.assertEquals("clinicCode_2", responseList.get(0).dispensaryObservations().get(1).clinicCode());
    Assertions.assertEquals("diagnosis_1", responseList.get(0).dispensaryObservations().get(0).diagnoses().get(0));
    Assertions.assertEquals("diagnosis_2", responseList.get(0).dispensaryObservations().get(0).diagnoses().get(1));
    Assertions.assertEquals("diagnosis_3", responseList.get(0).dispensaryObservations().get(0).diagnoses().get(2));
    Assertions.assertEquals("diagnosis_4", responseList.get(0).dispensaryObservations().get(1).diagnoses().get(0));
    Assertions.assertEquals("diagnosis_5", responseList.get(0).dispensaryObservations().get(1).diagnoses().get(1));
  }

  @Test
  @DisplayName("SQL запрос найден, данные по переданным параметрам в запросе найдены, "
      + "но не найдены диагнозы, ответ отправлен")
  void sqlQueryFoundDataInRequestFoundWithoutClinicCodeAndDiagnosesResponseSent() {

    String sqlQuery = "someSqlQuery";
    Mockito.when(appConfiguration.getSql()).thenReturn(sqlQuery);

    Mockito.doAnswer(invocationOnMock -> {
      ResultSetExtractor<List<DispensaryObservationResponse>> resultSetExtractor =
          invocationOnMock.getArgument(2);
      return resultSetExtractor.extractData(getDispensaryObservationResponseWithoutClinicCodeAndDiagnosesMock());
    }).when(jdbcTemplate).query(
        Mockito.anyString(),
        Mockito.any(MapSqlParameterSource.class),
        Mockito.any(PatientDataExtractor.class));

    List<DispensaryObservationResponse> responseList =
        dispensaryObservationService.getDispensaryObservations(new HashMap<>());
    Assertions.assertEquals(1, responseList.size());
    Assertions.assertEquals(0, responseList.get(0).dispensaryObservations().size());

    Assertions.assertEquals("lastName", responseList.get(0).lastName());
    Assertions.assertEquals("firstName", responseList.get(0).firstName());
    Assertions.assertEquals("middleName", responseList.get(0).middleName());
    Assertions.assertEquals(Date.valueOf("2023-01-01"), responseList.get(0).birthDate());
    Assertions.assertEquals("snils", responseList.get(0).snils());
    Assertions.assertEquals("omsNumber", responseList.get(0).omsNumber());
  }

  @Test
  @DisplayName("SQL запрос найден, данные по переданным параметрам в запросе не найдены")
  void sqlQueryFoundDataInRequestNotFoundResponseSent() {

    String sqlQuery = "someSqlQuery";
    Mockito.when(appConfiguration.getSql()).thenReturn(sqlQuery);

    Mockito.doAnswer(invocationOnMock -> {
      ResultSetExtractor<List<DispensaryObservationResponse>> resultSetExtractor =
          invocationOnMock.getArgument(2);
      return resultSetExtractor.extractData(getEmptyDispensaryObservationResponseMock());
    }).when(jdbcTemplate).query(
        Mockito.anyString(),
        Mockito.any(MapSqlParameterSource.class),
        Mockito.any(PatientDataExtractor.class));

    List<DispensaryObservationResponse> responseList =
        dispensaryObservationService.getDispensaryObservations(new HashMap<>());
    Assertions.assertEquals(0, responseList.size());
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

  private ResultSet getDispensaryObservationResponseWithOneClinicCodeAndMoreThatOneDiagnosesMock()
      throws SQLException {

    ResultSet resultSetMock = Mockito.mock(ResultSet.class);
    Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.LAST_NAME.getColumnName()))
        .thenReturn("lastName").thenReturn("lastName");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.FIRST_NAME.getColumnName()))
        .thenReturn("firstName").thenReturn("firstName");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.MIDDLE_NAME.getColumnName()))
        .thenReturn("middleName").thenReturn("middleName");
    Mockito.when(resultSetMock.getDate(DispensaryObservationSqlFields.BIRTH_DATE.getColumnName()))
        .thenReturn(Date.valueOf("2023-01-01")).thenReturn(Date.valueOf("2023-01-01"));
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.SNILS.getColumnName()))
        .thenReturn("snils").thenReturn("snils");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.OMS_NUMBER.getColumnName()))
        .thenReturn("omsNumber").thenReturn("omsNumber");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.CLINIC_CODE.getColumnName()))
        .thenReturn("clinicCode_1").thenReturn("clinicCode_1");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.DIAGNOSIS.getColumnName()))
        .thenReturn("diagnosis_1").thenReturn("diagnosis_2");
    return resultSetMock;
  }

  private ResultSet getDispensaryObservationResponseWithMoreThatOneClinicCodeAndMoreThatOneDiagnosesMock()
      throws SQLException {

    ResultSet resultSetMock = Mockito.mock(ResultSet.class);
    Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(true)
        .thenReturn(true).thenReturn(true).thenReturn(false);
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.LAST_NAME.getColumnName()))
        .thenReturn("lastName").thenReturn("lastName")
        .thenReturn("lastName").thenReturn("lastName")
        .thenReturn("lastName");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.FIRST_NAME.getColumnName()))
        .thenReturn("firstName").thenReturn("firstName")
        .thenReturn("firstName").thenReturn("firstName")
        .thenReturn("firstName");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.MIDDLE_NAME.getColumnName()))
        .thenReturn("middleName").thenReturn("middleName")
        .thenReturn("middleName").thenReturn("middleName")
        .thenReturn("middleName");
    Mockito.when(resultSetMock.getDate(DispensaryObservationSqlFields.BIRTH_DATE.getColumnName()))
        .thenReturn(Date.valueOf("2023-01-01")).thenReturn(Date.valueOf("2023-01-01"))
        .thenReturn(Date.valueOf("2023-01-01")).thenReturn(Date.valueOf("2023-01-01"))
        .thenReturn(Date.valueOf("2023-01-01"));
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.SNILS.getColumnName()))
        .thenReturn("snils").thenReturn("snils")
        .thenReturn("snils").thenReturn("snils")
        .thenReturn("snils");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.OMS_NUMBER.getColumnName()))
        .thenReturn("omsNumber").thenReturn("omsNumber")
        .thenReturn("omsNumber").thenReturn("omsNumber")
        .thenReturn("omsNumber");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.CLINIC_CODE.getColumnName()))
        .thenReturn("clinicCode_1").thenReturn("clinicCode_1")
        .thenReturn("clinicCode_1").thenReturn("clinicCode_2")
        .thenReturn("clinicCode_2");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.DIAGNOSIS.getColumnName()))
        .thenReturn("diagnosis_1").thenReturn("diagnosis_2")
        .thenReturn("diagnosis_3").thenReturn("diagnosis_4")
        .thenReturn("diagnosis_5");
    return resultSetMock;
  }

  private ResultSet getDispensaryObservationResponseWithoutClinicCodeAndDiagnosesMock()
      throws SQLException {

    ResultSet resultSetMock = Mockito.mock(ResultSet.class);
    Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.LAST_NAME.getColumnName())).thenReturn("lastName");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.FIRST_NAME.getColumnName())).thenReturn("firstName");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.MIDDLE_NAME.getColumnName())).thenReturn("middleName");
    Mockito.when(resultSetMock.getDate(DispensaryObservationSqlFields.BIRTH_DATE.getColumnName())).thenReturn(Date.valueOf("2023-01-01"));
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.SNILS.getColumnName())).thenReturn("snils");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.OMS_NUMBER.getColumnName())).thenReturn("omsNumber");
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.CLINIC_CODE.getColumnName())).thenReturn(null);
    Mockito.when(resultSetMock.getString(DispensaryObservationSqlFields.DIAGNOSIS.getColumnName())).thenReturn(null);
    return resultSetMock;
  }

  private ResultSet getEmptyDispensaryObservationResponseMock() throws SQLException {

    ResultSet resultSetMock = Mockito.mock(ResultSet.class);
    Mockito.when(resultSetMock.next()).thenReturn(false);
    return resultSetMock;
  }
}
