/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyAccessException;
/*     */ import org.springframework.beans.PropertyAccessorUtils;
/*     */ import org.springframework.beans.PropertyBatchUpdateException;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.support.FormatterPropertyEditorAdapter;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PatternMatchUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataBinder
/*     */   implements PropertyEditorRegistry, TypeConverter
/*     */ {
/*     */   public static final String DEFAULT_OBJECT_NAME = "target";
/*     */   public static final int DEFAULT_AUTO_GROW_COLLECTION_LIMIT = 256;
/* 127 */   protected static final Log logger = LogFactory.getLog(DataBinder.class);
/*     */   
/* 129 */   private static Class<?> javaUtilOptionalClass = null;
/*     */   private final Object target;
/*     */   
/*     */   static {
/*     */     try {
/* 134 */       javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", DataBinder.class.getClassLoader());
/*     */     }
/* 136 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final String objectName;
/*     */ 
/*     */   
/*     */   private AbstractPropertyBindingResult bindingResult;
/*     */ 
/*     */   
/*     */   private SimpleTypeConverter typeConverter;
/*     */ 
/*     */   
/*     */   private boolean ignoreUnknownFields = true;
/*     */   
/*     */   private boolean ignoreInvalidFields = false;
/*     */   
/*     */   private boolean autoGrowNestedPaths = true;
/*     */   
/* 156 */   private int autoGrowCollectionLimit = 256;
/*     */   
/*     */   private String[] allowedFields;
/*     */   
/*     */   private String[] disallowedFields;
/*     */   
/*     */   private String[] requiredFields;
/*     */   
/*     */   private ConversionService conversionService;
/*     */   
/*     */   private MessageCodesResolver messageCodesResolver;
/*     */   
/* 168 */   private BindingErrorProcessor bindingErrorProcessor = new DefaultBindingErrorProcessor();
/*     */   
/* 170 */   private final List<Validator> validators = new ArrayList<Validator>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBinder(Object target) {
/* 180 */     this(target, "target");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBinder(Object target, String objectName) {
/* 190 */     if (target != null && target.getClass() == javaUtilOptionalClass) {
/* 191 */       this.target = OptionalUnwrapper.unwrap(target);
/*     */     } else {
/*     */       
/* 194 */       this.target = target;
/*     */     } 
/* 196 */     this.objectName = objectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTarget() {
/* 204 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/* 211 */     return this.objectName;
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
/*     */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
/* 225 */     Assert.state((this.bindingResult == null), "DataBinder is already initialized - call setAutoGrowNestedPaths before other configuration methods");
/*     */     
/* 227 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowNestedPaths() {
/* 234 */     return this.autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
/* 245 */     Assert.state((this.bindingResult == null), "DataBinder is already initialized - call setAutoGrowCollectionLimit before other configuration methods");
/*     */     
/* 247 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAutoGrowCollectionLimit() {
/* 254 */     return this.autoGrowCollectionLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initBeanPropertyAccess() {
/* 264 */     Assert.state((this.bindingResult == null), "DataBinder is already initialized - call initBeanPropertyAccess before other configuration methods");
/*     */     
/* 266 */     this.bindingResult = createBeanPropertyBindingResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPropertyBindingResult createBeanPropertyBindingResult() {
/* 276 */     BeanPropertyBindingResult result = new BeanPropertyBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths(), getAutoGrowCollectionLimit());
/*     */     
/* 278 */     if (this.conversionService != null) {
/* 279 */       result.initConversion(this.conversionService);
/*     */     }
/* 281 */     if (this.messageCodesResolver != null) {
/* 282 */       result.setMessageCodesResolver(this.messageCodesResolver);
/*     */     }
/*     */     
/* 285 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initDirectFieldAccess() {
/* 295 */     Assert.state((this.bindingResult == null), "DataBinder is already initialized - call initDirectFieldAccess before other configuration methods");
/*     */     
/* 297 */     this.bindingResult = createDirectFieldBindingResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPropertyBindingResult createDirectFieldBindingResult() {
/* 307 */     DirectFieldBindingResult result = new DirectFieldBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths());
/*     */     
/* 309 */     if (this.conversionService != null) {
/* 310 */       result.initConversion(this.conversionService);
/*     */     }
/* 312 */     if (this.messageCodesResolver != null) {
/* 313 */       result.setMessageCodesResolver(this.messageCodesResolver);
/*     */     }
/*     */     
/* 316 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPropertyBindingResult getInternalBindingResult() {
/* 324 */     if (this.bindingResult == null) {
/* 325 */       initBeanPropertyAccess();
/*     */     }
/* 327 */     return this.bindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurablePropertyAccessor getPropertyAccessor() {
/* 334 */     return getInternalBindingResult().getPropertyAccessor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SimpleTypeConverter getSimpleTypeConverter() {
/* 341 */     if (this.typeConverter == null) {
/* 342 */       this.typeConverter = new SimpleTypeConverter();
/* 343 */       if (this.conversionService != null) {
/* 344 */         this.typeConverter.setConversionService(this.conversionService);
/*     */       }
/*     */     } 
/* 347 */     return this.typeConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertyEditorRegistry getPropertyEditorRegistry() {
/* 354 */     if (getTarget() != null) {
/* 355 */       return (PropertyEditorRegistry)getInternalBindingResult().getPropertyAccessor();
/*     */     }
/*     */     
/* 358 */     return (PropertyEditorRegistry)getSimpleTypeConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeConverter getTypeConverter() {
/* 366 */     if (getTarget() != null) {
/* 367 */       return (TypeConverter)getInternalBindingResult().getPropertyAccessor();
/*     */     }
/*     */     
/* 370 */     return (TypeConverter)getSimpleTypeConverter();
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
/*     */   public BindingResult getBindingResult() {
/* 384 */     return getInternalBindingResult();
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
/*     */   public void setIgnoreUnknownFields(boolean ignoreUnknownFields) {
/* 399 */     this.ignoreUnknownFields = ignoreUnknownFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoreUnknownFields() {
/* 406 */     return this.ignoreUnknownFields;
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
/*     */   public void setIgnoreInvalidFields(boolean ignoreInvalidFields) {
/* 421 */     this.ignoreInvalidFields = ignoreInvalidFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoreInvalidFields() {
/* 428 */     return this.ignoreInvalidFields;
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
/*     */   public void setAllowedFields(String... allowedFields) {
/* 444 */     this.allowedFields = PropertyAccessorUtils.canonicalPropertyNames(allowedFields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAllowedFields() {
/* 452 */     return this.allowedFields;
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
/*     */   public void setDisallowedFields(String... disallowedFields) {
/* 468 */     this.disallowedFields = PropertyAccessorUtils.canonicalPropertyNames(disallowedFields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDisallowedFields() {
/* 476 */     return this.disallowedFields;
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
/*     */   public void setRequiredFields(String... requiredFields) {
/* 490 */     this.requiredFields = PropertyAccessorUtils.canonicalPropertyNames(requiredFields);
/* 491 */     if (logger.isDebugEnabled()) {
/* 492 */       logger.debug("DataBinder requires binding of required fields [" + 
/* 493 */           StringUtils.arrayToCommaDelimitedString((Object[])requiredFields) + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getRequiredFields() {
/* 502 */     return this.requiredFields;
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
/*     */   @Deprecated
/*     */   public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {
/* 516 */     getPropertyAccessor().setExtractOldValueForEditor(extractOldValueForEditor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageCodesResolver(MessageCodesResolver messageCodesResolver) {
/* 527 */     Assert.state((this.messageCodesResolver == null), "DataBinder is already initialized with MessageCodesResolver");
/* 528 */     this.messageCodesResolver = messageCodesResolver;
/* 529 */     if (this.bindingResult != null && messageCodesResolver != null) {
/* 530 */       this.bindingResult.setMessageCodesResolver(messageCodesResolver);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBindingErrorProcessor(BindingErrorProcessor bindingErrorProcessor) {
/* 541 */     Assert.notNull(bindingErrorProcessor, "BindingErrorProcessor must not be null");
/* 542 */     this.bindingErrorProcessor = bindingErrorProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BindingErrorProcessor getBindingErrorProcessor() {
/* 549 */     return this.bindingErrorProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidator(Validator validator) {
/* 558 */     assertValidators(new Validator[] { validator });
/* 559 */     this.validators.clear();
/* 560 */     this.validators.add(validator);
/*     */   }
/*     */   
/*     */   private void assertValidators(Validator... validators) {
/* 564 */     Assert.notNull(validators, "Validators required");
/* 565 */     Object target = getTarget();
/* 566 */     for (Validator validator : validators) {
/* 567 */       if (validator != null && target != null && !validator.supports(target.getClass())) {
/* 568 */         throw new IllegalStateException("Invalid target for Validator [" + validator + "]: " + target);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addValidators(Validator... validators) {
/* 579 */     assertValidators(validators);
/* 580 */     this.validators.addAll(Arrays.asList(validators));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceValidators(Validator... validators) {
/* 589 */     assertValidators(validators);
/* 590 */     this.validators.clear();
/* 591 */     this.validators.addAll(Arrays.asList(validators));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Validator getValidator() {
/* 598 */     return (this.validators.size() > 0) ? this.validators.get(0) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Validator> getValidators() {
/* 605 */     return Collections.unmodifiableList(this.validators);
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
/*     */   public void setConversionService(ConversionService conversionService) {
/* 618 */     Assert.state((this.conversionService == null), "DataBinder is already initialized with ConversionService");
/* 619 */     this.conversionService = conversionService;
/* 620 */     if (this.bindingResult != null && conversionService != null) {
/* 621 */       this.bindingResult.initConversion(conversionService);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConversionService getConversionService() {
/* 629 */     return this.conversionService;
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
/*     */   public void addCustomFormatter(Formatter<?> formatter) {
/* 641 */     FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
/* 642 */     getPropertyEditorRegistry().registerCustomEditor(adapter.getFieldType(), (PropertyEditor)adapter);
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
/*     */   public void addCustomFormatter(Formatter<?> formatter, String... fields) {
/* 655 */     FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
/* 656 */     Class<?> fieldType = adapter.getFieldType();
/* 657 */     if (ObjectUtils.isEmpty((Object[])fields)) {
/* 658 */       getPropertyEditorRegistry().registerCustomEditor(fieldType, (PropertyEditor)adapter);
/*     */     } else {
/*     */       
/* 661 */       for (String field : fields) {
/* 662 */         getPropertyEditorRegistry().registerCustomEditor(fieldType, field, (PropertyEditor)adapter);
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
/*     */   public void addCustomFormatter(Formatter<?> formatter, Class<?>... fieldTypes) {
/* 679 */     FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
/* 680 */     if (ObjectUtils.isEmpty((Object[])fieldTypes)) {
/* 681 */       getPropertyEditorRegistry().registerCustomEditor(adapter.getFieldType(), (PropertyEditor)adapter);
/*     */     } else {
/*     */       
/* 684 */       for (Class<?> fieldType : fieldTypes) {
/* 685 */         getPropertyEditorRegistry().registerCustomEditor(fieldType, (PropertyEditor)adapter);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
/* 692 */     getPropertyEditorRegistry().registerCustomEditor(requiredType, propertyEditor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerCustomEditor(Class<?> requiredType, String field, PropertyEditor propertyEditor) {
/* 697 */     getPropertyEditorRegistry().registerCustomEditor(requiredType, field, propertyEditor);
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath) {
/* 702 */     return getPropertyEditorRegistry().findCustomEditor(requiredType, propertyPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
/* 707 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam) throws TypeMismatchException {
/* 714 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType, methodParam);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field) throws TypeMismatchException {
/* 721 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType, field);
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
/*     */   public void bind(PropertyValues pvs) {
/* 739 */     MutablePropertyValues mpvs = (pvs instanceof MutablePropertyValues) ? (MutablePropertyValues)pvs : new MutablePropertyValues(pvs);
/*     */     
/* 741 */     doBind(mpvs);
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
/*     */   protected void doBind(MutablePropertyValues mpvs) {
/* 754 */     checkAllowedFields(mpvs);
/* 755 */     checkRequiredFields(mpvs);
/* 756 */     applyPropertyValues(mpvs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkAllowedFields(MutablePropertyValues mpvs) {
/* 767 */     PropertyValue[] pvs = mpvs.getPropertyValues();
/* 768 */     for (PropertyValue pv : pvs) {
/* 769 */       String field = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
/* 770 */       if (!isAllowed(field)) {
/* 771 */         mpvs.removePropertyValue(pv);
/* 772 */         getBindingResult().recordSuppressedField(field);
/* 773 */         if (logger.isDebugEnabled()) {
/* 774 */           logger.debug("Field [" + field + "] has been removed from PropertyValues and will not be bound, because it has not been found in the list of allowed fields");
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
/*     */   
/*     */   protected boolean isAllowed(String field) {
/* 796 */     String[] allowed = getAllowedFields();
/* 797 */     String[] disallowed = getDisallowedFields();
/* 798 */     return ((ObjectUtils.isEmpty((Object[])allowed) || PatternMatchUtils.simpleMatch(allowed, field)) && (
/* 799 */       ObjectUtils.isEmpty((Object[])disallowed) || !PatternMatchUtils.simpleMatch(disallowed, field)));
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
/*     */   protected void checkRequiredFields(MutablePropertyValues mpvs) {
/* 811 */     String[] requiredFields = getRequiredFields();
/* 812 */     if (!ObjectUtils.isEmpty((Object[])requiredFields)) {
/* 813 */       Map<String, PropertyValue> propertyValues = new HashMap<String, PropertyValue>();
/* 814 */       PropertyValue[] pvs = mpvs.getPropertyValues();
/* 815 */       for (PropertyValue pv : pvs) {
/* 816 */         String canonicalName = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
/* 817 */         propertyValues.put(canonicalName, pv);
/*     */       } 
/* 819 */       for (String field : requiredFields) {
/* 820 */         PropertyValue pv = propertyValues.get(field);
/* 821 */         boolean empty = (pv == null || pv.getValue() == null);
/* 822 */         if (!empty) {
/* 823 */           if (pv.getValue() instanceof String) {
/* 824 */             empty = !StringUtils.hasText((String)pv.getValue());
/*     */           }
/* 826 */           else if (pv.getValue() instanceof String[]) {
/* 827 */             String[] values = (String[])pv.getValue();
/* 828 */             empty = (values.length == 0 || !StringUtils.hasText(values[0]));
/*     */           } 
/*     */         }
/* 831 */         if (empty) {
/*     */           
/* 833 */           getBindingErrorProcessor().processMissingFieldError(field, getInternalBindingResult());
/*     */ 
/*     */           
/* 836 */           if (pv != null) {
/* 837 */             mpvs.removePropertyValue(pv);
/* 838 */             propertyValues.remove(field);
/*     */           } 
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
/*     */   protected void applyPropertyValues(MutablePropertyValues mpvs) {
/*     */     try {
/* 860 */       getPropertyAccessor().setPropertyValues((PropertyValues)mpvs, isIgnoreUnknownFields(), isIgnoreInvalidFields());
/*     */     }
/* 862 */     catch (PropertyBatchUpdateException ex) {
/*     */       
/* 864 */       for (PropertyAccessException pae : ex.getPropertyAccessExceptions()) {
/* 865 */         getBindingErrorProcessor().processPropertyAccessException(pae, getInternalBindingResult());
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
/*     */   public void validate() {
/* 877 */     for (Validator validator : this.validators) {
/* 878 */       validator.validate(getTarget(), getBindingResult());
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
/*     */   public void validate(Object... validationHints) {
/* 890 */     for (Validator validator : getValidators()) {
/* 891 */       if (!ObjectUtils.isEmpty(validationHints) && validator instanceof SmartValidator) {
/* 892 */         ((SmartValidator)validator).validate(getTarget(), getBindingResult(), validationHints); continue;
/*     */       } 
/* 894 */       if (validator != null) {
/* 895 */         validator.validate(getTarget(), getBindingResult());
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
/*     */   public Map<?, ?> close() throws BindException {
/* 908 */     if (getBindingResult().hasErrors()) {
/* 909 */       throw new BindException(getBindingResult());
/*     */     }
/* 911 */     return getBindingResult().getModel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UsesJava8
/*     */   private static class OptionalUnwrapper
/*     */   {
/*     */     public static Object unwrap(Object optionalObject) {
/* 922 */       Optional<?> optional = (Optional)optionalObject;
/* 923 */       if (!optional.isPresent()) {
/* 924 */         return null;
/*     */       }
/* 926 */       Object result = optional.get();
/* 927 */       Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
/* 928 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\DataBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */