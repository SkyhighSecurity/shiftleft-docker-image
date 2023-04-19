/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionFailedException;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.NumberUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TypeConverterDelegate
/*     */ {
/*  56 */   private static final Log logger = LogFactory.getLog(TypeConverterDelegate.class);
/*     */ 
/*     */   
/*  59 */   private static Object javaUtilOptionalEmpty = null;
/*     */   
/*     */   static {
/*     */     try {
/*  63 */       Class<?> clazz = ClassUtils.forName("java.util.Optional", TypeConverterDelegate.class.getClassLoader());
/*  64 */       javaUtilOptionalEmpty = ClassUtils.getMethod(clazz, "empty", new Class[0]).invoke(null, new Object[0]);
/*     */     }
/*  66 */     catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final PropertyEditorRegistrySupport propertyEditorRegistry;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object targetObject;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry) {
/*  82 */     this(propertyEditorRegistry, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry, Object targetObject) {
/*  91 */     this.propertyEditorRegistry = propertyEditorRegistry;
/*  92 */     this.targetObject = targetObject;
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
/*     */   public <T> T convertIfNecessary(Object newValue, Class<T> requiredType, MethodParameter methodParam) throws IllegalArgumentException {
/* 109 */     return convertIfNecessary(null, null, newValue, requiredType, (methodParam != null) ? new TypeDescriptor(methodParam) : 
/* 110 */         TypeDescriptor.valueOf(requiredType));
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
/*     */   public <T> T convertIfNecessary(Object newValue, Class<T> requiredType, Field field) throws IllegalArgumentException {
/* 126 */     return convertIfNecessary(null, null, newValue, requiredType, (field != null) ? new TypeDescriptor(field) : 
/* 127 */         TypeDescriptor.valueOf(requiredType));
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
/*     */   public <T> T convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<T> requiredType) throws IllegalArgumentException {
/* 144 */     return convertIfNecessary(propertyName, oldValue, newValue, requiredType, TypeDescriptor.valueOf(requiredType));
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
/*     */   public <T> T convertIfNecessary(String propertyName, Object oldValue, Object<?> newValue, Class<T> requiredType, TypeDescriptor typeDescriptor) throws IllegalArgumentException {
/* 164 */     PropertyEditor editor = this.propertyEditorRegistry.findCustomEditor(requiredType, propertyName);
/*     */     
/* 166 */     ConversionFailedException conversionAttemptEx = null;
/*     */ 
/*     */     
/* 169 */     ConversionService conversionService = this.propertyEditorRegistry.getConversionService();
/* 170 */     if (editor == null && conversionService != null && newValue != null && typeDescriptor != null) {
/* 171 */       TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
/* 172 */       if (conversionService.canConvert(sourceTypeDesc, typeDescriptor)) {
/*     */         try {
/* 174 */           return (T)conversionService.convert(newValue, sourceTypeDesc, typeDescriptor);
/*     */         }
/* 176 */         catch (ConversionFailedException ex) {
/*     */           
/* 178 */           conversionAttemptEx = ex;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 183 */     Object<?> convertedValue = newValue;
/*     */ 
/*     */     
/* 186 */     if (editor != null || (requiredType != null && !ClassUtils.isAssignableValue(requiredType, convertedValue))) {
/* 187 */       if (typeDescriptor != null && requiredType != null && Collection.class.isAssignableFrom(requiredType) && convertedValue instanceof String) {
/*     */         
/* 189 */         TypeDescriptor elementTypeDesc = typeDescriptor.getElementTypeDescriptor();
/* 190 */         if (elementTypeDesc != null) {
/* 191 */           Class<?> elementType = elementTypeDesc.getType();
/* 192 */           if (Class.class == elementType || Enum.class.isAssignableFrom(elementType)) {
/* 193 */             convertedValue = (Object<?>)StringUtils.commaDelimitedListToStringArray((String)convertedValue);
/*     */           }
/*     */         } 
/*     */       } 
/* 197 */       if (editor == null) {
/* 198 */         editor = findDefaultEditor(requiredType);
/*     */       }
/* 200 */       convertedValue = (Object<?>)doConvertValue(oldValue, convertedValue, requiredType, editor);
/*     */     } 
/*     */     
/* 203 */     boolean standardConversion = false;
/*     */     
/* 205 */     if (requiredType != null) {
/*     */ 
/*     */       
/* 208 */       if (convertedValue != null) {
/* 209 */         if (Object.class == requiredType) {
/* 210 */           return (T)convertedValue;
/*     */         }
/* 212 */         if (requiredType.isArray()) {
/*     */           
/* 214 */           if (convertedValue instanceof String && Enum.class.isAssignableFrom(requiredType.getComponentType())) {
/* 215 */             convertedValue = (Object<?>)StringUtils.commaDelimitedListToStringArray((String)convertedValue);
/*     */           }
/* 217 */           return (T)convertToTypedArray(convertedValue, propertyName, requiredType.getComponentType());
/*     */         } 
/* 219 */         if (convertedValue instanceof Collection) {
/*     */           
/* 221 */           convertedValue = (Object<?>)convertToTypedCollection((Collection)convertedValue, propertyName, requiredType, typeDescriptor);
/*     */           
/* 223 */           standardConversion = true;
/*     */         }
/* 225 */         else if (convertedValue instanceof Map) {
/*     */           
/* 227 */           convertedValue = (Object<?>)convertToTypedMap((Map)convertedValue, propertyName, requiredType, typeDescriptor);
/*     */           
/* 229 */           standardConversion = true;
/*     */         } 
/* 231 */         if (convertedValue.getClass().isArray() && Array.getLength(convertedValue) == 1) {
/* 232 */           convertedValue = (Object<?>)Array.get(convertedValue, 0);
/* 233 */           standardConversion = true;
/*     */         } 
/* 235 */         if (String.class == requiredType && ClassUtils.isPrimitiveOrWrapper(convertedValue.getClass()))
/*     */         {
/* 237 */           return (T)convertedValue.toString();
/*     */         }
/* 239 */         if (convertedValue instanceof String && !requiredType.isInstance(convertedValue)) {
/* 240 */           if (conversionAttemptEx == null && !requiredType.isInterface() && !requiredType.isEnum()) {
/*     */             try {
/* 242 */               Constructor<T> strCtor = requiredType.getConstructor(new Class[] { String.class });
/* 243 */               return BeanUtils.instantiateClass(strCtor, new Object[] { convertedValue });
/*     */             }
/* 245 */             catch (NoSuchMethodException ex) {
/*     */               
/* 247 */               if (logger.isTraceEnabled()) {
/* 248 */                 logger.trace("No String constructor found on type [" + requiredType.getName() + "]", ex);
/*     */               }
/*     */             }
/* 251 */             catch (Exception ex) {
/* 252 */               if (logger.isDebugEnabled()) {
/* 253 */                 logger.debug("Construction via String failed for type [" + requiredType.getName() + "]", ex);
/*     */               }
/*     */             } 
/*     */           }
/* 257 */           String trimmedValue = ((String)convertedValue).trim();
/* 258 */           if (requiredType.isEnum() && "".equals(trimmedValue))
/*     */           {
/* 260 */             return null;
/*     */           }
/* 262 */           convertedValue = (Object<?>)attemptToConvertStringToEnum(requiredType, trimmedValue, convertedValue);
/* 263 */           standardConversion = true;
/*     */         }
/* 265 */         else if (convertedValue instanceof Number && Number.class.isAssignableFrom(requiredType)) {
/* 266 */           convertedValue = (Object<?>)NumberUtils.convertNumberToTargetClass((Number)convertedValue, requiredType);
/*     */           
/* 268 */           standardConversion = true;
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 273 */       else if (javaUtilOptionalEmpty != null && requiredType == javaUtilOptionalEmpty.getClass()) {
/* 274 */         convertedValue = (Object<?>)javaUtilOptionalEmpty;
/*     */       } 
/*     */ 
/*     */       
/* 278 */       if (!ClassUtils.isAssignableValue(requiredType, convertedValue)) {
/* 279 */         if (conversionAttemptEx != null)
/*     */         {
/* 281 */           throw conversionAttemptEx;
/*     */         }
/* 283 */         if (conversionService != null) {
/*     */ 
/*     */           
/* 286 */           TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
/* 287 */           if (conversionService.canConvert(sourceTypeDesc, typeDescriptor)) {
/* 288 */             return (T)conversionService.convert(newValue, sourceTypeDesc, typeDescriptor);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 293 */         StringBuilder msg = new StringBuilder();
/* 294 */         msg.append("Cannot convert value of type '").append(ClassUtils.getDescriptiveType(newValue));
/* 295 */         msg.append("' to required type '").append(ClassUtils.getQualifiedName(requiredType)).append("'");
/* 296 */         if (propertyName != null) {
/* 297 */           msg.append(" for property '").append(propertyName).append("'");
/*     */         }
/* 299 */         if (editor != null) {
/* 300 */           msg.append(": PropertyEditor [").append(editor.getClass().getName()).append("] returned inappropriate value of type '")
/* 301 */             .append(
/* 302 */               ClassUtils.getDescriptiveType(convertedValue)).append("'");
/* 303 */           throw new IllegalArgumentException(msg.toString());
/*     */         } 
/*     */         
/* 306 */         msg.append(": no matching editors or conversion strategy found");
/* 307 */         throw new IllegalStateException(msg.toString());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 312 */     if (conversionAttemptEx != null) {
/* 313 */       if (editor == null && !standardConversion && requiredType != null && Object.class != requiredType) {
/* 314 */         throw conversionAttemptEx;
/*     */       }
/* 316 */       logger.debug("Original ConversionService attempt failed - ignored since PropertyEditor based conversion eventually succeeded", (Throwable)conversionAttemptEx);
/*     */     } 
/*     */ 
/*     */     
/* 320 */     return (T)convertedValue;
/*     */   }
/*     */   
/*     */   private Object attemptToConvertStringToEnum(Class<?> requiredType, String trimmedValue, Object currentConvertedValue) {
/* 324 */     Object convertedValue = currentConvertedValue;
/*     */     
/* 326 */     if (Enum.class == requiredType) {
/*     */       
/* 328 */       int index = trimmedValue.lastIndexOf('.');
/* 329 */       if (index > -1) {
/* 330 */         String enumType = trimmedValue.substring(0, index);
/* 331 */         String fieldName = trimmedValue.substring(index + 1);
/* 332 */         ClassLoader cl = this.targetObject.getClass().getClassLoader();
/*     */         try {
/* 334 */           Class<?> enumValueType = ClassUtils.forName(enumType, cl);
/* 335 */           Field enumField = enumValueType.getField(fieldName);
/* 336 */           convertedValue = enumField.get(null);
/*     */         }
/* 338 */         catch (ClassNotFoundException ex) {
/* 339 */           if (logger.isTraceEnabled()) {
/* 340 */             logger.trace("Enum class [" + enumType + "] cannot be loaded", ex);
/*     */           }
/*     */         }
/* 343 */         catch (Throwable ex) {
/* 344 */           if (logger.isTraceEnabled()) {
/* 345 */             logger.trace("Field [" + fieldName + "] isn't an enum value for type [" + enumType + "]", ex);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 351 */     if (convertedValue == currentConvertedValue) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 356 */         Field enumField = requiredType.getField(trimmedValue);
/* 357 */         ReflectionUtils.makeAccessible(enumField);
/* 358 */         convertedValue = enumField.get(null);
/*     */       }
/* 360 */       catch (Throwable ex) {
/* 361 */         if (logger.isTraceEnabled()) {
/* 362 */           logger.trace("Field [" + convertedValue + "] isn't an enum value", ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 367 */     return convertedValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PropertyEditor findDefaultEditor(Class<?> requiredType) {
/* 375 */     PropertyEditor editor = null;
/* 376 */     if (requiredType != null) {
/*     */       
/* 378 */       editor = this.propertyEditorRegistry.getDefaultEditor(requiredType);
/* 379 */       if (editor == null && String.class != requiredType)
/*     */       {
/* 381 */         editor = BeanUtils.findEditorByConvention(requiredType);
/*     */       }
/*     */     } 
/* 384 */     return editor;
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
/*     */   private Object doConvertValue(Object oldValue, Object newValue, Class<?> requiredType, PropertyEditor editor) {
/* 399 */     Object convertedValue = newValue;
/*     */     
/* 401 */     if (editor != null && !(convertedValue instanceof String)) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/* 407 */         editor.setValue(convertedValue);
/* 408 */         Object newConvertedValue = editor.getValue();
/* 409 */         if (newConvertedValue != convertedValue) {
/* 410 */           convertedValue = newConvertedValue;
/*     */ 
/*     */           
/* 413 */           editor = null;
/*     */         }
/*     */       
/* 416 */       } catch (Exception ex) {
/* 417 */         if (logger.isDebugEnabled()) {
/* 418 */           logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 424 */     Object returnValue = convertedValue;
/*     */     
/* 426 */     if (requiredType != null && !requiredType.isArray() && convertedValue instanceof String[]) {
/*     */ 
/*     */ 
/*     */       
/* 430 */       if (logger.isTraceEnabled()) {
/* 431 */         logger.trace("Converting String array to comma-delimited String [" + convertedValue + "]");
/*     */       }
/* 433 */       convertedValue = StringUtils.arrayToCommaDelimitedString((Object[])convertedValue);
/*     */     } 
/*     */     
/* 436 */     if (convertedValue instanceof String) {
/* 437 */       if (editor != null) {
/*     */         
/* 439 */         if (logger.isTraceEnabled()) {
/* 440 */           logger.trace("Converting String to [" + requiredType + "] using property editor [" + editor + "]");
/*     */         }
/* 442 */         String newTextValue = (String)convertedValue;
/* 443 */         return doConvertTextValue(oldValue, newTextValue, editor);
/*     */       } 
/* 445 */       if (String.class == requiredType) {
/* 446 */         returnValue = convertedValue;
/*     */       }
/*     */     } 
/*     */     
/* 450 */     return returnValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object doConvertTextValue(Object oldValue, String newTextValue, PropertyEditor editor) {
/*     */     try {
/* 462 */       editor.setValue(oldValue);
/*     */     }
/* 464 */     catch (Exception ex) {
/* 465 */       if (logger.isDebugEnabled()) {
/* 466 */         logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", ex);
/*     */       }
/*     */     } 
/*     */     
/* 470 */     editor.setAsText(newTextValue);
/* 471 */     return editor.getValue();
/*     */   }
/*     */   
/*     */   private Object convertToTypedArray(Object input, String propertyName, Class<?> componentType) {
/* 475 */     if (input instanceof Collection) {
/*     */       
/* 477 */       Collection<?> coll = (Collection)input;
/* 478 */       Object object = Array.newInstance(componentType, coll.size());
/* 479 */       int i = 0;
/* 480 */       for (Iterator<?> it = coll.iterator(); it.hasNext(); i++) {
/* 481 */         Object object1 = convertIfNecessary(
/* 482 */             buildIndexedPropertyName(propertyName, i), null, it.next(), componentType);
/* 483 */         Array.set(object, i, object1);
/*     */       } 
/* 485 */       return object;
/*     */     } 
/* 487 */     if (input.getClass().isArray()) {
/*     */       
/* 489 */       if (componentType.equals(input.getClass().getComponentType()) && 
/* 490 */         !this.propertyEditorRegistry.hasCustomEditorForElement(componentType, propertyName)) {
/* 491 */         return input;
/*     */       }
/* 493 */       int arrayLength = Array.getLength(input);
/* 494 */       Object object = Array.newInstance(componentType, arrayLength);
/* 495 */       for (int i = 0; i < arrayLength; i++) {
/* 496 */         Object object1 = convertIfNecessary(
/* 497 */             buildIndexedPropertyName(propertyName, i), null, Array.get(input, i), componentType);
/* 498 */         Array.set(object, i, object1);
/*     */       } 
/* 500 */       return object;
/*     */     } 
/*     */ 
/*     */     
/* 504 */     Object result = Array.newInstance(componentType, 1);
/* 505 */     Object value = convertIfNecessary(
/* 506 */         buildIndexedPropertyName(propertyName, 0), null, input, componentType);
/* 507 */     Array.set(result, 0, value);
/* 508 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<?> convertToTypedCollection(Collection<?> original, String propertyName, Class<?> requiredType, TypeDescriptor typeDescriptor) {
/*     */     Iterator<?> it;
/*     */     Collection<Object> convertedCopy;
/* 516 */     if (!Collection.class.isAssignableFrom(requiredType)) {
/* 517 */       return original;
/*     */     }
/*     */     
/* 520 */     boolean approximable = CollectionFactory.isApproximableCollectionType(requiredType);
/* 521 */     if (!approximable && !canCreateCopy(requiredType)) {
/* 522 */       if (logger.isDebugEnabled()) {
/* 523 */         logger.debug("Custom Collection type [" + original.getClass().getName() + "] does not allow for creating a copy - injecting original Collection as-is");
/*     */       }
/*     */       
/* 526 */       return original;
/*     */     } 
/*     */     
/* 529 */     boolean originalAllowed = requiredType.isInstance(original);
/* 530 */     TypeDescriptor elementType = typeDescriptor.getElementTypeDescriptor();
/* 531 */     if (elementType == null && originalAllowed && 
/* 532 */       !this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName)) {
/* 533 */       return original;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 538 */       it = original.iterator();
/* 539 */       if (it == null) {
/* 540 */         if (logger.isDebugEnabled()) {
/* 541 */           logger.debug("Collection of type [" + original.getClass().getName() + "] returned null Iterator - injecting original Collection as-is");
/*     */         }
/*     */         
/* 544 */         return original;
/*     */       }
/*     */     
/* 547 */     } catch (Throwable ex) {
/* 548 */       if (logger.isDebugEnabled()) {
/* 549 */         logger.debug("Cannot access Collection of type [" + original.getClass().getName() + "] - injecting original Collection as-is: " + ex);
/*     */       }
/*     */       
/* 552 */       return original;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 557 */       if (approximable) {
/* 558 */         convertedCopy = CollectionFactory.createApproximateCollection(original, original.size());
/*     */       } else {
/*     */         
/* 561 */         convertedCopy = (Collection<Object>)requiredType.newInstance();
/*     */       }
/*     */     
/* 564 */     } catch (Throwable ex) {
/* 565 */       if (logger.isDebugEnabled()) {
/* 566 */         logger.debug("Cannot create copy of Collection type [" + original.getClass().getName() + "] - injecting original Collection as-is: " + ex);
/*     */       }
/*     */       
/* 569 */       return original;
/*     */     } 
/*     */     
/* 572 */     int i = 0;
/* 573 */     for (; it.hasNext(); i++) {
/* 574 */       Object element = it.next();
/* 575 */       String indexedPropertyName = buildIndexedPropertyName(propertyName, i);
/* 576 */       Object convertedElement = convertIfNecessary(indexedPropertyName, null, element, (elementType != null) ? elementType
/* 577 */           .getType() : null, elementType);
/*     */       try {
/* 579 */         convertedCopy.add(convertedElement);
/*     */       }
/* 581 */       catch (Throwable ex) {
/* 582 */         if (logger.isDebugEnabled()) {
/* 583 */           logger.debug("Collection type [" + original.getClass().getName() + "] seems to be read-only - injecting original Collection as-is: " + ex);
/*     */         }
/*     */         
/* 586 */         return original;
/*     */       } 
/* 588 */       originalAllowed = (originalAllowed && element == convertedElement);
/*     */     } 
/* 590 */     return originalAllowed ? original : convertedCopy;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<?, ?> convertToTypedMap(Map<?, ?> original, String propertyName, Class<?> requiredType, TypeDescriptor typeDescriptor) {
/*     */     Iterator<?> it;
/*     */     Map<Object, Object> convertedCopy;
/* 597 */     if (!Map.class.isAssignableFrom(requiredType)) {
/* 598 */       return original;
/*     */     }
/*     */     
/* 601 */     boolean approximable = CollectionFactory.isApproximableMapType(requiredType);
/* 602 */     if (!approximable && !canCreateCopy(requiredType)) {
/* 603 */       if (logger.isDebugEnabled()) {
/* 604 */         logger.debug("Custom Map type [" + original.getClass().getName() + "] does not allow for creating a copy - injecting original Map as-is");
/*     */       }
/*     */       
/* 607 */       return original;
/*     */     } 
/*     */     
/* 610 */     boolean originalAllowed = requiredType.isInstance(original);
/* 611 */     TypeDescriptor keyType = typeDescriptor.getMapKeyTypeDescriptor();
/* 612 */     TypeDescriptor valueType = typeDescriptor.getMapValueTypeDescriptor();
/* 613 */     if (keyType == null && valueType == null && originalAllowed && 
/* 614 */       !this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName)) {
/* 615 */       return original;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 620 */       it = original.entrySet().iterator();
/* 621 */       if (it == null) {
/* 622 */         if (logger.isDebugEnabled()) {
/* 623 */           logger.debug("Map of type [" + original.getClass().getName() + "] returned null Iterator - injecting original Map as-is");
/*     */         }
/*     */         
/* 626 */         return original;
/*     */       }
/*     */     
/* 629 */     } catch (Throwable ex) {
/* 630 */       if (logger.isDebugEnabled()) {
/* 631 */         logger.debug("Cannot access Map of type [" + original.getClass().getName() + "] - injecting original Map as-is: " + ex);
/*     */       }
/*     */       
/* 634 */       return original;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 639 */       if (approximable) {
/* 640 */         convertedCopy = CollectionFactory.createApproximateMap(original, original.size());
/*     */       } else {
/*     */         
/* 643 */         convertedCopy = (Map<Object, Object>)requiredType.newInstance();
/*     */       }
/*     */     
/* 646 */     } catch (Throwable ex) {
/* 647 */       if (logger.isDebugEnabled()) {
/* 648 */         logger.debug("Cannot create copy of Map type [" + original.getClass().getName() + "] - injecting original Map as-is: " + ex);
/*     */       }
/*     */       
/* 651 */       return original;
/*     */     } 
/*     */     
/* 654 */     while (it.hasNext()) {
/* 655 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)it.next();
/* 656 */       Object key = entry.getKey();
/* 657 */       Object value = entry.getValue();
/* 658 */       String keyedPropertyName = buildKeyedPropertyName(propertyName, key);
/* 659 */       Object convertedKey = convertIfNecessary(keyedPropertyName, null, key, (keyType != null) ? keyType
/* 660 */           .getType() : null, keyType);
/* 661 */       Object convertedValue = convertIfNecessary(keyedPropertyName, null, value, (valueType != null) ? valueType
/* 662 */           .getType() : null, valueType);
/*     */       try {
/* 664 */         convertedCopy.put(convertedKey, convertedValue);
/*     */       }
/* 666 */       catch (Throwable ex) {
/* 667 */         if (logger.isDebugEnabled()) {
/* 668 */           logger.debug("Map type [" + original.getClass().getName() + "] seems to be read-only - injecting original Map as-is: " + ex);
/*     */         }
/*     */         
/* 671 */         return original;
/*     */       } 
/* 673 */       originalAllowed = (originalAllowed && key == convertedKey && value == convertedValue);
/*     */     } 
/* 675 */     return originalAllowed ? original : convertedCopy;
/*     */   }
/*     */   
/*     */   private String buildIndexedPropertyName(String propertyName, int index) {
/* 679 */     return (propertyName != null) ? (propertyName + "[" + index + "]") : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String buildKeyedPropertyName(String propertyName, Object key) {
/* 685 */     return (propertyName != null) ? (propertyName + "[" + key + "]") : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canCreateCopy(Class<?> requiredType) {
/* 691 */     return (!requiredType.isInterface() && !Modifier.isAbstract(requiredType.getModifiers()) && 
/* 692 */       Modifier.isPublic(requiredType.getModifiers()) && ClassUtils.hasConstructor(requiredType, new Class[0]));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\TypeConverterDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */