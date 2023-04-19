/*      */ package org.springframework.beans;
/*      */ 
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.core.CollectionFactory;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.convert.ConversionException;
/*      */ import org.springframework.core.convert.ConverterNotFoundException;
/*      */ import org.springframework.core.convert.TypeDescriptor;
/*      */ import org.springframework.lang.UsesJava8;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractNestablePropertyAccessor
/*      */   extends AbstractPropertyAccessor
/*      */ {
/*   76 */   private static final Log logger = LogFactory.getLog(AbstractNestablePropertyAccessor.class);
/*      */   
/*   78 */   private static Class<?> javaUtilOptionalClass = null;
/*      */ 
/*      */   
/*      */   static {
/*      */     try {
/*   83 */       javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", AbstractNestablePropertyAccessor.class.getClassLoader());
/*      */     }
/*   85 */     catch (ClassNotFoundException classNotFoundException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   91 */   private int autoGrowCollectionLimit = Integer.MAX_VALUE;
/*      */   
/*      */   Object wrappedObject;
/*      */   
/*   95 */   private String nestedPath = "";
/*      */ 
/*      */ 
/*      */   
/*      */   Object rootObject;
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, AbstractNestablePropertyAccessor> nestedPropertyAccessors;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor() {
/*  109 */     this(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(boolean registerDefaultEditors) {
/*  119 */     if (registerDefaultEditors) {
/*  120 */       registerDefaultEditors();
/*      */     }
/*  122 */     this.typeConverterDelegate = new TypeConverterDelegate(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(Object object) {
/*  130 */     registerDefaultEditors();
/*  131 */     setWrappedInstance(object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(Class<?> clazz) {
/*  139 */     registerDefaultEditors();
/*  140 */     setWrappedInstance(BeanUtils.instantiateClass(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(Object object, String nestedPath, Object rootObject) {
/*  151 */     registerDefaultEditors();
/*  152 */     setWrappedInstance(object, nestedPath, rootObject);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(Object object, String nestedPath, AbstractNestablePropertyAccessor parent) {
/*  163 */     setWrappedInstance(object, nestedPath, parent.getWrappedInstance());
/*  164 */     setExtractOldValueForEditor(parent.isExtractOldValueForEditor());
/*  165 */     setAutoGrowNestedPaths(parent.isAutoGrowNestedPaths());
/*  166 */     setAutoGrowCollectionLimit(parent.getAutoGrowCollectionLimit());
/*  167 */     setConversionService(parent.getConversionService());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
/*  176 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAutoGrowCollectionLimit() {
/*  183 */     return this.autoGrowCollectionLimit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWrappedInstance(Object object) {
/*  192 */     setWrappedInstance(object, "", (Object)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWrappedInstance(Object object, String nestedPath, Object rootObject) {
/*  203 */     Assert.notNull(object, "Target object must not be null");
/*  204 */     if (object.getClass() == javaUtilOptionalClass) {
/*  205 */       this.wrappedObject = OptionalUnwrapper.unwrap(object);
/*      */     } else {
/*      */       
/*  208 */       this.wrappedObject = object;
/*      */     } 
/*  210 */     this.nestedPath = (nestedPath != null) ? nestedPath : "";
/*  211 */     this.rootObject = !"".equals(this.nestedPath) ? rootObject : this.wrappedObject;
/*  212 */     this.nestedPropertyAccessors = null;
/*  213 */     this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
/*      */   }
/*      */   
/*      */   public final Object getWrappedInstance() {
/*  217 */     return this.wrappedObject;
/*      */   }
/*      */   
/*      */   public final Class<?> getWrappedClass() {
/*  221 */     return (this.wrappedObject != null) ? this.wrappedObject.getClass() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getNestedPath() {
/*  228 */     return this.nestedPath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getRootInstance() {
/*  236 */     return this.rootObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Class<?> getRootClass() {
/*  244 */     return (this.rootObject != null) ? this.rootObject.getClass() : null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPropertyValue(String propertyName, Object value) throws BeansException {
/*      */     AbstractNestablePropertyAccessor nestedPa;
/*      */     try {
/*  251 */       nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*      */     }
/*  253 */     catch (NotReadablePropertyException ex) {
/*  254 */       throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, "Nested property in path '" + propertyName + "' does not exist", ex);
/*      */     } 
/*      */     
/*  257 */     PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
/*  258 */     nestedPa.setPropertyValue(tokens, new PropertyValue(propertyName, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPropertyValue(PropertyValue pv) throws BeansException {
/*  263 */     PropertyTokenHolder tokens = (PropertyTokenHolder)pv.resolvedTokens;
/*  264 */     if (tokens == null) {
/*  265 */       AbstractNestablePropertyAccessor nestedPa; String propertyName = pv.getName();
/*      */       
/*      */       try {
/*  268 */         nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*      */       }
/*  270 */       catch (NotReadablePropertyException ex) {
/*  271 */         throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, "Nested property in path '" + propertyName + "' does not exist", ex);
/*      */       } 
/*      */       
/*  274 */       tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
/*  275 */       if (nestedPa == this) {
/*  276 */         (pv.getOriginalPropertyValue()).resolvedTokens = tokens;
/*      */       }
/*  278 */       nestedPa.setPropertyValue(tokens, pv);
/*      */     } else {
/*      */       
/*  281 */       setPropertyValue(tokens, pv);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void setPropertyValue(PropertyTokenHolder tokens, PropertyValue pv) throws BeansException {
/*  286 */     if (tokens.keys != null) {
/*  287 */       processKeyedProperty(tokens, pv);
/*      */     } else {
/*      */       
/*  290 */       processLocalProperty(tokens, pv);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void processKeyedProperty(PropertyTokenHolder tokens, PropertyValue pv) {
/*  296 */     Object propValue = getPropertyHoldingValue(tokens);
/*  297 */     String lastKey = tokens.keys[tokens.keys.length - 1];
/*      */     
/*  299 */     if (propValue.getClass().isArray()) {
/*  300 */       PropertyHandler ph = getLocalPropertyHandler(tokens.actualName);
/*  301 */       Class<?> requiredType = propValue.getClass().getComponentType();
/*  302 */       int arrayIndex = Integer.parseInt(lastKey);
/*  303 */       Object oldValue = null;
/*      */       try {
/*  305 */         if (isExtractOldValueForEditor() && arrayIndex < Array.getLength(propValue)) {
/*  306 */           oldValue = Array.get(propValue, arrayIndex);
/*      */         }
/*  308 */         Object convertedValue = convertIfNecessary(tokens.canonicalName, oldValue, pv.getValue(), requiredType, ph
/*  309 */             .nested(tokens.keys.length));
/*  310 */         int length = Array.getLength(propValue);
/*  311 */         if (arrayIndex >= length && arrayIndex < this.autoGrowCollectionLimit) {
/*  312 */           Class<?> componentType = propValue.getClass().getComponentType();
/*  313 */           Object newArray = Array.newInstance(componentType, arrayIndex + 1);
/*  314 */           System.arraycopy(propValue, 0, newArray, 0, length);
/*  315 */           setPropertyValue(tokens.actualName, newArray);
/*  316 */           propValue = getPropertyValue(tokens.actualName);
/*      */         } 
/*  318 */         Array.set(propValue, arrayIndex, convertedValue);
/*      */       }
/*  320 */       catch (IndexOutOfBoundsException ex) {
/*  321 */         throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Invalid array index in property path '" + tokens.canonicalName + "'", ex);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  326 */     else if (propValue instanceof List) {
/*  327 */       PropertyHandler ph = getPropertyHandler(tokens.actualName);
/*  328 */       Class<?> requiredType = ph.getCollectionType(tokens.keys.length);
/*  329 */       List<Object> list = (List<Object>)propValue;
/*  330 */       int index = Integer.parseInt(lastKey);
/*  331 */       Object oldValue = null;
/*  332 */       if (isExtractOldValueForEditor() && index < list.size()) {
/*  333 */         oldValue = list.get(index);
/*      */       }
/*  335 */       Object convertedValue = convertIfNecessary(tokens.canonicalName, oldValue, pv.getValue(), requiredType, ph
/*  336 */           .nested(tokens.keys.length));
/*  337 */       int size = list.size();
/*  338 */       if (index >= size && index < this.autoGrowCollectionLimit) {
/*  339 */         for (int i = size; i < index; i++) {
/*      */           try {
/*  341 */             list.add(null);
/*      */           }
/*  343 */           catch (NullPointerException ex) {
/*  344 */             throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Cannot set element with index " + index + " in List of size " + size + ", accessed using property path '" + tokens.canonicalName + "': List does not support filling up gaps with null elements");
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  350 */         list.add(convertedValue);
/*      */       } else {
/*      */         
/*      */         try {
/*  354 */           list.set(index, convertedValue);
/*      */         }
/*  356 */         catch (IndexOutOfBoundsException ex) {
/*  357 */           throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Invalid list index in property path '" + tokens.canonicalName + "'", ex);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  363 */     else if (propValue instanceof Map) {
/*  364 */       PropertyHandler ph = getLocalPropertyHandler(tokens.actualName);
/*  365 */       Class<?> mapKeyType = ph.getMapKeyType(tokens.keys.length);
/*  366 */       Class<?> mapValueType = ph.getMapValueType(tokens.keys.length);
/*  367 */       Map<Object, Object> map = (Map<Object, Object>)propValue;
/*      */ 
/*      */       
/*  370 */       TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(mapKeyType);
/*  371 */       Object convertedMapKey = convertIfNecessary((String)null, (Object)null, lastKey, mapKeyType, typeDescriptor);
/*  372 */       Object oldValue = null;
/*  373 */       if (isExtractOldValueForEditor()) {
/*  374 */         oldValue = map.get(convertedMapKey);
/*      */       }
/*      */ 
/*      */       
/*  378 */       Object convertedMapValue = convertIfNecessary(tokens.canonicalName, oldValue, pv.getValue(), mapValueType, ph
/*  379 */           .nested(tokens.keys.length));
/*  380 */       map.put(convertedMapKey, convertedMapValue);
/*      */     }
/*      */     else {
/*      */       
/*  384 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Property referenced in indexed property path '" + tokens.canonicalName + "' is neither an array nor a List nor a Map; returned value was [" + propValue + "]");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Object getPropertyHoldingValue(PropertyTokenHolder tokens) {
/*      */     Object propValue;
/*  392 */     PropertyTokenHolder getterTokens = new PropertyTokenHolder();
/*  393 */     getterTokens.canonicalName = tokens.canonicalName;
/*  394 */     getterTokens.actualName = tokens.actualName;
/*  395 */     getterTokens.keys = new String[tokens.keys.length - 1];
/*  396 */     System.arraycopy(tokens.keys, 0, getterTokens.keys, 0, tokens.keys.length - 1);
/*      */ 
/*      */     
/*      */     try {
/*  400 */       propValue = getPropertyValue(getterTokens);
/*      */     }
/*  402 */     catch (NotReadablePropertyException ex) {
/*  403 */       throw new NotWritablePropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Cannot access indexed value in property referenced in indexed property path '" + tokens.canonicalName + "'", ex);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  408 */     if (propValue == null)
/*      */     {
/*  410 */       if (isAutoGrowNestedPaths()) {
/*  411 */         int lastKeyIndex = tokens.canonicalName.lastIndexOf('[');
/*  412 */         getterTokens.canonicalName = tokens.canonicalName.substring(0, lastKeyIndex);
/*  413 */         propValue = setDefaultValue(getterTokens);
/*      */       } else {
/*      */         
/*  416 */         throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + tokens.canonicalName, "Cannot access indexed value in property referenced in indexed property path '" + tokens.canonicalName + "': returned null");
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  421 */     return propValue;
/*      */   }
/*      */   
/*      */   private void processLocalProperty(PropertyTokenHolder tokens, PropertyValue pv) {
/*  425 */     PropertyHandler ph = getLocalPropertyHandler(tokens.actualName);
/*  426 */     if (ph == null || !ph.isWritable()) {
/*  427 */       if (pv.isOptional()) {
/*  428 */         if (logger.isDebugEnabled()) {
/*  429 */           logger.debug("Ignoring optional value for property '" + tokens.actualName + "' - property not found on bean class [" + 
/*  430 */               getRootClass().getName() + "]");
/*      */         }
/*      */         
/*      */         return;
/*      */       } 
/*  435 */       throw createNotWritablePropertyException(tokens.canonicalName);
/*      */     } 
/*      */ 
/*      */     
/*  439 */     Object oldValue = null;
/*      */     try {
/*  441 */       Object originalValue = pv.getValue();
/*  442 */       Object valueToApply = originalValue;
/*  443 */       if (!Boolean.FALSE.equals(pv.conversionNecessary)) {
/*  444 */         if (pv.isConverted()) {
/*  445 */           valueToApply = pv.getConvertedValue();
/*      */         } else {
/*      */           
/*  448 */           if (isExtractOldValueForEditor() && ph.isReadable()) {
/*      */             try {
/*  450 */               oldValue = ph.getValue();
/*      */             }
/*  452 */             catch (Exception ex) {
/*  453 */               if (ex instanceof PrivilegedActionException) {
/*  454 */                 ex = ((PrivilegedActionException)ex).getException();
/*      */               }
/*  456 */               if (logger.isDebugEnabled()) {
/*  457 */                 logger.debug("Could not read previous value of property '" + this.nestedPath + tokens.canonicalName + "'", ex);
/*      */               }
/*      */             } 
/*      */           }
/*      */           
/*  462 */           valueToApply = convertForProperty(tokens.canonicalName, oldValue, originalValue, ph
/*  463 */               .toTypeDescriptor());
/*      */         } 
/*  465 */         (pv.getOriginalPropertyValue()).conversionNecessary = Boolean.valueOf((valueToApply != originalValue));
/*      */       } 
/*  467 */       ph.setValue(this.wrappedObject, valueToApply);
/*      */     }
/*  469 */     catch (TypeMismatchException ex) {
/*  470 */       throw ex;
/*      */     }
/*  472 */     catch (InvocationTargetException ex) {
/*      */       
/*  474 */       PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this.rootObject, this.nestedPath + tokens.canonicalName, oldValue, pv.getValue());
/*  475 */       if (ex.getTargetException() instanceof ClassCastException) {
/*  476 */         throw new TypeMismatchException(propertyChangeEvent, ph.getPropertyType(), ex.getTargetException());
/*      */       }
/*      */       
/*  479 */       Throwable cause = ex.getTargetException();
/*  480 */       if (cause instanceof java.lang.reflect.UndeclaredThrowableException)
/*      */       {
/*  482 */         cause = cause.getCause();
/*      */       }
/*  484 */       throw new MethodInvocationException(propertyChangeEvent, cause);
/*      */     
/*      */     }
/*  487 */     catch (Exception ex) {
/*      */       
/*  489 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + tokens.canonicalName, oldValue, pv.getValue());
/*  490 */       throw new MethodInvocationException(pce, ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Class<?> getPropertyType(String propertyName) throws BeansException {
/*      */     try {
/*  497 */       PropertyHandler ph = getPropertyHandler(propertyName);
/*  498 */       if (ph != null) {
/*  499 */         return ph.getPropertyType();
/*      */       }
/*      */ 
/*      */       
/*  503 */       Object value = getPropertyValue(propertyName);
/*  504 */       if (value != null) {
/*  505 */         return value.getClass();
/*      */       }
/*      */ 
/*      */       
/*  509 */       Class<?> editorType = guessPropertyTypeFromEditors(propertyName);
/*  510 */       if (editorType != null) {
/*  511 */         return editorType;
/*      */       
/*      */       }
/*      */     }
/*  515 */     catch (InvalidPropertyException invalidPropertyException) {}
/*      */ 
/*      */     
/*  518 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
/*      */     try {
/*  524 */       AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*  525 */       String finalPath = getFinalPath(nestedPa, propertyName);
/*  526 */       PropertyTokenHolder tokens = getPropertyNameTokens(finalPath);
/*  527 */       PropertyHandler ph = nestedPa.getLocalPropertyHandler(tokens.actualName);
/*  528 */       if (ph != null) {
/*  529 */         if (tokens.keys != null) {
/*  530 */           if (ph.isReadable() || ph.isWritable()) {
/*  531 */             return ph.nested(tokens.keys.length);
/*      */           
/*      */           }
/*      */         }
/*  535 */         else if (ph.isReadable() || ph.isWritable()) {
/*  536 */           return ph.toTypeDescriptor();
/*      */         }
/*      */       
/*      */       }
/*      */     }
/*  541 */     catch (InvalidPropertyException invalidPropertyException) {}
/*      */ 
/*      */     
/*  544 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadableProperty(String propertyName) {
/*      */     try {
/*  550 */       PropertyHandler ph = getPropertyHandler(propertyName);
/*  551 */       if (ph != null) {
/*  552 */         return ph.isReadable();
/*      */       }
/*      */ 
/*      */       
/*  556 */       getPropertyValue(propertyName);
/*  557 */       return true;
/*      */     
/*      */     }
/*  560 */     catch (InvalidPropertyException invalidPropertyException) {
/*      */ 
/*      */       
/*  563 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isWritableProperty(String propertyName) {
/*      */     try {
/*  569 */       PropertyHandler ph = getPropertyHandler(propertyName);
/*  570 */       if (ph != null) {
/*  571 */         return ph.isWritable();
/*      */       }
/*      */ 
/*      */       
/*  575 */       getPropertyValue(propertyName);
/*  576 */       return true;
/*      */     
/*      */     }
/*  579 */     catch (InvalidPropertyException invalidPropertyException) {
/*      */ 
/*      */       
/*  582 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private Object convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<?> requiredType, TypeDescriptor td) throws TypeMismatchException {
/*      */     try {
/*  588 */       return this.typeConverterDelegate.convertIfNecessary(propertyName, oldValue, newValue, requiredType, td);
/*      */     }
/*  590 */     catch (ConverterNotFoundException ex) {
/*  591 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
/*      */       
/*  593 */       throw new ConversionNotSupportedException(pce, td.getType(), ex);
/*      */     }
/*  595 */     catch (ConversionException ex) {
/*  596 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
/*      */       
/*  598 */       throw new TypeMismatchException(pce, requiredType, ex);
/*      */     }
/*  600 */     catch (IllegalStateException ex) {
/*  601 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
/*      */       
/*  603 */       throw new ConversionNotSupportedException(pce, requiredType, ex);
/*      */     }
/*  605 */     catch (IllegalArgumentException ex) {
/*  606 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
/*      */       
/*  608 */       throw new TypeMismatchException(pce, requiredType, ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object convertForProperty(String propertyName, Object oldValue, Object newValue, TypeDescriptor td) throws TypeMismatchException {
/*  615 */     return convertIfNecessary(propertyName, oldValue, newValue, td.getType(), td);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getPropertyValue(String propertyName) throws BeansException {
/*  620 */     AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*  621 */     PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
/*  622 */     return nestedPa.getPropertyValue(tokens);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object getPropertyValue(PropertyTokenHolder tokens) throws BeansException {
/*  627 */     String propertyName = tokens.canonicalName;
/*  628 */     String actualName = tokens.actualName;
/*  629 */     PropertyHandler ph = getLocalPropertyHandler(actualName);
/*  630 */     if (ph == null || !ph.isReadable()) {
/*  631 */       throw new NotReadablePropertyException(getRootClass(), this.nestedPath + propertyName);
/*      */     }
/*      */     try {
/*  634 */       Object value = ph.getValue();
/*  635 */       if (tokens.keys != null) {
/*  636 */         if (value == null) {
/*  637 */           if (isAutoGrowNestedPaths()) {
/*  638 */             value = setDefaultValue(tokens.actualName);
/*      */           } else {
/*      */             
/*  641 */             throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, "Cannot access indexed value of property referenced in indexed property path '" + propertyName + "': returned null");
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*  646 */         String indexedPropertyName = tokens.actualName;
/*      */         
/*  648 */         for (int i = 0; i < tokens.keys.length; i++) {
/*  649 */           String key = tokens.keys[i];
/*  650 */           if (value == null) {
/*  651 */             throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, "Cannot access indexed value of property referenced in indexed property path '" + propertyName + "': returned null");
/*      */           }
/*      */ 
/*      */           
/*  655 */           if (value.getClass().isArray()) {
/*  656 */             int index = Integer.parseInt(key);
/*  657 */             value = growArrayIfNecessary(value, index, indexedPropertyName);
/*  658 */             value = Array.get(value, index);
/*      */           }
/*  660 */           else if (value instanceof List) {
/*  661 */             int index = Integer.parseInt(key);
/*  662 */             List<Object> list = (List<Object>)value;
/*  663 */             growCollectionIfNecessary(list, index, indexedPropertyName, ph, i + 1);
/*  664 */             value = list.get(index);
/*      */           }
/*  666 */           else if (value instanceof Set) {
/*      */             
/*  668 */             Set<Object> set = (Set<Object>)value;
/*  669 */             int index = Integer.parseInt(key);
/*  670 */             if (index < 0 || index >= set.size()) {
/*  671 */               throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Cannot get element with index " + index + " from Set of size " + set
/*      */                   
/*  673 */                   .size() + ", accessed using property path '" + propertyName + "'");
/*      */             }
/*  675 */             Iterator<Object> it = set.iterator();
/*  676 */             for (int j = 0; it.hasNext(); j++) {
/*  677 */               Object elem = it.next();
/*  678 */               if (j == index) {
/*  679 */                 value = elem;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*  684 */           } else if (value instanceof Map) {
/*  685 */             Map<Object, Object> map = (Map<Object, Object>)value;
/*  686 */             Class<?> mapKeyType = ph.getResolvableType().getNested(i + 1).asMap().resolveGeneric(new int[] { 0 });
/*      */ 
/*      */             
/*  689 */             TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(mapKeyType);
/*  690 */             Object convertedMapKey = convertIfNecessary((String)null, (Object)null, key, mapKeyType, typeDescriptor);
/*  691 */             value = map.get(convertedMapKey);
/*      */           } else {
/*      */             
/*  694 */             throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Property referenced in indexed property path '" + propertyName + "' is neither an array nor a List nor a Set nor a Map; returned value was [" + value + "]");
/*      */           } 
/*      */ 
/*      */           
/*  698 */           indexedPropertyName = indexedPropertyName + "[" + key + "]";
/*      */         } 
/*      */       } 
/*  701 */       return value;
/*      */     }
/*  703 */     catch (IndexOutOfBoundsException ex) {
/*  704 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Index of out of bounds in property path '" + propertyName + "'", ex);
/*      */     
/*      */     }
/*  707 */     catch (NumberFormatException ex) {
/*  708 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Invalid index in property path '" + propertyName + "'", ex);
/*      */     
/*      */     }
/*  711 */     catch (TypeMismatchException ex) {
/*  712 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Invalid index in property path '" + propertyName + "'", ex);
/*      */     
/*      */     }
/*  715 */     catch (InvocationTargetException ex) {
/*  716 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Getter for property '" + actualName + "' threw exception", ex);
/*      */     
/*      */     }
/*  719 */     catch (Exception ex) {
/*  720 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Illegal attempt to get property '" + actualName + "' threw exception", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertyHandler getPropertyHandler(String propertyName) throws BeansException {
/*  735 */     Assert.notNull(propertyName, "Property name must not be null");
/*  736 */     AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*  737 */     return nestedPa.getLocalPropertyHandler(getFinalPath(nestedPa, propertyName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object growArrayIfNecessary(Object array, int index, String name) {
/*  764 */     if (!isAutoGrowNestedPaths()) {
/*  765 */       return array;
/*      */     }
/*  767 */     int length = Array.getLength(array);
/*  768 */     if (index >= length && index < this.autoGrowCollectionLimit) {
/*  769 */       Class<?> componentType = array.getClass().getComponentType();
/*  770 */       Object newArray = Array.newInstance(componentType, index + 1);
/*  771 */       System.arraycopy(array, 0, newArray, 0, length);
/*  772 */       for (int i = length; i < Array.getLength(newArray); i++) {
/*  773 */         Array.set(newArray, i, newValue(componentType, (TypeDescriptor)null, name));
/*      */       }
/*  775 */       setPropertyValue(name, newArray);
/*  776 */       return getPropertyValue(name);
/*      */     } 
/*      */     
/*  779 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void growCollectionIfNecessary(Collection<Object> collection, int index, String name, PropertyHandler ph, int nestingLevel) {
/*  786 */     if (!isAutoGrowNestedPaths()) {
/*      */       return;
/*      */     }
/*  789 */     int size = collection.size();
/*  790 */     if (index >= size && index < this.autoGrowCollectionLimit) {
/*  791 */       Class<?> elementType = ph.getResolvableType().getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
/*  792 */       if (elementType != null) {
/*  793 */         for (int i = collection.size(); i < index + 1; i++) {
/*  794 */           collection.add(newValue(elementType, (TypeDescriptor)null, name));
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getFinalPath(AbstractNestablePropertyAccessor pa, String nestedPath) {
/*  807 */     if (pa == this) {
/*  808 */       return nestedPath;
/*      */     }
/*  810 */     return nestedPath.substring(PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(nestedPath) + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor getPropertyAccessorForPropertyPath(String propertyPath) {
/*  820 */     int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
/*      */     
/*  822 */     if (pos > -1) {
/*  823 */       String nestedProperty = propertyPath.substring(0, pos);
/*  824 */       String nestedPath = propertyPath.substring(pos + 1);
/*  825 */       AbstractNestablePropertyAccessor nestedPa = getNestedPropertyAccessor(nestedProperty);
/*  826 */       return nestedPa.getPropertyAccessorForPropertyPath(nestedPath);
/*      */     } 
/*      */     
/*  829 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AbstractNestablePropertyAccessor getNestedPropertyAccessor(String nestedProperty) {
/*  842 */     if (this.nestedPropertyAccessors == null) {
/*  843 */       this.nestedPropertyAccessors = new HashMap<String, AbstractNestablePropertyAccessor>();
/*      */     }
/*      */     
/*  846 */     PropertyTokenHolder tokens = getPropertyNameTokens(nestedProperty);
/*  847 */     String canonicalName = tokens.canonicalName;
/*  848 */     Object value = getPropertyValue(tokens);
/*  849 */     if (value == null || (value.getClass() == javaUtilOptionalClass && OptionalUnwrapper.isEmpty(value))) {
/*  850 */       if (isAutoGrowNestedPaths()) {
/*  851 */         value = setDefaultValue(tokens);
/*      */       } else {
/*      */         
/*  854 */         throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + canonicalName);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  859 */     AbstractNestablePropertyAccessor nestedPa = this.nestedPropertyAccessors.get(canonicalName);
/*  860 */     if (nestedPa == null || nestedPa.getWrappedInstance() != (
/*  861 */       (value.getClass() == javaUtilOptionalClass) ? OptionalUnwrapper.unwrap(value) : value)) {
/*  862 */       if (logger.isTraceEnabled()) {
/*  863 */         logger.trace("Creating new nested " + getClass().getSimpleName() + " for property '" + canonicalName + "'");
/*      */       }
/*  865 */       nestedPa = newNestedPropertyAccessor(value, this.nestedPath + canonicalName + ".");
/*      */       
/*  867 */       copyDefaultEditorsTo(nestedPa);
/*  868 */       copyCustomEditorsTo(nestedPa, canonicalName);
/*  869 */       this.nestedPropertyAccessors.put(canonicalName, nestedPa);
/*      */     
/*      */     }
/*  872 */     else if (logger.isTraceEnabled()) {
/*  873 */       logger.trace("Using cached nested property accessor for property '" + canonicalName + "'");
/*      */     } 
/*      */     
/*  876 */     return nestedPa;
/*      */   }
/*      */   
/*      */   private Object setDefaultValue(String propertyName) {
/*  880 */     PropertyTokenHolder tokens = new PropertyTokenHolder();
/*  881 */     tokens.actualName = propertyName;
/*  882 */     tokens.canonicalName = propertyName;
/*  883 */     return setDefaultValue(tokens);
/*      */   }
/*      */   
/*      */   private Object setDefaultValue(PropertyTokenHolder tokens) {
/*  887 */     PropertyValue pv = createDefaultPropertyValue(tokens);
/*  888 */     setPropertyValue(tokens, pv);
/*  889 */     return getPropertyValue(tokens);
/*      */   }
/*      */   
/*      */   private PropertyValue createDefaultPropertyValue(PropertyTokenHolder tokens) {
/*  893 */     TypeDescriptor desc = getPropertyTypeDescriptor(tokens.canonicalName);
/*  894 */     Class<?> type = desc.getType();
/*  895 */     if (type == null) {
/*  896 */       throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + tokens.canonicalName, "Could not determine property type for auto-growing a default value");
/*      */     }
/*      */     
/*  899 */     Object defaultValue = newValue(type, desc, tokens.canonicalName);
/*  900 */     return new PropertyValue(tokens.canonicalName, defaultValue);
/*      */   }
/*      */   
/*      */   private Object newValue(Class<?> type, TypeDescriptor desc, String name) {
/*      */     try {
/*  905 */       if (type.isArray()) {
/*  906 */         Class<?> componentType = type.getComponentType();
/*      */         
/*  908 */         if (componentType.isArray()) {
/*  909 */           Object array = Array.newInstance(componentType, 1);
/*  910 */           Array.set(array, 0, Array.newInstance(componentType.getComponentType(), 0));
/*  911 */           return array;
/*      */         } 
/*      */         
/*  914 */         return Array.newInstance(componentType, 0);
/*      */       } 
/*      */       
/*  917 */       if (Collection.class.isAssignableFrom(type)) {
/*  918 */         TypeDescriptor elementDesc = (desc != null) ? desc.getElementTypeDescriptor() : null;
/*  919 */         return CollectionFactory.createCollection(type, (elementDesc != null) ? elementDesc.getType() : null, 16);
/*      */       } 
/*  921 */       if (Map.class.isAssignableFrom(type)) {
/*  922 */         TypeDescriptor keyDesc = (desc != null) ? desc.getMapKeyTypeDescriptor() : null;
/*  923 */         return CollectionFactory.createMap(type, (keyDesc != null) ? keyDesc.getType() : null, 16);
/*      */       } 
/*      */       
/*  926 */       return BeanUtils.instantiate(type);
/*      */     
/*      */     }
/*  929 */     catch (Throwable ex) {
/*  930 */       throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + name, "Could not instantiate property type [" + type
/*  931 */           .getName() + "] to auto-grow nested property path", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private PropertyTokenHolder getPropertyNameTokens(String propertyName) {
/*  941 */     PropertyTokenHolder tokens = new PropertyTokenHolder();
/*  942 */     String actualName = null;
/*  943 */     List<String> keys = new ArrayList<String>(2);
/*  944 */     int searchIndex = 0;
/*  945 */     while (searchIndex != -1) {
/*  946 */       int keyStart = propertyName.indexOf("[", searchIndex);
/*  947 */       searchIndex = -1;
/*  948 */       if (keyStart != -1) {
/*  949 */         int keyEnd = propertyName.indexOf("]", keyStart + "[".length());
/*  950 */         if (keyEnd != -1) {
/*  951 */           if (actualName == null) {
/*  952 */             actualName = propertyName.substring(0, keyStart);
/*      */           }
/*  954 */           String key = propertyName.substring(keyStart + "[".length(), keyEnd);
/*  955 */           if ((key.length() > 1 && key.startsWith("'") && key.endsWith("'")) || (key
/*  956 */             .startsWith("\"") && key.endsWith("\""))) {
/*  957 */             key = key.substring(1, key.length() - 1);
/*      */           }
/*  959 */           keys.add(key);
/*  960 */           searchIndex = keyEnd + "]".length();
/*      */         } 
/*      */       } 
/*      */     } 
/*  964 */     tokens.actualName = (actualName != null) ? actualName : propertyName;
/*  965 */     tokens.canonicalName = tokens.actualName;
/*  966 */     if (!keys.isEmpty()) {
/*  967 */       tokens
/*  968 */         .canonicalName = tokens.canonicalName + "[" + StringUtils.collectionToDelimitedString(keys, "][") + "]";
/*      */       
/*  970 */       tokens.keys = StringUtils.toStringArray(keys);
/*      */     } 
/*  972 */     return tokens;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  977 */     StringBuilder sb = new StringBuilder(getClass().getName());
/*  978 */     if (this.wrappedObject != null) {
/*  979 */       sb.append(": wrapping object [").append(ObjectUtils.identityToString(this.wrappedObject)).append("]");
/*      */     } else {
/*      */       
/*  982 */       sb.append(": no wrapped object set");
/*      */     } 
/*  984 */     return sb.toString();
/*      */   }
/*      */   
/*      */   protected abstract PropertyHandler getLocalPropertyHandler(String paramString);
/*      */   
/*      */   protected abstract AbstractNestablePropertyAccessor newNestedPropertyAccessor(Object paramObject, String paramString);
/*      */   
/*      */   protected abstract NotWritablePropertyException createNotWritablePropertyException(String paramString);
/*      */   
/*      */   protected static abstract class PropertyHandler {
/*      */     private final Class<?> propertyType;
/*      */     
/*      */     public PropertyHandler(Class<?> propertyType, boolean readable, boolean writable) {
/*  997 */       this.propertyType = propertyType;
/*  998 */       this.readable = readable;
/*  999 */       this.writable = writable;
/*      */     }
/*      */     private final boolean readable; private final boolean writable;
/*      */     public Class<?> getPropertyType() {
/* 1003 */       return this.propertyType;
/*      */     }
/*      */     
/*      */     public boolean isReadable() {
/* 1007 */       return this.readable;
/*      */     }
/*      */     
/*      */     public boolean isWritable() {
/* 1011 */       return this.writable;
/*      */     }
/*      */     
/*      */     public abstract TypeDescriptor toTypeDescriptor();
/*      */     
/*      */     public abstract ResolvableType getResolvableType();
/*      */     
/*      */     public Class<?> getMapKeyType(int nestingLevel) {
/* 1019 */       return getResolvableType().getNested(nestingLevel).asMap().resolveGeneric(new int[] { 0 });
/*      */     }
/*      */     
/*      */     public Class<?> getMapValueType(int nestingLevel) {
/* 1023 */       return getResolvableType().getNested(nestingLevel).asMap().resolveGeneric(new int[] { 1 });
/*      */     }
/*      */     
/*      */     public Class<?> getCollectionType(int nestingLevel) {
/* 1027 */       return getResolvableType().getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract TypeDescriptor nested(int param1Int);
/*      */ 
/*      */     
/*      */     public abstract Object getValue() throws Exception;
/*      */ 
/*      */     
/*      */     public abstract void setValue(Object param1Object1, Object param1Object2) throws Exception;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static class PropertyTokenHolder
/*      */   {
/*      */     public String canonicalName;
/*      */     
/*      */     public String actualName;
/*      */     
/*      */     public String[] keys;
/*      */   }
/*      */ 
/*      */   
/*      */   @UsesJava8
/*      */   private static class OptionalUnwrapper
/*      */   {
/*      */     public static Object unwrap(Object optionalObject) {
/* 1055 */       Optional<?> optional = (Optional)optionalObject;
/* 1056 */       Assert.isTrue(optional.isPresent(), "Optional value must be present");
/* 1057 */       Object result = optional.get();
/* 1058 */       Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
/* 1059 */       return result;
/*      */     }
/*      */     
/*      */     public static boolean isEmpty(Object optionalObject) {
/* 1063 */       return !((Optional)optionalObject).isPresent();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\AbstractNestablePropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */