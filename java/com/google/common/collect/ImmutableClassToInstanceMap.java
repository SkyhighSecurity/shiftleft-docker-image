/*     */ package com.google.common.collect;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ImmutableClassToInstanceMap<B>
/*     */   extends ForwardingMap<Class<? extends B>, B>
/*     */   implements ClassToInstanceMap<B>
/*     */ {
/*     */   private final ImmutableMap<Class<? extends B>, B> delegate;
/*     */   
/*     */   public static <B> Builder<B> builder() {
/*  36 */     return new Builder<B>();
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
/*     */   public static final class Builder<B>
/*     */   {
/*  55 */     private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> Builder<B> put(Class<T> type, T value) {
/*  63 */       this.mapBuilder.put(type, (B)value);
/*  64 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> map) {
/*  78 */       for (Map.Entry<? extends Class<? extends T>, ? extends T> entry : map.entrySet()) {
/*  79 */         Class<? extends T> type = entry.getKey();
/*  80 */         T value = entry.getValue();
/*  81 */         this.mapBuilder.put(type, MutableClassToInstanceMap.cast((Class)type, value));
/*     */       } 
/*  83 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableClassToInstanceMap<B> build() {
/*  93 */       return new ImmutableClassToInstanceMap<B>(this.mapBuilder.build());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> map) {
/* 113 */     if (map instanceof ImmutableClassToInstanceMap) {
/* 114 */       return (ImmutableClassToInstanceMap)map;
/*     */     }
/* 116 */     return (new Builder<B>()).<S>putAll(map).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> delegate) {
/* 123 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */   protected Map<Class<? extends B>, B> delegate() {
/* 127 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends B> T getInstance(Class<T> type) {
/* 132 */     return (T)this.delegate.get(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends B> T putInstance(Class<T> type, T value) {
/* 141 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ImmutableClassToInstanceMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */