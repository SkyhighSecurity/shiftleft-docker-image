/*    */ package org.springframework.format.number.money;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.util.Locale;
/*    */ import javax.money.MonetaryAmount;
/*    */ import javax.money.format.MonetaryAmountFormat;
/*    */ import javax.money.format.MonetaryFormats;
/*    */ import org.springframework.format.Formatter;
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
/*    */ public class MonetaryAmountFormatter
/*    */   implements Formatter<MonetaryAmount>
/*    */ {
/*    */   private String formatName;
/*    */   
/*    */   public MonetaryAmountFormatter() {}
/*    */   
/*    */   public MonetaryAmountFormatter(String formatName) {
/* 52 */     this.formatName = formatName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFormatName(String formatName) {
/* 63 */     this.formatName = formatName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String print(MonetaryAmount object, Locale locale) {
/* 69 */     return getMonetaryAmountFormat(locale).format(object);
/*    */   }
/*    */ 
/*    */   
/*    */   public MonetaryAmount parse(String text, Locale locale) {
/* 74 */     return getMonetaryAmountFormat(locale).parse(text);
/*    */   }
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
/*    */   protected MonetaryAmountFormat getMonetaryAmountFormat(Locale locale) {
/* 88 */     if (this.formatName != null) {
/* 89 */       return MonetaryFormats.getAmountFormat(this.formatName, new String[0]);
/*    */     }
/*    */     
/* 92 */     return MonetaryFormats.getAmountFormat(locale, new String[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\number\money\MonetaryAmountFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */