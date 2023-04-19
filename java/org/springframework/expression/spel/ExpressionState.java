/*     */ package org.springframework.expression.spel;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Operation;
/*     */ import org.springframework.expression.OperatorOverloader;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExpressionState
/*     */ {
/*     */   private final EvaluationContext relatedContext;
/*     */   private final TypedValue rootObject;
/*     */   private final SpelParserConfiguration configuration;
/*     */   private Stack<TypedValue> contextObjects;
/*     */   private Stack<VariableScope> variableScopes;
/*     */   private Stack<TypedValue> scopeRootObjects;
/*     */   
/*     */   public ExpressionState(EvaluationContext context) {
/*  74 */     this(context, context.getRootObject(), new SpelParserConfiguration(false, false));
/*     */   }
/*     */   
/*     */   public ExpressionState(EvaluationContext context, SpelParserConfiguration configuration) {
/*  78 */     this(context, context.getRootObject(), configuration);
/*     */   }
/*     */   
/*     */   public ExpressionState(EvaluationContext context, TypedValue rootObject) {
/*  82 */     this(context, rootObject, new SpelParserConfiguration(false, false));
/*     */   }
/*     */   
/*     */   public ExpressionState(EvaluationContext context, TypedValue rootObject, SpelParserConfiguration configuration) {
/*  86 */     Assert.notNull(context, "EvaluationContext must not be null");
/*  87 */     Assert.notNull(configuration, "SpelParserConfiguration must not be null");
/*  88 */     this.relatedContext = context;
/*  89 */     this.rootObject = rootObject;
/*  90 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */   
/*     */   private void ensureVariableScopesInitialized() {
/*  95 */     if (this.variableScopes == null) {
/*  96 */       this.variableScopes = new Stack<VariableScope>();
/*     */       
/*  98 */       this.variableScopes.add(new VariableScope());
/*     */     } 
/* 100 */     if (this.scopeRootObjects == null) {
/* 101 */       this.scopeRootObjects = new Stack<TypedValue>();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getActiveContextObject() {
/* 109 */     if (CollectionUtils.isEmpty(this.contextObjects)) {
/* 110 */       return this.rootObject;
/*     */     }
/* 112 */     return this.contextObjects.peek();
/*     */   }
/*     */   
/*     */   public void pushActiveContextObject(TypedValue obj) {
/* 116 */     if (this.contextObjects == null) {
/* 117 */       this.contextObjects = new Stack<TypedValue>();
/*     */     }
/* 119 */     this.contextObjects.push(obj);
/*     */   }
/*     */   
/*     */   public void popActiveContextObject() {
/* 123 */     if (this.contextObjects == null) {
/* 124 */       this.contextObjects = new Stack<TypedValue>();
/*     */     }
/* 126 */     this.contextObjects.pop();
/*     */   }
/*     */   
/*     */   public TypedValue getRootContextObject() {
/* 130 */     return this.rootObject;
/*     */   }
/*     */   
/*     */   public TypedValue getScopeRootContextObject() {
/* 134 */     if (CollectionUtils.isEmpty(this.scopeRootObjects)) {
/* 135 */       return this.rootObject;
/*     */     }
/* 137 */     return this.scopeRootObjects.peek();
/*     */   }
/*     */   
/*     */   public void setVariable(String name, Object value) {
/* 141 */     this.relatedContext.setVariable(name, value);
/*     */   }
/*     */   
/*     */   public TypedValue lookupVariable(String name) {
/* 145 */     Object value = this.relatedContext.lookupVariable(name);
/* 146 */     return (value != null) ? new TypedValue(value) : TypedValue.NULL;
/*     */   }
/*     */   
/*     */   public TypeComparator getTypeComparator() {
/* 150 */     return this.relatedContext.getTypeComparator();
/*     */   }
/*     */   
/*     */   public Class<?> findType(String type) throws EvaluationException {
/* 154 */     return this.relatedContext.getTypeLocator().findType(type);
/*     */   }
/*     */   
/*     */   public Object convertValue(Object value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
/* 158 */     return this.relatedContext.getTypeConverter().convertValue(value, 
/* 159 */         TypeDescriptor.forObject(value), targetTypeDescriptor);
/*     */   }
/*     */   
/*     */   public TypeConverter getTypeConverter() {
/* 163 */     return this.relatedContext.getTypeConverter();
/*     */   }
/*     */   
/*     */   public Object convertValue(TypedValue value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
/* 167 */     Object val = value.getValue();
/* 168 */     return this.relatedContext.getTypeConverter().convertValue(val, TypeDescriptor.forObject(val), targetTypeDescriptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterScope(Map<String, Object> argMap) {
/* 175 */     ensureVariableScopesInitialized();
/* 176 */     this.variableScopes.push(new VariableScope(argMap));
/* 177 */     this.scopeRootObjects.push(getActiveContextObject());
/*     */   }
/*     */   
/*     */   public void enterScope() {
/* 181 */     ensureVariableScopesInitialized();
/* 182 */     this.variableScopes.push(new VariableScope(Collections.emptyMap()));
/* 183 */     this.scopeRootObjects.push(getActiveContextObject());
/*     */   }
/*     */   
/*     */   public void enterScope(String name, Object value) {
/* 187 */     ensureVariableScopesInitialized();
/* 188 */     this.variableScopes.push(new VariableScope(name, value));
/* 189 */     this.scopeRootObjects.push(getActiveContextObject());
/*     */   }
/*     */   
/*     */   public void exitScope() {
/* 193 */     ensureVariableScopesInitialized();
/* 194 */     this.variableScopes.pop();
/* 195 */     this.scopeRootObjects.pop();
/*     */   }
/*     */   
/*     */   public void setLocalVariable(String name, Object value) {
/* 199 */     ensureVariableScopesInitialized();
/* 200 */     ((VariableScope)this.variableScopes.peek()).setVariable(name, value);
/*     */   }
/*     */   
/*     */   public Object lookupLocalVariable(String name) {
/* 204 */     ensureVariableScopesInitialized();
/* 205 */     int scopeNumber = this.variableScopes.size() - 1;
/* 206 */     for (int i = scopeNumber; i >= 0; i--) {
/* 207 */       if (((VariableScope)this.variableScopes.get(i)).definesVariable(name)) {
/* 208 */         return ((VariableScope)this.variableScopes.get(i)).lookupVariable(name);
/*     */       }
/*     */     } 
/* 211 */     return null;
/*     */   }
/*     */   
/*     */   public TypedValue operate(Operation op, Object left, Object right) throws EvaluationException {
/* 215 */     OperatorOverloader overloader = this.relatedContext.getOperatorOverloader();
/* 216 */     if (overloader.overridesOperation(op, left, right)) {
/* 217 */       Object returnValue = overloader.operate(op, left, right);
/* 218 */       return new TypedValue(returnValue);
/*     */     } 
/*     */     
/* 221 */     String leftType = (left == null) ? "null" : left.getClass().getName();
/* 222 */     String rightType = (right == null) ? "null" : right.getClass().getName();
/* 223 */     throw new SpelEvaluationException(SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, new Object[] { op, leftType, rightType });
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PropertyAccessor> getPropertyAccessors() {
/* 228 */     return this.relatedContext.getPropertyAccessors();
/*     */   }
/*     */   
/*     */   public EvaluationContext getEvaluationContext() {
/* 232 */     return this.relatedContext;
/*     */   }
/*     */   
/*     */   public SpelParserConfiguration getConfiguration() {
/* 236 */     return this.configuration;
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
/*     */   private static class VariableScope
/*     */   {
/* 249 */     private final Map<String, Object> vars = new HashMap<String, Object>();
/*     */ 
/*     */     
/*     */     public VariableScope() {}
/*     */     
/*     */     public VariableScope(Map<String, Object> arguments) {
/* 255 */       if (arguments != null) {
/* 256 */         this.vars.putAll(arguments);
/*     */       }
/*     */     }
/*     */     
/*     */     public VariableScope(String name, Object value) {
/* 261 */       this.vars.put(name, value);
/*     */     }
/*     */     
/*     */     public Object lookupVariable(String name) {
/* 265 */       return this.vars.get(name);
/*     */     }
/*     */     
/*     */     public void setVariable(String name, Object value) {
/* 269 */       this.vars.put(name, value);
/*     */     }
/*     */     
/*     */     public boolean definesVariable(String name) {
/* 273 */       return this.vars.containsKey(name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ExpressionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */