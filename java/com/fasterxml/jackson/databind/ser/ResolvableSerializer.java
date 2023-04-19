package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public interface ResolvableSerializer {
  void resolve(SerializerProvider paramSerializerProvider) throws JsonMappingException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\ResolvableSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */