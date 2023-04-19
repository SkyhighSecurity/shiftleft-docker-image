/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastByteArrayOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final int DEFAULT_BLOCK_SIZE = 256;
/*  51 */   private final LinkedList<byte[]> buffers = (LinkedList)new LinkedList<byte>();
/*     */ 
/*     */   
/*     */   private final int initialBlockSize;
/*     */ 
/*     */   
/*  57 */   private int nextBlockSize = 0;
/*     */ 
/*     */ 
/*     */   
/*  61 */   private int alreadyBufferedSize = 0;
/*     */ 
/*     */   
/*  64 */   private int index = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastByteArrayOutputStream() {
/*  75 */     this(256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastByteArrayOutputStream(int initialBlockSize) {
/*  84 */     Assert.isTrue((initialBlockSize > 0), "Initial block size must be greater than 0");
/*  85 */     this.initialBlockSize = initialBlockSize;
/*  86 */     this.nextBlockSize = initialBlockSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int datum) throws IOException {
/*  94 */     if (this.closed) {
/*  95 */       throw new IOException("Stream closed");
/*     */     }
/*     */     
/*  98 */     if (this.buffers.peekLast() == null || ((byte[])this.buffers.getLast()).length == this.index) {
/*  99 */       addBuffer(1);
/*     */     }
/*     */     
/* 102 */     ((byte[])this.buffers.getLast())[this.index++] = (byte)datum;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] data, int offset, int length) throws IOException {
/* 108 */     if (data == null) {
/* 109 */       throw new NullPointerException();
/*     */     }
/* 111 */     if (offset < 0 || offset + length > data.length || length < 0) {
/* 112 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 114 */     if (this.closed) {
/* 115 */       throw new IOException("Stream closed");
/*     */     }
/*     */     
/* 118 */     if (this.buffers.peekLast() == null || ((byte[])this.buffers.getLast()).length == this.index) {
/* 119 */       addBuffer(length);
/*     */     }
/* 121 */     if (this.index + length > ((byte[])this.buffers.getLast()).length) {
/* 122 */       int pos = offset;
/*     */       do {
/* 124 */         if (this.index == ((byte[])this.buffers.getLast()).length) {
/* 125 */           addBuffer(length);
/*     */         }
/* 127 */         int copyLength = ((byte[])this.buffers.getLast()).length - this.index;
/* 128 */         if (length < copyLength) {
/* 129 */           copyLength = length;
/*     */         }
/* 131 */         System.arraycopy(data, pos, this.buffers.getLast(), this.index, copyLength);
/* 132 */         pos += copyLength;
/* 133 */         this.index += copyLength;
/* 134 */         length -= copyLength;
/*     */       }
/* 136 */       while (length > 0);
/*     */     }
/*     */     else {
/*     */       
/* 140 */       System.arraycopy(data, offset, this.buffers.getLast(), this.index, length);
/* 141 */       this.index += length;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 148 */     this.closed = true;
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
/*     */   public String toString() {
/* 165 */     return new String(toByteArrayUnsafe());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 175 */     return this.alreadyBufferedSize + this.index;
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
/*     */   public byte[] toByteArrayUnsafe() {
/* 191 */     int totalSize = size();
/* 192 */     if (totalSize == 0) {
/* 193 */       return new byte[0];
/*     */     }
/* 195 */     resize(totalSize);
/* 196 */     return this.buffers.getFirst();
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
/*     */   public byte[] toByteArray() {
/* 209 */     byte[] bytesUnsafe = toByteArrayUnsafe();
/* 210 */     byte[] ret = new byte[bytesUnsafe.length];
/* 211 */     System.arraycopy(bytesUnsafe, 0, ret, 0, bytesUnsafe.length);
/* 212 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 221 */     this.buffers.clear();
/* 222 */     this.nextBlockSize = this.initialBlockSize;
/* 223 */     this.closed = false;
/* 224 */     this.index = 0;
/* 225 */     this.alreadyBufferedSize = 0;
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
/*     */   public InputStream getInputStream() {
/* 237 */     return new FastByteArrayInputStream(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 245 */     Iterator<byte[]> it = (Iterator)this.buffers.iterator();
/* 246 */     while (it.hasNext()) {
/* 247 */       byte[] bytes = it.next();
/* 248 */       if (it.hasNext()) {
/* 249 */         out.write(bytes, 0, bytes.length);
/*     */         continue;
/*     */       } 
/* 252 */       out.write(bytes, 0, this.index);
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
/*     */   public void resize(int targetCapacity) {
/* 265 */     Assert.isTrue((targetCapacity >= size()), "New capacity must not be smaller than current size");
/* 266 */     if (this.buffers.peekFirst() == null) {
/* 267 */       this.nextBlockSize = targetCapacity - size();
/*     */     }
/* 269 */     else if (size() != targetCapacity || ((byte[])this.buffers.getFirst()).length != targetCapacity) {
/*     */ 
/*     */ 
/*     */       
/* 273 */       int totalSize = size();
/* 274 */       byte[] data = new byte[targetCapacity];
/* 275 */       int pos = 0;
/* 276 */       Iterator<byte[]> it = (Iterator)this.buffers.iterator();
/* 277 */       while (it.hasNext()) {
/* 278 */         byte[] bytes = it.next();
/* 279 */         if (it.hasNext()) {
/* 280 */           System.arraycopy(bytes, 0, data, pos, bytes.length);
/* 281 */           pos += bytes.length;
/*     */           continue;
/*     */         } 
/* 284 */         System.arraycopy(bytes, 0, data, pos, this.index);
/*     */       } 
/*     */       
/* 287 */       this.buffers.clear();
/* 288 */       this.buffers.add(data);
/* 289 */       this.index = totalSize;
/* 290 */       this.alreadyBufferedSize = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addBuffer(int minCapacity) {
/* 299 */     if (this.buffers.peekLast() != null) {
/* 300 */       this.alreadyBufferedSize += this.index;
/* 301 */       this.index = 0;
/*     */     } 
/* 303 */     if (this.nextBlockSize < minCapacity) {
/* 304 */       this.nextBlockSize = nextPowerOf2(minCapacity);
/*     */     }
/* 306 */     this.buffers.add(new byte[this.nextBlockSize]);
/* 307 */     this.nextBlockSize *= 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int nextPowerOf2(int val) {
/* 314 */     val--;
/* 315 */     val = val >> 1 | val;
/* 316 */     val = val >> 2 | val;
/* 317 */     val = val >> 4 | val;
/* 318 */     val = val >> 8 | val;
/* 319 */     val = val >> 16 | val;
/* 320 */     val++;
/* 321 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class FastByteArrayInputStream
/*     */     extends UpdateMessageDigestInputStream
/*     */   {
/*     */     private final FastByteArrayOutputStream fastByteArrayOutputStream;
/*     */ 
/*     */     
/*     */     private final Iterator<byte[]> buffersIterator;
/*     */ 
/*     */     
/*     */     private byte[] currentBuffer;
/*     */     
/* 337 */     private int currentBufferLength = 0;
/*     */     
/* 339 */     private int nextIndexInCurrentBuffer = 0;
/*     */     
/* 341 */     private int totalBytesRead = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FastByteArrayInputStream(FastByteArrayOutputStream fastByteArrayOutputStream) {
/* 348 */       this.fastByteArrayOutputStream = fastByteArrayOutputStream;
/* 349 */       this.buffersIterator = (Iterator)fastByteArrayOutputStream.buffers.iterator();
/* 350 */       if (this.buffersIterator.hasNext()) {
/* 351 */         this.currentBuffer = this.buffersIterator.next();
/* 352 */         if (this.currentBuffer == fastByteArrayOutputStream.buffers.getLast()) {
/* 353 */           this.currentBufferLength = fastByteArrayOutputStream.index;
/*     */         } else {
/*     */           
/* 356 */           this.currentBufferLength = this.currentBuffer.length;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() {
/* 363 */       if (this.currentBuffer == null)
/*     */       {
/* 365 */         return -1;
/*     */       }
/*     */       
/* 368 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 369 */         this.totalBytesRead++;
/* 370 */         return this.currentBuffer[this.nextIndexInCurrentBuffer++];
/*     */       } 
/*     */       
/* 373 */       if (this.buffersIterator.hasNext()) {
/* 374 */         this.currentBuffer = this.buffersIterator.next();
/* 375 */         if (this.currentBuffer == this.fastByteArrayOutputStream.buffers.getLast()) {
/* 376 */           this.currentBufferLength = this.fastByteArrayOutputStream.index;
/*     */         } else {
/*     */           
/* 379 */           this.currentBufferLength = this.currentBuffer.length;
/*     */         } 
/* 381 */         this.nextIndexInCurrentBuffer = 0;
/*     */       } else {
/*     */         
/* 384 */         this.currentBuffer = null;
/*     */       } 
/* 386 */       return read();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(byte[] b) {
/* 393 */       return read(b, 0, b.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) {
/* 398 */       if (b == null) {
/* 399 */         throw new NullPointerException();
/*     */       }
/* 401 */       if (off < 0 || len < 0 || len > b.length - off) {
/* 402 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 404 */       if (len == 0) {
/* 405 */         return 0;
/*     */       }
/*     */       
/* 408 */       if (this.currentBuffer == null)
/*     */       {
/* 410 */         return -1;
/*     */       }
/*     */       
/* 413 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 414 */         int bytesToCopy = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
/* 415 */         System.arraycopy(this.currentBuffer, this.nextIndexInCurrentBuffer, b, off, bytesToCopy);
/* 416 */         this.totalBytesRead += bytesToCopy;
/* 417 */         this.nextIndexInCurrentBuffer += bytesToCopy;
/* 418 */         int remaining = read(b, off + bytesToCopy, len - bytesToCopy);
/* 419 */         return bytesToCopy + Math.max(remaining, 0);
/*     */       } 
/*     */       
/* 422 */       if (this.buffersIterator.hasNext()) {
/* 423 */         this.currentBuffer = this.buffersIterator.next();
/* 424 */         if (this.currentBuffer == this.fastByteArrayOutputStream.buffers.getLast()) {
/* 425 */           this.currentBufferLength = this.fastByteArrayOutputStream.index;
/*     */         } else {
/*     */           
/* 428 */           this.currentBufferLength = this.currentBuffer.length;
/*     */         } 
/* 430 */         this.nextIndexInCurrentBuffer = 0;
/*     */       } else {
/*     */         
/* 433 */         this.currentBuffer = null;
/*     */       } 
/* 435 */       return read(b, off, len);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long skip(long n) throws IOException {
/* 443 */       if (n > 2147483647L) {
/* 444 */         throw new IllegalArgumentException("n exceeds maximum (2147483647): " + n);
/*     */       }
/* 446 */       if (n == 0L) {
/* 447 */         return 0L;
/*     */       }
/* 449 */       if (n < 0L) {
/* 450 */         throw new IllegalArgumentException("n must be 0 or greater: " + n);
/*     */       }
/* 452 */       int len = (int)n;
/* 453 */       if (this.currentBuffer == null)
/*     */       {
/* 455 */         return 0L;
/*     */       }
/*     */       
/* 458 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 459 */         int bytesToSkip = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
/* 460 */         this.totalBytesRead += bytesToSkip;
/* 461 */         this.nextIndexInCurrentBuffer += bytesToSkip;
/* 462 */         return bytesToSkip + skip((len - bytesToSkip));
/*     */       } 
/*     */       
/* 465 */       if (this.buffersIterator.hasNext()) {
/* 466 */         this.currentBuffer = this.buffersIterator.next();
/* 467 */         if (this.currentBuffer == this.fastByteArrayOutputStream.buffers.getLast()) {
/* 468 */           this.currentBufferLength = this.fastByteArrayOutputStream.index;
/*     */         } else {
/*     */           
/* 471 */           this.currentBufferLength = this.currentBuffer.length;
/*     */         } 
/* 473 */         this.nextIndexInCurrentBuffer = 0;
/*     */       } else {
/*     */         
/* 476 */         this.currentBuffer = null;
/*     */       } 
/* 478 */       return skip(len);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int available() {
/* 485 */       return this.fastByteArrayOutputStream.size() - this.totalBytesRead;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateMessageDigest(MessageDigest messageDigest) {
/* 494 */       updateMessageDigest(messageDigest, available());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateMessageDigest(MessageDigest messageDigest, int len) {
/* 505 */       if (this.currentBuffer == null) {
/*     */         return;
/*     */       }
/*     */       
/* 509 */       if (len == 0) {
/*     */         return;
/*     */       }
/* 512 */       if (len < 0) {
/* 513 */         throw new IllegalArgumentException("len must be 0 or greater: " + len);
/*     */       }
/*     */       
/* 516 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 517 */         int bytesToCopy = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
/* 518 */         messageDigest.update(this.currentBuffer, this.nextIndexInCurrentBuffer, bytesToCopy);
/* 519 */         this.nextIndexInCurrentBuffer += bytesToCopy;
/* 520 */         updateMessageDigest(messageDigest, len - bytesToCopy);
/*     */       } else {
/*     */         
/* 523 */         if (this.buffersIterator.hasNext()) {
/* 524 */           this.currentBuffer = this.buffersIterator.next();
/* 525 */           if (this.currentBuffer == this.fastByteArrayOutputStream.buffers.getLast()) {
/* 526 */             this.currentBufferLength = this.fastByteArrayOutputStream.index;
/*     */           } else {
/*     */             
/* 529 */             this.currentBufferLength = this.currentBuffer.length;
/*     */           } 
/* 531 */           this.nextIndexInCurrentBuffer = 0;
/*     */         } else {
/*     */           
/* 534 */           this.currentBuffer = null;
/*     */         } 
/* 536 */         updateMessageDigest(messageDigest, len);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\FastByteArrayOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */