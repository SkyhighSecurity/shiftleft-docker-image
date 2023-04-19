/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.FieldVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.transform.ClassTransformer;
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
/*     */ public class ClassEmitter
/*     */   extends ClassTransformer
/*     */ {
/*     */   private ClassInfo classInfo;
/*     */   private Map fieldInfo;
/*     */   private static int hookCounter;
/*     */   private MethodVisitor rawStaticInit;
/*     */   private CodeEmitter staticInit;
/*     */   private CodeEmitter staticHook;
/*     */   private Signature staticHookSig;
/*     */   
/*     */   public ClassEmitter(ClassVisitor cv) {
/*  43 */     setTarget(cv);
/*     */   }
/*     */   
/*     */   public ClassEmitter() {
/*  47 */     super(393216);
/*     */   }
/*     */   
/*     */   public void setTarget(ClassVisitor cv) {
/*  51 */     this.cv = cv;
/*  52 */     this.fieldInfo = new HashMap<Object, Object>();
/*     */ 
/*     */     
/*  55 */     this.staticInit = this.staticHook = null;
/*  56 */     this.staticHookSig = null;
/*     */   }
/*     */   
/*     */   private static synchronized int getNextHook() {
/*  60 */     return ++hookCounter;
/*     */   }
/*     */   
/*     */   public ClassInfo getClassInfo() {
/*  64 */     return this.classInfo;
/*     */   }
/*     */   
/*     */   public void begin_class(int version, final int access, String className, final Type superType, final Type[] interfaces, String source) {
/*  68 */     final Type classType = Type.getType("L" + className.replace('.', '/') + ";");
/*  69 */     this.classInfo = new ClassInfo() {
/*     */         public Type getType() {
/*  71 */           return classType;
/*     */         }
/*     */         public Type getSuperType() {
/*  74 */           return (superType != null) ? superType : Constants.TYPE_OBJECT;
/*     */         }
/*     */         public Type[] getInterfaces() {
/*  77 */           return interfaces;
/*     */         }
/*     */         public int getModifiers() {
/*  80 */           return access;
/*     */         }
/*     */       };
/*  83 */     this.cv.visit(version, access, this.classInfo
/*     */         
/*  85 */         .getType().getInternalName(), null, this.classInfo
/*     */         
/*  87 */         .getSuperType().getInternalName(), 
/*  88 */         TypeUtils.toInternalNames(interfaces));
/*  89 */     if (source != null)
/*  90 */       this.cv.visitSource(source, null); 
/*  91 */     init();
/*     */   }
/*     */   
/*     */   public CodeEmitter getStaticHook() {
/*  95 */     if (TypeUtils.isInterface(getAccess())) {
/*  96 */       throw new IllegalStateException("static hook is invalid for this class");
/*     */     }
/*  98 */     if (this.staticHook == null) {
/*  99 */       this.staticHookSig = new Signature("CGLIB$STATICHOOK" + getNextHook(), "()V");
/* 100 */       this.staticHook = begin_method(8, this.staticHookSig, null);
/*     */ 
/*     */       
/* 103 */       if (this.staticInit != null) {
/* 104 */         this.staticInit.invoke_static_this(this.staticHookSig);
/*     */       }
/*     */     } 
/* 107 */     return this.staticHook;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {}
/*     */   
/*     */   public int getAccess() {
/* 114 */     return this.classInfo.getModifiers();
/*     */   }
/*     */   
/*     */   public Type getClassType() {
/* 118 */     return this.classInfo.getType();
/*     */   }
/*     */   
/*     */   public Type getSuperType() {
/* 122 */     return this.classInfo.getSuperType();
/*     */   }
/*     */   
/*     */   public void end_class() {
/* 126 */     if (this.staticHook != null && this.staticInit == null)
/*     */     {
/* 128 */       begin_static();
/*     */     }
/* 130 */     if (this.staticInit != null) {
/* 131 */       this.staticHook.return_value();
/* 132 */       this.staticHook.end_method();
/* 133 */       this.rawStaticInit.visitInsn(177);
/* 134 */       this.rawStaticInit.visitMaxs(0, 0);
/* 135 */       this.staticInit = this.staticHook = null;
/* 136 */       this.staticHookSig = null;
/*     */     } 
/* 138 */     this.cv.visitEnd();
/*     */   }
/*     */   
/*     */   public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
/* 142 */     if (this.classInfo == null)
/* 143 */       throw new IllegalStateException("classInfo is null! " + this); 
/* 144 */     MethodVisitor v = this.cv.visitMethod(access, sig
/* 145 */         .getName(), sig
/* 146 */         .getDescriptor(), null, 
/*     */         
/* 148 */         TypeUtils.toInternalNames(exceptions));
/* 149 */     if (sig.equals(Constants.SIG_STATIC) && !TypeUtils.isInterface(getAccess())) {
/* 150 */       this.rawStaticInit = v;
/* 151 */       MethodVisitor wrapped = new MethodVisitor(393216, v)
/*     */         {
/*     */           public void visitMaxs(int maxStack, int maxLocals) {}
/*     */           
/*     */           public void visitInsn(int insn) {
/* 156 */             if (insn != 177) {
/* 157 */               super.visitInsn(insn);
/*     */             }
/*     */           }
/*     */         };
/* 161 */       this.staticInit = new CodeEmitter(this, wrapped, access, sig, exceptions);
/* 162 */       if (this.staticHook == null) {
/*     */         
/* 164 */         getStaticHook();
/*     */       } else {
/* 166 */         this.staticInit.invoke_static_this(this.staticHookSig);
/*     */       } 
/* 168 */       return this.staticInit;
/* 169 */     }  if (sig.equals(this.staticHookSig)) {
/* 170 */       return new CodeEmitter(this, v, access, sig, exceptions) {
/*     */           public boolean isStaticHook() {
/* 172 */             return true;
/*     */           }
/*     */         };
/*     */     }
/* 176 */     return new CodeEmitter(this, v, access, sig, exceptions);
/*     */   }
/*     */ 
/*     */   
/*     */   public CodeEmitter begin_static() {
/* 181 */     return begin_method(8, Constants.SIG_STATIC, null);
/*     */   }
/*     */   
/*     */   public void declare_field(int access, String name, Type type, Object value) {
/* 185 */     FieldInfo existing = (FieldInfo)this.fieldInfo.get(name);
/* 186 */     FieldInfo info = new FieldInfo(access, name, type, value);
/* 187 */     if (existing != null) {
/* 188 */       if (!info.equals(existing)) {
/* 189 */         throw new IllegalArgumentException("Field \"" + name + "\" has been declared differently");
/*     */       }
/*     */     } else {
/* 192 */       this.fieldInfo.put(name, info);
/* 193 */       this.cv.visitField(access, name, type.getDescriptor(), null, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isFieldDeclared(String name) {
/* 199 */     return (this.fieldInfo.get(name) != null);
/*     */   }
/*     */   
/*     */   FieldInfo getFieldInfo(String name) {
/* 203 */     FieldInfo field = (FieldInfo)this.fieldInfo.get(name);
/* 204 */     if (field == null) {
/* 205 */       throw new IllegalArgumentException("Field " + name + " is not declared in " + getClassType().getClassName());
/*     */     }
/* 207 */     return field;
/*     */   }
/*     */   
/*     */   static class FieldInfo {
/*     */     int access;
/*     */     String name;
/*     */     Type type;
/*     */     Object value;
/*     */     
/*     */     public FieldInfo(int access, String name, Type type, Object value) {
/* 217 */       this.access = access;
/* 218 */       this.name = name;
/* 219 */       this.type = type;
/* 220 */       this.value = value;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 224 */       if (o == null)
/* 225 */         return false; 
/* 226 */       if (!(o instanceof FieldInfo))
/* 227 */         return false; 
/* 228 */       FieldInfo other = (FieldInfo)o;
/* 229 */       if (this.access != other.access || 
/* 230 */         !this.name.equals(other.name) || 
/* 231 */         !this.type.equals(other.type)) {
/* 232 */         return false;
/*     */       }
/* 234 */       if ((((this.value == null) ? 1 : 0) ^ ((other.value == null) ? 1 : 0)) != 0)
/* 235 */         return false; 
/* 236 */       if (this.value != null && !this.value.equals(other.value))
/* 237 */         return false; 
/* 238 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 242 */       return this.access ^ this.name.hashCode() ^ this.type.hashCode() ^ ((this.value == null) ? 0 : this.value.hashCode());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/* 252 */     begin_class(version, access, name
/*     */         
/* 254 */         .replace('/', '.'), 
/* 255 */         TypeUtils.fromInternalName(superName), 
/* 256 */         TypeUtils.fromInternalNames(interfaces), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 261 */     end_class();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 269 */     declare_field(access, name, Type.getType(desc), value);
/* 270 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 278 */     return begin_method(access, new Signature(name, desc), 
/*     */         
/* 280 */         TypeUtils.fromInternalNames(exceptions));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cglib\core\ClassEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */