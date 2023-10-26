package ru.kostromin.dispensaryobservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.Builder;

/**
 * Представление ответа, содержащее данные пациента и диспансерного учета
 *
 * @param lastName Фамилия
 * @param firstName Имя
 * @param middleName Отчество
 * @param birthDate Дата рождения
 * @param snils СНИЛС
 * @param omsNumber Номер полиса ОМС
 * @param dispensaryObservations {@link List} содержащий представления диспансерного учета
 */
@Builder
public record DispensaryObservationResponse(String lastName,
                                            String firstName,
                                            String middleName,
                                            Date birthDate,
                                            @JsonProperty("SNILS")
                                            String snils,
                                            @JsonProperty("OMS")
                                            String omsNumber,
                                            @JsonProperty("dispensaryObservation")
                                            List<DispensaryObservation> dispensaryObservations) {

}
