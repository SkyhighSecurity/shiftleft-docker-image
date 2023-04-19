/*     */ package org.springframework.expression.spel.standard;
/*     */ 
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CompiledExpression;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelCompilerMode;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.SpelParserConfiguration;
/*     */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class SpelExpression
/*     */   implements Expression
/*     */ {
/*     */   private static final int INTERPRETED_COUNT_THRESHOLD = 100;
/*     */   private static final int FAILED_ATTEMPTS_THRESHOLD = 100;
/*     */   private final String expression;
/*     */   private final SpelNodeImpl ast;
/*     */   private final SpelParserConfiguration configuration;
/*     */   private EvaluationContext evaluationContext;
/*     */   private CompiledExpression compiledAst;
/*  69 */   private volatile int interpretedCount = 0;
/*     */ 
/*     */ 
/*     */   
/*  73 */   private volatile int failedAttempts = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelExpression(String expression, SpelNodeImpl ast, SpelParserConfiguration configuration) {
/*  80 */     this.expression = expression;
/*  81 */     this.ast = ast;
/*  82 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEvaluationContext(EvaluationContext evaluationContext) {
/*  91 */     this.evaluationContext = evaluationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EvaluationContext getEvaluationContext() {
/*  99 */     if (this.evaluationContext == null) {
/* 100 */       this.evaluationContext = (EvaluationContext)new StandardEvaluationContext();
/*     */     }
/* 102 */     return this.evaluationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExpressionString() {
/* 110 */     return this.expression;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue() throws EvaluationException {
/* 115 */     if (this.compiledAst != null) {
/*     */       try {
/* 117 */         EvaluationContext context = getEvaluationContext();
/* 118 */         return this.compiledAst.getValue(context.getRootObject().getValue(), context);
/*     */       }
/* 120 */       catch (Throwable ex) {
/*     */         
/* 122 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 123 */           this.interpretedCount = 0;
/* 124 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 128 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 133 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/* 134 */     Object result = this.ast.getValue(expressionState);
/* 135 */     checkCompile(expressionState);
/* 136 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValue(Class<T> expectedResultType) throws EvaluationException {
/* 142 */     if (this.compiledAst != null) {
/*     */       try {
/* 144 */         EvaluationContext context = getEvaluationContext();
/* 145 */         Object result = this.compiledAst.getValue(context.getRootObject().getValue(), context);
/* 146 */         if (expectedResultType == null) {
/* 147 */           return (T)result;
/*     */         }
/*     */         
/* 150 */         return (T)ExpressionUtils.convertTypedValue(
/* 151 */             getEvaluationContext(), new TypedValue(result), expectedResultType);
/*     */       
/*     */       }
/* 154 */       catch (Throwable ex) {
/*     */         
/* 156 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 157 */           this.interpretedCount = 0;
/* 158 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 162 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 167 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/* 168 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 169 */     checkCompile(expressionState);
/* 170 */     return (T)ExpressionUtils.convertTypedValue(expressionState
/* 171 */         .getEvaluationContext(), typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue(Object rootObject) throws EvaluationException {
/* 176 */     if (this.compiledAst != null) {
/*     */       try {
/* 178 */         return this.compiledAst.getValue(rootObject, getEvaluationContext());
/*     */       }
/* 180 */       catch (Throwable ex) {
/*     */         
/* 182 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 183 */           this.interpretedCount = 0;
/* 184 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 188 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 194 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 195 */     Object result = this.ast.getValue(expressionState);
/* 196 */     checkCompile(expressionState);
/* 197 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValue(Object rootObject, Class<T> expectedResultType) throws EvaluationException {
/* 203 */     if (this.compiledAst != null) {
/*     */       try {
/* 205 */         Object result = this.compiledAst.getValue(rootObject, getEvaluationContext());
/* 206 */         if (expectedResultType == null) {
/* 207 */           return (T)result;
/*     */         }
/*     */         
/* 210 */         return (T)ExpressionUtils.convertTypedValue(
/* 211 */             getEvaluationContext(), new TypedValue(result), expectedResultType);
/*     */       
/*     */       }
/* 214 */       catch (Throwable ex) {
/*     */         
/* 216 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 217 */           this.interpretedCount = 0;
/* 218 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 222 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 228 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 229 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 230 */     checkCompile(expressionState);
/* 231 */     return (T)ExpressionUtils.convertTypedValue(expressionState
/* 232 */         .getEvaluationContext(), typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue(EvaluationContext context) throws EvaluationException {
/* 237 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 239 */     if (this.compiledAst != null) {
/*     */       try {
/* 241 */         return this.compiledAst.getValue(context.getRootObject().getValue(), context);
/*     */       }
/* 243 */       catch (Throwable ex) {
/*     */         
/* 245 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 246 */           this.interpretedCount = 0;
/* 247 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 251 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 256 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 257 */     Object result = this.ast.getValue(expressionState);
/* 258 */     checkCompile(expressionState);
/* 259 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValue(EvaluationContext context, Class<T> expectedResultType) throws EvaluationException {
/* 265 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 267 */     if (this.compiledAst != null) {
/*     */       try {
/* 269 */         Object result = this.compiledAst.getValue(context.getRootObject().getValue(), context);
/* 270 */         if (expectedResultType != null) {
/* 271 */           return (T)ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
/*     */         }
/*     */         
/* 274 */         return (T)result;
/*     */       
/*     */       }
/* 277 */       catch (Throwable ex) {
/*     */         
/* 279 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 280 */           this.interpretedCount = 0;
/* 281 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 285 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 290 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 291 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 292 */     checkCompile(expressionState);
/* 293 */     return (T)ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 298 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 300 */     if (this.compiledAst != null) {
/*     */       try {
/* 302 */         return this.compiledAst.getValue(rootObject, context);
/*     */       }
/* 304 */       catch (Throwable ex) {
/*     */         
/* 306 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 307 */           this.interpretedCount = 0;
/* 308 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 312 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 317 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 318 */     Object result = this.ast.getValue(expressionState);
/* 319 */     checkCompile(expressionState);
/* 320 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValue(EvaluationContext context, Object rootObject, Class<T> expectedResultType) throws EvaluationException {
/* 328 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 330 */     if (this.compiledAst != null) {
/*     */       try {
/* 332 */         Object result = this.compiledAst.getValue(rootObject, context);
/* 333 */         if (expectedResultType != null) {
/* 334 */           return (T)ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
/*     */         }
/*     */         
/* 337 */         return (T)result;
/*     */       
/*     */       }
/* 340 */       catch (Throwable ex) {
/*     */         
/* 342 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 343 */           this.interpretedCount = 0;
/* 344 */           this.compiledAst = null;
/*     */         }
/*     */         else {
/*     */           
/* 348 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 353 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 354 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 355 */     checkCompile(expressionState);
/* 356 */     return (T)ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType() throws EvaluationException {
/* 361 */     return getValueType(getEvaluationContext());
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(Object rootObject) throws EvaluationException {
/* 366 */     return getValueType(getEvaluationContext(), rootObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context) throws EvaluationException {
/* 371 */     Assert.notNull(context, "EvaluationContext is required");
/* 372 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 373 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
/* 374 */     return (typeDescriptor != null) ? typeDescriptor.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 379 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 380 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
/* 381 */     return (typeDescriptor != null) ? typeDescriptor.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor() throws EvaluationException {
/* 386 */     return getValueTypeDescriptor(getEvaluationContext());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(Object rootObject) throws EvaluationException {
/* 392 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 393 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) throws EvaluationException {
/* 398 */     Assert.notNull(context, "EvaluationContext is required");
/* 399 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 400 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 407 */     Assert.notNull(context, "EvaluationContext is required");
/* 408 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 409 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(Object rootObject) throws EvaluationException {
/* 414 */     return this.ast.isWritable(new ExpressionState(
/* 415 */           getEvaluationContext(), toTypedValue(rootObject), this.configuration));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context) throws EvaluationException {
/* 420 */     Assert.notNull(context, "EvaluationContext is required");
/* 421 */     return this.ast.isWritable(new ExpressionState(context, this.configuration));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(EvaluationContext context, Object rootObject) throws EvaluationException {
/* 426 */     Assert.notNull(context, "EvaluationContext is required");
/* 427 */     return this.ast.isWritable(new ExpressionState(context, toTypedValue(rootObject), this.configuration));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Object rootObject, Object value) throws EvaluationException {
/* 432 */     this.ast.setValue(new ExpressionState(
/* 433 */           getEvaluationContext(), toTypedValue(rootObject), this.configuration), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, Object value) throws EvaluationException {
/* 438 */     Assert.notNull(context, "EvaluationContext is required");
/* 439 */     this.ast.setValue(new ExpressionState(context, this.configuration), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(EvaluationContext context, Object rootObject, Object value) throws EvaluationException {
/* 446 */     Assert.notNull(context, "EvaluationContext is required");
/* 447 */     this.ast.setValue(new ExpressionState(context, toTypedValue(rootObject), this.configuration), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkCompile(ExpressionState expressionState) {
/* 457 */     this.interpretedCount++;
/* 458 */     SpelCompilerMode compilerMode = expressionState.getConfiguration().getCompilerMode();
/* 459 */     if (compilerMode != SpelCompilerMode.OFF) {
/* 460 */       if (compilerMode == SpelCompilerMode.IMMEDIATE) {
/* 461 */         if (this.interpretedCount > 1) {
/* 462 */           compileExpression();
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 467 */       else if (this.interpretedCount > 100) {
/* 468 */         compileExpression();
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
/*     */   public boolean compileExpression() {
/* 481 */     if (this.failedAttempts > 100)
/*     */     {
/* 483 */       return false;
/*     */     }
/* 485 */     if (this.compiledAst == null) {
/* 486 */       synchronized (this.expression) {
/*     */         
/* 488 */         if (this.compiledAst != null) {
/* 489 */           return true;
/*     */         }
/* 491 */         SpelCompiler compiler = SpelCompiler.getCompiler(this.configuration.getCompilerClassLoader());
/* 492 */         this.compiledAst = compiler.compile(this.ast);
/* 493 */         if (this.compiledAst == null) {
/* 494 */           this.failedAttempts++;
/*     */         }
/*     */       } 
/*     */     }
/* 498 */     return (this.compiledAst != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void revertToInterpreted() {
/* 507 */     this.compiledAst = null;
/* 508 */     this.interpretedCount = 0;
/* 509 */     this.failedAttempts = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelNode getAST() {
/* 516 */     return (SpelNode)this.ast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 526 */     return this.ast.toStringAST();
/*     */   }
/*     */   
/*     */   private TypedValue toTypedValue(Object object) {
/* 530 */     return (object != null) ? new TypedValue(object) : TypedValue.NULL;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\standard\SpelExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */