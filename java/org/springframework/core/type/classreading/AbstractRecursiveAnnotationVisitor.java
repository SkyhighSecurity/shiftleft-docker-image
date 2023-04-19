/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessControlException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
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
/*     */ abstract class AbstractRecursiveAnnotationVisitor
/*     */   extends AnnotationVisitor
/*     */ {
/*  40 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   protected final AnnotationAttributes attributes;
/*     */   
/*     */   protected final ClassLoader classLoader;
/*     */ 
/*     */   
/*     */   public AbstractRecursiveAnnotationVisitor(ClassLoader classLoader, AnnotationAttributes attributes) {
/*  48 */     super(393216);
/*  49 */     this.classLoader = classLoader;
/*  50 */     this.attributes = attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(String attributeName, Object attributeValue) {
/*  56 */     this.attributes.put(attributeName, attributeValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor) {
/*  61 */     String annotationType = Type.getType(asmTypeDescriptor).getClassName();
/*  62 */     AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, this.classLoader);
/*  63 */     this.attributes.put(attributeName, nestedAttributes);
/*  64 */     return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, this.classLoader);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitArray(String attributeName) {
/*  69 */     return new RecursiveAnnotationArrayVisitor(attributeName, this.attributes, this.classLoader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnum(String attributeName, String asmTypeDescriptor, String attributeValue) {
/*  74 */     Object newValue = getEnumValue(asmTypeDescriptor, attributeValue);
/*  75 */     visit(attributeName, newValue);
/*     */   }
/*     */   
/*     */   protected Object getEnumValue(String asmTypeDescriptor, String attributeValue) {
/*  79 */     Object valueToUse = attributeValue;
/*     */     try {
/*  81 */       Class<?> enumType = this.classLoader.loadClass(Type.getType(asmTypeDescriptor).getClassName());
/*  82 */       Field enumConstant = ReflectionUtils.findField(enumType, attributeValue);
/*  83 */       if (enumConstant != null) {
/*  84 */         ReflectionUtils.makeAccessible(enumConstant);
/*  85 */         valueToUse = enumConstant.get(null);
/*     */       }
/*     */     
/*  88 */     } catch (ClassNotFoundException ex) {
/*  89 */       this.logger.debug("Failed to classload enum type while reading annotation metadata", ex);
/*     */     }
/*  91 */     catch (NoClassDefFoundError ex) {
/*  92 */       this.logger.debug("Failed to classload enum type while reading annotation metadata", ex);
/*     */     }
/*  94 */     catch (IllegalAccessException ex) {
/*  95 */       this.logger.debug("Could not access enum value while reading annotation metadata", ex);
/*     */     }
/*  97 */     catch (AccessControlException ex) {
/*  98 */       this.logger.debug("Could not access enum value while reading annotation metadata", ex);
/*     */     } 
/* 100 */     return valueToUse;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\AbstractRecursiveAnnotationVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */