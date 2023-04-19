/*     */ package org.springframework.http;
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
/*     */ public enum HttpStatus
/*     */ {
/*  40 */   CONTINUE(100, "Continue"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   SWITCHING_PROTOCOLS(101, "Switching Protocols"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   PROCESSING(102, "Processing"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   CHECKPOINT(103, "Checkpoint"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   OK(200, "OK"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   CREATED(201, "Created"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   ACCEPTED(202, "Accepted"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   NO_CONTENT(204, "No Content"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   RESET_CONTENT(205, "Reset Content"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   PARTIAL_CONTENT(206, "Partial Content"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   MULTI_STATUS(207, "Multi-Status"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   ALREADY_REPORTED(208, "Already Reported"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   IM_USED(226, "IM Used"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   MULTIPLE_CHOICES(300, "Multiple Choices"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   MOVED_PERMANENTLY(301, "Moved Permanently"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   FOUND(302, "Found"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   MOVED_TEMPORARILY(302, "Moved Temporarily"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   SEE_OTHER(303, "See Other"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   NOT_MODIFIED(304, "Not Modified"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   USE_PROXY(305, "Use Proxy"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   TEMPORARY_REDIRECT(307, "Temporary Redirect"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   PERMANENT_REDIRECT(308, "Permanent Redirect"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   BAD_REQUEST(400, "Bad Request"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   UNAUTHORIZED(401, "Unauthorized"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   PAYMENT_REQUIRED(402, "Payment Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   FORBIDDEN(403, "Forbidden"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   NOT_FOUND(404, "Not Found"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   NOT_ACCEPTABLE(406, "Not Acceptable"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 204 */   PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   REQUEST_TIMEOUT(408, "Request Timeout"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 214 */   CONFLICT(409, "Conflict"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 219 */   GONE(410, "Gone"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 224 */   LENGTH_REQUIRED(411, "Length Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 229 */   PRECONDITION_FAILED(412, "Precondition Failed"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 235 */   PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 241 */   REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   URI_TOO_LONG(414, "URI Too Long"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 254 */   REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 260 */   UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 265 */   REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 270 */   EXPECTATION_FAILED(417, "Expectation Failed"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 275 */   I_AM_A_TEAPOT(418, "I'm a teapot"),
/*     */ 
/*     */ 
/*     */   
/* 279 */   INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 284 */   METHOD_FAILURE(420, "Method Failure"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   DESTINATION_LOCKED(421, "Destination Locked"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 295 */   UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 300 */   LOCKED(423, "Locked"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 305 */   FAILED_DEPENDENCY(424, "Failed Dependency"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 310 */   UPGRADE_REQUIRED(426, "Upgrade Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 315 */   PRECONDITION_REQUIRED(428, "Precondition Required"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 320 */   TOO_MANY_REQUESTS(429, "Too Many Requests"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 325 */   REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 332 */   UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 340 */   INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 345 */   NOT_IMPLEMENTED(501, "Not Implemented"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 350 */   BAD_GATEWAY(502, "Bad Gateway"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 355 */   SERVICE_UNAVAILABLE(503, "Service Unavailable"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 360 */   GATEWAY_TIMEOUT(504, "Gateway Timeout"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 365 */   HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 370 */   VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 375 */   INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 380 */   LOOP_DETECTED(508, "Loop Detected"),
/*     */ 
/*     */ 
/*     */   
/* 384 */   BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 389 */   NOT_EXTENDED(510, "Not Extended"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 394 */   NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");
/*     */ 
/*     */   
/*     */   private final int value;
/*     */   
/*     */   private final String reasonPhrase;
/*     */ 
/*     */   
/*     */   HttpStatus(int value, String reasonPhrase) {
/* 403 */     this.value = value;
/* 404 */     this.reasonPhrase = reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int value() {
/* 412 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReasonPhrase() {
/* 419 */     return this.reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is1xxInformational() {
/* 428 */     return Series.INFORMATIONAL.equals(series());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is2xxSuccessful() {
/* 437 */     return Series.SUCCESSFUL.equals(series());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is3xxRedirection() {
/* 446 */     return Series.REDIRECTION.equals(series());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is4xxClientError() {
/* 456 */     return Series.CLIENT_ERROR.equals(series());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is5xxServerError() {
/* 465 */     return Series.SERVER_ERROR.equals(series());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Series series() {
/* 473 */     return Series.valueOf(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 481 */     return Integer.toString(this.value);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Series
/*     */   {
/* 507 */     INFORMATIONAL(1),
/* 508 */     SUCCESSFUL(2),
/* 509 */     REDIRECTION(3),
/* 510 */     CLIENT_ERROR(4),
/* 511 */     SERVER_ERROR(5);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     Series(int value) {
/* 516 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int value() {
/* 523 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\HttpStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */