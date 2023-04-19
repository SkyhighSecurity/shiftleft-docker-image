/*     */ package org.apache.commons.collections.functors;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.apache.commons.collections.Closure;
/*     */ import org.apache.commons.collections.Predicate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FunctorUtils
/*     */ {
/*     */   static final String UNSAFE_SERIALIZABLE_PROPERTY = "org.apache.commons.collections.enableUnsafeSerialization";
/*     */   
/*     */   static Predicate[] copy(Predicate[] predicates) {
/*  57 */     if (predicates == null) {
/*  58 */       return null;
/*     */     }
/*  60 */     return (Predicate[])predicates.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void validate(Predicate[] predicates) {
/*  69 */     if (predicates == null) {
/*  70 */       throw new IllegalArgumentException("The predicate array must not be null");
/*     */     }
/*  72 */     for (int i = 0; i < predicates.length; i++) {
/*  73 */       if (predicates[i] == null) {
/*  74 */         throw new IllegalArgumentException("The predicate array must not contain a null predicate, index " + i + " was null");
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
/*     */   static Predicate[] validate(Collection predicates) {
/*  86 */     if (predicates == null) {
/*  87 */       throw new IllegalArgumentException("The predicate collection must not be null");
/*     */     }
/*     */     
/*  90 */     Predicate[] preds = new Predicate[predicates.size()];
/*  91 */     int i = 0;
/*  92 */     for (Iterator it = predicates.iterator(); it.hasNext(); ) {
/*  93 */       preds[i] = it.next();
/*  94 */       if (preds[i] == null) {
/*  95 */         throw new IllegalArgumentException("The predicate collection must not contain a null predicate, index " + i + " was null");
/*     */       }
/*  97 */       i++;
/*     */     } 
/*  99 */     return preds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Closure[] copy(Closure[] closures) {
/* 109 */     if (closures == null) {
/* 110 */       return null;
/*     */     }
/* 112 */     return (Closure[])closures.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void validate(Closure[] closures) {
/* 121 */     if (closures == null) {
/* 122 */       throw new IllegalArgumentException("The closure array must not be null");
/*     */     }
/* 124 */     for (int i = 0; i < closures.length; i++) {
/* 125 */       if (closures[i] == null) {
/* 126 */         throw new IllegalArgumentException("The closure array must not contain a null closure, index " + i + " was null");
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
/*     */   static Transformer[] copy(Transformer[] transformers) {
/* 138 */     if (transformers == null) {
/* 139 */       return null;
/*     */     }
/* 141 */     return (Transformer[])transformers.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void validate(Transformer[] transformers) {
/* 150 */     if (transformers == null) {
/* 151 */       throw new IllegalArgumentException("The transformer array must not be null");
/*     */     }
/* 153 */     for (int i = 0; i < transformers.length; i++) {
/* 154 */       if (transformers[i] == null) {
/* 155 */         throw new IllegalArgumentException("The transformer array must not contain a null transformer, index " + i + " was null");
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
/*     */   static void checkUnsafeSerialization(Class clazz) {
/*     */     String str;
/*     */     try {
/* 172 */       str = AccessController.<String>doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Object run() {
/* 175 */               return System.getProperty("org.apache.commons.collections.enableUnsafeSerialization");
/*     */             }
/*     */           });
/* 178 */     } catch (SecurityException ex) {
/* 179 */       str = null;
/*     */     } 
/*     */     
/* 182 */     if (!"true".equalsIgnoreCase(str))
/* 183 */       throw new UnsupportedOperationException("Serialization support for " + clazz.getName() + " is disabled for security reasons. " + "To enable it set system property '" + "org.apache.commons.collections.enableUnsafeSerialization" + "' to 'true', " + "but you must ensure that your application does not de-serialize objects from untrusted sources."); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\FunctorUtils.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */