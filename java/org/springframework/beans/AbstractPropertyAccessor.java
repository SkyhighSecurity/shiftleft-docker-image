/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public abstract class AbstractPropertyAccessor
/*     */   extends TypeConverterSupport
/*     */   implements ConfigurablePropertyAccessor
/*     */ {
/*     */   private boolean extractOldValueForEditor = false;
/*     */   private boolean autoGrowNestedPaths = false;
/*     */   
/*     */   public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {
/*  44 */     this.extractOldValueForEditor = extractOldValueForEditor;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExtractOldValueForEditor() {
/*  49 */     return this.extractOldValueForEditor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
/*  54 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowNestedPaths() {
/*  59 */     return this.autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyValue(PropertyValue pv) throws BeansException {
/*  65 */     setPropertyValue(pv.getName(), pv.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyValues(Map<?, ?> map) throws BeansException {
/*  70 */     setPropertyValues(new MutablePropertyValues(map));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyValues(PropertyValues pvs) throws BeansException {
/*  75 */     setPropertyValues(pvs, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown) throws BeansException {
/*  80 */     setPropertyValues(pvs, ignoreUnknown, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown, boolean ignoreInvalid) throws BeansException {
/*  87 */     List<PropertyAccessException> propertyAccessExceptions = null;
/*     */     
/*  89 */     List<PropertyValue> propertyValues = (pvs instanceof MutablePropertyValues) ? ((MutablePropertyValues)pvs).getPropertyValueList() : Arrays.<PropertyValue>asList(pvs.getPropertyValues());
/*  90 */     for (PropertyValue pv : propertyValues) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  95 */         setPropertyValue(pv);
/*     */       }
/*  97 */       catch (NotWritablePropertyException ex) {
/*  98 */         if (!ignoreUnknown) {
/*  99 */           throw ex;
/*     */         
/*     */         }
/*     */       }
/* 103 */       catch (NullValueInNestedPathException ex) {
/* 104 */         if (!ignoreInvalid) {
/* 105 */           throw ex;
/*     */         
/*     */         }
/*     */       }
/* 109 */       catch (PropertyAccessException ex) {
/* 110 */         if (propertyAccessExceptions == null) {
/* 111 */           propertyAccessExceptions = new LinkedList<PropertyAccessException>();
/*     */         }
/* 113 */         propertyAccessExceptions.add(ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 118 */     if (propertyAccessExceptions != null) {
/*     */       
/* 120 */       PropertyAccessException[] paeArray = propertyAccessExceptions.<PropertyAccessException>toArray(new PropertyAccessException[propertyAccessExceptions.size()]);
/* 121 */       throw new PropertyBatchUpdateException(paeArray);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getPropertyType(String propertyPath) {
/* 129 */     return null;
/*     */   }
/*     */   
/*     */   public abstract Object getPropertyValue(String paramString) throws BeansException;
/*     */   
/*     */   public abstract void setPropertyValue(String paramString, Object paramObject) throws BeansException;
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\AbstractPropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */