package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.NOT_INCLUDING_SUBSCRIBED_PRODUCTS_CONDITION;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.NO_PRODUCTS_HAS_BEEN_SUBSCRIBED_BY_THIS_USER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCTID_ALREADY_EXISTS;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCT_DOES_NOT_EXISTS;
import static com.pearson.sam.bridgeapi.util.Utils.copyNonNullProperties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IProductBridgeService;
import com.pearson.sam.bridgeapi.iservice.IProductSearchService;
import com.pearson.sam.bridgeapi.iservice.IProductService;
import com.pearson.sam.bridgeapi.model.Details;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.repository.LicenceHolderRespository;
import com.pearson.sam.bridgeapi.repository.LicenceRespository;
import com.pearson.sam.bridgeapi.repository.ProductRepository;
import com.pearson.sam.bridgeapi.samclient.AccessCodeSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

/**
 * 
 * @author VKu99Ma
 *
 */
@Service
public class ProductBridgeServiceImpl implements IProductBridgeService {

  private static final Logger logger = LoggerFactory.getLogger(ProductBridgeServiceImpl.class);

  @Autowired
  protected ISessionFacade sessionFacade;

  @Autowired
  private AccessCodeSamClient accessCodeSamClient;

  @Value("${SPEC_FILE}")
  private String specJsonFile;

  @Autowired
  MongoOperations mongo;

  @Autowired
  IProductService productService;

  @Autowired
  private LicenceRespository licenceRespository;

  @Autowired
  private LicenceHolderRespository licenceHolderRespository;
  
  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private IProductSearchService productSearchService;

  public SessionUser getUser(boolean getLoggedInUser) {
    return sessionFacade.getLoggedInUser(getLoggedInUser);
  }

  public void loadProductsToMaster() {
    initLoad(PageRequest.of(0, productService.getTotalCount().intValue()));
  }

  private void initLoad(Pageable pageable) {
    List<Product> pdts = productService.findProductsToLoadIntoElastic();
    Set<String> pdtIds = pdts.stream().map(pdt -> pdt.getProductId())
        .collect(Collectors.toSet());

    
    productService.getMultipleProducts(pdtIds).stream().forEach(pdt -> {
      ProductSearch query = new ProductSearch();
      copyNonNullProperties(pdt, query);
      productSearchService.save(query);
    });
  }

  /**
   * This Method will add the Product.
   * 
   * @param method
   * @param product
   * @return Product
   */
  @Override
  public Product create(Product product) {
    product.setCreatedBy(getUser(true).getUid());
    checkPrdIdExists(product.getProductId());
    product = productService.create(product);
    productSearchService.save(getProductSearchObject(product));
    return product;
  }

  /**
   * This Method will update the Product.
   * 
   * @param method
   * @param userProduct
   * @return Product
   */
  @Override
  public Product updateProduct(Product userProduct) {
    userProduct.setUpdatedBy(getUser(true).getUid());
    userProduct = productService.update(userProduct);
    productSearchService.update(getProductSearchObject(userProduct));
    return userProduct;
  }

  /**
   * This Method will fetch one Product.
   * 
   * @param product
   * @return Product
   */
  @Override
  public Product fetchOneProduct(Product product) {
    product = productService.fetchOne(product);
    if (!Optional.ofNullable(product).isPresent()) {
      throw new BridgeApiGraphqlException(ErrorType.ValidationError, PRODUCT_DOES_NOT_EXISTS);
    }
    return productService.fetchOne(product);
  }

  /**
   * This Method will fetch fetch Multiple products based on input ids.
   * 
   * @param key
   * @param ids
   * @return List<Product>
   */
  @Override
  public List<Product> fetchMultipleProduct(KeyType key, List<String> ids) {
    List<Product> resultProductList = new ArrayList<>();
    switch (key) {
      case PRODUCT_ID:
        resultProductList = productService.fetchMultiple(null);
        break;
      case TYPE:
        break;
      case MULTIPLE_PRODUCTS:
        resultProductList = productService.getMultipleProducts(new HashSet<>(ids));
        break;
      case USER_ID:
        resultProductList = productService.getMultipleProducts(getUser(true).getProduct());
        break;
      default:
        break;
    }
    return resultProductList;
  }

  /**
   * This Method will remove the Product from DB.
   * 
   * @param product
   * @return Product
   */
  @Override
  public Product removeProduct(Product product) {
    return productService.delete(product);
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
    Page<Product> oldPages = getPaginatedMongoResponse(pageable, e);
    return PageableExecutionUtils.getPage(productService.getProductsList(oldPages.getContent()),
        pageable, () -> {
          return oldPages.getTotalElements();
        });
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
    Page<Product> oldPages = productService.pageIt(pageable, name, productIds);
    return PageableExecutionUtils.getPage(productService.getProductsList(oldPages.getContent()),
        pageable, () -> {
          return oldPages.getTotalElements();
        });
  }

  @Override
  public Page<ProductSearch> search(Pageable pageable, String searchable) {
    return productSearchService.search(pageable, searchable);
  }

  private Page<Product> getPaginatedMongoResponse(Pageable pageable, Example<Product> e) {
    Predicate<? super String> pred = pr -> {
      return pr.equalsIgnoreCase("teacher") || pr.equalsIgnoreCase("student");
    };
    if (getUser(true).getRoles().stream().map(String::toLowerCase).anyMatch(pred)) {
      return subscribedProductsPage(pageable, e);
    }
    return productService.pageIt(pageable, e);
  }

  private Page<Product> subscribedProductsPage(Pageable pageable, Example<Product> e) {

    Query query = new Query().with(pageable);
    query.addCriteria(Criteria.byExample(e));

    Set<String> productIds = getProducts(getUser(true).getSchool(), getUser(true).getIdentifier(),
        getUser(true).getRoles());
    if (productIds != null) {
      if (!productIds.isEmpty() && StringUtils.isNotBlank(e.getProbe().getProductId())
          && productIds.contains(e.getProbe().getProductId())) {
        //logger.info(NOT_INCLUDING_SUBSCRIBED_PRODUCTS_CONDITION);
      } else {
        query.addCriteria(Criteria.where("productId").in(productIds));
      }
    }

    if (CollectionUtils.isEmpty(productIds)) {
      //logger.info(NO_PRODUCTS_HAS_BEEN_SUBSCRIBED_BY_THIS_USER);
      productIds = new HashSet<>();
    }

    return PageableExecutionUtils.getPage(mongo.find(query, Product.class), pageable,
        () -> mongo.count(query, Product.class));
  }

  private Set<String> getProducts(List<String> schoolIds, String userName, Set<String> roles) {
    Map<String, Object> responseMap = accessCodeSamClient.getAllSubscriptionDetailsofUser(userName);
    Set<String> prods = Utils.convert(
        Utils.transformJson(responseMap, "get_user_subscribed_access_codes", specJsonFile),
        new TypeReference<Set<String>>() {
        });

    boolean schoolsAvailabity = Optional.ofNullable(schoolIds).isPresent();
    Set<String> lProds = null;
    if (schoolsAvailabity) {
      lProds = getProductsFromLicense(schoolIds, userName);
    }

    Set<String> teacherProducts = null;
    if (roles.contains("teacher") && schoolsAvailabity) {
      teacherProducts = getProductsOfFreeTeacherAccess(schoolIds);
    }

    if (CollectionUtils.isEmpty(prods)) {
      prods = new HashSet<>();
    }

    if (!CollectionUtils.isEmpty(lProds)) {

      prods.addAll(lProds);
    }

    if (!CollectionUtils.isEmpty(teacherProducts)) {
      prods.addAll(teacherProducts);
    }

    return prods;
  }

  private Set<String> getProductsFromLicense(List<String> schoolIds, String username) {

    List<List<Licence>> licencesList = getLicense(schoolIds, username);
    Set<String> products = new HashSet<String>();
    licencesList.forEach(lList -> {
      lList.forEach(l -> {
        l.getProducts().forEach(p -> products.add(p));
      });
    });

    return products;
  }

  private List<List<Licence>> getLicense(List<String> schoolIds, String username) {
    List<List<Licence>> lList = new ArrayList<>();
    List<Licence> licences = null;
    for (String id : schoolIds) {
      licences = licenceRespository.findByAttachedSchoolAndAttachedUser(id, username);
      lList.add(licences);
    }

    return lList;
  }

  private Set<String> getProductsOfFreeTeacherAccess(List<String> schoolIds) {
    Set<String> prodSet = new HashSet<>();
    Optional<List<LicenceHolder>> licenceHolderList = Optional
        .ofNullable(getLicenseHolderOfSchool(schoolIds));
    if (licenceHolderList.isPresent())
      licenceHolderList.get().forEach(holder -> {
        if (Optional.ofNullable(holder.getHolder()).isPresent()) {
          prodSet.add(holder.getHolder().getProduct().getProductId());
        }
        ;
      });
    return prodSet;
  }

  private List<LicenceHolder> getLicenseHolderOfSchool(List<String> schoolIds) {
    List<LicenceHolder> licenceHolderList = null;

    licenceHolderList = licenceHolderRespository
        .findAllByHolderSchoolSchoolIdInAndHolderTeacherAccess(schoolIds, true);

    return licenceHolderList;
  }

  private ProductSearch getProductSearchObject(Product product) {
    ProductSearch productSearch = new ProductSearch();
    Details detailsSearch = new Details();
    if (null != product.getDetails())
      Utils.copyNonNullProperties(product.getDetails(), detailsSearch);
    Utils.copyNonNullProperties(product, productSearch);
    productSearch.setDetails(detailsSearch);
    return productSearch;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.pearson.sam.bridgeapi.ibridgeservice.IProductBridgeService#pageIt(org.springframework.data.
   * domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch)
   */
  @Override
  public Page<ProductSearch> pageIt(Pageable pageable, ProductSearch searchable,StringMatcher sm) {
    return productSearchService.pageIt(pageable, searchable,sm);
  }
  
  private void checkPrdIdExists(String productId) {
	  if(Optional.ofNullable(productId).isPresent() && productRepository.existsByProductId(productId))
		  throw new BridgeApiGeneralException(PRODUCTID_ALREADY_EXISTS);  	    
  }

}