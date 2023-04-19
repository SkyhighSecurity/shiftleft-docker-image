/*     */ package lombok.launch;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import lombok.eclipse.EclipseAugments;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.jdt.core.IAnnotatable;
/*     */ import org.eclipse.jdt.core.IAnnotation;
/*     */ import org.eclipse.jdt.core.IField;
/*     */ import org.eclipse.jdt.core.IMethod;
/*     */ import org.eclipse.jdt.core.IType;
/*     */ import org.eclipse.jdt.core.JavaModelException;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
/*     */ import org.eclipse.jdt.core.dom.Annotation;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*     */ import org.eclipse.jdt.core.dom.Name;
/*     */ import org.eclipse.jdt.core.dom.NormalAnnotation;
/*     */ import org.eclipse.jdt.core.dom.SimpleName;
/*     */ import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
/*     */ import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
/*     */ import org.eclipse.jdt.core.search.SearchMatch;
/*     */ import org.eclipse.jdt.internal.compiler.ast.ASTNode;
/*     */ import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
/*     */ import org.eclipse.jdt.internal.compiler.ast.Annotation;
/*     */ import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
/*     */ import org.eclipse.jdt.internal.compiler.ast.Expression;
/*     */ import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
/*     */ import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
/*     */ import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
/*     */ import org.eclipse.jdt.internal.compiler.ast.MessageSend;
/*     */ import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
/*     */ import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
/*     */ import org.eclipse.jdt.internal.compiler.lookup.Scope;
/*     */ import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
/*     */ import org.eclipse.jdt.internal.compiler.parser.Parser;
/*     */ import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
/*     */ import org.eclipse.jdt.internal.core.SourceField;
/*     */ import org.eclipse.jdt.internal.core.dom.rewrite.NodeRewriteEvent;
/*     */ import org.eclipse.jdt.internal.core.dom.rewrite.RewriteEvent;
/*     */ import org.eclipse.jdt.internal.core.dom.rewrite.TokenScanner;
/*     */ import org.eclipse.jdt.internal.corext.refactoring.SearchResultGroup;
/*     */ import org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil;
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
/*     */ final class PatchFixesHider
/*     */ {
/*     */   public static final class Util
/*     */   {
/*     */     private static ClassLoader shadowLoader;
/*     */     
/*     */     public static Class<?> shadowLoadClass(String name) {
/*     */       try {
/*  88 */         if (shadowLoader == null) {
/*     */           try {
/*  90 */             Class.forName("lombok.core.LombokNode");
/*     */             
/*  92 */             shadowLoader = Util.class.getClassLoader();
/*  93 */           } catch (ClassNotFoundException classNotFoundException) {
/*     */             
/*  95 */             shadowLoader = Main.getShadowClassLoader();
/*     */           } 
/*     */         }
/*     */         
/*  99 */         return Class.forName(name, true, shadowLoader);
/* 100 */       } catch (ClassNotFoundException e) {
/* 101 */         throw sneakyThrow(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public static Method findMethod(Class<?> type, String name, Class... parameterTypes) {
/*     */       try {
/* 107 */         return type.getDeclaredMethod(name, parameterTypes);
/* 108 */       } catch (NoSuchMethodException e) {
/* 109 */         throw sneakyThrow(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public static Object invokeMethod(Method method, Object... args) {
/*     */       try {
/* 115 */         return method.invoke(null, args);
/* 116 */       } catch (IllegalAccessException e) {
/* 117 */         throw sneakyThrow(e);
/* 118 */       } catch (InvocationTargetException e) {
/* 119 */         throw sneakyThrow(e.getCause());
/*     */       } 
/*     */     }
/*     */     
/*     */     private static RuntimeException sneakyThrow(Throwable t) {
/* 124 */       if (t == null) throw new NullPointerException("t"); 
/* 125 */       sneakyThrow0(t);
/* 126 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     private static <T extends Throwable> void sneakyThrow0(Throwable t) throws T {
/* 131 */       throw (T)t;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class LombokDeps
/*     */   {
/*     */     public static final Method ADD_LOMBOK_NOTES;
/*     */     public static final Method POST_COMPILER_BYTES_STRING;
/*     */     public static final Method POST_COMPILER_OUTPUTSTREAM;
/*     */     public static final Method POST_COMPILER_BUFFEREDOUTPUTSTREAM_STRING_STRING;
/*     */     
/*     */     static {
/* 143 */       Class<?> shadowed = PatchFixesHider.Util.shadowLoadClass("lombok.eclipse.agent.PatchFixesShadowLoaded");
/* 144 */       ADD_LOMBOK_NOTES = PatchFixesHider.Util.findMethod(shadowed, "addLombokNotesToEclipseAboutDialog", new Class[] { String.class, String.class });
/* 145 */       POST_COMPILER_BYTES_STRING = PatchFixesHider.Util.findMethod(shadowed, "runPostCompiler", new Class[] { byte[].class, String.class });
/* 146 */       POST_COMPILER_OUTPUTSTREAM = PatchFixesHider.Util.findMethod(shadowed, "runPostCompiler", new Class[] { OutputStream.class });
/* 147 */       POST_COMPILER_BUFFEREDOUTPUTSTREAM_STRING_STRING = PatchFixesHider.Util.findMethod(shadowed, "runPostCompiler", new Class[] { BufferedOutputStream.class, String.class, String.class });
/*     */     }
/*     */     
/*     */     public static String addLombokNotesToEclipseAboutDialog(String origReturnValue, String key) {
/*     */       try {
/* 152 */         return (String)PatchFixesHider.Util.invokeMethod(ADD_LOMBOK_NOTES, new Object[] { origReturnValue, key });
/* 153 */       } catch (Throwable throwable) {
/* 154 */         return origReturnValue;
/*     */       } 
/*     */     }
/*     */     
/*     */     public static byte[] runPostCompiler(byte[] bytes, String fileName) {
/* 159 */       return (byte[])PatchFixesHider.Util.invokeMethod(POST_COMPILER_BYTES_STRING, new Object[] { bytes, fileName });
/*     */     }
/*     */     
/*     */     public static OutputStream runPostCompiler(OutputStream out) throws IOException {
/* 163 */       return (OutputStream)PatchFixesHider.Util.invokeMethod(POST_COMPILER_OUTPUTSTREAM, new Object[] { out });
/*     */     }
/*     */     
/*     */     public static BufferedOutputStream runPostCompiler(BufferedOutputStream out, String path, String name) throws IOException {
/* 167 */       return (BufferedOutputStream)PatchFixesHider.Util.invokeMethod(POST_COMPILER_BUFFEREDOUTPUTSTREAM_STRING_STRING, new Object[] { out, path, name });
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Transform {
/*     */     private static final Method TRANSFORM;
/*     */     private static final Method TRANSFORM_SWAPPED;
/*     */     
/*     */     static {
/* 176 */       Class<?> shadowed = PatchFixesHider.Util.shadowLoadClass("lombok.eclipse.TransformEclipseAST");
/* 177 */       TRANSFORM = PatchFixesHider.Util.findMethod(shadowed, "transform", new Class[] { Parser.class, CompilationUnitDeclaration.class });
/* 178 */       TRANSFORM_SWAPPED = PatchFixesHider.Util.findMethod(shadowed, "transform_swapped", new Class[] { CompilationUnitDeclaration.class, Parser.class });
/*     */     }
/*     */     
/*     */     public static void transform(Parser parser, CompilationUnitDeclaration ast) throws IOException {
/* 182 */       PatchFixesHider.Util.invokeMethod(TRANSFORM, new Object[] { parser, ast });
/*     */     }
/*     */     
/*     */     public static void transform_swapped(CompilationUnitDeclaration ast, Parser parser) throws IOException {
/* 186 */       PatchFixesHider.Util.invokeMethod(TRANSFORM_SWAPPED, new Object[] { ast, parser });
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Delegate
/*     */   {
/*     */     private static final Method HANDLE_DELEGATE_FOR_TYPE;
/*     */     
/*     */     static {
/* 195 */       Class<?> shadowed = PatchFixesHider.Util.shadowLoadClass("lombok.eclipse.agent.PatchDelegatePortal");
/* 196 */       HANDLE_DELEGATE_FOR_TYPE = PatchFixesHider.Util.findMethod(shadowed, "handleDelegateForType", new Class[] { Object.class });
/*     */     }
/*     */     
/*     */     public static boolean handleDelegateForType(Object classScope) {
/* 200 */       return ((Boolean)PatchFixesHider.Util.invokeMethod(HANDLE_DELEGATE_FOR_TYPE, new Object[] { classScope })).booleanValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ValPortal
/*     */   {
/*     */     private static final Method COPY_INITIALIZATION_OF_FOR_EACH_ITERABLE;
/*     */     private static final Method COPY_INITIALIZATION_OF_LOCAL_DECLARATION;
/*     */     private static final Method ADD_FINAL_AND_VAL_ANNOTATION_TO_VARIABLE_DECLARATION_STATEMENT;
/*     */     private static final Method ADD_FINAL_AND_VAL_ANNOTATION_TO_SINGLE_VARIABLE_DECLARATION;
/*     */     
/*     */     static {
/* 212 */       Class<?> shadowed = PatchFixesHider.Util.shadowLoadClass("lombok.eclipse.agent.PatchValEclipsePortal");
/* 213 */       COPY_INITIALIZATION_OF_FOR_EACH_ITERABLE = PatchFixesHider.Util.findMethod(shadowed, "copyInitializationOfForEachIterable", new Class[] { Object.class });
/* 214 */       COPY_INITIALIZATION_OF_LOCAL_DECLARATION = PatchFixesHider.Util.findMethod(shadowed, "copyInitializationOfLocalDeclaration", new Class[] { Object.class });
/* 215 */       ADD_FINAL_AND_VAL_ANNOTATION_TO_VARIABLE_DECLARATION_STATEMENT = PatchFixesHider.Util.findMethod(shadowed, "addFinalAndValAnnotationToVariableDeclarationStatement", new Class[] { Object.class, Object.class, Object.class });
/* 216 */       ADD_FINAL_AND_VAL_ANNOTATION_TO_SINGLE_VARIABLE_DECLARATION = PatchFixesHider.Util.findMethod(shadowed, "addFinalAndValAnnotationToSingleVariableDeclaration", new Class[] { Object.class, Object.class, Object.class });
/*     */     }
/*     */     
/*     */     public static void copyInitializationOfForEachIterable(Object parser) {
/* 220 */       PatchFixesHider.Util.invokeMethod(COPY_INITIALIZATION_OF_FOR_EACH_ITERABLE, new Object[] { parser });
/*     */     }
/*     */     
/*     */     public static void copyInitializationOfLocalDeclaration(Object parser) {
/* 224 */       PatchFixesHider.Util.invokeMethod(COPY_INITIALIZATION_OF_LOCAL_DECLARATION, new Object[] { parser });
/*     */     }
/*     */     
/*     */     public static void addFinalAndValAnnotationToVariableDeclarationStatement(Object converter, Object out, Object in) {
/* 228 */       PatchFixesHider.Util.invokeMethod(ADD_FINAL_AND_VAL_ANNOTATION_TO_VARIABLE_DECLARATION_STATEMENT, new Object[] { converter, out, in });
/*     */     }
/*     */     
/*     */     public static void addFinalAndValAnnotationToSingleVariableDeclaration(Object converter, Object out, Object in) {
/* 232 */       PatchFixesHider.Util.invokeMethod(ADD_FINAL_AND_VAL_ANNOTATION_TO_SINGLE_VARIABLE_DECLARATION, new Object[] { converter, out, in });
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Val
/*     */   {
/*     */     private static final Method SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED;
/*     */     private static final Method SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED2;
/*     */     private static final Method HANDLE_VAL_FOR_LOCAL_DECLARATION;
/*     */     private static final Method HANDLE_VAL_FOR_FOR_EACH;
/*     */     
/*     */     static {
/* 244 */       Class<?> shadowed = PatchFixesHider.Util.shadowLoadClass("lombok.eclipse.agent.PatchVal");
/* 245 */       SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED = PatchFixesHider.Util.findMethod(shadowed, "skipResolveInitializerIfAlreadyCalled", new Class[] { Expression.class, BlockScope.class });
/* 246 */       SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED2 = PatchFixesHider.Util.findMethod(shadowed, "skipResolveInitializerIfAlreadyCalled2", new Class[] { Expression.class, BlockScope.class, LocalDeclaration.class });
/* 247 */       HANDLE_VAL_FOR_LOCAL_DECLARATION = PatchFixesHider.Util.findMethod(shadowed, "handleValForLocalDeclaration", new Class[] { LocalDeclaration.class, BlockScope.class });
/* 248 */       HANDLE_VAL_FOR_FOR_EACH = PatchFixesHider.Util.findMethod(shadowed, "handleValForForEach", new Class[] { ForeachStatement.class, BlockScope.class });
/*     */     }
/*     */     
/*     */     public static TypeBinding skipResolveInitializerIfAlreadyCalled(Expression expr, BlockScope scope) {
/* 252 */       return (TypeBinding)PatchFixesHider.Util.invokeMethod(SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED, new Object[] { expr, scope });
/*     */     }
/*     */     
/*     */     public static TypeBinding skipResolveInitializerIfAlreadyCalled2(Expression expr, BlockScope scope, LocalDeclaration decl) {
/* 256 */       return (TypeBinding)PatchFixesHider.Util.invokeMethod(SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED2, new Object[] { expr, scope, decl });
/*     */     }
/*     */     
/*     */     public static boolean handleValForLocalDeclaration(LocalDeclaration local, BlockScope scope) {
/* 260 */       return ((Boolean)PatchFixesHider.Util.invokeMethod(HANDLE_VAL_FOR_LOCAL_DECLARATION, new Object[] { local, scope })).booleanValue();
/*     */     }
/*     */     
/*     */     public static boolean handleValForForEach(ForeachStatement forEach, BlockScope scope) {
/* 264 */       return ((Boolean)PatchFixesHider.Util.invokeMethod(HANDLE_VAL_FOR_FOR_EACH, new Object[] { forEach, scope })).booleanValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ExtensionMethod {
/*     */     private static final Method RESOLVE_TYPE;
/*     */     private static final Method ERROR_NO_METHOD_FOR;
/*     */     private static final Method INVALID_METHOD;
/*     */     private static final Method INVALID_METHOD2;
/*     */     
/*     */     static {
/* 275 */       Class<?> shadowed = PatchFixesHider.Util.shadowLoadClass("lombok.eclipse.agent.PatchExtensionMethod");
/* 276 */       RESOLVE_TYPE = PatchFixesHider.Util.findMethod(shadowed, "resolveType", new Class[] { TypeBinding.class, MessageSend.class, BlockScope.class });
/* 277 */       ERROR_NO_METHOD_FOR = PatchFixesHider.Util.findMethod(shadowed, "errorNoMethodFor", new Class[] { ProblemReporter.class, MessageSend.class, TypeBinding.class, TypeBinding[].class });
/* 278 */       INVALID_METHOD = PatchFixesHider.Util.findMethod(shadowed, "invalidMethod", new Class[] { ProblemReporter.class, MessageSend.class, MethodBinding.class });
/* 279 */       INVALID_METHOD2 = PatchFixesHider.Util.findMethod(shadowed, "invalidMethod", new Class[] { ProblemReporter.class, MessageSend.class, MethodBinding.class, Scope.class });
/*     */     }
/*     */     
/*     */     public static TypeBinding resolveType(TypeBinding resolvedType, MessageSend methodCall, BlockScope scope) {
/* 283 */       return (TypeBinding)PatchFixesHider.Util.invokeMethod(RESOLVE_TYPE, new Object[] { resolvedType, methodCall, scope });
/*     */     }
/*     */     
/*     */     public static void errorNoMethodFor(ProblemReporter problemReporter, MessageSend messageSend, TypeBinding recType, TypeBinding[] params) {
/* 287 */       PatchFixesHider.Util.invokeMethod(ERROR_NO_METHOD_FOR, new Object[] { problemReporter, messageSend, recType, params });
/*     */     }
/*     */     
/*     */     public static void invalidMethod(ProblemReporter problemReporter, MessageSend messageSend, MethodBinding method) {
/* 291 */       PatchFixesHider.Util.invokeMethod(INVALID_METHOD, new Object[] { problemReporter, messageSend, method });
/*     */     }
/*     */     
/*     */     public static void invalidMethod(ProblemReporter problemReporter, MessageSend messageSend, MethodBinding method, Scope scope) {
/* 295 */       PatchFixesHider.Util.invokeMethod(INVALID_METHOD2, new Object[] { problemReporter, messageSend, method, scope });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class PatchFixes
/*     */   {
/*     */     public static final int ALREADY_PROCESSED_FLAG = 8388608;
/*     */ 
/*     */     
/*     */     public static boolean isGenerated(ASTNode node) {
/* 307 */       boolean result = false;
/*     */       try {
/* 309 */         result = ((Boolean)node.getClass().getField("$isGenerated").get(node)).booleanValue();
/* 310 */         if (!result && node.getParent() != null && node.getParent() instanceof org.eclipse.jdt.core.dom.QualifiedName) {
/* 311 */           result = isGenerated(node.getParent());
/*     */         }
/* 313 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 316 */       return result;
/*     */     }
/*     */     
/*     */     public static boolean isGenerated(ASTNode node) {
/* 320 */       boolean result = false;
/*     */       try {
/* 322 */         result = (node.getClass().getField("$generatedBy").get(node) != null);
/* 323 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 326 */       return result;
/*     */     }
/*     */     
/*     */     public static boolean isListRewriteOnGeneratedNode(ListRewrite rewrite) {
/* 330 */       return isGenerated(rewrite.getParent());
/*     */     }
/*     */     
/*     */     public static boolean returnFalse(Object object) {
/* 334 */       return false;
/*     */     }
/*     */     
/*     */     public static boolean returnTrue(Object object) {
/* 338 */       return true;
/*     */     }
/*     */     
/*     */     public static List removeGeneratedNodes(List list) {
/*     */       try {
/* 343 */         List<Object> realNodes = new ArrayList(list.size());
/* 344 */         for (Object node : list) {
/* 345 */           if (!isGenerated((ASTNode)node)) {
/* 346 */             realNodes.add(node);
/*     */           }
/*     */         } 
/* 349 */         return realNodes;
/* 350 */       } catch (Exception exception) {
/*     */         
/* 352 */         return list;
/*     */       } 
/*     */     }
/*     */     public static String getRealMethodDeclarationSource(String original, Object processor, MethodDeclaration declaration) throws Exception {
/* 356 */       if (!isGenerated((ASTNode)declaration)) return original;
/*     */       
/* 358 */       List<Annotation> annotations = new ArrayList<Annotation>();
/* 359 */       for (Object modifier : declaration.modifiers()) {
/* 360 */         if (modifier instanceof Annotation) {
/* 361 */           Annotation annotation = (Annotation)modifier;
/* 362 */           String qualifiedAnnotationName = annotation.resolveTypeBinding().getQualifiedName();
/* 363 */           if (!"java.lang.Override".equals(qualifiedAnnotationName) && !"java.lang.SuppressWarnings".equals(qualifiedAnnotationName)) annotations.add(annotation);
/*     */         
/*     */         } 
/*     */       } 
/* 367 */       StringBuilder signature = new StringBuilder();
/* 368 */       addAnnotations(annotations, signature);
/*     */       
/* 370 */       if (((Boolean)processor.getClass().getDeclaredField("fPublic").get(processor)).booleanValue()) signature.append("public "); 
/* 371 */       if (((Boolean)processor.getClass().getDeclaredField("fAbstract").get(processor)).booleanValue()) signature.append("abstract ");
/*     */       
/* 373 */       signature
/* 374 */         .append(declaration.getReturnType2().toString())
/* 375 */         .append(" ").append(declaration.getName().getFullyQualifiedName())
/* 376 */         .append("(");
/*     */       
/* 378 */       boolean first = true;
/* 379 */       for (Object parameter : declaration.parameters()) {
/* 380 */         if (!first) signature.append(", "); 
/* 381 */         first = false;
/*     */         
/* 383 */         signature.append(parameter);
/*     */       } 
/*     */       
/* 386 */       signature.append(");");
/* 387 */       return signature.toString();
/*     */     }
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
/*     */     public static void addAnnotations(List<Annotation> annotations, StringBuilder signature) {
/* 407 */       for (Annotation annotation : annotations) {
/* 408 */         List<String> values = new ArrayList<String>();
/* 409 */         if (annotation.isSingleMemberAnnotation()) {
/* 410 */           SingleMemberAnnotation smAnn = (SingleMemberAnnotation)annotation;
/* 411 */           values.add(smAnn.getValue().toString());
/* 412 */         } else if (annotation.isNormalAnnotation()) {
/* 413 */           NormalAnnotation normalAnn = (NormalAnnotation)annotation;
/* 414 */           for (Object value : normalAnn.values()) values.add(value.toString());
/*     */         
/*     */         } 
/* 417 */         signature.append("@").append(annotation.resolveTypeBinding().getQualifiedName());
/* 418 */         if (!values.isEmpty()) {
/* 419 */           signature.append("(");
/* 420 */           boolean first = true;
/* 421 */           for (String string : values) {
/* 422 */             if (!first) signature.append(", "); 
/* 423 */             first = false;
/* 424 */             signature.append('"').append(string).append('"');
/*     */           } 
/* 426 */           signature.append(")");
/*     */         } 
/* 428 */         signature.append(" ");
/*     */       } 
/*     */     }
/*     */     
/*     */     public static MethodDeclaration getRealMethodDeclarationNode(IMethod sourceMethod, CompilationUnit cuUnit) throws JavaModelException {
/* 433 */       MethodDeclaration methodDeclarationNode = ASTNodeSearchUtil.getMethodDeclarationNode(sourceMethod, cuUnit);
/* 434 */       if (isGenerated((ASTNode)methodDeclarationNode)) {
/* 435 */         IType declaringType = sourceMethod.getDeclaringType();
/* 436 */         Stack<IType> typeStack = new Stack<IType>();
/* 437 */         while (declaringType != null) {
/* 438 */           typeStack.push(declaringType);
/* 439 */           declaringType = declaringType.getDeclaringType();
/*     */         } 
/*     */         
/* 442 */         IType rootType = typeStack.pop();
/* 443 */         AbstractTypeDeclaration typeDeclaration = findTypeDeclaration(rootType, cuUnit.types());
/* 444 */         while (!typeStack.isEmpty() && typeDeclaration != null) {
/* 445 */           typeDeclaration = findTypeDeclaration(typeStack.pop(), typeDeclaration.bodyDeclarations());
/*     */         }
/*     */         
/* 448 */         if (typeStack.isEmpty() && typeDeclaration != null) {
/* 449 */           String methodName = sourceMethod.getElementName();
/* 450 */           for (Object declaration : typeDeclaration.bodyDeclarations()) {
/* 451 */             if (declaration instanceof MethodDeclaration) {
/* 452 */               MethodDeclaration methodDeclaration = (MethodDeclaration)declaration;
/* 453 */               if (methodDeclaration.getName().toString().equals(methodName)) {
/* 454 */                 return methodDeclaration;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 460 */       return methodDeclarationNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public static AbstractTypeDeclaration findTypeDeclaration(IType searchType, List<?> nodes) {
/* 465 */       for (Object object : nodes) {
/* 466 */         if (object instanceof AbstractTypeDeclaration) {
/* 467 */           AbstractTypeDeclaration typeDeclaration = (AbstractTypeDeclaration)object;
/* 468 */           if (typeDeclaration.getName().toString().equals(searchType.getElementName()))
/* 469 */             return typeDeclaration; 
/*     */         } 
/*     */       } 
/* 472 */       return null;
/*     */     }
/*     */     
/*     */     public static int getSourceEndFixed(int sourceEnd, ASTNode node) throws Exception {
/* 476 */       if (sourceEnd == -1) {
/* 477 */         ASTNode object = (ASTNode)node.getClass().getField("$generatedBy").get(node);
/* 478 */         if (object != null) {
/* 479 */           return object.sourceEnd;
/*     */         }
/*     */       } 
/* 482 */       return sourceEnd;
/*     */     }
/*     */     
/*     */     public static int fixRetrieveStartingCatchPosition(int original, int start) {
/* 486 */       return (original == -1) ? start : original;
/*     */     }
/*     */     
/*     */     public static int fixRetrieveIdentifierEndPosition(int original, int start, int end) {
/* 490 */       if (original == -1) return end; 
/* 491 */       if (original < start) return end; 
/* 492 */       return original;
/*     */     }
/*     */     
/*     */     public static int fixRetrieveEllipsisStartPosition(int original, int end) {
/* 496 */       return (original == -1) ? end : original;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static int fixRetrieveRightBraceOrSemiColonPosition(int original, int end) {
/* 503 */       return (original == -1) ? end : original;
/*     */     }
/*     */     
/*     */     public static int fixRetrieveRightBraceOrSemiColonPosition(int retVal, AbstractMethodDeclaration amd) {
/* 507 */       if (retVal != -1 || amd == null) return retVal; 
/* 508 */       boolean isGenerated = (EclipseAugments.ASTNode_generatedBy.get(amd) != null);
/* 509 */       if (isGenerated) return amd.declarationSourceEnd; 
/* 510 */       return -1;
/*     */     }
/*     */     
/*     */     public static int fixRetrieveRightBraceOrSemiColonPosition(int retVal, FieldDeclaration fd) {
/* 514 */       if (retVal != -1 || fd == null) return retVal; 
/* 515 */       boolean isGenerated = (EclipseAugments.ASTNode_generatedBy.get(fd) != null);
/* 516 */       if (isGenerated) return fd.declarationSourceEnd; 
/* 517 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static boolean checkBit24(Object node) throws Exception {
/* 523 */       int bits = ((Integer)node.getClass().getField("bits").get(node)).intValue();
/* 524 */       return ((bits & 0x800000) != 0);
/*     */     }
/*     */     
/*     */     public static boolean skipRewritingGeneratedNodes(ASTNode node) throws Exception {
/* 528 */       return ((Boolean)node.getClass().getField("$isGenerated").get(node)).booleanValue();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static void setIsGeneratedFlag(ASTNode domNode, ASTNode internalNode) throws Exception {
/* 534 */       if (internalNode == null || domNode == null)
/* 535 */         return;  boolean isGenerated = (EclipseAugments.ASTNode_generatedBy.get(internalNode) != null);
/* 536 */       if (isGenerated) domNode.getClass().getField("$isGenerated").set(domNode, Boolean.valueOf(true)); 
/*     */     }
/*     */     
/*     */     public static void setIsGeneratedFlagForName(Name name, Object internalNode) throws Exception {
/* 540 */       if (internalNode instanceof ASTNode) {
/* 541 */         boolean isGenerated = (EclipseAugments.ASTNode_generatedBy.get(internalNode) != null);
/* 542 */         if (isGenerated) name.getClass().getField("$isGenerated").set(name, Boolean.valueOf(true)); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public static RewriteEvent[] listRewriteHandleGeneratedMethods(RewriteEvent parent) {
/* 547 */       RewriteEvent[] children = parent.getChildren();
/* 548 */       List<RewriteEvent> newChildren = new ArrayList<RewriteEvent>();
/* 549 */       List<RewriteEvent> modifiedChildren = new ArrayList<RewriteEvent>();
/* 550 */       for (int i = 0; i < children.length; i++) {
/* 551 */         RewriteEvent child = children[i];
/* 552 */         boolean isGenerated = isGenerated((ASTNode)child.getOriginalValue());
/* 553 */         if (isGenerated) {
/* 554 */           boolean isReplacedOrRemoved = !(child.getChangeKind() != 4 && child.getChangeKind() != 2);
/* 555 */           boolean convertingFromMethod = child.getOriginalValue() instanceof MethodDeclaration;
/* 556 */           if (isReplacedOrRemoved && convertingFromMethod && child.getNewValue() != null) {
/* 557 */             modifiedChildren.add(new NodeRewriteEvent(null, child.getNewValue()));
/*     */           }
/*     */         } else {
/* 560 */           newChildren.add(child);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 565 */       newChildren.addAll(modifiedChildren);
/* 566 */       return newChildren.<RewriteEvent>toArray(new RewriteEvent[0]);
/*     */     }
/*     */     
/*     */     public static int getTokenEndOffsetFixed(TokenScanner scanner, int token, int startOffset, Object domNode) throws CoreException {
/* 570 */       boolean isGenerated = false;
/*     */       try {
/* 572 */         isGenerated = ((Boolean)domNode.getClass().getField("$isGenerated").get(domNode)).booleanValue();
/* 573 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 576 */       if (isGenerated) return -1; 
/* 577 */       return scanner.getTokenEndOffset(token, startOffset);
/*     */     }
/*     */     
/*     */     public static IMethod[] removeGeneratedMethods(IMethod[] methods) throws Exception {
/* 581 */       List<IMethod> result = new ArrayList<IMethod>(); byte b; int i; IMethod[] arrayOfIMethod;
/* 582 */       for (i = (arrayOfIMethod = methods).length, b = 0; b < i; ) { IMethod m = arrayOfIMethod[b];
/* 583 */         if (m.getNameRange().getLength() > 0 && !m.getNameRange().equals(m.getSourceRange())) result.add(m);  b++; }
/*     */       
/* 585 */       return (result.size() == methods.length) ? methods : result.<IMethod>toArray(new IMethod[0]);
/*     */     }
/*     */     
/*     */     public static SearchMatch[] removeGenerated(SearchMatch[] returnValue) {
/* 589 */       List<SearchMatch> result = new ArrayList<SearchMatch>();
/* 590 */       for (int j = 0; j < returnValue.length; j++) {
/* 591 */         SearchMatch searchResult = returnValue[j];
/* 592 */         if (searchResult.getElement() instanceof IField) {
/* 593 */           IField field = (IField)searchResult.getElement();
/*     */ 
/*     */ 
/*     */           
/* 597 */           IAnnotation annotation = field.getAnnotation("Generated");
/* 598 */           if (annotation != null) {
/*     */             continue;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 604 */         result.add(searchResult); continue;
/*     */       } 
/* 606 */       return result.<SearchMatch>toArray(new SearchMatch[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static SearchResultGroup[] createFakeSearchResult(SearchResultGroup[] returnValue, Object processor) throws Exception {
/* 614 */       if (returnValue == null || returnValue.length == 0) {
/*     */         
/* 616 */         Field declaredField = processor.getClass().getDeclaredField("fField");
/* 617 */         if (declaredField != null) {
/* 618 */           declaredField.setAccessible(true);
/* 619 */           SourceField fField = (SourceField)declaredField.get(processor);
/* 620 */           IAnnotation dataAnnotation = fField.getDeclaringType().getAnnotation("Data");
/* 621 */           if (dataAnnotation != null)
/*     */           {
/* 623 */             return new SearchResultGroup[] { new SearchResultGroup(null, new SearchMatch[1]) };
/*     */           }
/*     */         } 
/*     */       } 
/* 627 */       return returnValue;
/*     */     }
/*     */     
/*     */     public static SimpleName[] removeGeneratedSimpleNames(SimpleName[] in) throws Exception {
/* 631 */       Field f = SimpleName.class.getField("$isGenerated");
/*     */       
/* 633 */       int count = 0;
/* 634 */       for (int i = 0; i < in.length; i++) {
/* 635 */         if (in[i] == null || !((Boolean)f.get(in[i])).booleanValue()) count++; 
/*     */       } 
/* 637 */       if (count == in.length) return in; 
/* 638 */       SimpleName[] newSimpleNames = new SimpleName[count];
/* 639 */       count = 0;
/* 640 */       for (int j = 0; j < in.length; j++) {
/* 641 */         if (in[j] == null || !((Boolean)f.get(in[j])).booleanValue()) newSimpleNames[count++] = in[j]; 
/*     */       } 
/* 643 */       return newSimpleNames;
/*     */     }
/*     */ 
/*     */     
/*     */     public static Annotation[] convertAnnotations(Annotation[] out, IAnnotatable annotatable) {
/*     */       IAnnotation[] in;
/*     */       try {
/* 650 */         in = annotatable.getAnnotations();
/* 651 */       } catch (Exception exception) {
/* 652 */         return out;
/*     */       } 
/*     */       
/* 655 */       if (out == null) return null; 
/* 656 */       int toWrite = 0;
/*     */       
/* 658 */       for (int idx = 0; idx < out.length; idx++) {
/* 659 */         String oName = new String((out[idx]).type.getLastToken());
/* 660 */         boolean found = false; byte b; int i; IAnnotation[] arrayOfIAnnotation;
/* 661 */         for (i = (arrayOfIAnnotation = in).length, b = 0; b < i; ) { IAnnotation iAnnotation = arrayOfIAnnotation[b];
/* 662 */           String name = iAnnotation.getElementName();
/* 663 */           int li = name.lastIndexOf('.');
/* 664 */           if (li > -1) name = name.substring(li + 1); 
/* 665 */           if (name.equals(oName)) {
/* 666 */             found = true; break;
/*     */           } 
/*     */           b++; }
/*     */         
/* 670 */         if (!found) { out[idx] = null; }
/* 671 */         else { toWrite++; }
/*     */       
/*     */       } 
/* 674 */       Annotation[] replace = out;
/* 675 */       if (toWrite < out.length) {
/* 676 */         replace = new Annotation[toWrite];
/* 677 */         int j = 0;
/* 678 */         for (int i = 0; i < out.length; i++) {
/* 679 */           if (out[i] != null) {
/* 680 */             replace[j++] = out[i];
/*     */           }
/*     */         } 
/*     */       } 
/* 684 */       return replace;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\launch\PatchFixesHider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */