/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public class Joiner
/*     */ {
/*     */   private final String separator;
/*     */   
/*     */   public static Joiner on(String separator) {
/*  57 */     return new Joiner(separator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Joiner on(char separator) {
/*  65 */     return new Joiner(String.valueOf(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Joiner(String separator) {
/*  71 */     this.separator = Preconditions.<String>checkNotNull(separator);
/*     */   }
/*     */   
/*     */   private Joiner(Joiner prototype) {
/*  75 */     this.separator = prototype.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
/*  84 */     Preconditions.checkNotNull(appendable);
/*  85 */     Iterator<?> iterator = parts.iterator();
/*  86 */     if (iterator.hasNext()) {
/*  87 */       appendable.append(toString(iterator.next()));
/*  88 */       while (iterator.hasNext()) {
/*  89 */         appendable.append(this.separator);
/*  90 */         appendable.append(toString(iterator.next()));
/*     */       } 
/*     */     } 
/*  93 */     return appendable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
/* 102 */     return appendTo(appendable, Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <A extends Appendable> A appendTo(A appendable, @Nullable Object first, @Nullable Object second, Object... rest) throws IOException {
/* 112 */     return appendTo(appendable, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
/*     */     try {
/* 124 */       appendTo(builder, parts);
/* 125 */     } catch (IOException impossible) {
/* 126 */       throw new AssertionError(impossible);
/*     */     } 
/* 128 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
/* 138 */     return appendTo(builder, Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringBuilder appendTo(StringBuilder builder, @Nullable Object first, @Nullable Object second, Object... rest) {
/* 148 */     return appendTo(builder, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Iterable<?> parts) {
/* 156 */     return appendTo(new StringBuilder(), parts).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Object[] parts) {
/* 164 */     return join(Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(@Nullable Object first, @Nullable Object second, Object... rest) {
/* 173 */     return join(iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Joiner useForNull(final String nullText) {
/* 181 */     Preconditions.checkNotNull(nullText);
/* 182 */     return new Joiner(this) {
/*     */         CharSequence toString(Object part) {
/* 184 */           return (part == null) ? nullText : Joiner.this.toString(part);
/*     */         }
/*     */         public Joiner useForNull(String nullText) {
/* 187 */           Preconditions.checkNotNull(nullText);
/*     */           
/* 189 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */         public Joiner skipNulls() {
/* 192 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Joiner skipNulls() {
/* 202 */     return new Joiner(this)
/*     */       {
/*     */         public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
/* 205 */           Preconditions.checkNotNull(appendable, "appendable");
/* 206 */           Preconditions.checkNotNull(parts, "parts");
/* 207 */           Iterator<?> iterator = parts.iterator();
/* 208 */           while (iterator.hasNext()) {
/* 209 */             Object part = iterator.next();
/* 210 */             if (part != null) {
/* 211 */               appendable.append(Joiner.this.toString(part));
/*     */               break;
/*     */             } 
/*     */           } 
/* 215 */           while (iterator.hasNext()) {
/* 216 */             Object part = iterator.next();
/* 217 */             if (part != null) {
/* 218 */               appendable.append(Joiner.this.separator);
/* 219 */               appendable.append(Joiner.this.toString(part));
/*     */             } 
/*     */           } 
/* 222 */           return appendable;
/*     */         }
/*     */         public Joiner useForNull(String nullText) {
/* 225 */           Preconditions.checkNotNull(nullText);
/* 226 */           throw new UnsupportedOperationException("already specified skipNulls");
/*     */         }
/*     */         public Joiner.MapJoiner withKeyValueSeparator(String kvs) {
/* 229 */           Preconditions.checkNotNull(kvs);
/* 230 */           throw new UnsupportedOperationException("can't use .skipNulls() with maps");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapJoiner withKeyValueSeparator(String keyValueSeparator) {
/* 241 */     return new MapJoiner(this, Preconditions.<String>checkNotNull(keyValueSeparator));
/*     */   }
/*     */ 
/*     */   
/*     */   public static class MapJoiner
/*     */   {
/*     */     private Joiner joiner;
/*     */     
/*     */     private String keyValueSeparator;
/*     */ 
/*     */     
/*     */     private MapJoiner(Joiner joiner, String keyValueSeparator) {
/* 253 */       this.joiner = joiner;
/* 254 */       this.keyValueSeparator = keyValueSeparator;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
/* 264 */       Preconditions.checkNotNull(appendable);
/* 265 */       Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
/* 266 */       if (iterator.hasNext()) {
/* 267 */         Map.Entry<?, ?> entry = iterator.next();
/* 268 */         appendable.append(this.joiner.toString(entry.getKey()));
/* 269 */         appendable.append(this.keyValueSeparator);
/* 270 */         appendable.append(this.joiner.toString(entry.getValue()));
/* 271 */         while (iterator.hasNext()) {
/* 272 */           appendable.append(this.joiner.separator);
/* 273 */           Map.Entry<?, ?> e = iterator.next();
/* 274 */           appendable.append(this.joiner.toString(e.getKey()));
/* 275 */           appendable.append(this.keyValueSeparator);
/* 276 */           appendable.append(this.joiner.toString(e.getValue()));
/*     */         } 
/*     */       } 
/* 279 */       return appendable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
/*     */       try {
/* 290 */         appendTo(builder, map);
/* 291 */       } catch (IOException impossible) {
/* 292 */         throw new AssertionError(impossible);
/*     */       } 
/* 294 */       return builder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String join(Map<?, ?> map) {
/* 303 */       return appendTo(new StringBuilder(), map).toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MapJoiner useForNull(String nullText) {
/* 312 */       return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator);
/*     */     }
/*     */   }
/*     */   
/*     */   CharSequence toString(Object part) {
/* 317 */     return (part instanceof CharSequence) ? (CharSequence)part : part.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
/* 324 */     Preconditions.checkNotNull(rest);
/* 325 */     return new AbstractList() {
/*     */         public int size() {
/* 327 */           return rest.length + 2;
/*     */         }
/*     */         public Object get(int index) {
/* 330 */           switch (index) {
/*     */             case 0:
/* 332 */               return first;
/*     */             case 1:
/* 334 */               return second;
/*     */           } 
/* 336 */           return rest[index - 2];
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\base\Joiner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */