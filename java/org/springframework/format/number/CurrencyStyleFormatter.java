/*     */ package org.springframework.format.number;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
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
/*     */ public class CurrencyStyleFormatter
/*     */   extends AbstractNumberFormatter
/*     */ {
/*  42 */   private int fractionDigits = 2;
/*     */ 
/*     */   
/*     */   private RoundingMode roundingMode;
/*     */ 
/*     */   
/*     */   private Currency currency;
/*     */ 
/*     */   
/*     */   private String pattern;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFractionDigits(int fractionDigits) {
/*  56 */     this.fractionDigits = fractionDigits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRoundingMode(RoundingMode roundingMode) {
/*  64 */     this.roundingMode = roundingMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrency(Currency currency) {
/*  71 */     this.currency = currency;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  80 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal parse(String text, Locale locale) throws ParseException {
/*  86 */     BigDecimal decimal = (BigDecimal)super.parse(text, locale);
/*  87 */     if (decimal != null) {
/*  88 */       if (this.roundingMode != null) {
/*  89 */         decimal = decimal.setScale(this.fractionDigits, this.roundingMode);
/*     */       } else {
/*     */         
/*  92 */         decimal = decimal.setScale(this.fractionDigits);
/*     */       } 
/*     */     }
/*  95 */     return decimal;
/*     */   }
/*     */ 
/*     */   
/*     */   protected NumberFormat getNumberFormat(Locale locale) {
/* 100 */     DecimalFormat format = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
/* 101 */     format.setParseBigDecimal(true);
/* 102 */     format.setMaximumFractionDigits(this.fractionDigits);
/* 103 */     format.setMinimumFractionDigits(this.fractionDigits);
/* 104 */     if (this.roundingMode != null) {
/* 105 */       format.setRoundingMode(this.roundingMode);
/*     */     }
/* 107 */     if (this.currency != null) {
/* 108 */       format.setCurrency(this.currency);
/*     */     }
/* 110 */     if (this.pattern != null) {
/* 111 */       format.applyPattern(this.pattern);
/*     */     }
/* 113 */     return format;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\number\CurrencyStyleFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */