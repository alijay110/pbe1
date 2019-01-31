package com.pearson.sam.bridgeapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.session.data.redis.config.ConfigureRedisAction;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dinesh Metkari
 * 
 */
//@Configuration
//@EnableRedisHttpSession
public class RedisConf {
	@Value("${redis.hostName}")
	private String hostName;

	@Value("${redis.port}")
	private int port;

	@Value("${redis.password}")
	private String password;

	@Value("${redis.timeout}")
	private int timeout;
	
//	@Bean
//	public static ConfigureRedisAction configureRedisAction() {
//		return ConfigureRedisAction.NO_OP;
//	}
//	
//    @Bean
//    public JedisPoolConfig jedisPoolConfig(){
//    	System.out.println("////////////@@@@@@ inside jedisPoolConfig:");
//      System.out.println("////////////@@@@@@ host:"+hostName);
//      System.out.println("////////////@@@@@@ port:"+port);
//      System.out.println("////////////@@@@@@ password:"+password);
//    	
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxTotal(30);
//        jedisPoolConfig.setMaxIdle(30);
//        jedisPoolConfig.setMinIdle(10);
//        jedisPoolConfig.setNumTestsPerEvictionRun(1024);
//        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
//        jedisPoolConfig.setMinEvictableIdleTimeMillis(1800000);
//        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(10000);
//        jedisPoolConfig.setMaxWaitMillis(15000);
//        jedisPoolConfig.setTestOnBorrow(true);
//        jedisPoolConfig.setTestWhileIdle(true);
//        jedisPoolConfig.setBlockWhenExhausted(false);
//        System.out.println("////////////####  outside redisTemplate:" + jedisPoolConfig);
//        return jedisPoolConfig;
//    }
//
//    /***********************单机redis*************************/
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory() {
//    	System.out.println("////////////@@@@@@ inside JedisConnectionFactory:");
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        //jedisConnectionFactory.setHostName("172.30.1.41");
//        jedisConnectionFactory.setHostName(hostName);
//        //jedisConnectionFactory.setHostName("redispoc.o78mko.ng.0001.apse1.cache.amazonaws.com");
//        jedisConnectionFactory.setPort(port);
//        jedisConnectionFactory.afterPropertiesSet();
//        jedisConnectionFactory.setDatabase(0);
//        System.out.println("////////#### outside JedisConnectionFactory:" + jedisConnectionFactory.getHostName() + ":" + jedisConnectionFactory.toString());
//        return jedisConnectionFactory;
//    }
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory redisConnectionFactory) {
//    	System.out.println("////////////@@@@@@ inside redisTemplate:");
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.afterPropertiesSet();
//        System.out.println("////////////####  outside redisTemplate:" + redisTemplate);
//        return redisTemplate;
//    }
//    /***********************单机redis结束*************************/
//
//    /***********************集群redis*************************/
//    @Bean
//    public JedisCluster jedisCluster(){
//    	System.out.println("////////////@@@@@@ inside jedisCluster:");
//        Set<HostAndPort> hostAndPortSet = new HashSet<HostAndPort>();
//        hostAndPortSet.add(new HostAndPort(hostName, 7001));
//        hostAndPortSet.add(new HostAndPort(hostName, 7002));
//        hostAndPortSet.add(new HostAndPort(hostName, 7002));
//        hostAndPortSet.add(new HostAndPort(hostName, 7003));
//        hostAndPortSet.add(new HostAndPort(hostName, 7004));
//        hostAndPortSet.add(new HostAndPort(hostName, 7005));
//        hostAndPortSet.add(new HostAndPort(hostName, 7006));
//        JedisCluster jedisCluster = new JedisCluster(hostAndPortSet, jedisPoolConfig());
//        System.out.println("////////////####  outside jedisCluster:" + jedisCluster);
//        return jedisCluster;
//    }
//    /***********************集群redis结束*************************/

}