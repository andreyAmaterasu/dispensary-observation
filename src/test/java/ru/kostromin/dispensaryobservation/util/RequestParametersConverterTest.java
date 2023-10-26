package ru.kostromin.dispensaryobservation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RequestParametersConverterTest {

  private RequestParametersConverter requestParametersConverter;

  @BeforeEach
  void setup() {

    requestParametersConverter = new RequestParametersConverter(new ObjectMapper());
  }

  @Test
  @DisplayName("Полученная строка json с параметрами конвертирована в Map")
  void convertSuccess() {

    String filter = "{\"$and\":[{\"lastName\":\"Колташева\"},{\"firstName\":\"Александра\"},{\"middleName\":\"Сергеевна\"},{\"birthDate\":\"1987-10-26\"},{\"SNILS\":\"16067057157\"},{\"OMS\":{\"number\":\"706265163962915\"}}]}";
    Map<String, String> map = requestParametersConverter.convert(filter);
    Assertions.assertNotNull(map);
    Assertions.assertEquals(6, map.size());
    Assertions.assertTrue(map.containsKey("lastName"));
    Assertions.assertTrue(map.containsKey("firstName"));
    Assertions.assertTrue(map.containsKey("middleName"));
    Assertions.assertTrue(map.containsKey("birthDate"));
    Assertions.assertTrue(map.containsKey("SNILS"));
    Assertions.assertTrue(map.containsKey("number"));
    Assertions.assertEquals("Колташева", map.get("lastName"));
    Assertions.assertEquals("Александра", map.get("firstName"));
    Assertions.assertEquals("Сергеевна", map.get("middleName"));
    Assertions.assertEquals("1987-10-26", map.get("birthDate"));
    Assertions.assertEquals("16067057157", map.get("SNILS"));
    Assertions.assertEquals("706265163962915", map.get("number"));
  }

  @Test
  @DisplayName("Полученная строка json с параметрами не была прочитана, возвращается пустая Map")
  void convertFailed() {

    String filter = "{\"$and\"{\"lastName\":\"Колташева\"},{\"firstName\":\"Александра\"},{\"middleName\":\"Сергеевна\"},{\"birthDate\":\"1987-10-26\"},{\"SNILS\":\"16067057157\"},{\"OMS\":{\"number\":\"706265163962915\"}}]}";
    Map<String, String> map = requestParametersConverter.convert(filter);
    Assertions.assertNotNull(map);
    Assertions.assertEquals(0, map.size());
  }
}
