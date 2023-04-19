/*    */ package com.shn.shiftleft;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown = true)
/*    */ public class ShiftLeftInlineData {
/*    */   private String userName;
/*    */   private String password;
/*    */   private String environment;
/*    */   private List<String> changes;
/*    */   
/* 12 */   public void setUserName(String userName) { this.userName = userName; } private String accessToken; private String cloneDir; private String cspName; private String bpsTenantId; public void setPassword(String password) { this.password = password; } public void setEnvironment(String environment) { this.environment = environment; } public void setChanges(List<String> changes) { this.changes = changes; } public void setAccessToken(String accessToken) { this.accessToken = accessToken; } public void setCloneDir(String cloneDir) { this.cloneDir = cloneDir; } public void setCspName(String cspName) { this.cspName = cspName; } public void setBpsTenantId(String bpsTenantId) { this.bpsTenantId = bpsTenantId; }
/*    */ 
/*    */   
/* 15 */   public String getUserName() { return this.userName; }
/* 16 */   public String getPassword() { return this.password; }
/* 17 */   public String getEnvironment() { return this.environment; }
/* 18 */   public List<String> getChanges() { return this.changes; }
/* 19 */   public String getAccessToken() { return this.accessToken; }
/* 20 */   public String getCloneDir() { return this.cloneDir; }
/* 21 */   public String getCspName() { return this.cspName; } public String getBpsTenantId() {
/* 22 */     return this.bpsTenantId;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\shn\shiftleft\ShiftLeftInlineData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */