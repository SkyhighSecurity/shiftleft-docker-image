package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.AccessPattern;

public interface NullValueProvider {
  Object getNullValue(DeserializationContext paramDeserializationContext) throws JsonMappingException;
  
  AccessPattern getNullAccessPattern();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\NullValueProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */