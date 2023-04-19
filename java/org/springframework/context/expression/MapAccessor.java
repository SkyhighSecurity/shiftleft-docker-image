/*     */ package org.springframework.context.expression;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompilablePropertyAccessor;
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
/*     */ public class MapAccessor
/*     */   implements CompilablePropertyAccessor
/*     */ {
/*     */   public Class<?>[] getSpecificTargetClasses() {
/*  40 */     return new Class[] { Map.class };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
/*  45 */     Map<?, ?> map = (Map<?, ?>)target;
/*  46 */     return map.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
/*  51 */     Map<?, ?> map = (Map<?, ?>)target;
/*  52 */     Object value = map.get(name);
/*  53 */     if (value == null && !map.containsKey(name)) {
/*  54 */       throw new MapAccessException(name);
/*     */     }
/*  56 */     return new TypedValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
/*  61 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
/*  67 */     Map<Object, Object> map = (Map<Object, Object>)target;
/*  68 */     map.put(name, newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getPropertyType() {
/*  78 */     return Object.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
/*  83 */     String descriptor = cf.lastDescriptor();
/*  84 */     if (descriptor == null || !descriptor.equals("Ljava/util/Map")) {
/*  85 */       if (descriptor == null) {
/*  86 */         cf.loadTarget(mv);
/*     */       }
/*  88 */       CodeFlow.insertCheckCast(mv, "Ljava/util/Map");
/*     */     } 
/*  90 */     mv.visitLdcInsn(propertyName);
/*  91 */     mv.visitMethodInsn(185, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MapAccessException
/*     */     extends AccessException
/*     */   {
/*     */     private final String key;
/*     */ 
/*     */ 
/*     */     
/*     */     public MapAccessException(String key) {
/* 105 */       super(null);
/* 106 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 111 */       return "Map does not contain a value for key '" + this.key + "'";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\expression\MapAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */