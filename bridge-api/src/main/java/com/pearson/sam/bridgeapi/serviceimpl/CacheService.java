package com.pearson.sam.bridgeapi.serviceimpl;

 import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

//@Service
public class CacheService {
  private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

  private static final String ALL = "*";

//  private RedisTemplate<String, Object> redisTemplate;
//  private HashOperations<String, String, Object> hashOperations;
//
//  @Autowired
//  public CacheService(RedisTemplate<String, Object> redisTemplate) {
//    this.redisTemplate = redisTemplate;
//  }
//
//  @PostConstruct
//  private void init() {
//    hashOperations = redisTemplate.opsForHash();
//  }
//
//  public void clearAll() {
//    hashOperations.entries(ALL).clear();
//  }
//
//  private void storeObjectInCache(Object obj, String sessionId, String identifier, String key) {
//    hashOperations.putIfAbsent(sessionId + key, identifier, obj);
//  }
//
//  private Object getObjectFromCache(String sessionId, String identifier, String key) {
//    if (hashOperations.hasKey(sessionId + key, identifier)) {
//      return hashOperations.get(sessionId + key, identifier);
//    }
//    return null;
//  }
//
//  public Object getObjectWithSessionId(Object obj, String sessionId, String identifier, String key,
//      RetrieveCurrentModel model) {
//    Object objFromCache = this.getObjectFromCache(sessionId, identifier, key);
//    if (objFromCache != null) {
//      return objFromCache;
//    }
//
//    Object retrievedCache = model.retrieveCurrentModel(obj);
//    this.storeObjectInCache(retrievedCache, sessionId, identifier, key);
//    return retrievedCache;
//  }
//
//  public void update(Object obj, String identifier, String objectKey,RetrieveCurrentModel model) {
//    Object retrievedObj = model.retrieveCurrentModel(obj);
//    Set<String> allKeys = redisTemplate.keys(ALL+objectKey);
//    //logger.info("redis: updating below keys :  {}", allKeys);
//    allKeys.parallelStream().forEach(key->hashOperations.put(key, identifier, retrievedObj));
//  }
//
//  
//  public void clearCache(String sessionId) {
//     hashOperations.entries(sessionId+ALL).clear();
//    //logger.info("redis: clearing related objects");
//  }


}