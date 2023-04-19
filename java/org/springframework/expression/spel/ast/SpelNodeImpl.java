/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Opcodes;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
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
/*     */ public abstract class SpelNodeImpl
/*     */   implements SpelNode, Opcodes
/*     */ {
/*  46 */   private static final SpelNodeImpl[] NO_CHILDREN = new SpelNodeImpl[0];
/*     */ 
/*     */   
/*     */   protected int pos;
/*     */   
/*  51 */   protected SpelNodeImpl[] children = NO_CHILDREN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SpelNodeImpl parent;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile String exitTypeDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelNodeImpl(int pos, SpelNodeImpl... operands) {
/*  68 */     this.pos = pos;
/*     */     
/*  70 */     Assert.isTrue((pos != 0), "Pos must not be 0");
/*  71 */     if (!ObjectUtils.isEmpty((Object[])operands)) {
/*  72 */       this.children = operands;
/*  73 */       for (SpelNodeImpl childNode : operands) {
/*  74 */         childNode.parent = this;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SpelNodeImpl getPreviousChild() {
/*  82 */     SpelNodeImpl result = null;
/*  83 */     if (this.parent != null) {
/*  84 */       for (SpelNodeImpl child : this.parent.children) {
/*  85 */         if (this == child) {
/*     */           break;
/*     */         }
/*  88 */         result = child;
/*     */       } 
/*     */     }
/*  91 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean nextChildIs(Class<?>... clazzes) {
/*  98 */     if (this.parent != null) {
/*  99 */       SpelNodeImpl[] peers = this.parent.children;
/* 100 */       for (int i = 0, max = peers.length; i < max; i++) {
/* 101 */         if (this == peers[i]) {
/* 102 */           if (i + 1 >= max) {
/* 103 */             return false;
/*     */           }
/* 105 */           Class<?> clazz = peers[i + 1].getClass();
/* 106 */           for (Class<?> desiredClazz : clazzes) {
/* 107 */             if (clazz.equals(desiredClazz)) {
/* 108 */               return true;
/*     */             }
/*     */           } 
/* 111 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object getValue(ExpressionState expressionState) throws EvaluationException {
/* 120 */     if (expressionState != null) {
/* 121 */       return getValueInternal(expressionState).getValue();
/*     */     }
/*     */ 
/*     */     
/* 125 */     return getValue(new ExpressionState((EvaluationContext)new StandardEvaluationContext()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypedValue getTypedValue(ExpressionState expressionState) throws EvaluationException {
/* 131 */     if (expressionState != null) {
/* 132 */       return getValueInternal(expressionState);
/*     */     }
/*     */ 
/*     */     
/* 136 */     return getTypedValue(new ExpressionState((EvaluationContext)new StandardEvaluationContext()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState expressionState) throws EvaluationException {
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState expressionState, Object newValue) throws EvaluationException {
/* 148 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.SETVALUE_NOT_SUPPORTED, new Object[] {
/* 149 */           getClass()
/*     */         });
/*     */   }
/*     */   
/*     */   public SpelNode getChild(int index) {
/* 154 */     return this.children[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChildCount() {
/* 159 */     return this.children.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectClass(Object obj) {
/* 164 */     if (obj == null) {
/* 165 */       return null;
/*     */     }
/* 167 */     return (obj instanceof Class) ? (Class)obj : obj.getClass();
/*     */   }
/*     */   
/*     */   protected final <T> T getValue(ExpressionState state, Class<T> desiredReturnType) throws EvaluationException {
/* 171 */     return (T)ExpressionUtils.convertTypedValue(state.getEvaluationContext(), getValueInternal(state), desiredReturnType);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartPosition() {
/* 176 */     return this.pos >> 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndPosition() {
/* 181 */     return this.pos & 0xFFFF;
/*     */   }
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/* 185 */     throw new SpelEvaluationException(this.pos, SpelMessage.NOT_ASSIGNABLE, new Object[] { toStringAST() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 195 */     return false;
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
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 207 */     throw new IllegalStateException(getClass().getName() + " has no generateCode(..) method");
/*     */   }
/*     */   
/*     */   public String getExitDescriptor() {
/* 211 */     return this.exitTypeDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypedValue getValueInternal(ExpressionState paramExpressionState) throws EvaluationException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void generateCodeForArguments(MethodVisitor mv, CodeFlow cf, Member member, SpelNodeImpl[] arguments) {
/* 227 */     String[] paramDescriptors = null;
/* 228 */     boolean isVarargs = false;
/* 229 */     if (member instanceof Constructor) {
/* 230 */       Constructor<?> ctor = (Constructor)member;
/* 231 */       paramDescriptors = CodeFlow.toDescriptors(ctor.getParameterTypes());
/* 232 */       isVarargs = ctor.isVarArgs();
/*     */     } else {
/*     */       
/* 235 */       Method method = (Method)member;
/* 236 */       paramDescriptors = CodeFlow.toDescriptors(method.getParameterTypes());
/* 237 */       isVarargs = method.isVarArgs();
/*     */     } 
/* 239 */     if (isVarargs) {
/*     */ 
/*     */       
/* 242 */       int p = 0;
/* 243 */       int childCount = arguments.length;
/*     */ 
/*     */       
/* 246 */       for (p = 0; p < paramDescriptors.length - 1; p++) {
/* 247 */         generateCodeForArgument(mv, cf, arguments[p], paramDescriptors[p]);
/*     */       }
/*     */       
/* 250 */       SpelNodeImpl lastChild = (childCount == 0) ? null : arguments[childCount - 1];
/* 251 */       String arrayType = paramDescriptors[paramDescriptors.length - 1];
/*     */ 
/*     */       
/* 254 */       if (lastChild != null && arrayType.equals(lastChild.getExitDescriptor())) {
/* 255 */         generateCodeForArgument(mv, cf, lastChild, paramDescriptors[p]);
/*     */       } else {
/*     */         
/* 258 */         arrayType = arrayType.substring(1);
/*     */         
/* 260 */         CodeFlow.insertNewArrayCode(mv, childCount - p, arrayType);
/*     */         
/* 262 */         int arrayindex = 0;
/* 263 */         while (p < childCount) {
/* 264 */           SpelNodeImpl child = arguments[p];
/* 265 */           mv.visitInsn(89);
/* 266 */           CodeFlow.insertOptimalLoad(mv, arrayindex++);
/* 267 */           generateCodeForArgument(mv, cf, child, arrayType);
/* 268 */           CodeFlow.insertArrayStore(mv, arrayType);
/* 269 */           p++;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 274 */       for (int i = 0; i < paramDescriptors.length; i++) {
/* 275 */         generateCodeForArgument(mv, cf, arguments[i], paramDescriptors[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void generateCodeForArgument(MethodVisitor mv, CodeFlow cf, SpelNodeImpl argument, String paramDesc) {
/* 285 */     cf.enterCompilationScope();
/* 286 */     argument.generateCode(mv, cf);
/* 287 */     String lastDesc = cf.lastDescriptor();
/* 288 */     boolean primitiveOnStack = CodeFlow.isPrimitive(lastDesc);
/*     */     
/* 290 */     if (primitiveOnStack && paramDesc.charAt(0) == 'L') {
/* 291 */       CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*     */     }
/* 293 */     else if (paramDesc.length() == 1 && !primitiveOnStack) {
/* 294 */       CodeFlow.insertUnboxInsns(mv, paramDesc.charAt(0), lastDesc);
/*     */     }
/* 296 */     else if (!paramDesc.equals(lastDesc)) {
/*     */       
/* 298 */       CodeFlow.insertCheckCast(mv, paramDesc);
/*     */     } 
/* 300 */     cf.exitCompilationScope();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\SpelNodeImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */