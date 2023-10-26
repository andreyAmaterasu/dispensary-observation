package ru.kostromin.dispensaryobservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;

/**
 * Представление диспансерного учета
 *
 * @param clinicCode код МО
 * @param diagnoses {@link List} диагнозов
 */
@Builder
public record DispensaryObservation(String clinicCode, @JsonProperty("diagnosis") List<String> diagnoses) {

}
