/*     */ package org.springframework.scripting.jruby;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.jruby.Ruby;
/*     */ import org.jruby.RubyArray;
/*     */ import org.jruby.ast.ClassNode;
/*     */ import org.jruby.ast.Colon2Node;
/*     */ import org.jruby.ast.NewlineNode;
/*     */ import org.jruby.ast.Node;
/*     */ import org.jruby.exceptions.JumpException;
/*     */ import org.jruby.exceptions.RaiseException;
/*     */ import org.jruby.javasupport.JavaEmbedUtils;
/*     */ import org.jruby.runtime.builtin.IRubyObject;
/*     */ import org.springframework.core.NestedRuntimeException;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @Deprecated
/*     */ public abstract class JRubyScriptUtils
/*     */ {
/*     */   public static Object createJRubyObject(String scriptSource, Class<?>... interfaces) throws JumpException {
/*  71 */     return createJRubyObject(scriptSource, interfaces, ClassUtils.getDefaultClassLoader());
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
/*     */   public static Object createJRubyObject(String scriptSource, Class<?>[] interfaces, ClassLoader classLoader) {
/*  83 */     Ruby ruby = initializeRuntime();
/*     */     
/*  85 */     Node scriptRootNode = ruby.parseEval(scriptSource, "", null, 0);
/*  86 */     IRubyObject rubyObject = ruby.runNormally(scriptRootNode);
/*     */     
/*  88 */     if (rubyObject instanceof org.jruby.RubyNil) {
/*  89 */       String className = findClassName(scriptRootNode);
/*  90 */       rubyObject = ruby.evalScriptlet("\n" + className + ".new");
/*     */     } 
/*     */     
/*  93 */     if (rubyObject instanceof org.jruby.RubyNil) {
/*  94 */       throw new IllegalStateException("Compilation of JRuby script returned RubyNil: " + rubyObject);
/*     */     }
/*     */     
/*  97 */     return Proxy.newProxyInstance(classLoader, interfaces, new RubyObjectInvocationHandler(rubyObject, ruby));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Ruby initializeRuntime() {
/* 105 */     return JavaEmbedUtils.initialize(Collections.EMPTY_LIST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String findClassName(Node rootNode) {
/* 114 */     ClassNode classNode = findClassNode(rootNode);
/* 115 */     if (classNode == null) {
/* 116 */       throw new IllegalArgumentException("Unable to determine class name for root node '" + rootNode + "'");
/*     */     }
/* 118 */     Colon2Node node = (Colon2Node)classNode.getCPath();
/* 119 */     return node.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ClassNode findClassNode(Node node) {
/* 127 */     if (node == null) {
/* 128 */       return null;
/*     */     }
/* 130 */     if (node instanceof ClassNode) {
/* 131 */       return (ClassNode)node;
/*     */     }
/* 133 */     List<Node> children = node.childNodes();
/* 134 */     for (Node child : children) {
/* 135 */       if (child instanceof ClassNode) {
/* 136 */         return (ClassNode)child;
/*     */       }
/* 138 */       if (child instanceof NewlineNode) {
/* 139 */         NewlineNode nn = (NewlineNode)child;
/* 140 */         ClassNode found = findClassNode(nn.getNextNode());
/* 141 */         if (found != null) {
/* 142 */           return found;
/*     */         }
/*     */       } 
/*     */     } 
/* 146 */     for (Node child : children) {
/* 147 */       ClassNode found = findClassNode(child);
/* 148 */       if (found != null) {
/* 149 */         return found;
/*     */       }
/*     */     } 
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RubyObjectInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final IRubyObject rubyObject;
/*     */     
/*     */     private final Ruby ruby;
/*     */ 
/*     */     
/*     */     public RubyObjectInvocationHandler(IRubyObject rubyObject, Ruby ruby) {
/* 166 */       this.rubyObject = rubyObject;
/* 167 */       this.ruby = ruby;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 172 */       if (ReflectionUtils.isEqualsMethod(method)) {
/* 173 */         return Boolean.valueOf(isProxyForSameRubyObject(args[0]));
/*     */       }
/* 175 */       if (ReflectionUtils.isHashCodeMethod(method)) {
/* 176 */         return Integer.valueOf(this.rubyObject.hashCode());
/*     */       }
/* 178 */       if (ReflectionUtils.isToStringMethod(method)) {
/* 179 */         String toStringResult = this.rubyObject.toString();
/* 180 */         if (!StringUtils.hasText(toStringResult)) {
/* 181 */           toStringResult = ObjectUtils.identityToString(this.rubyObject);
/*     */         }
/* 183 */         return "JRuby object [" + toStringResult + "]";
/*     */       } 
/*     */       try {
/* 186 */         IRubyObject[] rubyArgs = convertToRuby(args);
/*     */         
/* 188 */         IRubyObject rubyResult = this.rubyObject.callMethod(this.ruby.getCurrentContext(), method.getName(), rubyArgs);
/* 189 */         return convertFromRuby(rubyResult, method.getReturnType());
/*     */       }
/* 191 */       catch (RaiseException ex) {
/* 192 */         throw new JRubyScriptUtils.JRubyExecutionException(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean isProxyForSameRubyObject(Object other) {
/* 197 */       if (!Proxy.isProxyClass(other.getClass())) {
/* 198 */         return false;
/*     */       }
/* 200 */       InvocationHandler ih = Proxy.getInvocationHandler(other);
/* 201 */       return (ih instanceof RubyObjectInvocationHandler && this.rubyObject
/* 202 */         .equals(((RubyObjectInvocationHandler)ih).rubyObject));
/*     */     }
/*     */     
/*     */     private IRubyObject[] convertToRuby(Object[] javaArgs) {
/* 206 */       if (javaArgs == null || javaArgs.length == 0) {
/* 207 */         return new IRubyObject[0];
/*     */       }
/* 209 */       IRubyObject[] rubyArgs = new IRubyObject[javaArgs.length];
/* 210 */       for (int i = 0; i < javaArgs.length; i++) {
/* 211 */         rubyArgs[i] = JavaEmbedUtils.javaToRuby(this.ruby, javaArgs[i]);
/*     */       }
/* 213 */       return rubyArgs;
/*     */     }
/*     */     
/*     */     private Object convertFromRuby(IRubyObject rubyResult, Class<?> returnType) {
/* 217 */       Object result = JavaEmbedUtils.rubyToJava(this.ruby, rubyResult, returnType);
/* 218 */       if (result instanceof RubyArray && returnType.isArray()) {
/* 219 */         result = convertFromRubyArray(((RubyArray)result).toJavaArray(), returnType);
/*     */       }
/* 221 */       return result;
/*     */     }
/*     */     
/*     */     private Object convertFromRubyArray(IRubyObject[] rubyArray, Class<?> returnType) {
/* 225 */       Class<?> targetType = returnType.getComponentType();
/* 226 */       Object javaArray = Array.newInstance(targetType, rubyArray.length);
/* 227 */       for (int i = 0; i < rubyArray.length; i++) {
/* 228 */         IRubyObject rubyObject = rubyArray[i];
/* 229 */         Array.set(javaArray, i, convertFromRuby(rubyObject, targetType));
/*     */       } 
/* 231 */       return javaArray;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static class JRubyExecutionException
/*     */     extends NestedRuntimeException
/*     */   {
/*     */     public JRubyExecutionException(RaiseException ex) {
/* 249 */       super(ex.getMessage(), (Throwable)ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scripting\jruby\JRubyScriptUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */