/*    */ package org.springframework.core.type.classreading;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.asm.AnnotationVisitor;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ class RecursiveAnnotationArrayVisitor
/*    */   extends AbstractRecursiveAnnotationVisitor
/*    */ {
/*    */   private final String attributeName;
/* 37 */   private final List<AnnotationAttributes> allNestedAttributes = new ArrayList<AnnotationAttributes>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecursiveAnnotationArrayVisitor(String attributeName, AnnotationAttributes attributes, ClassLoader classLoader) {
/* 43 */     super(classLoader, attributes);
/* 44 */     this.attributeName = attributeName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(String attributeName, Object attributeValue) {
/* 50 */     Object newValue = attributeValue;
/* 51 */     Object existingValue = this.attributes.get(this.attributeName);
/* 52 */     if (existingValue != null) {
/* 53 */       newValue = ObjectUtils.addObjectToArray((Object[])existingValue, newValue);
/*    */     } else {
/*    */       
/* 56 */       Class<?> arrayClass = newValue.getClass();
/* 57 */       if (Enum.class.isAssignableFrom(arrayClass)) {
/* 58 */         while (arrayClass.getSuperclass() != null && !arrayClass.isEnum()) {
/* 59 */           arrayClass = arrayClass.getSuperclass();
/*    */         }
/*    */       }
/* 62 */       Object[] newArray = (Object[])Array.newInstance(arrayClass, 1);
/* 63 */       newArray[0] = newValue;
/* 64 */       newValue = newArray;
/*    */     } 
/* 66 */     this.attributes.put(this.attributeName, newValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor) {
/* 71 */     String annotationType = Type.getType(asmTypeDescriptor).getClassName();
/* 72 */     AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, this.classLoader);
/* 73 */     this.allNestedAttributes.add(nestedAttributes);
/* 74 */     return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, this.classLoader);
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitEnd() {
/* 79 */     if (!this.allNestedAttributes.isEmpty())
/* 80 */       this.attributes.put(this.attributeName, this.allNestedAttributes
/* 81 */           .toArray(new AnnotationAttributes[this.allNestedAttributes.size()])); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\RecursiveAnnotationArrayVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */