package org.springframework.format;

import java.util.Locale;

public interface Printer<T> {
  String print(T paramT, Locale paramLocale);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\Printer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */