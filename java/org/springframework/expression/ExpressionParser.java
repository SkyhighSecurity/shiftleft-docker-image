package org.springframework.expression;

public interface ExpressionParser {
  Expression parseExpression(String paramString) throws ParseException;
  
  Expression parseExpression(String paramString, ParserContext paramParserContext) throws ParseException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\ExpressionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */