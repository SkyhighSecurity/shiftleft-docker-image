/*    */ package org.springframework.validation;
/*    */ 
/*    */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*    */ import org.springframework.beans.PropertyAccessorFactory;
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
/*    */ public class DirectFieldBindingResult
/*    */   extends AbstractPropertyBindingResult
/*    */ {
/*    */   private final Object target;
/*    */   private final boolean autoGrowNestedPaths;
/*    */   private transient ConfigurablePropertyAccessor directFieldAccessor;
/*    */   
/*    */   public DirectFieldBindingResult(Object target, String objectName) {
/* 51 */     this(target, objectName, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DirectFieldBindingResult(Object target, String objectName, boolean autoGrowNestedPaths) {
/* 61 */     super(objectName);
/* 62 */     this.target = target;
/* 63 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final Object getTarget() {
/* 69 */     return this.target;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final ConfigurablePropertyAccessor getPropertyAccessor() {
/* 79 */     if (this.directFieldAccessor == null) {
/* 80 */       this.directFieldAccessor = createDirectFieldAccessor();
/* 81 */       this.directFieldAccessor.setExtractOldValueForEditor(true);
/* 82 */       this.directFieldAccessor.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/*    */     } 
/* 84 */     return this.directFieldAccessor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ConfigurablePropertyAccessor createDirectFieldAccessor() {
/* 92 */     if (this.target == null) {
/* 93 */       throw new IllegalStateException("Cannot access fields on null target instance '" + getObjectName() + "'");
/*    */     }
/* 95 */     return PropertyAccessorFactory.forDirectFieldAccess(this.target);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\DirectFieldBindingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */