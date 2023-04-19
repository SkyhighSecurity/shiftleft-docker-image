package org.springframework.format;

import java.text.ParseException;
import java.util.Locale;

public interface Parser<T> {
  T parse(String paramString, Locale paramLocale) throws ParseException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */