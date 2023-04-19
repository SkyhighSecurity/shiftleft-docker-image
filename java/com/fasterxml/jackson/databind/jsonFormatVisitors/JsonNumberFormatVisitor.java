package com.fasterxml.jackson.databind.jsonFormatVisitors;

import com.fasterxml.jackson.core.JsonParser;

public interface JsonNumberFormatVisitor extends JsonValueFormatVisitor {
  void numberType(JsonParser.NumberType paramNumberType);
  
  public static class Base extends JsonValueFormatVisitor.Base implements JsonNumberFormatVisitor {
    public void numberType(JsonParser.NumberType type) {}
  }
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonNumberFormatVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */