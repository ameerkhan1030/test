package com.example.test.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class WebServiceUtils.
 */

@Component
@Getter
@Setter
@NoArgsConstructor
public class WebServiceUtils {

	public class HeaderInfo {
		/** The server url with endpoint */
		private String url;

		/** The query params for url */
		private Map<String, String> queryParams;

		/** The url/path params for url */
		private Map<String, String> urlParams;

		/** The request headers */
		private HttpHeaders httpHeaders;

		private Map<String, String> headers;

		public String buildUrl() {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
			if (queryParams == null || queryParams.isEmpty()) {
				url = builder.toUriString();
			} else {
				for (Map.Entry<String, String> entry : queryParams.entrySet()) {
					builder.queryParam(entry.getKey(), entry.getValue());
				}
			}
			if (urlParams == null || urlParams.isEmpty()) {
				url = builder.toUriString();
			} else {
				url = builder.buildAndExpand(urlParams).toUriString();
			}
			String[] split = url.split(":");
			String service = split[1].replace("//", "");
			return url;
		}

		public HttpHeaders getHttpHeaders() {
			return httpHeaders;
		}

		public void setHttpHeaders(HttpHeaders httpHeaders) {
			this.httpHeaders = httpHeaders;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}

		/**
		 * Adding Headers
		 * 
		 * @param headers
		 * @return
		 */
		public void addHeaders(final Map<String, String> headers) {
			HttpHeaders httpHeader = new HttpHeaders();
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpHeader.add(header.getKey(), header.getValue());
			}
			this.httpHeaders = httpHeader;
		}
	}

	/** The server url with endpoint */
	private String url;

	/** The query params for url */
	private Map<String, String> queryParams;

	/** The url/path params for url */
	private Map<String, String> urlParams;

	/** The request headers */
	private HttpHeaders httpHeaders;

	private Map<String, String> headers;

	@Autowired @Lazy
	private RestTemplate restTemplate;


	/**
	 * HTTP POST method
	 * 
	 * @param         <T> The request object type
	 * @param         <V> The response object type
	 * @param payload the request details
	 * @return the V type response
	 */
	public <T, V> V post(final T payload, final Class<V> responseType, final HeaderInfo headerInfo) {
		return restTemplate.postForObject(headerInfo.buildUrl(), new HttpEntity<T>(payload, headerInfo.getHttpHeaders()), responseType);
	}

	/**
	 * @param payload      POST a string or text
	 * @param responseType the response type from the server
	 * @return the response object
	 */
	public <V> V postStr(final String payload, final Class<V> responseType, final HeaderInfo headerInfo) {
		return restTemplate.postForObject(headerInfo.buildUrl(), new HttpEntity<String>(payload, headerInfo.getHttpHeaders()), responseType);
	}

	/**
	 * HTTP GET request with path and query params
	 * 
	 * @param responseType The API response type
	 * @return The API response
	 */
	public <V> V get(final Class<V> responseType, final HeaderInfo headerInfo) {
		return restTemplate.exchange(headerInfo.buildUrl(), HttpMethod.GET, new HttpEntity<Object>(headerInfo.getHttpHeaders()), responseType,
				new HashMap<String, Object>()).getBody();
	}

	public <V> V delete(final Class<V> responseType, final HeaderInfo headerInfo) {
		return restTemplate.exchange(headerInfo.buildUrl(), HttpMethod.DELETE, new HttpEntity<Object>(headerInfo.getHttpHeaders()), responseType,
				new HashMap<String, Object>()).getBody();
	}

	/**
	 * HTTP PUT request with path and query params
	 * 
	 * @param payload
	 * @param responseType
	 * @return The API response
	 */
	public <T, V> V put(final T payload, final Class<V> responseType, final HeaderInfo headerInfo) {
		return restTemplate.exchange(headerInfo.buildUrl(), HttpMethod.PUT, new HttpEntity<Object>(payload, headerInfo.getHttpHeaders()),
				responseType, new HashMap<String, Object>()).getBody();
	}

}
