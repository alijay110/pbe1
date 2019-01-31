package com.pearson.sam.bridgeapi.resthelper;

import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.SAM_API_KEY;
import static com.pearson.sam.bridgeapi.samservices.constants.SamEndPointConstants.SAM_BASE_URL;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.pearson.sam.bridgeapi.util.PropertyHolder;

/**
 * rest req builder.
 * 
 * @author VKuma09
 *
 */
public class RestRequestBuilder {

	private static final Logger logger = LoggerFactory.getLogger(RestRequestBuilder.class);

	private RestClientRequest request;
	private Optional<Boolean> addDefaultHeaders = Optional.of(true);
	private Optional<MultiValueMap<String, String>> addlHeadersMap = Optional.empty();
	private Optional<Map<String, Object>> uriParams = Optional.empty();
	private Optional<MultiValueMap<String, String>> queryParams = Optional.empty();

	private Optional<String> constructedUrl = Optional.empty();
	private Optional<String> body = Optional.empty();
	
	Map<String, String> headersMap;

	public RestRequestBuilder() {
		super();
		this.request = new RestClientRequest();
	}

	/**
	 * constructor.
	 */
	public RestRequestBuilder(String service, String operation) {
		super();
		this.request = new RestClientRequest();
		this.constructedUrl = Optional.of(PropertyHolder.getStringProperty(SAM_BASE_URL)
				+ PropertyHolder.getStringProperty(service) + PropertyHolder.getStringProperty(operation));
		//withAdditionalHeaders(getSAMApiKey());
	}

	/**
	 * Use Service,Operation Constructor instead.
	 * 
	 * @deprecated Use Service,Operation Constructor.
	 * 
	 */
	@Deprecated
	public RestRequestBuilder(String constructedUrl) {
		super();
		this.request = new RestClientRequest();
		this.constructedUrl = Optional.of(constructedUrl);
	}

	public RestRequestBuilder noDefaultHeaders() {
		addDefaultHeaders = Optional.of(false);
		return this;
	}

	/**
	 * withAdditionalHeaders.
	 * 
	 * @return
	 */
	public RestRequestBuilder withAdditionalHeaders(Map<String, String> headersMap) {
		HttpHeaders requestHeaders = new HttpHeaders();
		headersMap.entrySet().forEach(e -> requestHeaders.add(e.getKey(), e.getValue()));
		this.addlHeadersMap = Optional.of(requestHeaders);
		return this;
	}

	/**
	 * addHeaders.
	 */
	public RestRequestBuilder addHeaders(String key, String value) {
		if (!addlHeadersMap.isPresent()) {
			addlHeadersMap = Optional.of(new HttpHeaders());
		}
		addlHeadersMap.get().add(key, value);
		return this;
	}

	public RestRequestBuilder withBody(String body) {
		this.body = Optional.of(body);
		return this;
	}

	private HttpHeaders addGenericHeaders(HttpHeaders requestHeaders) {
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.setExpires(1);
		requestHeaders.setPragma("no-cache");
		requestHeaders.setCacheControl("private, no-store, max-age=0, must-revalidate");
		requestHeaders.setCacheControl("private, no-store, max-age=0, must-revalidate");
		requestHeaders.set("x-api-key", PropertyHolder.getStringProperty(SAM_API_KEY));
		return requestHeaders;
	}

	private void constructUrl() {

		if (this.constructedUrl.isPresent() && (this.uriParams.isPresent() || this.queryParams.isPresent())) {
			UriComponentsBuilder builder = null;

			builder = UriComponentsBuilder.fromUriString(this.constructedUrl.get());

			if (this.uriParams.isPresent()) {
				builder = builder.uriVariables(this.uriParams.get());
			}

			if (this.queryParams.isPresent()) {
				builder = builder.queryParams(this.queryParams.get());
			}

			this.constructedUrl = Optional.of(builder.build().toString());
		}

		if (this.constructedUrl.isPresent()) {
			this.request.setConstructedUrl(this.constructedUrl.get());
		}
	}

	/**
	 * Use addUrlParams instead.
	 * 
	 * @deprecated Use addUrlParams instead.
	 */
	@Deprecated
	public RestRequestBuilder withUrlParams(Map<String, Object> urlParams) {
		this.uriParams = Optional.of(urlParams);
		return this;
	}

	/**
	 * addUrlParams.
	 */
	public RestRequestBuilder addUrlParams(String key, String value) {
		if (!this.uriParams.isPresent()) {
			this.uriParams = Optional.of(new HashMap<>());
		}
		this.uriParams.get().put(key, value);
		return this;
	}

	/**
	 * Use addQueryParams.
	 * 
	 * @deprecated Use addQueryParams.
	 */
	@Deprecated
	public RestRequestBuilder withQueryParams(Map<String, String> queryParam) {
		MultiValueMap<String, String> multiValueParams = new HttpHeaders();
		queryParam.entrySet().forEach(e -> multiValueParams.add(e.getKey(), e.getValue()));
		this.queryParams = Optional.of(multiValueParams);
		return this;
	}

	/**
	 * addUrlParams.
	 */
	public RestRequestBuilder addQueryParams(String key, String value) {
		if (!this.queryParams.isPresent()) {
			this.queryParams = Optional.of(new HttpHeaders());
		}

		if (StringUtils.isNotBlank(value)) {
			this.queryParams.get().add(key, value);
		}
		return this;
	}

	private void createRequestEntity(HttpHeaders requestHeaders) {
		if (this.body.isPresent()) {
			this.request.setRequestEntity(new HttpEntity<String>(this.body.get(), requestHeaders));
		} else {
			this.request.setRequestEntity(new HttpEntity<String>(requestHeaders));
		}
	}

	private HttpHeaders getHeaders() {
		HttpHeaders requestHeaders = new HttpHeaders();

		if (this.addDefaultHeaders.isPresent() && this.addDefaultHeaders.get()) {
			addGenericHeaders(requestHeaders);
		}

		if (this.addlHeadersMap.isPresent()) {
			requestHeaders.addAll(this.addlHeadersMap.get());
		}
		return requestHeaders;
	}

	/**
	 * build.
	 * 
	 * @return
	 */
	public RestClientRequest build() {

		HttpHeaders requestHeaders = getHeaders();

		createRequestEntity(requestHeaders);

		constructUrl();

		logger.info("Rest Request constructed: {}", this.request);

		return this.request;
	}
	
//	private Map<String, String> getSAMApiKey() {
//		headersMap = new HashMap<>();
//		headersMap.put("x-api-key", PropertyHolder.getStringProperty(SAM_API_KEY));
//		return headersMap;
//	}

}
