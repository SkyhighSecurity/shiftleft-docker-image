/*    */ package org.springframework.beans.factory.xml;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.util.Assert;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DelegatingEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   public static final String DTD_SUFFIX = ".dtd";
/*    */   public static final String XSD_SUFFIX = ".xsd";
/*    */   private final EntityResolver dtdResolver;
/*    */   private final EntityResolver schemaResolver;
/*    */   
/*    */   public DelegatingEntityResolver(ClassLoader classLoader) {
/* 61 */     this.dtdResolver = new BeansDtdResolver();
/* 62 */     this.schemaResolver = new PluggableSchemaResolver(classLoader);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DelegatingEntityResolver(EntityResolver dtdResolver, EntityResolver schemaResolver) {
/* 72 */     Assert.notNull(dtdResolver, "'dtdResolver' is required");
/* 73 */     Assert.notNull(schemaResolver, "'schemaResolver' is required");
/* 74 */     this.dtdResolver = dtdResolver;
/* 75 */     this.schemaResolver = schemaResolver;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 81 */     if (systemId != null) {
/* 82 */       if (systemId.endsWith(".dtd")) {
/* 83 */         return this.dtdResolver.resolveEntity(publicId, systemId);
/*    */       }
/* 85 */       if (systemId.endsWith(".xsd")) {
/* 86 */         return this.schemaResolver.resolveEntity(publicId, systemId);
/*    */       }
/*    */     } 
/* 89 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return "EntityResolver delegating .xsd to " + this.schemaResolver + " and " + ".dtd" + " to " + this.dtdResolver;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\DelegatingEntityResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */