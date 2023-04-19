package com.fasterxml.jackson.databind.jsontype;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import java.util.Collection;

public interface TypeResolverBuilder<T extends TypeResolverBuilder<T>> {
  Class<?> getDefaultImpl();
  
  TypeSerializer buildTypeSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, Collection<NamedType> paramCollection);
  
  TypeDeserializer buildTypeDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, Collection<NamedType> paramCollection);
  
  T init(JsonTypeInfo.Id paramId, TypeIdResolver paramTypeIdResolver);
  
  T inclusion(JsonTypeInfo.As paramAs);
  
  T typeProperty(String paramString);
  
  T defaultImpl(Class<?> paramClass);
  
  T typeIdVisibility(boolean paramBoolean);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\TypeResolverBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */