package org.omar.recipemanagementsystem.shareUtil;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;


public final class JsonUtil {

  public  static String textValue(JsonNode node, String field) {
    if (!node.isTextual()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              "Field '" + field + "' must be a string");
    }
    return node.textValue();
  }

  public static List<String> stringList(JsonNode node, String field) {
    if (!node.isArray()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              "Field '" + field + "' must be an array");
    }
    List<String> values = new ArrayList<>();
    for (JsonNode element : node) {
      if (!element.isTextual()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Field '" + field + "' must only contain strings");
      }
      values.add(element.textValue());
    }
    return values;
  }
}
