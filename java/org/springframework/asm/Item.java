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
/*     */ final class Item
/*     */ {
/*     */   int index;
/*     */   int type;
/*     */   int intVal;
/*     */   long longVal;
/*     */   String strVal1;
/*     */   String strVal2;
/*     */   String strVal3;
/*     */   int hashCode;
/*     */   Item next;
/*     */   
/*     */   Item() {}
/*     */   
/*     */   Item(int index) {
/* 123 */     this.index = index;
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
/*     */   Item(int index, Item i) {
/* 135 */     this.index = index;
/* 136 */     this.type = i.type;
/* 137 */     this.intVal = i.intVal;
/* 138 */     this.longVal = i.longVal;
/* 139 */     this.strVal1 = i.strVal1;
/* 140 */     this.strVal2 = i.strVal2;
/* 141 */     this.strVal3 = i.strVal3;
/* 142 */     this.hashCode = i.hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(int intVal) {
/* 152 */     this.type = 3;
/* 153 */     this.intVal = intVal;
/* 154 */     this.hashCode = Integer.MAX_VALUE & this.type + intVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(long longVal) {
/* 164 */     this.type = 5;
/* 165 */     this.longVal = longVal;
/* 166 */     this.hashCode = Integer.MAX_VALUE & this.type + (int)longVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(float floatVal) {
/* 176 */     this.type = 4;
/* 177 */     this.intVal = Float.floatToRawIntBits(floatVal);
/* 178 */     this.hashCode = Integer.MAX_VALUE & this.type + (int)floatVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(double doubleVal) {
/* 188 */     this.type = 6;
/* 189 */     this.longVal = Double.doubleToRawLongBits(doubleVal);
/* 190 */     this.hashCode = Integer.MAX_VALUE & this.type + (int)doubleVal;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void set(int type, String strVal1, String strVal2, String strVal3) {
/* 208 */     this.type = type;
/* 209 */     this.strVal1 = strVal1;
/* 210 */     this.strVal2 = strVal2;
/* 211 */     this.strVal3 = strVal3;
/* 212 */     switch (type) {
/*     */       case 7:
/* 214 */         this.intVal = 0;
/*     */       case 1:
/*     */       case 8:
/*     */       case 16:
/*     */       case 19:
/*     */       case 20:
/*     */       case 30:
/* 221 */         this.hashCode = Integer.MAX_VALUE & type + strVal1.hashCode();
/*     */         return;
/*     */       case 12:
/* 224 */         this
/* 225 */           .hashCode = Integer.MAX_VALUE & type + strVal1.hashCode() * strVal2.hashCode();
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     this
/* 234 */       .hashCode = Integer.MAX_VALUE & type + strVal1.hashCode() * strVal2.hashCode() * strVal3.hashCode();
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
/*     */   
/*     */   void set(String name, String desc, int bsmIndex) {
/* 249 */     this.type = 18;
/* 250 */     this.longVal = bsmIndex;
/* 251 */     this.strVal1 = name;
/* 252 */     this.strVal2 = desc;
/* 253 */     this
/* 254 */       .hashCode = Integer.MAX_VALUE & 18 + bsmIndex * this.strVal1.hashCode() * this.strVal2.hashCode();
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
/*     */   void set(int position, int hashCode) {
/* 268 */     this.type = 33;
/* 269 */     this.intVal = position;
/* 270 */     this.hashCode = hashCode;
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
/*     */   boolean isEqualTo(Item i) {
/* 284 */     switch (this.type) {
/*     */       case 1:
/*     */       case 7:
/*     */       case 8:
/*     */       case 16:
/*     */       case 19:
/*     */       case 20:
/*     */       case 30:
/* 292 */         return i.strVal1.equals(this.strVal1);
/*     */       case 5:
/*     */       case 6:
/*     */       case 32:
/* 296 */         return (i.longVal == this.longVal);
/*     */       case 3:
/*     */       case 4:
/* 299 */         return (i.intVal == this.intVal);
/*     */       case 31:
/* 301 */         return (i.intVal == this.intVal && i.strVal1.equals(this.strVal1));
/*     */       case 12:
/* 303 */         return (i.strVal1.equals(this.strVal1) && i.strVal2.equals(this.strVal2));
/*     */       case 18:
/* 305 */         return (i.longVal == this.longVal && i.strVal1.equals(this.strVal1) && i.strVal2
/* 306 */           .equals(this.strVal2));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     return (i.strVal1.equals(this.strVal1) && i.strVal2.equals(this.strVal2) && i.strVal3
/* 314 */       .equals(this.strVal3));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\asm\Item.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */