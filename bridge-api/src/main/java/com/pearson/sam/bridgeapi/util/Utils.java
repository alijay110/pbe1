package com.pearson.sam.bridgeapi.util;


import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

import com.bazaarvoice.jolt.Chainr;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.ChannelPartner;
import com.pearson.sam.bridgeapi.model.Holder;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.School;


public class Utils {

  private Utils() {
  }

  private static final Logger logger = LoggerFactory.getLogger(Utils.class);
  private static final ObjectMapper mapper = new ObjectMapper();
  
  private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final String ALPHABETS_STRING = "ABCDEFGHJKMNPQRSTUVWXYZ";
  private static final String AUSTRALIA_SYDNEY = "australia.sydney";
  private static final String SCHOOL = "SCHOOL";
  private static final String ORGANISATION = "ORGANISATION";
  private static final String LICENCEHOLDER = "LICENCEHOLDER";
  private static final String PRODUCT_MODEL = "productModel";

  /**
   * json mapping.
   * 
   * @param jsonString
   *          string
   * @param dataType
   *          type
   * @return
   */
  @SuppressWarnings("unchecked")
  public static JSONObject jsonMapper(String jsonString, String[] dataType) {
    JSONObject obj = null;
    JSONObject output = null;
    try {
      obj = (JSONObject) new JSONParser().parse(jsonString);
      output = new JSONObject();
      for (String data : dataType) {
        if (data.equals("password")) {
          String pwd = (String) obj.get("password");
          if (pwd != null && !pwd.isEmpty()) {
            output.put(data, pwd);
          } else {
            output.put(data, generateRandomPassword());
          }
        } else {
          output.put(data, (String) obj.get(data));
        }
      }

    } catch (ParseException e) {
      //logger.error("Parse Exception {}", e);
    }

    return output;
  }

  /**
   * generates RandomPassword.
   * 
   * @return string
   */
  public static String generateRandomPassword() {
    char[] upperAlphaCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    char[] lowerAlpahCharacters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    char[] numericCharacters = "0123456789".toCharArray();
    char[] splCharacters = "~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?".toCharArray();
    return RandomStringUtils.random(2, 0, upperAlphaCharacters.length - 1, false, false,
        upperAlphaCharacters, new SecureRandom())
        + RandomStringUtils.random(2, 0, splCharacters.length - 1, false, false, splCharacters,
            new SecureRandom())
        + RandomStringUtils.random(2, 0, numericCharacters.length - 1, false, false,
            numericCharacters, new SecureRandom())
        + RandomStringUtils.random(2, 0, lowerAlpahCharacters.length - 1, false, false,
            lowerAlpahCharacters, new SecureRandom());
  }

  /**
   * generates a RandomUserName.
   * 
   * @param firstName
   *          name
   * @return string
   */
  public static String generateRandomUserName(String firstName) {
    char[] lowerAlpahCharacters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    char[] numericCharacters = "0123456789".toCharArray();
    String userName = null;
    firstName = firstName.replace(" ", "");
    if (firstName.length() > 4) {
      userName = firstName.substring(0, 4);
    } else {
      userName = firstName + RandomStringUtils.random(4 - firstName.length(), 0,
          lowerAlpahCharacters.length - 1, false, false, lowerAlpahCharacters, new SecureRandom());
    }

    String saltStr = RandomStringUtils.random(2, 0, lowerAlpahCharacters.length - 1, false, false,
        lowerAlpahCharacters, new SecureRandom())
        + RandomStringUtils.random(2, 0, numericCharacters.length - 1, false, false,
            numericCharacters, new SecureRandom());
    userName = userName + saltStr;
    return userName;
  }

  /**
   * validates schema.
   * 
   * @param jsonString
   *          str
   * @param path
   *          path
   * @return a boolean
   * @throws IOException
   *           e
   * @throws ProcessingException
   *           e
   */
  public static boolean schemaValidator(String jsonString, String path)
      {
    JsonNode good=null;
    JsonNode fstabSchema=null;
    try {
      good = jsonStringIntoJsonNode(jsonString);
      fstabSchema = Utils.parse(path, JsonNode.class);
    } catch (IOException e1) {
      e1.printStackTrace();
      throw new BridgeApiGraphqlException(e1.getMessage());
    }
    final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
    try {
      final JsonSchema schema = factory.getJsonSchema(fstabSchema);
      ProcessingReport report;
      report = schema.validate(good);
      //logger.info("Validation: {}", report);
      if (report.isSuccess()) {
        return report.isSuccess();
      } else {
        throw new ProcessingException();
      }
    } catch (ProcessingException e) {
      e.printStackTrace();
      throw new BridgeApiGraphqlException(e.getMessage());
    }

  }

  public static JsonNode loadResource(String path) throws IOException{
    File schemaFile = new ClassPathResource(path).getFile();
    return JsonLoader.fromFile(schemaFile);
  }

  /**
   * Helper method that parses a JSON object from a resource on the classpath as an instance of the
   * provided type.
   *
   * @param resource
   *          the path to the resource (relative to this class)
   * @param clazz
   *          the type to parse the JSON into
   * @throws IOException 
   */

  public static <T> T parse(String resource, Class<T> clazz) throws IOException{
    try (InputStream stream = Utils.class.getResourceAsStream(resource)) {
      return mapper.readValue(stream, clazz);
    }
  }

  /**
   * converts jsonStringIntoJsonNode.
   * 
   * @param jsonString
   *          str
   * @return jsonnode dode
   * @throws JsonParseException
   *           e
   * @throws IOException
   *           e
   */
  public static JsonNode jsonStringIntoJsonNode(String jsonString) throws IOException{
    return mapper.readTree(jsonString);
  }

  /**
   * transforms JSON.
   * 
   * @param inputJson
   *          json
   * @param spec
   *          spc
   * @param specPath
   *          path
   * @return
   */
  @SuppressWarnings("unchecked")
  public static Object transformJson(Map<String, Object> inputJson, String spec, String specPath) {
    Map<String, Object> specJson = null;
    Object transformedOutput = null;
    //logger.info("Before transformation: {}", inputJson);
    try {
      specJson = Utils.parse(specPath, Map.class);
      Chainr chainr = Chainr.fromSpec(specJson.get(spec));
      transformedOutput = chainr.transform(inputJson);
      //logger.info("After transformation: {}", transformedOutput);
    } catch (IOException e1) {
      //logger.error("Parse Exception {}", e1);
    }

    return transformedOutput;
  }

  public static Map<String, Object> transformJsonAsMap(Map<String, Object> inputJson, String spec,
      String specPath) {
    return convertToMap(transformJson(inputJson, spec, specPath));
  }

  /**
   * extracts AnnotatedFieldNames.
   * 
   * @param classToBeExtracted
   *          cls
   * @param annotationCls
   *          annotation
   * @return
   */
  public static List<String> extractAnnotatedFieldNames(Class<?> classToBeExtracted,
      final Class<? extends Annotation> annotationCls) {

    List<Field> fieldList = FieldUtils.getFieldsListWithAnnotation(classToBeExtracted,
        annotationCls);
    List<String> returnFields = null;
    if (fieldList != null) {
      returnFields = new ArrayList<>();
      for (Field f : fieldList) {
        returnFields.add(f.getName());
      }
    }
    return returnFields;
  }

  /**
   * Copies properties from one object to another.
   * 
   * @param source
   *          src
   * @destination
   * @return
   */
  public static void copyNonNullProperties(Object source, Object destination) {
    try {
    BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
    //logger.info("copied src into " + source+" /n dest "+destination);
    }catch (Exception e) {
      //logger.error("ERROR: ",e);
    }
  }

  /**
   * Returns an array of null properties of an object.
   * 
   * @param source
   *          src
   * @return
   */
  private static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();
    for (java.beans.PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }
    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  /**
   * Returns an boolean after comparing .
   * 
   * @return boolean
   */

  public static boolean roleExists(List<String> panelRoles, Set<String> roles) {
    Set<String> panelRoleSet = panelRoles.parallelStream().collect(Collectors.toSet());
    return panelRoleSet.stream().anyMatch(roles::contains);
  }

  /**
   * matchMap1KeysNMap2Values.
   * 
   * @param map1
   *          m1
   * @param map2
   *          m2
   * @return retMap
   */
  public static Map<String, Set<String>> matchMap1KeysNMap2Values(Map<String, String> map1,
      Map<String, Set<String>> map2) {

    return map1.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> map2.get(e.getValue())));
  }

  public static Map<String, Object> convertToMap(Object obj) {
    return convert(obj, new TypeReference<Map<String, Object>>() {
    });
  }

  public static <T> T convert(Object obj, TypeReference<T> type) {
	  mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    return mapper.convertValue(obj, type);
  }
  
  public static int batchSizeCount(int actualSize) {
	  //int defaultBatchSize = PropertyHolder.getIntegerProperty(DATA_LOADER_BATCH_SIZE);
	  int defaultBatchSize = 50;
	  return defaultBatchSize < actualSize ? defaultBatchSize : actualSize;
  }

  public static int generateRandomNumber(int min, int max){
		Random rnd = new Random();
		return min + rnd.nextInt(max);
	}
	
  public static String generateRandomAlphaNumeric(int count) {
	  StringBuilder builder = new StringBuilder();
	  while (count-- != 0) {
		  int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
		  builder.append(ALPHA_NUMERIC_STRING.charAt(character));
	  }
	  return builder.toString();
   }
  
  /**
   * randomAlphabet.
   * 
   * @return
   */
  public static String generateRandomAlphabet(int count) {
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
        int character = (int) (Math.random() * ALPHABETS_STRING.length());
        builder.append(ALPHABETS_STRING.charAt(character));
    }
    return builder.toString();
  }
  
  public static boolean isEmpty(String str) {
	  return str == null || str.trim().isEmpty();
  }
  
  public static boolean isNotEmpty(String str) {
	  return !isEmpty(str);
  }
  
  /**
   * Generate Epoch Date
   * 
   * @return
   */
  public static String generateEpochDate() {
	  DateTime date = new DateTime(DateTimeZone.forID(PropertyHolder.getStringProperty(AUSTRALIA_SYDNEY)));
	  return String.valueOf(date.getMillis());
  }
  
	public static LicenceHolder getLicenceHolder(LicenceHolder licenceHolder, String operation) {
		Holder holder = null;
		if (operation.equals(SCHOOL)) {
			if (null == licenceHolder.getHolder()) {
				holder = new Holder();
				holder.setSchool(new School());
				licenceHolder.setHolder(holder);
			} else if (null == licenceHolder.getHolder().getSchool()) {
				licenceHolder.getHolder().setSchool(new School());
			}
		}

		if (operation.equals(ORGANISATION)) {
			if (null == licenceHolder.getHolder()) {
				holder = new Holder();
				holder.setOrganisation(new ChannelPartner());
				licenceHolder.setHolder(holder);
			} else if (null == licenceHolder.getHolder().getOrganisation()) {
				licenceHolder.getHolder().setOrganisation(new ChannelPartner());
			}
		}
		
		if (operation.equals(LICENCEHOLDER)) {
			if (null == licenceHolder.getHolder()) {
				holder = new Holder();
				holder.setOrganisation(new ChannelPartner());
				holder.setSchool(new School());
				holder.setProduct(new Product());
				licenceHolder.setHolder(holder);
			}else {
				holder = licenceHolder.getHolder();
				if(null == licenceHolder.getHolder().getSchool()) {
					holder.setSchool(new School());
					licenceHolder.setHolder(holder);
				}
				if(null == licenceHolder.getHolder().getOrganisation()) {
					holder.setOrganisation(new ChannelPartner());
					licenceHolder.setHolder(holder);
				}
				if(null == licenceHolder.getHolder().getProduct()) {
					holder.setProduct(new Product());
					licenceHolder.setHolder(holder);
				}
			}
		}
		return licenceHolder;
	}

	/**
	 * Generate Error Message
	 * 
	 * @return String
	 */
	public static String errorMsg(String message) {
		return PropertyHolder.getStringProperty(message);
	}
	
	/**
	   * Get ProductModel
	   * 
	   * @return
	   */
    public static String getProductModel() {
	  return PropertyHolder.getStringProperty(PRODUCT_MODEL);
    }
    
    public static BoolQueryBuilder getBoolQueryBuilder(Object obj){
    	BoolQueryBuilder builder = boolQuery();
    	Map<String,Object> mp = convertToMap(obj);
    	
    	for(String s : mp.keySet()){
    	  try {
    		if(null != mp.get(s))
//    		  builder = builder.filter(regexpQuery(s,(String)mp.get(s)));
    		  builder = builder.filter(matchPhraseQuery(s,(String)mp.get(s)));
    	  }catch(ClassCastException e) {
    	    //logger.error("swallows filter {}",s);
    	  }
    	}
    	return builder;
    }

    public static BoolQueryBuilder queryBuilderForRegex(Object obj){
      BoolQueryBuilder builder = boolQuery();
      Map<String,Object> mp = convertToMap(obj);
      
      for(String s : mp.keySet()){
        try {
        if(null != mp.get(s))
        {
          String processedString = (String)mp.get(s);
//          builder = builder.filter(regexpQuery(s,(String)mp.get(s)));
          builder = builder.filter(wildcardQuery(s,processregex(processedString.toLowerCase())));
        }
        }catch(ClassCastException e) {
          //logger.error("swallows filter {}",s);
        }
      }
      return builder;
    }

    public static BoolQueryBuilder getQueryBuilder(Object obj,StringMatcher sm){
      
      if(sm.equals(StringMatcher.CONTAINING))
        return queryBuilderForContains(obj);

      else if(sm.equals(StringMatcher.REGEX))
        return queryBuilderForRegex(obj);
      
      else
        return queryBuilderForExact(obj);

    }
    
    
    
    public static BoolQueryBuilder queryBuilderForContains(Object obj){
      BoolQueryBuilder builder = boolQuery();
      Map<String,Object> mp = convertToMap(obj);
      
      for(String s : mp.keySet()){
        try {
        if(null != mp.get(s))
//          builder = builder.filter(regexpQuery(s,(String)mp.get(s)));
          
          builder = builder.filter(matchPhraseQuery(s,((String)mp.get(s)).toLowerCase()));
        }catch(ClassCastException e) {
          //logger.error("swallows filter {}",s);
        }
      }
      return builder;
    }
    
    
    public static BoolQueryBuilder queryBuilderForExact(Object obj){
      BoolQueryBuilder builder = boolQuery();
      Map<String,Object> mp = convertToMap(obj);
      
      for(String s : mp.keySet()){
        try {
        if(null != mp.get(s))
//          builder = builder.filter(regexpQuery(s,(String)mp.get(s)));
          builder = builder.filter(matchQuery(s,((String)mp.get(s)).toLowerCase()).operator(Operator.AND));
        }catch(ClassCastException e) {
          //logger.error("swallows filter {}",s);
        }
      }
      return builder;
    }
    
    
    public static String processregex(String args) {
      try {
      return ("*"+args.split("\\)")[1].split("\\(")[1]+"*");
      }catch (Exception e) {
        //logger.error(e.getMessage());
      }
      return args;
    }    
    
}