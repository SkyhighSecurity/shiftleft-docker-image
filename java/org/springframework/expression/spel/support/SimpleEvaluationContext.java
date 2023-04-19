/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.BeanResolver;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.OperatorOverloader;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypeLocator;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleEvaluationContext
/*     */   implements EvaluationContext
/*     */ {
/*  91 */   private static final TypeLocator typeNotFoundTypeLocator = new TypeLocator()
/*     */     {
/*     */       public Class<?> findType(String typeName) throws EvaluationException {
/*  94 */         throw new SpelEvaluationException(SpelMessage.TYPE_NOT_FOUND, new Object[] { typeName });
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private final TypedValue rootObject;
/*     */   
/*     */   private final List<PropertyAccessor> propertyAccessors;
/*     */   
/*     */   private final List<MethodResolver> methodResolvers;
/*     */   
/*     */   private final TypeConverter typeConverter;
/*     */   
/* 107 */   private final TypeComparator typeComparator = new StandardTypeComparator();
/*     */   
/* 109 */   private final OperatorOverloader operatorOverloader = new StandardOperatorOverloader();
/*     */   
/* 111 */   private final Map<String, Object> variables = new HashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SimpleEvaluationContext(List<PropertyAccessor> accessors, List<MethodResolver> resolvers, TypeConverter converter, TypedValue rootObject) {
/* 117 */     this.propertyAccessors = accessors;
/* 118 */     this.methodResolvers = resolvers;
/* 119 */     this.typeConverter = (converter != null) ? converter : new StandardTypeConverter();
/* 120 */     this.rootObject = (rootObject != null) ? rootObject : TypedValue.NULL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getRootObject() {
/* 129 */     return this.rootObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<PropertyAccessor> getPropertyAccessors() {
/* 138 */     return this.propertyAccessors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ConstructorResolver> getConstructorResolvers() {
/* 147 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MethodResolver> getMethodResolvers() {
/* 156 */     return this.methodResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanResolver getBeanResolver() {
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeLocator getTypeLocator() {
/* 175 */     return typeNotFoundTypeLocator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConverter getTypeConverter() {
/* 186 */     return this.typeConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeComparator getTypeComparator() {
/* 194 */     return this.typeComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OperatorOverloader getOperatorOverloader() {
/* 202 */     return this.operatorOverloader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVariable(String name, Object value) {
/* 207 */     this.variables.put(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object lookupVariable(String name) {
/* 212 */     return this.variables.get(name);
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
/*     */   public static Builder forPropertyAccessors(PropertyAccessor... accessors) {
/* 226 */     for (PropertyAccessor accessor : accessors) {
/* 227 */       if (accessor.getClass() == ReflectivePropertyAccessor.class) {
/* 228 */         throw new IllegalArgumentException("SimpleEvaluationContext is not designed for use with a plain ReflectivePropertyAccessor. Consider using DataBindingPropertyAccessor or a custom subclass.");
/*     */       }
/*     */     } 
/*     */     
/* 232 */     return new Builder(accessors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder forReadOnlyDataBinding() {
/* 242 */     return new Builder(new PropertyAccessor[] { DataBindingPropertyAccessor.forReadOnlyAccess() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder forReadWriteDataBinding() {
/* 252 */     return new Builder(new PropertyAccessor[] { DataBindingPropertyAccessor.forReadWriteAccess() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final List<PropertyAccessor> accessors;
/*     */ 
/*     */     
/* 263 */     private List<MethodResolver> resolvers = Collections.emptyList();
/*     */     
/*     */     private TypeConverter typeConverter;
/*     */     
/*     */     private TypedValue rootObject;
/*     */     
/*     */     public Builder(PropertyAccessor... accessors) {
/* 270 */       this.accessors = Arrays.asList(accessors);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withMethodResolvers(MethodResolver... resolvers) {
/* 281 */       for (MethodResolver resolver : resolvers) {
/* 282 */         if (resolver.getClass() == ReflectiveMethodResolver.class) {
/* 283 */           throw new IllegalArgumentException("SimpleEvaluationContext is not designed for use with a plain ReflectiveMethodResolver. Consider using DataBindingMethodResolver or a custom subclass.");
/*     */         }
/*     */       } 
/*     */       
/* 287 */       this.resolvers = Arrays.asList(resolvers);
/* 288 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withInstanceMethods() {
/* 300 */       this.resolvers = Collections.singletonList(
/* 301 */           DataBindingMethodResolver.forInstanceMethodInvocation());
/* 302 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withConversionService(ConversionService conversionService) {
/* 314 */       this.typeConverter = new StandardTypeConverter(conversionService);
/* 315 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withTypeConverter(TypeConverter converter) {
/* 325 */       this.typeConverter = converter;
/* 326 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withRootObject(Object rootObject) {
/* 336 */       this.rootObject = new TypedValue(rootObject);
/* 337 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withTypedRootObject(Object rootObject, TypeDescriptor typeDescriptor) {
/* 347 */       this.rootObject = new TypedValue(rootObject, typeDescriptor);
/* 348 */       return this;
/*     */     }
/*     */     
/*     */     public SimpleEvaluationContext build() {
/* 352 */       return new SimpleEvaluationContext(this.accessors, this.resolvers, this.typeConverter, this.rootObject);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\SimpleEvaluationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */