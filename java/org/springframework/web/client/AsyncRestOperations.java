package org.springframework.web.client;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;

public interface AsyncRestOperations {
  RestOperations getRestOperations();
  
  <T> ListenableFuture<ResponseEntity<T>> getForEntity(String paramString, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> getForEntity(String paramString, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> getForEntity(URI paramURI, Class<T> paramClass) throws RestClientException;
  
  ListenableFuture<HttpHeaders> headForHeaders(String paramString, Object... paramVarArgs) throws RestClientException;
  
  ListenableFuture<HttpHeaders> headForHeaders(String paramString, Map<String, ?> paramMap) throws RestClientException;
  
  ListenableFuture<HttpHeaders> headForHeaders(URI paramURI) throws RestClientException;
  
  ListenableFuture<URI> postForLocation(String paramString, HttpEntity<?> paramHttpEntity, Object... paramVarArgs) throws RestClientException;
  
  ListenableFuture<URI> postForLocation(String paramString, HttpEntity<?> paramHttpEntity, Map<String, ?> paramMap) throws RestClientException;
  
  ListenableFuture<URI> postForLocation(URI paramURI, HttpEntity<?> paramHttpEntity) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> postForEntity(String paramString, HttpEntity<?> paramHttpEntity, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> postForEntity(String paramString, HttpEntity<?> paramHttpEntity, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> postForEntity(URI paramURI, HttpEntity<?> paramHttpEntity, Class<T> paramClass) throws RestClientException;
  
  ListenableFuture<?> put(String paramString, HttpEntity<?> paramHttpEntity, Object... paramVarArgs) throws RestClientException;
  
  ListenableFuture<?> put(String paramString, HttpEntity<?> paramHttpEntity, Map<String, ?> paramMap) throws RestClientException;
  
  ListenableFuture<?> put(URI paramURI, HttpEntity<?> paramHttpEntity) throws RestClientException;
  
  ListenableFuture<?> delete(String paramString, Object... paramVarArgs) throws RestClientException;
  
  ListenableFuture<?> delete(String paramString, Map<String, ?> paramMap) throws RestClientException;
  
  ListenableFuture<?> delete(URI paramURI) throws RestClientException;
  
  ListenableFuture<Set<HttpMethod>> optionsForAllow(String paramString, Object... paramVarArgs) throws RestClientException;
  
  ListenableFuture<Set<HttpMethod>> optionsForAllow(String paramString, Map<String, ?> paramMap) throws RestClientException;
  
  ListenableFuture<Set<HttpMethod>> optionsForAllow(URI paramURI) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> exchange(String paramString, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> exchange(String paramString, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> exchange(URI paramURI, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, Class<T> paramClass) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> exchange(String paramString, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference, Object... paramVarArgs) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> exchange(String paramString, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ListenableFuture<ResponseEntity<T>> exchange(URI paramURI, HttpMethod paramHttpMethod, HttpEntity<?> paramHttpEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference) throws RestClientException;
  
  <T> ListenableFuture<T> execute(String paramString, HttpMethod paramHttpMethod, AsyncRequestCallback paramAsyncRequestCallback, ResponseExtractor<T> paramResponseExtractor, Object... paramVarArgs) throws RestClientException;
  
  <T> ListenableFuture<T> execute(String paramString, HttpMethod paramHttpMethod, AsyncRequestCallback paramAsyncRequestCallback, ResponseExtractor<T> paramResponseExtractor, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ListenableFuture<T> execute(URI paramURI, HttpMethod paramHttpMethod, AsyncRequestCallback paramAsyncRequestCallback, ResponseExtractor<T> paramResponseExtractor) throws RestClientException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\client\AsyncRestOperations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */