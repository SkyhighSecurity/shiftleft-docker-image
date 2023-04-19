/*    */ package com.fasterxml.jackson.databind.node;
/*    */ 
/*    */ import java.io.Externalizable;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInput;
/*    */ import java.io.ObjectOutput;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class NodeSerialization
/*    */   implements Serializable, Externalizable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public byte[] json;
/*    */   
/*    */   public NodeSerialization() {}
/*    */   
/*    */   public NodeSerialization(byte[] b) {
/* 21 */     this.json = b;
/*    */   }
/*    */   protected Object readResolve() {
/*    */     try {
/* 25 */       return InternalNodeMapper.bytesToNode(this.json);
/* 26 */     } catch (IOException e) {
/* 27 */       throw new IllegalArgumentException("Failed to JDK deserialize `JsonNode` value: " + e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static NodeSerialization from(Object o) {
/*    */     try {
/* 33 */       return new NodeSerialization(InternalNodeMapper.valueToBytes(o));
/* 34 */     } catch (IOException e) {
/* 35 */       throw new IllegalArgumentException("Failed to JDK serialize `" + o.getClass().getSimpleName() + "` value: " + e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeExternal(ObjectOutput out) throws IOException {
/* 41 */     out.writeInt(this.json.length);
/* 42 */     out.write(this.json);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readExternal(ObjectInput in) throws IOException {
/* 47 */     int len = in.readInt();
/* 48 */     this.json = new byte[len];
/* 49 */     in.readFully(this.json, 0, len);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\node\NodeSerialization.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */