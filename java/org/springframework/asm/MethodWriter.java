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
/*      */ class MethodWriter
/*      */   extends MethodVisitor
/*      */ {
/*      */   static final int ACC_CONSTRUCTOR = 524288;
/*      */   static final int SAME_FRAME = 0;
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
/*      */   static final int RESERVED = 128;
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
/*      */   static final int CHOP_FRAME = 248;
/*      */   static final int SAME_FRAME_EXTENDED = 251;
/*      */   static final int APPEND_FRAME = 252;
/*      */   static final int FULL_FRAME = 255;
/*      */   static final int FRAMES = 0;
/*      */   static final int INSERTED_FRAMES = 1;
/*      */   static final int MAXS = 2;
/*      */   static final int NOTHING = 3;
/*      */   final ClassWriter cw;
/*      */   private int access;
/*      */   private final int name;
/*      */   private final int desc;
/*      */   private final String descriptor;
/*      */   String signature;
/*      */   int classReaderOffset;
/*      */   int classReaderLength;
/*      */   int exceptionCount;
/*      */   int[] exceptions;
/*      */   private ByteVector annd;
/*      */   private AnnotationWriter anns;
/*      */   private AnnotationWriter ianns;
/*      */   private AnnotationWriter tanns;
/*      */   private AnnotationWriter itanns;
/*      */   private AnnotationWriter[] panns;
/*      */   private AnnotationWriter[] ipanns;
/*      */   private int synthetics;
/*      */   private Attribute attrs;
/*  243 */   private ByteVector code = new ByteVector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int maxStack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int maxLocals;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int currentLocals;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int frameCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector stackMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int previousFrameOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] previousFrame;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] frame;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handlerCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Handler firstHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Handler lastHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int methodParametersCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector methodParameters;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int localVarCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector localVar;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int localVarTypeCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector localVarType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int lineNumberCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteVector lineNumber;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int lastCodeOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter ctanns;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AnnotationWriter ictanns;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Attribute cattrs;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int subroutines;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int compute;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label labels;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label previousBlock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Label currentBlock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int stackSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int maxStackSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   MethodWriter(ClassWriter cw, int access, String name, String desc, String signature, String[] exceptions, int compute) {
/*  459 */     super(393216);
/*  460 */     if (cw.firstMethod == null) {
/*  461 */       cw.firstMethod = this;
/*      */     } else {
/*  463 */       cw.lastMethod.mv = this;
/*      */     } 
/*  465 */     cw.lastMethod = this;
/*  466 */     this.cw = cw;
/*  467 */     this.access = access;
/*  468 */     if ("<init>".equals(name)) {
/*  469 */       this.access |= 0x80000;
/*      */     }
/*  471 */     this.name = cw.newUTF8(name);
/*  472 */     this.desc = cw.newUTF8(desc);
/*  473 */     this.descriptor = desc;
/*      */     
/*  475 */     this.signature = signature;
/*      */     
/*  477 */     if (exceptions != null && exceptions.length > 0) {
/*  478 */       this.exceptionCount = exceptions.length;
/*  479 */       this.exceptions = new int[this.exceptionCount];
/*  480 */       for (int i = 0; i < this.exceptionCount; i++) {
/*  481 */         this.exceptions[i] = cw.newClass(exceptions[i]);
/*      */       }
/*      */     } 
/*  484 */     this.compute = compute;
/*  485 */     if (compute != 3) {
/*      */       
/*  487 */       int size = Type.getArgumentsAndReturnSizes(this.descriptor) >> 2;
/*  488 */       if ((access & 0x8) != 0) {
/*  489 */         size--;
/*      */       }
/*  491 */       this.maxLocals = size;
/*  492 */       this.currentLocals = size;
/*      */       
/*  494 */       this.labels = new Label();
/*  495 */       this.labels.status |= 0x8;
/*  496 */       visitLabel(this.labels);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitParameter(String name, int access) {
/*  506 */     if (this.methodParameters == null) {
/*  507 */       this.methodParameters = new ByteVector();
/*      */     }
/*  509 */     this.methodParametersCount++;
/*  510 */     this.methodParameters.putShort((name == null) ? 0 : this.cw.newUTF8(name))
/*  511 */       .putShort(access);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitAnnotationDefault() {
/*  519 */     this.annd = new ByteVector();
/*  520 */     return new AnnotationWriter(this.cw, false, this.annd, null, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  529 */     ByteVector bv = new ByteVector();
/*      */     
/*  531 */     bv.putShort(this.cw.newUTF8(desc)).putShort(0);
/*  532 */     AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, 2);
/*  533 */     if (visible) {
/*  534 */       aw.next = this.anns;
/*  535 */       this.anns = aw;
/*      */     } else {
/*  537 */       aw.next = this.ianns;
/*  538 */       this.ianns = aw;
/*      */     } 
/*  540 */     return aw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/*  549 */     ByteVector bv = new ByteVector();
/*      */     
/*  551 */     AnnotationWriter.putTarget(typeRef, typePath, bv);
/*      */     
/*  553 */     bv.putShort(this.cw.newUTF8(desc)).putShort(0);
/*  554 */     AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, bv.length - 2);
/*      */     
/*  556 */     if (visible) {
/*  557 */       aw.next = this.tanns;
/*  558 */       this.tanns = aw;
/*      */     } else {
/*  560 */       aw.next = this.itanns;
/*  561 */       this.itanns = aw;
/*      */     } 
/*  563 */     return aw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
/*  572 */     ByteVector bv = new ByteVector();
/*  573 */     if ("Ljava/lang/Synthetic;".equals(desc)) {
/*      */ 
/*      */       
/*  576 */       this.synthetics = Math.max(this.synthetics, parameter + 1);
/*  577 */       return new AnnotationWriter(this.cw, false, bv, null, 0);
/*      */     } 
/*      */     
/*  580 */     bv.putShort(this.cw.newUTF8(desc)).putShort(0);
/*  581 */     AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, 2);
/*  582 */     if (visible) {
/*  583 */       if (this.panns == null) {
/*  584 */         this.panns = new AnnotationWriter[(Type.getArgumentTypes(this.descriptor)).length];
/*      */       }
/*  586 */       aw.next = this.panns[parameter];
/*  587 */       this.panns[parameter] = aw;
/*      */     } else {
/*  589 */       if (this.ipanns == null) {
/*  590 */         this.ipanns = new AnnotationWriter[(Type.getArgumentTypes(this.descriptor)).length];
/*      */       }
/*  592 */       aw.next = this.ipanns[parameter];
/*  593 */       this.ipanns[parameter] = aw;
/*      */     } 
/*  595 */     return aw;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitAttribute(Attribute attr) {
/*  600 */     if (attr.isCodeAttribute()) {
/*  601 */       attr.next = this.cattrs;
/*  602 */       this.cattrs = attr;
/*      */     } else {
/*  604 */       attr.next = this.attrs;
/*  605 */       this.attrs = attr;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitCode() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
/*  616 */     if (this.compute == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  620 */     if (this.compute == 1) {
/*  621 */       if (this.currentBlock.frame == null) {
/*      */ 
/*      */ 
/*      */         
/*  625 */         this.currentBlock.frame = new CurrentFrame();
/*  626 */         this.currentBlock.frame.owner = this.currentBlock;
/*  627 */         this.currentBlock.frame.initInputFrame(this.cw, this.access, 
/*  628 */             Type.getArgumentTypes(this.descriptor), nLocal);
/*  629 */         visitImplicitFirstFrame();
/*      */       } else {
/*  631 */         if (type == -1) {
/*  632 */           this.currentBlock.frame.set(this.cw, nLocal, local, nStack, stack);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  640 */         visitFrame(this.currentBlock.frame);
/*      */       } 
/*  642 */     } else if (type == -1) {
/*  643 */       if (this.previousFrame == null) {
/*  644 */         visitImplicitFirstFrame();
/*      */       }
/*  646 */       this.currentLocals = nLocal;
/*  647 */       int frameIndex = startFrame(this.code.length, nLocal, nStack); int i;
/*  648 */       for (i = 0; i < nLocal; i++) {
/*  649 */         if (local[i] instanceof String) {
/*  650 */           this.frame[frameIndex++] = 0x1700000 | this.cw
/*  651 */             .addType((String)local[i]);
/*  652 */         } else if (local[i] instanceof Integer) {
/*  653 */           this.frame[frameIndex++] = ((Integer)local[i]).intValue();
/*      */         } else {
/*  655 */           this.frame[frameIndex++] = 0x1800000 | this.cw
/*  656 */             .addUninitializedType("", ((Label)local[i]).position);
/*      */         } 
/*      */       } 
/*      */       
/*  660 */       for (i = 0; i < nStack; i++) {
/*  661 */         if (stack[i] instanceof String) {
/*  662 */           this.frame[frameIndex++] = 0x1700000 | this.cw
/*  663 */             .addType((String)stack[i]);
/*  664 */         } else if (stack[i] instanceof Integer) {
/*  665 */           this.frame[frameIndex++] = ((Integer)stack[i]).intValue();
/*      */         } else {
/*  667 */           this.frame[frameIndex++] = 0x1800000 | this.cw
/*  668 */             .addUninitializedType("", ((Label)stack[i]).position);
/*      */         } 
/*      */       } 
/*      */       
/*  672 */       endFrame();
/*      */     } else {
/*      */       int delta, i;
/*  675 */       if (this.stackMap == null) {
/*  676 */         this.stackMap = new ByteVector();
/*  677 */         delta = this.code.length;
/*      */       } else {
/*  679 */         delta = this.code.length - this.previousFrameOffset - 1;
/*  680 */         if (delta < 0) {
/*  681 */           if (type == 3) {
/*      */             return;
/*      */           }
/*  684 */           throw new IllegalStateException();
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  689 */       switch (type) {
/*      */         case 0:
/*  691 */           this.currentLocals = nLocal;
/*  692 */           this.stackMap.putByte(255).putShort(delta).putShort(nLocal);
/*  693 */           for (i = 0; i < nLocal; i++) {
/*  694 */             writeFrameType(local[i]);
/*      */           }
/*  696 */           this.stackMap.putShort(nStack);
/*  697 */           for (i = 0; i < nStack; i++) {
/*  698 */             writeFrameType(stack[i]);
/*      */           }
/*      */           break;
/*      */         case 1:
/*  702 */           this.currentLocals += nLocal;
/*  703 */           this.stackMap.putByte(251 + nLocal).putShort(delta);
/*  704 */           for (i = 0; i < nLocal; i++) {
/*  705 */             writeFrameType(local[i]);
/*      */           }
/*      */           break;
/*      */         case 2:
/*  709 */           this.currentLocals -= nLocal;
/*  710 */           this.stackMap.putByte(251 - nLocal).putShort(delta);
/*      */           break;
/*      */         case 3:
/*  713 */           if (delta < 64) {
/*  714 */             this.stackMap.putByte(delta); break;
/*      */           } 
/*  716 */           this.stackMap.putByte(251).putShort(delta);
/*      */           break;
/*      */         
/*      */         case 4:
/*  720 */           if (delta < 64) {
/*  721 */             this.stackMap.putByte(64 + delta);
/*      */           } else {
/*  723 */             this.stackMap.putByte(247)
/*  724 */               .putShort(delta);
/*      */           } 
/*  726 */           writeFrameType(stack[0]);
/*      */           break;
/*      */       } 
/*      */       
/*  730 */       this.previousFrameOffset = this.code.length;
/*  731 */       this.frameCount++;
/*      */     } 
/*      */     
/*  734 */     this.maxStack = Math.max(this.maxStack, nStack);
/*  735 */     this.maxLocals = Math.max(this.maxLocals, this.currentLocals);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitInsn(int opcode) {
/*  740 */     this.lastCodeOffset = this.code.length;
/*      */     
/*  742 */     this.code.putByte(opcode);
/*      */ 
/*      */     
/*  745 */     if (this.currentBlock != null) {
/*  746 */       if (this.compute == 0 || this.compute == 1) {
/*  747 */         this.currentBlock.frame.execute(opcode, 0, null, null);
/*      */       } else {
/*      */         
/*  750 */         int size = this.stackSize + Frame.SIZE[opcode];
/*  751 */         if (size > this.maxStackSize) {
/*  752 */           this.maxStackSize = size;
/*      */         }
/*  754 */         this.stackSize = size;
/*      */       } 
/*      */       
/*  757 */       if ((opcode >= 172 && opcode <= 177) || opcode == 191)
/*      */       {
/*  759 */         noSuccessor();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIntInsn(int opcode, int operand) {
/*  766 */     this.lastCodeOffset = this.code.length;
/*      */     
/*  768 */     if (this.currentBlock != null) {
/*  769 */       if (this.compute == 0 || this.compute == 1) {
/*  770 */         this.currentBlock.frame.execute(opcode, operand, null, null);
/*  771 */       } else if (opcode != 188) {
/*      */ 
/*      */         
/*  774 */         int size = this.stackSize + 1;
/*  775 */         if (size > this.maxStackSize) {
/*  776 */           this.maxStackSize = size;
/*      */         }
/*  778 */         this.stackSize = size;
/*      */       } 
/*      */     }
/*      */     
/*  782 */     if (opcode == 17) {
/*  783 */       this.code.put12(opcode, operand);
/*      */     } else {
/*  785 */       this.code.put11(opcode, operand);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitVarInsn(int opcode, int var) {
/*  791 */     this.lastCodeOffset = this.code.length;
/*      */     
/*  793 */     if (this.currentBlock != null) {
/*  794 */       if (this.compute == 0 || this.compute == 1) {
/*  795 */         this.currentBlock.frame.execute(opcode, var, null, null);
/*      */       
/*      */       }
/*  798 */       else if (opcode == 169) {
/*      */         
/*  800 */         this.currentBlock.status |= 0x100;
/*      */ 
/*      */         
/*  803 */         this.currentBlock.inputStackTop = this.stackSize;
/*  804 */         noSuccessor();
/*      */       } else {
/*  806 */         int size = this.stackSize + Frame.SIZE[opcode];
/*  807 */         if (size > this.maxStackSize) {
/*  808 */           this.maxStackSize = size;
/*      */         }
/*  810 */         this.stackSize = size;
/*      */       } 
/*      */     }
/*      */     
/*  814 */     if (this.compute != 3) {
/*      */       int n;
/*      */       
/*  817 */       if (opcode == 22 || opcode == 24 || opcode == 55 || opcode == 57) {
/*      */         
/*  819 */         n = var + 2;
/*      */       } else {
/*  821 */         n = var + 1;
/*      */       } 
/*  823 */       if (n > this.maxLocals) {
/*  824 */         this.maxLocals = n;
/*      */       }
/*      */     } 
/*      */     
/*  828 */     if (var < 4 && opcode != 169) {
/*      */       int opt;
/*  830 */       if (opcode < 54) {
/*      */         
/*  832 */         opt = 26 + (opcode - 21 << 2) + var;
/*      */       } else {
/*      */         
/*  835 */         opt = 59 + (opcode - 54 << 2) + var;
/*      */       } 
/*  837 */       this.code.putByte(opt);
/*  838 */     } else if (var >= 256) {
/*  839 */       this.code.putByte(196).put12(opcode, var);
/*      */     } else {
/*  841 */       this.code.put11(opcode, var);
/*      */     } 
/*  843 */     if (opcode >= 54 && this.compute == 0 && this.handlerCount > 0) {
/*  844 */       visitLabel(new Label());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitTypeInsn(int opcode, String type) {
/*  850 */     this.lastCodeOffset = this.code.length;
/*  851 */     Item i = this.cw.newStringishItem(7, type);
/*      */     
/*  853 */     if (this.currentBlock != null) {
/*  854 */       if (this.compute == 0 || this.compute == 1) {
/*  855 */         this.currentBlock.frame.execute(opcode, this.code.length, this.cw, i);
/*  856 */       } else if (opcode == 187) {
/*      */ 
/*      */         
/*  859 */         int size = this.stackSize + 1;
/*  860 */         if (size > this.maxStackSize) {
/*  861 */           this.maxStackSize = size;
/*      */         }
/*  863 */         this.stackSize = size;
/*      */       } 
/*      */     }
/*      */     
/*  867 */     this.code.put12(opcode, i.index);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/*  873 */     this.lastCodeOffset = this.code.length;
/*  874 */     Item i = this.cw.newFieldItem(owner, name, desc);
/*      */     
/*  876 */     if (this.currentBlock != null) {
/*  877 */       if (this.compute == 0 || this.compute == 1) {
/*  878 */         this.currentBlock.frame.execute(opcode, 0, this.cw, i);
/*      */       } else {
/*      */         int size;
/*      */         
/*  882 */         char c = desc.charAt(0);
/*  883 */         switch (opcode) {
/*      */           case 178:
/*  885 */             size = this.stackSize + ((c == 'D' || c == 'J') ? 2 : 1);
/*      */             break;
/*      */           case 179:
/*  888 */             size = this.stackSize + ((c == 'D' || c == 'J') ? -2 : -1);
/*      */             break;
/*      */           case 180:
/*  891 */             size = this.stackSize + ((c == 'D' || c == 'J') ? 1 : 0);
/*      */             break;
/*      */           
/*      */           default:
/*  895 */             size = this.stackSize + ((c == 'D' || c == 'J') ? -3 : -2);
/*      */             break;
/*      */         } 
/*      */         
/*  899 */         if (size > this.maxStackSize) {
/*  900 */           this.maxStackSize = size;
/*      */         }
/*  902 */         this.stackSize = size;
/*      */       } 
/*      */     }
/*      */     
/*  906 */     this.code.put12(opcode, i.index);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
/*  912 */     this.lastCodeOffset = this.code.length;
/*  913 */     Item i = this.cw.newMethodItem(owner, name, desc, itf);
/*  914 */     int argSize = i.intVal;
/*      */     
/*  916 */     if (this.currentBlock != null) {
/*  917 */       if (this.compute == 0 || this.compute == 1) {
/*  918 */         this.currentBlock.frame.execute(opcode, 0, this.cw, i);
/*      */       } else {
/*      */         int size;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  928 */         if (argSize == 0) {
/*      */ 
/*      */           
/*  931 */           argSize = Type.getArgumentsAndReturnSizes(desc);
/*      */ 
/*      */           
/*  934 */           i.intVal = argSize;
/*      */         } 
/*      */         
/*  937 */         if (opcode == 184) {
/*  938 */           size = this.stackSize - (argSize >> 2) + (argSize & 0x3) + 1;
/*      */         } else {
/*  940 */           size = this.stackSize - (argSize >> 2) + (argSize & 0x3);
/*      */         } 
/*      */         
/*  943 */         if (size > this.maxStackSize) {
/*  944 */           this.maxStackSize = size;
/*      */         }
/*  946 */         this.stackSize = size;
/*      */       } 
/*      */     }
/*      */     
/*  950 */     if (opcode == 185) {
/*  951 */       if (argSize == 0) {
/*  952 */         argSize = Type.getArgumentsAndReturnSizes(desc);
/*  953 */         i.intVal = argSize;
/*      */       } 
/*  955 */       this.code.put12(185, i.index).put11(argSize >> 2, 0);
/*      */     } else {
/*  957 */       this.code.put12(opcode, i.index);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
/*  964 */     this.lastCodeOffset = this.code.length;
/*  965 */     Item i = this.cw.newInvokeDynamicItem(name, desc, bsm, bsmArgs);
/*  966 */     int argSize = i.intVal;
/*      */     
/*  968 */     if (this.currentBlock != null) {
/*  969 */       if (this.compute == 0 || this.compute == 1) {
/*  970 */         this.currentBlock.frame.execute(186, 0, this.cw, i);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  980 */         if (argSize == 0) {
/*      */ 
/*      */           
/*  983 */           argSize = Type.getArgumentsAndReturnSizes(desc);
/*      */ 
/*      */           
/*  986 */           i.intVal = argSize;
/*      */         } 
/*  988 */         int size = this.stackSize - (argSize >> 2) + (argSize & 0x3) + 1;
/*      */ 
/*      */         
/*  991 */         if (size > this.maxStackSize) {
/*  992 */           this.maxStackSize = size;
/*      */         }
/*  994 */         this.stackSize = size;
/*      */       } 
/*      */     }
/*      */     
/*  998 */     this.code.put12(186, i.index);
/*  999 */     this.code.putShort(0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitJumpInsn(int opcode, Label label) {
/* 1004 */     boolean isWide = (opcode >= 200);
/* 1005 */     opcode = isWide ? (opcode - 33) : opcode;
/* 1006 */     this.lastCodeOffset = this.code.length;
/* 1007 */     Label nextInsn = null;
/*      */     
/* 1009 */     if (this.currentBlock != null) {
/* 1010 */       if (this.compute == 0) {
/* 1011 */         this.currentBlock.frame.execute(opcode, 0, null, null);
/*      */         
/* 1013 */         (label.getFirst()).status |= 0x10;
/*      */         
/* 1015 */         addSuccessor(0, label);
/* 1016 */         if (opcode != 167)
/*      */         {
/* 1018 */           nextInsn = new Label();
/*      */         }
/* 1020 */       } else if (this.compute == 1) {
/* 1021 */         this.currentBlock.frame.execute(opcode, 0, null, null);
/*      */       }
/* 1023 */       else if (opcode == 168) {
/* 1024 */         if ((label.status & 0x200) == 0) {
/* 1025 */           label.status |= 0x200;
/* 1026 */           this.subroutines++;
/*      */         } 
/* 1028 */         this.currentBlock.status |= 0x80;
/* 1029 */         addSuccessor(this.stackSize + 1, label);
/*      */         
/* 1031 */         nextInsn = new Label();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1042 */         this.stackSize += Frame.SIZE[opcode];
/* 1043 */         addSuccessor(this.stackSize, label);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1048 */     if ((label.status & 0x2) != 0 && label.position - this.code.length < -32768) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1057 */       if (opcode == 167) {
/* 1058 */         this.code.putByte(200);
/* 1059 */       } else if (opcode == 168) {
/* 1060 */         this.code.putByte(201);
/*      */       }
/*      */       else {
/*      */         
/* 1064 */         if (nextInsn != null) {
/* 1065 */           nextInsn.status |= 0x10;
/*      */         }
/* 1067 */         this.code.putByte((opcode <= 166) ? ((opcode + 1 ^ 0x1) - 1) : (opcode ^ 0x1));
/*      */         
/* 1069 */         this.code.putShort(8);
/*      */ 
/*      */ 
/*      */         
/* 1073 */         this.code.putByte(220);
/* 1074 */         this.cw.hasAsmInsns = true;
/*      */       } 
/* 1076 */       label.put(this, this.code, this.code.length - 1, true);
/* 1077 */     } else if (isWide) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1083 */       this.code.putByte(opcode + 33);
/* 1084 */       label.put(this, this.code, this.code.length - 1, true);
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/* 1092 */       this.code.putByte(opcode);
/* 1093 */       label.put(this, this.code, this.code.length - 1, false);
/*      */     } 
/* 1095 */     if (this.currentBlock != null) {
/* 1096 */       if (nextInsn != null)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1101 */         visitLabel(nextInsn);
/*      */       }
/* 1103 */       if (opcode == 167) {
/* 1104 */         noSuccessor();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLabel(Label label) {
/* 1112 */     this.cw.hasAsmInsns |= label.resolve(this, this.code.length, this.code.data);
/*      */     
/* 1114 */     if ((label.status & 0x1) != 0) {
/*      */       return;
/*      */     }
/* 1117 */     if (this.compute == 0) {
/* 1118 */       if (this.currentBlock != null) {
/* 1119 */         if (label.position == this.currentBlock.position) {
/*      */           
/* 1121 */           this.currentBlock.status |= label.status & 0x10;
/* 1122 */           label.frame = this.currentBlock.frame;
/*      */           
/*      */           return;
/*      */         } 
/* 1126 */         addSuccessor(0, label);
/*      */       } 
/*      */       
/* 1129 */       this.currentBlock = label;
/* 1130 */       if (label.frame == null) {
/* 1131 */         label.frame = new Frame();
/* 1132 */         label.frame.owner = label;
/*      */       } 
/*      */       
/* 1135 */       if (this.previousBlock != null) {
/* 1136 */         if (label.position == this.previousBlock.position) {
/* 1137 */           this.previousBlock.status |= label.status & 0x10;
/* 1138 */           label.frame = this.previousBlock.frame;
/* 1139 */           this.currentBlock = this.previousBlock;
/*      */           return;
/*      */         } 
/* 1142 */         this.previousBlock.successor = label;
/*      */       } 
/* 1144 */       this.previousBlock = label;
/* 1145 */     } else if (this.compute == 1) {
/* 1146 */       if (this.currentBlock == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1151 */         this.currentBlock = label;
/*      */       }
/*      */       else {
/*      */         
/* 1155 */         this.currentBlock.frame.owner = label;
/*      */       } 
/* 1157 */     } else if (this.compute == 2) {
/* 1158 */       if (this.currentBlock != null) {
/*      */         
/* 1160 */         this.currentBlock.outputStackMax = this.maxStackSize;
/* 1161 */         addSuccessor(this.stackSize, label);
/*      */       } 
/*      */       
/* 1164 */       this.currentBlock = label;
/*      */       
/* 1166 */       this.stackSize = 0;
/* 1167 */       this.maxStackSize = 0;
/*      */       
/* 1169 */       if (this.previousBlock != null) {
/* 1170 */         this.previousBlock.successor = label;
/*      */       }
/* 1172 */       this.previousBlock = label;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLdcInsn(Object cst) {
/* 1178 */     this.lastCodeOffset = this.code.length;
/* 1179 */     Item i = this.cw.newConstItem(cst);
/*      */     
/* 1181 */     if (this.currentBlock != null) {
/* 1182 */       if (this.compute == 0 || this.compute == 1) {
/* 1183 */         this.currentBlock.frame.execute(18, 0, this.cw, i);
/*      */       } else {
/*      */         int size;
/*      */         
/* 1187 */         if (i.type == 5 || i.type == 6) {
/* 1188 */           size = this.stackSize + 2;
/*      */         } else {
/* 1190 */           size = this.stackSize + 1;
/*      */         } 
/*      */         
/* 1193 */         if (size > this.maxStackSize) {
/* 1194 */           this.maxStackSize = size;
/*      */         }
/* 1196 */         this.stackSize = size;
/*      */       } 
/*      */     }
/*      */     
/* 1200 */     int index = i.index;
/* 1201 */     if (i.type == 5 || i.type == 6) {
/* 1202 */       this.code.put12(20, index);
/* 1203 */     } else if (index >= 256) {
/* 1204 */       this.code.put12(19, index);
/*      */     } else {
/* 1206 */       this.code.put11(18, index);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitIincInsn(int var, int increment) {
/* 1212 */     this.lastCodeOffset = this.code.length;
/* 1213 */     if (this.currentBlock != null && (
/* 1214 */       this.compute == 0 || this.compute == 1)) {
/* 1215 */       this.currentBlock.frame.execute(132, var, null, null);
/*      */     }
/*      */     
/* 1218 */     if (this.compute != 3) {
/*      */       
/* 1220 */       int n = var + 1;
/* 1221 */       if (n > this.maxLocals) {
/* 1222 */         this.maxLocals = n;
/*      */       }
/*      */     } 
/*      */     
/* 1226 */     if (var > 255 || increment > 127 || increment < -128) {
/* 1227 */       this.code.putByte(196).put12(132, var)
/* 1228 */         .putShort(increment);
/*      */     } else {
/* 1230 */       this.code.putByte(132).put11(var, increment);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
/* 1237 */     this.lastCodeOffset = this.code.length;
/*      */     
/* 1239 */     int source = this.code.length;
/* 1240 */     this.code.putByte(170);
/* 1241 */     this.code.putByteArray(null, 0, (4 - this.code.length % 4) % 4);
/* 1242 */     dflt.put(this, this.code, source, true);
/* 1243 */     this.code.putInt(min).putInt(max);
/* 1244 */     for (int i = 0; i < labels.length; i++) {
/* 1245 */       labels[i].put(this, this.code, source, true);
/*      */     }
/*      */     
/* 1248 */     visitSwitchInsn(dflt, labels);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 1254 */     this.lastCodeOffset = this.code.length;
/*      */     
/* 1256 */     int source = this.code.length;
/* 1257 */     this.code.putByte(171);
/* 1258 */     this.code.putByteArray(null, 0, (4 - this.code.length % 4) % 4);
/* 1259 */     dflt.put(this, this.code, source, true);
/* 1260 */     this.code.putInt(labels.length);
/* 1261 */     for (int i = 0; i < labels.length; i++) {
/* 1262 */       this.code.putInt(keys[i]);
/* 1263 */       labels[i].put(this, this.code, source, true);
/*      */     } 
/*      */     
/* 1266 */     visitSwitchInsn(dflt, labels);
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitSwitchInsn(Label dflt, Label[] labels) {
/* 1271 */     if (this.currentBlock != null) {
/* 1272 */       if (this.compute == 0) {
/* 1273 */         this.currentBlock.frame.execute(171, 0, null, null);
/*      */         
/* 1275 */         addSuccessor(0, dflt);
/* 1276 */         (dflt.getFirst()).status |= 0x10;
/* 1277 */         for (int i = 0; i < labels.length; i++) {
/* 1278 */           addSuccessor(0, labels[i]);
/* 1279 */           (labels[i].getFirst()).status |= 0x10;
/*      */         } 
/*      */       } else {
/*      */         
/* 1283 */         this.stackSize--;
/*      */         
/* 1285 */         addSuccessor(this.stackSize, dflt);
/* 1286 */         for (int i = 0; i < labels.length; i++) {
/* 1287 */           addSuccessor(this.stackSize, labels[i]);
/*      */         }
/*      */       } 
/*      */       
/* 1291 */       noSuccessor();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMultiANewArrayInsn(String desc, int dims) {
/* 1297 */     this.lastCodeOffset = this.code.length;
/* 1298 */     Item i = this.cw.newStringishItem(7, desc);
/*      */     
/* 1300 */     if (this.currentBlock != null) {
/* 1301 */       if (this.compute == 0 || this.compute == 1) {
/* 1302 */         this.currentBlock.frame.execute(197, dims, this.cw, i);
/*      */       }
/*      */       else {
/*      */         
/* 1306 */         this.stackSize += 1 - dims;
/*      */       } 
/*      */     }
/*      */     
/* 1310 */     this.code.put12(197, i.index).putByte(dims);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 1319 */     ByteVector bv = new ByteVector();
/*      */     
/* 1321 */     typeRef = typeRef & 0xFF0000FF | this.lastCodeOffset << 8;
/* 1322 */     AnnotationWriter.putTarget(typeRef, typePath, bv);
/*      */     
/* 1324 */     bv.putShort(this.cw.newUTF8(desc)).putShort(0);
/* 1325 */     AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, bv.length - 2);
/*      */     
/* 1327 */     if (visible) {
/* 1328 */       aw.next = this.ctanns;
/* 1329 */       this.ctanns = aw;
/*      */     } else {
/* 1331 */       aw.next = this.ictanns;
/* 1332 */       this.ictanns = aw;
/*      */     } 
/* 1334 */     return aw;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/* 1340 */     this.handlerCount++;
/* 1341 */     Handler h = new Handler();
/* 1342 */     h.start = start;
/* 1343 */     h.end = end;
/* 1344 */     h.handler = handler;
/* 1345 */     h.desc = type;
/* 1346 */     h.type = (type != null) ? this.cw.newClass(type) : 0;
/* 1347 */     if (this.lastHandler == null) {
/* 1348 */       this.firstHandler = h;
/*      */     } else {
/* 1350 */       this.lastHandler.next = h;
/*      */     } 
/* 1352 */     this.lastHandler = h;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 1361 */     ByteVector bv = new ByteVector();
/*      */     
/* 1363 */     AnnotationWriter.putTarget(typeRef, typePath, bv);
/*      */     
/* 1365 */     bv.putShort(this.cw.newUTF8(desc)).putShort(0);
/* 1366 */     AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, bv.length - 2);
/*      */     
/* 1368 */     if (visible) {
/* 1369 */       aw.next = this.ctanns;
/* 1370 */       this.ctanns = aw;
/*      */     } else {
/* 1372 */       aw.next = this.ictanns;
/* 1373 */       this.ictanns = aw;
/*      */     } 
/* 1375 */     return aw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
/* 1382 */     if (signature != null) {
/* 1383 */       if (this.localVarType == null) {
/* 1384 */         this.localVarType = new ByteVector();
/*      */       }
/* 1386 */       this.localVarTypeCount++;
/* 1387 */       this.localVarType.putShort(start.position)
/* 1388 */         .putShort(end.position - start.position)
/* 1389 */         .putShort(this.cw.newUTF8(name)).putShort(this.cw.newUTF8(signature))
/* 1390 */         .putShort(index);
/*      */     } 
/* 1392 */     if (this.localVar == null) {
/* 1393 */       this.localVar = new ByteVector();
/*      */     }
/* 1395 */     this.localVarCount++;
/* 1396 */     this.localVar.putShort(start.position)
/* 1397 */       .putShort(end.position - start.position)
/* 1398 */       .putShort(this.cw.newUTF8(name)).putShort(this.cw.newUTF8(desc))
/* 1399 */       .putShort(index);
/* 1400 */     if (this.compute != 3) {
/*      */       
/* 1402 */       char c = desc.charAt(0);
/* 1403 */       int n = index + ((c == 'J' || c == 'D') ? 2 : 1);
/* 1404 */       if (n > this.maxLocals) {
/* 1405 */         this.maxLocals = n;
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
/*      */   public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible) {
/* 1417 */     ByteVector bv = new ByteVector();
/*      */     
/* 1419 */     bv.putByte(typeRef >>> 24).putShort(start.length);
/* 1420 */     for (int i = 0; i < start.length; i++) {
/* 1421 */       bv.putShort((start[i]).position)
/* 1422 */         .putShort((end[i]).position - (start[i]).position)
/* 1423 */         .putShort(index[i]);
/*      */     }
/* 1425 */     if (typePath == null) {
/* 1426 */       bv.putByte(0);
/*      */     } else {
/* 1428 */       int length = typePath.b[typePath.offset] * 2 + 1;
/* 1429 */       bv.putByteArray(typePath.b, typePath.offset, length);
/*      */     } 
/*      */     
/* 1432 */     bv.putShort(this.cw.newUTF8(desc)).putShort(0);
/* 1433 */     AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, bv.length - 2);
/*      */     
/* 1435 */     if (visible) {
/* 1436 */       aw.next = this.ctanns;
/* 1437 */       this.ctanns = aw;
/*      */     } else {
/* 1439 */       aw.next = this.ictanns;
/* 1440 */       this.ictanns = aw;
/*      */     } 
/* 1442 */     return aw;
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitLineNumber(int line, Label start) {
/* 1447 */     if (this.lineNumber == null) {
/* 1448 */       this.lineNumber = new ByteVector();
/*      */     }
/* 1450 */     this.lineNumberCount++;
/* 1451 */     this.lineNumber.putShort(start.position);
/* 1452 */     this.lineNumber.putShort(line);
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMaxs(int maxStack, int maxLocals) {
/* 1457 */     if (this.compute == 0) {
/*      */       
/* 1459 */       Handler handler = this.firstHandler;
/* 1460 */       while (handler != null) {
/* 1461 */         Label label1 = handler.start.getFirst();
/* 1462 */         Label h = handler.handler.getFirst();
/* 1463 */         Label e = handler.end.getFirst();
/*      */         
/* 1465 */         String t = (handler.desc == null) ? "java/lang/Throwable" : handler.desc;
/*      */         
/* 1467 */         int kind = 0x1700000 | this.cw.addType(t);
/*      */         
/* 1469 */         h.status |= 0x10;
/*      */         
/* 1471 */         while (label1 != e) {
/*      */           
/* 1473 */           Edge b = new Edge();
/* 1474 */           b.info = kind;
/* 1475 */           b.successor = h;
/*      */           
/* 1477 */           b.next = label1.successors;
/* 1478 */           label1.successors = b;
/*      */           
/* 1480 */           label1 = label1.successor;
/*      */         } 
/* 1482 */         handler = handler.next;
/*      */       } 
/*      */ 
/*      */       
/* 1486 */       Frame f = this.labels.frame;
/* 1487 */       f.initInputFrame(this.cw, this.access, Type.getArgumentTypes(this.descriptor), this.maxLocals);
/*      */       
/* 1489 */       visitFrame(f);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1497 */       int max = 0;
/* 1498 */       Label changed = this.labels;
/* 1499 */       while (changed != null) {
/*      */         
/* 1501 */         Label label = changed;
/* 1502 */         changed = changed.next;
/* 1503 */         label.next = null;
/* 1504 */         f = label.frame;
/*      */         
/* 1506 */         if ((label.status & 0x10) != 0) {
/* 1507 */           label.status |= 0x20;
/*      */         }
/*      */         
/* 1510 */         label.status |= 0x40;
/*      */         
/* 1512 */         int blockMax = f.inputStack.length + label.outputStackMax;
/* 1513 */         if (blockMax > max) {
/* 1514 */           max = blockMax;
/*      */         }
/*      */         
/* 1517 */         Edge e = label.successors;
/* 1518 */         while (e != null) {
/* 1519 */           Label n = e.successor.getFirst();
/* 1520 */           boolean change = f.merge(this.cw, n.frame, e.info);
/* 1521 */           if (change && n.next == null) {
/*      */ 
/*      */             
/* 1524 */             n.next = changed;
/* 1525 */             changed = n;
/*      */           } 
/* 1527 */           e = e.next;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1532 */       Label l = this.labels;
/* 1533 */       while (l != null) {
/* 1534 */         f = l.frame;
/* 1535 */         if ((l.status & 0x20) != 0) {
/* 1536 */           visitFrame(f);
/*      */         }
/* 1538 */         if ((l.status & 0x40) == 0) {
/*      */           
/* 1540 */           Label k = l.successor;
/* 1541 */           int start = l.position;
/* 1542 */           int end = ((k == null) ? this.code.length : k.position) - 1;
/*      */           
/* 1544 */           if (end >= start) {
/* 1545 */             max = Math.max(max, 1);
/*      */             
/* 1547 */             for (int i = start; i < end; i++) {
/* 1548 */               this.code.data[i] = 0;
/*      */             }
/* 1550 */             this.code.data[end] = -65;
/*      */             
/* 1552 */             int frameIndex = startFrame(start, 0, 1);
/* 1553 */             this.frame[frameIndex] = 0x1700000 | this.cw
/* 1554 */               .addType("java/lang/Throwable");
/* 1555 */             endFrame();
/*      */ 
/*      */             
/* 1558 */             this.firstHandler = Handler.remove(this.firstHandler, l, k);
/*      */           } 
/*      */         } 
/* 1561 */         l = l.successor;
/*      */       } 
/*      */       
/* 1564 */       handler = this.firstHandler;
/* 1565 */       this.handlerCount = 0;
/* 1566 */       while (handler != null) {
/* 1567 */         this.handlerCount++;
/* 1568 */         handler = handler.next;
/*      */       } 
/*      */       
/* 1571 */       this.maxStack = max;
/* 1572 */     } else if (this.compute == 2) {
/*      */       
/* 1574 */       Handler handler = this.firstHandler;
/* 1575 */       while (handler != null) {
/* 1576 */         Label l = handler.start;
/* 1577 */         Label h = handler.handler;
/* 1578 */         Label e = handler.end;
/*      */         
/* 1580 */         while (l != e) {
/*      */           
/* 1582 */           Edge b = new Edge();
/* 1583 */           b.info = Integer.MAX_VALUE;
/* 1584 */           b.successor = h;
/*      */           
/* 1586 */           if ((l.status & 0x80) == 0) {
/* 1587 */             b.next = l.successors;
/* 1588 */             l.successors = b;
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 1593 */             b.next = l.successors.next.next;
/* 1594 */             l.successors.next.next = b;
/*      */           } 
/*      */           
/* 1597 */           l = l.successor;
/*      */         } 
/* 1599 */         handler = handler.next;
/*      */       } 
/*      */       
/* 1602 */       if (this.subroutines > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1609 */         int id = 0;
/* 1610 */         this.labels.visitSubroutine(null, 1L, this.subroutines);
/*      */         
/* 1612 */         Label l = this.labels;
/* 1613 */         while (l != null) {
/* 1614 */           if ((l.status & 0x80) != 0) {
/*      */             
/* 1616 */             Label subroutine = l.successors.next.successor;
/*      */             
/* 1618 */             if ((subroutine.status & 0x400) == 0) {
/*      */               
/* 1620 */               id++;
/* 1621 */               subroutine.visitSubroutine(null, id / 32L << 32L | 1L << id % 32, this.subroutines);
/*      */             } 
/*      */           } 
/*      */           
/* 1625 */           l = l.successor;
/*      */         } 
/*      */         
/* 1628 */         l = this.labels;
/* 1629 */         while (l != null) {
/* 1630 */           if ((l.status & 0x80) != 0) {
/* 1631 */             Label L = this.labels;
/* 1632 */             while (L != null) {
/* 1633 */               L.status &= 0xFFFFF7FF;
/* 1634 */               L = L.successor;
/*      */             } 
/*      */             
/* 1637 */             Label subroutine = l.successors.next.successor;
/* 1638 */             subroutine.visitSubroutine(l, 0L, this.subroutines);
/*      */           } 
/* 1640 */           l = l.successor;
/*      */         } 
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
/* 1654 */       int max = 0;
/* 1655 */       Label stack = this.labels;
/* 1656 */       while (stack != null) {
/*      */         
/* 1658 */         Label l = stack;
/* 1659 */         stack = stack.next;
/*      */         
/* 1661 */         int start = l.inputStackTop;
/* 1662 */         int blockMax = start + l.outputStackMax;
/*      */         
/* 1664 */         if (blockMax > max) {
/* 1665 */           max = blockMax;
/*      */         }
/*      */         
/* 1668 */         Edge b = l.successors;
/* 1669 */         if ((l.status & 0x80) != 0)
/*      */         {
/* 1671 */           b = b.next;
/*      */         }
/* 1673 */         while (b != null) {
/* 1674 */           l = b.successor;
/*      */           
/* 1676 */           if ((l.status & 0x8) == 0) {
/*      */             
/* 1678 */             l.inputStackTop = (b.info == Integer.MAX_VALUE) ? 1 : (start + b.info);
/*      */ 
/*      */             
/* 1681 */             l.status |= 0x8;
/* 1682 */             l.next = stack;
/* 1683 */             stack = l;
/*      */           } 
/* 1685 */           b = b.next;
/*      */         } 
/*      */       } 
/* 1688 */       this.maxStack = Math.max(maxStack, max);
/*      */     } else {
/* 1690 */       this.maxStack = maxStack;
/* 1691 */       this.maxLocals = maxLocals;
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
/*      */   public void visitEnd() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addSuccessor(int info, Label successor) {
/* 1713 */     Edge b = new Edge();
/* 1714 */     b.info = info;
/* 1715 */     b.successor = successor;
/*      */     
/* 1717 */     b.next = this.currentBlock.successors;
/* 1718 */     this.currentBlock.successors = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void noSuccessor() {
/* 1726 */     if (this.compute == 0) {
/* 1727 */       Label l = new Label();
/* 1728 */       l.frame = new Frame();
/* 1729 */       l.frame.owner = l;
/* 1730 */       l.resolve(this, this.code.length, this.code.data);
/* 1731 */       this.previousBlock.successor = l;
/* 1732 */       this.previousBlock = l;
/*      */     } else {
/* 1734 */       this.currentBlock.outputStackMax = this.maxStackSize;
/*      */     } 
/* 1736 */     if (this.compute != 1) {
/* 1737 */       this.currentBlock = null;
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
/*      */   private void visitFrame(Frame f) {
/* 1753 */     int nTop = 0;
/* 1754 */     int nLocal = 0;
/* 1755 */     int nStack = 0;
/* 1756 */     int[] locals = f.inputLocals;
/* 1757 */     int[] stacks = f.inputStack;
/*      */     
/*      */     int i;
/* 1760 */     for (i = 0; i < locals.length; i++) {
/* 1761 */       int t = locals[i];
/* 1762 */       if (t == 16777216) {
/* 1763 */         nTop++;
/*      */       } else {
/* 1765 */         nLocal += nTop + 1;
/* 1766 */         nTop = 0;
/*      */       } 
/* 1768 */       if (t == 16777220 || t == 16777219) {
/* 1769 */         i++;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1774 */     for (i = 0; i < stacks.length; i++) {
/* 1775 */       int t = stacks[i];
/* 1776 */       nStack++;
/* 1777 */       if (t == 16777220 || t == 16777219) {
/* 1778 */         i++;
/*      */       }
/*      */     } 
/*      */     
/* 1782 */     int frameIndex = startFrame(f.owner.position, nLocal, nStack);
/* 1783 */     for (i = 0; nLocal > 0; i++, nLocal--) {
/* 1784 */       int t = locals[i];
/* 1785 */       this.frame[frameIndex++] = t;
/* 1786 */       if (t == 16777220 || t == 16777219) {
/* 1787 */         i++;
/*      */       }
/*      */     } 
/* 1790 */     for (i = 0; i < stacks.length; i++) {
/* 1791 */       int t = stacks[i];
/* 1792 */       this.frame[frameIndex++] = t;
/* 1793 */       if (t == 16777220 || t == 16777219) {
/* 1794 */         i++;
/*      */       }
/*      */     } 
/* 1797 */     endFrame();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitImplicitFirstFrame() {
/* 1805 */     int frameIndex = startFrame(0, this.descriptor.length() + 1, 0);
/* 1806 */     if ((this.access & 0x8) == 0) {
/* 1807 */       if ((this.access & 0x80000) == 0) {
/* 1808 */         this.frame[frameIndex++] = 0x1700000 | this.cw.addType(this.cw.thisName);
/*      */       } else {
/* 1810 */         this.frame[frameIndex++] = 6;
/*      */       } 
/*      */     }
/* 1813 */     int i = 1;
/*      */     while (true) {
/* 1815 */       int j = i;
/* 1816 */       switch (this.descriptor.charAt(i++)) {
/*      */         case 'B':
/*      */         case 'C':
/*      */         case 'I':
/*      */         case 'S':
/*      */         case 'Z':
/* 1822 */           this.frame[frameIndex++] = 1;
/*      */           continue;
/*      */         case 'F':
/* 1825 */           this.frame[frameIndex++] = 2;
/*      */           continue;
/*      */         case 'J':
/* 1828 */           this.frame[frameIndex++] = 4;
/*      */           continue;
/*      */         case 'D':
/* 1831 */           this.frame[frameIndex++] = 3;
/*      */           continue;
/*      */         case '[':
/* 1834 */           while (this.descriptor.charAt(i) == '[') {
/* 1835 */             i++;
/*      */           }
/* 1837 */           if (this.descriptor.charAt(i) == 'L') {
/* 1838 */             i++;
/* 1839 */             while (this.descriptor.charAt(i) != ';') {
/* 1840 */               i++;
/*      */             }
/*      */           } 
/* 1843 */           this.frame[frameIndex++] = 0x1700000 | this.cw
/* 1844 */             .addType(this.descriptor.substring(j, ++i));
/*      */           continue;
/*      */         case 'L':
/* 1847 */           while (this.descriptor.charAt(i) != ';') {
/* 1848 */             i++;
/*      */           }
/* 1850 */           this.frame[frameIndex++] = 0x1700000 | this.cw
/* 1851 */             .addType(this.descriptor.substring(j + 1, i++));
/*      */           continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 1857 */     this.frame[1] = frameIndex - 3;
/* 1858 */     endFrame();
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
/*      */   private int startFrame(int offset, int nLocal, int nStack) {
/* 1873 */     int n = 3 + nLocal + nStack;
/* 1874 */     if (this.frame == null || this.frame.length < n) {
/* 1875 */       this.frame = new int[n];
/*      */     }
/* 1877 */     this.frame[0] = offset;
/* 1878 */     this.frame[1] = nLocal;
/* 1879 */     this.frame[2] = nStack;
/* 1880 */     return 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void endFrame() {
/* 1888 */     if (this.previousFrame != null) {
/* 1889 */       if (this.stackMap == null) {
/* 1890 */         this.stackMap = new ByteVector();
/*      */       }
/* 1892 */       writeFrame();
/* 1893 */       this.frameCount++;
/*      */     } 
/* 1895 */     this.previousFrame = this.frame;
/* 1896 */     this.frame = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeFrame() {
/* 1904 */     int delta, clocalsSize = this.frame[1];
/* 1905 */     int cstackSize = this.frame[2];
/* 1906 */     if ((this.cw.version & 0xFFFF) < 50) {
/* 1907 */       this.stackMap.putShort(this.frame[0]).putShort(clocalsSize);
/* 1908 */       writeFrameTypes(3, 3 + clocalsSize);
/* 1909 */       this.stackMap.putShort(cstackSize);
/* 1910 */       writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
/*      */       return;
/*      */     } 
/* 1913 */     int localsSize = this.previousFrame[1];
/* 1914 */     int type = 255;
/* 1915 */     int k = 0;
/*      */     
/* 1917 */     if (this.frameCount == 0) {
/* 1918 */       delta = this.frame[0];
/*      */     } else {
/* 1920 */       delta = this.frame[0] - this.previousFrame[0] - 1;
/*      */     } 
/* 1922 */     if (cstackSize == 0) {
/* 1923 */       k = clocalsSize - localsSize;
/* 1924 */       switch (k) {
/*      */         case -3:
/*      */         case -2:
/*      */         case -1:
/* 1928 */           type = 248;
/* 1929 */           localsSize = clocalsSize;
/*      */           break;
/*      */         case 0:
/* 1932 */           type = (delta < 64) ? 0 : 251;
/*      */           break;
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/* 1937 */           type = 252;
/*      */           break;
/*      */       } 
/* 1940 */     } else if (clocalsSize == localsSize && cstackSize == 1) {
/* 1941 */       type = (delta < 63) ? 64 : 247;
/*      */     } 
/*      */     
/* 1944 */     if (type != 255) {
/*      */       
/* 1946 */       int l = 3;
/* 1947 */       for (int j = 0; j < localsSize; j++) {
/* 1948 */         if (this.frame[l] != this.previousFrame[l]) {
/* 1949 */           type = 255;
/*      */           break;
/*      */         } 
/* 1952 */         l++;
/*      */       } 
/*      */     } 
/* 1955 */     switch (type) {
/*      */       case 0:
/* 1957 */         this.stackMap.putByte(delta);
/*      */         return;
/*      */       case 64:
/* 1960 */         this.stackMap.putByte(64 + delta);
/* 1961 */         writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
/*      */         return;
/*      */       case 247:
/* 1964 */         this.stackMap.putByte(247).putShort(delta);
/*      */         
/* 1966 */         writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
/*      */         return;
/*      */       case 251:
/* 1969 */         this.stackMap.putByte(251).putShort(delta);
/*      */         return;
/*      */       case 248:
/* 1972 */         this.stackMap.putByte(251 + k).putShort(delta);
/*      */         return;
/*      */       case 252:
/* 1975 */         this.stackMap.putByte(251 + k).putShort(delta);
/* 1976 */         writeFrameTypes(3 + localsSize, 3 + clocalsSize);
/*      */         return;
/*      */     } 
/*      */     
/* 1980 */     this.stackMap.putByte(255).putShort(delta).putShort(clocalsSize);
/* 1981 */     writeFrameTypes(3, 3 + clocalsSize);
/* 1982 */     this.stackMap.putShort(cstackSize);
/* 1983 */     writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
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
/*      */   private void writeFrameTypes(int start, int end) {
/* 1999 */     for (int i = start; i < end; i++) {
/* 2000 */       int t = this.frame[i];
/* 2001 */       int d = t & 0xF0000000;
/* 2002 */       if (d == 0) {
/* 2003 */         int v = t & 0xFFFFF;
/* 2004 */         switch (t & 0xFF00000) {
/*      */           case 24117248:
/* 2006 */             this.stackMap.putByte(7).putShort(this.cw
/* 2007 */                 .newClass((this.cw.typeTable[v]).strVal1));
/*      */             break;
/*      */           case 25165824:
/* 2010 */             this.stackMap.putByte(8).putShort((this.cw.typeTable[v]).intVal);
/*      */             break;
/*      */           default:
/* 2013 */             this.stackMap.putByte(v); break;
/*      */         } 
/*      */       } else {
/* 2016 */         StringBuilder sb = new StringBuilder();
/* 2017 */         d >>= 28;
/* 2018 */         while (d-- > 0) {
/* 2019 */           sb.append('[');
/*      */         }
/* 2021 */         if ((t & 0xFF00000) == 24117248) {
/* 2022 */           sb.append('L');
/* 2023 */           sb.append((this.cw.typeTable[t & 0xFFFFF]).strVal1);
/* 2024 */           sb.append(';');
/*      */         } else {
/* 2026 */           switch (t & 0xF) {
/*      */             case 1:
/* 2028 */               sb.append('I');
/*      */               break;
/*      */             case 2:
/* 2031 */               sb.append('F');
/*      */               break;
/*      */             case 3:
/* 2034 */               sb.append('D');
/*      */               break;
/*      */             case 9:
/* 2037 */               sb.append('Z');
/*      */               break;
/*      */             case 10:
/* 2040 */               sb.append('B');
/*      */               break;
/*      */             case 11:
/* 2043 */               sb.append('C');
/*      */               break;
/*      */             case 12:
/* 2046 */               sb.append('S');
/*      */               break;
/*      */             default:
/* 2049 */               sb.append('J'); break;
/*      */           } 
/*      */         } 
/* 2052 */         this.stackMap.putByte(7).putShort(this.cw.newClass(sb.toString()));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void writeFrameType(Object type) {
/* 2058 */     if (type instanceof String) {
/* 2059 */       this.stackMap.putByte(7).putShort(this.cw.newClass((String)type));
/* 2060 */     } else if (type instanceof Integer) {
/* 2061 */       this.stackMap.putByte(((Integer)type).intValue());
/*      */     } else {
/* 2063 */       this.stackMap.putByte(8).putShort(((Label)type).position);
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
/*      */   final int getSize() {
/* 2077 */     if (this.classReaderOffset != 0) {
/* 2078 */       return 6 + this.classReaderLength;
/*      */     }
/* 2080 */     int size = 8;
/* 2081 */     if (this.code.length > 0) {
/* 2082 */       if (this.code.length > 65535) {
/* 2083 */         throw new RuntimeException("Method code too large!");
/*      */       }
/* 2085 */       this.cw.newUTF8("Code");
/* 2086 */       size += 18 + this.code.length + 8 * this.handlerCount;
/* 2087 */       if (this.localVar != null) {
/* 2088 */         this.cw.newUTF8("LocalVariableTable");
/* 2089 */         size += 8 + this.localVar.length;
/*      */       } 
/* 2091 */       if (this.localVarType != null) {
/* 2092 */         this.cw.newUTF8("LocalVariableTypeTable");
/* 2093 */         size += 8 + this.localVarType.length;
/*      */       } 
/* 2095 */       if (this.lineNumber != null) {
/* 2096 */         this.cw.newUTF8("LineNumberTable");
/* 2097 */         size += 8 + this.lineNumber.length;
/*      */       } 
/* 2099 */       if (this.stackMap != null) {
/* 2100 */         boolean zip = ((this.cw.version & 0xFFFF) >= 50);
/* 2101 */         this.cw.newUTF8(zip ? "StackMapTable" : "StackMap");
/* 2102 */         size += 8 + this.stackMap.length;
/*      */       } 
/* 2104 */       if (this.ctanns != null) {
/* 2105 */         this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
/* 2106 */         size += 8 + this.ctanns.getSize();
/*      */       } 
/* 2108 */       if (this.ictanns != null) {
/* 2109 */         this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
/* 2110 */         size += 8 + this.ictanns.getSize();
/*      */       } 
/* 2112 */       if (this.cattrs != null) {
/* 2113 */         size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
/*      */       }
/*      */     } 
/*      */     
/* 2117 */     if (this.exceptionCount > 0) {
/* 2118 */       this.cw.newUTF8("Exceptions");
/* 2119 */       size += 8 + 2 * this.exceptionCount;
/*      */     } 
/* 2121 */     if ((this.access & 0x1000) != 0 && ((
/* 2122 */       this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0)) {
/*      */       
/* 2124 */       this.cw.newUTF8("Synthetic");
/* 2125 */       size += 6;
/*      */     } 
/*      */     
/* 2128 */     if ((this.access & 0x20000) != 0) {
/* 2129 */       this.cw.newUTF8("Deprecated");
/* 2130 */       size += 6;
/*      */     } 
/* 2132 */     if (this.signature != null) {
/* 2133 */       this.cw.newUTF8("Signature");
/* 2134 */       this.cw.newUTF8(this.signature);
/* 2135 */       size += 8;
/*      */     } 
/* 2137 */     if (this.methodParameters != null) {
/* 2138 */       this.cw.newUTF8("MethodParameters");
/* 2139 */       size += 7 + this.methodParameters.length;
/*      */     } 
/* 2141 */     if (this.annd != null) {
/* 2142 */       this.cw.newUTF8("AnnotationDefault");
/* 2143 */       size += 6 + this.annd.length;
/*      */     } 
/* 2145 */     if (this.anns != null) {
/* 2146 */       this.cw.newUTF8("RuntimeVisibleAnnotations");
/* 2147 */       size += 8 + this.anns.getSize();
/*      */     } 
/* 2149 */     if (this.ianns != null) {
/* 2150 */       this.cw.newUTF8("RuntimeInvisibleAnnotations");
/* 2151 */       size += 8 + this.ianns.getSize();
/*      */     } 
/* 2153 */     if (this.tanns != null) {
/* 2154 */       this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
/* 2155 */       size += 8 + this.tanns.getSize();
/*      */     } 
/* 2157 */     if (this.itanns != null) {
/* 2158 */       this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
/* 2159 */       size += 8 + this.itanns.getSize();
/*      */     } 
/* 2161 */     if (this.panns != null) {
/* 2162 */       this.cw.newUTF8("RuntimeVisibleParameterAnnotations");
/* 2163 */       size += 7 + 2 * (this.panns.length - this.synthetics);
/* 2164 */       for (int i = this.panns.length - 1; i >= this.synthetics; i--) {
/* 2165 */         size += (this.panns[i] == null) ? 0 : this.panns[i].getSize();
/*      */       }
/*      */     } 
/* 2168 */     if (this.ipanns != null) {
/* 2169 */       this.cw.newUTF8("RuntimeInvisibleParameterAnnotations");
/* 2170 */       size += 7 + 2 * (this.ipanns.length - this.synthetics);
/* 2171 */       for (int i = this.ipanns.length - 1; i >= this.synthetics; i--) {
/* 2172 */         size += (this.ipanns[i] == null) ? 0 : this.ipanns[i].getSize();
/*      */       }
/*      */     } 
/* 2175 */     if (this.attrs != null) {
/* 2176 */       size += this.attrs.getSize(this.cw, null, 0, -1, -1);
/*      */     }
/* 2178 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void put(ByteVector out) {
/* 2189 */     int FACTOR = 64;
/* 2190 */     int mask = 0xE0000 | (this.access & 0x40000) / 64;
/*      */ 
/*      */     
/* 2193 */     out.putShort(this.access & (mask ^ 0xFFFFFFFF)).putShort(this.name).putShort(this.desc);
/* 2194 */     if (this.classReaderOffset != 0) {
/* 2195 */       out.putByteArray(this.cw.cr.b, this.classReaderOffset, this.classReaderLength);
/*      */       return;
/*      */     } 
/* 2198 */     int attributeCount = 0;
/* 2199 */     if (this.code.length > 0) {
/* 2200 */       attributeCount++;
/*      */     }
/* 2202 */     if (this.exceptionCount > 0) {
/* 2203 */       attributeCount++;
/*      */     }
/* 2205 */     if ((this.access & 0x1000) != 0 && ((
/* 2206 */       this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0))
/*      */     {
/* 2208 */       attributeCount++;
/*      */     }
/*      */     
/* 2211 */     if ((this.access & 0x20000) != 0) {
/* 2212 */       attributeCount++;
/*      */     }
/* 2214 */     if (this.signature != null) {
/* 2215 */       attributeCount++;
/*      */     }
/* 2217 */     if (this.methodParameters != null) {
/* 2218 */       attributeCount++;
/*      */     }
/* 2220 */     if (this.annd != null) {
/* 2221 */       attributeCount++;
/*      */     }
/* 2223 */     if (this.anns != null) {
/* 2224 */       attributeCount++;
/*      */     }
/* 2226 */     if (this.ianns != null) {
/* 2227 */       attributeCount++;
/*      */     }
/* 2229 */     if (this.tanns != null) {
/* 2230 */       attributeCount++;
/*      */     }
/* 2232 */     if (this.itanns != null) {
/* 2233 */       attributeCount++;
/*      */     }
/* 2235 */     if (this.panns != null) {
/* 2236 */       attributeCount++;
/*      */     }
/* 2238 */     if (this.ipanns != null) {
/* 2239 */       attributeCount++;
/*      */     }
/* 2241 */     if (this.attrs != null) {
/* 2242 */       attributeCount += this.attrs.getCount();
/*      */     }
/* 2244 */     out.putShort(attributeCount);
/* 2245 */     if (this.code.length > 0) {
/* 2246 */       int size = 12 + this.code.length + 8 * this.handlerCount;
/* 2247 */       if (this.localVar != null) {
/* 2248 */         size += 8 + this.localVar.length;
/*      */       }
/* 2250 */       if (this.localVarType != null) {
/* 2251 */         size += 8 + this.localVarType.length;
/*      */       }
/* 2253 */       if (this.lineNumber != null) {
/* 2254 */         size += 8 + this.lineNumber.length;
/*      */       }
/* 2256 */       if (this.stackMap != null) {
/* 2257 */         size += 8 + this.stackMap.length;
/*      */       }
/* 2259 */       if (this.ctanns != null) {
/* 2260 */         size += 8 + this.ctanns.getSize();
/*      */       }
/* 2262 */       if (this.ictanns != null) {
/* 2263 */         size += 8 + this.ictanns.getSize();
/*      */       }
/* 2265 */       if (this.cattrs != null) {
/* 2266 */         size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
/*      */       }
/*      */       
/* 2269 */       out.putShort(this.cw.newUTF8("Code")).putInt(size);
/* 2270 */       out.putShort(this.maxStack).putShort(this.maxLocals);
/* 2271 */       out.putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
/* 2272 */       out.putShort(this.handlerCount);
/* 2273 */       if (this.handlerCount > 0) {
/* 2274 */         Handler h = this.firstHandler;
/* 2275 */         while (h != null) {
/* 2276 */           out.putShort(h.start.position).putShort(h.end.position)
/* 2277 */             .putShort(h.handler.position).putShort(h.type);
/* 2278 */           h = h.next;
/*      */         } 
/*      */       } 
/* 2281 */       attributeCount = 0;
/* 2282 */       if (this.localVar != null) {
/* 2283 */         attributeCount++;
/*      */       }
/* 2285 */       if (this.localVarType != null) {
/* 2286 */         attributeCount++;
/*      */       }
/* 2288 */       if (this.lineNumber != null) {
/* 2289 */         attributeCount++;
/*      */       }
/* 2291 */       if (this.stackMap != null) {
/* 2292 */         attributeCount++;
/*      */       }
/* 2294 */       if (this.ctanns != null) {
/* 2295 */         attributeCount++;
/*      */       }
/* 2297 */       if (this.ictanns != null) {
/* 2298 */         attributeCount++;
/*      */       }
/* 2300 */       if (this.cattrs != null) {
/* 2301 */         attributeCount += this.cattrs.getCount();
/*      */       }
/* 2303 */       out.putShort(attributeCount);
/* 2304 */       if (this.localVar != null) {
/* 2305 */         out.putShort(this.cw.newUTF8("LocalVariableTable"));
/* 2306 */         out.putInt(this.localVar.length + 2).putShort(this.localVarCount);
/* 2307 */         out.putByteArray(this.localVar.data, 0, this.localVar.length);
/*      */       } 
/* 2309 */       if (this.localVarType != null) {
/* 2310 */         out.putShort(this.cw.newUTF8("LocalVariableTypeTable"));
/* 2311 */         out.putInt(this.localVarType.length + 2).putShort(this.localVarTypeCount);
/* 2312 */         out.putByteArray(this.localVarType.data, 0, this.localVarType.length);
/*      */       } 
/* 2314 */       if (this.lineNumber != null) {
/* 2315 */         out.putShort(this.cw.newUTF8("LineNumberTable"));
/* 2316 */         out.putInt(this.lineNumber.length + 2).putShort(this.lineNumberCount);
/* 2317 */         out.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
/*      */       } 
/* 2319 */       if (this.stackMap != null) {
/* 2320 */         boolean zip = ((this.cw.version & 0xFFFF) >= 50);
/* 2321 */         out.putShort(this.cw.newUTF8(zip ? "StackMapTable" : "StackMap"));
/* 2322 */         out.putInt(this.stackMap.length + 2).putShort(this.frameCount);
/* 2323 */         out.putByteArray(this.stackMap.data, 0, this.stackMap.length);
/*      */       } 
/* 2325 */       if (this.ctanns != null) {
/* 2326 */         out.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
/* 2327 */         this.ctanns.put(out);
/*      */       } 
/* 2329 */       if (this.ictanns != null) {
/* 2330 */         out.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
/* 2331 */         this.ictanns.put(out);
/*      */       } 
/* 2333 */       if (this.cattrs != null) {
/* 2334 */         this.cattrs.put(this.cw, this.code.data, this.code.length, this.maxLocals, this.maxStack, out);
/*      */       }
/*      */     } 
/* 2337 */     if (this.exceptionCount > 0) {
/* 2338 */       out.putShort(this.cw.newUTF8("Exceptions")).putInt(2 * this.exceptionCount + 2);
/*      */       
/* 2340 */       out.putShort(this.exceptionCount);
/* 2341 */       for (int i = 0; i < this.exceptionCount; i++) {
/* 2342 */         out.putShort(this.exceptions[i]);
/*      */       }
/*      */     } 
/* 2345 */     if ((this.access & 0x1000) != 0 && ((
/* 2346 */       this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0))
/*      */     {
/* 2348 */       out.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
/*      */     }
/*      */     
/* 2351 */     if ((this.access & 0x20000) != 0) {
/* 2352 */       out.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
/*      */     }
/* 2354 */     if (this.signature != null) {
/* 2355 */       out.putShort(this.cw.newUTF8("Signature")).putInt(2)
/* 2356 */         .putShort(this.cw.newUTF8(this.signature));
/*      */     }
/* 2358 */     if (this.methodParameters != null) {
/* 2359 */       out.putShort(this.cw.newUTF8("MethodParameters"));
/* 2360 */       out.putInt(this.methodParameters.length + 1).putByte(this.methodParametersCount);
/*      */       
/* 2362 */       out.putByteArray(this.methodParameters.data, 0, this.methodParameters.length);
/*      */     } 
/* 2364 */     if (this.annd != null) {
/* 2365 */       out.putShort(this.cw.newUTF8("AnnotationDefault"));
/* 2366 */       out.putInt(this.annd.length);
/* 2367 */       out.putByteArray(this.annd.data, 0, this.annd.length);
/*      */     } 
/* 2369 */     if (this.anns != null) {
/* 2370 */       out.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
/* 2371 */       this.anns.put(out);
/*      */     } 
/* 2373 */     if (this.ianns != null) {
/* 2374 */       out.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
/* 2375 */       this.ianns.put(out);
/*      */     } 
/* 2377 */     if (this.tanns != null) {
/* 2378 */       out.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
/* 2379 */       this.tanns.put(out);
/*      */     } 
/* 2381 */     if (this.itanns != null) {
/* 2382 */       out.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
/* 2383 */       this.itanns.put(out);
/*      */     } 
/* 2385 */     if (this.panns != null) {
/* 2386 */       out.putShort(this.cw.newUTF8("RuntimeVisibleParameterAnnotations"));
/* 2387 */       AnnotationWriter.put(this.panns, this.synthetics, out);
/*      */     } 
/* 2389 */     if (this.ipanns != null) {
/* 2390 */       out.putShort(this.cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
/* 2391 */       AnnotationWriter.put(this.ipanns, this.synthetics, out);
/*      */     } 
/* 2393 */     if (this.attrs != null)
/* 2394 */       this.attrs.put(this.cw, null, 0, -1, -1, out); 
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\asm\MethodWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */