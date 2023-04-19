/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collection;
/*     */ import java.util.Currency;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TimeZone;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
/*     */ import org.springframework.beans.propertyeditors.CharArrayPropertyEditor;
/*     */ import org.springframework.beans.propertyeditors.CharacterEditor;
/*     */ import org.springframework.beans.propertyeditors.CharsetEditor;
/*     */ import org.springframework.beans.propertyeditors.ClassArrayEditor;
/*     */ import org.springframework.beans.propertyeditors.ClassEditor;
/*     */ import org.springframework.beans.propertyeditors.CurrencyEditor;
/*     */ import org.springframework.beans.propertyeditors.CustomBooleanEditor;
/*     */ import org.springframework.beans.propertyeditors.CustomCollectionEditor;
/*     */ import org.springframework.beans.propertyeditors.CustomMapEditor;
/*     */ import org.springframework.beans.propertyeditors.CustomNumberEditor;
/*     */ import org.springframework.beans.propertyeditors.FileEditor;
/*     */ import org.springframework.beans.propertyeditors.InputSourceEditor;
/*     */ import org.springframework.beans.propertyeditors.InputStreamEditor;
/*     */ import org.springframework.beans.propertyeditors.LocaleEditor;
/*     */ import org.springframework.beans.propertyeditors.PathEditor;
/*     */ import org.springframework.beans.propertyeditors.PatternEditor;
/*     */ import org.springframework.beans.propertyeditors.PropertiesEditor;
/*     */ import org.springframework.beans.propertyeditors.ReaderEditor;
/*     */ import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
/*     */ import org.springframework.beans.propertyeditors.TimeZoneEditor;
/*     */ import org.springframework.beans.propertyeditors.URIEditor;
/*     */ import org.springframework.beans.propertyeditors.URLEditor;
/*     */ import org.springframework.beans.propertyeditors.UUIDEditor;
/*     */ import org.springframework.beans.propertyeditors.ZoneIdEditor;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourceArrayPropertyEditor;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class PropertyEditorRegistrySupport
/*     */   implements PropertyEditorRegistry
/*     */ {
/*     */   private static Class<?> pathClass;
/*     */   private static Class<?> zoneIdClass;
/*     */   private ConversionService conversionService;
/*     */   
/*     */   static {
/*  96 */     ClassLoader cl = PropertyEditorRegistrySupport.class.getClassLoader();
/*     */     try {
/*  98 */       pathClass = ClassUtils.forName("java.nio.file.Path", cl);
/*     */     }
/* 100 */     catch (ClassNotFoundException ex) {
/*     */       
/* 102 */       pathClass = null;
/*     */     } 
/*     */     try {
/* 105 */       zoneIdClass = ClassUtils.forName("java.time.ZoneId", cl);
/*     */     }
/* 107 */     catch (ClassNotFoundException ex) {
/*     */       
/* 109 */       zoneIdClass = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean defaultEditorsActive = false;
/*     */ 
/*     */   
/*     */   private boolean configValueEditorsActive = false;
/*     */ 
/*     */   
/*     */   private Map<Class<?>, PropertyEditor> defaultEditors;
/*     */ 
/*     */   
/*     */   private Map<Class<?>, PropertyEditor> overriddenDefaultEditors;
/*     */ 
/*     */   
/*     */   private Map<Class<?>, PropertyEditor> customEditors;
/*     */ 
/*     */   
/*     */   private Map<String, CustomEditorHolder> customEditorsForPath;
/*     */   
/*     */   private Map<Class<?>, PropertyEditor> customEditorCache;
/*     */ 
/*     */   
/*     */   public void setConversionService(ConversionService conversionService) {
/* 136 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConversionService getConversionService() {
/* 143 */     return this.conversionService;
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
/*     */   protected void registerDefaultEditors() {
/* 156 */     this.defaultEditorsActive = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void useConfigValueEditors() {
/* 167 */     this.configValueEditorsActive = true;
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
/*     */   public void overrideDefaultEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
/* 180 */     if (this.overriddenDefaultEditors == null) {
/* 181 */       this.overriddenDefaultEditors = new HashMap<Class<?>, PropertyEditor>();
/*     */     }
/* 183 */     this.overriddenDefaultEditors.put(requiredType, propertyEditor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyEditor getDefaultEditor(Class<?> requiredType) {
/* 194 */     if (!this.defaultEditorsActive) {
/* 195 */       return null;
/*     */     }
/* 197 */     if (this.overriddenDefaultEditors != null) {
/* 198 */       PropertyEditor editor = this.overriddenDefaultEditors.get(requiredType);
/* 199 */       if (editor != null) {
/* 200 */         return editor;
/*     */       }
/*     */     } 
/* 203 */     if (this.defaultEditors == null) {
/* 204 */       createDefaultEditors();
/*     */     }
/* 206 */     return this.defaultEditors.get(requiredType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createDefaultEditors() {
/* 213 */     this.defaultEditors = new HashMap<Class<?>, PropertyEditor>(64);
/*     */ 
/*     */ 
/*     */     
/* 217 */     this.defaultEditors.put(Charset.class, new CharsetEditor());
/* 218 */     this.defaultEditors.put(Class.class, new ClassEditor());
/* 219 */     this.defaultEditors.put(Class[].class, new ClassArrayEditor());
/* 220 */     this.defaultEditors.put(Currency.class, new CurrencyEditor());
/* 221 */     this.defaultEditors.put(File.class, new FileEditor());
/* 222 */     this.defaultEditors.put(InputStream.class, new InputStreamEditor());
/* 223 */     this.defaultEditors.put(InputSource.class, new InputSourceEditor());
/* 224 */     this.defaultEditors.put(Locale.class, new LocaleEditor());
/* 225 */     if (pathClass != null) {
/* 226 */       this.defaultEditors.put(pathClass, new PathEditor());
/*     */     }
/* 228 */     this.defaultEditors.put(Pattern.class, new PatternEditor());
/* 229 */     this.defaultEditors.put(Properties.class, new PropertiesEditor());
/* 230 */     this.defaultEditors.put(Reader.class, new ReaderEditor());
/* 231 */     this.defaultEditors.put(Resource[].class, new ResourceArrayPropertyEditor());
/* 232 */     this.defaultEditors.put(TimeZone.class, new TimeZoneEditor());
/* 233 */     this.defaultEditors.put(URI.class, new URIEditor());
/* 234 */     this.defaultEditors.put(URL.class, new URLEditor());
/* 235 */     this.defaultEditors.put(UUID.class, new UUIDEditor());
/* 236 */     if (zoneIdClass != null) {
/* 237 */       this.defaultEditors.put(zoneIdClass, new ZoneIdEditor());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 242 */     this.defaultEditors.put(Collection.class, new CustomCollectionEditor(Collection.class));
/* 243 */     this.defaultEditors.put(Set.class, new CustomCollectionEditor(Set.class));
/* 244 */     this.defaultEditors.put(SortedSet.class, new CustomCollectionEditor(SortedSet.class));
/* 245 */     this.defaultEditors.put(List.class, new CustomCollectionEditor(List.class));
/* 246 */     this.defaultEditors.put(SortedMap.class, new CustomMapEditor(SortedMap.class));
/*     */ 
/*     */     
/* 249 */     this.defaultEditors.put(byte[].class, new ByteArrayPropertyEditor());
/* 250 */     this.defaultEditors.put(char[].class, new CharArrayPropertyEditor());
/*     */ 
/*     */     
/* 253 */     this.defaultEditors.put(char.class, new CharacterEditor(false));
/* 254 */     this.defaultEditors.put(Character.class, new CharacterEditor(true));
/*     */ 
/*     */     
/* 257 */     this.defaultEditors.put(boolean.class, new CustomBooleanEditor(false));
/* 258 */     this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));
/*     */ 
/*     */ 
/*     */     
/* 262 */     this.defaultEditors.put(byte.class, new CustomNumberEditor(Byte.class, false));
/* 263 */     this.defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, true));
/* 264 */     this.defaultEditors.put(short.class, new CustomNumberEditor(Short.class, false));
/* 265 */     this.defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, true));
/* 266 */     this.defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));
/* 267 */     this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
/* 268 */     this.defaultEditors.put(long.class, new CustomNumberEditor(Long.class, false));
/* 269 */     this.defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, true));
/* 270 */     this.defaultEditors.put(float.class, new CustomNumberEditor(Float.class, false));
/* 271 */     this.defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
/* 272 */     this.defaultEditors.put(double.class, new CustomNumberEditor(Double.class, false));
/* 273 */     this.defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
/* 274 */     this.defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
/* 275 */     this.defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
/*     */ 
/*     */     
/* 278 */     if (this.configValueEditorsActive) {
/* 279 */       StringArrayPropertyEditor sae = new StringArrayPropertyEditor();
/* 280 */       this.defaultEditors.put(String[].class, sae);
/* 281 */       this.defaultEditors.put(short[].class, sae);
/* 282 */       this.defaultEditors.put(int[].class, sae);
/* 283 */       this.defaultEditors.put(long[].class, sae);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyDefaultEditorsTo(PropertyEditorRegistrySupport target) {
/* 292 */     target.defaultEditorsActive = this.defaultEditorsActive;
/* 293 */     target.configValueEditorsActive = this.configValueEditorsActive;
/* 294 */     target.defaultEditors = this.defaultEditors;
/* 295 */     target.overriddenDefaultEditors = this.overriddenDefaultEditors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
/* 305 */     registerCustomEditor(requiredType, null, propertyEditor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerCustomEditor(Class<?> requiredType, String propertyPath, PropertyEditor propertyEditor) {
/* 310 */     if (requiredType == null && propertyPath == null) {
/* 311 */       throw new IllegalArgumentException("Either requiredType or propertyPath is required");
/*     */     }
/* 313 */     if (propertyPath != null) {
/* 314 */       if (this.customEditorsForPath == null) {
/* 315 */         this.customEditorsForPath = new LinkedHashMap<String, CustomEditorHolder>(16);
/*     */       }
/* 317 */       this.customEditorsForPath.put(propertyPath, new CustomEditorHolder(propertyEditor, requiredType));
/*     */     } else {
/*     */       
/* 320 */       if (this.customEditors == null) {
/* 321 */         this.customEditors = new LinkedHashMap<Class<?>, PropertyEditor>(16);
/*     */       }
/* 323 */       this.customEditors.put(requiredType, propertyEditor);
/* 324 */       this.customEditorCache = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath) {
/* 330 */     Class<?> requiredTypeToUse = requiredType;
/* 331 */     if (propertyPath != null) {
/* 332 */       if (this.customEditorsForPath != null) {
/*     */         
/* 334 */         PropertyEditor editor = getCustomEditor(propertyPath, requiredType);
/* 335 */         if (editor == null) {
/* 336 */           List<String> strippedPaths = new LinkedList<String>();
/* 337 */           addStrippedPropertyPaths(strippedPaths, "", propertyPath);
/* 338 */           for (Iterator<String> it = strippedPaths.iterator(); it.hasNext() && editor == null; ) {
/* 339 */             String strippedPath = it.next();
/* 340 */             editor = getCustomEditor(strippedPath, requiredType);
/*     */           } 
/*     */         } 
/* 343 */         if (editor != null) {
/* 344 */           return editor;
/*     */         }
/*     */       } 
/* 347 */       if (requiredType == null) {
/* 348 */         requiredTypeToUse = getPropertyType(propertyPath);
/*     */       }
/*     */     } 
/*     */     
/* 352 */     return getCustomEditor(requiredTypeToUse);
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
/*     */   public boolean hasCustomEditorForElement(Class<?> elementType, String propertyPath) {
/* 365 */     if (propertyPath != null && this.customEditorsForPath != null) {
/* 366 */       for (Map.Entry<String, CustomEditorHolder> entry : this.customEditorsForPath.entrySet()) {
/* 367 */         if (PropertyAccessorUtils.matchesProperty(entry.getKey(), propertyPath) && (
/* 368 */           (CustomEditorHolder)entry.getValue()).getPropertyEditor(elementType) != null) {
/* 369 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 375 */     return (elementType != null && this.customEditors != null && this.customEditors.containsKey(elementType));
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
/*     */   protected Class<?> getPropertyType(String propertyPath) {
/* 390 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PropertyEditor getCustomEditor(String propertyName, Class<?> requiredType) {
/* 400 */     CustomEditorHolder holder = this.customEditorsForPath.get(propertyName);
/* 401 */     return (holder != null) ? holder.getPropertyEditor(requiredType) : null;
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
/*     */   private PropertyEditor getCustomEditor(Class<?> requiredType) {
/* 413 */     if (requiredType == null || this.customEditors == null) {
/* 414 */       return null;
/*     */     }
/*     */     
/* 417 */     PropertyEditor editor = this.customEditors.get(requiredType);
/* 418 */     if (editor == null) {
/*     */       
/* 420 */       if (this.customEditorCache != null) {
/* 421 */         editor = this.customEditorCache.get(requiredType);
/*     */       }
/* 423 */       if (editor == null)
/*     */       {
/* 425 */         for (Iterator<Class<?>> it = this.customEditors.keySet().iterator(); it.hasNext() && editor == null; ) {
/* 426 */           Class<?> key = it.next();
/* 427 */           if (key.isAssignableFrom(requiredType)) {
/* 428 */             editor = this.customEditors.get(key);
/*     */ 
/*     */             
/* 431 */             if (this.customEditorCache == null) {
/* 432 */               this.customEditorCache = new HashMap<Class<?>, PropertyEditor>();
/*     */             }
/* 434 */             this.customEditorCache.put(requiredType, editor);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 439 */     return editor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> guessPropertyTypeFromEditors(String propertyName) {
/* 449 */     if (this.customEditorsForPath != null) {
/* 450 */       CustomEditorHolder editorHolder = this.customEditorsForPath.get(propertyName);
/* 451 */       if (editorHolder == null) {
/* 452 */         List<String> strippedPaths = new LinkedList<String>();
/* 453 */         addStrippedPropertyPaths(strippedPaths, "", propertyName);
/* 454 */         for (Iterator<String> it = strippedPaths.iterator(); it.hasNext() && editorHolder == null; ) {
/* 455 */           String strippedName = it.next();
/* 456 */           editorHolder = this.customEditorsForPath.get(strippedName);
/*     */         } 
/*     */       } 
/* 459 */       if (editorHolder != null) {
/* 460 */         return editorHolder.getRegisteredType();
/*     */       }
/*     */     } 
/* 463 */     return null;
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
/*     */   protected void copyCustomEditorsTo(PropertyEditorRegistry target, String nestedProperty) {
/* 475 */     String actualPropertyName = (nestedProperty != null) ? PropertyAccessorUtils.getPropertyName(nestedProperty) : null;
/* 476 */     if (this.customEditors != null) {
/* 477 */       for (Map.Entry<Class<?>, PropertyEditor> entry : this.customEditors.entrySet()) {
/* 478 */         target.registerCustomEditor(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/* 481 */     if (this.customEditorsForPath != null) {
/* 482 */       for (Map.Entry<String, CustomEditorHolder> entry : this.customEditorsForPath.entrySet()) {
/* 483 */         String editorPath = entry.getKey();
/* 484 */         CustomEditorHolder editorHolder = entry.getValue();
/* 485 */         if (nestedProperty != null) {
/* 486 */           int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(editorPath);
/* 487 */           if (pos != -1) {
/* 488 */             String editorNestedProperty = editorPath.substring(0, pos);
/* 489 */             String editorNestedPath = editorPath.substring(pos + 1);
/* 490 */             if (editorNestedProperty.equals(nestedProperty) || editorNestedProperty.equals(actualPropertyName)) {
/* 491 */               target.registerCustomEditor(editorHolder
/* 492 */                   .getRegisteredType(), editorNestedPath, editorHolder.getPropertyEditor());
/*     */             }
/*     */           } 
/*     */           continue;
/*     */         } 
/* 497 */         target.registerCustomEditor(editorHolder
/* 498 */             .getRegisteredType(), editorPath, editorHolder.getPropertyEditor());
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
/*     */   private void addStrippedPropertyPaths(List<String> strippedPaths, String nestedPath, String propertyPath) {
/* 513 */     int startIndex = propertyPath.indexOf('[');
/* 514 */     if (startIndex != -1) {
/* 515 */       int endIndex = propertyPath.indexOf(']');
/* 516 */       if (endIndex != -1) {
/* 517 */         String prefix = propertyPath.substring(0, startIndex);
/* 518 */         String key = propertyPath.substring(startIndex, endIndex + 1);
/* 519 */         String suffix = propertyPath.substring(endIndex + 1, propertyPath.length());
/*     */         
/* 521 */         strippedPaths.add(nestedPath + prefix + suffix);
/*     */         
/* 523 */         addStrippedPropertyPaths(strippedPaths, nestedPath + prefix, suffix);
/*     */         
/* 525 */         addStrippedPropertyPaths(strippedPaths, nestedPath + prefix + key, suffix);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CustomEditorHolder
/*     */   {
/*     */     private final PropertyEditor propertyEditor;
/*     */ 
/*     */     
/*     */     private final Class<?> registeredType;
/*     */ 
/*     */ 
/*     */     
/*     */     private CustomEditorHolder(PropertyEditor propertyEditor, Class<?> registeredType) {
/* 542 */       this.propertyEditor = propertyEditor;
/* 543 */       this.registeredType = registeredType;
/*     */     }
/*     */     
/*     */     private PropertyEditor getPropertyEditor() {
/* 547 */       return this.propertyEditor;
/*     */     }
/*     */     
/*     */     private Class<?> getRegisteredType() {
/* 551 */       return this.registeredType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private PropertyEditor getPropertyEditor(Class<?> requiredType) {
/* 561 */       if (this.registeredType == null || (requiredType != null && (
/*     */         
/* 563 */         ClassUtils.isAssignable(this.registeredType, requiredType) || 
/* 564 */         ClassUtils.isAssignable(requiredType, this.registeredType))) || (requiredType == null && 
/*     */         
/* 566 */         !Collection.class.isAssignableFrom(this.registeredType) && !this.registeredType.isArray())) {
/* 567 */         return this.propertyEditor;
/*     */       }
/*     */       
/* 570 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyEditorRegistrySupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */