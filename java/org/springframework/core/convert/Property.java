/*     */ package org.springframework.core.convert;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ public final class Property
/*     */ {
/*  50 */   private static Map<Property, Annotation[]> annotationCache = (Map<Property, Annotation[]>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */   
/*     */   private final Class<?> objectType;
/*     */   
/*     */   private final Method readMethod;
/*     */   
/*     */   private final Method writeMethod;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final MethodParameter methodParameter;
/*     */   
/*     */   private Annotation[] annotations;
/*     */ 
/*     */   
/*     */   public Property(Class<?> objectType, Method readMethod, Method writeMethod) {
/*  67 */     this(objectType, readMethod, writeMethod, null);
/*     */   }
/*     */   
/*     */   public Property(Class<?> objectType, Method readMethod, Method writeMethod, String name) {
/*  71 */     this.objectType = objectType;
/*  72 */     this.readMethod = readMethod;
/*  73 */     this.writeMethod = writeMethod;
/*  74 */     this.methodParameter = resolveMethodParameter();
/*  75 */     this.name = (name != null) ? name : resolveName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/*  83 */     return this.objectType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  90 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getType() {
/*  97 */     return this.methodParameter.getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Method getReadMethod() {
/* 104 */     return this.readMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Method getWriteMethod() {
/* 111 */     return this.writeMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MethodParameter getMethodParameter() {
/* 118 */     return this.methodParameter;
/*     */   }
/*     */   
/*     */   Annotation[] getAnnotations() {
/* 122 */     if (this.annotations == null) {
/* 123 */       this.annotations = resolveAnnotations();
/*     */     }
/* 125 */     return this.annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String resolveName() {
/* 132 */     if (this.readMethod != null) {
/* 133 */       int i = this.readMethod.getName().indexOf("get");
/* 134 */       if (i != -1) {
/* 135 */         i += 3;
/*     */       } else {
/*     */         
/* 138 */         i = this.readMethod.getName().indexOf("is");
/* 139 */         if (i == -1) {
/* 140 */           throw new IllegalArgumentException("Not a getter method");
/*     */         }
/* 142 */         i += 2;
/*     */       } 
/* 144 */       return StringUtils.uncapitalize(this.readMethod.getName().substring(i));
/*     */     } 
/*     */     
/* 147 */     int index = this.writeMethod.getName().indexOf("set");
/* 148 */     if (index == -1) {
/* 149 */       throw new IllegalArgumentException("Not a setter method");
/*     */     }
/* 151 */     index += 3;
/* 152 */     return StringUtils.uncapitalize(this.writeMethod.getName().substring(index));
/*     */   }
/*     */ 
/*     */   
/*     */   private MethodParameter resolveMethodParameter() {
/* 157 */     MethodParameter read = resolveReadMethodParameter();
/* 158 */     MethodParameter write = resolveWriteMethodParameter();
/* 159 */     if (write == null) {
/* 160 */       if (read == null) {
/* 161 */         throw new IllegalStateException("Property is neither readable nor writeable");
/*     */       }
/* 163 */       return read;
/*     */     } 
/* 165 */     if (read != null) {
/* 166 */       Class<?> readType = read.getParameterType();
/* 167 */       Class<?> writeType = write.getParameterType();
/* 168 */       if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
/* 169 */         return read;
/*     */       }
/*     */     } 
/* 172 */     return write;
/*     */   }
/*     */   
/*     */   private MethodParameter resolveReadMethodParameter() {
/* 176 */     if (getReadMethod() == null) {
/* 177 */       return null;
/*     */     }
/* 179 */     return resolveParameterType(new MethodParameter(getReadMethod(), -1));
/*     */   }
/*     */   
/*     */   private MethodParameter resolveWriteMethodParameter() {
/* 183 */     if (getWriteMethod() == null) {
/* 184 */       return null;
/*     */     }
/* 186 */     return resolveParameterType(new MethodParameter(getWriteMethod(), 0));
/*     */   }
/*     */ 
/*     */   
/*     */   private MethodParameter resolveParameterType(MethodParameter parameter) {
/* 191 */     GenericTypeResolver.resolveParameterType(parameter, getObjectType());
/* 192 */     return parameter;
/*     */   }
/*     */   
/*     */   private Annotation[] resolveAnnotations() {
/* 196 */     Annotation[] annotations = annotationCache.get(this);
/* 197 */     if (annotations == null) {
/* 198 */       Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<Class<? extends Annotation>, Annotation>();
/*     */       
/* 200 */       addAnnotationsToMap(annotationMap, getReadMethod());
/* 201 */       addAnnotationsToMap(annotationMap, getWriteMethod());
/* 202 */       addAnnotationsToMap(annotationMap, getField());
/* 203 */       annotations = (Annotation[])annotationMap.values().toArray((Object[])new Annotation[annotationMap.size()]);
/* 204 */       annotationCache.put(this, annotations);
/*     */     } 
/* 206 */     return annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAnnotationsToMap(Map<Class<? extends Annotation>, Annotation> annotationMap, AnnotatedElement object) {
/* 212 */     if (object != null) {
/* 213 */       for (Annotation annotation : object.getAnnotations()) {
/* 214 */         annotationMap.put(annotation.annotationType(), annotation);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Field getField() {
/* 220 */     String name = getName();
/* 221 */     if (!StringUtils.hasLength(name)) {
/* 222 */       return null;
/*     */     }
/* 224 */     Class<?> declaringClass = declaringClass();
/* 225 */     Field field = ReflectionUtils.findField(declaringClass, name);
/* 226 */     if (field == null) {
/*     */       
/* 228 */       field = ReflectionUtils.findField(declaringClass, StringUtils.uncapitalize(name));
/* 229 */       if (field == null) {
/* 230 */         field = ReflectionUtils.findField(declaringClass, StringUtils.capitalize(name));
/*     */       }
/*     */     } 
/* 233 */     return field;
/*     */   }
/*     */   
/*     */   private Class<?> declaringClass() {
/* 237 */     if (getReadMethod() != null) {
/* 238 */       return getReadMethod().getDeclaringClass();
/*     */     }
/*     */     
/* 241 */     return getWriteMethod().getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 248 */     if (this == other) {
/* 249 */       return true;
/*     */     }
/* 251 */     if (!(other instanceof Property)) {
/* 252 */       return false;
/*     */     }
/* 254 */     Property otherProperty = (Property)other;
/* 255 */     return (ObjectUtils.nullSafeEquals(this.objectType, otherProperty.objectType) && 
/* 256 */       ObjectUtils.nullSafeEquals(this.name, otherProperty.name) && 
/* 257 */       ObjectUtils.nullSafeEquals(this.readMethod, otherProperty.readMethod) && 
/* 258 */       ObjectUtils.nullSafeEquals(this.writeMethod, otherProperty.writeMethod));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 263 */     return ObjectUtils.nullSafeHashCode(this.objectType) * 31 + ObjectUtils.nullSafeHashCode(this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\Property.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */