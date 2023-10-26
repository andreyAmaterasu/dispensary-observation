package ru.kostromin.dispensaryobservation.dto;

import lombok.Getter;

/**
 * Перечисление полей SQL запроса
 */
@Getter
public enum DispensaryObservationSqlFields {

  LAST_NAME("lastName"),
  FIRST_NAME("firstName"),
  MIDDLE_NAME("middleName"),
  BIRTH_DATE("birthDate"),
  SNILS("snils"),
  OMS_NUMBER("omsNumber"),
  CLINIC_CODE("clinicCode"),
  DIAGNOSIS("diagnosis");

  private final String columnName;

  DispensaryObservationSqlFields(String columnName) {
    this.columnName = columnName;
  }
}
