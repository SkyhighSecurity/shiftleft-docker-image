/*     */ package org.springframework.objenesis.instantiator.sun;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*     */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*     */ import org.springframework.objenesis.instantiator.annotations.Typology;
/*     */ import org.springframework.objenesis.instantiator.util.ClassDefinitionUtils;
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
/*     */ @Instantiator(Typology.STANDARD)
/*     */ public class MagicInstantiator<T>
/*     */   implements ObjectInstantiator<T>
/*     */ {
/*  40 */   private static final String MAGIC_ACCESSOR = getMagicClass();
/*     */   
/*     */   private static final int INDEX_CLASS_THIS = 1;
/*     */   
/*     */   private static final int INDEX_CLASS_SUPERCLASS = 2;
/*     */   private static final int INDEX_UTF8_CONSTRUCTOR_NAME = 3;
/*     */   private static final int INDEX_UTF8_CONSTRUCTOR_DESC = 4;
/*     */   private static final int INDEX_UTF8_CODE_ATTRIBUTE = 5;
/*     */   private static final int INDEX_UTF8_INSTANTIATOR_CLASS = 7;
/*     */   private static final int INDEX_UTF8_SUPERCLASS = 8;
/*     */   private static final int INDEX_CLASS_INTERFACE = 9;
/*     */   private static final int INDEX_UTF8_INTERFACE = 10;
/*     */   private static final int INDEX_UTF8_NEWINSTANCE_NAME = 11;
/*     */   private static final int INDEX_UTF8_NEWINSTANCE_DESC = 12;
/*     */   private static final int INDEX_METHODREF_OBJECT_CONSTRUCTOR = 13;
/*     */   private static final int INDEX_CLASS_OBJECT = 14;
/*     */   private static final int INDEX_UTF8_OBJECT = 15;
/*     */   private static final int INDEX_NAMEANDTYPE_DEFAULT_CONSTRUCTOR = 16;
/*     */   private static final int INDEX_CLASS_TYPE = 17;
/*     */   private static final int INDEX_UTF8_TYPE = 18;
/*  60 */   private static int CONSTANT_POOL_COUNT = 19;
/*     */   
/*  62 */   private static final byte[] CONSTRUCTOR_CODE = new byte[] { 42, -73, 0, 13, -79 };
/*  63 */   private static final int CONSTRUCTOR_CODE_ATTRIBUTE_LENGTH = 12 + CONSTRUCTOR_CODE.length;
/*     */   
/*  65 */   private static final byte[] NEWINSTANCE_CODE = new byte[] { -69, 0, 17, 89, -73, 0, 13, -80 };
/*  66 */   private static final int NEWINSTANCE_CODE_ATTRIBUTE_LENGTH = 12 + NEWINSTANCE_CODE.length;
/*     */   
/*     */   private static final String CONSTRUCTOR_NAME = "<init>";
/*     */   
/*     */   private static final String CONSTRUCTOR_DESC = "()V";
/*     */   private ObjectInstantiator<T> instantiator;
/*     */   
/*     */   public MagicInstantiator(Class<T> type) {
/*  74 */     this.instantiator = newInstantiatorOf(type);
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
/*     */   public ObjectInstantiator<T> getInstantiator() {
/*  87 */     return this.instantiator;
/*     */   }
/*     */   
/*     */   private <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
/*  91 */     String suffix = type.getSimpleName();
/*  92 */     String className = getClass().getName() + "$$$" + suffix;
/*     */     
/*  94 */     Class<ObjectInstantiator<T>> clazz = ClassDefinitionUtils.getExistingClass(getClass().getClassLoader(), className);
/*     */     
/*  96 */     if (clazz == null) {
/*  97 */       byte[] classBytes = writeExtendingClass(type, className);
/*     */       
/*     */       try {
/* 100 */         clazz = ClassDefinitionUtils.defineClass(className, classBytes, getClass().getClassLoader());
/* 101 */       } catch (Exception e) {
/* 102 */         throw new ObjenesisException(e);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 107 */       return clazz.newInstance();
/* 108 */     } catch (InstantiationException e) {
/* 109 */       throw new ObjenesisException(e);
/* 110 */     } catch (IllegalAccessException e) {
/* 111 */       throw new ObjenesisException(e);
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
/*     */   private byte[] writeExtendingClass(Class<?> type, String className) {
/* 125 */     String clazz = ClassDefinitionUtils.classNameToInternalClassName(className);
/*     */     
/* 127 */     DataOutputStream in = null;
/* 128 */     ByteArrayOutputStream bIn = new ByteArrayOutputStream(1000);
/*     */     try {
/* 130 */       in = new DataOutputStream(bIn);
/*     */       
/* 132 */       in.write(ClassDefinitionUtils.MAGIC);
/* 133 */       in.write(ClassDefinitionUtils.VERSION);
/* 134 */       in.writeShort(CONSTANT_POOL_COUNT);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 139 */       in.writeByte(7);
/* 140 */       in.writeShort(7);
/*     */ 
/*     */       
/* 143 */       in.writeByte(7);
/* 144 */       in.writeShort(8);
/*     */ 
/*     */       
/* 147 */       in.writeByte(1);
/* 148 */       in.writeUTF("<init>");
/*     */ 
/*     */       
/* 151 */       in.writeByte(1);
/* 152 */       in.writeUTF("()V");
/*     */ 
/*     */       
/* 155 */       in.writeByte(1);
/* 156 */       in.writeUTF("Code");
/*     */ 
/*     */       
/* 159 */       in.writeByte(1);
/* 160 */       in.writeUTF("L" + clazz + ";");
/*     */ 
/*     */       
/* 163 */       in.writeByte(1);
/* 164 */       in.writeUTF(clazz);
/*     */ 
/*     */       
/* 167 */       in.writeByte(1);
/*     */       
/* 169 */       in.writeUTF(MAGIC_ACCESSOR);
/*     */ 
/*     */       
/* 172 */       in.writeByte(7);
/* 173 */       in.writeShort(10);
/*     */ 
/*     */       
/* 176 */       in.writeByte(1);
/* 177 */       in.writeUTF(ObjectInstantiator.class.getName().replace('.', '/'));
/*     */ 
/*     */       
/* 180 */       in.writeByte(1);
/* 181 */       in.writeUTF("newInstance");
/*     */ 
/*     */       
/* 184 */       in.writeByte(1);
/* 185 */       in.writeUTF("()Ljava/lang/Object;");
/*     */ 
/*     */       
/* 188 */       in.writeByte(10);
/* 189 */       in.writeShort(14);
/* 190 */       in.writeShort(16);
/*     */ 
/*     */       
/* 193 */       in.writeByte(7);
/* 194 */       in.writeShort(15);
/*     */ 
/*     */       
/* 197 */       in.writeByte(1);
/* 198 */       in.writeUTF("java/lang/Object");
/*     */ 
/*     */       
/* 201 */       in.writeByte(12);
/* 202 */       in.writeShort(3);
/* 203 */       in.writeShort(4);
/*     */ 
/*     */       
/* 206 */       in.writeByte(7);
/* 207 */       in.writeShort(18);
/*     */ 
/*     */       
/* 210 */       in.writeByte(1);
/* 211 */       in.writeUTF(ClassDefinitionUtils.classNameToInternalClassName(type.getName()));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 216 */       in.writeShort(49);
/*     */ 
/*     */       
/* 219 */       in.writeShort(1);
/*     */ 
/*     */       
/* 222 */       in.writeShort(2);
/*     */ 
/*     */       
/* 225 */       in.writeShort(1);
/* 226 */       in.writeShort(9);
/*     */ 
/*     */       
/* 229 */       in.writeShort(0);
/*     */ 
/*     */       
/* 232 */       in.writeShort(2);
/*     */ 
/*     */       
/* 235 */       in.writeShort(1);
/* 236 */       in.writeShort(3);
/* 237 */       in.writeShort(4);
/* 238 */       in.writeShort(1);
/*     */ 
/*     */       
/* 241 */       in.writeShort(5);
/* 242 */       in.writeInt(CONSTRUCTOR_CODE_ATTRIBUTE_LENGTH);
/* 243 */       in.writeShort(0);
/* 244 */       in.writeShort(1);
/* 245 */       in.writeInt(CONSTRUCTOR_CODE.length);
/* 246 */       in.write(CONSTRUCTOR_CODE);
/* 247 */       in.writeShort(0);
/* 248 */       in.writeShort(0);
/*     */ 
/*     */       
/* 251 */       in.writeShort(1);
/* 252 */       in.writeShort(11);
/* 253 */       in.writeShort(12);
/* 254 */       in.writeShort(1);
/*     */ 
/*     */       
/* 257 */       in.writeShort(5);
/* 258 */       in.writeInt(NEWINSTANCE_CODE_ATTRIBUTE_LENGTH);
/* 259 */       in.writeShort(2);
/* 260 */       in.writeShort(1);
/* 261 */       in.writeInt(NEWINSTANCE_CODE.length);
/* 262 */       in.write(NEWINSTANCE_CODE);
/* 263 */       in.writeShort(0);
/* 264 */       in.writeShort(0);
/*     */ 
/*     */       
/* 267 */       in.writeShort(0);
/*     */     }
/* 269 */     catch (IOException e) {
/* 270 */       throw new ObjenesisException(e);
/*     */     } finally {
/* 272 */       if (in != null) {
/*     */         try {
/* 274 */           in.close();
/* 275 */         } catch (IOException e) {
/* 276 */           throw new ObjenesisException(e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 281 */     return bIn.toByteArray();
/*     */   }
/*     */   
/*     */   public T newInstance() {
/* 285 */     return (T)this.instantiator.newInstance();
/*     */   }
/*     */   
/*     */   private static String getMagicClass() {
/*     */     try {
/* 290 */       Class.forName("sun.reflect.MagicAccessorImpl", false, MagicInstantiator.class.getClassLoader());
/* 291 */       return "sun/reflect/MagicAccessorImpl";
/* 292 */     } catch (ClassNotFoundException e) {
/* 293 */       return "jdk/internal/reflect/MagicAccessorImpl";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\instantiator\sun\MagicInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */