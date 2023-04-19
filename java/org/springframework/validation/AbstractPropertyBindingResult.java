/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*     */ import org.springframework.beans.PropertyAccessorUtils;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.support.ConvertingPropertyEditorAdapter;
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
/*     */ public abstract class AbstractPropertyBindingResult
/*     */   extends AbstractBindingResult
/*     */ {
/*     */   private transient ConversionService conversionService;
/*     */   
/*     */   protected AbstractPropertyBindingResult(String objectName) {
/*  54 */     super(objectName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initConversion(ConversionService conversionService) {
/*  59 */     Assert.notNull(conversionService, "ConversionService must not be null");
/*  60 */     this.conversionService = conversionService;
/*  61 */     if (getTarget() != null) {
/*  62 */       getPropertyAccessor().setConversionService(conversionService);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyEditorRegistry getPropertyEditorRegistry() {
/*  72 */     return (PropertyEditorRegistry)getPropertyAccessor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String canonicalFieldName(String field) {
/*  81 */     return PropertyAccessorUtils.canonicalPropertyName(field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getFieldType(String field) {
/*  90 */     return getPropertyAccessor().getPropertyType(fixedField(field));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getActualFieldValue(String field) {
/*  99 */     return getPropertyAccessor().getPropertyValue(field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object formatFieldValue(String field, Object value) {
/* 108 */     String fixedField = fixedField(field);
/*     */     
/* 110 */     PropertyEditor customEditor = getCustomEditor(fixedField);
/* 111 */     if (customEditor != null) {
/* 112 */       customEditor.setValue(value);
/* 113 */       String textValue = customEditor.getAsText();
/*     */ 
/*     */       
/* 116 */       if (textValue != null) {
/* 117 */         return textValue;
/*     */       }
/*     */     } 
/* 120 */     if (this.conversionService != null) {
/*     */       
/* 122 */       TypeDescriptor fieldDesc = getPropertyAccessor().getPropertyTypeDescriptor(fixedField);
/* 123 */       TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);
/* 124 */       if (fieldDesc != null && this.conversionService.canConvert(fieldDesc, strDesc)) {
/* 125 */         return this.conversionService.convert(value, fieldDesc, strDesc);
/*     */       }
/*     */     } 
/* 128 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertyEditor getCustomEditor(String fixedField) {
/* 137 */     Class<?> targetType = getPropertyAccessor().getPropertyType(fixedField);
/* 138 */     PropertyEditor editor = getPropertyAccessor().findCustomEditor(targetType, fixedField);
/* 139 */     if (editor == null) {
/* 140 */       editor = BeanUtils.findEditorByConvention(targetType);
/*     */     }
/* 142 */     return editor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyEditor findEditor(String field, Class<?> valueType) {
/*     */     ConvertingPropertyEditorAdapter convertingPropertyEditorAdapter;
/* 151 */     Class<?> valueTypeForLookup = valueType;
/* 152 */     if (valueTypeForLookup == null) {
/* 153 */       valueTypeForLookup = getFieldType(field);
/*     */     }
/* 155 */     PropertyEditor editor = super.findEditor(field, valueTypeForLookup);
/* 156 */     if (editor == null && this.conversionService != null) {
/* 157 */       TypeDescriptor td = null;
/* 158 */       if (field != null) {
/* 159 */         TypeDescriptor ptd = getPropertyAccessor().getPropertyTypeDescriptor(fixedField(field));
/* 160 */         if (valueType == null || valueType.isAssignableFrom(ptd.getType())) {
/* 161 */           td = ptd;
/*     */         }
/*     */       } 
/* 164 */       if (td == null) {
/* 165 */         td = TypeDescriptor.valueOf(valueTypeForLookup);
/*     */       }
/* 167 */       if (this.conversionService.canConvert(TypeDescriptor.valueOf(String.class), td)) {
/* 168 */         convertingPropertyEditorAdapter = new ConvertingPropertyEditorAdapter(this.conversionService, td);
/*     */       }
/*     */     } 
/* 171 */     return (PropertyEditor)convertingPropertyEditorAdapter;
/*     */   }
/*     */   
/*     */   public abstract ConfigurablePropertyAccessor getPropertyAccessor();
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\AbstractPropertyBindingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */