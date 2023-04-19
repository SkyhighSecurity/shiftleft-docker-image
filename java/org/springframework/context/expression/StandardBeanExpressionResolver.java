/*     */ package org.springframework.context.expression;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanExpressionException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*     */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.ExpressionParser;
/*     */ import org.springframework.expression.ParserContext;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypeLocator;
/*     */ import org.springframework.expression.spel.SpelParserConfiguration;
/*     */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*     */ import org.springframework.expression.spel.support.StandardTypeConverter;
/*     */ import org.springframework.expression.spel.support.StandardTypeLocator;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class StandardBeanExpressionResolver
/*     */   implements BeanExpressionResolver
/*     */ {
/*     */   public static final String DEFAULT_EXPRESSION_PREFIX = "#{";
/*     */   public static final String DEFAULT_EXPRESSION_SUFFIX = "}";
/*  58 */   private String expressionPrefix = "#{";
/*     */   
/*  60 */   private String expressionSuffix = "}";
/*     */   
/*     */   private ExpressionParser expressionParser;
/*     */   
/*  64 */   private final Map<String, Expression> expressionCache = new ConcurrentHashMap<String, Expression>(256);
/*     */   
/*  66 */   private final Map<BeanExpressionContext, StandardEvaluationContext> evaluationCache = new ConcurrentHashMap<BeanExpressionContext, StandardEvaluationContext>(8);
/*     */ 
/*     */   
/*  69 */   private final ParserContext beanExpressionParserContext = new ParserContext()
/*     */     {
/*     */       public boolean isTemplate() {
/*  72 */         return true;
/*     */       }
/*     */       
/*     */       public String getExpressionPrefix() {
/*  76 */         return StandardBeanExpressionResolver.this.expressionPrefix;
/*     */       }
/*     */       
/*     */       public String getExpressionSuffix() {
/*  80 */         return StandardBeanExpressionResolver.this.expressionSuffix;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardBeanExpressionResolver() {
/*  89 */     this.expressionParser = (ExpressionParser)new SpelExpressionParser();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardBeanExpressionResolver(ClassLoader beanClassLoader) {
/*  98 */     this.expressionParser = (ExpressionParser)new SpelExpressionParser(new SpelParserConfiguration(null, beanClassLoader));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpressionPrefix(String expressionPrefix) {
/* 108 */     Assert.hasText(expressionPrefix, "Expression prefix must not be empty");
/* 109 */     this.expressionPrefix = expressionPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpressionSuffix(String expressionSuffix) {
/* 118 */     Assert.hasText(expressionSuffix, "Expression suffix must not be empty");
/* 119 */     this.expressionSuffix = expressionSuffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpressionParser(ExpressionParser expressionParser) {
/* 128 */     Assert.notNull(expressionParser, "ExpressionParser must not be null");
/* 129 */     this.expressionParser = expressionParser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object evaluate(String value, BeanExpressionContext evalContext) throws BeansException {
/* 135 */     if (!StringUtils.hasLength(value)) {
/* 136 */       return value;
/*     */     }
/*     */     try {
/* 139 */       Expression expr = this.expressionCache.get(value);
/* 140 */       if (expr == null) {
/* 141 */         expr = this.expressionParser.parseExpression(value, this.beanExpressionParserContext);
/* 142 */         this.expressionCache.put(value, expr);
/*     */       } 
/* 144 */       StandardEvaluationContext sec = this.evaluationCache.get(evalContext);
/* 145 */       if (sec == null) {
/* 146 */         sec = new StandardEvaluationContext(evalContext);
/* 147 */         sec.addPropertyAccessor(new BeanExpressionContextAccessor());
/* 148 */         sec.addPropertyAccessor(new BeanFactoryAccessor());
/* 149 */         sec.addPropertyAccessor((PropertyAccessor)new MapAccessor());
/* 150 */         sec.addPropertyAccessor(new EnvironmentAccessor());
/* 151 */         sec.setBeanResolver(new BeanFactoryResolver((BeanFactory)evalContext.getBeanFactory()));
/* 152 */         sec.setTypeLocator((TypeLocator)new StandardTypeLocator(evalContext.getBeanFactory().getBeanClassLoader()));
/* 153 */         ConversionService conversionService = evalContext.getBeanFactory().getConversionService();
/* 154 */         if (conversionService != null) {
/* 155 */           sec.setTypeConverter((TypeConverter)new StandardTypeConverter(conversionService));
/*     */         }
/* 157 */         customizeEvaluationContext(sec);
/* 158 */         this.evaluationCache.put(evalContext, sec);
/*     */       } 
/* 160 */       return expr.getValue((EvaluationContext)sec);
/*     */     }
/* 162 */     catch (Throwable ex) {
/* 163 */       throw new BeanExpressionException("Expression parsing failed", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void customizeEvaluationContext(StandardEvaluationContext evalContext) {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\expression\StandardBeanExpressionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */