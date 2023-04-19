/*     */ package org.apache.commons.collections.functors;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.apache.commons.collections.Factory;
/*     */ import org.apache.commons.collections.FunctorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstantiateFactory
/*     */   implements Factory, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7732226881069447957L;
/*     */   private final Class iClassToInstantiate;
/*     */   private final Class[] iParamTypes;
/*     */   private final Object[] iArgs;
/*  59 */   private transient Constructor iConstructor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Factory getInstance(Class classToInstantiate, Class[] paramTypes, Object[] args) {
/*  70 */     if (classToInstantiate == null) {
/*  71 */       throw new IllegalArgumentException("Class to instantiate must not be null");
/*     */     }
/*  73 */     if ((paramTypes == null && args != null) || (paramTypes != null && args == null) || (paramTypes != null && args != null && paramTypes.length != args.length))
/*     */     {
/*     */       
/*  76 */       throw new IllegalArgumentException("Parameter types must match the arguments");
/*     */     }
/*     */     
/*  79 */     if (paramTypes == null || paramTypes.length == 0) {
/*  80 */       return new InstantiateFactory(classToInstantiate);
/*     */     }
/*  82 */     paramTypes = (Class[])paramTypes.clone();
/*  83 */     args = (Object[])args.clone();
/*  84 */     return new InstantiateFactory(classToInstantiate, paramTypes, args);
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
/*     */   public InstantiateFactory(Class classToInstantiate) {
/*  96 */     this.iClassToInstantiate = classToInstantiate;
/*  97 */     this.iParamTypes = null;
/*  98 */     this.iArgs = null;
/*  99 */     findConstructor();
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
/*     */   public InstantiateFactory(Class classToInstantiate, Class[] paramTypes, Object[] args) {
/* 112 */     this.iClassToInstantiate = classToInstantiate;
/* 113 */     this.iParamTypes = paramTypes;
/* 114 */     this.iArgs = args;
/* 115 */     findConstructor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findConstructor() {
/*     */     try {
/* 123 */       this.iConstructor = this.iClassToInstantiate.getConstructor(this.iParamTypes);
/*     */     }
/* 125 */     catch (NoSuchMethodException ex) {
/* 126 */       throw new IllegalArgumentException("InstantiateFactory: The constructor must exist and be public ");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object create() {
/* 137 */     if (this.iConstructor == null) {
/* 138 */       findConstructor();
/*     */     }
/*     */     
/*     */     try {
/* 142 */       return this.iConstructor.newInstance(this.iArgs);
/*     */     }
/* 144 */     catch (InstantiationException ex) {
/* 145 */       throw new FunctorException("InstantiateFactory: InstantiationException", ex);
/* 146 */     } catch (IllegalAccessException ex) {
/* 147 */       throw new FunctorException("InstantiateFactory: Constructor must be public", ex);
/* 148 */     } catch (InvocationTargetException ex) {
/* 149 */       throw new FunctorException("InstantiateFactory: Constructor threw an exception", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream os) throws IOException {
/* 158 */     FunctorUtils.checkUnsafeSerialization(InstantiateFactory.class);
/* 159 */     os.defaultWriteObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
/* 167 */     FunctorUtils.checkUnsafeSerialization(InstantiateFactory.class);
/* 168 */     is.defaultReadObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\InstantiateFactory.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */