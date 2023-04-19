/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.bind.annotation.SessionAttributes;
/*     */ import org.springframework.web.bind.support.SessionAttributeStore;
/*     */ import org.springframework.web.context.request.WebRequest;
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
/*     */ public class SessionAttributesHandler
/*     */ {
/*  51 */   private final Set<String> attributeNames = new HashSet<String>();
/*     */   
/*  53 */   private final Set<Class<?>> attributeTypes = new HashSet<Class<?>>();
/*     */ 
/*     */   
/*  56 */   private final Set<String> knownAttributeNames = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(4));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SessionAttributeStore sessionAttributeStore;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionAttributesHandler(Class<?> handlerType, SessionAttributeStore sessionAttributeStore) {
/*  69 */     Assert.notNull(sessionAttributeStore, "SessionAttributeStore may not be null");
/*  70 */     this.sessionAttributeStore = sessionAttributeStore;
/*     */     
/*  72 */     SessionAttributes ann = (SessionAttributes)AnnotatedElementUtils.findMergedAnnotation(handlerType, SessionAttributes.class);
/*  73 */     if (ann != null) {
/*  74 */       this.attributeNames.addAll(Arrays.asList(ann.names()));
/*  75 */       this.attributeTypes.addAll(Arrays.asList(ann.types()));
/*     */     } 
/*  77 */     this.knownAttributeNames.addAll(this.attributeNames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSessionAttributes() {
/*  86 */     return (!this.attributeNames.isEmpty() || !this.attributeTypes.isEmpty());
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
/*     */   public boolean isHandlerSessionAttribute(String attributeName, Class<?> attributeType) {
/*  99 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 100 */     if (this.attributeNames.contains(attributeName) || this.attributeTypes.contains(attributeType)) {
/* 101 */       this.knownAttributeNames.add(attributeName);
/* 102 */       return true;
/*     */     } 
/*     */     
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void storeAttributes(WebRequest request, Map<String, ?> attributes) {
/* 116 */     for (String name : attributes.keySet()) {
/* 117 */       Object value = attributes.get(name);
/* 118 */       Class<?> attrType = (value != null) ? value.getClass() : null;
/* 119 */       if (isHandlerSessionAttribute(name, attrType)) {
/* 120 */         this.sessionAttributeStore.storeAttribute(request, name, value);
/*     */       }
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
/*     */   public Map<String, Object> retrieveAttributes(WebRequest request) {
/* 133 */     Map<String, Object> attributes = new HashMap<String, Object>();
/* 134 */     for (String name : this.knownAttributeNames) {
/* 135 */       Object value = this.sessionAttributeStore.retrieveAttribute(request, name);
/* 136 */       if (value != null) {
/* 137 */         attributes.put(name, value);
/*     */       }
/*     */     } 
/* 140 */     return attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanupAttributes(WebRequest request) {
/* 150 */     for (String attributeName : this.knownAttributeNames) {
/* 151 */       this.sessionAttributeStore.cleanupAttribute(request, attributeName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object retrieveAttribute(WebRequest request, String attributeName) {
/* 162 */     return this.sessionAttributeStore.retrieveAttribute(request, attributeName);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\SessionAttributesHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */