/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InlineMap
/*     */   extends SpelNodeImpl
/*     */ {
/*  37 */   private TypedValue constant = null;
/*     */ 
/*     */   
/*     */   public InlineMap(int pos, SpelNodeImpl... args) {
/*  41 */     super(pos, args);
/*  42 */     checkIfConstant();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkIfConstant() {
/*  52 */     boolean isConstant = true;
/*  53 */     for (int c = 0, max = getChildCount(); c < max; c++) {
/*  54 */       SpelNode child = getChild(c);
/*  55 */       if (!(child instanceof Literal)) {
/*  56 */         if (child instanceof InlineList) {
/*  57 */           InlineList inlineList = (InlineList)child;
/*  58 */           if (!inlineList.isConstant()) {
/*  59 */             isConstant = false;
/*     */             
/*     */             break;
/*     */           } 
/*  63 */         } else if (child instanceof InlineMap) {
/*  64 */           InlineMap inlineMap = (InlineMap)child;
/*  65 */           if (!inlineMap.isConstant()) {
/*  66 */             isConstant = false;
/*     */             
/*     */             break;
/*     */           } 
/*  70 */         } else if (c % 2 != 0 || !(child instanceof PropertyOrFieldReference)) {
/*  71 */           isConstant = false;
/*     */           break;
/*     */         } 
/*     */       }
/*     */     } 
/*  76 */     if (isConstant) {
/*  77 */       Map<Object, Object> constantMap = new LinkedHashMap<Object, Object>();
/*  78 */       int childCount = getChildCount();
/*  79 */       for (int i = 0; i < childCount; i++) {
/*  80 */         SpelNode keyChild = getChild(i++);
/*  81 */         SpelNode valueChild = getChild(i);
/*  82 */         Object key = null;
/*  83 */         Object<Object> value = null;
/*  84 */         if (keyChild instanceof Literal) {
/*  85 */           key = ((Literal)keyChild).getLiteralValue().getValue();
/*     */         }
/*  87 */         else if (keyChild instanceof PropertyOrFieldReference) {
/*  88 */           key = ((PropertyOrFieldReference)keyChild).getName();
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */         
/*  93 */         if (valueChild instanceof Literal) {
/*  94 */           value = (Object<Object>)((Literal)valueChild).getLiteralValue().getValue();
/*     */         }
/*  96 */         else if (valueChild instanceof InlineList) {
/*  97 */           value = (Object<Object>)((InlineList)valueChild).getConstantValue();
/*     */         }
/*  99 */         else if (valueChild instanceof InlineMap) {
/* 100 */           value = (Object)((InlineMap)valueChild).getConstantValue();
/*     */         } 
/* 102 */         constantMap.put(key, value);
/*     */       } 
/* 104 */       this.constant = new TypedValue(Collections.unmodifiableMap(constantMap));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState expressionState) throws EvaluationException {
/* 110 */     if (this.constant != null) {
/* 111 */       return this.constant;
/*     */     }
/*     */     
/* 114 */     Map<Object, Object> returnValue = new LinkedHashMap<Object, Object>();
/* 115 */     int childcount = getChildCount();
/* 116 */     for (int c = 0; c < childcount; c++) {
/*     */       
/* 118 */       SpelNode keyChild = getChild(c++);
/* 119 */       Object key = null;
/* 120 */       if (keyChild instanceof PropertyOrFieldReference) {
/* 121 */         PropertyOrFieldReference reference = (PropertyOrFieldReference)keyChild;
/* 122 */         key = reference.getName();
/*     */       } else {
/*     */         
/* 125 */         key = keyChild.getValue(expressionState);
/*     */       } 
/* 127 */       Object value = getChild(c).getValue(expressionState);
/* 128 */       returnValue.put(key, value);
/*     */     } 
/* 130 */     return new TypedValue(returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 136 */     StringBuilder sb = new StringBuilder("{");
/* 137 */     int count = getChildCount();
/* 138 */     for (int c = 0; c < count; c++) {
/* 139 */       if (c > 0) {
/* 140 */         sb.append(",");
/*     */       }
/* 142 */       sb.append(getChild(c++).toStringAST());
/* 143 */       sb.append(":");
/* 144 */       sb.append(getChild(c).toStringAST());
/*     */     } 
/* 146 */     sb.append("}");
/* 147 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/* 154 */     return (this.constant != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Object, Object> getConstantValue() {
/* 159 */     return (Map<Object, Object>)this.constant.getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\InlineMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */