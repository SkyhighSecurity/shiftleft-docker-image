package org.springframework.core.style;

public interface ToStringStyler {
  void styleStart(StringBuilder paramStringBuilder, Object paramObject);
  
  void styleEnd(StringBuilder paramStringBuilder, Object paramObject);
  
  void styleField(StringBuilder paramStringBuilder, String paramString, Object paramObject);
  
  void styleValue(StringBuilder paramStringBuilder, Object paramObject);
  
  void styleFieldSeparator(StringBuilder paramStringBuilder);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\style\ToStringStyler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */