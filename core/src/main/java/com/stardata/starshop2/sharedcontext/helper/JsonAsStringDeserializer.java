package com.stardata.starshop2.sharedcontext.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @author Samson Shu
 * @email shush@stardata.top
 * @date 2020/7/8 15:12
 */

public class JsonAsStringDeserializer extends JsonDeserializer<String> {
  @Override
  public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    TreeNode tree = jsonParser.getCodec().readTree(jsonParser);
    return tree.toString();
  }
}
