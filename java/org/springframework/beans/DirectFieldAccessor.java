/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class DirectFieldAccessor
/*     */   extends AbstractNestablePropertyAccessor
/*     */ {
/*  49 */   private final Map<String, FieldPropertyHandler> fieldMap = new HashMap<String, FieldPropertyHandler>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectFieldAccessor(Object object) {
/*  57 */     super(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DirectFieldAccessor(Object object, String nestedPath, DirectFieldAccessor parent) {
/*  68 */     super(object, nestedPath, parent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FieldPropertyHandler getLocalPropertyHandler(String propertyName) {
/*  74 */     FieldPropertyHandler propertyHandler = this.fieldMap.get(propertyName);
/*  75 */     if (propertyHandler == null) {
/*  76 */       Field field = ReflectionUtils.findField(getWrappedClass(), propertyName);
/*  77 */       if (field != null) {
/*  78 */         propertyHandler = new FieldPropertyHandler(field);
/*  79 */         this.fieldMap.put(propertyName, propertyHandler);
/*     */       } 
/*     */     } 
/*  82 */     return propertyHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DirectFieldAccessor newNestedPropertyAccessor(Object object, String nestedPath) {
/*  87 */     return new DirectFieldAccessor(object, nestedPath, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected NotWritablePropertyException createNotWritablePropertyException(String propertyName) {
/*  92 */     PropertyMatches matches = PropertyMatches.forField(propertyName, getRootClass());
/*  93 */     throw new NotWritablePropertyException(
/*  94 */         getRootClass(), getNestedPath() + propertyName, matches
/*  95 */         .buildErrorMessage(), matches.getPossibleMatches());
/*     */   }
/*     */   
/*     */   private class FieldPropertyHandler
/*     */     extends AbstractNestablePropertyAccessor.PropertyHandler
/*     */   {
/*     */     private final Field field;
/*     */     
/*     */     public FieldPropertyHandler(Field field) {
/* 104 */       super(field.getType(), true, true);
/* 105 */       this.field = field;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeDescriptor toTypeDescriptor() {
/* 110 */       return new TypeDescriptor(this.field);
/*     */     }
/*     */ 
/*     */     
/*     */     public ResolvableType getResolvableType() {
/* 115 */       return ResolvableType.forField(this.field);
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeDescriptor nested(int level) {
/* 120 */       return TypeDescriptor.nested(this.field, level);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValue() throws Exception {
/*     */       try {
/* 126 */         ReflectionUtils.makeAccessible(this.field);
/* 127 */         return this.field.get(DirectFieldAccessor.this.getWrappedInstance());
/*     */       
/*     */       }
/* 130 */       catch (IllegalAccessException ex) {
/* 131 */         throw new InvalidPropertyException(DirectFieldAccessor.this.getWrappedClass(), this.field
/* 132 */             .getName(), "Field is not accessible", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(Object object, Object value) throws Exception {
/*     */       try {
/* 139 */         ReflectionUtils.makeAccessible(this.field);
/* 140 */         this.field.set(object, value);
/*     */       }
/* 142 */       catch (IllegalAccessException ex) {
/* 143 */         throw new InvalidPropertyException(DirectFieldAccessor.this.getWrappedClass(), this.field.getName(), "Field is not accessible", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\DirectFieldAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */