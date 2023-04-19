/*     */ package org.springframework.core.type.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.type.ClassMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
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
/*     */ public abstract class AbstractTypeHierarchyTraversingFilter
/*     */   implements TypeFilter
/*     */ {
/*  42 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final boolean considerInherited;
/*     */   
/*     */   private final boolean considerInterfaces;
/*     */ 
/*     */   
/*     */   protected AbstractTypeHierarchyTraversingFilter(boolean considerInherited, boolean considerInterfaces) {
/*  50 */     this.considerInherited = considerInherited;
/*  51 */     this.considerInterfaces = considerInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
/*  61 */     if (matchSelf(metadataReader)) {
/*  62 */       return true;
/*     */     }
/*  64 */     ClassMetadata metadata = metadataReader.getClassMetadata();
/*  65 */     if (matchClassName(metadata.getClassName())) {
/*  66 */       return true;
/*     */     }
/*     */     
/*  69 */     if (this.considerInherited && 
/*  70 */       metadata.hasSuperClass()) {
/*     */       
/*  72 */       Boolean superClassMatch = matchSuperClass(metadata.getSuperClassName());
/*  73 */       if (superClassMatch != null) {
/*  74 */         if (superClassMatch.booleanValue()) {
/*  75 */           return true;
/*     */         }
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/*  81 */           if (match(metadata.getSuperClassName(), metadataReaderFactory)) {
/*  82 */             return true;
/*     */           }
/*     */         }
/*  85 */         catch (IOException ex) {
/*  86 */           this.logger.debug("Could not read super class [" + metadata.getSuperClassName() + "] of type-filtered class [" + metadata
/*  87 */               .getClassName() + "]");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  93 */     if (this.considerInterfaces) {
/*  94 */       for (String ifc : metadata.getInterfaceNames()) {
/*     */         
/*  96 */         Boolean interfaceMatch = matchInterface(ifc);
/*  97 */         if (interfaceMatch != null) {
/*  98 */           if (interfaceMatch.booleanValue()) {
/*  99 */             return true;
/*     */           }
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/* 105 */             if (match(ifc, metadataReaderFactory)) {
/* 106 */               return true;
/*     */             }
/*     */           }
/* 109 */           catch (IOException ex) {
/* 110 */             this.logger.debug("Could not read interface [" + ifc + "] for type-filtered class [" + metadata
/* 111 */                 .getClassName() + "]");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 117 */     return false;
/*     */   }
/*     */   
/*     */   private boolean match(String className, MetadataReaderFactory metadataReaderFactory) throws IOException {
/* 121 */     return match(metadataReaderFactory.getMetadataReader(className), metadataReaderFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean matchSelf(MetadataReader metadataReader) {
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean matchClassName(String className) {
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Boolean matchSuperClass(String superClassName) {
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Boolean matchInterface(String interfaceName) {
/* 151 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\filter\AbstractTypeHierarchyTraversingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */