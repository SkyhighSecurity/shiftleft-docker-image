/*     */ package org.springframework.expression.common;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.ExpressionParser;
/*     */ import org.springframework.expression.ParseException;
/*     */ import org.springframework.expression.ParserContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TemplateAwareExpressionParser
/*     */   implements ExpressionParser
/*     */ {
/*  42 */   private static final ParserContext NON_TEMPLATE_PARSER_CONTEXT = new ParserContext()
/*     */     {
/*     */       public String getExpressionPrefix() {
/*  45 */         return null;
/*     */       }
/*     */       
/*     */       public String getExpressionSuffix() {
/*  49 */         return null;
/*     */       }
/*     */       
/*     */       public boolean isTemplate() {
/*  53 */         return false;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression parseExpression(String expressionString) throws ParseException {
/*  60 */     return parseExpression(expressionString, NON_TEMPLATE_PARSER_CONTEXT);
/*     */   }
/*     */ 
/*     */   
/*     */   public Expression parseExpression(String expressionString, ParserContext context) throws ParseException {
/*  65 */     if (context == null) {
/*  66 */       context = NON_TEMPLATE_PARSER_CONTEXT;
/*     */     }
/*     */     
/*  69 */     if (context.isTemplate()) {
/*  70 */       return parseTemplate(expressionString, context);
/*     */     }
/*     */     
/*  73 */     return doParseExpression(expressionString, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression parseTemplate(String expressionString, ParserContext context) throws ParseException {
/*  79 */     if (expressionString.isEmpty()) {
/*  80 */       return new LiteralExpression("");
/*     */     }
/*     */     
/*  83 */     Expression[] expressions = parseExpressions(expressionString, context);
/*  84 */     if (expressions.length == 1) {
/*  85 */       return expressions[0];
/*     */     }
/*     */     
/*  88 */     return new CompositeStringExpression(expressionString, expressions);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression[] parseExpressions(String expressionString, ParserContext context) throws ParseException {
/* 111 */     List<Expression> expressions = new LinkedList<Expression>();
/* 112 */     String prefix = context.getExpressionPrefix();
/* 113 */     String suffix = context.getExpressionSuffix();
/* 114 */     int startIdx = 0;
/*     */     
/* 116 */     while (startIdx < expressionString.length()) {
/* 117 */       int prefixIndex = expressionString.indexOf(prefix, startIdx);
/* 118 */       if (prefixIndex >= startIdx) {
/*     */         
/* 120 */         if (prefixIndex > startIdx) {
/* 121 */           expressions.add(new LiteralExpression(expressionString.substring(startIdx, prefixIndex)));
/*     */         }
/* 123 */         int afterPrefixIndex = prefixIndex + prefix.length();
/* 124 */         int suffixIndex = skipToCorrectEndSuffix(suffix, expressionString, afterPrefixIndex);
/* 125 */         if (suffixIndex == -1) {
/* 126 */           throw new ParseException(expressionString, prefixIndex, "No ending suffix '" + suffix + "' for expression starting at character " + prefixIndex + ": " + expressionString
/*     */               
/* 128 */               .substring(prefixIndex));
/*     */         }
/* 130 */         if (suffixIndex == afterPrefixIndex) {
/* 131 */           throw new ParseException(expressionString, prefixIndex, "No expression defined within delimiter '" + prefix + suffix + "' at character " + prefixIndex);
/*     */         }
/*     */ 
/*     */         
/* 135 */         String expr = expressionString.substring(prefixIndex + prefix.length(), suffixIndex);
/* 136 */         expr = expr.trim();
/* 137 */         if (expr.isEmpty()) {
/* 138 */           throw new ParseException(expressionString, prefixIndex, "No expression defined within delimiter '" + prefix + suffix + "' at character " + prefixIndex);
/*     */         }
/*     */ 
/*     */         
/* 142 */         expressions.add(doParseExpression(expr, context));
/* 143 */         startIdx = suffixIndex + suffix.length();
/*     */         
/*     */         continue;
/*     */       } 
/* 147 */       expressions.add(new LiteralExpression(expressionString.substring(startIdx)));
/* 148 */       startIdx = expressionString.length();
/*     */     } 
/*     */ 
/*     */     
/* 152 */     return expressions.<Expression>toArray(new Expression[expressions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSuffixHere(String expressionString, int pos, String suffix) {
/* 163 */     int suffixPosition = 0;
/* 164 */     for (int i = 0; i < suffix.length() && pos < expressionString.length(); i++) {
/* 165 */       if (expressionString.charAt(pos++) != suffix.charAt(suffixPosition++)) {
/* 166 */         return false;
/*     */       }
/*     */     } 
/* 169 */     if (suffixPosition != suffix.length())
/*     */     {
/* 171 */       return false;
/*     */     }
/* 173 */     return true;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private int skipToCorrectEndSuffix(String suffix, String expressionString, int afterPrefixIndex) throws ParseException {
/* 191 */     int pos = afterPrefixIndex;
/* 192 */     int maxlen = expressionString.length();
/* 193 */     int nextSuffix = expressionString.indexOf(suffix, afterPrefixIndex);
/* 194 */     if (nextSuffix == -1) {
/* 195 */       return -1;
/*     */     }
/* 197 */     Stack<Bracket> stack = new Stack<Bracket>();
/* 198 */     while (pos < maxlen && (
/* 199 */       !isSuffixHere(expressionString, pos, suffix) || !stack.isEmpty())) {
/*     */       Bracket p;
/*     */       int endLiteral;
/* 202 */       char ch = expressionString.charAt(pos);
/* 203 */       switch (ch) {
/*     */         case '(':
/*     */         case '[':
/*     */         case '{':
/* 207 */           stack.push(new Bracket(ch, pos));
/*     */           break;
/*     */         case ')':
/*     */         case ']':
/*     */         case '}':
/* 212 */           if (stack.isEmpty()) {
/* 213 */             throw new ParseException(expressionString, pos, "Found closing '" + ch + "' at position " + pos + " without an opening '" + 
/*     */                 
/* 215 */                 Bracket.theOpenBracketFor(ch) + "'");
/*     */           }
/* 217 */           p = stack.pop();
/* 218 */           if (!p.compatibleWithCloseBracket(ch)) {
/* 219 */             throw new ParseException(expressionString, pos, "Found closing '" + ch + "' at position " + pos + " but most recent opening is '" + p.bracket + "' at position " + p.pos);
/*     */           }
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case '"':
/*     */         case '\'':
/* 227 */           endLiteral = expressionString.indexOf(ch, pos + 1);
/* 228 */           if (endLiteral == -1) {
/* 229 */             throw new ParseException(expressionString, pos, "Found non terminating string literal starting at position " + pos);
/*     */           }
/*     */           
/* 232 */           pos = endLiteral;
/*     */           break;
/*     */       } 
/* 235 */       pos++;
/*     */     } 
/* 237 */     if (!stack.isEmpty()) {
/* 238 */       Bracket p = stack.pop();
/* 239 */       throw new ParseException(expressionString, p.pos, "Missing closing '" + 
/* 240 */           Bracket.theCloseBracketFor(p.bracket) + "' for '" + p.bracket + "' at position " + p.pos);
/*     */     } 
/* 242 */     if (!isSuffixHere(expressionString, pos, suffix)) {
/* 243 */       return -1;
/*     */     }
/* 245 */     return pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Expression doParseExpression(String paramString, ParserContext paramParserContext) throws ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Bracket
/*     */   {
/*     */     char bracket;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int pos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Bracket(char bracket, int pos) {
/* 273 */       this.bracket = bracket;
/* 274 */       this.pos = pos;
/*     */     }
/*     */     
/*     */     boolean compatibleWithCloseBracket(char closeBracket) {
/* 278 */       if (this.bracket == '{') {
/* 279 */         return (closeBracket == '}');
/*     */       }
/* 281 */       if (this.bracket == '[') {
/* 282 */         return (closeBracket == ']');
/*     */       }
/* 284 */       return (closeBracket == ')');
/*     */     }
/*     */     
/*     */     static char theOpenBracketFor(char closeBracket) {
/* 288 */       if (closeBracket == '}') {
/* 289 */         return '{';
/*     */       }
/* 291 */       if (closeBracket == ']') {
/* 292 */         return '[';
/*     */       }
/* 294 */       return '(';
/*     */     }
/*     */     
/*     */     static char theCloseBracketFor(char openBracket) {
/* 298 */       if (openBracket == '{') {
/* 299 */         return '}';
/*     */       }
/* 301 */       if (openBracket == '[') {
/* 302 */         return ']';
/*     */       }
/* 304 */       return ')';
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\common\TemplateAwareExpressionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */