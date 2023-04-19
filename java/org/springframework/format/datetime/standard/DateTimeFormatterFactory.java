/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.FormatStyle;
/*     */ import java.time.format.ResolverStyle;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.format.annotation.DateTimeFormat;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.util.Assert;
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
/*     */ @UsesJava8
/*     */ public class DateTimeFormatterFactory
/*     */ {
/*     */   private String pattern;
/*     */   private DateTimeFormat.ISO iso;
/*     */   private FormatStyle dateStyle;
/*     */   private FormatStyle timeStyle;
/*     */   private TimeZone timeZone;
/*     */   
/*     */   public DateTimeFormatterFactory() {}
/*     */   
/*     */   public DateTimeFormatterFactory(String pattern) {
/*  72 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  81 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIso(DateTimeFormat.ISO iso) {
/*  89 */     this.iso = iso;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateStyle(FormatStyle dateStyle) {
/*  96 */     this.dateStyle = dateStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeStyle(FormatStyle timeStyle) {
/* 103 */     this.timeStyle = timeStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateTimeStyle(FormatStyle dateTimeStyle) {
/* 110 */     this.dateStyle = dateTimeStyle;
/* 111 */     this.timeStyle = dateTimeStyle;
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
/*     */ 
/*     */   
/*     */   public void setStylePattern(String style) {
/* 131 */     Assert.isTrue((style != null && style.length() == 2), "Style pattern must consist of two characters");
/* 132 */     this.dateStyle = convertStyleCharacter(style.charAt(0));
/* 133 */     this.timeStyle = convertStyleCharacter(style.charAt(1));
/*     */   }
/*     */   
/*     */   private FormatStyle convertStyleCharacter(char c) {
/* 137 */     switch (c) { case 'S':
/* 138 */         return FormatStyle.SHORT;
/* 139 */       case 'M': return FormatStyle.MEDIUM;
/* 140 */       case 'L': return FormatStyle.LONG;
/* 141 */       case 'F': return FormatStyle.FULL;
/* 142 */       case '-': return null; }
/* 143 */      throw new IllegalArgumentException("Invalid style character '" + c + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 152 */     this.timeZone = timeZone;
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
/*     */   public DateTimeFormatter createDateTimeFormatter() {
/* 164 */     return createDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
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
/*     */   public DateTimeFormatter createDateTimeFormatter(DateTimeFormatter fallbackFormatter) {
/* 176 */     DateTimeFormatter dateTimeFormatter = null;
/* 177 */     if (StringUtils.hasLength(this.pattern)) {
/*     */ 
/*     */ 
/*     */       
/* 181 */       String patternToUse = this.pattern.replace("yy", "uu");
/* 182 */       dateTimeFormatter = DateTimeFormatter.ofPattern(patternToUse).withResolverStyle(ResolverStyle.STRICT);
/*     */     }
/* 184 */     else if (this.iso != null && this.iso != DateTimeFormat.ISO.NONE) {
/* 185 */       switch (this.iso) {
/*     */         case DATE:
/* 187 */           dateTimeFormatter = DateTimeFormatter.ISO_DATE;
/*     */           break;
/*     */         case TIME:
/* 190 */           dateTimeFormatter = DateTimeFormatter.ISO_TIME;
/*     */           break;
/*     */         case DATE_TIME:
/* 193 */           dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
/*     */           break;
/*     */         
/*     */         case NONE:
/*     */           break;
/*     */         default:
/* 199 */           throw new IllegalStateException("Unsupported ISO format: " + this.iso);
/*     */       } 
/*     */     
/* 202 */     } else if (this.dateStyle != null && this.timeStyle != null) {
/* 203 */       dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(this.dateStyle, this.timeStyle);
/*     */     }
/* 205 */     else if (this.dateStyle != null) {
/* 206 */       dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(this.dateStyle);
/*     */     }
/* 208 */     else if (this.timeStyle != null) {
/* 209 */       dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(this.timeStyle);
/*     */     } 
/*     */     
/* 212 */     if (dateTimeFormatter != null && this.timeZone != null) {
/* 213 */       dateTimeFormatter = dateTimeFormatter.withZone(this.timeZone.toZoneId());
/*     */     }
/* 215 */     return (dateTimeFormatter != null) ? dateTimeFormatter : fallbackFormatter;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\standard\DateTimeFormatterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */