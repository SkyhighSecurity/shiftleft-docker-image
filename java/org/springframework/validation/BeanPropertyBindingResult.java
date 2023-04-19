/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
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
/*     */ public class BeanPropertyBindingResult
/*     */   extends AbstractPropertyBindingResult
/*     */   implements Serializable
/*     */ {
/*     */   private final Object target;
/*     */   private final boolean autoGrowNestedPaths;
/*     */   private final int autoGrowCollectionLimit;
/*     */   private transient BeanWrapper beanWrapper;
/*     */   
/*     */   public BeanPropertyBindingResult(Object target, String objectName) {
/*  60 */     this(target, objectName, true, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanPropertyBindingResult(Object target, String objectName, boolean autoGrowNestedPaths, int autoGrowCollectionLimit) {
/*  71 */     super(objectName);
/*  72 */     this.target = target;
/*  73 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*  74 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getTarget() {
/*  80 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConfigurablePropertyAccessor getPropertyAccessor() {
/*  90 */     if (this.beanWrapper == null) {
/*  91 */       this.beanWrapper = createBeanWrapper();
/*  92 */       this.beanWrapper.setExtractOldValueForEditor(true);
/*  93 */       this.beanWrapper.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/*  94 */       this.beanWrapper.setAutoGrowCollectionLimit(this.autoGrowCollectionLimit);
/*     */     } 
/*  96 */     return (ConfigurablePropertyAccessor)this.beanWrapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanWrapper createBeanWrapper() {
/* 104 */     if (this.target == null) {
/* 105 */       throw new IllegalStateException("Cannot access properties on null bean instance '" + getObjectName() + "'");
/*     */     }
/* 107 */     return PropertyAccessorFactory.forBeanPropertyAccess(this.target);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\BeanPropertyBindingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */