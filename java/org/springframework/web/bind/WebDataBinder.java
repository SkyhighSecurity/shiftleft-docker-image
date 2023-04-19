/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.validation.DataBinder;
/*     */ import org.springframework.web.multipart.MultipartFile;
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
/*     */ public class WebDataBinder
/*     */   extends DataBinder
/*     */ {
/*     */   public static final String DEFAULT_FIELD_MARKER_PREFIX = "_";
/*     */   public static final String DEFAULT_FIELD_DEFAULT_PREFIX = "!";
/*  77 */   private String fieldMarkerPrefix = "_";
/*     */   
/*  79 */   private String fieldDefaultPrefix = "!";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean bindEmptyMultipartFiles = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebDataBinder(Object target) {
/*  91 */     super(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebDataBinder(Object target, String objectName) {
/* 101 */     super(target, objectName);
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
/*     */   public void setFieldMarkerPrefix(String fieldMarkerPrefix) {
/* 127 */     this.fieldMarkerPrefix = fieldMarkerPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldMarkerPrefix() {
/* 134 */     return this.fieldMarkerPrefix;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFieldDefaultPrefix(String fieldDefaultPrefix) {
/* 152 */     this.fieldDefaultPrefix = fieldDefaultPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldDefaultPrefix() {
/* 159 */     return this.fieldDefaultPrefix;
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
/*     */   public void setBindEmptyMultipartFiles(boolean bindEmptyMultipartFiles) {
/* 171 */     this.bindEmptyMultipartFiles = bindEmptyMultipartFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBindEmptyMultipartFiles() {
/* 178 */     return this.bindEmptyMultipartFiles;
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
/*     */   protected void doBind(MutablePropertyValues mpvs) {
/* 190 */     checkFieldDefaults(mpvs);
/* 191 */     checkFieldMarkers(mpvs);
/* 192 */     super.doBind(mpvs);
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
/*     */   protected void checkFieldDefaults(MutablePropertyValues mpvs) {
/* 204 */     String fieldDefaultPrefix = getFieldDefaultPrefix();
/* 205 */     if (fieldDefaultPrefix != null) {
/* 206 */       PropertyValue[] pvArray = mpvs.getPropertyValues();
/* 207 */       for (PropertyValue pv : pvArray) {
/* 208 */         if (pv.getName().startsWith(fieldDefaultPrefix)) {
/* 209 */           String field = pv.getName().substring(fieldDefaultPrefix.length());
/* 210 */           if (getPropertyAccessor().isWritableProperty(field) && !mpvs.contains(field)) {
/* 211 */             mpvs.add(field, pv.getValue());
/*     */           }
/* 213 */           mpvs.removePropertyValue(pv);
/*     */         } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFieldMarkers(MutablePropertyValues mpvs) {
/* 231 */     String fieldMarkerPrefix = getFieldMarkerPrefix();
/* 232 */     if (fieldMarkerPrefix != null) {
/* 233 */       PropertyValue[] pvArray = mpvs.getPropertyValues();
/* 234 */       for (PropertyValue pv : pvArray) {
/* 235 */         if (pv.getName().startsWith(fieldMarkerPrefix)) {
/* 236 */           String field = pv.getName().substring(fieldMarkerPrefix.length());
/* 237 */           if (getPropertyAccessor().isWritableProperty(field) && !mpvs.contains(field)) {
/* 238 */             Class<?> fieldType = getPropertyAccessor().getPropertyType(field);
/* 239 */             mpvs.add(field, getEmptyValue(field, fieldType));
/*     */           } 
/* 241 */           mpvs.removePropertyValue(pv);
/*     */         } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getEmptyValue(String field, Class<?> fieldType) {
/* 262 */     if (fieldType != null) {
/*     */       try {
/* 264 */         if (boolean.class == fieldType || Boolean.class == fieldType)
/*     */         {
/* 266 */           return Boolean.FALSE;
/*     */         }
/* 268 */         if (fieldType.isArray())
/*     */         {
/* 270 */           return Array.newInstance(fieldType.getComponentType(), 0);
/*     */         }
/* 272 */         if (Collection.class.isAssignableFrom(fieldType)) {
/* 273 */           return CollectionFactory.createCollection(fieldType, 0);
/*     */         }
/* 275 */         if (Map.class.isAssignableFrom(fieldType)) {
/* 276 */           return CollectionFactory.createMap(fieldType, 0);
/*     */         }
/*     */       }
/* 279 */       catch (IllegalArgumentException ex) {
/* 280 */         if (logger.isDebugEnabled()) {
/* 281 */           logger.debug("Failed to create default value - falling back to null: " + ex.getMessage());
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 286 */     return null;
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
/*     */   protected void bindMultipart(Map<String, List<MultipartFile>> multipartFiles, MutablePropertyValues mpvs) {
/* 300 */     for (Map.Entry<String, List<MultipartFile>> entry : multipartFiles.entrySet()) {
/* 301 */       String key = entry.getKey();
/* 302 */       List<MultipartFile> values = entry.getValue();
/* 303 */       if (values.size() == 1) {
/* 304 */         MultipartFile value = values.get(0);
/* 305 */         if (isBindEmptyMultipartFiles() || !value.isEmpty()) {
/* 306 */           mpvs.add(key, value);
/*     */         }
/*     */         continue;
/*     */       } 
/* 310 */       mpvs.add(key, values);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\WebDataBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */