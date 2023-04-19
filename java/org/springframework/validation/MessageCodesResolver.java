package org.springframework.validation;

public interface MessageCodesResolver {
  String[] resolveMessageCodes(String paramString1, String paramString2);
  
  String[] resolveMessageCodes(String paramString1, String paramString2, String paramString3, Class<?> paramClass);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\MessageCodesResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */