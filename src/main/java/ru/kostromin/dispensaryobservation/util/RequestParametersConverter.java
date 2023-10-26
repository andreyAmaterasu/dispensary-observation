package ru.kostromin.dispensaryobservation.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Конвертер строки, содержащей json с параметрами запроса
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestParametersConverter implements Converter<String, Map<String, String>> {

  private final ObjectMapper mapper;

  @Override
  public Map<String, String> convert(String source) {

    return Try.of(() -> mapper.readTree(source))
        .fold(e -> {
          log.error("Ошибка чтения json полученного в параметре запроса: {}", e.getMessage());
          return Map.of();
        }, this::extractNodes);
  }

  private Map<String, String> extractNodes(JsonNode jsonNode) {

    Map<String, String> map = new HashMap<>();
    if (jsonNode.isArray()) {
      for (Iterator<JsonNode> jsonNodeIterator = jsonNode.elements(); jsonNodeIterator.hasNext(); ) {
        map.putAll(extractNodes(jsonNodeIterator.next()));
      }
    } else if (jsonNode.isObject()) {
      for (Iterator<String> jsonNodeIterator = jsonNode.fieldNames(); jsonNodeIterator.hasNext(); ) {
        String fieldName = jsonNodeIterator.next();
        JsonNode jsonSubNode = jsonNode.get(fieldName);
        if (jsonSubNode.isValueNode()) {
          map.put(fieldName, jsonSubNode.textValue());
        } else if (jsonSubNode.isObject()) {
          map.putAll(extractNodes(jsonSubNode));
        } else if (jsonSubNode.isArray()) {
          return extractNodes(jsonSubNode);
        }
      }
    }
    return map;
  }
}
