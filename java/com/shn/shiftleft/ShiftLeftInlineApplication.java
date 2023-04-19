/*     */ package com.shn.shiftleft;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Joiner;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.collections.MapUtils;
/*     */ import org.springframework.core.io.FileSystemResource;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.client.HttpClientErrorException;
/*     */ import org.springframework.web.client.RestClientException;
/*     */ import org.springframework.web.client.RestTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShiftLeftInlineApplication
/*     */ {
/*  38 */   private static Logger logger = Logger.getLogger(ShiftLeftInlineApplication.class.getName());
/*     */   
/*  40 */   private static RestTemplate restTemplate = new RestTemplate();
/*  41 */   private static ObjectMapper objectMapper = new ObjectMapper();
/*     */   
/*     */   public static void main(String... args) throws Exception {
/*  44 */     performShiftLeft(args);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void performShiftLeft(String[] args) throws InterruptedException, JsonProcessingException {
/*  49 */     objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
/*  50 */     ShiftLeftInlineData shiftLeftInlineData = new ShiftLeftInlineData();
/*  51 */     shiftLeftInlineData.setChanges((List<String>)Stream.<String>of(args[0].split(",", -1))
/*  52 */         .filter(s -> !s.equalsIgnoreCase("buildspec.yml"))
/*  53 */         .filter(s -> !s.equalsIgnoreCase("azure-mvc-shiftleft-pipeline.yml"))
/*  54 */         .map(s -> s.trim())
/*  55 */         .collect(Collectors.toList()));
/*  56 */     shiftLeftInlineData.setUserName(args[1]);
/*  57 */     shiftLeftInlineData.setPassword(args[2]);
/*  58 */     shiftLeftInlineData.setCloneDir(args[3]);
/*  59 */     shiftLeftInlineData.setCspName(args[4]);
/*  60 */     shiftLeftInlineData.setEnvironment(args[5]);
/*  61 */     if (args.length == 7) {
/*  62 */       shiftLeftInlineData.setBpsTenantId(args[6]);
/*     */     }
/*  64 */     if (CollectionUtils.isEmpty(shiftLeftInlineData.getChanges())) {
/*  65 */       logger.warning("Exiting, since there are no changes available to perform evaluation");
/*     */       return;
/*     */     } 
/*  68 */     updateAccessToken(shiftLeftInlineData);
/*  69 */     List<String> violatedFiles = new ArrayList<>();
/*  70 */     label50: for (String file : shiftLeftInlineData.getChanges()) {
/*  71 */       ResponseEntity<String> response = submitFileForScan(file, shiftLeftInlineData);
/*  72 */       if (Objects.isNull(response)) {
/*     */         continue;
/*     */       }
/*     */       
/*  76 */       Map<String, String> submitResponse = (Map<String, String>)objectMapper.readValue((String)response.getBody(), Map.class);
/*  77 */       String status = submitResponse.get("status");
/*  78 */       String message = submitResponse.get("message");
/*  79 */       if (status.equalsIgnoreCase("Failure"))
/*  80 */         throw new ShiftLeftInlineException(message); 
/*     */       while (true) {
/*     */         ResponseEntity<String> statusResponse;
/*  83 */         HttpHeaders requestHeaders = new HttpHeaders();
/*  84 */         requestHeaders.add("x-access-token", shiftLeftInlineData
/*  85 */             .getAccessToken());
/*  86 */         requestHeaders.setContentType(MediaType.APPLICATION_JSON);
/*  87 */         HttpEntity request = new HttpEntity((MultiValueMap)requestHeaders);
/*     */         
/*     */         try {
/*  90 */           TimeUnit.SECONDS.sleep(30L);
/*  91 */           logger.info(String.format("Checking the status for the file: %s", new Object[] { file }));
/*     */           
/*  93 */           statusResponse = restTemplate.exchange(message, HttpMethod.GET, request, String.class, new Object[0]);
/*  94 */         } catch (RestClientException e) {
/*  95 */           if (e instanceof HttpClientErrorException && ((HttpClientErrorException)e)
/*  96 */             .getRawStatusCode() == HttpStatus.UNAUTHORIZED
/*  97 */             .value()) {
/*  98 */             updateAccessToken(shiftLeftInlineData);
/*     */             continue;
/*     */           } 
/* 101 */           logger.severe(
/* 102 */               String.format("Error while getting the status of the scan for file: %s for scan with exception: %s", new Object[] { file, e }));
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 108 */         Map<String, String> scanEvalResponse = (Map<String, String>)objectMapper.readValue((String)statusResponse.getBody(), Map.class);
/*     */         
/* 110 */         if (Arrays.<String>asList(new String[] { "Submitted", "In Progress"
/* 111 */             }).contains(scanEvalResponse.get("status"))) {
/* 112 */           logger.info(scanEvalResponse.get("message")); continue;
/*     */         } 
/* 114 */         if (((String)scanEvalResponse.get("status")).equalsIgnoreCase("Failure"))
/*     */         {
/* 116 */           throw new ShiftLeftInlineException((String)scanEvalResponse
/* 117 */               .get("message"));
/*     */         }
/*     */ 
/*     */         
/* 121 */         Map<String, Object> violationDetails = (Map<String, Object>)objectMapper.convertValue(scanEvalResponse.get("message"), Map.class);
/*     */         
/* 123 */         Integer violationCount = (Integer)violationDetails.get("violation_count");
/*     */ 
/*     */         
/* 126 */         List<String> policiesViolated = (List<String>)violationDetails.get("policies_violated");
/* 127 */         String file_name = violationDetails.get("file_name").toString();
/* 128 */         if (violationCount.intValue() > 0) {
/* 129 */           violatedFiles.add(file_name);
/* 130 */           String msg = String.format("%s violations were found for the file: %s. Violated the policies: %s", new Object[] { violationCount, file_name, policiesViolated });
/*     */ 
/*     */           
/* 133 */           logger.severe(msg); continue label50;
/*     */         } 
/* 135 */         if (violationCount.intValue() == 0) {
/* 136 */           logger.info(String.format("No violations were found for the file: %s", new Object[] { file_name }));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 143 */     if (CollectionUtils.isNotEmpty(violatedFiles))
/* 144 */       throw new ShiftLeftInlineException(
/* 145 */           String.format("Failing the build, since violations were found for the files: %s.", new Object[] {
/* 146 */               Joiner.on(", ").join(violatedFiles)
/*     */             })); 
/*     */   }
/*     */   
/*     */   private static void updateAccessToken(ShiftLeftInlineData shiftLeftInlineData) {
/* 151 */     HttpHeaders headers = new HttpHeaders();
/* 152 */     headers.setContentType(MediaType.APPLICATION_JSON);
/* 153 */     headers.add("x-auth-username", shiftLeftInlineData.getUserName());
/* 154 */     headers.add("x-auth-password", shiftLeftInlineData.getPassword());
/* 155 */     if (!StringUtils.isEmpty(shiftLeftInlineData.getBpsTenantId())) {
/* 156 */       headers.add("BPS-TENANT-ID", shiftLeftInlineData.getBpsTenantId());
/*     */     }
/* 158 */     HttpEntity request = new HttpEntity((MultiValueMap)headers);
/* 159 */     ResponseEntity<String> response = null;
/*     */     try {
/* 161 */       String url = shiftLeftInlineData.getEnvironment() + "/neo/neo-auth-service/oauth/token?grant_type=password";
/* 162 */       logger.info(url);
/* 163 */       response = restTemplate.exchange(url, HttpMethod.POST, request, String.class, new Object[0]);
/* 164 */     } catch (RestClientException e) {
/*     */       
/* 166 */       String errorMsg = String.format("Unable to fetch access token for user: %s", new Object[] { shiftLeftInlineData.getUserName() });
/* 167 */       logger.severe(errorMsg);
/* 168 */       throw new ShiftLeftInlineException(errorMsg);
/*     */     } 
/* 170 */     if (response == null || response.getStatusCode() != HttpStatus.OK) {
/*     */       
/* 172 */       String errorMsg = String.format("Unable to fetch access token for user: %s", new Object[] { shiftLeftInlineData.getUserName() });
/* 173 */       logger.severe(errorMsg);
/* 174 */       throw new ShiftLeftInlineException(errorMsg);
/*     */     } 
/* 176 */     Map<String, String> tokenResponse = null;
/*     */     try {
/* 178 */       tokenResponse = (Map<String, String>)objectMapper.readValue((String)response.getBody(), Map.class);
/* 179 */     } catch (IOException e) {
/* 180 */       String errorMsg = String.format("Unable to parse the access token for user: %s", new Object[] { shiftLeftInlineData
/* 181 */             .getUserName() });
/* 182 */       logger.severe(errorMsg);
/* 183 */       throw new ShiftLeftInlineException(errorMsg);
/*     */     } 
/* 185 */     if (MapUtils.isNotEmpty(tokenResponse)) {
/* 186 */       shiftLeftInlineData.setAccessToken(tokenResponse.get("access_token"));
/*     */       
/* 188 */       logger.info(
/* 189 */           String.format("Successfully received the access token for user: %s", new Object[] {
/* 190 */               shiftLeftInlineData.getUserName()
/*     */             }));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ResponseEntity<String> submitFileForScan(String fileName, ShiftLeftInlineData shiftLeftInlineData) throws InterruptedException {
/* 196 */     HttpHeaders headers = new HttpHeaders();
/* 197 */     headers.setContentType(MediaType.MULTIPART_FORM_DATA);
/* 198 */     headers.add("x-access-token", shiftLeftInlineData
/* 199 */         .getAccessToken());
/* 200 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 201 */     File file = new File(shiftLeftInlineData.getCloneDir() + File.separator + fileName);
/*     */ 
/*     */     
/* 204 */     linkedMultiValueMap.add("templateFile", new FileSystemResource(file));
/* 205 */     HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(linkedMultiValueMap, (MultiValueMap)headers);
/*     */     
/* 207 */     ResponseEntity<String> response = null;
/*     */     try {
/* 209 */       response = restTemplate.postForEntity(shiftLeftInlineData
/* 210 */           .getEnvironment() + "/neo/config-audit/devops/v1/scan?service=" + shiftLeftInlineData
/* 211 */           .getCspName(), requestEntity, String.class, new Object[0]);
/*     */     }
/* 213 */     catch (RestClientException e) {
/* 214 */       if (e instanceof HttpClientErrorException && ((HttpClientErrorException)e)
/* 215 */         .getRawStatusCode() == HttpStatus.UNAUTHORIZED
/* 216 */         .value()) {
/* 217 */         updateAccessToken(shiftLeftInlineData);
/* 218 */         return submitFileForScan(fileName, shiftLeftInlineData);
/*     */       } 
/* 220 */       logger.severe(
/* 221 */           String.format("Error while submitting the file: %s for scan due to : %s", new Object[] {
/* 222 */               fileName, e.getMessage() }));
/* 223 */       throw e;
/*     */     } 
/* 225 */     if (response != null && response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
/* 226 */       TimeUnit.MINUTES.sleep(1L);
/* 227 */       return submitFileForScan(fileName, shiftLeftInlineData);
/*     */     } 
/* 229 */     logger.info(String.format("Successfully submitted the file: %s for scan", new Object[] { fileName }));
/* 230 */     return response;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\shn\shiftleft\ShiftLeftInlineApplication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */