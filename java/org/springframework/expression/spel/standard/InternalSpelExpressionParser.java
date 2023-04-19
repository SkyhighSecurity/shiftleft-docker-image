/*      */ package org.springframework.expression.spel.standard;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Stack;
/*      */ import java.util.regex.Pattern;
/*      */ import org.springframework.expression.Expression;
/*      */ import org.springframework.expression.ParseException;
/*      */ import org.springframework.expression.ParserContext;
/*      */ import org.springframework.expression.common.TemplateAwareExpressionParser;
/*      */ import org.springframework.expression.spel.InternalParseException;
/*      */ import org.springframework.expression.spel.SpelMessage;
/*      */ import org.springframework.expression.spel.SpelParseException;
/*      */ import org.springframework.expression.spel.SpelParserConfiguration;
/*      */ import org.springframework.expression.spel.ast.Assign;
/*      */ import org.springframework.expression.spel.ast.BeanReference;
/*      */ import org.springframework.expression.spel.ast.BooleanLiteral;
/*      */ import org.springframework.expression.spel.ast.CompoundExpression;
/*      */ import org.springframework.expression.spel.ast.ConstructorReference;
/*      */ import org.springframework.expression.spel.ast.Elvis;
/*      */ import org.springframework.expression.spel.ast.FunctionReference;
/*      */ import org.springframework.expression.spel.ast.Identifier;
/*      */ import org.springframework.expression.spel.ast.Indexer;
/*      */ import org.springframework.expression.spel.ast.InlineList;
/*      */ import org.springframework.expression.spel.ast.InlineMap;
/*      */ import org.springframework.expression.spel.ast.Literal;
/*      */ import org.springframework.expression.spel.ast.MethodReference;
/*      */ import org.springframework.expression.spel.ast.NullLiteral;
/*      */ import org.springframework.expression.spel.ast.OpAnd;
/*      */ import org.springframework.expression.spel.ast.OpDec;
/*      */ import org.springframework.expression.spel.ast.OpDivide;
/*      */ import org.springframework.expression.spel.ast.OpEQ;
/*      */ import org.springframework.expression.spel.ast.OpGE;
/*      */ import org.springframework.expression.spel.ast.OpGT;
/*      */ import org.springframework.expression.spel.ast.OpInc;
/*      */ import org.springframework.expression.spel.ast.OpLE;
/*      */ import org.springframework.expression.spel.ast.OpLT;
/*      */ import org.springframework.expression.spel.ast.OpMinus;
/*      */ import org.springframework.expression.spel.ast.OpModulus;
/*      */ import org.springframework.expression.spel.ast.OpMultiply;
/*      */ import org.springframework.expression.spel.ast.OpNE;
/*      */ import org.springframework.expression.spel.ast.OpOr;
/*      */ import org.springframework.expression.spel.ast.OpPlus;
/*      */ import org.springframework.expression.spel.ast.OperatorBetween;
/*      */ import org.springframework.expression.spel.ast.OperatorInstanceof;
/*      */ import org.springframework.expression.spel.ast.OperatorMatches;
/*      */ import org.springframework.expression.spel.ast.OperatorNot;
/*      */ import org.springframework.expression.spel.ast.OperatorPower;
/*      */ import org.springframework.expression.spel.ast.Projection;
/*      */ import org.springframework.expression.spel.ast.PropertyOrFieldReference;
/*      */ import org.springframework.expression.spel.ast.QualifiedIdentifier;
/*      */ import org.springframework.expression.spel.ast.Selection;
/*      */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*      */ import org.springframework.expression.spel.ast.StringLiteral;
/*      */ import org.springframework.expression.spel.ast.Ternary;
/*      */ import org.springframework.expression.spel.ast.TypeReference;
/*      */ import org.springframework.expression.spel.ast.VariableReference;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class InternalSpelExpressionParser
/*      */   extends TemplateAwareExpressionParser
/*      */ {
/*   88 */   private static final Pattern VALID_QUALIFIED_ID_PATTERN = Pattern.compile("[\\p{L}\\p{N}_$]+");
/*      */ 
/*      */   
/*      */   private final SpelParserConfiguration configuration;
/*      */ 
/*      */   
/*   94 */   private final Stack<SpelNodeImpl> constructedNodes = new Stack<SpelNodeImpl>();
/*      */ 
/*      */ 
/*      */   
/*      */   private String expressionString;
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Token> tokenStream;
/*      */ 
/*      */ 
/*      */   
/*      */   private int tokenStreamLength;
/*      */ 
/*      */   
/*      */   private int tokenStreamPointer;
/*      */ 
/*      */ 
/*      */   
/*      */   public InternalSpelExpressionParser(SpelParserConfiguration configuration) {
/*  114 */     this.configuration = configuration;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected SpelExpression doParseExpression(String expressionString, ParserContext context) throws ParseException {
/*      */     try {
/*  121 */       this.expressionString = expressionString;
/*  122 */       Tokenizer tokenizer = new Tokenizer(expressionString);
/*  123 */       this.tokenStream = tokenizer.process();
/*  124 */       this.tokenStreamLength = this.tokenStream.size();
/*  125 */       this.tokenStreamPointer = 0;
/*  126 */       this.constructedNodes.clear();
/*  127 */       SpelNodeImpl ast = eatExpression();
/*  128 */       if (moreTokens()) {
/*  129 */         throw new SpelParseException((peekToken()).startPos, SpelMessage.MORE_INPUT, new Object[] { toString(nextToken()) });
/*      */       }
/*  131 */       Assert.isTrue(this.constructedNodes.isEmpty(), "At least one node expected");
/*  132 */       return new SpelExpression(expressionString, ast, this.configuration);
/*      */     }
/*  134 */     catch (InternalParseException ex) {
/*  135 */       throw ex.getCause();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatExpression() {
/*      */     NullLiteral nullLiteral;
/*  146 */     SpelNodeImpl expr = eatLogicalOrExpression();
/*  147 */     if (moreTokens()) {
/*  148 */       Token t = peekToken();
/*  149 */       if (t.kind == TokenKind.ASSIGN) {
/*  150 */         if (expr == null) {
/*  151 */           nullLiteral = new NullLiteral(toPos(t.startPos - 1, t.endPos - 1));
/*      */         }
/*  153 */         nextToken();
/*  154 */         SpelNodeImpl assignedValue = eatLogicalOrExpression();
/*  155 */         return (SpelNodeImpl)new Assign(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)nullLiteral, assignedValue });
/*      */       } 
/*      */       
/*  158 */       if (t.kind == TokenKind.ELVIS) {
/*  159 */         NullLiteral nullLiteral1; if (nullLiteral == null) {
/*  160 */           nullLiteral = new NullLiteral(toPos(t.startPos - 1, t.endPos - 2));
/*      */         }
/*  162 */         nextToken();
/*  163 */         SpelNodeImpl valueIfNull = eatExpression();
/*  164 */         if (valueIfNull == null) {
/*  165 */           nullLiteral1 = new NullLiteral(toPos(t.startPos + 1, t.endPos + 1));
/*      */         }
/*  167 */         return (SpelNodeImpl)new Elvis(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)nullLiteral, (SpelNodeImpl)nullLiteral1 });
/*      */       } 
/*      */       
/*  170 */       if (t.kind == TokenKind.QMARK) {
/*  171 */         if (nullLiteral == null) {
/*  172 */           nullLiteral = new NullLiteral(toPos(t.startPos - 1, t.endPos - 1));
/*      */         }
/*  174 */         nextToken();
/*  175 */         SpelNodeImpl ifTrueExprValue = eatExpression();
/*  176 */         eatToken(TokenKind.COLON);
/*  177 */         SpelNodeImpl ifFalseExprValue = eatExpression();
/*  178 */         return (SpelNodeImpl)new Ternary(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)nullLiteral, ifTrueExprValue, ifFalseExprValue });
/*      */       } 
/*      */     } 
/*  181 */     return (SpelNodeImpl)nullLiteral;
/*      */   }
/*      */   
/*      */   private SpelNodeImpl eatLogicalOrExpression() {
/*      */     OpOr opOr;
/*  186 */     SpelNodeImpl expr = eatLogicalAndExpression();
/*  187 */     while (peekIdentifierToken("or") || peekToken(TokenKind.SYMBOLIC_OR)) {
/*  188 */       Token t = nextToken();
/*  189 */       SpelNodeImpl rhExpr = eatLogicalAndExpression();
/*  190 */       checkOperands(t, expr, rhExpr);
/*  191 */       opOr = new OpOr(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  193 */     return (SpelNodeImpl)opOr;
/*      */   }
/*      */   
/*      */   private SpelNodeImpl eatLogicalAndExpression() {
/*      */     OpAnd opAnd;
/*  198 */     SpelNodeImpl expr = eatRelationalExpression();
/*  199 */     while (peekIdentifierToken("and") || peekToken(TokenKind.SYMBOLIC_AND)) {
/*  200 */       Token t = nextToken();
/*  201 */       SpelNodeImpl rhExpr = eatRelationalExpression();
/*  202 */       checkOperands(t, expr, rhExpr);
/*  203 */       opAnd = new OpAnd(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  205 */     return (SpelNodeImpl)opAnd;
/*      */   }
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatRelationalExpression() {
/*  210 */     SpelNodeImpl expr = eatSumExpression();
/*  211 */     Token relationalOperatorToken = maybeEatRelationalOperator();
/*  212 */     if (relationalOperatorToken != null) {
/*  213 */       Token t = nextToken();
/*  214 */       SpelNodeImpl rhExpr = eatSumExpression();
/*  215 */       checkOperands(t, expr, rhExpr);
/*  216 */       TokenKind tk = relationalOperatorToken.kind;
/*      */       
/*  218 */       if (relationalOperatorToken.isNumericRelationalOperator()) {
/*  219 */         int pos = toPos(t);
/*  220 */         if (tk == TokenKind.GT) {
/*  221 */           return (SpelNodeImpl)new OpGT(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  223 */         if (tk == TokenKind.LT) {
/*  224 */           return (SpelNodeImpl)new OpLT(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  226 */         if (tk == TokenKind.LE) {
/*  227 */           return (SpelNodeImpl)new OpLE(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  229 */         if (tk == TokenKind.GE) {
/*  230 */           return (SpelNodeImpl)new OpGE(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  232 */         if (tk == TokenKind.EQ) {
/*  233 */           return (SpelNodeImpl)new OpEQ(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  235 */         Assert.isTrue((tk == TokenKind.NE), "Not-equals token expected");
/*  236 */         return (SpelNodeImpl)new OpNE(pos, new SpelNodeImpl[] { expr, rhExpr });
/*      */       } 
/*      */       
/*  239 */       if (tk == TokenKind.INSTANCEOF) {
/*  240 */         return (SpelNodeImpl)new OperatorInstanceof(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */       }
/*      */       
/*  243 */       if (tk == TokenKind.MATCHES) {
/*  244 */         return (SpelNodeImpl)new OperatorMatches(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */       }
/*      */       
/*  247 */       Assert.isTrue((tk == TokenKind.BETWEEN), "Between token expected");
/*  248 */       return (SpelNodeImpl)new OperatorBetween(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  250 */     return expr;
/*      */   }
/*      */   
/*      */   private SpelNodeImpl eatSumExpression() {
/*      */     OpMinus opMinus;
/*  255 */     SpelNodeImpl expr = eatProductExpression();
/*  256 */     while (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.INC)) {
/*  257 */       OpPlus opPlus; Token t = nextToken();
/*  258 */       SpelNodeImpl rhExpr = eatProductExpression();
/*  259 */       checkRightOperand(t, rhExpr);
/*  260 */       if (t.kind == TokenKind.PLUS) {
/*  261 */         opPlus = new OpPlus(toPos(t), new SpelNodeImpl[] { expr, rhExpr }); continue;
/*      */       } 
/*  263 */       if (t.kind == TokenKind.MINUS) {
/*  264 */         opMinus = new OpMinus(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)opPlus, rhExpr });
/*      */       }
/*      */     } 
/*  267 */     return (SpelNodeImpl)opMinus;
/*      */   }
/*      */   
/*      */   private SpelNodeImpl eatProductExpression() {
/*      */     OpModulus opModulus;
/*  272 */     SpelNodeImpl expr = eatPowerIncDecExpression();
/*  273 */     while (peekToken(TokenKind.STAR, TokenKind.DIV, TokenKind.MOD)) {
/*  274 */       OpMultiply opMultiply; OpDivide opDivide; Token t = nextToken();
/*  275 */       SpelNodeImpl rhExpr = eatPowerIncDecExpression();
/*  276 */       checkOperands(t, expr, rhExpr);
/*  277 */       if (t.kind == TokenKind.STAR) {
/*  278 */         opMultiply = new OpMultiply(toPos(t), new SpelNodeImpl[] { expr, rhExpr }); continue;
/*      */       } 
/*  280 */       if (t.kind == TokenKind.DIV) {
/*  281 */         opDivide = new OpDivide(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)opMultiply, rhExpr });
/*      */         continue;
/*      */       } 
/*  284 */       Assert.isTrue((t.kind == TokenKind.MOD), "Mod token expected");
/*  285 */       opModulus = new OpModulus(toPos(t), new SpelNodeImpl[] { (SpelNodeImpl)opDivide, rhExpr });
/*      */     } 
/*      */     
/*  288 */     return (SpelNodeImpl)opModulus;
/*      */   }
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatPowerIncDecExpression() {
/*  293 */     SpelNodeImpl expr = eatUnaryExpression();
/*  294 */     if (peekToken(TokenKind.POWER)) {
/*  295 */       Token t = nextToken();
/*  296 */       SpelNodeImpl rhExpr = eatUnaryExpression();
/*  297 */       checkRightOperand(t, rhExpr);
/*  298 */       return (SpelNodeImpl)new OperatorPower(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*      */     
/*  301 */     if (expr != null && peekToken(TokenKind.INC, TokenKind.DEC)) {
/*  302 */       Token t = nextToken();
/*  303 */       if (t.getKind() == TokenKind.INC) {
/*  304 */         return (SpelNodeImpl)new OpInc(toPos(t), true, new SpelNodeImpl[] { expr });
/*      */       }
/*  306 */       return (SpelNodeImpl)new OpDec(toPos(t), true, new SpelNodeImpl[] { expr });
/*      */     } 
/*      */     
/*  309 */     return expr;
/*      */   }
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatUnaryExpression() {
/*  314 */     if (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.NOT)) {
/*  315 */       Token t = nextToken();
/*  316 */       SpelNodeImpl expr = eatUnaryExpression();
/*  317 */       if (t.kind == TokenKind.NOT) {
/*  318 */         return (SpelNodeImpl)new OperatorNot(toPos(t), expr);
/*      */       }
/*      */       
/*  321 */       if (t.kind == TokenKind.PLUS) {
/*  322 */         return (SpelNodeImpl)new OpPlus(toPos(t), new SpelNodeImpl[] { expr });
/*      */       }
/*  324 */       Assert.isTrue((t.kind == TokenKind.MINUS), "Minus token expected");
/*  325 */       return (SpelNodeImpl)new OpMinus(toPos(t), new SpelNodeImpl[] { expr });
/*      */     } 
/*      */     
/*  328 */     if (peekToken(TokenKind.INC, TokenKind.DEC)) {
/*  329 */       Token t = nextToken();
/*  330 */       SpelNodeImpl expr = eatUnaryExpression();
/*  331 */       if (t.getKind() == TokenKind.INC) {
/*  332 */         return (SpelNodeImpl)new OpInc(toPos(t), false, new SpelNodeImpl[] { expr });
/*      */       }
/*  334 */       return (SpelNodeImpl)new OpDec(toPos(t), false, new SpelNodeImpl[] { expr });
/*      */     } 
/*      */     
/*  337 */     return eatPrimaryExpression();
/*      */   }
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatPrimaryExpression() {
/*  342 */     List<SpelNodeImpl> nodes = new ArrayList<SpelNodeImpl>();
/*  343 */     SpelNodeImpl start = eatStartNode();
/*  344 */     nodes.add(start);
/*  345 */     while (maybeEatNode()) {
/*  346 */       nodes.add(pop());
/*      */     }
/*  348 */     if (nodes.size() == 1) {
/*  349 */       return nodes.get(0);
/*      */     }
/*  351 */     return (SpelNodeImpl)new CompoundExpression(toPos(start.getStartPosition(), ((SpelNodeImpl)nodes
/*  352 */           .get(nodes.size() - 1)).getEndPosition()), nodes
/*  353 */         .<SpelNodeImpl>toArray(new SpelNodeImpl[nodes.size()]));
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatNode() {
/*  358 */     SpelNodeImpl expr = null;
/*  359 */     if (peekToken(TokenKind.DOT, TokenKind.SAFE_NAVI)) {
/*  360 */       expr = eatDottedNode();
/*      */     } else {
/*      */       
/*  363 */       expr = maybeEatNonDottedNode();
/*      */     } 
/*      */     
/*  366 */     if (expr == null) {
/*  367 */       return false;
/*      */     }
/*      */     
/*  370 */     push(expr);
/*  371 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private SpelNodeImpl maybeEatNonDottedNode() {
/*  377 */     if (peekToken(TokenKind.LSQUARE) && 
/*  378 */       maybeEatIndexer()) {
/*  379 */       return pop();
/*      */     }
/*      */     
/*  382 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatDottedNode() {
/*  395 */     Token t = nextToken();
/*  396 */     boolean nullSafeNavigation = (t.kind == TokenKind.SAFE_NAVI);
/*  397 */     if (maybeEatMethodOrProperty(nullSafeNavigation) || maybeEatFunctionOrVar() || 
/*  398 */       maybeEatProjection(nullSafeNavigation) || maybeEatSelection(nullSafeNavigation)) {
/*  399 */       return pop();
/*      */     }
/*  401 */     if (peekToken() == null) {
/*      */       
/*  403 */       raiseInternalException(t.startPos, SpelMessage.OOD, new Object[0]);
/*      */     } else {
/*      */       
/*  406 */       raiseInternalException(t.startPos, SpelMessage.UNEXPECTED_DATA_AFTER_DOT, new Object[] { toString(peekToken()) });
/*      */     } 
/*  408 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatFunctionOrVar() {
/*  418 */     if (!peekToken(TokenKind.HASH)) {
/*  419 */       return false;
/*      */     }
/*  421 */     Token t = nextToken();
/*  422 */     Token functionOrVariableName = eatToken(TokenKind.IDENTIFIER);
/*  423 */     SpelNodeImpl[] args = maybeEatMethodArgs();
/*  424 */     if (args == null) {
/*  425 */       push((SpelNodeImpl)new VariableReference(functionOrVariableName.data, 
/*  426 */             toPos(t.startPos, functionOrVariableName.endPos)));
/*  427 */       return true;
/*      */     } 
/*      */     
/*  430 */     push((SpelNodeImpl)new FunctionReference(functionOrVariableName.data, 
/*  431 */           toPos(t.startPos, functionOrVariableName.endPos), args));
/*  432 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private SpelNodeImpl[] maybeEatMethodArgs() {
/*  437 */     if (!peekToken(TokenKind.LPAREN)) {
/*  438 */       return null;
/*      */     }
/*  440 */     List<SpelNodeImpl> args = new ArrayList<SpelNodeImpl>();
/*  441 */     consumeArguments(args);
/*  442 */     eatToken(TokenKind.RPAREN);
/*  443 */     return args.<SpelNodeImpl>toArray(new SpelNodeImpl[args.size()]);
/*      */   }
/*      */   
/*      */   private void eatConstructorArgs(List<SpelNodeImpl> accumulatedArguments) {
/*  447 */     if (!peekToken(TokenKind.LPAREN)) {
/*  448 */       throw new InternalParseException(new SpelParseException(this.expressionString, 
/*  449 */             positionOf(peekToken()), SpelMessage.MISSING_CONSTRUCTOR_ARGS, new Object[0]));
/*      */     }
/*  451 */     consumeArguments(accumulatedArguments);
/*  452 */     eatToken(TokenKind.RPAREN);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void consumeArguments(List<SpelNodeImpl> accumulatedArguments) {
/*      */     Token next;
/*  459 */     int pos = (peekToken()).startPos;
/*      */     
/*      */     do {
/*  462 */       nextToken();
/*  463 */       Token t = peekToken();
/*  464 */       if (t == null) {
/*  465 */         raiseInternalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
/*      */       }
/*  467 */       if (t.kind != TokenKind.RPAREN) {
/*  468 */         accumulatedArguments.add(eatExpression());
/*      */       }
/*  470 */       next = peekToken();
/*      */     }
/*  472 */     while (next != null && next.kind == TokenKind.COMMA);
/*      */     
/*  474 */     if (next == null) {
/*  475 */       raiseInternalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   private int positionOf(Token t) {
/*  480 */     if (t == null)
/*      */     {
/*      */       
/*  483 */       return this.expressionString.length();
/*      */     }
/*  485 */     return t.startPos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatStartNode() {
/*  500 */     if (maybeEatLiteral()) {
/*  501 */       return pop();
/*      */     }
/*  503 */     if (maybeEatParenExpression()) {
/*  504 */       return pop();
/*      */     }
/*  506 */     if (maybeEatTypeReference() || maybeEatNullReference() || maybeEatConstructorReference() || 
/*  507 */       maybeEatMethodOrProperty(false) || maybeEatFunctionOrVar()) {
/*  508 */       return pop();
/*      */     }
/*  510 */     if (maybeEatBeanReference()) {
/*  511 */       return pop();
/*      */     }
/*  513 */     if (maybeEatProjection(false) || maybeEatSelection(false) || maybeEatIndexer()) {
/*  514 */       return pop();
/*      */     }
/*  516 */     if (maybeEatInlineListOrMap()) {
/*  517 */       return pop();
/*      */     }
/*      */     
/*  520 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatBeanReference() {
/*  527 */     if (peekToken(TokenKind.BEAN_REF) || peekToken(TokenKind.FACTORY_BEAN_REF)) {
/*  528 */       BeanReference beanReference; Token beanRefToken = nextToken();
/*  529 */       Token beanNameToken = null;
/*  530 */       String beanName = null;
/*  531 */       if (peekToken(TokenKind.IDENTIFIER)) {
/*  532 */         beanNameToken = eatToken(TokenKind.IDENTIFIER);
/*  533 */         beanName = beanNameToken.data;
/*      */       }
/*  535 */       else if (peekToken(TokenKind.LITERAL_STRING)) {
/*  536 */         beanNameToken = eatToken(TokenKind.LITERAL_STRING);
/*  537 */         beanName = beanNameToken.stringValue();
/*  538 */         beanName = beanName.substring(1, beanName.length() - 1);
/*      */       } else {
/*      */         
/*  541 */         raiseInternalException(beanRefToken.startPos, SpelMessage.INVALID_BEAN_REFERENCE, new Object[0]);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  546 */       if (beanRefToken.getKind() == TokenKind.FACTORY_BEAN_REF) {
/*      */         
/*  548 */         String beanNameString = TokenKind.FACTORY_BEAN_REF.tokenChars + beanName;
/*      */         
/*  550 */         beanReference = new BeanReference(toPos(beanRefToken.startPos, beanNameToken.endPos), beanNameString);
/*      */       } else {
/*      */         
/*  553 */         beanReference = new BeanReference(toPos(beanNameToken), beanName);
/*      */       } 
/*  555 */       this.constructedNodes.push(beanReference);
/*  556 */       return true;
/*      */     } 
/*  558 */     return false;
/*      */   }
/*      */   
/*      */   private boolean maybeEatTypeReference() {
/*  562 */     if (peekToken(TokenKind.IDENTIFIER)) {
/*  563 */       Token typeName = peekToken();
/*  564 */       if (!"T".equals(typeName.stringValue())) {
/*  565 */         return false;
/*      */       }
/*      */       
/*  568 */       Token t = nextToken();
/*  569 */       if (peekToken(TokenKind.RSQUARE)) {
/*      */         
/*  571 */         push((SpelNodeImpl)new PropertyOrFieldReference(false, t.data, toPos(t)));
/*  572 */         return true;
/*      */       } 
/*  574 */       eatToken(TokenKind.LPAREN);
/*  575 */       SpelNodeImpl node = eatPossiblyQualifiedId();
/*      */ 
/*      */       
/*  578 */       int dims = 0;
/*  579 */       while (peekToken(TokenKind.LSQUARE, true)) {
/*  580 */         eatToken(TokenKind.RSQUARE);
/*  581 */         dims++;
/*      */       } 
/*  583 */       eatToken(TokenKind.RPAREN);
/*  584 */       this.constructedNodes.push(new TypeReference(toPos(typeName), node, dims));
/*  585 */       return true;
/*      */     } 
/*  587 */     return false;
/*      */   }
/*      */   
/*      */   private boolean maybeEatNullReference() {
/*  591 */     if (peekToken(TokenKind.IDENTIFIER)) {
/*  592 */       Token nullToken = peekToken();
/*  593 */       if (!"null".equalsIgnoreCase(nullToken.stringValue())) {
/*  594 */         return false;
/*      */       }
/*  596 */       nextToken();
/*  597 */       this.constructedNodes.push(new NullLiteral(toPos(nullToken)));
/*  598 */       return true;
/*      */     } 
/*  600 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatProjection(boolean nullSafeNavigation) {
/*  605 */     Token t = peekToken();
/*  606 */     if (!peekToken(TokenKind.PROJECT, true)) {
/*  607 */       return false;
/*      */     }
/*  609 */     SpelNodeImpl expr = eatExpression();
/*  610 */     eatToken(TokenKind.RSQUARE);
/*  611 */     this.constructedNodes.push(new Projection(nullSafeNavigation, toPos(t), expr));
/*  612 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatInlineListOrMap() {
/*      */     InlineMap inlineMap;
/*  618 */     Token t = peekToken();
/*  619 */     if (!peekToken(TokenKind.LCURLY, true)) {
/*  620 */       return false;
/*      */     }
/*  622 */     SpelNodeImpl expr = null;
/*  623 */     Token closingCurly = peekToken();
/*  624 */     if (peekToken(TokenKind.RCURLY, true))
/*      */     
/*  626 */     { InlineList inlineList = new InlineList(toPos(t.startPos, closingCurly.endPos), new SpelNodeImpl[0]); }
/*      */     
/*  628 */     else if (peekToken(TokenKind.COLON, true))
/*  629 */     { closingCurly = eatToken(TokenKind.RCURLY);
/*      */       
/*  631 */       inlineMap = new InlineMap(toPos(t.startPos, closingCurly.endPos), new SpelNodeImpl[0]); }
/*      */     else
/*      */     
/*  634 */     { SpelNodeImpl firstExpression = eatExpression();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  639 */       if (peekToken(TokenKind.RCURLY))
/*  640 */       { List<SpelNodeImpl> listElements = new ArrayList<SpelNodeImpl>();
/*  641 */         listElements.add(firstExpression);
/*  642 */         closingCurly = eatToken(TokenKind.RCURLY);
/*      */         
/*  644 */         InlineList inlineList = new InlineList(toPos(t.startPos, closingCurly.endPos), listElements.<SpelNodeImpl>toArray(new SpelNodeImpl[listElements.size()])); }
/*      */       else
/*  646 */       { if (peekToken(TokenKind.COMMA, true))
/*  647 */         { List<SpelNodeImpl> listElements = new ArrayList<SpelNodeImpl>();
/*  648 */           listElements.add(firstExpression);
/*      */           while (true)
/*  650 */           { listElements.add(eatExpression());
/*      */             
/*  652 */             if (!peekToken(TokenKind.COMMA, true))
/*  653 */             { closingCurly = eatToken(TokenKind.RCURLY);
/*      */               
/*  655 */               InlineList inlineList = new InlineList(toPos(t.startPos, closingCurly.endPos), listElements.<SpelNodeImpl>toArray(new SpelNodeImpl[listElements.size()]));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  675 */               this.constructedNodes.push(inlineList);
/*  676 */               return true; }  }  }  if (peekToken(TokenKind.COLON, true)) { List<SpelNodeImpl> mapElements = new ArrayList<SpelNodeImpl>(); mapElements.add(firstExpression); mapElements.add(eatExpression()); while (peekToken(TokenKind.COMMA, true)) { mapElements.add(eatExpression()); eatToken(TokenKind.COLON); mapElements.add(eatExpression()); }  closingCurly = eatToken(TokenKind.RCURLY); inlineMap = new InlineMap(toPos(t.startPos, closingCurly.endPos), mapElements.<SpelNodeImpl>toArray(new SpelNodeImpl[mapElements.size()])); } else { raiseInternalException(t.startPos, SpelMessage.OOD, new Object[0]); }  }  }  this.constructedNodes.push(inlineMap); return true;
/*      */   }
/*      */   
/*      */   private boolean maybeEatIndexer() {
/*  680 */     Token t = peekToken();
/*  681 */     if (!peekToken(TokenKind.LSQUARE, true)) {
/*  682 */       return false;
/*      */     }
/*  684 */     SpelNodeImpl expr = eatExpression();
/*  685 */     eatToken(TokenKind.RSQUARE);
/*  686 */     this.constructedNodes.push(new Indexer(toPos(t), expr));
/*  687 */     return true;
/*      */   }
/*      */   
/*      */   private boolean maybeEatSelection(boolean nullSafeNavigation) {
/*  691 */     Token t = peekToken();
/*  692 */     if (!peekSelectToken()) {
/*  693 */       return false;
/*      */     }
/*  695 */     nextToken();
/*  696 */     SpelNodeImpl expr = eatExpression();
/*  697 */     if (expr == null) {
/*  698 */       raiseInternalException(toPos(t), SpelMessage.MISSING_SELECTION_EXPRESSION, new Object[0]);
/*      */     }
/*  700 */     eatToken(TokenKind.RSQUARE);
/*  701 */     if (t.kind == TokenKind.SELECT_FIRST) {
/*  702 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 1, toPos(t), expr));
/*      */     }
/*  704 */     else if (t.kind == TokenKind.SELECT_LAST) {
/*  705 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 2, toPos(t), expr));
/*      */     } else {
/*      */       
/*  708 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 0, toPos(t), expr));
/*      */     } 
/*  710 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatPossiblyQualifiedId() {
/*  718 */     LinkedList<SpelNodeImpl> qualifiedIdPieces = new LinkedList<SpelNodeImpl>();
/*  719 */     Token node = peekToken();
/*  720 */     while (isValidQualifiedId(node)) {
/*  721 */       nextToken();
/*  722 */       if (node.kind != TokenKind.DOT) {
/*  723 */         qualifiedIdPieces.add(new Identifier(node.stringValue(), toPos(node)));
/*      */       }
/*  725 */       node = peekToken();
/*      */     } 
/*  727 */     if (qualifiedIdPieces.isEmpty()) {
/*  728 */       if (node == null) {
/*  729 */         raiseInternalException(this.expressionString.length(), SpelMessage.OOD, new Object[0]);
/*      */       }
/*  731 */       raiseInternalException(node.startPos, SpelMessage.NOT_EXPECTED_TOKEN, new Object[] { "qualified ID", node
/*  732 */             .getKind().toString().toLowerCase() });
/*      */     } 
/*  734 */     int pos = toPos(((SpelNodeImpl)qualifiedIdPieces.getFirst()).getStartPosition(), ((SpelNodeImpl)qualifiedIdPieces
/*  735 */         .getLast()).getEndPosition());
/*  736 */     return (SpelNodeImpl)new QualifiedIdentifier(pos, qualifiedIdPieces
/*  737 */         .<SpelNodeImpl>toArray(new SpelNodeImpl[qualifiedIdPieces.size()]));
/*      */   }
/*      */   
/*      */   private boolean isValidQualifiedId(Token node) {
/*  741 */     if (node == null || node.kind == TokenKind.LITERAL_STRING) {
/*  742 */       return false;
/*      */     }
/*  744 */     if (node.kind == TokenKind.DOT || node.kind == TokenKind.IDENTIFIER) {
/*  745 */       return true;
/*      */     }
/*  747 */     String value = node.stringValue();
/*  748 */     return (StringUtils.hasLength(value) && VALID_QUALIFIED_ID_PATTERN.matcher(value).matches());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatMethodOrProperty(boolean nullSafeNavigation) {
/*  755 */     if (peekToken(TokenKind.IDENTIFIER)) {
/*  756 */       Token methodOrPropertyName = nextToken();
/*  757 */       SpelNodeImpl[] args = maybeEatMethodArgs();
/*  758 */       if (args == null) {
/*      */         
/*  760 */         push((SpelNodeImpl)new PropertyOrFieldReference(nullSafeNavigation, methodOrPropertyName.data, 
/*  761 */               toPos(methodOrPropertyName)));
/*  762 */         return true;
/*      */       } 
/*      */       
/*  765 */       push((SpelNodeImpl)new MethodReference(nullSafeNavigation, methodOrPropertyName.data, 
/*  766 */             toPos(methodOrPropertyName), args));
/*      */       
/*  768 */       return true;
/*      */     } 
/*  770 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatConstructorReference() {
/*  776 */     if (peekIdentifierToken("new")) {
/*  777 */       Token newToken = nextToken();
/*      */       
/*  779 */       if (peekToken(TokenKind.RSQUARE)) {
/*      */         
/*  781 */         push((SpelNodeImpl)new PropertyOrFieldReference(false, newToken.data, toPos(newToken)));
/*  782 */         return true;
/*      */       } 
/*  784 */       SpelNodeImpl possiblyQualifiedConstructorName = eatPossiblyQualifiedId();
/*  785 */       List<SpelNodeImpl> nodes = new ArrayList<SpelNodeImpl>();
/*  786 */       nodes.add(possiblyQualifiedConstructorName);
/*  787 */       if (peekToken(TokenKind.LSQUARE)) {
/*      */         
/*  789 */         List<SpelNodeImpl> dimensions = new ArrayList<SpelNodeImpl>();
/*  790 */         while (peekToken(TokenKind.LSQUARE, true)) {
/*  791 */           if (!peekToken(TokenKind.RSQUARE)) {
/*  792 */             dimensions.add(eatExpression());
/*      */           } else {
/*      */             
/*  795 */             dimensions.add(null);
/*      */           } 
/*  797 */           eatToken(TokenKind.RSQUARE);
/*      */         } 
/*  799 */         if (maybeEatInlineListOrMap()) {
/*  800 */           nodes.add(pop());
/*      */         }
/*  802 */         push((SpelNodeImpl)new ConstructorReference(toPos(newToken), dimensions
/*  803 */               .<SpelNodeImpl>toArray(new SpelNodeImpl[dimensions.size()]), nodes
/*  804 */               .<SpelNodeImpl>toArray(new SpelNodeImpl[nodes.size()])));
/*      */       }
/*      */       else {
/*      */         
/*  808 */         eatConstructorArgs(nodes);
/*      */         
/*  810 */         push((SpelNodeImpl)new ConstructorReference(toPos(newToken), nodes
/*  811 */               .<SpelNodeImpl>toArray(new SpelNodeImpl[nodes.size()])));
/*      */       } 
/*  813 */       return true;
/*      */     } 
/*  815 */     return false;
/*      */   }
/*      */   
/*      */   private void push(SpelNodeImpl newNode) {
/*  819 */     this.constructedNodes.push(newNode);
/*      */   }
/*      */   
/*      */   private SpelNodeImpl pop() {
/*  823 */     return this.constructedNodes.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatLiteral() {
/*  835 */     Token t = peekToken();
/*  836 */     if (t == null) {
/*  837 */       return false;
/*      */     }
/*  839 */     if (t.kind == TokenKind.LITERAL_INT) {
/*  840 */       push((SpelNodeImpl)Literal.getIntLiteral(t.data, toPos(t), 10));
/*      */     }
/*  842 */     else if (t.kind == TokenKind.LITERAL_LONG) {
/*  843 */       push((SpelNodeImpl)Literal.getLongLiteral(t.data, toPos(t), 10));
/*      */     }
/*  845 */     else if (t.kind == TokenKind.LITERAL_HEXINT) {
/*  846 */       push((SpelNodeImpl)Literal.getIntLiteral(t.data, toPos(t), 16));
/*      */     }
/*  848 */     else if (t.kind == TokenKind.LITERAL_HEXLONG) {
/*  849 */       push((SpelNodeImpl)Literal.getLongLiteral(t.data, toPos(t), 16));
/*      */     }
/*  851 */     else if (t.kind == TokenKind.LITERAL_REAL) {
/*  852 */       push((SpelNodeImpl)Literal.getRealLiteral(t.data, toPos(t), false));
/*      */     }
/*  854 */     else if (t.kind == TokenKind.LITERAL_REAL_FLOAT) {
/*  855 */       push((SpelNodeImpl)Literal.getRealLiteral(t.data, toPos(t), true));
/*      */     }
/*  857 */     else if (peekIdentifierToken("true")) {
/*  858 */       push((SpelNodeImpl)new BooleanLiteral(t.data, toPos(t), true));
/*      */     }
/*  860 */     else if (peekIdentifierToken("false")) {
/*  861 */       push((SpelNodeImpl)new BooleanLiteral(t.data, toPos(t), false));
/*      */     }
/*  863 */     else if (t.kind == TokenKind.LITERAL_STRING) {
/*  864 */       push((SpelNodeImpl)new StringLiteral(t.data, toPos(t), t.data));
/*      */     } else {
/*      */       
/*  867 */       return false;
/*      */     } 
/*  869 */     nextToken();
/*  870 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatParenExpression() {
/*  875 */     if (peekToken(TokenKind.LPAREN)) {
/*  876 */       nextToken();
/*  877 */       SpelNodeImpl expr = eatExpression();
/*  878 */       eatToken(TokenKind.RPAREN);
/*  879 */       push(expr);
/*  880 */       return true;
/*      */     } 
/*      */     
/*  883 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Token maybeEatRelationalOperator() {
/*  891 */     Token t = peekToken();
/*  892 */     if (t == null) {
/*  893 */       return null;
/*      */     }
/*  895 */     if (t.isNumericRelationalOperator()) {
/*  896 */       return t;
/*      */     }
/*  898 */     if (t.isIdentifier()) {
/*  899 */       String idString = t.stringValue();
/*  900 */       if (idString.equalsIgnoreCase("instanceof")) {
/*  901 */         return t.asInstanceOfToken();
/*      */       }
/*  903 */       if (idString.equalsIgnoreCase("matches")) {
/*  904 */         return t.asMatchesToken();
/*      */       }
/*  906 */       if (idString.equalsIgnoreCase("between")) {
/*  907 */         return t.asBetweenToken();
/*      */       }
/*      */     } 
/*  910 */     return null;
/*      */   }
/*      */   
/*      */   private Token eatToken(TokenKind expectedKind) {
/*  914 */     Token t = nextToken();
/*  915 */     if (t == null) {
/*  916 */       raiseInternalException(this.expressionString.length(), SpelMessage.OOD, new Object[0]);
/*      */     }
/*  918 */     if (t.kind != expectedKind) {
/*  919 */       raiseInternalException(t.startPos, SpelMessage.NOT_EXPECTED_TOKEN, new Object[] { expectedKind
/*  920 */             .toString().toLowerCase(), t.getKind().toString().toLowerCase() });
/*      */     }
/*  922 */     return t;
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind desiredTokenKind) {
/*  926 */     return peekToken(desiredTokenKind, false);
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind desiredTokenKind, boolean consumeIfMatched) {
/*  930 */     if (!moreTokens()) {
/*  931 */       return false;
/*      */     }
/*  933 */     Token t = peekToken();
/*  934 */     if (t.kind == desiredTokenKind) {
/*  935 */       if (consumeIfMatched) {
/*  936 */         this.tokenStreamPointer++;
/*      */       }
/*  938 */       return true;
/*      */     } 
/*      */     
/*  941 */     if (desiredTokenKind == TokenKind.IDENTIFIER)
/*      */     {
/*      */ 
/*      */       
/*  945 */       if (t.kind.ordinal() >= TokenKind.DIV.ordinal() && t.kind.ordinal() <= TokenKind.NOT.ordinal() && t.data != null)
/*      */       {
/*      */         
/*  948 */         return true;
/*      */       }
/*      */     }
/*  951 */     return false;
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind possible1, TokenKind possible2) {
/*  955 */     if (!moreTokens()) {
/*  956 */       return false;
/*      */     }
/*  958 */     Token t = peekToken();
/*  959 */     return (t.kind == possible1 || t.kind == possible2);
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind possible1, TokenKind possible2, TokenKind possible3) {
/*  963 */     if (!moreTokens()) {
/*  964 */       return false;
/*      */     }
/*  966 */     Token t = peekToken();
/*  967 */     return (t.kind == possible1 || t.kind == possible2 || t.kind == possible3);
/*      */   }
/*      */   
/*      */   private boolean peekIdentifierToken(String identifierString) {
/*  971 */     if (!moreTokens()) {
/*  972 */       return false;
/*      */     }
/*  974 */     Token t = peekToken();
/*  975 */     return (t.kind == TokenKind.IDENTIFIER && t.stringValue().equalsIgnoreCase(identifierString));
/*      */   }
/*      */   
/*      */   private boolean peekSelectToken() {
/*  979 */     if (!moreTokens()) {
/*  980 */       return false;
/*      */     }
/*  982 */     Token t = peekToken();
/*  983 */     return (t.kind == TokenKind.SELECT || t.kind == TokenKind.SELECT_FIRST || t.kind == TokenKind.SELECT_LAST);
/*      */   }
/*      */   
/*      */   private boolean moreTokens() {
/*  987 */     return (this.tokenStreamPointer < this.tokenStream.size());
/*      */   }
/*      */   
/*      */   private Token nextToken() {
/*  991 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/*  992 */       return null;
/*      */     }
/*  994 */     return this.tokenStream.get(this.tokenStreamPointer++);
/*      */   }
/*      */   
/*      */   private Token peekToken() {
/*  998 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/*  999 */       return null;
/*      */     }
/* 1001 */     return this.tokenStream.get(this.tokenStreamPointer);
/*      */   }
/*      */   
/*      */   private void raiseInternalException(int pos, SpelMessage message, Object... inserts) {
/* 1005 */     throw new InternalParseException(new SpelParseException(this.expressionString, pos, message, inserts));
/*      */   }
/*      */   
/*      */   public String toString(Token t) {
/* 1009 */     if (t.getKind().hasPayload()) {
/* 1010 */       return t.stringValue();
/*      */     }
/* 1012 */     return t.kind.toString().toLowerCase();
/*      */   }
/*      */   
/*      */   private void checkOperands(Token token, SpelNodeImpl left, SpelNodeImpl right) {
/* 1016 */     checkLeftOperand(token, left);
/* 1017 */     checkRightOperand(token, right);
/*      */   }
/*      */   
/*      */   private void checkLeftOperand(Token token, SpelNodeImpl operandExpression) {
/* 1021 */     if (operandExpression == null) {
/* 1022 */       raiseInternalException(token.startPos, SpelMessage.LEFT_OPERAND_PROBLEM, new Object[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkRightOperand(Token token, SpelNodeImpl operandExpression) {
/* 1027 */     if (operandExpression == null) {
/* 1028 */       raiseInternalException(token.startPos, SpelMessage.RIGHT_OPERAND_PROBLEM, new Object[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private int toPos(Token t) {
/* 1034 */     return (t.startPos << 16) + t.endPos;
/*      */   }
/*      */   
/*      */   private int toPos(int start, int end) {
/* 1038 */     return (start << 16) + end;
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\standard\InternalSpelExpressionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */