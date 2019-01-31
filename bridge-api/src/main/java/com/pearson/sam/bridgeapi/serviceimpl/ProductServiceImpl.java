package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.MONGO_MAP;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.SAM_MAP;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_PRODUCT_DETAILS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.DATA_NOT_FOUND;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.IProductService;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.repository.ProductRepository;
import com.pearson.sam.bridgeapi.samclient.IProductSamClient;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;
import graphql.GraphQLException;

import io.netty.util.internal.StringUtil;


/**
 * 
 * @author VKu99Ma
 *
 */
@Service
public class ProductServiceImpl implements IProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
	private static final String KEY = "Product";
	
	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	IProductSamClient productSamClient;

//	@Autowired
//	CacheService cacheService;

	/**
	 * This Method will add the Product.
	 * 
	 * @param method
	 * @param product
	 * @return Product
	 */
	@Override
	public Product create(Product product) {
		//logger.info("Request: {}", product);		
		String productId = product.getProductId();
		if (Utils.isEmpty(productId)) {
			productId = generateRandomProduct(product.getProductName());
			product.setProductId(productId);
		}
		product.setCreatedOn(null != product.getCreatedOn() ? product.getCreatedOn() : Utils.generateEpochDate());
		product.setActiveTimestamp(Utils.generateEpochDate());
		product.setProductModel(Utils.getProductModel());
		//logger.info("ProductServiceImpl saving {}", product);
		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(product);
		Product mongoProduct = Utils.convert(segregatedDataMap.get(MONGO_MAP), new TypeReference<Product>() {
		});
		mongoProduct.setCreatedBy(product.getCreatedBy());
		mongoProduct.setCreatedOn(null != product.getCreatedOn() ? product.getCreatedOn() : Utils.generateEpochDate());
		product = productRepository.insert(mongoProduct);
		if (null != product) {
			JsonObject documents = gson.toJsonTree(segregatedDataMap.get(SAM_MAP)).getAsJsonObject();
			try {
				JSONArray courseList = null;
				if(Optional.ofNullable(documents.get("resourceCourseIds")).isPresent() &&
						Optional.ofNullable(documents.get("productCode")).isPresent()){
					
					courseList = getCourseList((JsonArray) documents.get("resourceCourseIds"), product.getProductCode(),product.getType());
				}
				
				documents.addProperty("productIdentifier", product.getProductId());
				documents.add("courseList", gson.toJsonTree(courseList));
				documents.remove("resourceCourseIds");
				documents.remove("productCode");
				Product restObj = productSamClient.create(documents); 
				product.setProductId(restObj.getProductId());
			} catch (BridgeApiGeneralException e) {
				productRepository.deleteByProductId(productId);
			}
		}
		
		Product prod =fetchOne(product);
		if(Optional.ofNullable(prod).isPresent() && Optional.ofNullable(prod.getResourceCourseIds()).isPresent()) {
			List<String> resourceList = prod.getResourceCourseIds();
			resourceList.remove(product.getProductCode());
		}
		return prod;
	}

	@SuppressWarnings("unchecked")
	private JSONArray getCourseList(JsonArray resourceCourseIds, String productCode, List<String> types) {
		JSONArray courseList = new JSONArray();
		
	    resourceCourseIds.forEach(id ->{
	    	JsonObject course =  new JsonObject();
	    	course.add("courseId", gson.toJsonTree(id) );
			course.add("type", gson.toJsonTree("pulse2.0"));
			courseList.add(course);
	    });
	    
	    JsonObject courseForReaderPluse =  new JsonObject();
	    courseForReaderPluse.add("courseId", gson.toJsonTree(productCode) );
	    if(Optional.ofNullable(types).isPresent()) {
	    	 types.forEach(id ->{
	 	    	if(id.equals("reader-plus"))
	 	    		courseForReaderPluse.add("type", gson.toJsonTree("reader+"));
	 	    });
	    }
	    courseList.add(courseForReaderPluse);
		
		return courseList;
	}

	/**
	 * This Method will update the Product.
	 * 
	 * @param method
	 * @param userProduct
	 * @return Product
	 */
	@Override
	public Product update(Product product) {
		Product responseProduct = new Product();

		product.setUpdatedOn(null != product.getUpdatedOn() ? product.getUpdatedOn() : Utils.generateEpochDate());

		Map<String, Map<String, Object>> segregatedDataMap = segregatedData(product);
		Map<String, Object> samMap = segregatedDataMap.get(SAM_MAP);
		Map<String, Object> transformedMap = Utils.transformJsonAsMap(samMap, UPDATE_PRODUCT_DETAILS, specJsonFile);
		//logger.info("transformedMap :{}", transformedMap);
		Map<String, Object> mongoMap = segregatedDataMap.get(MONGO_MAP);

		Product dbProduct = productRepository.findByProductId(mongoMap.get("productId").toString());
		if (!Optional.ofNullable(dbProduct).isPresent()) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, "Product doesn't exists");
		}
		Product productCon = Utils.convert(mongoMap, new TypeReference<Product>() {
		});
		Utils.copyNonNullProperties(productCon, dbProduct);
		dbProduct = productRepository.save(dbProduct);
		if (null != dbProduct) {
			try {
				
				JsonObject documents = gson.toJsonTree(transformedMap).getAsJsonObject();
				Map<String, String> mp = new HashMap<>();
				
				JSONArray courseList = null;
				if(Optional.ofNullable(documents.get("resourceCourseIds")).isPresent() &&
						Optional.ofNullable(documents.get("productCode")).isPresent()){
					courseList = getCourseList((JsonArray) documents.get("resourceCourseIds"), product.getProductCode(), product.getType());
				}
				documents.add("courseList", gson.toJsonTree(courseList));
				documents.remove("resourceCourseIds");
				documents.remove("productCode");
				mp.put("productIdentifier", product.getProductId());
				responseProduct = productSamClient.update(mp, documents);
			} catch (BridgeApiGeneralException e) {
			}
		}
//		cacheService.update(responseProduct, responseProduct.getProductId(), KEY, this::retrieveProduct);
		Product prod =fetchOne(responseProduct);
		List<String> resourceList = prod.getResourceCourseIds();
		if (Optional.ofNullable(resourceList).isPresent()) 
			resourceList.remove(product.getProductCode());
		return prod;
	}

	@Override
	public Product fetchOne(Product product) {
//		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		return getProductWithSessionId(product, StringUtils.EMPTY);
	}

	private Product getProductWithSessionId(Product product, String sessionId) {
//		Object obj = cacheService.getObjectWithSessionId(product, sessionId, product.getProductId(), KEY,
//				this::retrieveProduct);
	  Object obj = this.retrieveProduct(product);
		return Utils.convert(obj, new TypeReference<Product>() {
		});
	}

	private Object retrieveProduct(Object obj) {
		Product product = Utils.convert(obj, new TypeReference<Product>() {
		});
		product = productRepository.findByProductId(product.getProductId());
		if (null != product) {
			Map<String, String> request = new HashMap<>();
			request.put("productId", product.getProductId());
			JsonObject documents = gson.toJsonTree(request).getAsJsonObject();
			Map<String, Object> mongoMap = Utils.convertToMap(product);
			try {
				Product restProduct = productSamClient.fetchOne(documents);
				removeProductCodeFromResourceCourseIds(restProduct,product);
				if (null != restProduct) {
					Map<String, Object> restMap = Utils.convertToMap(restProduct);
					mongoMap.putAll(restMap);
				}
				product = Utils.convert(mongoMap, new TypeReference<Product>() {
				});
			} catch (BridgeApiGeneralException e) {
			}
		}
		return product;
	}

	/**
	 * This Method will fetch fetch Multiple products based on input ids.
	 * 
	 * @param key
	 * @param ids
	 * @return List<Product>
	 */
	@Override
	public List<Product> fetchMultiple(List<String> ids) {
		List<Product> productList = null;
		List<Product> resultProductList = new ArrayList<>();
		try {
			List<Product> resultList = new ArrayList<>();
			productList = productSamClient.fetchMultiple(null);
			productList.stream().forEach(product -> {
				Product mongoproduct = productRepository.findByProductId(product.getProductId());
				if (null != mongoproduct) {
					Map<String, Object> restMap = Utils.convertToMap(product);
					Map<String, Object> mongoMap = Utils.convertToMap(mongoproduct);
					mongoMap.putAll(restMap);
					product = Utils.convert(mongoMap, new TypeReference<Product>() {
					});
				}
				removeProductCodeFromResourceCourseIds(product, mongoproduct);
				resultList.add(product);
			});
			resultProductList.addAll(resultList);
		} catch (BridgeApiGeneralException e) {
		}
		return resultProductList;
	}

	/**
	 * This Method will delete the Product from DB.
	 * 
	 * @param product
	 * @return Product
	 */
	@Override
	public Product delete(Product product) {
		product = productRepository.findByProductId(product.getProductId());
		checkAvailable(product);
		productRepository.delete(product);
		return product;
	}

	/**
	 * This Method will give the paginated products
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<Product>
	 */
	@Override
	public Page<Product> pageIt(Pageable pageable, Example<Product> e) {
		return productRepository.findAll(e, pageable);
	}

	/**
	 * This Method will give the paginated products based on input productIds.
	 * 
	 * @param pageable
	 * @param name
	 * @param productIds
	 * @return Page<Product>
	 */
	@Override
	public Page<Product> pageIt(Pageable pageable, String name, List<String> productIds) {
		return productRepository.findAllByProductIdIn(productIds, pageable);
	}

	private String generateRandomProduct(String productName) {
		StringBuffer sb = new StringBuffer();
		String names[] = productName.trim().split(" ");
		if (names.length >= 2) {
			if (Utils.isNotEmpty(names[0])) {
				sb.append(names[0].substring(0, 1).toUpperCase());
			}
			if (Utils.isNotEmpty(names[1])) {
				sb.append(names[1].substring(0, 1).toUpperCase());
			}
		} else if (names.length == 1 && Utils.isNotEmpty(names[0])) {
			if (names[0].length() > 1)
				sb.append(names[0].substring(0, 2).toUpperCase());
			else if (names[0].length() == 1) {
				sb.append(names[0].substring(0, 1).toUpperCase());
				sb.append(names[0].substring(0, 1).toUpperCase());
			}
		}
		sb.append("P");
		sb.append(Utils.generateRandomNumber(100000, 900000));
		return sb.toString();
	}

	private Map<String, Map<String, Object>> segregatedData(Product product) {
		Map<String, Map<String, Object>> segregatedMap = new HashMap<>();

		Map<String, Object> dataMap = Utils.convertToMap(product);

		List<String> mongoFields = Utils.extractAnnotatedFieldNames(Product.class, MongoSource.class);
		List<String> samFields = Utils.extractAnnotatedFieldNames(Product.class, SamSource.class);

		Map<String, Object> samMap = new HashMap<>(dataMap);
		samMap.keySet().retainAll(samFields);
		Map<String, Object> mongoMap = dataMap;
		mongoMap.keySet().retainAll(mongoFields);

		segregatedMap.put("samMap", samMap);
		segregatedMap.put("mongoMap", mongoMap);

		return segregatedMap;
	}

	@Override
	public List<Product> getMultipleProducts(Set<String> productId) {
		List<Product> productList = null;
		productList = productRepository.findAllByProductIdIn(new HashSet<>(productId));
		return getProductsList(productList);
	}

	 @Override
	  public List<Product> findProductsToLoadIntoElastic() {
	   return productRepository.findAll();
	 }
	 
	@Override
	public List<Product> getProductsList(List<Product> productsList) {
		DataLoaderOptions dlo = new DataLoaderOptions();
		dlo.setBatchingEnabled(true);
		dlo.setCachingEnabled(true);
		dlo.setMaxBatchSize(Utils.batchSizeCount(productsList.size()));
//		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

		BatchLoader<Product, Product> batchLoader = (List<Product> products) -> {
			//logger.info("calling batchLoader load...");
			return CompletableFuture.supplyAsync(() -> {
				return products.parallelStream().map(product -> this.loadProduct(product, StringUtils.EMPTY))
						.collect(Collectors.toList());
			});
		};
		DataLoader<Product, Product> productsDataLoader = new DataLoader<>(batchLoader, dlo);
		CompletableFuture<List<Product>> cf = productsDataLoader.loadMany(productsList);
		productsDataLoader.dispatchAndJoin();

		List<Product> l = null;
		try {
			l = cf.get();
		} catch (InterruptedException e) {
			throw new GraphQLException(e.getMessage());
		} catch (ExecutionException e) {
			throw new GraphQLException(e.getMessage());
		}

		return l;
	}

	private Product loadProduct(Product product, String sessionId) {
	  try {
		product.replaceWith(getProductWithSessionId(product, sessionId));
	  }catch (Exception e) {
      //logger.error("Exception while product loading {}",e);
    }
		return product;
	}

	private void checkAvailable(Object obj) {
		if (obj == null) {
			throw new BridgeApiGraphqlException(ErrorType.DataFetchingException, DATA_NOT_FOUND);
		}
	}

	@Override
	public Map<String, Object> getLearningMaterial(String orgId, String courseId, String userId) {
		return productSamClient.getLearningMaterial(orgId, courseId, userId);
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.IProductService#getTotalCount()
   */
  @Override
  public Long getTotalCount() {
    // TODO Auto-generated method stub
    return productRepository.count();
  }
	
	private void removeProductCodeFromResourceCourseIds(Product restProduct,Product product) {
		List<String> resourceCourseIds = restProduct.getResourceCourseIds();
		if(Optional.ofNullable(resourceCourseIds).isPresent() && null!= product) {
			resourceCourseIds.remove(product.getProductCode());
			restProduct.setResourceCourseIds(resourceCourseIds);
			restProduct.setResourcesCount(Long.valueOf(resourceCourseIds.size()));
		}
	}
	
}
