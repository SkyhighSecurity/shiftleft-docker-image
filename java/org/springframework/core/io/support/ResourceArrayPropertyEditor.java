/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ResourceArrayPropertyEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*  58 */   private static final Log logger = LogFactory.getLog(ResourceArrayPropertyEditor.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final ResourcePatternResolver resourcePatternResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   private PropertyResolver propertyResolver;
/*     */ 
/*     */   
/*     */   private final boolean ignoreUnresolvablePlaceholders;
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceArrayPropertyEditor() {
/*  74 */     this(new PathMatchingResourcePatternResolver(), null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, PropertyResolver propertyResolver) {
/*  84 */     this(resourcePatternResolver, propertyResolver, true);
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
/*     */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders) {
/*  98 */     Assert.notNull(resourcePatternResolver, "ResourcePatternResolver must not be null");
/*  99 */     this.resourcePatternResolver = resourcePatternResolver;
/* 100 */     this.propertyResolver = propertyResolver;
/* 101 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) {
/* 110 */     String pattern = resolvePath(text).trim();
/*     */     try {
/* 112 */       setValue(this.resourcePatternResolver.getResources(pattern));
/*     */     }
/* 114 */     catch (IOException ex) {
/* 115 */       throw new IllegalArgumentException("Could not resolve resource location pattern [" + pattern + "]: " + ex
/* 116 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) throws IllegalArgumentException {
/* 126 */     if (value instanceof Collection || (value instanceof Object[] && !(value instanceof Resource[]))) {
/* 127 */       Collection<?> input = (value instanceof Collection) ? (Collection)value : Arrays.asList((Object[])value);
/* 128 */       List<Resource> merged = new ArrayList<Resource>();
/* 129 */       for (Object element : input) {
/* 130 */         if (element instanceof String) {
/*     */ 
/*     */           
/* 133 */           String pattern = resolvePath((String)element).trim();
/*     */           try {
/* 135 */             Resource[] resources = this.resourcePatternResolver.getResources(pattern);
/* 136 */             for (Resource resource : resources) {
/* 137 */               if (!merged.contains(resource)) {
/* 138 */                 merged.add(resource);
/*     */               }
/*     */             }
/*     */           
/* 142 */           } catch (IOException ex) {
/*     */             
/* 144 */             if (logger.isDebugEnabled())
/* 145 */               logger.debug("Could not retrieve resources for pattern '" + pattern + "'", ex); 
/*     */           } 
/*     */           continue;
/*     */         } 
/* 149 */         if (element instanceof Resource) {
/*     */           
/* 151 */           Resource resource = (Resource)element;
/* 152 */           if (!merged.contains(resource)) {
/* 153 */             merged.add(resource);
/*     */           }
/*     */           continue;
/*     */         } 
/* 157 */         throw new IllegalArgumentException("Cannot convert element [" + element + "] to [" + Resource.class
/* 158 */             .getName() + "]: only location String and Resource object supported");
/*     */       } 
/*     */       
/* 161 */       super.setValue(merged.toArray(new Resource[merged.size()]));
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 167 */       super.setValue(value);
/*     */     } 
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
/*     */   protected String resolvePath(String path) {
/* 180 */     if (this.propertyResolver == null) {
/* 181 */       this.propertyResolver = (PropertyResolver)new StandardEnvironment();
/*     */     }
/* 183 */     return this.ignoreUnresolvablePlaceholders ? this.propertyResolver.resolvePlaceholders(path) : this.propertyResolver
/* 184 */       .resolveRequiredPlaceholders(path);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\ResourceArrayPropertyEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */