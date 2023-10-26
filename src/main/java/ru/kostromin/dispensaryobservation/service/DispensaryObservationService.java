package ru.kostromin.dispensaryobservation.service;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.kostromin.dispensaryobservation.configuration.AppConfiguration;
import ru.kostromin.dispensaryobservation.dto.DispensaryObservationResponse;
import ru.kostromin.dispensaryobservation.util.PatientDataExtractor;

/**
 * Сервис для получения данных о пациенте для отправки ответа
 */
@Service
@RequiredArgsConstructor
public class DispensaryObservationService {

  private final AppConfiguration appConfiguration;
  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final PatientDataExtractor patientDataExtractor;

  /**
   * Поиск диагнозов диспансерного учета
   *
   * @param queryParameters {@link Map} содержащий параметры запроса
   * @return {@link List} представлений ответа
   */
  public List<DispensaryObservationResponse> getDispensaryObservations(
      Map<String, String> queryParameters) {

    return jdbcTemplate.query(
        appConfiguration.getSql(),
        new MapSqlParameterSource().addValues(queryParameters),
        patientDataExtractor);
  }
}
