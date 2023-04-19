/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanDefinitionVisitor
/*     */ {
/*     */   private StringValueResolver valueResolver;
/*     */   
/*     */   public BeanDefinitionVisitor(StringValueResolver valueResolver) {
/*  58 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/*  59 */     this.valueResolver = valueResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanDefinitionVisitor() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitBeanDefinition(BeanDefinition beanDefinition) {
/*  77 */     visitParentName(beanDefinition);
/*  78 */     visitBeanClassName(beanDefinition);
/*  79 */     visitFactoryBeanName(beanDefinition);
/*  80 */     visitFactoryMethodName(beanDefinition);
/*  81 */     visitScope(beanDefinition);
/*  82 */     visitPropertyValues(beanDefinition.getPropertyValues());
/*  83 */     ConstructorArgumentValues cas = beanDefinition.getConstructorArgumentValues();
/*  84 */     visitIndexedArgumentValues(cas.getIndexedArgumentValues());
/*  85 */     visitGenericArgumentValues(cas.getGenericArgumentValues());
/*     */   }
/*     */   
/*     */   protected void visitParentName(BeanDefinition beanDefinition) {
/*  89 */     String parentName = beanDefinition.getParentName();
/*  90 */     if (parentName != null) {
/*  91 */       String resolvedName = resolveStringValue(parentName);
/*  92 */       if (!parentName.equals(resolvedName)) {
/*  93 */         beanDefinition.setParentName(resolvedName);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitBeanClassName(BeanDefinition beanDefinition) {
/*  99 */     String beanClassName = beanDefinition.getBeanClassName();
/* 100 */     if (beanClassName != null) {
/* 101 */       String resolvedName = resolveStringValue(beanClassName);
/* 102 */       if (!beanClassName.equals(resolvedName)) {
/* 103 */         beanDefinition.setBeanClassName(resolvedName);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitFactoryBeanName(BeanDefinition beanDefinition) {
/* 109 */     String factoryBeanName = beanDefinition.getFactoryBeanName();
/* 110 */     if (factoryBeanName != null) {
/* 111 */       String resolvedName = resolveStringValue(factoryBeanName);
/* 112 */       if (!factoryBeanName.equals(resolvedName)) {
/* 113 */         beanDefinition.setFactoryBeanName(resolvedName);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitFactoryMethodName(BeanDefinition beanDefinition) {
/* 119 */     String factoryMethodName = beanDefinition.getFactoryMethodName();
/* 120 */     if (factoryMethodName != null) {
/* 121 */       String resolvedName = resolveStringValue(factoryMethodName);
/* 122 */       if (!factoryMethodName.equals(resolvedName)) {
/* 123 */         beanDefinition.setFactoryMethodName(resolvedName);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitScope(BeanDefinition beanDefinition) {
/* 129 */     String scope = beanDefinition.getScope();
/* 130 */     if (scope != null) {
/* 131 */       String resolvedScope = resolveStringValue(scope);
/* 132 */       if (!scope.equals(resolvedScope)) {
/* 133 */         beanDefinition.setScope(resolvedScope);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitPropertyValues(MutablePropertyValues pvs) {
/* 139 */     PropertyValue[] pvArray = pvs.getPropertyValues();
/* 140 */     for (PropertyValue pv : pvArray) {
/* 141 */       Object newVal = resolveValue(pv.getValue());
/* 142 */       if (!ObjectUtils.nullSafeEquals(newVal, pv.getValue())) {
/* 143 */         pvs.add(pv.getName(), newVal);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitIndexedArgumentValues(Map<Integer, ConstructorArgumentValues.ValueHolder> ias) {
/* 149 */     for (ConstructorArgumentValues.ValueHolder valueHolder : ias.values()) {
/* 150 */       Object newVal = resolveValue(valueHolder.getValue());
/* 151 */       if (!ObjectUtils.nullSafeEquals(newVal, valueHolder.getValue())) {
/* 152 */         valueHolder.setValue(newVal);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitGenericArgumentValues(List<ConstructorArgumentValues.ValueHolder> gas) {
/* 158 */     for (ConstructorArgumentValues.ValueHolder valueHolder : gas) {
/* 159 */       Object newVal = resolveValue(valueHolder.getValue());
/* 160 */       if (!ObjectUtils.nullSafeEquals(newVal, valueHolder.getValue())) {
/* 161 */         valueHolder.setValue(newVal);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object resolveValue(Object value) {
/* 168 */     if (value instanceof BeanDefinition) {
/* 169 */       visitBeanDefinition((BeanDefinition)value);
/*     */     }
/* 171 */     else if (value instanceof BeanDefinitionHolder) {
/* 172 */       visitBeanDefinition(((BeanDefinitionHolder)value).getBeanDefinition());
/*     */     }
/* 174 */     else if (value instanceof RuntimeBeanReference) {
/* 175 */       RuntimeBeanReference ref = (RuntimeBeanReference)value;
/* 176 */       String newBeanName = resolveStringValue(ref.getBeanName());
/* 177 */       if (!newBeanName.equals(ref.getBeanName())) {
/* 178 */         return new RuntimeBeanReference(newBeanName);
/*     */       }
/*     */     }
/* 181 */     else if (value instanceof RuntimeBeanNameReference) {
/* 182 */       RuntimeBeanNameReference ref = (RuntimeBeanNameReference)value;
/* 183 */       String newBeanName = resolveStringValue(ref.getBeanName());
/* 184 */       if (!newBeanName.equals(ref.getBeanName())) {
/* 185 */         return new RuntimeBeanNameReference(newBeanName);
/*     */       }
/*     */     }
/* 188 */     else if (value instanceof Object[]) {
/* 189 */       visitArray((Object[])value);
/*     */     }
/* 191 */     else if (value instanceof List) {
/* 192 */       visitList((List)value);
/*     */     }
/* 194 */     else if (value instanceof Set) {
/* 195 */       visitSet((Set)value);
/*     */     }
/* 197 */     else if (value instanceof Map) {
/* 198 */       visitMap((Map<?, ?>)value);
/*     */     }
/* 200 */     else if (value instanceof TypedStringValue) {
/* 201 */       TypedStringValue typedStringValue = (TypedStringValue)value;
/* 202 */       String stringValue = typedStringValue.getValue();
/* 203 */       if (stringValue != null) {
/* 204 */         String visitedString = resolveStringValue(stringValue);
/* 205 */         typedStringValue.setValue(visitedString);
/*     */       }
/*     */     
/* 208 */     } else if (value instanceof String) {
/* 209 */       return resolveStringValue((String)value);
/*     */     } 
/* 211 */     return value;
/*     */   }
/*     */   
/*     */   protected void visitArray(Object[] arrayVal) {
/* 215 */     for (int i = 0; i < arrayVal.length; i++) {
/* 216 */       Object elem = arrayVal[i];
/* 217 */       Object newVal = resolveValue(elem);
/* 218 */       if (!ObjectUtils.nullSafeEquals(newVal, elem)) {
/* 219 */         arrayVal[i] = newVal;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitList(List<Object> listVal) {
/* 226 */     for (int i = 0; i < listVal.size(); i++) {
/* 227 */       Object elem = listVal.get(i);
/* 228 */       Object newVal = resolveValue(elem);
/* 229 */       if (!ObjectUtils.nullSafeEquals(newVal, elem)) {
/* 230 */         listVal.set(i, newVal);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitSet(Set<Object> setVal) {
/* 237 */     Set<Object> newContent = new LinkedHashSet();
/* 238 */     boolean entriesModified = false;
/* 239 */     for (Object elem : setVal) {
/* 240 */       int elemHash = (elem != null) ? elem.hashCode() : 0;
/* 241 */       Object newVal = resolveValue(elem);
/* 242 */       int newValHash = (newVal != null) ? newVal.hashCode() : 0;
/* 243 */       newContent.add(newVal);
/* 244 */       entriesModified = (entriesModified || newVal != elem || newValHash != elemHash);
/*     */     } 
/* 246 */     if (entriesModified) {
/* 247 */       setVal.clear();
/* 248 */       setVal.addAll(newContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitMap(Map<?, ?> mapVal) {
/* 254 */     Map<Object, Object> newContent = new LinkedHashMap<Object, Object>();
/* 255 */     boolean entriesModified = false;
/* 256 */     for (Map.Entry<?, ?> entry : mapVal.entrySet()) {
/* 257 */       Object key = entry.getKey();
/* 258 */       int keyHash = (key != null) ? key.hashCode() : 0;
/* 259 */       Object newKey = resolveValue(key);
/* 260 */       int newKeyHash = (newKey != null) ? newKey.hashCode() : 0;
/* 261 */       Object val = entry.getValue();
/* 262 */       Object newVal = resolveValue(val);
/* 263 */       newContent.put(newKey, newVal);
/* 264 */       entriesModified = (entriesModified || newVal != val || newKey != key || newKeyHash != keyHash);
/*     */     } 
/* 266 */     if (entriesModified) {
/* 267 */       mapVal.clear();
/* 268 */       mapVal.putAll(newContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveStringValue(String strVal) {
/* 278 */     if (this.valueResolver == null) {
/* 279 */       throw new IllegalStateException("No StringValueResolver specified - pass a resolver object into the constructor or override the 'resolveStringValue' method");
/*     */     }
/*     */     
/* 282 */     String resolvedValue = this.valueResolver.resolveStringValue(strVal);
/*     */     
/* 284 */     return strVal.equals(resolvedValue) ? strVal : resolvedValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\BeanDefinitionVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */