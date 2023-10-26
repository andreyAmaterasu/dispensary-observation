package ru.kostromin.dispensaryobservation.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kostromin.dispensaryobservation.configuration.AppConfiguration;
import ru.kostromin.dispensaryobservation.dto.DispensaryObservationResponse;
import ru.kostromin.dispensaryobservation.service.DispensaryObservationService;

/**
 * REST Controller для методов работы с пациентом
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DispensaryObservationController {

  private final DispensaryObservationService dispensaryObservationService;
  private final AppConfiguration appConfiguration;

  /**
   * Метод для передачи диагнозов диспансерного учета пациента
   *
   * @param filter json, содержащий параметры запроса
   * @return {@link ResponseEntity} содержащий представление ответа
   */
  @GetMapping("/patients")
  public ResponseEntity getPatientWithData(@RequestParam("filter") Map<String, String> filter) {

    if (CollectionUtils.isEmpty(filter)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(appConfiguration.getBadRequestResponseBody());
    }

    log.info("Получен запрос с параметрами: {}", filter);
    List<DispensaryObservationResponse> responseList =
        dispensaryObservationService.getDispensaryObservations(filter);

    if (CollectionUtils.isEmpty(responseList)) {
      log.error("Пациент с параметрами из запроса не найден");
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(appConfiguration.getPatientNotFoundResponseBody());
    }

    log.info("Пациент найден, отправлен ответ: {}", responseList);
    return ResponseEntity.status(HttpStatus.OK).body(responseList);
  }
}