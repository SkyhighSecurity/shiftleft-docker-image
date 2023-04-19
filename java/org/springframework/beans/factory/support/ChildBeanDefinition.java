/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChildBeanDefinition
/*     */   extends AbstractBeanDefinition
/*     */ {
/*     */   private String parentName;
/*     */   
/*     */   public ChildBeanDefinition(String parentName) {
/*  62 */     this.parentName = parentName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChildBeanDefinition(String parentName, MutablePropertyValues pvs) {
/*  71 */     super(null, pvs);
/*  72 */     this.parentName = parentName;
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
/*     */   public ChildBeanDefinition(String parentName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/*  84 */     super(cargs, pvs);
/*  85 */     this.parentName = parentName;
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
/*     */ 
/*     */   
/*     */   public ChildBeanDefinition(String parentName, Class<?> beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/*  99 */     super(cargs, pvs);
/* 100 */     this.parentName = parentName;
/* 101 */     setBeanClass(beanClass);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public ChildBeanDefinition(String parentName, String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/* 116 */     super(cargs, pvs);
/* 117 */     this.parentName = parentName;
/* 118 */     setBeanClassName(beanClassName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChildBeanDefinition(ChildBeanDefinition original) {
/* 127 */     super(original);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentName(String parentName) {
/* 133 */     this.parentName = parentName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParentName() {
/* 138 */     return this.parentName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate() throws BeanDefinitionValidationException {
/* 143 */     super.validate();
/* 144 */     if (this.parentName == null) {
/* 145 */       throw new BeanDefinitionValidationException("'parentName' must be set in ChildBeanDefinition");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBeanDefinition cloneBeanDefinition() {
/* 152 */     return new ChildBeanDefinition(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 157 */     if (this == other) {
/* 158 */       return true;
/*     */     }
/* 160 */     if (!(other instanceof ChildBeanDefinition)) {
/* 161 */       return false;
/*     */     }
/* 163 */     ChildBeanDefinition that = (ChildBeanDefinition)other;
/* 164 */     return (ObjectUtils.nullSafeEquals(this.parentName, that.parentName) && super.equals(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 169 */     return ObjectUtils.nullSafeHashCode(this.parentName) * 29 + super.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 174 */     return "Child bean with parent '" + this.parentName + "': " + super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\ChildBeanDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */