# dispensary-observation

Сервис для передачи данных в ТелеМед по диспансерному учету пациентов

### Используемое ПО
- Java 17
- Spring Boot 3.0.5
- MSSQL-JDBC 12.2.0.jre11

## Сборка
```sh
mvn clean install 
```

### Описание сервиса
Сервис **DispensaryObservation** это REST сервис. \
Сервис имеет один get-метод, который передает в ответе информацию, содержащую информацию 
по диспансерному учету пациента.

### Описание параметров конфигурации (application.yaml)
Настройки подключения к БД: \
<span style="color:green;">datasource.url</span> - адрес подключения к БД \
<span style="color:green;">datasource.username</span> - имя пользователя БД \
<span style="color:green;">datasource.password</span> - пароль пользователя БД

Общие настройки для работы сервиса: \
<span style="color:green;">app.patientNotFoundResponseBody</span> - представление ответа, если 
пациент не найден \
<span style="color:green;">app.badRequestResponseBody</span> - представление ответа, если
параметры запроса не были прочитаны \
<span style="color:green;">app.sql</span> - SQL запрос для получения данных пациента

### Алгоритм работы сервиса
**1 -** Получение запроса от клиента с параметром **filter**, который представляет собой строку, 
содержащую информацию о пациенте в формате json \
**2 -** Json конвертируется в Map<String, String>, содержащий параметры для SQL запроса к БД. Если
json не удалось прочитать и конвертировать в Map<String, String>, то сервис отдаёт ответ с кодом 
400 (BAD_REQUEST), в теле которого содержится сообщение об ошибке вида: \
<sub>_*в этом случае шаг 3 не выполняется_</sub>
```json
{
  "error": "Невозможно прочитать параметры запроса"
}
```
**3 -** Выполняется запрос к БД, используя все полученные значения. \
Параметры из запроса сопоставляются с таблицами в БД по следующей схеме:

| Параметр в запросе | Поле в таблице hlt_MKAB |
|:------------------:|:-----------------------:|
|     firstName      |          NAME           |
|      lastName      |         FAMILY          |
|     middleName     |           OT            |
|    OMS: number     |          N_POL          |
|       SNILS        |           SS            |

Таблица hlt_MKAB связывается с таблицей hlt_RegMedicalCheck через hlt_MKAB.MKABID = 
hlt_RegMedicalCheck.rf_MKABID. \
Таблица hlt_RegMedicalCheck связывается с таблицами oms_MKB и oms_LPU 
через hlt_RegMedicalCheck.rf_MKBID = oms_MKB.MKBID и hlt_RegMedicalCheck.rf_LPUID = oms_LPU.LPUID.

| Параметр в запросе | Поля в таблицах oms_MKB и oms_LPU |
|:------------------:|:---------------------------------:|
|     clinicCode     |            oms_MKB.DS             |
|     diagnosis      |           oms_LPU.MCOD            |

Если данные найдены, то возвращается ответ, содержащий данные пациента и найденную информацию 
по диспансерному учету с кодом 200 (OK), например:
```json
[
  {
    "SNILS": "417-723-841 89",
    "OMS": "7501980112334349",
    "dispensaryObservation": [
      {
        "diagnosis": [
          "F05.8",
          "F19.5"
        ],
        "clinicCode": "0000157"
      }
    ],
    "lastName": "Петров",
    "firstName": "Петр",
    "middleName": "Петрович",
    "birthDate": "2023-01-01"
  }
]
```
Если данные не найдены, то сервис отдаёт ответ с кодом 404 (NOT_FOUND), в теле которого содержится 
сообщение об ошибке вида:

```json
{
  "error": "Не найден пациент"
}
```