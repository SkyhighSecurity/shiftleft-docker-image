/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourcePatternUtils;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ public class DefaultBeanDefinitionDocumentReader
/*     */   implements BeanDefinitionDocumentReader
/*     */ {
/*     */   public static final String BEAN_ELEMENT = "bean";
/*     */   public static final String NESTED_BEANS_ELEMENT = "beans";
/*     */   public static final String ALIAS_ELEMENT = "alias";
/*     */   public static final String NAME_ATTRIBUTE = "name";
/*     */   public static final String ALIAS_ATTRIBUTE = "alias";
/*     */   public static final String IMPORT_ELEMENT = "import";
/*     */   public static final String RESOURCE_ATTRIBUTE = "resource";
/*     */   public static final String PROFILE_ATTRIBUTE = "profile";
/*  76 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XmlReaderContext readerContext;
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanDefinitionParserDelegate delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
/*  91 */     this.readerContext = readerContext;
/*  92 */     this.logger.debug("Loading bean definitions");
/*  93 */     Element root = doc.getDocumentElement();
/*  94 */     doRegisterBeanDefinitions(root);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final XmlReaderContext getReaderContext() {
/* 101 */     return this.readerContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object extractSource(Element ele) {
/* 109 */     return getReaderContext().extractSource(ele);
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
/*     */   protected void doRegisterBeanDefinitions(Element root) {
/* 123 */     BeanDefinitionParserDelegate parent = this.delegate;
/* 124 */     this.delegate = createDelegate(getReaderContext(), root, parent);
/*     */     
/* 126 */     if (this.delegate.isDefaultNamespace(root)) {
/* 127 */       String profileSpec = root.getAttribute("profile");
/* 128 */       if (StringUtils.hasText(profileSpec)) {
/* 129 */         String[] specifiedProfiles = StringUtils.tokenizeToStringArray(profileSpec, ",; ");
/*     */         
/* 131 */         if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
/* 132 */           if (this.logger.isInfoEnabled()) {
/* 133 */             this.logger.info("Skipped XML bean definition file due to specified profiles [" + profileSpec + "] not matching: " + 
/* 134 */                 getReaderContext().getResource());
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 141 */     preProcessXml(root);
/* 142 */     parseBeanDefinitions(root, this.delegate);
/* 143 */     postProcessXml(root);
/*     */     
/* 145 */     this.delegate = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanDefinitionParserDelegate createDelegate(XmlReaderContext readerContext, Element root, BeanDefinitionParserDelegate parentDelegate) {
/* 151 */     BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
/* 152 */     delegate.initDefaults(root, parentDelegate);
/* 153 */     return delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
/* 162 */     if (delegate.isDefaultNamespace(root)) {
/* 163 */       NodeList nl = root.getChildNodes();
/* 164 */       for (int i = 0; i < nl.getLength(); i++) {
/* 165 */         Node node = nl.item(i);
/* 166 */         if (node instanceof Element) {
/* 167 */           Element ele = (Element)node;
/* 168 */           if (delegate.isDefaultNamespace(ele)) {
/* 169 */             parseDefaultElement(ele, delegate);
/*     */           } else {
/*     */             
/* 172 */             delegate.parseCustomElement(ele);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 178 */       delegate.parseCustomElement(root);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
/* 183 */     if (delegate.nodeNameEquals(ele, "import")) {
/* 184 */       importBeanDefinitionResource(ele);
/*     */     }
/* 186 */     else if (delegate.nodeNameEquals(ele, "alias")) {
/* 187 */       processAliasRegistration(ele);
/*     */     }
/* 189 */     else if (delegate.nodeNameEquals(ele, "bean")) {
/* 190 */       processBeanDefinition(ele, delegate);
/*     */     }
/* 192 */     else if (delegate.nodeNameEquals(ele, "beans")) {
/*     */       
/* 194 */       doRegisterBeanDefinitions(ele);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void importBeanDefinitionResource(Element ele) {
/* 203 */     String location = ele.getAttribute("resource");
/* 204 */     if (!StringUtils.hasText(location)) {
/* 205 */       getReaderContext().error("Resource location must not be empty", ele);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 210 */     location = getReaderContext().getEnvironment().resolveRequiredPlaceholders(location);
/*     */     
/* 212 */     Set<Resource> actualResources = new LinkedHashSet<Resource>(4);
/*     */ 
/*     */     
/* 215 */     boolean absoluteLocation = false;
/*     */     try {
/* 217 */       absoluteLocation = (ResourcePatternUtils.isUrl(location) || ResourceUtils.toURI(location).isAbsolute());
/*     */     }
/* 219 */     catch (URISyntaxException uRISyntaxException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     if (absoluteLocation) {
/*     */       try {
/* 227 */         int importCount = getReaderContext().getReader().loadBeanDefinitions(location, actualResources);
/* 228 */         if (this.logger.isDebugEnabled()) {
/* 229 */           this.logger.debug("Imported " + importCount + " bean definitions from URL location [" + location + "]");
/*     */         }
/*     */       }
/* 232 */       catch (BeanDefinitionStoreException ex) {
/* 233 */         getReaderContext().error("Failed to import bean definitions from URL location [" + location + "]", ele, (Throwable)ex);
/*     */       } 
/*     */     } else {
/*     */       try {
/*     */         int importCount;
/*     */ 
/*     */ 
/*     */         
/* 241 */         Resource relativeResource = getReaderContext().getResource().createRelative(location);
/* 242 */         if (relativeResource.exists()) {
/* 243 */           importCount = getReaderContext().getReader().loadBeanDefinitions(relativeResource);
/* 244 */           actualResources.add(relativeResource);
/*     */         } else {
/*     */           
/* 247 */           String baseLocation = getReaderContext().getResource().getURL().toString();
/* 248 */           importCount = getReaderContext().getReader().loadBeanDefinitions(
/* 249 */               StringUtils.applyRelativePath(baseLocation, location), actualResources);
/*     */         } 
/* 251 */         if (this.logger.isDebugEnabled()) {
/* 252 */           this.logger.debug("Imported " + importCount + " bean definitions from relative location [" + location + "]");
/*     */         }
/*     */       }
/* 255 */       catch (IOException ex) {
/* 256 */         getReaderContext().error("Failed to resolve current resource location", ele, ex);
/*     */       }
/* 258 */       catch (BeanDefinitionStoreException ex) {
/* 259 */         getReaderContext().error("Failed to import bean definitions from relative location [" + location + "]", ele, (Throwable)ex);
/*     */       } 
/*     */     } 
/*     */     
/* 263 */     Resource[] actResArray = actualResources.<Resource>toArray(new Resource[actualResources.size()]);
/* 264 */     getReaderContext().fireImportProcessed(location, actResArray, extractSource(ele));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processAliasRegistration(Element ele) {
/* 271 */     String name = ele.getAttribute("name");
/* 272 */     String alias = ele.getAttribute("alias");
/* 273 */     boolean valid = true;
/* 274 */     if (!StringUtils.hasText(name)) {
/* 275 */       getReaderContext().error("Name must not be empty", ele);
/* 276 */       valid = false;
/*     */     } 
/* 278 */     if (!StringUtils.hasText(alias)) {
/* 279 */       getReaderContext().error("Alias must not be empty", ele);
/* 280 */       valid = false;
/*     */     } 
/* 282 */     if (valid) {
/*     */       try {
/* 284 */         getReaderContext().getRegistry().registerAlias(name, alias);
/*     */       }
/* 286 */       catch (Exception ex) {
/* 287 */         getReaderContext().error("Failed to register alias '" + alias + "' for bean with name '" + name + "'", ele, ex);
/*     */       } 
/*     */       
/* 290 */       getReaderContext().fireAliasRegistered(name, alias, extractSource(ele));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
/* 299 */     BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
/* 300 */     if (bdHolder != null) {
/* 301 */       bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
/*     */       
/*     */       try {
/* 304 */         BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
/*     */       }
/* 306 */       catch (BeanDefinitionStoreException ex) {
/* 307 */         getReaderContext().error("Failed to register bean definition with name '" + bdHolder
/* 308 */             .getBeanName() + "'", ele, (Throwable)ex);
/*     */       } 
/*     */       
/* 311 */       getReaderContext().fireComponentRegistered((ComponentDefinition)new BeanComponentDefinition(bdHolder));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void preProcessXml(Element root) {}
/*     */   
/*     */   protected void postProcessXml(Element root) {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\xml\DefaultBeanDefinitionDocumentReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */