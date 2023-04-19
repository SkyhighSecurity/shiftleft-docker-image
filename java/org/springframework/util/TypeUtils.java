/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.WildcardType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeUtils
/*     */ {
/*     */   public static boolean isAssignable(Type lhsType, Type rhsType) {
/*  43 */     Assert.notNull(lhsType, "Left-hand side type must not be null");
/*  44 */     Assert.notNull(rhsType, "Right-hand side type must not be null");
/*     */ 
/*     */     
/*  47 */     if (lhsType.equals(rhsType) || Object.class == lhsType) {
/*  48 */       return true;
/*     */     }
/*     */     
/*  51 */     if (lhsType instanceof Class) {
/*  52 */       Class<?> lhsClass = (Class)lhsType;
/*     */ 
/*     */       
/*  55 */       if (rhsType instanceof Class) {
/*  56 */         return ClassUtils.isAssignable(lhsClass, (Class)rhsType);
/*     */       }
/*     */       
/*  59 */       if (rhsType instanceof ParameterizedType) {
/*  60 */         Type rhsRaw = ((ParameterizedType)rhsType).getRawType();
/*     */ 
/*     */         
/*  63 */         if (rhsRaw instanceof Class) {
/*  64 */           return ClassUtils.isAssignable(lhsClass, (Class)rhsRaw);
/*     */         }
/*     */       }
/*  67 */       else if (lhsClass.isArray() && rhsType instanceof GenericArrayType) {
/*  68 */         Type rhsComponent = ((GenericArrayType)rhsType).getGenericComponentType();
/*     */         
/*  70 */         return isAssignable(lhsClass.getComponentType(), rhsComponent);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  75 */     if (lhsType instanceof ParameterizedType) {
/*  76 */       if (rhsType instanceof Class) {
/*  77 */         Type lhsRaw = ((ParameterizedType)lhsType).getRawType();
/*     */         
/*  79 */         if (lhsRaw instanceof Class) {
/*  80 */           return ClassUtils.isAssignable((Class)lhsRaw, (Class)rhsType);
/*     */         }
/*     */       }
/*  83 */       else if (rhsType instanceof ParameterizedType) {
/*  84 */         return isAssignable((ParameterizedType)lhsType, (ParameterizedType)rhsType);
/*     */       } 
/*     */     }
/*     */     
/*  88 */     if (lhsType instanceof GenericArrayType) {
/*  89 */       Type lhsComponent = ((GenericArrayType)lhsType).getGenericComponentType();
/*     */       
/*  91 */       if (rhsType instanceof Class) {
/*  92 */         Class<?> rhsClass = (Class)rhsType;
/*     */         
/*  94 */         if (rhsClass.isArray()) {
/*  95 */           return isAssignable(lhsComponent, rhsClass.getComponentType());
/*     */         }
/*     */       }
/*  98 */       else if (rhsType instanceof GenericArrayType) {
/*  99 */         Type rhsComponent = ((GenericArrayType)rhsType).getGenericComponentType();
/*     */         
/* 101 */         return isAssignable(lhsComponent, rhsComponent);
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     if (lhsType instanceof WildcardType) {
/* 106 */       return isAssignable((WildcardType)lhsType, rhsType);
/*     */     }
/*     */     
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isAssignable(ParameterizedType lhsType, ParameterizedType rhsType) {
/* 113 */     if (lhsType.equals(rhsType)) {
/* 114 */       return true;
/*     */     }
/*     */     
/* 117 */     Type[] lhsTypeArguments = lhsType.getActualTypeArguments();
/* 118 */     Type[] rhsTypeArguments = rhsType.getActualTypeArguments();
/*     */     
/* 120 */     if (lhsTypeArguments.length != rhsTypeArguments.length) {
/* 121 */       return false;
/*     */     }
/*     */     
/* 124 */     for (int size = lhsTypeArguments.length, i = 0; i < size; i++) {
/* 125 */       Type lhsArg = lhsTypeArguments[i];
/* 126 */       Type rhsArg = rhsTypeArguments[i];
/*     */       
/* 128 */       if (!lhsArg.equals(rhsArg) && (!(lhsArg instanceof WildcardType) || 
/* 129 */         !isAssignable((WildcardType)lhsArg, rhsArg))) {
/* 130 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 134 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isAssignable(WildcardType lhsType, Type rhsType) {
/* 138 */     Type[] lUpperBounds = lhsType.getUpperBounds();
/*     */ 
/*     */     
/* 141 */     if (lUpperBounds.length == 0) {
/* 142 */       lUpperBounds = new Type[] { Object.class };
/*     */     }
/*     */     
/* 145 */     Type[] lLowerBounds = lhsType.getLowerBounds();
/*     */ 
/*     */     
/* 148 */     if (lLowerBounds.length == 0) {
/* 149 */       lLowerBounds = new Type[] { null };
/*     */     }
/*     */     
/* 152 */     if (rhsType instanceof WildcardType) {
/*     */ 
/*     */ 
/*     */       
/* 156 */       WildcardType rhsWcType = (WildcardType)rhsType;
/* 157 */       Type[] rUpperBounds = rhsWcType.getUpperBounds();
/*     */       
/* 159 */       if (rUpperBounds.length == 0) {
/* 160 */         rUpperBounds = new Type[] { Object.class };
/*     */       }
/*     */       
/* 163 */       Type[] rLowerBounds = rhsWcType.getLowerBounds();
/*     */       
/* 165 */       if (rLowerBounds.length == 0) {
/* 166 */         rLowerBounds = new Type[] { null };
/*     */       }
/*     */       
/* 169 */       for (Type lBound : lUpperBounds) {
/* 170 */         for (Type rBound : rUpperBounds) {
/* 171 */           if (!isAssignableBound(lBound, rBound)) {
/* 172 */             return false;
/*     */           }
/*     */         } 
/*     */         
/* 176 */         for (Type rBound : rLowerBounds) {
/* 177 */           if (!isAssignableBound(lBound, rBound)) {
/* 178 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 183 */       for (Type lBound : lLowerBounds) {
/* 184 */         for (Type rBound : rUpperBounds) {
/* 185 */           if (!isAssignableBound(rBound, lBound)) {
/* 186 */             return false;
/*     */           }
/*     */         } 
/*     */         
/* 190 */         for (Type rBound : rLowerBounds) {
/* 191 */           if (!isAssignableBound(rBound, lBound)) {
/* 192 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 198 */       for (Type lBound : lUpperBounds) {
/* 199 */         if (!isAssignableBound(lBound, rhsType)) {
/* 200 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 204 */       for (Type lBound : lLowerBounds) {
/* 205 */         if (!isAssignableBound(rhsType, lBound)) {
/* 206 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isAssignableBound(Type lhsType, Type rhsType) {
/* 215 */     if (rhsType == null) {
/* 216 */       return true;
/*     */     }
/* 218 */     if (lhsType == null) {
/* 219 */       return false;
/*     */     }
/* 221 */     return isAssignable(lhsType, rhsType);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\TypeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */