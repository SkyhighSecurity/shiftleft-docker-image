/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.BeanMetadataElement;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class AliasDefinition
/*    */   implements BeanMetadataElement
/*    */ {
/*    */   private final String beanName;
/*    */   private final String alias;
/*    */   private final Object source;
/*    */   
/*    */   public AliasDefinition(String beanName, String alias) {
/* 44 */     this(beanName, alias, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AliasDefinition(String beanName, String alias, Object source) {
/* 54 */     Assert.notNull(beanName, "Bean name must not be null");
/* 55 */     Assert.notNull(alias, "Alias must not be null");
/* 56 */     this.beanName = beanName;
/* 57 */     this.alias = alias;
/* 58 */     this.source = source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getBeanName() {
/* 66 */     return this.beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getAlias() {
/* 73 */     return this.alias;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Object getSource() {
/* 78 */     return this.source;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\AliasDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */