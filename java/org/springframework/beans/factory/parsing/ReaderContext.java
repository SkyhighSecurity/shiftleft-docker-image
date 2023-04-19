/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public class ReaderContext
/*     */ {
/*     */   private final Resource resource;
/*     */   private final ProblemReporter problemReporter;
/*     */   private final ReaderEventListener eventListener;
/*     */   private final SourceExtractor sourceExtractor;
/*     */   
/*     */   public ReaderContext(Resource resource, ProblemReporter problemReporter, ReaderEventListener eventListener, SourceExtractor sourceExtractor) {
/*  50 */     this.resource = resource;
/*  51 */     this.problemReporter = problemReporter;
/*  52 */     this.eventListener = eventListener;
/*  53 */     this.sourceExtractor = sourceExtractor;
/*     */   }
/*     */   
/*     */   public final Resource getResource() {
/*  57 */     return this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(String message, Object source) {
/*  67 */     fatal(message, source, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(String message, Object source, Throwable ex) {
/*  74 */     fatal(message, source, null, ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(String message, Object source, ParseState parseState) {
/*  81 */     fatal(message, source, parseState, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(String message, Object source, ParseState parseState, Throwable cause) {
/*  88 */     Location location = new Location(getResource(), source);
/*  89 */     this.problemReporter.fatal(new Problem(message, location, parseState, cause));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, Object source) {
/*  96 */     error(message, source, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, Object source, Throwable ex) {
/* 103 */     error(message, source, null, ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, Object source, ParseState parseState) {
/* 110 */     error(message, source, parseState, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, Object source, ParseState parseState, Throwable cause) {
/* 117 */     Location location = new Location(getResource(), source);
/* 118 */     this.problemReporter.error(new Problem(message, location, parseState, cause));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String message, Object source) {
/* 125 */     warning(message, source, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String message, Object source, Throwable ex) {
/* 132 */     warning(message, source, null, ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String message, Object source, ParseState parseState) {
/* 139 */     warning(message, source, parseState, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String message, Object source, ParseState parseState, Throwable cause) {
/* 146 */     Location location = new Location(getResource(), source);
/* 147 */     this.problemReporter.warning(new Problem(message, location, parseState, cause));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireDefaultsRegistered(DefaultsDefinition defaultsDefinition) {
/* 157 */     this.eventListener.defaultsRegistered(defaultsDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireComponentRegistered(ComponentDefinition componentDefinition) {
/* 164 */     this.eventListener.componentRegistered(componentDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireAliasRegistered(String beanName, String alias, Object source) {
/* 171 */     this.eventListener.aliasRegistered(new AliasDefinition(beanName, alias, source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireImportProcessed(String importedResource, Object source) {
/* 178 */     this.eventListener.importProcessed(new ImportDefinition(importedResource, source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireImportProcessed(String importedResource, Resource[] actualResources, Object source) {
/* 185 */     this.eventListener.importProcessed(new ImportDefinition(importedResource, actualResources, source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourceExtractor getSourceExtractor() {
/* 195 */     return this.sourceExtractor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object extractSource(Object sourceCandidate) {
/* 206 */     return this.sourceExtractor.extractSource(sourceCandidate, this.resource);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\ReaderContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */