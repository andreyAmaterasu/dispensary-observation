package ru.kostromin.dispensaryobservation.configuration;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app")
@Data
@Validated
public class AppConfiguration {

  /**
   * Представление тела ответа когда пациент не найден
   */
  private Map<String, String> patientNotFoundResponseBody;

  /**
   * Представление тела ответа когда параметры запроса не были прочитаны
   */
  private Map<String, String> badRequestResponseBody;

  /**
   * Запрос к базе данных для получения диагнозов диспансерного учета пациента
   */
  @NotBlank(message = "SQL запрос не найден в конфигурационном файле")
  private String sql;
}
