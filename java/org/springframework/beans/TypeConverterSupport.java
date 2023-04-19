/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.core.convert.ConversionException;
/*    */ import org.springframework.core.convert.ConverterNotFoundException;
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
/*    */ public abstract class TypeConverterSupport
/*    */   extends PropertyEditorRegistrySupport
/*    */   implements TypeConverter
/*    */ {
/*    */   TypeConverterDelegate typeConverterDelegate;
/*    */   
/*    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
/* 40 */     return doConvert(value, requiredType, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam) throws TypeMismatchException {
/* 47 */     return doConvert(value, requiredType, methodParam, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field) throws TypeMismatchException {
/* 54 */     return doConvert(value, requiredType, null, field);
/*    */   }
/*    */ 
/*    */   
/*    */   private <T> T doConvert(Object value, Class<T> requiredType, MethodParameter methodParam, Field field) throws TypeMismatchException {
/*    */     try {
/* 60 */       if (field != null) {
/* 61 */         return this.typeConverterDelegate.convertIfNecessary(value, requiredType, field);
/*    */       }
/*    */       
/* 64 */       return this.typeConverterDelegate.convertIfNecessary(value, requiredType, methodParam);
/*    */     
/*    */     }
/* 67 */     catch (ConverterNotFoundException ex) {
/* 68 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/*    */     }
/* 70 */     catch (ConversionException ex) {
/* 71 */       throw new TypeMismatchException(value, requiredType, ex);
/*    */     }
/* 73 */     catch (IllegalStateException ex) {
/* 74 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/*    */     }
/* 76 */     catch (IllegalArgumentException ex) {
/* 77 */       throw new TypeMismatchException(value, requiredType, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\TypeConverterSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */