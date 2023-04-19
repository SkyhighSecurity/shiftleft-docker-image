/*     */ package org.springframework.asm;
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
/*     */ final class ModuleWriter
/*     */   extends ModuleVisitor
/*     */ {
/*     */   private final ClassWriter cw;
/*     */   int size;
/*     */   int attributeCount;
/*     */   int attributesSize;
/*     */   private final int name;
/*     */   private final int access;
/*     */   private final int version;
/*     */   private int mainClass;
/*     */   private int packageCount;
/*     */   private ByteVector packages;
/*     */   private int requireCount;
/*     */   private ByteVector requires;
/*     */   private int exportCount;
/*     */   private ByteVector exports;
/*     */   private int openCount;
/*     */   private ByteVector opens;
/*     */   private int useCount;
/*     */   private ByteVector uses;
/*     */   private int provideCount;
/*     */   private ByteVector provides;
/*     */   
/*     */   ModuleWriter(ClassWriter cw, int name, int access, int version) {
/* 146 */     super(393216);
/* 147 */     this.cw = cw;
/* 148 */     this.size = 16;
/* 149 */     this.name = name;
/* 150 */     this.access = access;
/* 151 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitMainClass(String mainClass) {
/* 156 */     if (this.mainClass == 0) {
/* 157 */       this.cw.newUTF8("ModuleMainClass");
/* 158 */       this.attributeCount++;
/* 159 */       this.attributesSize += 8;
/*     */     } 
/* 161 */     this.mainClass = this.cw.newClass(mainClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitPackage(String packaze) {
/* 166 */     if (this.packages == null) {
/*     */       
/* 168 */       this.cw.newUTF8("ModulePackages");
/* 169 */       this.packages = new ByteVector();
/* 170 */       this.attributeCount++;
/* 171 */       this.attributesSize += 8;
/*     */     } 
/* 173 */     this.packages.putShort(this.cw.newPackage(packaze));
/* 174 */     this.packageCount++;
/* 175 */     this.attributesSize += 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitRequire(String module, int access, String version) {
/* 180 */     if (this.requires == null) {
/* 181 */       this.requires = new ByteVector();
/*     */     }
/* 183 */     this.requires.putShort(this.cw.newModule(module))
/* 184 */       .putShort(access)
/* 185 */       .putShort((version == null) ? 0 : this.cw.newUTF8(version));
/* 186 */     this.requireCount++;
/* 187 */     this.size += 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitExport(String packaze, int access, String... modules) {
/* 192 */     if (this.exports == null) {
/* 193 */       this.exports = new ByteVector();
/*     */     }
/* 195 */     this.exports.putShort(this.cw.newPackage(packaze)).putShort(access);
/* 196 */     if (modules == null) {
/* 197 */       this.exports.putShort(0);
/* 198 */       this.size += 6;
/*     */     } else {
/* 200 */       this.exports.putShort(modules.length);
/* 201 */       for (String module : modules) {
/* 202 */         this.exports.putShort(this.cw.newModule(module));
/*     */       }
/* 204 */       this.size += 6 + 2 * modules.length;
/*     */     } 
/* 206 */     this.exportCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitOpen(String packaze, int access, String... modules) {
/* 211 */     if (this.opens == null) {
/* 212 */       this.opens = new ByteVector();
/*     */     }
/* 214 */     this.opens.putShort(this.cw.newPackage(packaze)).putShort(access);
/* 215 */     if (modules == null) {
/* 216 */       this.opens.putShort(0);
/* 217 */       this.size += 6;
/*     */     } else {
/* 219 */       this.opens.putShort(modules.length);
/* 220 */       for (String module : modules) {
/* 221 */         this.opens.putShort(this.cw.newModule(module));
/*     */       }
/* 223 */       this.size += 6 + 2 * modules.length;
/*     */     } 
/* 225 */     this.openCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitUse(String service) {
/* 230 */     if (this.uses == null) {
/* 231 */       this.uses = new ByteVector();
/*     */     }
/* 233 */     this.uses.putShort(this.cw.newClass(service));
/* 234 */     this.useCount++;
/* 235 */     this.size += 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitProvide(String service, String... providers) {
/* 240 */     if (this.provides == null) {
/* 241 */       this.provides = new ByteVector();
/*     */     }
/* 243 */     this.provides.putShort(this.cw.newClass(service));
/* 244 */     this.provides.putShort(providers.length);
/* 245 */     for (String provider : providers) {
/* 246 */       this.provides.putShort(this.cw.newClass(provider));
/*     */     }
/* 248 */     this.provideCount++;
/* 249 */     this.size += 4 + 2 * providers.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {}
/*     */ 
/*     */   
/*     */   void putAttributes(ByteVector out) {
/* 258 */     if (this.mainClass != 0) {
/* 259 */       out.putShort(this.cw.newUTF8("ModuleMainClass")).putInt(2).putShort(this.mainClass);
/*     */     }
/* 261 */     if (this.packages != null) {
/* 262 */       out.putShort(this.cw.newUTF8("ModulePackages"))
/* 263 */         .putInt(2 + 2 * this.packageCount)
/* 264 */         .putShort(this.packageCount)
/* 265 */         .putByteArray(this.packages.data, 0, this.packages.length);
/*     */     }
/*     */   }
/*     */   
/*     */   void put(ByteVector out) {
/* 270 */     out.putInt(this.size);
/* 271 */     out.putShort(this.name).putShort(this.access).putShort(this.version);
/* 272 */     out.putShort(this.requireCount);
/* 273 */     if (this.requires != null) {
/* 274 */       out.putByteArray(this.requires.data, 0, this.requires.length);
/*     */     }
/* 276 */     out.putShort(this.exportCount);
/* 277 */     if (this.exports != null) {
/* 278 */       out.putByteArray(this.exports.data, 0, this.exports.length);
/*     */     }
/* 280 */     out.putShort(this.openCount);
/* 281 */     if (this.opens != null) {
/* 282 */       out.putByteArray(this.opens.data, 0, this.opens.length);
/*     */     }
/* 284 */     out.putShort(this.useCount);
/* 285 */     if (this.uses != null) {
/* 286 */       out.putByteArray(this.uses.data, 0, this.uses.length);
/*     */     }
/* 288 */     out.putShort(this.provideCount);
/* 289 */     if (this.provides != null)
/* 290 */       out.putByteArray(this.provides.data, 0, this.provides.length); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\asm\ModuleWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */