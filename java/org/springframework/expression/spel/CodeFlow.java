/*      */ package org.springframework.expression.spel;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Stack;
/*      */ import org.springframework.asm.ClassWriter;
/*      */ import org.springframework.asm.MethodVisitor;
/*      */ import org.springframework.asm.Opcodes;
/*      */ import org.springframework.util.Assert;
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
/*      */ public class CodeFlow
/*      */   implements Opcodes
/*      */ {
/*      */   private final String className;
/*      */   private final ClassWriter classWriter;
/*      */   private final Stack<ArrayList<String>> compilationScopes;
/*      */   private List<FieldAdder> fieldAdders;
/*      */   private List<ClinitAdder> clinitAdders;
/*   80 */   private int nextFieldId = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   private int nextFreeVariableId = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CodeFlow(String className, ClassWriter classWriter) {
/*   95 */     this.className = className;
/*   96 */     this.classWriter = classWriter;
/*   97 */     this.compilationScopes = new Stack<ArrayList<String>>();
/*   98 */     this.compilationScopes.add(new ArrayList<String>());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadTarget(MethodVisitor mv) {
/*  108 */     mv.visitVarInsn(25, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadEvaluationContext(MethodVisitor mv) {
/*  118 */     mv.visitVarInsn(25, 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void pushDescriptor(String descriptor) {
/*  126 */     Assert.notNull(descriptor, "Descriptor must not be null");
/*  127 */     ((ArrayList<String>)this.compilationScopes.peek()).add(descriptor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterCompilationScope() {
/*  136 */     this.compilationScopes.push(new ArrayList<String>());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void exitCompilationScope() {
/*  145 */     this.compilationScopes.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String lastDescriptor() {
/*  152 */     ArrayList<String> scopes = this.compilationScopes.peek();
/*  153 */     return !scopes.isEmpty() ? scopes.get(scopes.size() - 1) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unboxBooleanIfNecessary(MethodVisitor mv) {
/*  162 */     if ("Ljava/lang/Boolean".equals(lastDescriptor())) {
/*  163 */       mv.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void finish() {
/*  173 */     if (this.fieldAdders != null) {
/*  174 */       for (FieldAdder fieldAdder : this.fieldAdders) {
/*  175 */         fieldAdder.generateField(this.classWriter, this);
/*      */       }
/*      */     }
/*  178 */     if (this.clinitAdders != null) {
/*  179 */       MethodVisitor mv = this.classWriter.visitMethod(9, "<clinit>", "()V", null, null);
/*  180 */       mv.visitCode();
/*  181 */       this.nextFreeVariableId = 0;
/*  182 */       for (ClinitAdder clinitAdder : this.clinitAdders) {
/*  183 */         clinitAdder.generateCode(mv, this);
/*      */       }
/*  185 */       mv.visitInsn(177);
/*  186 */       mv.visitMaxs(0, 0);
/*  187 */       mv.visitEnd();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerNewField(FieldAdder fieldAdder) {
/*  197 */     if (this.fieldAdders == null) {
/*  198 */       this.fieldAdders = new ArrayList<FieldAdder>();
/*      */     }
/*  200 */     this.fieldAdders.add(fieldAdder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerNewClinit(ClinitAdder clinitAdder) {
/*  209 */     if (this.clinitAdders == null) {
/*  210 */       this.clinitAdders = new ArrayList<ClinitAdder>();
/*      */     }
/*  212 */     this.clinitAdders.add(clinitAdder);
/*      */   }
/*      */   
/*      */   public int nextFieldId() {
/*  216 */     return this.nextFieldId++;
/*      */   }
/*      */   
/*      */   public int nextFreeVariableId() {
/*  220 */     return this.nextFreeVariableId++;
/*      */   }
/*      */   
/*      */   public String getClassName() {
/*  224 */     return this.className;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public String getClassname() {
/*  229 */     return this.className;
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
/*      */   public static void insertUnboxInsns(MethodVisitor mv, char ch, String stackDescriptor) {
/*  241 */     switch (ch) {
/*      */       case 'Z':
/*  243 */         if (!stackDescriptor.equals("Ljava/lang/Boolean")) {
/*  244 */           mv.visitTypeInsn(192, "java/lang/Boolean");
/*      */         }
/*  246 */         mv.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
/*      */         return;
/*      */       case 'B':
/*  249 */         if (!stackDescriptor.equals("Ljava/lang/Byte")) {
/*  250 */           mv.visitTypeInsn(192, "java/lang/Byte");
/*      */         }
/*  252 */         mv.visitMethodInsn(182, "java/lang/Byte", "byteValue", "()B", false);
/*      */         return;
/*      */       case 'C':
/*  255 */         if (!stackDescriptor.equals("Ljava/lang/Character")) {
/*  256 */           mv.visitTypeInsn(192, "java/lang/Character");
/*      */         }
/*  258 */         mv.visitMethodInsn(182, "java/lang/Character", "charValue", "()C", false);
/*      */         return;
/*      */       case 'D':
/*  261 */         if (!stackDescriptor.equals("Ljava/lang/Double")) {
/*  262 */           mv.visitTypeInsn(192, "java/lang/Double");
/*      */         }
/*  264 */         mv.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
/*      */         return;
/*      */       case 'F':
/*  267 */         if (!stackDescriptor.equals("Ljava/lang/Float")) {
/*  268 */           mv.visitTypeInsn(192, "java/lang/Float");
/*      */         }
/*  270 */         mv.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
/*      */         return;
/*      */       case 'I':
/*  273 */         if (!stackDescriptor.equals("Ljava/lang/Integer")) {
/*  274 */           mv.visitTypeInsn(192, "java/lang/Integer");
/*      */         }
/*  276 */         mv.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
/*      */         return;
/*      */       case 'J':
/*  279 */         if (!stackDescriptor.equals("Ljava/lang/Long")) {
/*  280 */           mv.visitTypeInsn(192, "java/lang/Long");
/*      */         }
/*  282 */         mv.visitMethodInsn(182, "java/lang/Long", "longValue", "()J", false);
/*      */         return;
/*      */       case 'S':
/*  285 */         if (!stackDescriptor.equals("Ljava/lang/Short")) {
/*  286 */           mv.visitTypeInsn(192, "java/lang/Short");
/*      */         }
/*  288 */         mv.visitMethodInsn(182, "java/lang/Short", "shortValue", "()S", false);
/*      */         return;
/*      */     } 
/*  291 */     throw new IllegalArgumentException("Unboxing should not be attempted for descriptor '" + ch + "'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertUnboxNumberInsns(MethodVisitor mv, char targetDescriptor, String stackDescriptor) {
/*  302 */     switch (targetDescriptor) {
/*      */       case 'D':
/*  304 */         if (stackDescriptor.equals("Ljava/lang/Object")) {
/*  305 */           mv.visitTypeInsn(192, "java/lang/Number");
/*      */         }
/*  307 */         mv.visitMethodInsn(182, "java/lang/Number", "doubleValue", "()D", false);
/*      */         return;
/*      */       case 'F':
/*  310 */         if (stackDescriptor.equals("Ljava/lang/Object")) {
/*  311 */           mv.visitTypeInsn(192, "java/lang/Number");
/*      */         }
/*  313 */         mv.visitMethodInsn(182, "java/lang/Number", "floatValue", "()F", false);
/*      */         return;
/*      */       case 'J':
/*  316 */         if (stackDescriptor.equals("Ljava/lang/Object")) {
/*  317 */           mv.visitTypeInsn(192, "java/lang/Number");
/*      */         }
/*  319 */         mv.visitMethodInsn(182, "java/lang/Number", "longValue", "()J", false);
/*      */         return;
/*      */       case 'I':
/*  322 */         if (stackDescriptor.equals("Ljava/lang/Object")) {
/*  323 */           mv.visitTypeInsn(192, "java/lang/Number");
/*      */         }
/*  325 */         mv.visitMethodInsn(182, "java/lang/Number", "intValue", "()I", false);
/*      */         return;
/*      */     } 
/*      */     
/*  329 */     throw new IllegalArgumentException("Unboxing should not be attempted for descriptor '" + targetDescriptor + "'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertAnyNecessaryTypeConversionBytecodes(MethodVisitor mv, char targetDescriptor, String stackDescriptor) {
/*  340 */     if (isPrimitive(stackDescriptor)) {
/*  341 */       char stackTop = stackDescriptor.charAt(0);
/*  342 */       if (stackTop == 'I' || stackTop == 'B' || stackTop == 'S' || stackTop == 'C') {
/*  343 */         if (targetDescriptor == 'D') {
/*  344 */           mv.visitInsn(135);
/*      */         }
/*  346 */         else if (targetDescriptor == 'F') {
/*  347 */           mv.visitInsn(134);
/*      */         }
/*  349 */         else if (targetDescriptor == 'J') {
/*  350 */           mv.visitInsn(133);
/*      */         }
/*  352 */         else if (targetDescriptor != 'I') {
/*      */ 
/*      */ 
/*      */           
/*  356 */           throw new IllegalStateException("Cannot get from " + stackTop + " to " + targetDescriptor);
/*      */         }
/*      */       
/*  359 */       } else if (stackTop == 'J') {
/*  360 */         if (targetDescriptor == 'D') {
/*  361 */           mv.visitInsn(138);
/*      */         }
/*  363 */         else if (targetDescriptor == 'F') {
/*  364 */           mv.visitInsn(137);
/*      */         }
/*  366 */         else if (targetDescriptor != 'J') {
/*      */ 
/*      */           
/*  369 */           if (targetDescriptor == 'I') {
/*  370 */             mv.visitInsn(136);
/*      */           } else {
/*      */             
/*  373 */             throw new IllegalStateException("Cannot get from " + stackTop + " to " + targetDescriptor);
/*      */           } 
/*      */         } 
/*  376 */       } else if (stackTop == 'F') {
/*  377 */         if (targetDescriptor == 'D') {
/*  378 */           mv.visitInsn(141);
/*      */         }
/*  380 */         else if (targetDescriptor != 'F') {
/*      */ 
/*      */           
/*  383 */           if (targetDescriptor == 'J') {
/*  384 */             mv.visitInsn(140);
/*      */           }
/*  386 */           else if (targetDescriptor == 'I') {
/*  387 */             mv.visitInsn(139);
/*      */           } else {
/*      */             
/*  390 */             throw new IllegalStateException("Cannot get from " + stackTop + " to " + targetDescriptor);
/*      */           } 
/*      */         } 
/*  393 */       } else if (stackTop == 'D' && 
/*  394 */         targetDescriptor != 'D') {
/*      */ 
/*      */         
/*  397 */         if (targetDescriptor == 'F') {
/*  398 */           mv.visitInsn(144);
/*      */         }
/*  400 */         else if (targetDescriptor == 'J') {
/*  401 */           mv.visitInsn(143);
/*      */         }
/*  403 */         else if (targetDescriptor == 'I') {
/*  404 */           mv.visitInsn(142);
/*      */         } else {
/*      */           
/*  407 */           throw new IllegalStateException("Cannot get from " + stackDescriptor + " to " + targetDescriptor);
/*      */         } 
/*      */       } 
/*      */     } 
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
/*      */   public static String createSignatureDescriptor(Method method) {
/*  424 */     Class<?>[] params = method.getParameterTypes();
/*  425 */     StringBuilder sb = new StringBuilder();
/*  426 */     sb.append("(");
/*  427 */     for (Class<?> param : params) {
/*  428 */       sb.append(toJvmDescriptor(param));
/*      */     }
/*  430 */     sb.append(")");
/*  431 */     sb.append(toJvmDescriptor(method.getReturnType()));
/*  432 */     return sb.toString();
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
/*      */   public static String createSignatureDescriptor(Constructor<?> ctor) {
/*  445 */     Class<?>[] params = ctor.getParameterTypes();
/*  446 */     StringBuilder sb = new StringBuilder();
/*  447 */     sb.append("(");
/*  448 */     for (Class<?> param : params) {
/*  449 */       sb.append(toJvmDescriptor(param));
/*      */     }
/*  451 */     sb.append(")V");
/*  452 */     return sb.toString();
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
/*      */   public static String toJvmDescriptor(Class<?> clazz) {
/*  464 */     StringBuilder sb = new StringBuilder();
/*  465 */     if (clazz.isArray()) {
/*  466 */       while (clazz.isArray()) {
/*  467 */         sb.append("[");
/*  468 */         clazz = clazz.getComponentType();
/*      */       } 
/*      */     }
/*  471 */     if (clazz.isPrimitive()) {
/*  472 */       if (clazz == boolean.class) {
/*  473 */         sb.append('Z');
/*      */       }
/*  475 */       else if (clazz == byte.class) {
/*  476 */         sb.append('B');
/*      */       }
/*  478 */       else if (clazz == char.class) {
/*  479 */         sb.append('C');
/*      */       }
/*  481 */       else if (clazz == double.class) {
/*  482 */         sb.append('D');
/*      */       }
/*  484 */       else if (clazz == float.class) {
/*  485 */         sb.append('F');
/*      */       }
/*  487 */       else if (clazz == int.class) {
/*  488 */         sb.append('I');
/*      */       }
/*  490 */       else if (clazz == long.class) {
/*  491 */         sb.append('J');
/*      */       }
/*  493 */       else if (clazz == short.class) {
/*  494 */         sb.append('S');
/*      */       }
/*  496 */       else if (clazz == void.class) {
/*  497 */         sb.append('V');
/*      */       } 
/*      */     } else {
/*      */       
/*  501 */       sb.append("L");
/*  502 */       sb.append(clazz.getName().replace('.', '/'));
/*  503 */       sb.append(";");
/*      */     } 
/*  505 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toDescriptorFromObject(Object value) {
/*  515 */     if (value == null) {
/*  516 */       return "Ljava/lang/Object";
/*      */     }
/*      */     
/*  519 */     return toDescriptor(value.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isBooleanCompatible(String descriptor) {
/*  529 */     return (descriptor != null && (descriptor.equals("Z") || descriptor.equals("Ljava/lang/Boolean")));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitive(String descriptor) {
/*  538 */     return (descriptor != null && descriptor.length() == 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveArray(String descriptor) {
/*  547 */     boolean primitive = true;
/*  548 */     for (int i = 0, max = descriptor.length(); i < max; ) {
/*  549 */       char ch = descriptor.charAt(i);
/*  550 */       if (ch == '[') {
/*      */         i++; continue;
/*      */       } 
/*  553 */       primitive = (ch != 'L');
/*      */     } 
/*      */     
/*  556 */     return primitive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean areBoxingCompatible(String desc1, String desc2) {
/*  565 */     if (desc1.equals(desc2)) {
/*  566 */       return true;
/*      */     }
/*  568 */     if (desc1.length() == 1) {
/*  569 */       if (desc1.equals("Z")) {
/*  570 */         return desc2.equals("Ljava/lang/Boolean");
/*      */       }
/*  572 */       if (desc1.equals("D")) {
/*  573 */         return desc2.equals("Ljava/lang/Double");
/*      */       }
/*  575 */       if (desc1.equals("F")) {
/*  576 */         return desc2.equals("Ljava/lang/Float");
/*      */       }
/*  578 */       if (desc1.equals("I")) {
/*  579 */         return desc2.equals("Ljava/lang/Integer");
/*      */       }
/*  581 */       if (desc1.equals("J")) {
/*  582 */         return desc2.equals("Ljava/lang/Long");
/*      */       }
/*      */     }
/*  585 */     else if (desc2.length() == 1) {
/*  586 */       if (desc2.equals("Z")) {
/*  587 */         return desc1.equals("Ljava/lang/Boolean");
/*      */       }
/*  589 */       if (desc2.equals("D")) {
/*  590 */         return desc1.equals("Ljava/lang/Double");
/*      */       }
/*  592 */       if (desc2.equals("F")) {
/*  593 */         return desc1.equals("Ljava/lang/Float");
/*      */       }
/*  595 */       if (desc2.equals("I")) {
/*  596 */         return desc1.equals("Ljava/lang/Integer");
/*      */       }
/*  598 */       if (desc2.equals("J")) {
/*  599 */         return desc1.equals("Ljava/lang/Long");
/*      */       }
/*      */     } 
/*  602 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveOrUnboxableSupportedNumberOrBoolean(String descriptor) {
/*  613 */     if (descriptor == null) {
/*  614 */       return false;
/*      */     }
/*  616 */     if (isPrimitiveOrUnboxableSupportedNumber(descriptor)) {
/*  617 */       return true;
/*      */     }
/*  619 */     return ("Z".equals(descriptor) || descriptor.equals("Ljava/lang/Boolean"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveOrUnboxableSupportedNumber(String descriptor) {
/*  630 */     if (descriptor == null) {
/*  631 */       return false;
/*      */     }
/*  633 */     if (descriptor.length() == 1) {
/*  634 */       return "DFIJ".contains(descriptor);
/*      */     }
/*  636 */     if (descriptor.startsWith("Ljava/lang/")) {
/*  637 */       String name = descriptor.substring("Ljava/lang/".length());
/*  638 */       if (name.equals("Double") || name.equals("Float") || name.equals("Integer") || name.equals("Long")) {
/*  639 */         return true;
/*      */       }
/*      */     } 
/*  642 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isIntegerForNumericOp(Number number) {
/*  652 */     return (number instanceof Integer || number instanceof Short || number instanceof Byte);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char toPrimitiveTargetDesc(String descriptor) {
/*  661 */     if (descriptor.length() == 1) {
/*  662 */       return descriptor.charAt(0);
/*      */     }
/*  664 */     if (descriptor.equals("Ljava/lang/Boolean")) {
/*  665 */       return 'Z';
/*      */     }
/*  667 */     if (descriptor.equals("Ljava/lang/Byte")) {
/*  668 */       return 'B';
/*      */     }
/*  670 */     if (descriptor.equals("Ljava/lang/Character")) {
/*  671 */       return 'C';
/*      */     }
/*  673 */     if (descriptor.equals("Ljava/lang/Double")) {
/*  674 */       return 'D';
/*      */     }
/*  676 */     if (descriptor.equals("Ljava/lang/Float")) {
/*  677 */       return 'F';
/*      */     }
/*  679 */     if (descriptor.equals("Ljava/lang/Integer")) {
/*  680 */       return 'I';
/*      */     }
/*  682 */     if (descriptor.equals("Ljava/lang/Long")) {
/*  683 */       return 'J';
/*      */     }
/*  685 */     if (descriptor.equals("Ljava/lang/Short")) {
/*  686 */       return 'S';
/*      */     }
/*      */     
/*  689 */     throw new IllegalStateException("No primitive for '" + descriptor + "'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertCheckCast(MethodVisitor mv, String descriptor) {
/*  699 */     if (descriptor.length() != 1) {
/*  700 */       if (descriptor.charAt(0) == '[') {
/*  701 */         if (isPrimitiveArray(descriptor)) {
/*  702 */           mv.visitTypeInsn(192, descriptor);
/*      */         } else {
/*      */           
/*  705 */           mv.visitTypeInsn(192, descriptor + ";");
/*      */         }
/*      */       
/*      */       }
/*  709 */       else if (!descriptor.equals("Ljava/lang/Object")) {
/*      */         
/*  711 */         mv.visitTypeInsn(192, descriptor.substring(1));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertBoxIfNecessary(MethodVisitor mv, String descriptor) {
/*  724 */     if (descriptor.length() == 1) {
/*  725 */       insertBoxIfNecessary(mv, descriptor.charAt(0));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertBoxIfNecessary(MethodVisitor mv, char ch) {
/*  736 */     switch (ch) {
/*      */       case 'Z':
/*  738 */         mv.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
/*      */       
/*      */       case 'B':
/*  741 */         mv.visitMethodInsn(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
/*      */       
/*      */       case 'C':
/*  744 */         mv.visitMethodInsn(184, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
/*      */       
/*      */       case 'D':
/*  747 */         mv.visitMethodInsn(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
/*      */       
/*      */       case 'F':
/*  750 */         mv.visitMethodInsn(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
/*      */       
/*      */       case 'I':
/*  753 */         mv.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
/*      */       
/*      */       case 'J':
/*  756 */         mv.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
/*      */       
/*      */       case 'S':
/*  759 */         mv.visitMethodInsn(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
/*      */       
/*      */       case 'L':
/*      */       case 'V':
/*      */       case '[':
/*      */         return;
/*      */     } 
/*      */     
/*  767 */     throw new IllegalArgumentException("Boxing should not be attempted for descriptor '" + ch + "'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toDescriptor(Class<?> type) {
/*  778 */     String name = type.getName();
/*  779 */     if (type.isPrimitive()) {
/*  780 */       switch (name.length()) {
/*      */         case 3:
/*  782 */           return "I";
/*      */         case 4:
/*  784 */           if (name.equals("byte")) {
/*  785 */             return "B";
/*      */           }
/*  787 */           if (name.equals("char")) {
/*  788 */             return "C";
/*      */           }
/*  790 */           if (name.equals("long")) {
/*  791 */             return "J";
/*      */           }
/*  793 */           if (name.equals("void")) {
/*  794 */             return "V";
/*      */           }
/*      */           break;
/*      */         case 5:
/*  798 */           if (name.equals("float")) {
/*  799 */             return "F";
/*      */           }
/*  801 */           if (name.equals("short")) {
/*  802 */             return "S";
/*      */           }
/*      */           break;
/*      */         case 6:
/*  806 */           if (name.equals("double")) {
/*  807 */             return "D";
/*      */           }
/*      */           break;
/*      */         case 7:
/*  811 */           if (name.equals("boolean")) {
/*  812 */             return "Z";
/*      */           }
/*      */           break;
/*      */       } 
/*      */     
/*      */     } else {
/*  818 */       if (name.charAt(0) != '[') {
/*  819 */         return "L" + type.getName().replace('.', '/');
/*      */       }
/*      */       
/*  822 */       if (name.endsWith(";")) {
/*  823 */         return name.substring(0, name.length() - 1).replace('.', '/');
/*      */       }
/*      */       
/*  826 */       return name;
/*      */     } 
/*      */ 
/*      */     
/*  830 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] toParamDescriptors(Method method) {
/*  840 */     return toDescriptors(method.getParameterTypes());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] toParamDescriptors(Constructor<?> ctor) {
/*  850 */     return toDescriptors(ctor.getParameterTypes());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] toDescriptors(Class<?>[] types) {
/*  859 */     int typesCount = types.length;
/*  860 */     String[] descriptors = new String[typesCount];
/*  861 */     for (int p = 0; p < typesCount; p++) {
/*  862 */       descriptors[p] = toDescriptor(types[p]);
/*      */     }
/*  864 */     return descriptors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertOptimalLoad(MethodVisitor mv, int value) {
/*  873 */     if (value < 6) {
/*  874 */       mv.visitInsn(3 + value);
/*      */     }
/*  876 */     else if (value < 127) {
/*  877 */       mv.visitIntInsn(16, value);
/*      */     }
/*  879 */     else if (value < 32767) {
/*  880 */       mv.visitIntInsn(17, value);
/*      */     } else {
/*      */       
/*  883 */       mv.visitLdcInsn(Integer.valueOf(value));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void insertArrayStore(MethodVisitor mv, String arrayElementType) {
/*  895 */     if (arrayElementType.length() == 1) {
/*  896 */       switch (arrayElementType.charAt(0)) {
/*      */         case 'I':
/*  898 */           mv.visitInsn(79);
/*      */           return;
/*      */         case 'J':
/*  901 */           mv.visitInsn(80);
/*      */           return;
/*      */         case 'F':
/*  904 */           mv.visitInsn(81);
/*      */           return;
/*      */         case 'D':
/*  907 */           mv.visitInsn(82);
/*      */           return;
/*      */         case 'B':
/*  910 */           mv.visitInsn(84);
/*      */           return;
/*      */         case 'C':
/*  913 */           mv.visitInsn(85);
/*      */           return;
/*      */         case 'S':
/*  916 */           mv.visitInsn(86);
/*      */           return;
/*      */         case 'Z':
/*  919 */           mv.visitInsn(84);
/*      */           return;
/*      */       } 
/*  922 */       throw new IllegalArgumentException("Unexpected arraytype " + arrayElementType
/*  923 */           .charAt(0));
/*      */     } 
/*      */ 
/*      */     
/*  927 */     mv.visitInsn(83);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int arrayCodeFor(String arraytype) {
/*  937 */     switch (arraytype.charAt(0)) { case 'I':
/*  938 */         return 10;
/*  939 */       case 'J': return 11;
/*  940 */       case 'F': return 6;
/*  941 */       case 'D': return 7;
/*  942 */       case 'B': return 8;
/*  943 */       case 'C': return 5;
/*  944 */       case 'S': return 9;
/*  945 */       case 'Z': return 4; }
/*      */     
/*  947 */     throw new IllegalArgumentException("Unexpected arraytype " + arraytype.charAt(0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isReferenceTypeArray(String arraytype) {
/*  955 */     int length = arraytype.length();
/*  956 */     for (int i = 0; i < length; ) {
/*  957 */       char ch = arraytype.charAt(i);
/*  958 */       if (ch == '[') {
/*      */         i++; continue;
/*      */       } 
/*  961 */       return (ch == 'L');
/*      */     } 
/*  963 */     return false;
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
/*      */   public static void insertNewArrayCode(MethodVisitor mv, int size, String arraytype) {
/*  975 */     insertOptimalLoad(mv, size);
/*  976 */     if (arraytype.length() == 1) {
/*  977 */       mv.visitIntInsn(188, arrayCodeFor(arraytype));
/*      */     
/*      */     }
/*  980 */     else if (arraytype.charAt(0) == '[') {
/*      */ 
/*      */       
/*  983 */       if (isReferenceTypeArray(arraytype)) {
/*  984 */         mv.visitTypeInsn(189, arraytype + ";");
/*      */       } else {
/*      */         
/*  987 */         mv.visitTypeInsn(189, arraytype);
/*      */       } 
/*      */     } else {
/*      */       
/*  991 */       mv.visitTypeInsn(189, arraytype.substring(1));
/*      */     } 
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
/*      */   
/*      */   public static void insertNumericUnboxOrPrimitiveTypeCoercion(MethodVisitor mv, String stackDescriptor, char targetDescriptor) {
/* 1008 */     if (!isPrimitive(stackDescriptor)) {
/* 1009 */       insertUnboxNumberInsns(mv, targetDescriptor, stackDescriptor);
/*      */     } else {
/*      */       
/* 1012 */       insertAnyNecessaryTypeConversionBytecodes(mv, targetDescriptor, stackDescriptor);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String toBoxedDescriptor(String primitiveDescriptor) {
/* 1017 */     switch (primitiveDescriptor.charAt(0)) { case 'I':
/* 1018 */         return "Ljava/lang/Integer";
/* 1019 */       case 'J': return "Ljava/lang/Long";
/* 1020 */       case 'F': return "Ljava/lang/Float";
/* 1021 */       case 'D': return "Ljava/lang/Double";
/* 1022 */       case 'B': return "Ljava/lang/Byte";
/* 1023 */       case 'C': return "Ljava/lang/Character";
/* 1024 */       case 'S': return "Ljava/lang/Short";
/* 1025 */       case 'Z': return "Ljava/lang/Boolean"; }
/*      */     
/* 1027 */     throw new IllegalArgumentException("Unexpected non primitive descriptor " + primitiveDescriptor);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface ClinitAdder {
/*      */     void generateCode(MethodVisitor param1MethodVisitor, CodeFlow param1CodeFlow);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface FieldAdder {
/*      */     void generateField(ClassWriter param1ClassWriter, CodeFlow param1CodeFlow);
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\CodeFlow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */