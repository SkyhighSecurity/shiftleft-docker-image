/*    */ package org.springframework.expression.spel.standard;
/*    */ 
/*    */ import org.springframework.expression.Expression;
/*    */ import org.springframework.expression.ParseException;
/*    */ import org.springframework.expression.ParserContext;
/*    */ import org.springframework.expression.common.TemplateAwareExpressionParser;
/*    */ import org.springframework.expression.spel.SpelParserConfiguration;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpelExpressionParser
/*    */   extends TemplateAwareExpressionParser
/*    */ {
/*    */   private final SpelParserConfiguration configuration;
/*    */   
/*    */   public SpelExpressionParser() {
/* 41 */     this.configuration = new SpelParserConfiguration();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SpelExpressionParser(SpelParserConfiguration configuration) {
/* 49 */     Assert.notNull(configuration, "SpelParserConfiguration must not be null");
/* 50 */     this.configuration = configuration;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpelExpression parseRaw(String expressionString) throws ParseException {
/* 55 */     return doParseExpression(expressionString, (ParserContext)null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SpelExpression doParseExpression(String expressionString, ParserContext context) throws ParseException {
/* 60 */     return (new InternalSpelExpressionParser(this.configuration)).doParseExpression(expressionString, context);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\standard\SpelExpressionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */