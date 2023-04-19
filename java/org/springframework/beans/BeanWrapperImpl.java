/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.convert.Property;
/*     */ import org.springframework.core.convert.TypeDescriptor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanWrapperImpl
/*     */   extends AbstractNestablePropertyAccessor
/*     */   implements BeanWrapper
/*     */ {
/*     */   private CachedIntrospectionResults cachedIntrospectionResults;
/*     */   private AccessControlContext acc;
/*     */   
/*     */   public BeanWrapperImpl() {
/*  83 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWrapperImpl(boolean registerDefaultEditors) {
/*  93 */     super(registerDefaultEditors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWrapperImpl(Object object) {
/* 101 */     super(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWrapperImpl(Class<?> clazz) {
/* 109 */     super(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWrapperImpl(Object object, String nestedPath, Object rootObject) {
/* 120 */     super(object, nestedPath, rootObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanWrapperImpl(Object object, String nestedPath, BeanWrapperImpl parent) {
/* 131 */     super(object, nestedPath, parent);
/* 132 */     setSecurityContext(parent.acc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanInstance(Object object) {
/* 143 */     this.wrappedObject = object;
/* 144 */     this.rootObject = object;
/* 145 */     this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
/* 146 */     setIntrospectionClass(object.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWrappedInstance(Object object, String nestedPath, Object rootObject) {
/* 151 */     super.setWrappedInstance(object, nestedPath, rootObject);
/* 152 */     setIntrospectionClass(getWrappedClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setIntrospectionClass(Class<?> clazz) {
/* 161 */     if (this.cachedIntrospectionResults != null && this.cachedIntrospectionResults.getBeanClass() != clazz) {
/* 162 */       this.cachedIntrospectionResults = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CachedIntrospectionResults getCachedIntrospectionResults() {
/* 171 */     Assert.state((getWrappedInstance() != null), "BeanWrapper does not hold a bean instance");
/* 172 */     if (this.cachedIntrospectionResults == null) {
/* 173 */       this.cachedIntrospectionResults = CachedIntrospectionResults.forClass(getWrappedClass());
/*     */     }
/* 175 */     return this.cachedIntrospectionResults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSecurityContext(AccessControlContext acc) {
/* 183 */     this.acc = acc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AccessControlContext getSecurityContext() {
/* 191 */     return this.acc;
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
/*     */   public Object convertForProperty(Object value, String propertyName) throws TypeMismatchException {
/* 206 */     CachedIntrospectionResults cachedIntrospectionResults = getCachedIntrospectionResults();
/* 207 */     PropertyDescriptor pd = cachedIntrospectionResults.getPropertyDescriptor(propertyName);
/* 208 */     if (pd == null) {
/* 209 */       throw new InvalidPropertyException(getRootClass(), getNestedPath() + propertyName, "No property '" + propertyName + "' found");
/*     */     }
/*     */     
/* 212 */     TypeDescriptor td = cachedIntrospectionResults.getTypeDescriptor(pd);
/* 213 */     if (td == null) {
/* 214 */       td = cachedIntrospectionResults.addTypeDescriptor(pd, new TypeDescriptor(property(pd)));
/*     */     }
/* 216 */     return convertForProperty(propertyName, (Object)null, value, td);
/*     */   }
/*     */   
/*     */   private Property property(PropertyDescriptor pd) {
/* 220 */     GenericTypeAwarePropertyDescriptor gpd = (GenericTypeAwarePropertyDescriptor)pd;
/* 221 */     return new Property(gpd.getBeanClass(), gpd.getReadMethod(), gpd.getWriteMethod(), gpd.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanPropertyHandler getLocalPropertyHandler(String propertyName) {
/* 226 */     PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(propertyName);
/* 227 */     return (pd != null) ? new BeanPropertyHandler(pd) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanWrapperImpl newNestedPropertyAccessor(Object object, String nestedPath) {
/* 232 */     return new BeanWrapperImpl(object, nestedPath, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected NotWritablePropertyException createNotWritablePropertyException(String propertyName) {
/* 237 */     PropertyMatches matches = PropertyMatches.forProperty(propertyName, getRootClass());
/* 238 */     throw new NotWritablePropertyException(getRootClass(), getNestedPath() + propertyName, matches
/* 239 */         .buildErrorMessage(), matches.getPossibleMatches());
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyDescriptor[] getPropertyDescriptors() {
/* 244 */     return getCachedIntrospectionResults().getPropertyDescriptors();
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException {
/* 249 */     BeanWrapperImpl nestedBw = (BeanWrapperImpl)getPropertyAccessorForPropertyPath(propertyName);
/* 250 */     String finalPath = getFinalPath(nestedBw, propertyName);
/* 251 */     PropertyDescriptor pd = nestedBw.getCachedIntrospectionResults().getPropertyDescriptor(finalPath);
/* 252 */     if (pd == null) {
/* 253 */       throw new InvalidPropertyException(getRootClass(), getNestedPath() + propertyName, "No property '" + propertyName + "' found");
/*     */     }
/*     */     
/* 256 */     return pd;
/*     */   }
/*     */   
/*     */   private class BeanPropertyHandler
/*     */     extends AbstractNestablePropertyAccessor.PropertyHandler
/*     */   {
/*     */     private final PropertyDescriptor pd;
/*     */     
/*     */     public BeanPropertyHandler(PropertyDescriptor pd) {
/* 265 */       super(pd.getPropertyType(), (pd.getReadMethod() != null), (pd.getWriteMethod() != null));
/* 266 */       this.pd = pd;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResolvableType getResolvableType() {
/* 271 */       return ResolvableType.forMethodReturnType(this.pd.getReadMethod());
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeDescriptor toTypeDescriptor() {
/* 276 */       return new TypeDescriptor(BeanWrapperImpl.this.property(this.pd));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeDescriptor nested(int level) {
/* 281 */       return TypeDescriptor.nested(BeanWrapperImpl.this.property(this.pd), level);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValue() throws Exception {
/* 286 */       final Method readMethod = this.pd.getReadMethod();
/* 287 */       if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()) && !readMethod.isAccessible()) {
/* 288 */         if (System.getSecurityManager() != null) {
/* 289 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */               {
/*     */                 public Object run() {
/* 292 */                   readMethod.setAccessible(true);
/* 293 */                   return null;
/*     */                 }
/*     */               });
/*     */         } else {
/*     */           
/* 298 */           readMethod.setAccessible(true);
/*     */         } 
/*     */       }
/* 301 */       if (System.getSecurityManager() != null) {
/*     */         try {
/* 303 */           return AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */               {
/*     */                 public Object run() throws Exception {
/* 306 */                   return readMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), (Object[])null);
/*     */                 }
/* 308 */               }BeanWrapperImpl.this.acc);
/*     */         }
/* 310 */         catch (PrivilegedActionException pae) {
/* 311 */           throw pae.getException();
/*     */         } 
/*     */       }
/*     */       
/* 315 */       return readMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), (Object[])null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(final Object object, Object valueToApply) throws Exception {
/* 323 */       final Method writeMethod = (this.pd instanceof GenericTypeAwarePropertyDescriptor) ? ((GenericTypeAwarePropertyDescriptor)this.pd).getWriteMethodForActualAccess() : this.pd.getWriteMethod();
/* 324 */       if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers()) && !writeMethod.isAccessible()) {
/* 325 */         if (System.getSecurityManager() != null) {
/* 326 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */               {
/*     */                 public Object run() {
/* 329 */                   writeMethod.setAccessible(true);
/* 330 */                   return null;
/*     */                 }
/*     */               });
/*     */         } else {
/*     */           
/* 335 */           writeMethod.setAccessible(true);
/*     */         } 
/*     */       }
/* 338 */       final Object value = valueToApply;
/* 339 */       if (System.getSecurityManager() != null) {
/*     */         try {
/* 341 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */               {
/*     */                 public Object run() throws Exception {
/* 344 */                   writeMethod.invoke(object, new Object[] { this.val$value });
/* 345 */                   return null;
/*     */                 }
/* 347 */               }BeanWrapperImpl.this.acc);
/*     */         }
/* 349 */         catch (PrivilegedActionException ex) {
/* 350 */           throw ex.getException();
/*     */         } 
/*     */       } else {
/*     */         
/* 354 */         writeMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), new Object[] { value });
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\BeanWrapperImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */