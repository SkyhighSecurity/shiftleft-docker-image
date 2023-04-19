package org.springframework.context;

import java.util.Locale;

public interface MessageSource {
  String getMessage(String paramString1, Object[] paramArrayOfObject, String paramString2, Locale paramLocale);
  
  String getMessage(String paramString, Object[] paramArrayOfObject, Locale paramLocale) throws NoSuchMessageException;
  
  String getMessage(MessageSourceResolvable paramMessageSourceResolvable, Locale paramLocale) throws NoSuchMessageException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\MessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */