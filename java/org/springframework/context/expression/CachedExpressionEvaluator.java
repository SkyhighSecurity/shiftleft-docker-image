/*     */ package org.springframework.context.expression;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CachedExpressionEvaluator
/*     */ {
/*     */   private final SpelExpressionParser parser;
/*  40 */   private final ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CachedExpressionEvaluator(SpelExpressionParser parser) {
/*  47 */     Assert.notNull(parser, "SpelExpressionParser must not be null");
/*  48 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CachedExpressionEvaluator() {
/*  55 */     this(new SpelExpressionParser());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SpelExpressionParser getParser() {
/*  63 */     return this.parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ParameterNameDiscoverer getParameterNameDiscoverer() {
/*  71 */     return this.parameterNameDiscoverer;
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
/*     */   protected Expression getExpression(Map<ExpressionKey, Expression> cache, AnnotatedElementKey elementKey, String expression) {
/*  85 */     ExpressionKey expressionKey = createKey(elementKey, expression);
/*  86 */     Expression expr = cache.get(expressionKey);
/*  87 */     if (expr == null) {
/*  88 */       expr = getParser().parseExpression(expression);
/*  89 */       cache.put(expressionKey, expr);
/*     */     } 
/*  91 */     return expr;
/*     */   }
/*     */   
/*     */   private ExpressionKey createKey(AnnotatedElementKey elementKey, String expression) {
/*  95 */     return new ExpressionKey(elementKey, expression);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class ExpressionKey
/*     */     implements Comparable<ExpressionKey>
/*     */   {
/*     */     private final AnnotatedElementKey element;
/*     */     private final String expression;
/*     */     
/*     */     protected ExpressionKey(AnnotatedElementKey element, String expression) {
/* 106 */       this.element = element;
/* 107 */       this.expression = expression;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 112 */       if (this == other) {
/* 113 */         return true;
/*     */       }
/* 115 */       if (!(other instanceof ExpressionKey)) {
/* 116 */         return false;
/*     */       }
/* 118 */       ExpressionKey otherKey = (ExpressionKey)other;
/* 119 */       return (this.element.equals(otherKey.element) && 
/* 120 */         ObjectUtils.nullSafeEquals(this.expression, otherKey.expression));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 125 */       return this.element.hashCode() + ((this.expression != null) ? (this.expression.hashCode() * 29) : 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 130 */       return this.element + ((this.expression != null) ? (" with expression \"" + this.expression) : "\"");
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(ExpressionKey other) {
/* 135 */       int result = this.element.toString().compareTo(other.element.toString());
/* 136 */       if (result == 0 && this.expression != null) {
/* 137 */         result = this.expression.compareTo(other.expression);
/*     */       }
/* 139 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\expression\CachedExpressionEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */