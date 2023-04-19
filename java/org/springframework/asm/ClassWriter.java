/*      */ package org.springframework.asm;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassWriter
/*      */   extends ClassVisitor
/*      */ {
/*      */   public static final int COMPUTE_MAXS = 1;
/*      */   public static final int COMPUTE_FRAMES = 2;
/*      */   static final int ACC_SYNTHETIC_ATTRIBUTE = 262144;
/*      */   static final int TO_ACC_SYNTHETIC = 64;
/*      */   static final int NOARG_INSN = 0;
/*      */   static final int SBYTE_INSN = 1;
/*      */   static final int SHORT_INSN = 2;
/*      */   static final int VAR_INSN = 3;
/*      */   static final int IMPLVAR_INSN = 4;
/*      */   static final int TYPE_INSN = 5;
/*      */   static final int FIELDORMETH_INSN = 6;
/*      */   static final int ITFMETH_INSN = 7;
/*      */   static final int INDYMETH_INSN = 8;
/*      */   static final int LABEL_INSN = 9;
/*      */   static final int LABELW_INSN = 10;
/*      */   static final int LDC_INSN = 11;
/*      */   static final int LDCW_INSN = 12;
/*      */   static final int IINC_INSN = 13;
/*      */   static final int TABL_INSN = 14;
/*      */   static final int LOOK_INSN = 15;
/*      */   static final int MANA_INSN = 16;
/*      */   static final int WIDE_INSN = 17;
/*      */   static final int ASM_LABEL_INSN = 18;
/*      */   static final int ASM_LABELW_INSN = 19;
/*      */   static final int F_INSERT = 256;
/*      */   static final byte[] TYPE;
/*      */   static final int CLASS = 7;
/*      */   static final int FIELD = 9;
/*      */   static final int METH = 10;
/*      */   static final int IMETH = 11;
/*      */   static final int STR = 8;
/*      */   static final int INT = 3;
/*      */   static final int FLOAT = 4;
/*      */   static final int LONG = 5;
/*      */   static final int DOUBLE = 6;
/*      */   static final int NAME_TYPE = 12;
/*      */   static final int UTF8 = 1;
/*      */   static final int MTYPE = 16;
/*      */   static final int HANDLE = 15;
/*      */   static final int INDY = 18;
/*      */   static final int MODULE = 19;
/*      */   static final int PACKAGE = 20;
/*      */   static final int HANDLE_BASE = 20;
/*      */   static final int TYPE_NORMAL = 30;
/*      */   static final int TYPE_UNINIT = 31;
/*      */   static final int TYPE_MERGED = 32;
/*      */   static final int BSM = 33;
/*      */   ClassReader cr;
/*      */   int version;
/*      */   int index;
/*      */   final ByteVector pool;
/*      */   Item[] items;
/*      */   int threshold;
/*      */   final Item key;
/*      */   final Item key2;
/*      */   final Item key3;
/*      */   final Item key4;
/*      */   Item[] typeTable;
/*      */   private short typeCount;
/*      */   private int access;
/*      */   private int name;
/*      */   String thisName;
/*      */   private int signature;
/*      */   private int superName;
/*      */   private int interfaceCount;
/*      */   private int[] interfaces;
/*      */   private int sourceFile;
/*      */   private ByteVector sourceDebug;
/*      */   private ModuleWriter moduleWriter;
/*      */   private int enclosingMethodOwner;
/*      */   private int enclosingMethod;
/*      */   private AnnotationWriter anns;
/*      */   private AnnotationWriter ianns;
/*      */   private AnnotationWriter tanns;
/*      */   private AnnotationWriter itanns;
/*      */   private Attribute attrs;
/*      */   private int innerClassesCount;
/*      */   private ByteVector innerClasses;
/*      */   int bootstrapMethodsCount;
/*      */   ByteVector bootstrapMethods;
/*      */   FieldWriter firstField;
/*      */   FieldWriter lastField;
/*      */   MethodWriter firstMethod;
/*      */   MethodWriter lastMethod;
/*      */   private int compute;
/*      */   boolean hasAsmInsns;
/*      */   
/*      */   static {
/*  546 */     byte[] b = new byte[221];
/*  547 */     String s = "AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKSSSSSSSSSSSSSSSSSST";
/*      */ 
/*      */ 
/*      */     
/*  551 */     for (int i = 0; i < b.length; i++) {
/*  552 */       b[i] = (byte)(s.charAt(i) - 65);
/*      */     }
/*  554 */     TYPE = b;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassWriter(int flags) {
/*  639 */     super(393216);
/*  640 */     this.index = 1;
/*  641 */     this.pool = new ByteVector();
/*  642 */     this.items = new Item[256];
/*  643 */     this.threshold = (int)(0.75D * this.items.length);
/*  644 */     this.key = new Item();
/*  645 */     this.key2 = new Item();
/*  646 */     this.key3 = new Item();
/*  647 */     this.key4 = new Item();
/*  648 */     this.compute = ((flags & 0x2) != 0) ? 0 : (((flags & 0x1) != 0) ? 2 : 3);
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
/*      */   public ClassWriter(ClassReader classReader, int flags) {
/*  686 */     this(flags);
/*  687 */     classReader.copyPool(this);
/*  688 */     this.cr = classReader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/*  699 */     this.version = version;
/*  700 */     this.access = access;
/*  701 */     this.name = newClass(name);
/*  702 */     this.thisName = name;
/*  703 */     if (signature != null) {
/*  704 */       this.signature = newUTF8(signature);
/*      */     }
/*  706 */     this.superName = (superName == null) ? 0 : newClass(superName);
/*  707 */     if (interfaces != null && interfaces.length > 0) {
/*  708 */       this.interfaceCount = interfaces.length;
/*  709 */       this.interfaces = new int[this.interfaceCount];
/*  710 */       for (int i = 0; i < this.interfaceCount; i++) {
/*  711 */         this.interfaces[i] = newClass(interfaces[i]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void visitSource(String file, String debug) {
/*  718 */     if (file != null) {
/*  719 */       this.sourceFile = newUTF8(file);
/*      */     }
/*  721 */     if (debug != null) {
/*  722 */       this.sourceDebug = (new ByteVector()).encodeUTF8(debug, 0, 2147483647);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ModuleVisitor visitModule(String name, int access, String version) {
/*  730 */     return this
/*      */       
/*  732 */       .moduleWriter = new ModuleWriter(this, newModule(name), access, (version == null) ? 0 : newUTF8(version));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void visitOuterClass(String owner, String name, String desc) {
/*  738 */     this.enclosingMethodOwner = newClass(owner);
/*  739 */     if (name != null && desc != null) {
/*  740 */       this.enclosingMethod = newNameType(name, desc);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  750 */     ByteVector bv = new ByteVector();
/*      */     
/*  752 */     bv.putShort(newUTF8(desc)).putShort(0);
/*  753 */     AnnotationWriter aw = new AnnotationWriter(this, true, bv, bv, 2);
/*  754 */     if (visible) {
/*  755 */       aw.next = this.anns;
/*  756 */       this.anns = aw;
/*      */     } else {
/*  758 */       aw.next = this.ianns;
/*  759 */       this.ianns = aw;
/*      */     } 
/*  761 */     return aw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/*  770 */     ByteVector bv = new ByteVector();
/*      */     
/*  772 */     AnnotationWriter.putTarget(typeRef, typePath, bv);
/*      */     
/*  774 */     bv.putShort(newUTF8(desc)).putShort(0);
/*  775 */     AnnotationWriter aw = new AnnotationWriter(this, true, bv, bv, bv.length - 2);
/*      */     
/*  777 */     if (visible) {
/*  778 */       aw.next = this.tanns;
/*  779 */       this.tanns = aw;
/*      */     } else {
/*  781 */       aw.next = this.itanns;
/*  782 */       this.itanns = aw;
/*      */     } 
/*  784 */     return aw;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void visitAttribute(Attribute attr) {
/*  789 */     attr.next = this.attrs;
/*  790 */     this.attrs = attr;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void visitInnerClass(String name, String outerName, String innerName, int access) {
/*  796 */     if (this.innerClasses == null) {
/*  797 */       this.innerClasses = new ByteVector();
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
/*  809 */     Item nameItem = newStringishItem(7, name);
/*  810 */     if (nameItem.intVal == 0) {
/*  811 */       this.innerClassesCount++;
/*  812 */       this.innerClasses.putShort(nameItem.index);
/*  813 */       this.innerClasses.putShort((outerName == null) ? 0 : newClass(outerName));
/*  814 */       this.innerClasses.putShort((innerName == null) ? 0 : newUTF8(innerName));
/*  815 */       this.innerClasses.putShort(access);
/*  816 */       nameItem.intVal = this.innerClassesCount;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/*  827 */     return new FieldWriter(this, access, name, desc, signature, value);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/*  833 */     return new MethodWriter(this, access, name, desc, signature, exceptions, this.compute);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void visitEnd() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] toByteArray() {
/*  851 */     if (this.index > 65535) {
/*  852 */       throw new RuntimeException("Class file too large!");
/*      */     }
/*      */     
/*  855 */     int size = 24 + 2 * this.interfaceCount;
/*  856 */     int nbFields = 0;
/*  857 */     FieldWriter fb = this.firstField;
/*  858 */     while (fb != null) {
/*  859 */       nbFields++;
/*  860 */       size += fb.getSize();
/*  861 */       fb = (FieldWriter)fb.fv;
/*      */     } 
/*  863 */     int nbMethods = 0;
/*  864 */     MethodWriter mb = this.firstMethod;
/*  865 */     while (mb != null) {
/*  866 */       nbMethods++;
/*  867 */       size += mb.getSize();
/*  868 */       mb = (MethodWriter)mb.mv;
/*      */     } 
/*  870 */     int attributeCount = 0;
/*  871 */     if (this.bootstrapMethods != null) {
/*      */ 
/*      */       
/*  874 */       attributeCount++;
/*  875 */       size += 8 + this.bootstrapMethods.length;
/*  876 */       newUTF8("BootstrapMethods");
/*      */     } 
/*  878 */     if (this.signature != 0) {
/*  879 */       attributeCount++;
/*  880 */       size += 8;
/*  881 */       newUTF8("Signature");
/*      */     } 
/*  883 */     if (this.sourceFile != 0) {
/*  884 */       attributeCount++;
/*  885 */       size += 8;
/*  886 */       newUTF8("SourceFile");
/*      */     } 
/*  888 */     if (this.sourceDebug != null) {
/*  889 */       attributeCount++;
/*  890 */       size += this.sourceDebug.length + 6;
/*  891 */       newUTF8("SourceDebugExtension");
/*      */     } 
/*  893 */     if (this.enclosingMethodOwner != 0) {
/*  894 */       attributeCount++;
/*  895 */       size += 10;
/*  896 */       newUTF8("EnclosingMethod");
/*      */     } 
/*  898 */     if ((this.access & 0x20000) != 0) {
/*  899 */       attributeCount++;
/*  900 */       size += 6;
/*  901 */       newUTF8("Deprecated");
/*      */     } 
/*  903 */     if ((this.access & 0x1000) != 0 && ((
/*  904 */       this.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0)) {
/*      */       
/*  906 */       attributeCount++;
/*  907 */       size += 6;
/*  908 */       newUTF8("Synthetic");
/*      */     } 
/*      */     
/*  911 */     if (this.innerClasses != null) {
/*  912 */       attributeCount++;
/*  913 */       size += 8 + this.innerClasses.length;
/*  914 */       newUTF8("InnerClasses");
/*      */     } 
/*  916 */     if (this.anns != null) {
/*  917 */       attributeCount++;
/*  918 */       size += 8 + this.anns.getSize();
/*  919 */       newUTF8("RuntimeVisibleAnnotations");
/*      */     } 
/*  921 */     if (this.ianns != null) {
/*  922 */       attributeCount++;
/*  923 */       size += 8 + this.ianns.getSize();
/*  924 */       newUTF8("RuntimeInvisibleAnnotations");
/*      */     } 
/*  926 */     if (this.tanns != null) {
/*  927 */       attributeCount++;
/*  928 */       size += 8 + this.tanns.getSize();
/*  929 */       newUTF8("RuntimeVisibleTypeAnnotations");
/*      */     } 
/*  931 */     if (this.itanns != null) {
/*  932 */       attributeCount++;
/*  933 */       size += 8 + this.itanns.getSize();
/*  934 */       newUTF8("RuntimeInvisibleTypeAnnotations");
/*      */     } 
/*  936 */     if (this.moduleWriter != null) {
/*  937 */       attributeCount += 1 + this.moduleWriter.attributeCount;
/*  938 */       size += 6 + this.moduleWriter.size + this.moduleWriter.attributesSize;
/*  939 */       newUTF8("Module");
/*      */     } 
/*  941 */     if (this.attrs != null) {
/*  942 */       attributeCount += this.attrs.getCount();
/*  943 */       size += this.attrs.getSize(this, null, 0, -1, -1);
/*      */     } 
/*  945 */     size += this.pool.length;
/*      */ 
/*      */     
/*  948 */     ByteVector out = new ByteVector(size);
/*  949 */     out.putInt(-889275714).putInt(this.version);
/*  950 */     out.putShort(this.index).putByteArray(this.pool.data, 0, this.pool.length);
/*  951 */     int mask = 0x60000 | (this.access & 0x40000) / 64;
/*      */     
/*  953 */     out.putShort(this.access & (mask ^ 0xFFFFFFFF)).putShort(this.name).putShort(this.superName);
/*  954 */     out.putShort(this.interfaceCount);
/*  955 */     for (int i = 0; i < this.interfaceCount; i++) {
/*  956 */       out.putShort(this.interfaces[i]);
/*      */     }
/*  958 */     out.putShort(nbFields);
/*  959 */     fb = this.firstField;
/*  960 */     while (fb != null) {
/*  961 */       fb.put(out);
/*  962 */       fb = (FieldWriter)fb.fv;
/*      */     } 
/*  964 */     out.putShort(nbMethods);
/*  965 */     mb = this.firstMethod;
/*  966 */     while (mb != null) {
/*  967 */       mb.put(out);
/*  968 */       mb = (MethodWriter)mb.mv;
/*      */     } 
/*  970 */     out.putShort(attributeCount);
/*  971 */     if (this.bootstrapMethods != null) {
/*  972 */       out.putShort(newUTF8("BootstrapMethods"));
/*  973 */       out.putInt(this.bootstrapMethods.length + 2).putShort(this.bootstrapMethodsCount);
/*      */       
/*  975 */       out.putByteArray(this.bootstrapMethods.data, 0, this.bootstrapMethods.length);
/*      */     } 
/*  977 */     if (this.signature != 0) {
/*  978 */       out.putShort(newUTF8("Signature")).putInt(2).putShort(this.signature);
/*      */     }
/*  980 */     if (this.sourceFile != 0) {
/*  981 */       out.putShort(newUTF8("SourceFile")).putInt(2).putShort(this.sourceFile);
/*      */     }
/*  983 */     if (this.sourceDebug != null) {
/*  984 */       int len = this.sourceDebug.length;
/*  985 */       out.putShort(newUTF8("SourceDebugExtension")).putInt(len);
/*  986 */       out.putByteArray(this.sourceDebug.data, 0, len);
/*      */     } 
/*  988 */     if (this.moduleWriter != null) {
/*  989 */       out.putShort(newUTF8("Module"));
/*  990 */       this.moduleWriter.put(out);
/*  991 */       this.moduleWriter.putAttributes(out);
/*      */     } 
/*  993 */     if (this.enclosingMethodOwner != 0) {
/*  994 */       out.putShort(newUTF8("EnclosingMethod")).putInt(4);
/*  995 */       out.putShort(this.enclosingMethodOwner).putShort(this.enclosingMethod);
/*      */     } 
/*  997 */     if ((this.access & 0x20000) != 0) {
/*  998 */       out.putShort(newUTF8("Deprecated")).putInt(0);
/*      */     }
/* 1000 */     if ((this.access & 0x1000) != 0 && ((
/* 1001 */       this.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0))
/*      */     {
/* 1003 */       out.putShort(newUTF8("Synthetic")).putInt(0);
/*      */     }
/*      */     
/* 1006 */     if (this.innerClasses != null) {
/* 1007 */       out.putShort(newUTF8("InnerClasses"));
/* 1008 */       out.putInt(this.innerClasses.length + 2).putShort(this.innerClassesCount);
/* 1009 */       out.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
/*      */     } 
/* 1011 */     if (this.anns != null) {
/* 1012 */       out.putShort(newUTF8("RuntimeVisibleAnnotations"));
/* 1013 */       this.anns.put(out);
/*      */     } 
/* 1015 */     if (this.ianns != null) {
/* 1016 */       out.putShort(newUTF8("RuntimeInvisibleAnnotations"));
/* 1017 */       this.ianns.put(out);
/*      */     } 
/* 1019 */     if (this.tanns != null) {
/* 1020 */       out.putShort(newUTF8("RuntimeVisibleTypeAnnotations"));
/* 1021 */       this.tanns.put(out);
/*      */     } 
/* 1023 */     if (this.itanns != null) {
/* 1024 */       out.putShort(newUTF8("RuntimeInvisibleTypeAnnotations"));
/* 1025 */       this.itanns.put(out);
/*      */     } 
/* 1027 */     if (this.attrs != null) {
/* 1028 */       this.attrs.put(this, null, 0, -1, -1, out);
/*      */     }
/* 1030 */     if (this.hasAsmInsns) {
/* 1031 */       int j; boolean hasFrames = false;
/* 1032 */       mb = this.firstMethod;
/* 1033 */       while (mb != null) {
/* 1034 */         j = hasFrames | ((mb.frameCount > 0) ? 1 : 0);
/* 1035 */         mb = (MethodWriter)mb.mv;
/*      */       } 
/* 1037 */       this.anns = null;
/* 1038 */       this.ianns = null;
/* 1039 */       this.attrs = null;
/* 1040 */       this.moduleWriter = null;
/* 1041 */       this.innerClassesCount = 0;
/* 1042 */       this.innerClasses = null;
/* 1043 */       this.firstField = null;
/* 1044 */       this.lastField = null;
/* 1045 */       this.firstMethod = null;
/* 1046 */       this.lastMethod = null;
/* 1047 */       this.compute = (j != 0) ? 1 : 0;
/* 1048 */       this.hasAsmInsns = false;
/* 1049 */       (new ClassReader(out.data)).accept(this, ((j != 0) ? 8 : 0) | 0x100);
/*      */ 
/*      */       
/* 1052 */       return toByteArray();
/*      */     } 
/* 1054 */     return out.data;
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
/*      */   Item newConstItem(Object cst) {
/* 1073 */     if (cst instanceof Integer) {
/* 1074 */       int val = ((Integer)cst).intValue();
/* 1075 */       return newInteger(val);
/* 1076 */     }  if (cst instanceof Byte) {
/* 1077 */       int val = ((Byte)cst).intValue();
/* 1078 */       return newInteger(val);
/* 1079 */     }  if (cst instanceof Character) {
/* 1080 */       int val = ((Character)cst).charValue();
/* 1081 */       return newInteger(val);
/* 1082 */     }  if (cst instanceof Short) {
/* 1083 */       int val = ((Short)cst).intValue();
/* 1084 */       return newInteger(val);
/* 1085 */     }  if (cst instanceof Boolean) {
/* 1086 */       int val = ((Boolean)cst).booleanValue() ? 1 : 0;
/* 1087 */       return newInteger(val);
/* 1088 */     }  if (cst instanceof Float) {
/* 1089 */       float val = ((Float)cst).floatValue();
/* 1090 */       return newFloat(val);
/* 1091 */     }  if (cst instanceof Long) {
/* 1092 */       long val = ((Long)cst).longValue();
/* 1093 */       return newLong(val);
/* 1094 */     }  if (cst instanceof Double) {
/* 1095 */       double val = ((Double)cst).doubleValue();
/* 1096 */       return newDouble(val);
/* 1097 */     }  if (cst instanceof String)
/* 1098 */       return newStringishItem(8, (String)cst); 
/* 1099 */     if (cst instanceof Type) {
/* 1100 */       Type t = (Type)cst;
/* 1101 */       int s = t.getSort();
/* 1102 */       if (s == 10)
/* 1103 */         return newStringishItem(7, t.getInternalName()); 
/* 1104 */       if (s == 11) {
/* 1105 */         return newStringishItem(16, t.getDescriptor());
/*      */       }
/* 1107 */       return newStringishItem(7, t.getDescriptor());
/*      */     } 
/* 1109 */     if (cst instanceof Handle) {
/* 1110 */       Handle h = (Handle)cst;
/* 1111 */       return newHandleItem(h.tag, h.owner, h.name, h.desc, h.itf);
/*      */     } 
/* 1113 */     throw new IllegalArgumentException("value " + cst);
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
/*      */   public int newConst(Object cst) {
/* 1131 */     return (newConstItem(cst)).index;
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
/*      */   public int newUTF8(String value) {
/* 1145 */     this.key.set(1, value, null, null);
/* 1146 */     Item result = get(this.key);
/* 1147 */     if (result == null) {
/* 1148 */       this.pool.putByte(1).putUTF8(value);
/* 1149 */       result = new Item(this.index++, this.key);
/* 1150 */       put(result);
/*      */     } 
/* 1152 */     return result.index;
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
/*      */   Item newStringishItem(int type, String value) {
/* 1167 */     this.key2.set(type, value, null, null);
/* 1168 */     Item result = get(this.key2);
/* 1169 */     if (result == null) {
/* 1170 */       this.pool.put12(type, newUTF8(value));
/* 1171 */       result = new Item(this.index++, this.key2);
/* 1172 */       put(result);
/*      */     } 
/* 1174 */     return result;
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
/*      */   public int newClass(String value) {
/* 1188 */     return (newStringishItem(7, value)).index;
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
/*      */   public int newMethodType(String methodDesc) {
/* 1203 */     return (newStringishItem(16, methodDesc)).index;
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
/*      */   public int newModule(String moduleName) {
/* 1218 */     return (newStringishItem(19, moduleName)).index;
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
/*      */   public int newPackage(String packageName) {
/* 1233 */     return (newStringishItem(20, packageName)).index;
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
/*      */   Item newHandleItem(int tag, String owner, String name, String desc, boolean itf) {
/* 1262 */     this.key4.set(20 + tag, owner, name, desc);
/* 1263 */     Item result = get(this.key4);
/* 1264 */     if (result == null) {
/* 1265 */       if (tag <= 4) {
/* 1266 */         put112(15, tag, newField(owner, name, desc));
/*      */       } else {
/* 1268 */         put112(15, tag, 
/*      */             
/* 1270 */             newMethod(owner, name, desc, itf));
/*      */       } 
/* 1272 */       result = new Item(this.index++, this.key4);
/* 1273 */       put(result);
/*      */     } 
/* 1275 */     return result;
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
/*      */   @Deprecated
/*      */   public int newHandle(int tag, String owner, String name, String desc) {
/* 1307 */     return newHandle(tag, owner, name, desc, (tag == 9));
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
/*      */   public int newHandle(int tag, String owner, String name, String desc, boolean itf) {
/* 1337 */     return (newHandleItem(tag, owner, name, desc, itf)).index;
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
/*      */   Item newInvokeDynamicItem(String name, String desc, Handle bsm, Object... bsmArgs) {
/*      */     int bootstrapMethodIndex;
/* 1360 */     ByteVector bootstrapMethods = this.bootstrapMethods;
/* 1361 */     if (bootstrapMethods == null) {
/* 1362 */       bootstrapMethods = this.bootstrapMethods = new ByteVector();
/*      */     }
/*      */     
/* 1365 */     int position = bootstrapMethods.length;
/*      */     
/* 1367 */     int hashCode = bsm.hashCode();
/* 1368 */     bootstrapMethods.putShort(newHandle(bsm.tag, bsm.owner, bsm.name, bsm.desc, bsm
/* 1369 */           .isInterface()));
/*      */     
/* 1371 */     int argsLength = bsmArgs.length;
/* 1372 */     bootstrapMethods.putShort(argsLength);
/*      */     
/* 1374 */     for (int i = 0; i < argsLength; i++) {
/* 1375 */       Object bsmArg = bsmArgs[i];
/* 1376 */       hashCode ^= bsmArg.hashCode();
/* 1377 */       bootstrapMethods.putShort(newConst(bsmArg));
/*      */     } 
/*      */     
/* 1380 */     byte[] data = bootstrapMethods.data;
/* 1381 */     int length = 2 + argsLength << 1;
/* 1382 */     hashCode &= Integer.MAX_VALUE;
/* 1383 */     Item result = this.items[hashCode % this.items.length];
/* 1384 */     label35: while (result != null) {
/* 1385 */       if (result.type != 33 || result.hashCode != hashCode) {
/* 1386 */         result = result.next;
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1392 */       int resultPosition = result.intVal;
/* 1393 */       for (int p = 0; p < length; p++) {
/* 1394 */         if (data[position + p] != data[resultPosition + p]) {
/* 1395 */           result = result.next;
/*      */ 
/*      */           
/*      */           continue label35;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1403 */     if (result != null) {
/* 1404 */       bootstrapMethodIndex = result.index;
/* 1405 */       bootstrapMethods.length = position;
/*      */     } else {
/* 1407 */       bootstrapMethodIndex = this.bootstrapMethodsCount++;
/* 1408 */       result = new Item(bootstrapMethodIndex);
/* 1409 */       result.set(position, hashCode);
/* 1410 */       put(result);
/*      */     } 
/*      */ 
/*      */     
/* 1414 */     this.key3.set(name, desc, bootstrapMethodIndex);
/* 1415 */     result = get(this.key3);
/* 1416 */     if (result == null) {
/* 1417 */       put122(18, bootstrapMethodIndex, newNameType(name, desc));
/* 1418 */       result = new Item(this.index++, this.key3);
/* 1419 */       put(result);
/*      */     } 
/* 1421 */     return result;
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
/*      */   public int newInvokeDynamic(String name, String desc, Handle bsm, Object... bsmArgs) {
/* 1444 */     return (newInvokeDynamicItem(name, desc, bsm, bsmArgs)).index;
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
/*      */   Item newFieldItem(String owner, String name, String desc) {
/* 1460 */     this.key3.set(9, owner, name, desc);
/* 1461 */     Item result = get(this.key3);
/* 1462 */     if (result == null) {
/* 1463 */       put122(9, newClass(owner), newNameType(name, desc));
/* 1464 */       result = new Item(this.index++, this.key3);
/* 1465 */       put(result);
/*      */     } 
/* 1467 */     return result;
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
/*      */   public int newField(String owner, String name, String desc) {
/* 1485 */     return (newFieldItem(owner, name, desc)).index;
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
/*      */   Item newMethodItem(String owner, String name, String desc, boolean itf) {
/* 1504 */     int type = itf ? 11 : 10;
/* 1505 */     this.key3.set(type, owner, name, desc);
/* 1506 */     Item result = get(this.key3);
/* 1507 */     if (result == null) {
/* 1508 */       put122(type, newClass(owner), newNameType(name, desc));
/* 1509 */       result = new Item(this.index++, this.key3);
/* 1510 */       put(result);
/*      */     } 
/* 1512 */     return result;
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
/*      */   public int newMethod(String owner, String name, String desc, boolean itf) {
/* 1533 */     return (newMethodItem(owner, name, desc, itf)).index;
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
/*      */   Item newInteger(int value) {
/* 1545 */     this.key.set(value);
/* 1546 */     Item result = get(this.key);
/* 1547 */     if (result == null) {
/* 1548 */       this.pool.putByte(3).putInt(value);
/* 1549 */       result = new Item(this.index++, this.key);
/* 1550 */       put(result);
/*      */     } 
/* 1552 */     return result;
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
/*      */   Item newFloat(float value) {
/* 1564 */     this.key.set(value);
/* 1565 */     Item result = get(this.key);
/* 1566 */     if (result == null) {
/* 1567 */       this.pool.putByte(4).putInt(this.key.intVal);
/* 1568 */       result = new Item(this.index++, this.key);
/* 1569 */       put(result);
/*      */     } 
/* 1571 */     return result;
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
/*      */   Item newLong(long value) {
/* 1583 */     this.key.set(value);
/* 1584 */     Item result = get(this.key);
/* 1585 */     if (result == null) {
/* 1586 */       this.pool.putByte(5).putLong(value);
/* 1587 */       result = new Item(this.index, this.key);
/* 1588 */       this.index += 2;
/* 1589 */       put(result);
/*      */     } 
/* 1591 */     return result;
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
/*      */   Item newDouble(double value) {
/* 1603 */     this.key.set(value);
/* 1604 */     Item result = get(this.key);
/* 1605 */     if (result == null) {
/* 1606 */       this.pool.putByte(6).putLong(this.key.longVal);
/* 1607 */       result = new Item(this.index, this.key);
/* 1608 */       this.index += 2;
/* 1609 */       put(result);
/*      */     } 
/* 1611 */     return result;
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
/*      */   public int newNameType(String name, String desc) {
/* 1627 */     return (newNameTypeItem(name, desc)).index;
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
/*      */   Item newNameTypeItem(String name, String desc) {
/* 1641 */     this.key2.set(12, name, desc, null);
/* 1642 */     Item result = get(this.key2);
/* 1643 */     if (result == null) {
/* 1644 */       put122(12, newUTF8(name), newUTF8(desc));
/* 1645 */       result = new Item(this.index++, this.key2);
/* 1646 */       put(result);
/*      */     } 
/* 1648 */     return result;
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
/*      */   int addType(String type) {
/* 1660 */     this.key.set(30, type, null, null);
/* 1661 */     Item result = get(this.key);
/* 1662 */     if (result == null) {
/* 1663 */       result = addType(this.key);
/*      */     }
/* 1665 */     return result.index;
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
/*      */   int addUninitializedType(String type, int offset) {
/* 1681 */     this.key.type = 31;
/* 1682 */     this.key.intVal = offset;
/* 1683 */     this.key.strVal1 = type;
/* 1684 */     this.key.hashCode = Integer.MAX_VALUE & 31 + type.hashCode() + offset;
/* 1685 */     Item result = get(this.key);
/* 1686 */     if (result == null) {
/* 1687 */       result = addType(this.key);
/*      */     }
/* 1689 */     return result.index;
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
/*      */   private Item addType(Item item) {
/* 1701 */     this.typeCount = (short)(this.typeCount + 1);
/* 1702 */     Item result = new Item(this.typeCount, item);
/* 1703 */     put(result);
/* 1704 */     if (this.typeTable == null) {
/* 1705 */       this.typeTable = new Item[16];
/*      */     }
/* 1707 */     if (this.typeCount == this.typeTable.length) {
/* 1708 */       Item[] newTable = new Item[2 * this.typeTable.length];
/* 1709 */       System.arraycopy(this.typeTable, 0, newTable, 0, this.typeTable.length);
/* 1710 */       this.typeTable = newTable;
/*      */     } 
/* 1712 */     this.typeTable[this.typeCount] = result;
/* 1713 */     return result;
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
/*      */   int getMergedType(int type1, int type2) {
/* 1729 */     this.key2.type = 32;
/* 1730 */     this.key2.longVal = type1 | type2 << 32L;
/* 1731 */     this.key2.hashCode = Integer.MAX_VALUE & 32 + type1 + type2;
/* 1732 */     Item result = get(this.key2);
/* 1733 */     if (result == null) {
/* 1734 */       String t = (this.typeTable[type1]).strVal1;
/* 1735 */       String u = (this.typeTable[type2]).strVal1;
/* 1736 */       this.key2.intVal = addType(getCommonSuperClass(t, u));
/* 1737 */       result = new Item(0, this.key2);
/* 1738 */       put(result);
/*      */     } 
/* 1740 */     return result.intVal;
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
/*      */   protected String getCommonSuperClass(String type1, String type2) {
/*      */     Class<?> c, d;
/* 1762 */     ClassLoader classLoader = getClassLoader();
/*      */     try {
/* 1764 */       c = Class.forName(type1.replace('/', '.'), false, classLoader);
/* 1765 */       d = Class.forName(type2.replace('/', '.'), false, classLoader);
/* 1766 */     } catch (Exception e) {
/* 1767 */       throw new RuntimeException(e.toString());
/*      */     } 
/* 1769 */     if (c.isAssignableFrom(d)) {
/* 1770 */       return type1;
/*      */     }
/* 1772 */     if (d.isAssignableFrom(c)) {
/* 1773 */       return type2;
/*      */     }
/* 1775 */     if (c.isInterface() || d.isInterface()) {
/* 1776 */       return "java/lang/Object";
/*      */     }
/*      */     while (true) {
/* 1779 */       c = c.getSuperclass();
/* 1780 */       if (c.isAssignableFrom(d)) {
/* 1781 */         return c.getName().replace('.', '/');
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected ClassLoader getClassLoader() {
/* 1787 */     ClassLoader classLoader = null;
/*      */     try {
/* 1789 */       classLoader = Thread.currentThread().getContextClassLoader();
/* 1790 */     } catch (Throwable throwable) {}
/*      */ 
/*      */     
/* 1793 */     return (classLoader != null) ? classLoader : getClass().getClassLoader();
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
/*      */   private Item get(Item key) {
/* 1806 */     Item i = this.items[key.hashCode % this.items.length];
/* 1807 */     while (i != null && (i.type != key.type || !key.isEqualTo(i))) {
/* 1808 */       i = i.next;
/*      */     }
/* 1810 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void put(Item i) {
/* 1821 */     if (this.index + this.typeCount > this.threshold) {
/* 1822 */       int ll = this.items.length;
/* 1823 */       int nl = ll * 2 + 1;
/* 1824 */       Item[] newItems = new Item[nl];
/* 1825 */       for (int l = ll - 1; l >= 0; l--) {
/* 1826 */         Item j = this.items[l];
/* 1827 */         while (j != null) {
/* 1828 */           int m = j.hashCode % newItems.length;
/* 1829 */           Item k = j.next;
/* 1830 */           j.next = newItems[m];
/* 1831 */           newItems[m] = j;
/* 1832 */           j = k;
/*      */         } 
/*      */       } 
/* 1835 */       this.items = newItems;
/* 1836 */       this.threshold = (int)(nl * 0.75D);
/*      */     } 
/* 1838 */     int index = i.hashCode % this.items.length;
/* 1839 */     i.next = this.items[index];
/* 1840 */     this.items[index] = i;
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
/*      */   private void put122(int b, int s1, int s2) {
/* 1854 */     this.pool.put12(b, s1).putShort(s2);
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
/*      */   private void put112(int b1, int b2, int s) {
/* 1868 */     this.pool.put11(b1, b2).putShort(s);
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\asm\ClassWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */