/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.internal.CustomizerRegistry;
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
/*     */ 
/*     */ 
/*     */ public abstract class KeyFactory
/*     */ {
/*  60 */   private static final Signature GET_NAME = TypeUtils.parseSignature("String getName()");
/*     */   
/*  62 */   private static final Signature GET_CLASS = TypeUtils.parseSignature("Class getClass()");
/*     */   
/*  64 */   private static final Signature HASH_CODE = TypeUtils.parseSignature("int hashCode()");
/*     */   
/*  66 */   private static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(Object)");
/*     */   
/*  68 */   private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
/*     */   
/*  70 */   private static final Signature APPEND_STRING = TypeUtils.parseSignature("StringBuffer append(String)");
/*     */   
/*  72 */   private static final Type KEY_FACTORY = TypeUtils.parseType("org.springframework.cglib.core.KeyFactory");
/*     */   
/*  74 */   private static final Signature GET_SORT = TypeUtils.parseSignature("int getSort()");
/*     */ 
/*     */   
/*  77 */   private static final int[] PRIMES = new int[] { 11, 73, 179, 331, 521, 787, 1213, 1823, 2609, 3691, 5189, 7247, 10037, 13931, 19289, 26627, 36683, 50441, 69403, 95401, 131129, 180179, 247501, 340057, 467063, 641371, 880603, 1209107, 1660097, 2279161, 3129011, 4295723, 5897291, 8095873, 11114263, 15257791, 20946017, 28754629, 39474179, 54189869, 74391461, 102123817, 140194277, 192456917, 264202273, 362693231, 497900099, 683510293, 938313161, 1288102441, 1768288259 };
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
/*  93 */   public static final Customizer CLASS_BY_NAME = new Customizer() {
/*     */       public void customize(CodeEmitter e, Type type) {
/*  95 */         if (type.equals(Constants.TYPE_CLASS)) {
/*  96 */           e.invoke_virtual(Constants.TYPE_CLASS, KeyFactory.GET_NAME);
/*     */         }
/*     */       }
/*     */     };
/*     */   
/* 101 */   public static final FieldTypeCustomizer STORE_CLASS_AS_STRING = new FieldTypeCustomizer() {
/*     */       public void customize(CodeEmitter e, int index, Type type) {
/* 103 */         if (type.equals(Constants.TYPE_CLASS)) {
/* 104 */           e.invoke_virtual(Constants.TYPE_CLASS, KeyFactory.GET_NAME);
/*     */         }
/*     */       }
/*     */       
/*     */       public Type getOutType(int index, Type type) {
/* 109 */         if (type.equals(Constants.TYPE_CLASS)) {
/* 110 */           return Constants.TYPE_STRING;
/*     */         }
/* 112 */         return type;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static final HashCodeCustomizer HASH_ASM_TYPE = new HashCodeCustomizer() {
/*     */       public boolean customize(CodeEmitter e, Type type) {
/* 122 */         if (Constants.TYPE_TYPE.equals(type)) {
/* 123 */           e.invoke_virtual(type, KeyFactory.GET_SORT);
/* 124 */           return true;
/*     */         } 
/* 126 */         return false;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 135 */   public static final Customizer OBJECT_BY_CLASS = new Customizer() {
/*     */       public void customize(CodeEmitter e, Type type) {
/* 137 */         e.invoke_virtual(Constants.TYPE_OBJECT, KeyFactory.GET_CLASS);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static KeyFactory create(Class keyInterface) {
/* 145 */     return create(keyInterface, null);
/*     */   }
/*     */   
/*     */   public static KeyFactory create(Class keyInterface, Customizer customizer) {
/* 149 */     return create(keyInterface.getClassLoader(), keyInterface, customizer);
/*     */   }
/*     */   
/*     */   public static KeyFactory create(Class keyInterface, KeyFactoryCustomizer first, List<KeyFactoryCustomizer> next) {
/* 153 */     return create(keyInterface.getClassLoader(), keyInterface, first, next);
/*     */   }
/*     */   
/*     */   public static KeyFactory create(ClassLoader loader, Class keyInterface, Customizer customizer) {
/* 157 */     return create(loader, keyInterface, customizer, Collections.emptyList());
/*     */   }
/*     */ 
/*     */   
/*     */   public static KeyFactory create(ClassLoader loader, Class keyInterface, KeyFactoryCustomizer customizer, List<KeyFactoryCustomizer> next) {
/* 162 */     Generator gen = new Generator();
/* 163 */     gen.setInterface(keyInterface);
/*     */     
/* 165 */     if (customizer != null) {
/* 166 */       gen.addCustomizer(customizer);
/*     */     }
/* 168 */     if (next != null && !next.isEmpty()) {
/* 169 */       for (KeyFactoryCustomizer keyFactoryCustomizer : next) {
/* 170 */         gen.addCustomizer(keyFactoryCustomizer);
/*     */       }
/*     */     }
/* 173 */     gen.setClassLoader(loader);
/* 174 */     return gen.create();
/*     */   }
/*     */   
/*     */   public static class Generator extends AbstractClassGenerator {
/* 178 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(KeyFactory.class.getName());
/* 179 */     private static final Class[] KNOWN_CUSTOMIZER_TYPES = new Class[] { Customizer.class, FieldTypeCustomizer.class };
/*     */     
/*     */     private Class keyInterface;
/*     */     
/* 183 */     private CustomizerRegistry customizers = new CustomizerRegistry(KNOWN_CUSTOMIZER_TYPES);
/*     */     private int constant;
/*     */     private int multiplier;
/*     */     
/*     */     public Generator() {
/* 188 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/* 192 */       return this.keyInterface.getClassLoader();
/*     */     }
/*     */     
/*     */     protected ProtectionDomain getProtectionDomain() {
/* 196 */       return ReflectUtils.getProtectionDomain(this.keyInterface);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void setCustomizer(Customizer customizer) {
/* 204 */       this.customizers = CustomizerRegistry.singleton(customizer);
/*     */     }
/*     */     
/*     */     public void addCustomizer(KeyFactoryCustomizer customizer) {
/* 208 */       this.customizers.add(customizer);
/*     */     }
/*     */     
/*     */     public <T> List<T> getCustomizers(Class<T> klass) {
/* 212 */       return this.customizers.get(klass);
/*     */     }
/*     */     
/*     */     public void setInterface(Class keyInterface) {
/* 216 */       this.keyInterface = keyInterface;
/*     */     }
/*     */     
/*     */     public KeyFactory create() {
/* 220 */       setNamePrefix(this.keyInterface.getName());
/* 221 */       return (KeyFactory)create(this.keyInterface.getName());
/*     */     }
/*     */     
/*     */     public void setHashConstant(int constant) {
/* 225 */       this.constant = constant;
/*     */     }
/*     */     
/*     */     public void setHashMultiplier(int multiplier) {
/* 229 */       this.multiplier = multiplier;
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 233 */       return ReflectUtils.newInstance(type);
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 237 */       return instance;
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) {
/* 241 */       ClassEmitter ce = new ClassEmitter(v);
/*     */       
/* 243 */       Method newInstance = ReflectUtils.findNewInstance(this.keyInterface);
/* 244 */       if (!newInstance.getReturnType().equals(Object.class)) {
/* 245 */         throw new IllegalArgumentException("newInstance method must return Object");
/*     */       }
/*     */       
/* 248 */       Type[] parameterTypes = TypeUtils.getTypes(newInstance.getParameterTypes());
/* 249 */       ce.begin_class(46, 1, 
/*     */           
/* 251 */           getClassName(), KeyFactory
/* 252 */           .KEY_FACTORY, new Type[] {
/* 253 */             Type.getType(this.keyInterface) }, "<generated>");
/*     */       
/* 255 */       EmitUtils.null_constructor(ce);
/* 256 */       EmitUtils.factory_method(ce, ReflectUtils.getSignature(newInstance));
/*     */       
/* 258 */       int seed = 0;
/* 259 */       CodeEmitter e = ce.begin_method(1, 
/* 260 */           TypeUtils.parseConstructor(parameterTypes), null);
/*     */       
/* 262 */       e.load_this();
/* 263 */       e.super_invoke_constructor();
/* 264 */       e.load_this();
/* 265 */       List<FieldTypeCustomizer> fieldTypeCustomizers = getCustomizers(FieldTypeCustomizer.class);
/* 266 */       for (int i = 0; i < parameterTypes.length; i++) {
/* 267 */         Type parameterType = parameterTypes[i];
/* 268 */         Type fieldType = parameterType;
/* 269 */         for (FieldTypeCustomizer customizer : fieldTypeCustomizers) {
/* 270 */           fieldType = customizer.getOutType(i, fieldType);
/*     */         }
/* 272 */         seed += fieldType.hashCode();
/* 273 */         ce.declare_field(18, 
/* 274 */             getFieldName(i), fieldType, null);
/*     */ 
/*     */         
/* 277 */         e.dup();
/* 278 */         e.load_arg(i);
/* 279 */         for (FieldTypeCustomizer customizer : fieldTypeCustomizers) {
/* 280 */           customizer.customize(e, i, parameterType);
/*     */         }
/* 282 */         e.putfield(getFieldName(i));
/*     */       } 
/* 284 */       e.return_value();
/* 285 */       e.end_method();
/*     */ 
/*     */       
/* 288 */       e = ce.begin_method(1, KeyFactory.HASH_CODE, null);
/* 289 */       int hc = (this.constant != 0) ? this.constant : KeyFactory.PRIMES[Math.abs(seed) % KeyFactory.PRIMES.length];
/* 290 */       int hm = (this.multiplier != 0) ? this.multiplier : KeyFactory.PRIMES[Math.abs(seed * 13) % KeyFactory.PRIMES.length];
/* 291 */       e.push(hc);
/* 292 */       for (int j = 0; j < parameterTypes.length; j++) {
/* 293 */         e.load_this();
/* 294 */         e.getfield(getFieldName(j));
/* 295 */         EmitUtils.hash_code(e, parameterTypes[j], hm, this.customizers);
/*     */       } 
/* 297 */       e.return_value();
/* 298 */       e.end_method();
/*     */ 
/*     */       
/* 301 */       e = ce.begin_method(1, KeyFactory.EQUALS, null);
/* 302 */       Label fail = e.make_label();
/* 303 */       e.load_arg(0);
/* 304 */       e.instance_of_this();
/* 305 */       e.if_jump(153, fail); int k;
/* 306 */       for (k = 0; k < parameterTypes.length; k++) {
/* 307 */         e.load_this();
/* 308 */         e.getfield(getFieldName(k));
/* 309 */         e.load_arg(0);
/* 310 */         e.checkcast_this();
/* 311 */         e.getfield(getFieldName(k));
/* 312 */         EmitUtils.not_equals(e, parameterTypes[k], fail, this.customizers);
/*     */       } 
/* 314 */       e.push(1);
/* 315 */       e.return_value();
/* 316 */       e.mark(fail);
/* 317 */       e.push(0);
/* 318 */       e.return_value();
/* 319 */       e.end_method();
/*     */ 
/*     */       
/* 322 */       e = ce.begin_method(1, KeyFactory.TO_STRING, null);
/* 323 */       e.new_instance(Constants.TYPE_STRING_BUFFER);
/* 324 */       e.dup();
/* 325 */       e.invoke_constructor(Constants.TYPE_STRING_BUFFER);
/* 326 */       for (k = 0; k < parameterTypes.length; k++) {
/* 327 */         if (k > 0) {
/* 328 */           e.push(", ");
/* 329 */           e.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.APPEND_STRING);
/*     */         } 
/* 331 */         e.load_this();
/* 332 */         e.getfield(getFieldName(k));
/* 333 */         EmitUtils.append_string(e, parameterTypes[k], EmitUtils.DEFAULT_DELIMITERS, this.customizers);
/*     */       } 
/* 335 */       e.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.TO_STRING);
/* 336 */       e.return_value();
/* 337 */       e.end_method();
/*     */       
/* 339 */       ce.end_class();
/*     */     }
/*     */     
/*     */     private String getFieldName(int arg) {
/* 343 */       return "FIELD_" + arg;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\core\KeyFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */