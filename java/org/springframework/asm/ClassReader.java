/*      */ package org.springframework.asm;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
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
/*      */ public class ClassReader
/*      */ {
/*      */   static final boolean SIGNATURES = true;
/*      */   static final boolean ANNOTATIONS = true;
/*      */   static final boolean FRAMES = true;
/*      */   static final boolean WRITER = true;
/*      */   static final boolean RESIZE = true;
/*      */   public static final int SKIP_CODE = 1;
/*      */   public static final int SKIP_DEBUG = 2;
/*      */   public static final int SKIP_FRAMES = 4;
/*      */   public static final int EXPAND_FRAMES = 8;
/*      */   static final int EXPAND_ASM_INSNS = 256;
/*      */   public final byte[] b;
/*      */   private final int[] items;
/*      */   private final String[] strings;
/*      */   private final int maxStringLength;
/*      */   public final int header;
/*      */   
/*      */   public ClassReader(byte[] b) {
/*  168 */     this(b, 0, b.length);
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
/*      */   public ClassReader(byte[] b, int off, int len) {
/*  182 */     this.b = b;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  190 */     this.items = new int[readUnsignedShort(off + 8)];
/*  191 */     int n = this.items.length;
/*  192 */     this.strings = new String[n];
/*  193 */     int max = 0;
/*  194 */     int index = off + 10;
/*  195 */     for (int i = 1; i < n; i++) {
/*  196 */       int size; this.items[i] = index + 1;
/*      */       
/*  198 */       switch (b[index]) {
/*      */         case 3:
/*      */         case 4:
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*      */         case 18:
/*  206 */           size = 5;
/*      */           break;
/*      */         case 5:
/*      */         case 6:
/*  210 */           size = 9;
/*  211 */           i++;
/*      */           break;
/*      */         case 1:
/*  214 */           size = 3 + readUnsignedShort(index + 1);
/*  215 */           if (size > max) {
/*  216 */             max = size;
/*      */           }
/*      */           break;
/*      */         case 15:
/*  220 */           size = 4;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/*  228 */           size = 3;
/*      */           break;
/*      */       } 
/*  231 */       index += size;
/*      */     } 
/*  233 */     this.maxStringLength = max;
/*      */     
/*  235 */     this.header = index;
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
/*      */   public int getAccess() {
/*  248 */     return readUnsignedShort(this.header);
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
/*      */   public String getClassName() {
/*  260 */     return readClass(this.header + 2, new char[this.maxStringLength]);
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
/*      */   public String getSuperName() {
/*  274 */     return readClass(this.header + 4, new char[this.maxStringLength]);
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
/*      */   public String[] getInterfaces() {
/*  287 */     int index = this.header + 6;
/*  288 */     int n = readUnsignedShort(index);
/*  289 */     String[] interfaces = new String[n];
/*  290 */     if (n > 0) {
/*  291 */       char[] buf = new char[this.maxStringLength];
/*  292 */       for (int i = 0; i < n; i++) {
/*  293 */         index += 2;
/*  294 */         interfaces[i] = readClass(index, buf);
/*      */       } 
/*      */     } 
/*  297 */     return interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void copyPool(ClassWriter classWriter) {
/*  308 */     char[] buf = new char[this.maxStringLength];
/*  309 */     int ll = this.items.length;
/*  310 */     Item[] items2 = new Item[ll];
/*  311 */     for (int i = 1; i < ll; i++) {
/*  312 */       int nameType; String s; int fieldOrMethodRef, index = this.items[i];
/*  313 */       int tag = this.b[index - 1];
/*  314 */       Item item = new Item(i);
/*      */       
/*  316 */       switch (tag) {
/*      */         case 9:
/*      */         case 10:
/*      */         case 11:
/*  320 */           nameType = this.items[readUnsignedShort(index + 2)];
/*  321 */           item.set(tag, readClass(index, buf), readUTF8(nameType, buf), 
/*  322 */               readUTF8(nameType + 2, buf));
/*      */           break;
/*      */         case 3:
/*  325 */           item.set(readInt(index));
/*      */           break;
/*      */         case 4:
/*  328 */           item.set(Float.intBitsToFloat(readInt(index)));
/*      */           break;
/*      */         case 12:
/*  331 */           item.set(tag, readUTF8(index, buf), readUTF8(index + 2, buf), null);
/*      */           break;
/*      */         
/*      */         case 5:
/*  335 */           item.set(readLong(index));
/*  336 */           i++;
/*      */           break;
/*      */         case 6:
/*  339 */           item.set(Double.longBitsToDouble(readLong(index)));
/*  340 */           i++;
/*      */           break;
/*      */         case 1:
/*  343 */           s = this.strings[i];
/*  344 */           if (s == null) {
/*  345 */             index = this.items[i];
/*  346 */             s = this.strings[i] = readUTF(index + 2, 
/*  347 */                 readUnsignedShort(index), buf);
/*      */           } 
/*  349 */           item.set(tag, s, null, null);
/*      */           break;
/*      */         
/*      */         case 15:
/*  353 */           fieldOrMethodRef = this.items[readUnsignedShort(index + 1)];
/*  354 */           nameType = this.items[readUnsignedShort(fieldOrMethodRef + 2)];
/*  355 */           item.set(20 + readByte(index), 
/*  356 */               readClass(fieldOrMethodRef, buf), 
/*  357 */               readUTF8(nameType, buf), readUTF8(nameType + 2, buf));
/*      */           break;
/*      */         
/*      */         case 18:
/*  361 */           if (classWriter.bootstrapMethods == null) {
/*  362 */             copyBootstrapMethods(classWriter, items2, buf);
/*      */           }
/*  364 */           nameType = this.items[readUnsignedShort(index + 2)];
/*  365 */           item.set(readUTF8(nameType, buf), readUTF8(nameType + 2, buf), 
/*  366 */               readUnsignedShort(index));
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/*  374 */           item.set(tag, readUTF8(index, buf), null, null);
/*      */           break;
/*      */       } 
/*      */       
/*  378 */       int index2 = item.hashCode % items2.length;
/*  379 */       item.next = items2[index2];
/*  380 */       items2[index2] = item;
/*      */     } 
/*      */     
/*  383 */     int off = this.items[1] - 1;
/*  384 */     classWriter.pool.putByteArray(this.b, off, this.header - off);
/*  385 */     classWriter.items = items2;
/*  386 */     classWriter.threshold = (int)(0.75D * ll);
/*  387 */     classWriter.index = ll;
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
/*      */   private void copyBootstrapMethods(ClassWriter classWriter, Item[] items, char[] c) {
/*  400 */     int u = getAttributes();
/*  401 */     boolean found = false;
/*  402 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/*  403 */       String attrName = readUTF8(u + 2, c);
/*  404 */       if ("BootstrapMethods".equals(attrName)) {
/*  405 */         found = true;
/*      */         break;
/*      */       } 
/*  408 */       u += 6 + readInt(u + 4);
/*      */     } 
/*  410 */     if (!found) {
/*      */       return;
/*      */     }
/*      */     
/*  414 */     int boostrapMethodCount = readUnsignedShort(u + 8);
/*  415 */     for (int j = 0, v = u + 10; j < boostrapMethodCount; j++) {
/*  416 */       int position = v - u - 10;
/*  417 */       int hashCode = readConst(readUnsignedShort(v), c).hashCode();
/*  418 */       for (int k = readUnsignedShort(v + 2); k > 0; k--) {
/*  419 */         hashCode ^= readConst(readUnsignedShort(v + 4), c).hashCode();
/*  420 */         v += 2;
/*      */       } 
/*  422 */       v += 4;
/*  423 */       Item item = new Item(j);
/*  424 */       item.set(position, hashCode & Integer.MAX_VALUE);
/*  425 */       int index = item.hashCode % items.length;
/*  426 */       item.next = items[index];
/*  427 */       items[index] = item;
/*      */     } 
/*  429 */     int attrSize = readInt(u + 4);
/*  430 */     ByteVector bootstrapMethods = new ByteVector(attrSize + 62);
/*  431 */     bootstrapMethods.putByteArray(this.b, u + 10, attrSize - 2);
/*  432 */     classWriter.bootstrapMethodsCount = boostrapMethodCount;
/*  433 */     classWriter.bootstrapMethods = bootstrapMethods;
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
/*      */   public ClassReader(InputStream is) throws IOException {
/*  445 */     this(readClass(is, false));
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
/*      */   public ClassReader(String name) throws IOException {
/*  457 */     this(readClass(
/*  458 */           ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class"), true));
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
/*      */   
/*      */   private static byte[] readClass(InputStream is, boolean close) throws IOException {
/*  475 */     if (is == null) {
/*  476 */       throw new IOException("Class not found");
/*      */     }
/*      */     try {
/*  479 */       byte[] b = new byte[is.available()];
/*  480 */       int len = 0;
/*      */       while (true) {
/*  482 */         int n = is.read(b, len, b.length - len);
/*  483 */         if (n == -1) {
/*  484 */           if (len < b.length) {
/*  485 */             byte[] c = new byte[len];
/*  486 */             System.arraycopy(b, 0, c, 0, len);
/*  487 */             b = c;
/*      */           } 
/*  489 */           return b;
/*      */         } 
/*  491 */         len += n;
/*  492 */         if (len == b.length) {
/*  493 */           int last = is.read();
/*  494 */           if (last < 0) {
/*  495 */             return b;
/*      */           }
/*  497 */           byte[] c = new byte[b.length + 1000];
/*  498 */           System.arraycopy(b, 0, c, 0, len);
/*  499 */           c[len++] = (byte)last;
/*  500 */           b = c;
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  504 */       if (close) {
/*  505 */         is.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void accept(ClassVisitor classVisitor, int flags) {
/*  527 */     accept(classVisitor, new Attribute[0], flags);
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
/*      */   public void accept(ClassVisitor classVisitor, Attribute[] attrs, int flags) {
/*  553 */     int u = this.header;
/*  554 */     char[] c = new char[this.maxStringLength];
/*      */     
/*  556 */     Context context = new Context();
/*  557 */     context.attrs = attrs;
/*  558 */     context.flags = flags;
/*  559 */     context.buffer = c;
/*      */ 
/*      */     
/*  562 */     int access = readUnsignedShort(u);
/*  563 */     String name = readClass(u + 2, c);
/*  564 */     String superClass = readClass(u + 4, c);
/*  565 */     String[] interfaces = new String[readUnsignedShort(u + 6)];
/*  566 */     u += 8;
/*  567 */     for (int i = 0; i < interfaces.length; i++) {
/*  568 */       interfaces[i] = readClass(u, c);
/*  569 */       u += 2;
/*      */     } 
/*      */ 
/*      */     
/*  573 */     String signature = null;
/*  574 */     String sourceFile = null;
/*  575 */     String sourceDebug = null;
/*  576 */     String enclosingOwner = null;
/*  577 */     String enclosingName = null;
/*  578 */     String enclosingDesc = null;
/*  579 */     String moduleMainClass = null;
/*  580 */     int anns = 0;
/*  581 */     int ianns = 0;
/*  582 */     int tanns = 0;
/*  583 */     int itanns = 0;
/*  584 */     int innerClasses = 0;
/*  585 */     int module = 0;
/*  586 */     int packages = 0;
/*  587 */     Attribute attributes = null;
/*      */     
/*  589 */     u = getAttributes(); int j;
/*  590 */     for (j = readUnsignedShort(u); j > 0; j--) {
/*  591 */       String attrName = readUTF8(u + 2, c);
/*      */ 
/*      */       
/*  594 */       if ("SourceFile".equals(attrName)) {
/*  595 */         sourceFile = readUTF8(u + 8, c);
/*  596 */       } else if ("InnerClasses".equals(attrName)) {
/*  597 */         innerClasses = u + 8;
/*  598 */       } else if ("EnclosingMethod".equals(attrName)) {
/*  599 */         enclosingOwner = readClass(u + 8, c);
/*  600 */         int item = readUnsignedShort(u + 10);
/*  601 */         if (item != 0) {
/*  602 */           enclosingName = readUTF8(this.items[item], c);
/*  603 */           enclosingDesc = readUTF8(this.items[item] + 2, c);
/*      */         } 
/*  605 */       } else if ("Signature".equals(attrName)) {
/*  606 */         signature = readUTF8(u + 8, c);
/*  607 */       } else if ("RuntimeVisibleAnnotations"
/*  608 */         .equals(attrName)) {
/*  609 */         anns = u + 8;
/*  610 */       } else if ("RuntimeVisibleTypeAnnotations"
/*  611 */         .equals(attrName)) {
/*  612 */         tanns = u + 8;
/*  613 */       } else if ("Deprecated".equals(attrName)) {
/*  614 */         access |= 0x20000;
/*  615 */       } else if ("Synthetic".equals(attrName)) {
/*  616 */         access |= 0x41000;
/*      */       }
/*  618 */       else if ("SourceDebugExtension".equals(attrName)) {
/*  619 */         int len = readInt(u + 4);
/*  620 */         sourceDebug = readUTF(u + 8, len, new char[len]);
/*  621 */       } else if ("RuntimeInvisibleAnnotations"
/*  622 */         .equals(attrName)) {
/*  623 */         ianns = u + 8;
/*  624 */       } else if ("RuntimeInvisibleTypeAnnotations"
/*  625 */         .equals(attrName)) {
/*  626 */         itanns = u + 8;
/*  627 */       } else if ("Module".equals(attrName)) {
/*  628 */         module = u + 8;
/*  629 */       } else if ("ModuleMainClass".equals(attrName)) {
/*  630 */         moduleMainClass = readClass(u + 8, c);
/*  631 */       } else if ("ModulePackages".equals(attrName)) {
/*  632 */         packages = u + 10;
/*  633 */       } else if ("BootstrapMethods".equals(attrName)) {
/*  634 */         int[] bootstrapMethods = new int[readUnsignedShort(u + 8)];
/*  635 */         for (int k = 0, v = u + 10; k < bootstrapMethods.length; k++) {
/*  636 */           bootstrapMethods[k] = v;
/*  637 */           v += 2 + readUnsignedShort(v + 2) << 1;
/*      */         } 
/*  639 */         context.bootstrapMethods = bootstrapMethods;
/*      */       } else {
/*  641 */         Attribute attr = readAttribute(attrs, attrName, u + 8, 
/*  642 */             readInt(u + 4), c, -1, null);
/*  643 */         if (attr != null) {
/*  644 */           attr.next = attributes;
/*  645 */           attributes = attr;
/*      */         } 
/*      */       } 
/*  648 */       u += 6 + readInt(u + 4);
/*      */     } 
/*      */ 
/*      */     
/*  652 */     classVisitor.visit(readInt(this.items[1] - 7), access, name, signature, superClass, interfaces);
/*      */ 
/*      */ 
/*      */     
/*  656 */     if ((flags & 0x2) == 0 && (sourceFile != null || sourceDebug != null))
/*      */     {
/*  658 */       classVisitor.visitSource(sourceFile, sourceDebug);
/*      */     }
/*      */ 
/*      */     
/*  662 */     if (module != 0) {
/*  663 */       readModule(classVisitor, context, module, moduleMainClass, packages);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  668 */     if (enclosingOwner != null) {
/*  669 */       classVisitor.visitOuterClass(enclosingOwner, enclosingName, enclosingDesc);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  674 */     if (anns != 0) {
/*  675 */       int v; for (j = readUnsignedShort(anns), v = anns + 2; j > 0; j--) {
/*  676 */         v = readAnnotationValues(v + 2, c, true, classVisitor
/*  677 */             .visitAnnotation(readUTF8(v, c), true));
/*      */       }
/*      */     } 
/*  680 */     if (ianns != 0) {
/*  681 */       int v; for (j = readUnsignedShort(ianns), v = ianns + 2; j > 0; j--) {
/*  682 */         v = readAnnotationValues(v + 2, c, true, classVisitor
/*  683 */             .visitAnnotation(readUTF8(v, c), false));
/*      */       }
/*      */     } 
/*  686 */     if (tanns != 0) {
/*  687 */       int v; for (j = readUnsignedShort(tanns), v = tanns + 2; j > 0; j--) {
/*  688 */         v = readAnnotationTarget(context, v);
/*  689 */         v = readAnnotationValues(v + 2, c, true, classVisitor
/*  690 */             .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  691 */               readUTF8(v, c), true));
/*      */       } 
/*      */     } 
/*  694 */     if (itanns != 0) {
/*  695 */       int v; for (j = readUnsignedShort(itanns), v = itanns + 2; j > 0; j--) {
/*  696 */         v = readAnnotationTarget(context, v);
/*  697 */         v = readAnnotationValues(v + 2, c, true, classVisitor
/*  698 */             .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  699 */               readUTF8(v, c), false));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  704 */     while (attributes != null) {
/*  705 */       Attribute attr = attributes.next;
/*  706 */       attributes.next = null;
/*  707 */       classVisitor.visitAttribute(attributes);
/*  708 */       attributes = attr;
/*      */     } 
/*      */ 
/*      */     
/*  712 */     if (innerClasses != 0) {
/*  713 */       int v = innerClasses + 2;
/*  714 */       for (int k = readUnsignedShort(innerClasses); k > 0; k--) {
/*  715 */         classVisitor.visitInnerClass(readClass(v, c), 
/*  716 */             readClass(v + 2, c), readUTF8(v + 4, c), 
/*  717 */             readUnsignedShort(v + 6));
/*  718 */         v += 8;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  723 */     u = this.header + 10 + 2 * interfaces.length;
/*  724 */     for (j = readUnsignedShort(u - 2); j > 0; j--) {
/*  725 */       u = readField(classVisitor, context, u);
/*      */     }
/*  727 */     u += 2;
/*  728 */     for (j = readUnsignedShort(u - 2); j > 0; j--) {
/*  729 */       u = readMethod(classVisitor, context, u);
/*      */     }
/*      */ 
/*      */     
/*  733 */     classVisitor.visitEnd();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readModule(ClassVisitor classVisitor, Context context, int u, String mainClass, int packages) {
/*  754 */     char[] buffer = context.buffer;
/*      */ 
/*      */     
/*  757 */     String name = readModule(u, buffer);
/*  758 */     int flags = readUnsignedShort(u + 2);
/*  759 */     String version = readUTF8(u + 4, buffer);
/*  760 */     u += 6;
/*      */     
/*  762 */     ModuleVisitor mv = classVisitor.visitModule(name, flags, version);
/*  763 */     if (mv == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  768 */     if (mainClass != null) {
/*  769 */       mv.visitMainClass(mainClass);
/*      */     }
/*      */     
/*  772 */     if (packages != 0) {
/*  773 */       for (int j = readUnsignedShort(packages - 2); j > 0; j--) {
/*  774 */         String packaze = readPackage(packages, buffer);
/*  775 */         mv.visitPackage(packaze);
/*  776 */         packages += 2;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  781 */     u += 2; int i;
/*  782 */     for (i = readUnsignedShort(u - 2); i > 0; i--) {
/*  783 */       String module = readModule(u, buffer);
/*  784 */       int access = readUnsignedShort(u + 2);
/*  785 */       String requireVersion = readUTF8(u + 4, buffer);
/*  786 */       mv.visitRequire(module, access, requireVersion);
/*  787 */       u += 6;
/*      */     } 
/*      */ 
/*      */     
/*  791 */     u += 2;
/*  792 */     for (i = readUnsignedShort(u - 2); i > 0; i--) {
/*  793 */       String export = readPackage(u, buffer);
/*  794 */       int access = readUnsignedShort(u + 2);
/*  795 */       int exportToCount = readUnsignedShort(u + 4);
/*  796 */       u += 6;
/*  797 */       String[] tos = null;
/*  798 */       if (exportToCount != 0) {
/*  799 */         tos = new String[exportToCount];
/*  800 */         for (int j = 0; j < tos.length; j++) {
/*  801 */           tos[j] = readModule(u, buffer);
/*  802 */           u += 2;
/*      */         } 
/*      */       } 
/*  805 */       mv.visitExport(export, access, tos);
/*      */     } 
/*      */ 
/*      */     
/*  809 */     u += 2;
/*  810 */     for (i = readUnsignedShort(u - 2); i > 0; i--) {
/*  811 */       String open = readPackage(u, buffer);
/*  812 */       int access = readUnsignedShort(u + 2);
/*  813 */       int openToCount = readUnsignedShort(u + 4);
/*  814 */       u += 6;
/*  815 */       String[] tos = null;
/*  816 */       if (openToCount != 0) {
/*  817 */         tos = new String[openToCount];
/*  818 */         for (int j = 0; j < tos.length; j++) {
/*  819 */           tos[j] = readModule(u, buffer);
/*  820 */           u += 2;
/*      */         } 
/*      */       } 
/*  823 */       mv.visitOpen(open, access, tos);
/*      */     } 
/*      */ 
/*      */     
/*  827 */     u += 2;
/*  828 */     for (i = readUnsignedShort(u - 2); i > 0; i--) {
/*  829 */       mv.visitUse(readClass(u, buffer));
/*  830 */       u += 2;
/*      */     } 
/*      */ 
/*      */     
/*  834 */     u += 2;
/*  835 */     for (i = readUnsignedShort(u - 2); i > 0; i--) {
/*  836 */       String service = readClass(u, buffer);
/*  837 */       int provideWithCount = readUnsignedShort(u + 2);
/*  838 */       u += 4;
/*  839 */       String[] withs = new String[provideWithCount];
/*  840 */       for (int j = 0; j < withs.length; j++) {
/*  841 */         withs[j] = readClass(u, buffer);
/*  842 */         u += 2;
/*      */       } 
/*  844 */       mv.visitProvide(service, withs);
/*      */     } 
/*      */     
/*  847 */     mv.visitEnd();
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
/*      */   
/*      */   private int readField(ClassVisitor classVisitor, Context context, int u) {
/*  864 */     char[] c = context.buffer;
/*  865 */     int access = readUnsignedShort(u);
/*  866 */     String name = readUTF8(u + 2, c);
/*  867 */     String desc = readUTF8(u + 4, c);
/*  868 */     u += 6;
/*      */ 
/*      */     
/*  871 */     String signature = null;
/*  872 */     int anns = 0;
/*  873 */     int ianns = 0;
/*  874 */     int tanns = 0;
/*  875 */     int itanns = 0;
/*  876 */     Object value = null;
/*  877 */     Attribute attributes = null;
/*      */     
/*  879 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/*  880 */       String attrName = readUTF8(u + 2, c);
/*      */ 
/*      */       
/*  883 */       if ("ConstantValue".equals(attrName)) {
/*  884 */         int item = readUnsignedShort(u + 8);
/*  885 */         value = (item == 0) ? null : readConst(item, c);
/*  886 */       } else if ("Signature".equals(attrName)) {
/*  887 */         signature = readUTF8(u + 8, c);
/*  888 */       } else if ("Deprecated".equals(attrName)) {
/*  889 */         access |= 0x20000;
/*  890 */       } else if ("Synthetic".equals(attrName)) {
/*  891 */         access |= 0x41000;
/*      */       }
/*  893 */       else if ("RuntimeVisibleAnnotations"
/*  894 */         .equals(attrName)) {
/*  895 */         anns = u + 8;
/*  896 */       } else if ("RuntimeVisibleTypeAnnotations"
/*  897 */         .equals(attrName)) {
/*  898 */         tanns = u + 8;
/*  899 */       } else if ("RuntimeInvisibleAnnotations"
/*  900 */         .equals(attrName)) {
/*  901 */         ianns = u + 8;
/*  902 */       } else if ("RuntimeInvisibleTypeAnnotations"
/*  903 */         .equals(attrName)) {
/*  904 */         itanns = u + 8;
/*      */       } else {
/*  906 */         Attribute attr = readAttribute(context.attrs, attrName, u + 8, 
/*  907 */             readInt(u + 4), c, -1, null);
/*  908 */         if (attr != null) {
/*  909 */           attr.next = attributes;
/*  910 */           attributes = attr;
/*      */         } 
/*      */       } 
/*  913 */       u += 6 + readInt(u + 4);
/*      */     } 
/*  915 */     u += 2;
/*      */ 
/*      */     
/*  918 */     FieldVisitor fv = classVisitor.visitField(access, name, desc, signature, value);
/*      */     
/*  920 */     if (fv == null) {
/*  921 */       return u;
/*      */     }
/*      */ 
/*      */     
/*  925 */     if (anns != 0) {
/*  926 */       for (int j = readUnsignedShort(anns), v = anns + 2; j > 0; j--) {
/*  927 */         v = readAnnotationValues(v + 2, c, true, fv
/*  928 */             .visitAnnotation(readUTF8(v, c), true));
/*      */       }
/*      */     }
/*  931 */     if (ianns != 0) {
/*  932 */       for (int j = readUnsignedShort(ianns), v = ianns + 2; j > 0; j--) {
/*  933 */         v = readAnnotationValues(v + 2, c, true, fv
/*  934 */             .visitAnnotation(readUTF8(v, c), false));
/*      */       }
/*      */     }
/*  937 */     if (tanns != 0) {
/*  938 */       for (int j = readUnsignedShort(tanns), v = tanns + 2; j > 0; j--) {
/*  939 */         v = readAnnotationTarget(context, v);
/*  940 */         v = readAnnotationValues(v + 2, c, true, fv
/*  941 */             .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  942 */               readUTF8(v, c), true));
/*      */       } 
/*      */     }
/*  945 */     if (itanns != 0) {
/*  946 */       for (int j = readUnsignedShort(itanns), v = itanns + 2; j > 0; j--) {
/*  947 */         v = readAnnotationTarget(context, v);
/*  948 */         v = readAnnotationValues(v + 2, c, true, fv
/*  949 */             .visitTypeAnnotation(context.typeRef, context.typePath, 
/*  950 */               readUTF8(v, c), false));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  955 */     while (attributes != null) {
/*  956 */       Attribute attr = attributes.next;
/*  957 */       attributes.next = null;
/*  958 */       fv.visitAttribute(attributes);
/*  959 */       attributes = attr;
/*      */     } 
/*      */ 
/*      */     
/*  963 */     fv.visitEnd();
/*      */     
/*  965 */     return u;
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
/*      */   
/*      */   private int readMethod(ClassVisitor classVisitor, Context context, int u) {
/*  982 */     char[] c = context.buffer;
/*  983 */     context.access = readUnsignedShort(u);
/*  984 */     context.name = readUTF8(u + 2, c);
/*  985 */     context.desc = readUTF8(u + 4, c);
/*  986 */     u += 6;
/*      */ 
/*      */     
/*  989 */     int code = 0;
/*  990 */     int exception = 0;
/*  991 */     String[] exceptions = null;
/*  992 */     String signature = null;
/*  993 */     int methodParameters = 0;
/*  994 */     int anns = 0;
/*  995 */     int ianns = 0;
/*  996 */     int tanns = 0;
/*  997 */     int itanns = 0;
/*  998 */     int dann = 0;
/*  999 */     int mpanns = 0;
/* 1000 */     int impanns = 0;
/* 1001 */     int firstAttribute = u;
/* 1002 */     Attribute attributes = null;
/*      */     
/* 1004 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/* 1005 */       String attrName = readUTF8(u + 2, c);
/*      */ 
/*      */       
/* 1008 */       if ("Code".equals(attrName)) {
/* 1009 */         if ((context.flags & 0x1) == 0) {
/* 1010 */           code = u + 8;
/*      */         }
/* 1012 */       } else if ("Exceptions".equals(attrName)) {
/* 1013 */         exceptions = new String[readUnsignedShort(u + 8)];
/* 1014 */         exception = u + 10;
/* 1015 */         for (int j = 0; j < exceptions.length; j++) {
/* 1016 */           exceptions[j] = readClass(exception, c);
/* 1017 */           exception += 2;
/*      */         } 
/* 1019 */       } else if ("Signature".equals(attrName)) {
/* 1020 */         signature = readUTF8(u + 8, c);
/* 1021 */       } else if ("Deprecated".equals(attrName)) {
/* 1022 */         context.access |= 0x20000;
/* 1023 */       } else if ("RuntimeVisibleAnnotations"
/* 1024 */         .equals(attrName)) {
/* 1025 */         anns = u + 8;
/* 1026 */       } else if ("RuntimeVisibleTypeAnnotations"
/* 1027 */         .equals(attrName)) {
/* 1028 */         tanns = u + 8;
/* 1029 */       } else if ("AnnotationDefault".equals(attrName)) {
/* 1030 */         dann = u + 8;
/* 1031 */       } else if ("Synthetic".equals(attrName)) {
/* 1032 */         context.access |= 0x41000;
/*      */       }
/* 1034 */       else if ("RuntimeInvisibleAnnotations"
/* 1035 */         .equals(attrName)) {
/* 1036 */         ianns = u + 8;
/* 1037 */       } else if ("RuntimeInvisibleTypeAnnotations"
/* 1038 */         .equals(attrName)) {
/* 1039 */         itanns = u + 8;
/* 1040 */       } else if ("RuntimeVisibleParameterAnnotations"
/* 1041 */         .equals(attrName)) {
/* 1042 */         mpanns = u + 8;
/* 1043 */       } else if ("RuntimeInvisibleParameterAnnotations"
/* 1044 */         .equals(attrName)) {
/* 1045 */         impanns = u + 8;
/* 1046 */       } else if ("MethodParameters".equals(attrName)) {
/* 1047 */         methodParameters = u + 8;
/*      */       } else {
/* 1049 */         Attribute attr = readAttribute(context.attrs, attrName, u + 8, 
/* 1050 */             readInt(u + 4), c, -1, null);
/* 1051 */         if (attr != null) {
/* 1052 */           attr.next = attributes;
/* 1053 */           attributes = attr;
/*      */         } 
/*      */       } 
/* 1056 */       u += 6 + readInt(u + 4);
/*      */     } 
/* 1058 */     u += 2;
/*      */ 
/*      */     
/* 1061 */     MethodVisitor mv = classVisitor.visitMethod(context.access, context.name, context.desc, signature, exceptions);
/*      */     
/* 1063 */     if (mv == null) {
/* 1064 */       return u;
/*      */     }
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
/* 1077 */     if (mv instanceof MethodWriter) {
/* 1078 */       MethodWriter mw = (MethodWriter)mv;
/* 1079 */       if (mw.cw.cr == this && ((signature != null) ? signature
/* 1080 */         .equals(mw.signature) : (mw.signature == null))) {
/* 1081 */         boolean sameExceptions = false;
/* 1082 */         if (exceptions == null) {
/* 1083 */           sameExceptions = (mw.exceptionCount == 0);
/* 1084 */         } else if (exceptions.length == mw.exceptionCount) {
/* 1085 */           sameExceptions = true;
/* 1086 */           for (int j = exceptions.length - 1; j >= 0; j--) {
/* 1087 */             exception -= 2;
/* 1088 */             if (mw.exceptions[j] != readUnsignedShort(exception)) {
/* 1089 */               sameExceptions = false;
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/* 1094 */         if (sameExceptions) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1100 */           mw.classReaderOffset = firstAttribute;
/* 1101 */           mw.classReaderLength = u - firstAttribute;
/* 1102 */           return u;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1108 */     if (methodParameters != 0) {
/* 1109 */       int v; for (int j = this.b[methodParameters] & 0xFF; j > 0; j--, v += 4) {
/* 1110 */         mv.visitParameter(readUTF8(v, c), readUnsignedShort(v + 2));
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1115 */     if (dann != 0) {
/* 1116 */       AnnotationVisitor dv = mv.visitAnnotationDefault();
/* 1117 */       readAnnotationValue(dann, c, null, dv);
/* 1118 */       if (dv != null) {
/* 1119 */         dv.visitEnd();
/*      */       }
/*      */     } 
/* 1122 */     if (anns != 0) {
/* 1123 */       for (int j = readUnsignedShort(anns), v = anns + 2; j > 0; j--) {
/* 1124 */         v = readAnnotationValues(v + 2, c, true, mv
/* 1125 */             .visitAnnotation(readUTF8(v, c), true));
/*      */       }
/*      */     }
/* 1128 */     if (ianns != 0) {
/* 1129 */       for (int j = readUnsignedShort(ianns), v = ianns + 2; j > 0; j--) {
/* 1130 */         v = readAnnotationValues(v + 2, c, true, mv
/* 1131 */             .visitAnnotation(readUTF8(v, c), false));
/*      */       }
/*      */     }
/* 1134 */     if (tanns != 0) {
/* 1135 */       for (int j = readUnsignedShort(tanns), v = tanns + 2; j > 0; j--) {
/* 1136 */         v = readAnnotationTarget(context, v);
/* 1137 */         v = readAnnotationValues(v + 2, c, true, mv
/* 1138 */             .visitTypeAnnotation(context.typeRef, context.typePath, 
/* 1139 */               readUTF8(v, c), true));
/*      */       } 
/*      */     }
/* 1142 */     if (itanns != 0) {
/* 1143 */       for (int j = readUnsignedShort(itanns), v = itanns + 2; j > 0; j--) {
/* 1144 */         v = readAnnotationTarget(context, v);
/* 1145 */         v = readAnnotationValues(v + 2, c, true, mv
/* 1146 */             .visitTypeAnnotation(context.typeRef, context.typePath, 
/* 1147 */               readUTF8(v, c), false));
/*      */       } 
/*      */     }
/* 1150 */     if (mpanns != 0) {
/* 1151 */       readParameterAnnotations(mv, context, mpanns, true);
/*      */     }
/* 1153 */     if (impanns != 0) {
/* 1154 */       readParameterAnnotations(mv, context, impanns, false);
/*      */     }
/*      */ 
/*      */     
/* 1158 */     while (attributes != null) {
/* 1159 */       Attribute attr = attributes.next;
/* 1160 */       attributes.next = null;
/* 1161 */       mv.visitAttribute(attributes);
/* 1162 */       attributes = attr;
/*      */     } 
/*      */ 
/*      */     
/* 1166 */     if (code != 0) {
/* 1167 */       mv.visitCode();
/* 1168 */       readCode(mv, context, code);
/*      */     } 
/*      */ 
/*      */     
/* 1172 */     mv.visitEnd();
/*      */     
/* 1174 */     return u;
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
/*      */   private void readCode(MethodVisitor mv, Context context, int u) {
/* 1189 */     byte[] b = this.b;
/* 1190 */     char[] c = context.buffer;
/* 1191 */     int maxStack = readUnsignedShort(u);
/* 1192 */     int maxLocals = readUnsignedShort(u + 2);
/* 1193 */     int codeLength = readInt(u + 4);
/* 1194 */     u += 8;
/*      */ 
/*      */     
/* 1197 */     int codeStart = u;
/* 1198 */     int codeEnd = u + codeLength;
/* 1199 */     Label[] labels = context.labels = new Label[codeLength + 2];
/* 1200 */     readLabel(codeLength + 1, labels);
/* 1201 */     while (u < codeEnd) {
/* 1202 */       int k, offset = u - codeStart;
/* 1203 */       int opcode = b[u] & 0xFF;
/* 1204 */       switch (ClassWriter.TYPE[opcode]) {
/*      */         case 0:
/*      */         case 4:
/* 1207 */           u++;
/*      */           continue;
/*      */         case 9:
/* 1210 */           readLabel(offset + readShort(u + 1), labels);
/* 1211 */           u += 3;
/*      */           continue;
/*      */         case 18:
/* 1214 */           readLabel(offset + readUnsignedShort(u + 1), labels);
/* 1215 */           u += 3;
/*      */           continue;
/*      */         case 10:
/*      */         case 19:
/* 1219 */           readLabel(offset + readInt(u + 1), labels);
/* 1220 */           u += 5;
/*      */           continue;
/*      */         case 17:
/* 1223 */           opcode = b[u + 1] & 0xFF;
/* 1224 */           if (opcode == 132) {
/* 1225 */             u += 6; continue;
/*      */           } 
/* 1227 */           u += 4;
/*      */           continue;
/*      */ 
/*      */         
/*      */         case 14:
/* 1232 */           u = u + 4 - (offset & 0x3);
/*      */           
/* 1234 */           readLabel(offset + readInt(u), labels);
/* 1235 */           for (k = readInt(u + 8) - readInt(u + 4) + 1; k > 0; k--) {
/* 1236 */             readLabel(offset + readInt(u + 12), labels);
/* 1237 */             u += 4;
/*      */           } 
/* 1239 */           u += 12;
/*      */           continue;
/*      */         
/*      */         case 15:
/* 1243 */           u = u + 4 - (offset & 0x3);
/*      */           
/* 1245 */           readLabel(offset + readInt(u), labels);
/* 1246 */           for (k = readInt(u + 4); k > 0; k--) {
/* 1247 */             readLabel(offset + readInt(u + 12), labels);
/* 1248 */             u += 8;
/*      */           } 
/* 1250 */           u += 8;
/*      */           continue;
/*      */         case 1:
/*      */         case 3:
/*      */         case 11:
/* 1255 */           u += 2;
/*      */           continue;
/*      */         case 2:
/*      */         case 5:
/*      */         case 6:
/*      */         case 12:
/*      */         case 13:
/* 1262 */           u += 3;
/*      */           continue;
/*      */         case 7:
/*      */         case 8:
/* 1266 */           u += 5;
/*      */           continue;
/*      */       } 
/*      */       
/* 1270 */       u += 4;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1276 */     for (int i = readUnsignedShort(u); i > 0; i--) {
/* 1277 */       Label start = readLabel(readUnsignedShort(u + 2), labels);
/* 1278 */       Label end = readLabel(readUnsignedShort(u + 4), labels);
/* 1279 */       Label handler = readLabel(readUnsignedShort(u + 6), labels);
/* 1280 */       String type = readUTF8(this.items[readUnsignedShort(u + 8)], c);
/* 1281 */       mv.visitTryCatchBlock(start, end, handler, type);
/* 1282 */       u += 8;
/*      */     } 
/* 1284 */     u += 2;
/*      */ 
/*      */     
/* 1287 */     int[] tanns = null;
/* 1288 */     int[] itanns = null;
/* 1289 */     int tann = 0;
/* 1290 */     int itann = 0;
/* 1291 */     int ntoff = -1;
/* 1292 */     int nitoff = -1;
/* 1293 */     int varTable = 0;
/* 1294 */     int varTypeTable = 0;
/* 1295 */     boolean zip = true;
/* 1296 */     boolean unzip = ((context.flags & 0x8) != 0);
/* 1297 */     int stackMap = 0;
/* 1298 */     int stackMapSize = 0;
/* 1299 */     int frameCount = 0;
/* 1300 */     Context frame = null;
/* 1301 */     Attribute attributes = null;
/*      */     int j;
/* 1303 */     for (j = readUnsignedShort(u); j > 0; j--) {
/* 1304 */       String attrName = readUTF8(u + 2, c);
/* 1305 */       if ("LocalVariableTable".equals(attrName)) {
/* 1306 */         if ((context.flags & 0x2) == 0) {
/* 1307 */           varTable = u + 8;
/* 1308 */           for (int k = readUnsignedShort(u + 8), v = u; k > 0; k--) {
/* 1309 */             int label = readUnsignedShort(v + 10);
/* 1310 */             if (labels[label] == null) {
/* 1311 */               (readLabel(label, labels)).status |= 0x1;
/*      */             }
/* 1313 */             label += readUnsignedShort(v + 12);
/* 1314 */             if (labels[label] == null) {
/* 1315 */               (readLabel(label, labels)).status |= 0x1;
/*      */             }
/* 1317 */             v += 10;
/*      */           } 
/*      */         } 
/* 1320 */       } else if ("LocalVariableTypeTable".equals(attrName)) {
/* 1321 */         varTypeTable = u + 8;
/* 1322 */       } else if ("LineNumberTable".equals(attrName)) {
/* 1323 */         if ((context.flags & 0x2) == 0) {
/* 1324 */           for (int k = readUnsignedShort(u + 8), v = u; k > 0; k--) {
/* 1325 */             int label = readUnsignedShort(v + 10);
/* 1326 */             if (labels[label] == null) {
/* 1327 */               (readLabel(label, labels)).status |= 0x1;
/*      */             }
/* 1329 */             Label l = labels[label];
/* 1330 */             while (l.line > 0) {
/* 1331 */               if (l.next == null) {
/* 1332 */                 l.next = new Label();
/*      */               }
/* 1334 */               l = l.next;
/*      */             } 
/* 1336 */             l.line = readUnsignedShort(v + 12);
/* 1337 */             v += 4;
/*      */           } 
/*      */         }
/* 1340 */       } else if ("RuntimeVisibleTypeAnnotations"
/* 1341 */         .equals(attrName)) {
/* 1342 */         tanns = readTypeAnnotations(mv, context, u + 8, true);
/*      */         
/* 1344 */         ntoff = (tanns.length == 0 || readByte(tanns[0]) < 67) ? -1 : readUnsignedShort(tanns[0] + 1);
/* 1345 */       } else if ("RuntimeInvisibleTypeAnnotations"
/* 1346 */         .equals(attrName)) {
/* 1347 */         itanns = readTypeAnnotations(mv, context, u + 8, false);
/*      */         
/* 1349 */         nitoff = (itanns.length == 0 || readByte(itanns[0]) < 67) ? -1 : readUnsignedShort(itanns[0] + 1);
/* 1350 */       } else if ("StackMapTable".equals(attrName)) {
/* 1351 */         if ((context.flags & 0x4) == 0) {
/* 1352 */           stackMap = u + 10;
/* 1353 */           stackMapSize = readInt(u + 4);
/* 1354 */           frameCount = readUnsignedShort(u + 8);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1374 */       else if ("StackMap".equals(attrName)) {
/* 1375 */         if ((context.flags & 0x4) == 0) {
/* 1376 */           zip = false;
/* 1377 */           stackMap = u + 10;
/* 1378 */           stackMapSize = readInt(u + 4);
/* 1379 */           frameCount = readUnsignedShort(u + 8);
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1387 */         for (int k = 0; k < context.attrs.length; k++) {
/* 1388 */           if ((context.attrs[k]).type.equals(attrName)) {
/* 1389 */             Attribute attr = context.attrs[k].read(this, u + 8, 
/* 1390 */                 readInt(u + 4), c, codeStart - 8, labels);
/* 1391 */             if (attr != null) {
/* 1392 */               attr.next = attributes;
/* 1393 */               attributes = attr;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1398 */       u += 6 + readInt(u + 4);
/*      */     } 
/* 1400 */     u += 2;
/*      */ 
/*      */     
/* 1403 */     if (stackMap != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1409 */       frame = context;
/* 1410 */       frame.offset = -1;
/* 1411 */       frame.mode = 0;
/* 1412 */       frame.localCount = 0;
/* 1413 */       frame.localDiff = 0;
/* 1414 */       frame.stackCount = 0;
/* 1415 */       frame.local = new Object[maxLocals];
/* 1416 */       frame.stack = new Object[maxStack];
/* 1417 */       if (unzip) {
/* 1418 */         getImplicitFrame(context);
/*      */       }
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
/* 1431 */       for (j = stackMap; j < stackMap + stackMapSize - 2; j++) {
/* 1432 */         if (b[j] == 8) {
/* 1433 */           int v = readUnsignedShort(j + 1);
/* 1434 */           if (v >= 0 && v < codeLength && (
/* 1435 */             b[codeStart + v] & 0xFF) == 187) {
/* 1436 */             readLabel(v, labels);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1442 */     if ((context.flags & 0x100) != 0 && (context.flags & 0x8) != 0)
/*      */     {
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
/* 1455 */       mv.visitFrame(-1, maxLocals, null, 0, null);
/*      */     }
/*      */ 
/*      */     
/* 1459 */     int opcodeDelta = ((context.flags & 0x100) == 0) ? -33 : 0;
/* 1460 */     boolean insertFrame = false;
/* 1461 */     u = codeStart;
/* 1462 */     while (u < codeEnd) {
/* 1463 */       Label target; int label, cpIndex, min, len; boolean itf; int bsmIndex, max, keys[]; String iowner; Handle bsm; Label[] table, values; String iname; int bsmArgCount, k; String idesc; Object[] bsmArgs; int m; String str1, str2; int offset = u - codeStart;
/*      */ 
/*      */       
/* 1466 */       Label l = labels[offset];
/* 1467 */       if (l != null) {
/* 1468 */         Label next = l.next;
/* 1469 */         l.next = null;
/* 1470 */         mv.visitLabel(l);
/* 1471 */         if ((context.flags & 0x2) == 0 && l.line > 0) {
/* 1472 */           mv.visitLineNumber(l.line, l);
/* 1473 */           while (next != null) {
/* 1474 */             mv.visitLineNumber(next.line, l);
/* 1475 */             next = next.next;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1481 */       while (frame != null && (frame.offset == offset || frame.offset == -1)) {
/*      */ 
/*      */ 
/*      */         
/* 1485 */         if (frame.offset != -1) {
/* 1486 */           if (!zip || unzip) {
/* 1487 */             mv.visitFrame(-1, frame.localCount, frame.local, frame.stackCount, frame.stack);
/*      */           } else {
/*      */             
/* 1490 */             mv.visitFrame(frame.mode, frame.localDiff, frame.local, frame.stackCount, frame.stack);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1495 */           insertFrame = false;
/*      */         } 
/* 1497 */         if (frameCount > 0) {
/* 1498 */           stackMap = readFrame(stackMap, zip, unzip, frame);
/* 1499 */           frameCount--; continue;
/*      */         } 
/* 1501 */         frame = null;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1507 */       if (insertFrame) {
/* 1508 */         mv.visitFrame(256, 0, null, 0, null);
/* 1509 */         insertFrame = false;
/*      */       } 
/*      */ 
/*      */       
/* 1513 */       int opcode = b[u] & 0xFF;
/* 1514 */       switch (ClassWriter.TYPE[opcode]) {
/*      */         case 0:
/* 1516 */           mv.visitInsn(opcode);
/* 1517 */           u++;
/*      */           break;
/*      */         case 4:
/* 1520 */           if (opcode > 54) {
/* 1521 */             opcode -= 59;
/* 1522 */             mv.visitVarInsn(54 + (opcode >> 2), opcode & 0x3);
/*      */           } else {
/*      */             
/* 1525 */             opcode -= 26;
/* 1526 */             mv.visitVarInsn(21 + (opcode >> 2), opcode & 0x3);
/*      */           } 
/* 1528 */           u++;
/*      */           break;
/*      */         case 9:
/* 1531 */           mv.visitJumpInsn(opcode, labels[offset + readShort(u + 1)]);
/* 1532 */           u += 3;
/*      */           break;
/*      */         case 10:
/* 1535 */           mv.visitJumpInsn(opcode + opcodeDelta, labels[offset + 
/* 1536 */                 readInt(u + 1)]);
/* 1537 */           u += 5;
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 18:
/* 1543 */           opcode = (opcode < 218) ? (opcode - 49) : (opcode - 20);
/* 1544 */           target = labels[offset + readUnsignedShort(u + 1)];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1550 */           if (opcode == 167 || opcode == 168) {
/* 1551 */             mv.visitJumpInsn(opcode + 33, target);
/*      */           } else {
/* 1553 */             opcode = (opcode <= 166) ? ((opcode + 1 ^ 0x1) - 1) : (opcode ^ 0x1);
/*      */             
/* 1555 */             Label endif = readLabel(offset + 3, labels);
/* 1556 */             mv.visitJumpInsn(opcode, endif);
/* 1557 */             mv.visitJumpInsn(200, target);
/*      */ 
/*      */ 
/*      */             
/* 1561 */             insertFrame = true;
/*      */           } 
/* 1563 */           u += 3;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 19:
/* 1568 */           mv.visitJumpInsn(200, labels[offset + readInt(u + 1)]);
/*      */ 
/*      */ 
/*      */           
/* 1572 */           insertFrame = true;
/* 1573 */           u += 5;
/*      */           break;
/*      */         
/*      */         case 17:
/* 1577 */           opcode = b[u + 1] & 0xFF;
/* 1578 */           if (opcode == 132) {
/* 1579 */             mv.visitIincInsn(readUnsignedShort(u + 2), readShort(u + 4));
/* 1580 */             u += 6; break;
/*      */           } 
/* 1582 */           mv.visitVarInsn(opcode, readUnsignedShort(u + 2));
/* 1583 */           u += 4;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 14:
/* 1588 */           u = u + 4 - (offset & 0x3);
/*      */           
/* 1590 */           label = offset + readInt(u);
/* 1591 */           min = readInt(u + 4);
/* 1592 */           max = readInt(u + 8);
/* 1593 */           table = new Label[max - min + 1];
/* 1594 */           u += 12;
/* 1595 */           for (k = 0; k < table.length; k++) {
/* 1596 */             table[k] = labels[offset + readInt(u)];
/* 1597 */             u += 4;
/*      */           } 
/* 1599 */           mv.visitTableSwitchInsn(min, max, labels[label], table);
/*      */           break;
/*      */ 
/*      */         
/*      */         case 15:
/* 1604 */           u = u + 4 - (offset & 0x3);
/*      */           
/* 1606 */           label = offset + readInt(u);
/* 1607 */           len = readInt(u + 4);
/* 1608 */           keys = new int[len];
/* 1609 */           values = new Label[len];
/* 1610 */           u += 8;
/* 1611 */           for (k = 0; k < len; k++) {
/* 1612 */             keys[k] = readInt(u);
/* 1613 */             values[k] = labels[offset + readInt(u + 4)];
/* 1614 */             u += 8;
/*      */           } 
/* 1616 */           mv.visitLookupSwitchInsn(labels[label], keys, values);
/*      */           break;
/*      */         
/*      */         case 3:
/* 1620 */           mv.visitVarInsn(opcode, b[u + 1] & 0xFF);
/* 1621 */           u += 2;
/*      */           break;
/*      */         case 1:
/* 1624 */           mv.visitIntInsn(opcode, b[u + 1]);
/* 1625 */           u += 2;
/*      */           break;
/*      */         case 2:
/* 1628 */           mv.visitIntInsn(opcode, readShort(u + 1));
/* 1629 */           u += 3;
/*      */           break;
/*      */         case 11:
/* 1632 */           mv.visitLdcInsn(readConst(b[u + 1] & 0xFF, c));
/* 1633 */           u += 2;
/*      */           break;
/*      */         case 12:
/* 1636 */           mv.visitLdcInsn(readConst(readUnsignedShort(u + 1), c));
/* 1637 */           u += 3;
/*      */           break;
/*      */         case 6:
/*      */         case 7:
/* 1641 */           cpIndex = this.items[readUnsignedShort(u + 1)];
/* 1642 */           itf = (b[cpIndex - 1] == 11);
/* 1643 */           iowner = readClass(cpIndex, c);
/* 1644 */           cpIndex = this.items[readUnsignedShort(cpIndex + 2)];
/* 1645 */           iname = readUTF8(cpIndex, c);
/* 1646 */           idesc = readUTF8(cpIndex + 2, c);
/* 1647 */           if (opcode < 182) {
/* 1648 */             mv.visitFieldInsn(opcode, iowner, iname, idesc);
/*      */           } else {
/* 1650 */             mv.visitMethodInsn(opcode, iowner, iname, idesc, itf);
/*      */           } 
/* 1652 */           if (opcode == 185) {
/* 1653 */             u += 5; break;
/*      */           } 
/* 1655 */           u += 3;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 8:
/* 1660 */           cpIndex = this.items[readUnsignedShort(u + 1)];
/* 1661 */           bsmIndex = context.bootstrapMethods[readUnsignedShort(cpIndex)];
/* 1662 */           bsm = (Handle)readConst(readUnsignedShort(bsmIndex), c);
/* 1663 */           bsmArgCount = readUnsignedShort(bsmIndex + 2);
/* 1664 */           bsmArgs = new Object[bsmArgCount];
/* 1665 */           bsmIndex += 4;
/* 1666 */           for (m = 0; m < bsmArgCount; m++) {
/* 1667 */             bsmArgs[m] = readConst(readUnsignedShort(bsmIndex), c);
/* 1668 */             bsmIndex += 2;
/*      */           } 
/* 1670 */           cpIndex = this.items[readUnsignedShort(cpIndex + 2)];
/* 1671 */           str1 = readUTF8(cpIndex, c);
/* 1672 */           str2 = readUTF8(cpIndex + 2, c);
/* 1673 */           mv.visitInvokeDynamicInsn(str1, str2, bsm, bsmArgs);
/* 1674 */           u += 5;
/*      */           break;
/*      */         
/*      */         case 5:
/* 1678 */           mv.visitTypeInsn(opcode, readClass(u + 1, c));
/* 1679 */           u += 3;
/*      */           break;
/*      */         case 13:
/* 1682 */           mv.visitIincInsn(b[u + 1] & 0xFF, b[u + 2]);
/* 1683 */           u += 3;
/*      */           break;
/*      */         
/*      */         default:
/* 1687 */           mv.visitMultiANewArrayInsn(readClass(u + 1, c), b[u + 3] & 0xFF);
/* 1688 */           u += 4;
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 1693 */       while (tanns != null && tann < tanns.length && ntoff <= offset) {
/* 1694 */         if (ntoff == offset) {
/* 1695 */           int v = readAnnotationTarget(context, tanns[tann]);
/* 1696 */           readAnnotationValues(v + 2, c, true, mv
/* 1697 */               .visitInsnAnnotation(context.typeRef, context.typePath, 
/* 1698 */                 readUTF8(v, c), true));
/*      */         } 
/*      */         
/* 1701 */         ntoff = (++tann >= tanns.length || readByte(tanns[tann]) < 67) ? -1 : readUnsignedShort(tanns[tann] + 1);
/*      */       } 
/* 1703 */       while (itanns != null && itann < itanns.length && nitoff <= offset) {
/* 1704 */         if (nitoff == offset) {
/* 1705 */           int v = readAnnotationTarget(context, itanns[itann]);
/* 1706 */           readAnnotationValues(v + 2, c, true, mv
/* 1707 */               .visitInsnAnnotation(context.typeRef, context.typePath, 
/* 1708 */                 readUTF8(v, c), false));
/*      */         } 
/*      */ 
/*      */         
/* 1712 */         nitoff = (++itann >= itanns.length || readByte(itanns[itann]) < 67) ? -1 : readUnsignedShort(itanns[itann] + 1);
/*      */       } 
/*      */     } 
/* 1715 */     if (labels[codeLength] != null) {
/* 1716 */       mv.visitLabel(labels[codeLength]);
/*      */     }
/*      */ 
/*      */     
/* 1720 */     if ((context.flags & 0x2) == 0 && varTable != 0) {
/* 1721 */       int[] typeTable = null;
/* 1722 */       if (varTypeTable != 0) {
/* 1723 */         u = varTypeTable + 2;
/* 1724 */         typeTable = new int[readUnsignedShort(varTypeTable) * 3];
/* 1725 */         for (int m = typeTable.length; m > 0; ) {
/* 1726 */           typeTable[--m] = u + 6;
/* 1727 */           typeTable[--m] = readUnsignedShort(u + 8);
/* 1728 */           typeTable[--m] = readUnsignedShort(u);
/* 1729 */           u += 10;
/*      */         } 
/*      */       } 
/* 1732 */       u = varTable + 2;
/* 1733 */       for (int k = readUnsignedShort(varTable); k > 0; k--) {
/* 1734 */         int start = readUnsignedShort(u);
/* 1735 */         int length = readUnsignedShort(u + 2);
/* 1736 */         int index = readUnsignedShort(u + 8);
/* 1737 */         String vsignature = null;
/* 1738 */         if (typeTable != null) {
/* 1739 */           for (int m = 0; m < typeTable.length; m += 3) {
/* 1740 */             if (typeTable[m] == start && typeTable[m + 1] == index) {
/* 1741 */               vsignature = readUTF8(typeTable[m + 2], c);
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/* 1746 */         mv.visitLocalVariable(readUTF8(u + 4, c), readUTF8(u + 6, c), vsignature, labels[start], labels[start + length], index);
/*      */ 
/*      */         
/* 1749 */         u += 10;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1754 */     if (tanns != null) {
/* 1755 */       for (int k = 0; k < tanns.length; k++) {
/* 1756 */         if (readByte(tanns[k]) >> 1 == 32) {
/* 1757 */           int v = readAnnotationTarget(context, tanns[k]);
/* 1758 */           v = readAnnotationValues(v + 2, c, true, mv
/* 1759 */               .visitLocalVariableAnnotation(context.typeRef, context.typePath, context.start, context.end, context.index, 
/*      */                 
/* 1761 */                 readUTF8(v, c), true));
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 1766 */     if (itanns != null) {
/* 1767 */       for (int k = 0; k < itanns.length; k++) {
/* 1768 */         if (readByte(itanns[k]) >> 1 == 32) {
/* 1769 */           int v = readAnnotationTarget(context, itanns[k]);
/* 1770 */           v = readAnnotationValues(v + 2, c, true, mv
/* 1771 */               .visitLocalVariableAnnotation(context.typeRef, context.typePath, context.start, context.end, context.index, 
/*      */                 
/* 1773 */                 readUTF8(v, c), false));
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1780 */     while (attributes != null) {
/* 1781 */       Attribute attr = attributes.next;
/* 1782 */       attributes.next = null;
/* 1783 */       mv.visitAttribute(attributes);
/* 1784 */       attributes = attr;
/*      */     } 
/*      */ 
/*      */     
/* 1788 */     mv.visitMaxs(maxStack, maxLocals);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] readTypeAnnotations(MethodVisitor mv, Context context, int u, boolean visible) {
/* 1809 */     char[] c = context.buffer;
/* 1810 */     int[] offsets = new int[readUnsignedShort(u)];
/* 1811 */     u += 2;
/* 1812 */     for (int i = 0; i < offsets.length; i++) {
/* 1813 */       int j; offsets[i] = u;
/* 1814 */       int target = readInt(u);
/* 1815 */       switch (target >>> 24) {
/*      */         case 0:
/*      */         case 1:
/*      */         case 22:
/* 1819 */           u += 2;
/*      */           break;
/*      */         case 19:
/*      */         case 20:
/*      */         case 21:
/* 1824 */           u++;
/*      */           break;
/*      */         case 64:
/*      */         case 65:
/* 1828 */           for (j = readUnsignedShort(u + 1); j > 0; j--) {
/* 1829 */             int start = readUnsignedShort(u + 3);
/* 1830 */             int length = readUnsignedShort(u + 5);
/* 1831 */             readLabel(start, context.labels);
/* 1832 */             readLabel(start + length, context.labels);
/* 1833 */             u += 6;
/*      */           } 
/* 1835 */           u += 3;
/*      */           break;
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/* 1842 */           u += 4;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 1854 */           u += 3;
/*      */           break;
/*      */       } 
/* 1857 */       int pathLength = readByte(u);
/* 1858 */       if (target >>> 24 == 66) {
/* 1859 */         TypePath path = (pathLength == 0) ? null : new TypePath(this.b, u);
/* 1860 */         u += 1 + 2 * pathLength;
/* 1861 */         u = readAnnotationValues(u + 2, c, true, mv
/* 1862 */             .visitTryCatchAnnotation(target, path, 
/* 1863 */               readUTF8(u, c), visible));
/*      */       } else {
/* 1865 */         u = readAnnotationValues(u + 3 + 2 * pathLength, c, true, null);
/*      */       } 
/*      */     } 
/* 1868 */     return offsets;
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
/*      */ 
/*      */   
/*      */   private int readAnnotationTarget(Context context, int u) {
/* 1886 */     int n, i, target = readInt(u);
/* 1887 */     switch (target >>> 24) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 22:
/* 1891 */         target &= 0xFFFF0000;
/* 1892 */         u += 2;
/*      */         break;
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/* 1897 */         target &= 0xFF000000;
/* 1898 */         u++;
/*      */         break;
/*      */       case 64:
/*      */       case 65:
/* 1902 */         target &= 0xFF000000;
/* 1903 */         n = readUnsignedShort(u + 1);
/* 1904 */         context.start = new Label[n];
/* 1905 */         context.end = new Label[n];
/* 1906 */         context.index = new int[n];
/* 1907 */         u += 3;
/* 1908 */         for (i = 0; i < n; i++) {
/* 1909 */           int start = readUnsignedShort(u);
/* 1910 */           int length = readUnsignedShort(u + 2);
/* 1911 */           context.start[i] = readLabel(start, context.labels);
/* 1912 */           context.end[i] = readLabel(start + length, context.labels);
/* 1913 */           context.index[i] = readUnsignedShort(u + 4);
/* 1914 */           u += 6;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/* 1923 */         target &= 0xFF0000FF;
/* 1924 */         u += 4;
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       default:
/* 1936 */         target &= (target >>> 24 < 67) ? -256 : -16777216;
/* 1937 */         u += 3;
/*      */         break;
/*      */     } 
/* 1940 */     int pathLength = readByte(u);
/* 1941 */     context.typeRef = target;
/* 1942 */     context.typePath = (pathLength == 0) ? null : new TypePath(this.b, u);
/* 1943 */     return u + 1 + 2 * pathLength;
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
/*      */ 
/*      */ 
/*      */   
/*      */   private void readParameterAnnotations(MethodVisitor mv, Context context, int v, boolean visible) {
/* 1962 */     int n = this.b[v++] & 0xFF;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1969 */     int synthetics = (Type.getArgumentTypes(context.desc)).length - n;
/*      */     int i;
/* 1971 */     for (i = 0; i < synthetics; i++) {
/*      */       
/* 1973 */       AnnotationVisitor av = mv.visitParameterAnnotation(i, "Ljava/lang/Synthetic;", false);
/* 1974 */       if (av != null) {
/* 1975 */         av.visitEnd();
/*      */       }
/*      */     } 
/* 1978 */     char[] c = context.buffer;
/* 1979 */     for (; i < n + synthetics; i++) {
/* 1980 */       int j = readUnsignedShort(v);
/* 1981 */       v += 2;
/* 1982 */       for (; j > 0; j--) {
/* 1983 */         AnnotationVisitor av = mv.visitParameterAnnotation(i, readUTF8(v, c), visible);
/* 1984 */         v = readAnnotationValues(v + 2, c, true, av);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readAnnotationValues(int v, char[] buf, boolean named, AnnotationVisitor av) {
/* 2008 */     int i = readUnsignedShort(v);
/* 2009 */     v += 2;
/* 2010 */     if (named) {
/* 2011 */       for (; i > 0; i--) {
/* 2012 */         v = readAnnotationValue(v + 2, buf, readUTF8(v, buf), av);
/*      */       }
/*      */     } else {
/* 2015 */       for (; i > 0; i--) {
/* 2016 */         v = readAnnotationValue(v, buf, null, av);
/*      */       }
/*      */     } 
/* 2019 */     if (av != null) {
/* 2020 */       av.visitEnd();
/*      */     }
/* 2022 */     return v;
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
/*      */   private int readAnnotationValue(int v, char[] buf, String name, AnnotationVisitor av) {
/*      */     int i;
/*      */     int size;
/*      */     byte[] bv;
/*      */     boolean[] zv;
/*      */     short[] sv;
/*      */     char[] cv;
/*      */     int[] iv;
/*      */     long[] lv;
/*      */     float[] fv;
/*      */     double[] dv;
/* 2044 */     if (av == null) {
/* 2045 */       switch (this.b[v] & 0xFF) {
/*      */         case 101:
/* 2047 */           return v + 5;
/*      */         case 64:
/* 2049 */           return readAnnotationValues(v + 3, buf, true, null);
/*      */         case 91:
/* 2051 */           return readAnnotationValues(v + 1, buf, false, null);
/*      */       } 
/* 2053 */       return v + 3;
/*      */     } 
/*      */     
/* 2056 */     switch (this.b[v++] & 0xFF) {
/*      */       case 68:
/*      */       case 70:
/*      */       case 73:
/*      */       case 74:
/* 2061 */         av.visit(name, readConst(readUnsignedShort(v), buf));
/* 2062 */         v += 2;
/*      */         break;
/*      */       case 66:
/* 2065 */         av.visit(name, Byte.valueOf((byte)readInt(this.items[readUnsignedShort(v)])));
/* 2066 */         v += 2;
/*      */         break;
/*      */       case 90:
/* 2069 */         av.visit(name, 
/* 2070 */             (readInt(this.items[readUnsignedShort(v)]) == 0) ? Boolean.FALSE : Boolean.TRUE);
/*      */         
/* 2072 */         v += 2;
/*      */         break;
/*      */       case 83:
/* 2075 */         av.visit(name, Short.valueOf((short)readInt(this.items[readUnsignedShort(v)])));
/* 2076 */         v += 2;
/*      */         break;
/*      */       case 67:
/* 2079 */         av.visit(name, Character.valueOf((char)readInt(this.items[readUnsignedShort(v)])));
/* 2080 */         v += 2;
/*      */         break;
/*      */       case 115:
/* 2083 */         av.visit(name, readUTF8(v, buf));
/* 2084 */         v += 2;
/*      */         break;
/*      */       case 101:
/* 2087 */         av.visitEnum(name, readUTF8(v, buf), readUTF8(v + 2, buf));
/* 2088 */         v += 4;
/*      */         break;
/*      */       case 99:
/* 2091 */         av.visit(name, Type.getType(readUTF8(v, buf)));
/* 2092 */         v += 2;
/*      */         break;
/*      */       case 64:
/* 2095 */         v = readAnnotationValues(v + 2, buf, true, av
/* 2096 */             .visitAnnotation(name, readUTF8(v, buf)));
/*      */         break;
/*      */       case 91:
/* 2099 */         size = readUnsignedShort(v);
/* 2100 */         v += 2;
/* 2101 */         if (size == 0) {
/* 2102 */           return readAnnotationValues(v - 2, buf, false, av
/* 2103 */               .visitArray(name));
/*      */         }
/* 2105 */         switch (this.b[v++] & 0xFF) {
/*      */           case 66:
/* 2107 */             bv = new byte[size];
/* 2108 */             for (i = 0; i < size; i++) {
/* 2109 */               bv[i] = (byte)readInt(this.items[readUnsignedShort(v)]);
/* 2110 */               v += 3;
/*      */             } 
/* 2112 */             av.visit(name, bv);
/* 2113 */             v--;
/*      */             break;
/*      */           case 90:
/* 2116 */             zv = new boolean[size];
/* 2117 */             for (i = 0; i < size; i++) {
/* 2118 */               zv[i] = (readInt(this.items[readUnsignedShort(v)]) != 0);
/* 2119 */               v += 3;
/*      */             } 
/* 2121 */             av.visit(name, zv);
/* 2122 */             v--;
/*      */             break;
/*      */           case 83:
/* 2125 */             sv = new short[size];
/* 2126 */             for (i = 0; i < size; i++) {
/* 2127 */               sv[i] = (short)readInt(this.items[readUnsignedShort(v)]);
/* 2128 */               v += 3;
/*      */             } 
/* 2130 */             av.visit(name, sv);
/* 2131 */             v--;
/*      */             break;
/*      */           case 67:
/* 2134 */             cv = new char[size];
/* 2135 */             for (i = 0; i < size; i++) {
/* 2136 */               cv[i] = (char)readInt(this.items[readUnsignedShort(v)]);
/* 2137 */               v += 3;
/*      */             } 
/* 2139 */             av.visit(name, cv);
/* 2140 */             v--;
/*      */             break;
/*      */           case 73:
/* 2143 */             iv = new int[size];
/* 2144 */             for (i = 0; i < size; i++) {
/* 2145 */               iv[i] = readInt(this.items[readUnsignedShort(v)]);
/* 2146 */               v += 3;
/*      */             } 
/* 2148 */             av.visit(name, iv);
/* 2149 */             v--;
/*      */             break;
/*      */           case 74:
/* 2152 */             lv = new long[size];
/* 2153 */             for (i = 0; i < size; i++) {
/* 2154 */               lv[i] = readLong(this.items[readUnsignedShort(v)]);
/* 2155 */               v += 3;
/*      */             } 
/* 2157 */             av.visit(name, lv);
/* 2158 */             v--;
/*      */             break;
/*      */           case 70:
/* 2161 */             fv = new float[size];
/* 2162 */             for (i = 0; i < size; i++) {
/* 2163 */               fv[i] = 
/* 2164 */                 Float.intBitsToFloat(readInt(this.items[readUnsignedShort(v)]));
/* 2165 */               v += 3;
/*      */             } 
/* 2167 */             av.visit(name, fv);
/* 2168 */             v--;
/*      */             break;
/*      */           case 68:
/* 2171 */             dv = new double[size];
/* 2172 */             for (i = 0; i < size; i++) {
/* 2173 */               dv[i] = 
/* 2174 */                 Double.longBitsToDouble(readLong(this.items[readUnsignedShort(v)]));
/* 2175 */               v += 3;
/*      */             } 
/* 2177 */             av.visit(name, dv);
/* 2178 */             v--;
/*      */             break;
/*      */         } 
/* 2181 */         v = readAnnotationValues(v - 3, buf, false, av.visitArray(name));
/*      */         break;
/*      */     } 
/* 2184 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void getImplicitFrame(Context frame) {
/* 2195 */     String desc = frame.desc;
/* 2196 */     Object[] locals = frame.local;
/* 2197 */     int local = 0;
/* 2198 */     if ((frame.access & 0x8) == 0) {
/* 2199 */       if ("<init>".equals(frame.name)) {
/* 2200 */         locals[local++] = Opcodes.UNINITIALIZED_THIS;
/*      */       } else {
/* 2202 */         locals[local++] = readClass(this.header + 2, frame.buffer);
/*      */       } 
/*      */     }
/* 2205 */     int i = 1;
/*      */     while (true) {
/* 2207 */       int j = i;
/* 2208 */       switch (desc.charAt(i++)) {
/*      */         case 'B':
/*      */         case 'C':
/*      */         case 'I':
/*      */         case 'S':
/*      */         case 'Z':
/* 2214 */           locals[local++] = Opcodes.INTEGER;
/*      */           continue;
/*      */         case 'F':
/* 2217 */           locals[local++] = Opcodes.FLOAT;
/*      */           continue;
/*      */         case 'J':
/* 2220 */           locals[local++] = Opcodes.LONG;
/*      */           continue;
/*      */         case 'D':
/* 2223 */           locals[local++] = Opcodes.DOUBLE;
/*      */           continue;
/*      */         case '[':
/* 2226 */           while (desc.charAt(i) == '[') {
/* 2227 */             i++;
/*      */           }
/* 2229 */           if (desc.charAt(i) == 'L') {
/* 2230 */             i++;
/* 2231 */             while (desc.charAt(i) != ';') {
/* 2232 */               i++;
/*      */             }
/*      */           } 
/* 2235 */           locals[local++] = desc.substring(j, ++i);
/*      */           continue;
/*      */         case 'L':
/* 2238 */           while (desc.charAt(i) != ';') {
/* 2239 */             i++;
/*      */           }
/* 2241 */           locals[local++] = desc.substring(j + 1, i++);
/*      */           continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 2247 */     frame.localCount = local;
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
/*      */ 
/*      */   
/*      */   private int readFrame(int stackMap, boolean zip, boolean unzip, Context frame) {
/*      */     int tag, delta;
/* 2266 */     char[] c = frame.buffer;
/* 2267 */     Label[] labels = frame.labels;
/*      */ 
/*      */     
/* 2270 */     if (zip) {
/* 2271 */       tag = this.b[stackMap++] & 0xFF;
/*      */     } else {
/* 2273 */       tag = 255;
/* 2274 */       frame.offset = -1;
/*      */     } 
/* 2276 */     frame.localDiff = 0;
/* 2277 */     if (tag < 64) {
/* 2278 */       delta = tag;
/* 2279 */       frame.mode = 3;
/* 2280 */       frame.stackCount = 0;
/* 2281 */     } else if (tag < 128) {
/* 2282 */       delta = tag - 64;
/* 2283 */       stackMap = readFrameType(frame.stack, 0, stackMap, c, labels);
/* 2284 */       frame.mode = 4;
/* 2285 */       frame.stackCount = 1;
/*      */     } else {
/* 2287 */       delta = readUnsignedShort(stackMap);
/* 2288 */       stackMap += 2;
/* 2289 */       if (tag == 247) {
/* 2290 */         stackMap = readFrameType(frame.stack, 0, stackMap, c, labels);
/* 2291 */         frame.mode = 4;
/* 2292 */         frame.stackCount = 1;
/* 2293 */       } else if (tag >= 248 && tag < 251) {
/*      */         
/* 2295 */         frame.mode = 2;
/* 2296 */         frame.localDiff = 251 - tag;
/* 2297 */         frame.localCount -= frame.localDiff;
/* 2298 */         frame.stackCount = 0;
/* 2299 */       } else if (tag == 251) {
/* 2300 */         frame.mode = 3;
/* 2301 */         frame.stackCount = 0;
/* 2302 */       } else if (tag < 255) {
/* 2303 */         int local = unzip ? frame.localCount : 0;
/* 2304 */         for (int i = tag - 251; i > 0; i--) {
/* 2305 */           stackMap = readFrameType(frame.local, local++, stackMap, c, labels);
/*      */         }
/*      */         
/* 2308 */         frame.mode = 1;
/* 2309 */         frame.localDiff = tag - 251;
/* 2310 */         frame.localCount += frame.localDiff;
/* 2311 */         frame.stackCount = 0;
/*      */       } else {
/* 2313 */         frame.mode = 0;
/* 2314 */         int n = readUnsignedShort(stackMap);
/* 2315 */         stackMap += 2;
/* 2316 */         frame.localDiff = n;
/* 2317 */         frame.localCount = n;
/* 2318 */         for (int local = 0; n > 0; n--) {
/* 2319 */           stackMap = readFrameType(frame.local, local++, stackMap, c, labels);
/*      */         }
/*      */         
/* 2322 */         n = readUnsignedShort(stackMap);
/* 2323 */         stackMap += 2;
/* 2324 */         frame.stackCount = n;
/* 2325 */         for (int stack = 0; n > 0; n--) {
/* 2326 */           stackMap = readFrameType(frame.stack, stack++, stackMap, c, labels);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2331 */     frame.offset += delta + 1;
/* 2332 */     readLabel(frame.offset, labels);
/* 2333 */     return stackMap;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readFrameType(Object[] frame, int index, int v, char[] buf, Label[] labels) {
/* 2357 */     int type = this.b[v++] & 0xFF;
/* 2358 */     switch (type)
/*      */     { case 0:
/* 2360 */         frame[index] = Opcodes.TOP;
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
/* 2388 */         return v;case 1: frame[index] = Opcodes.INTEGER; return v;case 2: frame[index] = Opcodes.FLOAT; return v;case 3: frame[index] = Opcodes.DOUBLE; return v;case 4: frame[index] = Opcodes.LONG; return v;case 5: frame[index] = Opcodes.NULL; return v;case 6: frame[index] = Opcodes.UNINITIALIZED_THIS; return v;case 7: frame[index] = readClass(v, buf); v += 2; return v; }  frame[index] = readLabel(readUnsignedShort(v), labels); v += 2; return v;
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
/*      */ 
/*      */   
/*      */   protected Label readLabel(int offset, Label[] labels) {
/* 2406 */     if (offset >= labels.length) {
/* 2407 */       return new Label();
/*      */     }
/*      */     
/* 2410 */     if (labels[offset] == null) {
/* 2411 */       labels[offset] = new Label();
/*      */     }
/* 2413 */     return labels[offset];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getAttributes() {
/* 2423 */     int u = this.header + 8 + readUnsignedShort(this.header + 6) * 2;
/*      */     int i;
/* 2425 */     for (i = readUnsignedShort(u); i > 0; i--) {
/* 2426 */       for (int j = readUnsignedShort(u + 8); j > 0; j--) {
/* 2427 */         u += 6 + readInt(u + 12);
/*      */       }
/* 2429 */       u += 8;
/*      */     } 
/* 2431 */     u += 2;
/* 2432 */     for (i = readUnsignedShort(u); i > 0; i--) {
/* 2433 */       for (int j = readUnsignedShort(u + 8); j > 0; j--) {
/* 2434 */         u += 6 + readInt(u + 12);
/*      */       }
/* 2436 */       u += 8;
/*      */     } 
/*      */     
/* 2439 */     return u + 2;
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
/*      */   private Attribute readAttribute(Attribute[] attrs, String type, int off, int len, char[] buf, int codeOff, Label[] labels) {
/* 2478 */     for (int i = 0; i < attrs.length; i++) {
/* 2479 */       if ((attrs[i]).type.equals(type)) {
/* 2480 */         return attrs[i].read(this, off, len, buf, codeOff, labels);
/*      */       }
/*      */     } 
/* 2483 */     return (new Attribute(type)).read(this, off, len, null, -1, null);
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
/*      */   public int getItemCount() {
/* 2496 */     return this.items.length;
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
/*      */   public int getItem(int item) {
/* 2510 */     return this.items[item];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStringLength() {
/* 2521 */     return this.maxStringLength;
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
/*      */   public int readByte(int index) {
/* 2534 */     return this.b[index] & 0xFF;
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
/*      */   public int readUnsignedShort(int index) {
/* 2547 */     byte[] b = this.b;
/* 2548 */     return (b[index] & 0xFF) << 8 | b[index + 1] & 0xFF;
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
/*      */   public short readShort(int index) {
/* 2561 */     byte[] b = this.b;
/* 2562 */     return (short)((b[index] & 0xFF) << 8 | b[index + 1] & 0xFF);
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
/*      */   public int readInt(int index) {
/* 2575 */     byte[] b = this.b;
/* 2576 */     return (b[index] & 0xFF) << 24 | (b[index + 1] & 0xFF) << 16 | (b[index + 2] & 0xFF) << 8 | b[index + 3] & 0xFF;
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
/*      */   public long readLong(int index) {
/* 2590 */     long l1 = readInt(index);
/* 2591 */     long l0 = readInt(index + 4) & 0xFFFFFFFFL;
/* 2592 */     return l1 << 32L | l0;
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
/*      */   
/*      */   public String readUTF8(int index, char[] buf) {
/* 2609 */     int item = readUnsignedShort(index);
/* 2610 */     if (index == 0 || item == 0) {
/* 2611 */       return null;
/*      */     }
/* 2613 */     String s = this.strings[item];
/* 2614 */     if (s != null) {
/* 2615 */       return s;
/*      */     }
/* 2617 */     index = this.items[item];
/* 2618 */     this.strings[item] = readUTF(index + 2, readUnsignedShort(index), buf); return readUTF(index + 2, readUnsignedShort(index), buf);
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
/*      */   private String readUTF(int index, int utfLen, char[] buf) {
/* 2634 */     int endIndex = index + utfLen;
/* 2635 */     byte[] b = this.b;
/* 2636 */     int strLen = 0;
/*      */     
/* 2638 */     int st = 0;
/* 2639 */     char cc = Character.MIN_VALUE;
/* 2640 */     while (index < endIndex) {
/* 2641 */       int c = b[index++];
/* 2642 */       switch (st) {
/*      */         case 0:
/* 2644 */           c &= 0xFF;
/* 2645 */           if (c < 128) {
/* 2646 */             buf[strLen++] = (char)c; continue;
/* 2647 */           }  if (c < 224 && c > 191) {
/* 2648 */             cc = (char)(c & 0x1F);
/* 2649 */             st = 1; continue;
/*      */           } 
/* 2651 */           cc = (char)(c & 0xF);
/* 2652 */           st = 2;
/*      */ 
/*      */ 
/*      */         
/*      */         case 1:
/* 2657 */           buf[strLen++] = (char)(cc << 6 | c & 0x3F);
/* 2658 */           st = 0;
/*      */ 
/*      */         
/*      */         case 2:
/* 2662 */           cc = (char)(cc << 6 | c & 0x3F);
/* 2663 */           st = 1;
/*      */       } 
/*      */     
/*      */     } 
/* 2667 */     return new String(buf, 0, strLen);
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
/*      */   private String readStringish(int index, char[] buf) {
/* 2681 */     return readUTF8(this.items[readUnsignedShort(index)], buf);
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
/*      */   
/*      */   public String readClass(int index, char[] buf) {
/* 2698 */     return readStringish(index, buf);
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
/*      */   
/*      */   public String readModule(int index, char[] buf) {
/* 2715 */     return readStringish(index, buf);
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
/*      */   
/*      */   public String readPackage(int index, char[] buf) {
/* 2732 */     return readStringish(index, buf);
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
/*      */ 
/*      */   
/*      */   public Object readConst(int item, char[] buf) {
/* 2750 */     int index = this.items[item];
/* 2751 */     switch (this.b[index - 1]) {
/*      */       case 3:
/* 2753 */         return Integer.valueOf(readInt(index));
/*      */       case 4:
/* 2755 */         return Float.valueOf(Float.intBitsToFloat(readInt(index)));
/*      */       case 5:
/* 2757 */         return Long.valueOf(readLong(index));
/*      */       case 6:
/* 2759 */         return Double.valueOf(Double.longBitsToDouble(readLong(index)));
/*      */       case 7:
/* 2761 */         return Type.getObjectType(readUTF8(index, buf));
/*      */       case 8:
/* 2763 */         return readUTF8(index, buf);
/*      */       case 16:
/* 2765 */         return Type.getMethodType(readUTF8(index, buf));
/*      */     } 
/* 2767 */     int tag = readByte(index);
/* 2768 */     int[] items = this.items;
/* 2769 */     int cpIndex = items[readUnsignedShort(index + 1)];
/* 2770 */     boolean itf = (this.b[cpIndex - 1] == 11);
/* 2771 */     String owner = readClass(cpIndex, buf);
/* 2772 */     cpIndex = items[readUnsignedShort(cpIndex + 2)];
/* 2773 */     String name = readUTF8(cpIndex, buf);
/* 2774 */     String desc = readUTF8(cpIndex + 2, buf);
/* 2775 */     return new Handle(tag, owner, name, desc, itf);
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\asm\ClassReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */