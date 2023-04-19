/*     */ package org.springframework.format.datetime;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.annotation.DateTimeFormat;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateFormatter
/*     */   implements Formatter<Date>
/*     */ {
/*  45 */   private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
/*     */   private static final Map<DateTimeFormat.ISO, String> ISO_PATTERNS;
/*     */   private String pattern;
/*     */   
/*     */   static {
/*  50 */     Map<DateTimeFormat.ISO, String> formats = new EnumMap<DateTimeFormat.ISO, String>(DateTimeFormat.ISO.class);
/*  51 */     formats.put(DateTimeFormat.ISO.DATE, "yyyy-MM-dd");
/*  52 */     formats.put(DateTimeFormat.ISO.TIME, "HH:mm:ss.SSSZ");
/*  53 */     formats.put(DateTimeFormat.ISO.DATE_TIME, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
/*  54 */     ISO_PATTERNS = Collections.unmodifiableMap(formats);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private int style = 2;
/*     */ 
/*     */ 
/*     */   
/*     */   private String stylePattern;
/*     */ 
/*     */ 
/*     */   
/*     */   private DateTimeFormat.ISO iso;
/*     */ 
/*     */ 
/*     */   
/*     */   private TimeZone timeZone;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean lenient = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public DateFormatter(String pattern) {
/*  81 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  90 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIso(DateTimeFormat.ISO iso) {
/*  99 */     this.iso = iso;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStyle(int style) {
/* 112 */     this.style = style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStylePattern(String stylePattern) {
/* 130 */     this.stylePattern = stylePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 137 */     this.timeZone = timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLenient(boolean lenient) {
/* 146 */     this.lenient = lenient;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String print(Date date, Locale locale) {
/* 152 */     return getDateFormat(locale).format(date);
/*     */   }
/*     */ 
/*     */   
/*     */   public Date parse(String text, Locale locale) throws ParseException {
/* 157 */     return getDateFormat(locale).parse(text);
/*     */   }
/*     */ 
/*     */   
/*     */   protected DateFormat getDateFormat(Locale locale) {
/* 162 */     DateFormat dateFormat = createDateFormat(locale);
/* 163 */     if (this.timeZone != null) {
/* 164 */       dateFormat.setTimeZone(this.timeZone);
/*     */     }
/* 166 */     dateFormat.setLenient(this.lenient);
/* 167 */     return dateFormat;
/*     */   }
/*     */   
/*     */   private DateFormat createDateFormat(Locale locale) {
/* 171 */     if (StringUtils.hasLength(this.pattern)) {
/* 172 */       return new SimpleDateFormat(this.pattern, locale);
/*     */     }
/* 174 */     if (this.iso != null && this.iso != DateTimeFormat.ISO.NONE) {
/* 175 */       String pattern = ISO_PATTERNS.get(this.iso);
/* 176 */       if (pattern == null) {
/* 177 */         throw new IllegalStateException("Unsupported ISO format " + this.iso);
/*     */       }
/* 179 */       SimpleDateFormat format = new SimpleDateFormat(pattern);
/* 180 */       format.setTimeZone(UTC);
/* 181 */       return format;
/*     */     } 
/* 183 */     if (StringUtils.hasLength(this.stylePattern)) {
/* 184 */       int dateStyle = getStylePatternForChar(0);
/* 185 */       int timeStyle = getStylePatternForChar(1);
/* 186 */       if (dateStyle != -1 && timeStyle != -1) {
/* 187 */         return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
/*     */       }
/* 189 */       if (dateStyle != -1) {
/* 190 */         return DateFormat.getDateInstance(dateStyle, locale);
/*     */       }
/* 192 */       if (timeStyle != -1) {
/* 193 */         return DateFormat.getTimeInstance(timeStyle, locale);
/*     */       }
/* 195 */       throw new IllegalStateException("Unsupported style pattern '" + this.stylePattern + "'");
/*     */     } 
/*     */     
/* 198 */     return DateFormat.getDateInstance(this.style, locale);
/*     */   }
/*     */   
/*     */   private int getStylePatternForChar(int index) {
/* 202 */     if (this.stylePattern != null && this.stylePattern.length() > index) {
/* 203 */       switch (this.stylePattern.charAt(index)) { case 'S':
/* 204 */           return 3;
/* 205 */         case 'M': return 2;
/* 206 */         case 'L': return 1;
/* 207 */         case 'F': return 0;
/* 208 */         case '-': return -1; }
/*     */     
/*     */     }
/* 211 */     throw new IllegalStateException("Unsupported style pattern '" + this.stylePattern + "'");
/*     */   }
/*     */   
/*     */   public DateFormatter() {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\DateFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */