/*     */ package org.springframework.core.convert.converter;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.comparator.ComparableComparator;
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
/*     */ public class ConvertingComparator<S, T>
/*     */   implements Comparator<S>
/*     */ {
/*     */   private final Comparator<T> comparator;
/*     */   private final Converter<S, T> converter;
/*     */   
/*     */   public ConvertingComparator(Converter<S, T> converter) {
/*  49 */     this((Comparator<T>)ComparableComparator.INSTANCE, converter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConvertingComparator(Comparator<T> comparator, Converter<S, T> converter) {
/*  58 */     Assert.notNull(comparator, "Comparator must not be null");
/*  59 */     Assert.notNull(converter, "Converter must not be null");
/*  60 */     this.comparator = comparator;
/*  61 */     this.converter = converter;
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
/*     */   public ConvertingComparator(Comparator<T> comparator, ConversionService conversionService, Class<? extends T> targetType) {
/*  73 */     this(comparator, new ConversionServiceConverter<S, T>(conversionService, targetType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(S o1, S o2) {
/*  79 */     T c1 = this.converter.convert(o1);
/*  80 */     T c2 = this.converter.convert(o2);
/*  81 */     return this.comparator.compare(c1, c2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ConvertingComparator<Map.Entry<K, V>, K> mapEntryKeys(Comparator<K> comparator) {
/*  91 */     return new ConvertingComparator<Map.Entry<K, V>, K>(comparator, new Converter<Map.Entry<K, V>, K>()
/*     */         {
/*     */           public K convert(Map.Entry<K, V> source) {
/*  94 */             return source.getKey();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ConvertingComparator<Map.Entry<K, V>, V> mapEntryValues(Comparator<V> comparator) {
/* 106 */     return new ConvertingComparator<Map.Entry<K, V>, V>(comparator, new Converter<Map.Entry<K, V>, V>()
/*     */         {
/*     */           public V convert(Map.Entry<K, V> source) {
/* 109 */             return source.getValue();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConversionServiceConverter<S, T>
/*     */     implements Converter<S, T>
/*     */   {
/*     */     private final ConversionService conversionService;
/*     */ 
/*     */     
/*     */     private final Class<? extends T> targetType;
/*     */ 
/*     */     
/*     */     public ConversionServiceConverter(ConversionService conversionService, Class<? extends T> targetType) {
/* 126 */       Assert.notNull(conversionService, "ConversionService must not be null");
/* 127 */       Assert.notNull(targetType, "TargetType must not be null");
/* 128 */       this.conversionService = conversionService;
/* 129 */       this.targetType = targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     public T convert(S source) {
/* 134 */       return (T)this.conversionService.convert(source, this.targetType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\converter\ConvertingComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */