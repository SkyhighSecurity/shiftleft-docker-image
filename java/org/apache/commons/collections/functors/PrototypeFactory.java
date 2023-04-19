/*     */ package org.apache.commons.collections.functors;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrototypeFactory
/*     */ {
/*     */   static Class class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeCloneFactory;
/*     */   static Class class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeSerializationFactory;
/*     */   
/*     */   public static Factory getInstance(Object prototype) {
/*  69 */     if (prototype == null) {
/*  70 */       return ConstantFactory.NULL_INSTANCE;
/*     */     }
/*     */     try {
/*  73 */       Method method = prototype.getClass().getMethod("clone", (Class[])null);
/*  74 */       return new PrototypeCloneFactory(prototype, method);
/*     */     }
/*  76 */     catch (NoSuchMethodException ex) {
/*     */       try {
/*  78 */         prototype.getClass().getConstructor(new Class[] { prototype.getClass() });
/*  79 */         return new InstantiateFactory(prototype.getClass(), new Class[] { prototype.getClass() }, new Object[] { prototype });
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*  84 */       catch (NoSuchMethodException ex2) {
/*  85 */         if (prototype instanceof Serializable) {
/*  86 */           return new PrototypeSerializationFactory((Serializable)prototype);
/*     */         }
/*     */ 
/*     */         
/*  90 */         throw new IllegalArgumentException("The prototype must be cloneable via a public clone method");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class PrototypeCloneFactory
/*     */     implements Factory, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 5604271422565175555L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Object iPrototype;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private transient Method iCloneMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private PrototypeCloneFactory(Object prototype, Method method) {
/* 121 */       this.iPrototype = prototype;
/* 122 */       this.iCloneMethod = method;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void findCloneMethod() {
/*     */       try {
/* 130 */         this.iCloneMethod = this.iPrototype.getClass().getMethod("clone", (Class[])null);
/*     */       }
/* 132 */       catch (NoSuchMethodException ex) {
/* 133 */         throw new IllegalArgumentException("PrototypeCloneFactory: The clone method must exist and be public ");
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object create() {
/* 144 */       if (this.iCloneMethod == null) {
/* 145 */         findCloneMethod();
/*     */       }
/*     */       
/*     */       try {
/* 149 */         return this.iCloneMethod.invoke(this.iPrototype, (Object[])null);
/*     */       }
/* 151 */       catch (IllegalAccessException ex) {
/* 152 */         throw new FunctorException("PrototypeCloneFactory: Clone method must be public", ex);
/* 153 */       } catch (InvocationTargetException ex) {
/* 154 */         throw new FunctorException("PrototypeCloneFactory: Clone method threw an exception", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void writeObject(ObjectOutputStream os) throws IOException {
/* 163 */       FunctorUtils.checkUnsafeSerialization((PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeCloneFactory == null) ? (PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeCloneFactory = PrototypeFactory.class$("org.apache.commons.collections.functors.PrototypeFactory$PrototypeCloneFactory")) : PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeCloneFactory);
/* 164 */       os.defaultWriteObject();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
/* 172 */       FunctorUtils.checkUnsafeSerialization((PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeCloneFactory == null) ? (PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeCloneFactory = PrototypeFactory.class$("org.apache.commons.collections.functors.PrototypeFactory$PrototypeCloneFactory")) : PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeCloneFactory);
/* 173 */       is.defaultReadObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static Class class$(String x0) {
/*     */     try {
/*     */       return Class.forName(x0);
/*     */     } catch (ClassNotFoundException x1) {
/*     */       throw new NoClassDefFoundError(x1.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static class PrototypeSerializationFactory
/*     */     implements Factory, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -8704966966139178833L;
/*     */     
/*     */     private final Serializable iPrototype;
/*     */     
/*     */     private PrototypeSerializationFactory(Serializable prototype) {
/* 195 */       this.iPrototype = prototype;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object create() {
/* 204 */       ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
/* 205 */       ByteArrayInputStream bais = null;
/*     */       try {
/* 207 */         ObjectOutputStream out = new ObjectOutputStream(baos);
/* 208 */         out.writeObject(this.iPrototype);
/*     */         
/* 210 */         bais = new ByteArrayInputStream(baos.toByteArray());
/* 211 */         ObjectInputStream in = new ObjectInputStream(bais);
/* 212 */         return in.readObject();
/*     */       }
/* 214 */       catch (ClassNotFoundException ex) {
/* 215 */         throw new FunctorException(ex);
/* 216 */       } catch (IOException ex) {
/* 217 */         throw new FunctorException(ex);
/*     */       } finally {
/*     */         try {
/* 220 */           if (bais != null) {
/* 221 */             bais.close();
/*     */           }
/* 223 */         } catch (IOException ex) {}
/*     */ 
/*     */         
/*     */         try {
/* 227 */           if (baos != null) {
/* 228 */             baos.close();
/*     */           }
/* 230 */         } catch (IOException ex) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void writeObject(ObjectOutputStream os) throws IOException {
/* 241 */       FunctorUtils.checkUnsafeSerialization((PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeSerializationFactory == null) ? (PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeSerializationFactory = PrototypeFactory.class$("org.apache.commons.collections.functors.PrototypeFactory$PrototypeSerializationFactory")) : PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeSerializationFactory);
/* 242 */       os.defaultWriteObject();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
/* 250 */       FunctorUtils.checkUnsafeSerialization((PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeSerializationFactory == null) ? (PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeSerializationFactory = PrototypeFactory.class$("org.apache.commons.collections.functors.PrototypeFactory$PrototypeSerializationFactory")) : PrototypeFactory.class$org$apache$commons$collections$functors$PrototypeFactory$PrototypeSerializationFactory);
/* 251 */       is.defaultReadObject();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\PrototypeFactory.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */