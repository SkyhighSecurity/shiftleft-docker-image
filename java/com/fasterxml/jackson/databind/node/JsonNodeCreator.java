package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.util.RawValue;
import java.math.BigDecimal;
import java.math.BigInteger;

public interface JsonNodeCreator {
  ValueNode booleanNode(boolean paramBoolean);
  
  ValueNode nullNode();
  
  ValueNode numberNode(byte paramByte);
  
  ValueNode numberNode(Byte paramByte);
  
  ValueNode numberNode(short paramShort);
  
  ValueNode numberNode(Short paramShort);
  
  ValueNode numberNode(int paramInt);
  
  ValueNode numberNode(Integer paramInteger);
  
  ValueNode numberNode(long paramLong);
  
  ValueNode numberNode(Long paramLong);
  
  ValueNode numberNode(BigInteger paramBigInteger);
  
  ValueNode numberNode(float paramFloat);
  
  ValueNode numberNode(Float paramFloat);
  
  ValueNode numberNode(double paramDouble);
  
  ValueNode numberNode(Double paramDouble);
  
  ValueNode numberNode(BigDecimal paramBigDecimal);
  
  ValueNode textNode(String paramString);
  
  ValueNode binaryNode(byte[] paramArrayOfbyte);
  
  ValueNode binaryNode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  ValueNode pojoNode(Object paramObject);
  
  ValueNode rawValueNode(RawValue paramRawValue);
  
  ArrayNode arrayNode();
  
  ArrayNode arrayNode(int paramInt);
  
  ObjectNode objectNode();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\JsonNodeCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */