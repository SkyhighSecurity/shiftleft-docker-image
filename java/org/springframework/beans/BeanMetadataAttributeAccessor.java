/*    */ package org.springframework.beans;
/*    */ 
/*    */ import org.springframework.core.AttributeAccessorSupport;
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
/*    */ public class BeanMetadataAttributeAccessor
/*    */   extends AttributeAccessorSupport
/*    */   implements BeanMetadataElement
/*    */ {
/*    */   private Object source;
/*    */   
/*    */   public void setSource(Object source) {
/* 40 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 45 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addMetadataAttribute(BeanMetadataAttribute attribute) {
/* 54 */     super.setAttribute(attribute.getName(), attribute);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BeanMetadataAttribute getMetadataAttribute(String name) {
/* 64 */     return (BeanMetadataAttribute)super.getAttribute(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setAttribute(String name, Object value) {
/* 69 */     super.setAttribute(name, new BeanMetadataAttribute(name, value));
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getAttribute(String name) {
/* 74 */     BeanMetadataAttribute attribute = (BeanMetadataAttribute)super.getAttribute(name);
/* 75 */     return (attribute != null) ? attribute.getValue() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object removeAttribute(String name) {
/* 80 */     BeanMetadataAttribute attribute = (BeanMetadataAttribute)super.removeAttribute(name);
/* 81 */     return (attribute != null) ? attribute.getValue() : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\BeanMetadataAttributeAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */