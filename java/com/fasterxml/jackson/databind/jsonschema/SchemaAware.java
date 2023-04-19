package com.fasterxml.jackson.databind.jsonschema;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.lang.reflect.Type;

public interface SchemaAware {
  JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType) throws JsonMappingException;
  
  JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType, boolean paramBoolean) throws JsonMappingException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsonschema\SchemaAware.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */