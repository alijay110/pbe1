package com.pearson.sam.bridgeapi.config;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GUEST_OPERATIONS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.QUERIES_INFO;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SPEC_OPERATIONS_TO_QUERY;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.vanroy.springdata.jest.JestElasticsearchTemplate;
import com.pearson.sam.bridgeapi.util.Utils;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.servlet.Filter;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class AppConfig {

	@Value("${elastic.protocol}")
	private String protocol;

	@Value("${spring.data.jest.uri}")
	private String elasticUri;

	/**
	 * loadRoleInfo.
	 * 
	 * @param rolesPath
	 *            path
	 * @throws IOException
	 *             e
	 */
	@Bean
	@Qualifier(QUERIES_INFO)
	public Map<String, Set<String>> loadRoleInfo(@Value(value = "${ROLES_PATH}") String rolesPath,
			@Value("${SPEC_FILE}") String specJsonFile) throws IOException {
		return Utils.convert(Collections.unmodifiableMap(Utils.transformJsonAsMap(
				Utils.convert(Utils.parse(rolesPath, Map.class), new TypeReference<Map<String, Object>>() {
				}), SPEC_OPERATIONS_TO_QUERY, specJsonFile)), new TypeReference<Map<String, Set<String>>>() {
				});

	}

	@Bean
	@Qualifier(GUEST_OPERATIONS)
	public Set<String> getGuestQueries(@Qualifier(QUERIES_INFO) Map<String, Set<String>> loadRoleInfo)
			throws IOException {
		return loadRoleInfo.getOrDefault(GUEST_OPERATIONS, Collections.emptySet());
	}

//	@Bean
//	JedisConnectionFactory jedisConnectionFactory() {
//		return new JedisConnectionFactory();
//	}
//
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate() {
//		final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//		template.setConnectionFactory(jedisConnectionFactory());
//		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
//		return template;
//	}

	@SuppressWarnings("deprecation")
	@Bean
	public JestClient client()
			throws UnknownHostException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		// trust ALL certificates
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			@Override
			public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				return true;
			}
		}).build();

		// skip hostname checks
		HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
		SchemeIOSessionStrategy httpsIOSessionStrategy = new SSLIOSessionStrategy(sslContext, hostnameVerifier);

		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(elasticUri).defaultSchemeForDiscoveredNodes(protocol) // required,otherwise
																														// uses
																														// http
				.sslSocketFactory(sslSocketFactory) // this only affectssync calls
				.multiThreaded(true).httpsIOSessionStrategy(httpsIOSessionStrategy) // this only affects async calls
				.build());
		return factory.getObject();
	}

	@Bean
	public JestElasticsearchTemplate elasticsearchTemplate()
			throws UnknownHostException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		return new JestElasticsearchTemplate(client());
	}

	@Bean
	public Filter TracingFilter() {
		return new AWSXRayServletFilter("pp2-microservices01");
	}

}
