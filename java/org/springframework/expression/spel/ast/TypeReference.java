/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
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
/*     */ public class TypeReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final int dimensions;
/*     */   private transient Class<?> type;
/*     */   
/*     */   public TypeReference(int pos, SpelNodeImpl qualifiedId) {
/*  41 */     this(pos, qualifiedId, 0);
/*     */   }
/*     */   
/*     */   public TypeReference(int pos, SpelNodeImpl qualifiedId, int dims) {
/*  45 */     super(pos, new SpelNodeImpl[] { qualifiedId });
/*  46 */     this.dimensions = dims;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  53 */     String typeName = (String)this.children[0].getValueInternal(state).getValue();
/*  54 */     if (!typeName.contains(".") && Character.isLowerCase(typeName.charAt(0))) {
/*  55 */       TypeCode tc = TypeCode.valueOf(typeName.toUpperCase());
/*  56 */       if (tc != TypeCode.OBJECT) {
/*     */         
/*  58 */         Class<?> clazz1 = makeArrayIfNecessary(tc.getType());
/*  59 */         this.exitTypeDescriptor = "Ljava/lang/Class";
/*  60 */         this.type = clazz1;
/*  61 */         return new TypedValue(clazz1);
/*     */       } 
/*     */     } 
/*  64 */     Class<?> clazz = state.findType(typeName);
/*  65 */     clazz = makeArrayIfNecessary(clazz);
/*  66 */     this.exitTypeDescriptor = "Ljava/lang/Class";
/*  67 */     this.type = clazz;
/*  68 */     return new TypedValue(clazz);
/*     */   }
/*     */   
/*     */   private Class<?> makeArrayIfNecessary(Class<?> clazz) {
/*  72 */     if (this.dimensions != 0) {
/*  73 */       for (int i = 0; i < this.dimensions; i++) {
/*  74 */         Object array = Array.newInstance(clazz, 0);
/*  75 */         clazz = array.getClass();
/*     */       } 
/*     */     }
/*  78 */     return clazz;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  83 */     StringBuilder sb = new StringBuilder("T(");
/*  84 */     sb.append(getChild(0).toStringAST());
/*  85 */     for (int d = 0; d < this.dimensions; d++) {
/*  86 */       sb.append("[]");
/*     */     }
/*  88 */     sb.append(")");
/*  89 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  94 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 100 */     if (this.type.isPrimitive()) {
/* 101 */       if (this.type == boolean.class) {
/* 102 */         mv.visitFieldInsn(178, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 104 */       else if (this.type == byte.class) {
/* 105 */         mv.visitFieldInsn(178, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 107 */       else if (this.type == char.class) {
/* 108 */         mv.visitFieldInsn(178, "java/lang/Character", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 110 */       else if (this.type == double.class) {
/* 111 */         mv.visitFieldInsn(178, "java/lang/Double", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 113 */       else if (this.type == float.class) {
/* 114 */         mv.visitFieldInsn(178, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 116 */       else if (this.type == int.class) {
/* 117 */         mv.visitFieldInsn(178, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 119 */       else if (this.type == long.class) {
/* 120 */         mv.visitFieldInsn(178, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 122 */       else if (this.type == short.class) {
/* 123 */         mv.visitFieldInsn(178, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
/*     */       } 
/*     */     } else {
/*     */       
/* 127 */       mv.visitLdcInsn(Type.getType(this.type));
/*     */     } 
/* 129 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\TypeReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */