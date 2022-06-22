package com.stardata.starshop2.sharedcontext.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * @author Samson Shu
 * @email shush@stardata.top
 * @date 2020/7/8 11:44
 */

public class JSONUtil {
  private static ObjectMapper mapper = null;

  public static ObjectMapper jsonMapper() {
    if (mapper == null ) {
      mapper = new ObjectMapper();
    }
    return mapper;
  }

  public static JsonNode readTree(String jsonStr) throws JsonProcessingException {
    return jsonMapper().readTree(jsonStr);
  }

  public static ObjectNode createObjectNode() {
    return jsonMapper().createObjectNode();
  }

  public static String toJSONString(Object object) {
    try {
      return jsonMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public static <T> T parseObject(String jsonStr, Class<T> valueType) throws JsonProcessingException {
      return jsonMapper().readValue(jsonStr,valueType);
  }

  public static <T> T parseObject(String jsonStr, JavaType valueType) throws JsonProcessingException {
    return jsonMapper().readValue(jsonStr,valueType);
  }

  public static <T> List<T> parseList(String jsonStr, Class<T> valueType) throws JsonProcessingException {
    JavaType javaType = jsonMapper().getTypeFactory().constructCollectionType(List.class, valueType);
    return jsonMapper().readValue(jsonStr, javaType);
  }


  public static Long getLong(JsonNode jsonNode, String fieldName) {
    JsonNode node = jsonNode.get(fieldName);
    if (node == null) return null;
    String str = node.asText();
    try {
      return Long.parseLong(str);
    }catch (NumberFormatException e) {
      return null;
    }
  }
}
