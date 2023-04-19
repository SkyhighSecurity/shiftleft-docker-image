/*     */ package org.apache.commons.collections.functors;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.apache.commons.collections.FunctorException;
/*     */ import org.apache.commons.collections.Transformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstantiateTransformer
/*     */   implements Transformer, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3786388740793356347L;
/*  53 */   public static final Transformer NO_ARG_INSTANCE = new InstantiateTransformer();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Class[] iParamTypes;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object[] iArgs;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Transformer getInstance(Class[] paramTypes, Object[] args) {
/*  68 */     if ((paramTypes == null && args != null) || (paramTypes != null && args == null) || (paramTypes != null && args != null && paramTypes.length != args.length))
/*     */     {
/*     */       
/*  71 */       throw new IllegalArgumentException("Parameter types must match the arguments");
/*     */     }
/*     */     
/*  74 */     if (paramTypes == null || paramTypes.length == 0) {
/*  75 */       return NO_ARG_INSTANCE;
/*     */     }
/*  77 */     paramTypes = (Class[])paramTypes.clone();
/*  78 */     args = (Object[])args.clone();
/*     */     
/*  80 */     return new InstantiateTransformer(paramTypes, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InstantiateTransformer() {
/*  88 */     this.iParamTypes = null;
/*  89 */     this.iArgs = null;
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
/*     */   public InstantiateTransformer(Class[] paramTypes, Object[] args) {
/* 101 */     this.iParamTypes = paramTypes;
/* 102 */     this.iArgs = args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object transform(Object input) {
/*     */     try {
/* 113 */       if (!(input instanceof Class)) {
/* 114 */         throw new FunctorException("InstantiateTransformer: Input object was not an instanceof Class, it was a " + ((input == null) ? "null object" : input.getClass().getName()));
/*     */       }
/*     */ 
/*     */       
/* 118 */       Constructor con = ((Class)input).getConstructor(this.iParamTypes);
/* 119 */       return con.newInstance(this.iArgs);
/*     */     }
/* 121 */     catch (NoSuchMethodException ex) {
/* 122 */       throw new FunctorException("InstantiateTransformer: The constructor must exist and be public ");
/* 123 */     } catch (InstantiationException ex) {
/* 124 */       throw new FunctorException("InstantiateTransformer: InstantiationException", ex);
/* 125 */     } catch (IllegalAccessException ex) {
/* 126 */       throw new FunctorException("InstantiateTransformer: Constructor must be public", ex);
/* 127 */     } catch (InvocationTargetException ex) {
/* 128 */       throw new FunctorException("InstantiateTransformer: Constructor threw an exception", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream os) throws IOException {
/* 137 */     FunctorUtils.checkUnsafeSerialization(InstantiateTransformer.class);
/* 138 */     os.defaultWriteObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
/* 146 */     FunctorUtils.checkUnsafeSerialization(InstantiateTransformer.class);
/* 147 */     is.defaultReadObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\InstantiateTransformer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */