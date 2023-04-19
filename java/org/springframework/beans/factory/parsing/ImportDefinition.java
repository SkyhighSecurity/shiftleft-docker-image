/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.BeanMetadataElement;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ public class ImportDefinition
/*    */   implements BeanMetadataElement
/*    */ {
/*    */   private final String importedResource;
/*    */   private final Resource[] actualResources;
/*    */   private final Object source;
/*    */   
/*    */   public ImportDefinition(String importedResource) {
/* 44 */     this(importedResource, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImportDefinition(String importedResource, Object source) {
/* 53 */     this(importedResource, null, source);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImportDefinition(String importedResource, Resource[] actualResources, Object source) {
/* 62 */     Assert.notNull(importedResource, "Imported resource must not be null");
/* 63 */     this.importedResource = importedResource;
/* 64 */     this.actualResources = actualResources;
/* 65 */     this.source = source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getImportedResource() {
/* 73 */     return this.importedResource;
/*    */   }
/*    */   
/*    */   public final Resource[] getActualResources() {
/* 77 */     return this.actualResources;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Object getSource() {
/* 82 */     return this.source;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\ImportDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */