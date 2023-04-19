/*    */ package org.springframework.format.number;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.NumberFormat;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NumberStyleFormatter
/*    */   extends AbstractNumberFormatter
/*    */ {
/*    */   private String pattern;
/*    */   
/*    */   public NumberStyleFormatter() {}
/*    */   
/*    */   public NumberStyleFormatter(String pattern) {
/* 54 */     this.pattern = pattern;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPattern(String pattern) {
/* 64 */     this.pattern = pattern;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public NumberFormat getNumberFormat(Locale locale) {
/* 70 */     NumberFormat format = NumberFormat.getInstance(locale);
/* 71 */     if (!(format instanceof DecimalFormat)) {
/* 72 */       if (this.pattern != null) {
/* 73 */         throw new IllegalStateException("Cannot support pattern for non-DecimalFormat: " + format);
/*    */       }
/* 75 */       return format;
/*    */     } 
/* 77 */     DecimalFormat decimalFormat = (DecimalFormat)format;
/* 78 */     decimalFormat.setParseBigDecimal(true);
/* 79 */     if (this.pattern != null) {
/* 80 */       decimalFormat.applyPattern(this.pattern);
/*    */     }
/* 82 */     return decimalFormat;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\number\NumberStyleFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */