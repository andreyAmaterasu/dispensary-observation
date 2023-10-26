package ru.kostromin.dispensaryobservation.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.kostromin.dispensaryobservation.dto.DispensaryObservation;
import ru.kostromin.dispensaryobservation.dto.DispensaryObservationResponse;
import ru.kostromin.dispensaryobservation.dto.DispensaryObservationSqlFields;

/**
 * Экстрактор данных пациента из результата запроса
 */
@Component
public class PatientDataExtractor implements ResultSetExtractor<List<DispensaryObservationResponse>> {

  /**
   * Метод для извлечения данных пациента из результата запроса
   *
   * @param rs набор результатов, полученных из базы данных
   * @return {@link List} представлений ответа
   * @throws SQLException исключение, возникающее при ошибках доступа к базе данных
   * @throws DataAccessException исключение, возникающее при ошибках доступа к данным
   */
  @Override
  public List<DispensaryObservationResponse> extractData(ResultSet rs)
      throws SQLException, DataAccessException {

    List<DispensaryObservationResponse> responseList = new ArrayList<>();
    String previousClinicCode = null;
    DispensaryObservationResponse response = null;
    DispensaryObservation dispensaryObservation = null;
    while (rs.next()) {
      if (response == null) {
        response = DispensaryObservationResponse.builder()
            .lastName(rs.getString(DispensaryObservationSqlFields.LAST_NAME.getColumnName()))
            .firstName(rs.getString(DispensaryObservationSqlFields.FIRST_NAME.getColumnName()))
            .middleName(rs.getString(DispensaryObservationSqlFields.MIDDLE_NAME.getColumnName()))
            .birthDate(rs.getDate(DispensaryObservationSqlFields.BIRTH_DATE.getColumnName()))
            .snils(rs.getString(DispensaryObservationSqlFields.SNILS.getColumnName()))
            .omsNumber(rs.getString(DispensaryObservationSqlFields.OMS_NUMBER.getColumnName()))
            .dispensaryObservations(new ArrayList<>())
            .build();
        responseList.add(response);
      }

      String currentClinicCode = rs.getString(DispensaryObservationSqlFields.CLINIC_CODE.getColumnName());
      if (currentClinicCode != null) {
        if (!currentClinicCode.equals(previousClinicCode)) {
          previousClinicCode = currentClinicCode;
          dispensaryObservation = DispensaryObservation.builder()
              .clinicCode(currentClinicCode)
              .diagnoses(new ArrayList<>())
              .build();
          response.dispensaryObservations().add(dispensaryObservation);
        }
        dispensaryObservation.diagnoses().add(rs.getString(DispensaryObservationSqlFields.DIAGNOSIS.getColumnName()));
      }
    }
    return responseList;
  }
}