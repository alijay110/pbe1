package com.pearson.sam.bridgeapi.util;

import java.text.Normalizer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PropertyHolder {

  public String getEmptyString() {
    return StringUtils.EMPTY;
  }
  
  public static Integer getIntegerProperty(String name) {
    return getPropertyValue(name, Integer.class) != null ? getPropertyValue(name, Integer.class)
        : null;
  }

  public static Boolean getBooleanProperty(String name) {
    return getPropertyValue(name, Boolean.class) != null ? getPropertyValue(name, Boolean.class)
        : null;
  }

  
  /**
   * get String Property.
   * @param name input
   * @return String
   */
  public static String getStringProperty(String name) {
    if (ObjectUtils.isEmpty(getPropertyValue(name, String.class))) {
      return "";
    }
    return Normalizer.normalize(getPropertyValue(name, String.class), Normalizer.Form.NFKC);
  }

  private static <T> T getPropertyValue(String name, Class<T> clazz) {
    return BeanUtil.getBean(Environment.class).getProperty(name, clazz);
  }
}
