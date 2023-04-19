/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.ReaderContext;
/*     */ import org.springframework.beans.factory.parsing.ReaderEventListener;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class XmlReaderContext
/*     */   extends ReaderContext
/*     */ {
/*     */   private final XmlBeanDefinitionReader reader;
/*     */   private final NamespaceHandlerResolver namespaceHandlerResolver;
/*     */   
/*     */   public XmlReaderContext(Resource resource, ProblemReporter problemReporter, ReaderEventListener eventListener, SourceExtractor sourceExtractor, XmlBeanDefinitionReader reader, NamespaceHandlerResolver namespaceHandlerResolver) {
/*  56 */     super(resource, problemReporter, eventListener, sourceExtractor);
/*  57 */     this.reader = reader;
/*  58 */     this.namespaceHandlerResolver = namespaceHandlerResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public final XmlBeanDefinitionReader getReader() {
/*  63 */     return this.reader;
/*     */   }
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/*  67 */     return this.reader.getRegistry();
/*     */   }
/*     */   
/*     */   public final ResourceLoader getResourceLoader() {
/*  71 */     return this.reader.getResourceLoader();
/*     */   }
/*     */   
/*     */   public final ClassLoader getBeanClassLoader() {
/*  75 */     return this.reader.getBeanClassLoader();
/*     */   }
/*     */   
/*     */   public final Environment getEnvironment() {
/*  79 */     return this.reader.getEnvironment();
/*     */   }
/*     */   
/*     */   public final NamespaceHandlerResolver getNamespaceHandlerResolver() {
/*  83 */     return this.namespaceHandlerResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public String generateBeanName(BeanDefinition beanDefinition) {
/*  88 */     return this.reader.getBeanNameGenerator().generateBeanName(beanDefinition, getRegistry());
/*     */   }
/*     */   
/*     */   public String registerWithGeneratedName(BeanDefinition beanDefinition) {
/*  92 */     String generatedName = generateBeanName(beanDefinition);
/*  93 */     getRegistry().registerBeanDefinition(generatedName, beanDefinition);
/*  94 */     return generatedName;
/*     */   }
/*     */   
/*     */   public Document readDocumentFromString(String documentContent) {
/*  98 */     InputSource is = new InputSource(new StringReader(documentContent));
/*     */     try {
/* 100 */       return this.reader.doLoadDocument(is, getResource());
/*     */     }
/* 102 */     catch (Exception ex) {
/* 103 */       throw new BeanDefinitionStoreException("Failed to read XML document", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\XmlReaderContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */