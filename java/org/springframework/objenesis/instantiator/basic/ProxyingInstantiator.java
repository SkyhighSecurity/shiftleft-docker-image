/*     */ package org.springframework.objenesis.instantiator.basic;
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
/*     */ 
/*     */ 
/*     */ @Instantiator(Typology.STANDARD)
/*     */ public class ProxyingInstantiator<T>
/*     */   implements ObjectInstantiator<T>
/*     */ {
/*     */   private static final int INDEX_CLASS_THIS = 1;
/*     */   private static final int INDEX_CLASS_SUPERCLASS = 2;
/*     */   private static final int INDEX_UTF8_CONSTRUCTOR_NAME = 3;
/*     */   private static final int INDEX_UTF8_CONSTRUCTOR_DESC = 4;
/*     */   private static final int INDEX_UTF8_CODE_ATTRIBUTE = 5;
/*     */   private static final int INDEX_UTF8_CLASS = 7;
/*     */   private static final int INDEX_UTF8_SUPERCLASS = 8;
/*  49 */   private static int CONSTANT_POOL_COUNT = 9;
/*     */   
/*  51 */   private static final byte[] CODE = new byte[] { 42, -79 };
/*  52 */   private static final int CODE_ATTRIBUTE_LENGTH = 12 + CODE.length;
/*     */   
/*     */   private static final String SUFFIX = "$$$Objenesis";
/*     */   
/*     */   private static final String CONSTRUCTOR_NAME = "<init>";
/*     */   
/*     */   private static final String CONSTRUCTOR_DESC = "()V";
/*     */   
/*     */   private final Class<?> newType;
/*     */   
/*     */   public ProxyingInstantiator(Class<T> type) {
/*  63 */     byte[] classBytes = writeExtendingClass(type, "$$$Objenesis");
/*     */     
/*     */     try {
/*  66 */       this.newType = ClassDefinitionUtils.defineClass(type.getName() + "$$$Objenesis", classBytes, type.getClassLoader());
/*  67 */     } catch (Exception e) {
/*  68 */       throw new ObjenesisException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public T newInstance() {
/*     */     try {
/*  75 */       return (T)this.newType.newInstance();
/*  76 */     } catch (InstantiationException e) {
/*  77 */       throw new ObjenesisException(e);
/*  78 */     } catch (IllegalAccessException e) {
/*  79 */       throw new ObjenesisException(e);
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
/*     */   private static byte[] writeExtendingClass(Class<?> type, String suffix) {
/*  93 */     String parentClazz = ClassDefinitionUtils.classNameToInternalClassName(type.getName());
/*  94 */     String clazz = parentClazz + suffix;
/*     */     
/*  96 */     DataOutputStream in = null;
/*  97 */     ByteArrayOutputStream bIn = new ByteArrayOutputStream(1000);
/*     */     try {
/*  99 */       in = new DataOutputStream(bIn);
/*     */       
/* 101 */       in.write(ClassDefinitionUtils.MAGIC);
/* 102 */       in.write(ClassDefinitionUtils.VERSION);
/* 103 */       in.writeShort(CONSTANT_POOL_COUNT);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 108 */       in.writeByte(7);
/* 109 */       in.writeShort(7);
/*     */ 
/*     */       
/* 112 */       in.writeByte(7);
/* 113 */       in.writeShort(8);
/*     */ 
/*     */       
/* 116 */       in.writeByte(1);
/* 117 */       in.writeUTF("<init>");
/*     */ 
/*     */       
/* 120 */       in.writeByte(1);
/* 121 */       in.writeUTF("()V");
/*     */ 
/*     */       
/* 124 */       in.writeByte(1);
/* 125 */       in.writeUTF("Code");
/*     */ 
/*     */       
/* 128 */       in.writeByte(1);
/* 129 */       in.writeUTF("L" + clazz + ";");
/*     */ 
/*     */       
/* 132 */       in.writeByte(1);
/* 133 */       in.writeUTF(clazz);
/*     */ 
/*     */       
/* 136 */       in.writeByte(1);
/* 137 */       in.writeUTF(parentClazz);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 142 */       in.writeShort(33);
/*     */ 
/*     */       
/* 145 */       in.writeShort(1);
/*     */ 
/*     */       
/* 148 */       in.writeShort(2);
/*     */ 
/*     */       
/* 151 */       in.writeShort(0);
/*     */ 
/*     */       
/* 154 */       in.writeShort(0);
/*     */ 
/*     */       
/* 157 */       in.writeShort(1);
/*     */ 
/*     */       
/* 160 */       in.writeShort(1);
/* 161 */       in.writeShort(3);
/* 162 */       in.writeShort(4);
/* 163 */       in.writeShort(1);
/*     */ 
/*     */       
/* 166 */       in.writeShort(5);
/* 167 */       in.writeInt(CODE_ATTRIBUTE_LENGTH);
/* 168 */       in.writeShort(1);
/* 169 */       in.writeShort(1);
/* 170 */       in.writeInt(CODE.length);
/* 171 */       in.write(CODE);
/* 172 */       in.writeShort(0);
/* 173 */       in.writeShort(0);
/*     */ 
/*     */       
/* 176 */       in.writeShort(0);
/*     */     
/*     */     }
/* 179 */     catch (IOException e) {
/* 180 */       throw new ObjenesisException(e);
/*     */     } finally {
/* 182 */       if (in != null) {
/*     */         try {
/* 184 */           in.close();
/* 185 */         } catch (IOException e) {
/* 186 */           throw new ObjenesisException(e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return bIn.toByteArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\instantiator\basic\ProxyingInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */