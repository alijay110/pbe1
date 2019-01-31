package com.pearson.sam.bridgeapi.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  public BeanUtil() {
    super();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    BeanUtil.initAppContxt(applicationContext);
  }

  public static void initAppContxt(ApplicationContext applicationContext) {
    BeanUtil.applicationContext = applicationContext;    
  }
  
  public static <T> T getBean(Class<T> clzz) {
    return BeanUtil.applicationContext.getBean(clzz);
  }

  public static <T> T getBean(String beanName, Class<T> clzz) {
    return BeanUtil.applicationContext.getBean(beanName, clzz);
  }
}
