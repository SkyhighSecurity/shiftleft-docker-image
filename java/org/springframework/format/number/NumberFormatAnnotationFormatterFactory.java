/*    */ package org.springframework.format.number;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.Set;
/*    */ import org.springframework.context.support.EmbeddedValueResolutionSupport;
/*    */ import org.springframework.format.AnnotationFormatterFactory;
/*    */ import org.springframework.format.Formatter;
/*    */ import org.springframework.format.Parser;
/*    */ import org.springframework.format.Printer;
/*    */ import org.springframework.format.annotation.NumberFormat;
/*    */ import org.springframework.util.NumberUtils;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class NumberFormatAnnotationFormatterFactory
/*    */   extends EmbeddedValueResolutionSupport
/*    */   implements AnnotationFormatterFactory<NumberFormat>
/*    */ {
/*    */   public Set<Class<?>> getFieldTypes() {
/* 44 */     return NumberUtils.STANDARD_NUMBER_TYPES;
/*    */   }
/*    */ 
/*    */   
/*    */   public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType) {
/* 49 */     return (Printer<Number>)configureFormatterFrom(annotation);
/*    */   }
/*    */ 
/*    */   
/*    */   public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType) {
/* 54 */     return (Parser<Number>)configureFormatterFrom(annotation);
/*    */   }
/*    */ 
/*    */   
/*    */   private Formatter<Number> configureFormatterFrom(NumberFormat annotation) {
/* 59 */     if (StringUtils.hasLength(annotation.pattern())) {
/* 60 */       return new NumberStyleFormatter(resolveEmbeddedValue(annotation.pattern()));
/*    */     }
/*    */     
/* 63 */     NumberFormat.Style style = annotation.style();
/* 64 */     if (style == NumberFormat.Style.CURRENCY) {
/* 65 */       return new CurrencyStyleFormatter();
/*    */     }
/* 67 */     if (style == NumberFormat.Style.PERCENT) {
/* 68 */       return new PercentStyleFormatter();
/*    */     }
/*    */     
/* 71 */     return new NumberStyleFormatter();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\number\NumberFormatAnnotationFormatterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */