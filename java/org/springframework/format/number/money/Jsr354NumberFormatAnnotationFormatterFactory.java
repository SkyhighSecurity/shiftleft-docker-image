/*     */ package org.springframework.format.number.money;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collections;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.money.CurrencyUnit;
/*     */ import javax.money.Monetary;
/*     */ import javax.money.MonetaryAmount;
/*     */ import org.springframework.context.support.EmbeddedValueResolutionSupport;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.Parser;
/*     */ import org.springframework.format.Printer;
/*     */ import org.springframework.format.annotation.NumberFormat;
/*     */ import org.springframework.format.number.CurrencyStyleFormatter;
/*     */ import org.springframework.format.number.NumberStyleFormatter;
/*     */ import org.springframework.format.number.PercentStyleFormatter;
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
/*     */ public class Jsr354NumberFormatAnnotationFormatterFactory
/*     */   extends EmbeddedValueResolutionSupport
/*     */   implements AnnotationFormatterFactory<NumberFormat>
/*     */ {
/*     */   private static final String CURRENCY_CODE_PATTERN = "¤¤";
/*     */   
/*     */   public Set<Class<?>> getFieldTypes() {
/*  57 */     return Collections.singleton(MonetaryAmount.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public Printer<MonetaryAmount> getPrinter(NumberFormat annotation, Class<?> fieldType) {
/*  62 */     return (Printer<MonetaryAmount>)configureFormatterFrom(annotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public Parser<MonetaryAmount> getParser(NumberFormat annotation, Class<?> fieldType) {
/*  67 */     return (Parser<MonetaryAmount>)configureFormatterFrom(annotation);
/*     */   }
/*     */ 
/*     */   
/*     */   private Formatter<MonetaryAmount> configureFormatterFrom(NumberFormat annotation) {
/*  72 */     if (StringUtils.hasLength(annotation.pattern())) {
/*  73 */       return new PatternDecoratingFormatter(resolveEmbeddedValue(annotation.pattern()));
/*     */     }
/*     */     
/*  76 */     NumberFormat.Style style = annotation.style();
/*  77 */     if (style == NumberFormat.Style.NUMBER) {
/*  78 */       return new NumberDecoratingFormatter((Formatter<Number>)new NumberStyleFormatter());
/*     */     }
/*  80 */     if (style == NumberFormat.Style.PERCENT) {
/*  81 */       return new NumberDecoratingFormatter((Formatter<Number>)new PercentStyleFormatter());
/*     */     }
/*     */     
/*  84 */     return new NumberDecoratingFormatter((Formatter<Number>)new CurrencyStyleFormatter());
/*     */   }
/*     */ 
/*     */   
/*     */   private static class NumberDecoratingFormatter
/*     */     implements Formatter<MonetaryAmount>
/*     */   {
/*     */     private final Formatter<Number> numberFormatter;
/*     */ 
/*     */     
/*     */     public NumberDecoratingFormatter(Formatter<Number> numberFormatter) {
/*  95 */       this.numberFormatter = numberFormatter;
/*     */     }
/*     */ 
/*     */     
/*     */     public String print(MonetaryAmount object, Locale locale) {
/* 100 */       return this.numberFormatter.print(object.getNumber(), locale);
/*     */     }
/*     */ 
/*     */     
/*     */     public MonetaryAmount parse(String text, Locale locale) throws ParseException {
/* 105 */       CurrencyUnit currencyUnit = Monetary.getCurrency(locale, new String[0]);
/* 106 */       Number numberValue = (Number)this.numberFormatter.parse(text, locale);
/* 107 */       return Monetary.getDefaultAmountFactory().setNumber(numberValue).setCurrency(currencyUnit).create();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PatternDecoratingFormatter
/*     */     implements Formatter<MonetaryAmount>
/*     */   {
/*     */     private final String pattern;
/*     */     
/*     */     public PatternDecoratingFormatter(String pattern) {
/* 117 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public String print(MonetaryAmount object, Locale locale) {
/* 122 */       CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();
/* 123 */       formatter.setCurrency(Currency.getInstance(object.getCurrency().getCurrencyCode()));
/* 124 */       formatter.setPattern(this.pattern);
/* 125 */       return formatter.print((Number)object.getNumber(), locale);
/*     */     }
/*     */ 
/*     */     
/*     */     public MonetaryAmount parse(String text, Locale locale) throws ParseException {
/* 130 */       CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();
/* 131 */       Currency currency = determineCurrency(text, locale);
/* 132 */       CurrencyUnit currencyUnit = Monetary.getCurrency(currency.getCurrencyCode(), new String[0]);
/* 133 */       formatter.setCurrency(currency);
/* 134 */       formatter.setPattern(this.pattern);
/* 135 */       Number numberValue = formatter.parse(text, locale);
/* 136 */       return Monetary.getDefaultAmountFactory().setNumber(numberValue).setCurrency(currencyUnit).create();
/*     */     }
/*     */     
/*     */     private Currency determineCurrency(String text, Locale locale) {
/*     */       try {
/* 141 */         if (text.length() < 3)
/*     */         {
/*     */           
/* 144 */           return Currency.getInstance(locale);
/*     */         }
/* 146 */         if (this.pattern.startsWith("¤¤")) {
/* 147 */           return Currency.getInstance(text.substring(0, 3));
/*     */         }
/* 149 */         if (this.pattern.endsWith("¤¤")) {
/* 150 */           return Currency.getInstance(text.substring(text.length() - 3));
/*     */         }
/*     */ 
/*     */         
/* 154 */         return Currency.getInstance(locale);
/*     */       
/*     */       }
/* 157 */       catch (IllegalArgumentException ex) {
/* 158 */         throw new IllegalArgumentException("Cannot determine currency for number value [" + text + "]", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\number\money\Jsr354NumberFormatAnnotationFormatterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */