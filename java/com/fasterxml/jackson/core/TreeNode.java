package com.fasterxml.jackson.core;

import java.util.Iterator;

public interface TreeNode {
  JsonToken asToken();
  
  JsonParser.NumberType numberType();
  
  int size();
  
  boolean isValueNode();
  
  boolean isContainerNode();
  
  boolean isMissingNode();
  
  boolean isArray();
  
  boolean isObject();
  
  TreeNode get(String paramString);
  
  TreeNode get(int paramInt);
  
  TreeNode path(String paramString);
  
  TreeNode path(int paramInt);
  
  Iterator<String> fieldNames();
  
  TreeNode at(JsonPointer paramJsonPointer);
  
  TreeNode at(String paramString) throws IllegalArgumentException;
  
  JsonParser traverse();
  
  JsonParser traverse(ObjectCodec paramObjectCodec);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\core\TreeNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */