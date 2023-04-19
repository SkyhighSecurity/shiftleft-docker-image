/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.BeanResolver;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.MethodFilter;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.OperatorOverloader;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypeLocator;
/*     */ import org.springframework.expression.TypedValue;
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
/*     */ public class StandardEvaluationContext
/*     */   implements EvaluationContext
/*     */ {
/*     */   private TypedValue rootObject;
/*     */   private List<ConstructorResolver> constructorResolvers;
/*     */   private List<MethodResolver> methodResolvers;
/*     */   private BeanResolver beanResolver;
/*     */   private ReflectiveMethodResolver reflectiveMethodResolver;
/*     */   private List<PropertyAccessor> propertyAccessors;
/*     */   private TypeLocator typeLocator;
/*     */   private TypeConverter typeConverter;
/*  71 */   private TypeComparator typeComparator = new StandardTypeComparator();
/*     */   
/*  73 */   private OperatorOverloader operatorOverloader = new StandardOperatorOverloader();
/*     */   
/*  75 */   private final Map<String, Object> variables = new HashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardEvaluationContext() {
/*  82 */     setRootObject(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardEvaluationContext(Object rootObject) {
/*  91 */     setRootObject(rootObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRootObject(Object rootObject, TypeDescriptor typeDescriptor) {
/*  96 */     this.rootObject = new TypedValue(rootObject, typeDescriptor);
/*     */   }
/*     */   
/*     */   public void setRootObject(Object rootObject) {
/* 100 */     this.rootObject = (rootObject != null) ? new TypedValue(rootObject) : TypedValue.NULL;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getRootObject() {
/* 105 */     return this.rootObject;
/*     */   }
/*     */   
/*     */   public void setPropertyAccessors(List<PropertyAccessor> propertyAccessors) {
/* 109 */     this.propertyAccessors = propertyAccessors;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PropertyAccessor> getPropertyAccessors() {
/* 114 */     ensurePropertyAccessorsInitialized();
/* 115 */     return this.propertyAccessors;
/*     */   }
/*     */   
/*     */   public void addPropertyAccessor(PropertyAccessor accessor) {
/* 119 */     ensurePropertyAccessorsInitialized();
/* 120 */     this.propertyAccessors.add(this.propertyAccessors.size() - 1, accessor);
/*     */   }
/*     */   
/*     */   public boolean removePropertyAccessor(PropertyAccessor accessor) {
/* 124 */     return this.propertyAccessors.remove(accessor);
/*     */   }
/*     */   
/*     */   public void setConstructorResolvers(List<ConstructorResolver> constructorResolvers) {
/* 128 */     this.constructorResolvers = constructorResolvers;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ConstructorResolver> getConstructorResolvers() {
/* 133 */     ensureConstructorResolversInitialized();
/* 134 */     return this.constructorResolvers;
/*     */   }
/*     */   
/*     */   public void addConstructorResolver(ConstructorResolver resolver) {
/* 138 */     ensureConstructorResolversInitialized();
/* 139 */     this.constructorResolvers.add(this.constructorResolvers.size() - 1, resolver);
/*     */   }
/*     */   
/*     */   public boolean removeConstructorResolver(ConstructorResolver resolver) {
/* 143 */     ensureConstructorResolversInitialized();
/* 144 */     return this.constructorResolvers.remove(resolver);
/*     */   }
/*     */   
/*     */   public void setMethodResolvers(List<MethodResolver> methodResolvers) {
/* 148 */     this.methodResolvers = methodResolvers;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MethodResolver> getMethodResolvers() {
/* 153 */     ensureMethodResolversInitialized();
/* 154 */     return this.methodResolvers;
/*     */   }
/*     */   
/*     */   public void addMethodResolver(MethodResolver resolver) {
/* 158 */     ensureMethodResolversInitialized();
/* 159 */     this.methodResolvers.add(this.methodResolvers.size() - 1, resolver);
/*     */   }
/*     */   
/*     */   public boolean removeMethodResolver(MethodResolver methodResolver) {
/* 163 */     ensureMethodResolversInitialized();
/* 164 */     return this.methodResolvers.remove(methodResolver);
/*     */   }
/*     */   
/*     */   public void setBeanResolver(BeanResolver beanResolver) {
/* 168 */     this.beanResolver = beanResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanResolver getBeanResolver() {
/* 173 */     return this.beanResolver;
/*     */   }
/*     */   
/*     */   public void setTypeLocator(TypeLocator typeLocator) {
/* 177 */     Assert.notNull(typeLocator, "TypeLocator must not be null");
/* 178 */     this.typeLocator = typeLocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeLocator getTypeLocator() {
/* 183 */     if (this.typeLocator == null) {
/* 184 */       this.typeLocator = new StandardTypeLocator();
/*     */     }
/* 186 */     return this.typeLocator;
/*     */   }
/*     */   
/*     */   public void setTypeConverter(TypeConverter typeConverter) {
/* 190 */     Assert.notNull(typeConverter, "TypeConverter must not be null");
/* 191 */     this.typeConverter = typeConverter;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeConverter getTypeConverter() {
/* 196 */     if (this.typeConverter == null) {
/* 197 */       this.typeConverter = new StandardTypeConverter();
/*     */     }
/* 199 */     return this.typeConverter;
/*     */   }
/*     */   
/*     */   public void setTypeComparator(TypeComparator typeComparator) {
/* 203 */     Assert.notNull(typeComparator, "TypeComparator must not be null");
/* 204 */     this.typeComparator = typeComparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeComparator getTypeComparator() {
/* 209 */     return this.typeComparator;
/*     */   }
/*     */   
/*     */   public void setOperatorOverloader(OperatorOverloader operatorOverloader) {
/* 213 */     Assert.notNull(operatorOverloader, "OperatorOverloader must not be null");
/* 214 */     this.operatorOverloader = operatorOverloader;
/*     */   }
/*     */ 
/*     */   
/*     */   public OperatorOverloader getOperatorOverloader() {
/* 219 */     return this.operatorOverloader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVariable(String name, Object value) {
/* 224 */     this.variables.put(name, value);
/*     */   }
/*     */   
/*     */   public void setVariables(Map<String, Object> variables) {
/* 228 */     this.variables.putAll(variables);
/*     */   }
/*     */   
/*     */   public void registerFunction(String name, Method method) {
/* 232 */     this.variables.put(name, method);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object lookupVariable(String name) {
/* 237 */     return this.variables.get(name);
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
/*     */   public void registerMethodFilter(Class<?> type, MethodFilter filter) throws IllegalStateException {
/* 250 */     ensureMethodResolversInitialized();
/* 251 */     if (this.reflectiveMethodResolver != null) {
/* 252 */       this.reflectiveMethodResolver.registerMethodFilter(type, filter);
/*     */     } else {
/*     */       
/* 255 */       throw new IllegalStateException("Method filter cannot be set as the reflective method resolver is not in use");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void ensurePropertyAccessorsInitialized() {
/* 261 */     if (this.propertyAccessors == null) {
/* 262 */       initializePropertyAccessors();
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void initializePropertyAccessors() {
/* 267 */     if (this.propertyAccessors == null) {
/* 268 */       List<PropertyAccessor> defaultAccessors = new ArrayList<PropertyAccessor>();
/* 269 */       defaultAccessors.add(new ReflectivePropertyAccessor());
/* 270 */       this.propertyAccessors = defaultAccessors;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void ensureConstructorResolversInitialized() {
/* 275 */     if (this.constructorResolvers == null) {
/* 276 */       initializeConstructorResolvers();
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void initializeConstructorResolvers() {
/* 281 */     if (this.constructorResolvers == null) {
/* 282 */       List<ConstructorResolver> defaultResolvers = new ArrayList<ConstructorResolver>();
/* 283 */       defaultResolvers.add(new ReflectiveConstructorResolver());
/* 284 */       this.constructorResolvers = defaultResolvers;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void ensureMethodResolversInitialized() {
/* 289 */     if (this.methodResolvers == null) {
/* 290 */       initializeMethodResolvers();
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void initializeMethodResolvers() {
/* 295 */     if (this.methodResolvers == null) {
/* 296 */       List<MethodResolver> defaultResolvers = new ArrayList<MethodResolver>();
/* 297 */       this.reflectiveMethodResolver = new ReflectiveMethodResolver();
/* 298 */       defaultResolvers.add(this.reflectiveMethodResolver);
/* 299 */       this.methodResolvers = defaultResolvers;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\support\StandardEvaluationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */