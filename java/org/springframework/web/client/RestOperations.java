package org.springframework.web.client;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

public interface RestOperations {
  <T> T getForObject(String paramString, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> T getForObject(String paramString, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> T getForObject(URI paramURI, Class<T> paramClass) throws RestClientException;
  
  <T> ResponseEntity<T> getForEntity(String paramString, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> ResponseEntity<T> getForEntity(String paramString, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ResponseEntity<T> getForEntity(URI paramURI, Class<T> paramClass) throws RestClientException;
  
  HttpHeaders headForHeaders(String paramString, Object... paramVarArgs) throws RestClientException;
  
  HttpHeaders headForHeaders(String paramString, Map<String, ?> paramMap) throws RestClientException;
  
  HttpHeaders headForHeaders(URI paramURI) throws RestClientException;
  
  URI postForLocation(String paramString, Object paramObject, Object... paramVarArgs) throws RestClientException;
  
  URI postForLocation(String paramString, Object paramObject, Map<String, ?> paramMap) throws RestClientException;
  
  URI postForLocation(URI paramURI, Object paramObject) throws RestClientException;
  
  <T> T postForObject(String paramString, Object paramObject, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> T postForObject(String paramString, Object paramObject, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> T postForObject(URI paramURI, Object paramObject, Class<T> paramClass) throws RestClientException;
  
  <T> ResponseEntity<T> postForEntity(String paramString, Object paramObject, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> ResponseEntity<T> postForEntity(String paramString, Object paramObject, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ResponseEntity<T> postForEntity(URI paramURI, Object paramObject, Class<T> paramClass) throws RestClientException;
  
  void put(String paramString, Object paramObject, Object... paramVarArgs) throws RestClientException;
  
  void put(String paramString, Object paramObject, Map<String, ?> paramMap) throws RestClientException;
  
  void put(URI paramURI, Object paramObject) throws RestClientException;
  
  <T> T patchForObject(String paramString, Object paramObject, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> T patchForObject(String paramString, Object paramObject, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> T patchForObject(URI paramURI, Object paramObject, Class<T> paramClass) throws RestClientException;
  
  void delete(String paramString, Object... paramVarArgs) throws RestClientException;
  
  void delete(String paramString, Map<String, ?> paramMap) throws RestClientException;
  
  void delete(URI paramURI) throws RestClientException;
  
  Set<HttpMethod> optionsForAllow(String paramString, Object... paramVarArgs) throws RestClientException;
  
  Set<HttpMethod> optionsForAllow(String paramString, Map<String, ?> paramMap) throws RestClientException;
  
  Set<HttpMethod> optionsForAllow(URI paramURI) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(String paramString, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(String paramString, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(URI paramURI, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, Class<T> paramClass) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(String paramString, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference, Object... paramVarArgs) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(String paramString, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(URI paramURI, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(RequestEntity<?> paramRequestEntity, Class<T> paramClass) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(RequestEntity<?> paramRequestEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference) throws RestClientException;
  
  <T> T execute(String paramString, HttpMethod paramHttpMethod, RequestCallback paramRequestCallback, ResponseExtractor<T> paramResponseExtractor, Object... paramVarArgs) throws RestClientException;
  
  <T> T execute(String paramString, HttpMethod paramHttpMethod, RequestCallback paramRequestCallback, ResponseExtractor<T> paramResponseExtractor, Map<String, ?> paramMap) throws RestClientException;
  
  <T> T execute(URI paramURI, HttpMethod paramHttpMethod, RequestCallback paramRequestCallback, ResponseExtractor<T> paramResponseExtractor) throws RestClientException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\RestOperations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */