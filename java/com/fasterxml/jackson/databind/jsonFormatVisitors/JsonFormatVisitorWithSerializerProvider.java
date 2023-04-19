package com.fasterxml.jackson.databind.jsonFormatVisitors;

import com.fasterxml.jackson.databind.SerializerProvider;

public interface JsonFormatVisitorWithSerializerProvider {
  SerializerProvider getProvider();
  
  void setProvider(SerializerProvider paramSerializerProvider);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonFormatVisitorWithSerializerProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */