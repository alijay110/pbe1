package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GET_PRODUCT_DATA_BY_PRODUCT_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_ADD_PRODUCT;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_REMOVE_PRODUCT;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_UPDATE_PRODUCT;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_ALL_PRODUCTS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_MULTIPLE_PRODUCTS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_MULTIPLE_PRODUCTS_BY_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_PRODUCT;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_PRODUCTS_BY_IDS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_GET_PRODUCT_BY_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_PRODUCT;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_PRODUCTS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_PRODUCT_PAGINATED;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PGE_GRAPHQL_QUERY_GET_PRODUCTSEARCH;
import static com.pearson.sam.bridgeapi.util.Utils.copyNonNullProperties;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.ibridgeservice.IProductBridgeService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

/**
 * @author VGowdSa
 *
 */
@Component
public class ProductResolver {
	
	private static final String ID = "Id";

	@Autowired
	IProductBridgeService productBridgeService;
	
	@Autowired
	ModelValidator validator;

	/**
	 * This Method will add the Product.
	 * 
	 * @param Product
	 * @return Product
	 */
	@GraphQLMutation(name = GRAPHQL_MUTATION_ADD_PRODUCT)
	public Product addProduct(@GraphQLArgument(name = "data") Product data, @GraphQLRootContext AuthContext context) {
		validator.validateEmptyNullValue(data.getProductName(), "ProductName");		
		validator.validateEmptyNullValue(data.getCourseUrl(), "CourseUrl");
		validator.validateModel(data, MethodType.CREATE.toString());
		return productBridgeService.create(data);
	}

	/**
	 * This Method will update the Product data.
	 * 
	 * @param productId
	 * @param data
	 * @return Product
	 */
	@GraphQLMutation(name = GRAPHQL_MUTATION_UPDATE_PRODUCT)
	public Product updateProduct(@GraphQLArgument(name = "productId") String productId,
			@GraphQLArgument(name = "data") Product data, @GraphQLRootContext AuthContext context) {
		validator.validateModel(data, MethodType.CREATE.toString());
		data.setProductId(productId);
		return productBridgeService.updateProduct(data);
	}

	/**
	 * This Method will remove the Product from DB.
	 * 
	 * @param data
	 * @return Product
	 */
	@GraphQLMutation(name = GRAPHQL_MUTATION_REMOVE_PRODUCT)
	public Product removeProduct(@GraphQLArgument(name = "productId") String data,
			@GraphQLRootContext AuthContext context) {
		Product product = new Product();
		product.setProductId(data);
		return productBridgeService.removeProduct(product);
	}

	/**
	 * This Method will fetch one Product by productId.
	 * 
	 * @param data
	 * @return Product
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_PRODUCT)
	public Product product(@GraphQLArgument(name = "productId") String data, @GraphQLRootContext AuthContext context) {
		Product product = new Product();
		product.setProductId(data);
		return productBridgeService.fetchOneProduct(product);
	}

	/**
	 * This Method will fetch one Product by id.
	 * 
	 * @param id
	 * @param context
	 * @return Product
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_PRODUCT_BY_ID)
	public Product getProductById(@GraphQLArgument(name = "id") String id, @GraphQLRootContext AuthContext context) {
		validator.validateEmptyNullValue(id, ID);
		Product product = new Product();
		product.setProductId(id);
		return productBridgeService.fetchOneProduct(product);
	}

	/**
	 * This Method will fetch fetch All products.
	 * 
	 * @return List<Product>
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_PRODUCTS)
	public List<Product> products(@GraphQLRootContext AuthContext context) {
		return productBridgeService.fetchMultipleProduct(KeyType.PRODUCT_ID, null);
	}

	/**
	 * This Method will fetch fetch All products.
	 * 
	 * @return List<Product>
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_ALL_PRODUCTS)
	public List<Product> getAllProducts(@GraphQLRootContext AuthContext context) {
		return productBridgeService.fetchMultipleProduct(KeyType.PRODUCT_ID, null);
	}

	/**
	 * This Method will fetch fetch Multiple products based on input products.
	 * 
	 * @param products
	 * @param context
	 * @return List<Product>
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_MULTIPLE_PRODUCTS)
	public List<Product> getMultipleProducts(@GraphQLArgument(name = "products") List<String> products,
			@GraphQLRootContext AuthContext context) {
		return productBridgeService.fetchMultipleProduct(KeyType.MULTIPLE_PRODUCTS, products);
	}

	/**
	 * This Method will fetch fetch Multiple products based on input products.
	 * 
	 * @param products
	 * @param context
	 * @return List<Product>
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_PRODUCTS_BY_IDS)
	public List<Product> getProductsByIds(@GraphQLArgument(name = "products") List<String> products,
			@GraphQLRootContext AuthContext context) {
		return productBridgeService.fetchMultipleProduct(KeyType.MULTIPLE_PRODUCTS, products);
	}

	/**
	 * This Method will give the paginated products
	 * 
	 * @param pageNumber
	 * @param pageLimit
	 * @param filter
	 * @param sm
	 * @param sort
	 * @param context
	 * @return Page<Product>
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_PRODUCT_PAGINATED)
	public Page<Product> getPaginatedProductTableData(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") Product filter,
			@GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"productId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {
	  Pageable pageable = PageRequest.of(pageNumber, pageLimit,
        Direction.valueOf(sort.getOrder().toString()), sort.getField());
    ProductSearch query = new ProductSearch();
    copyNonNullProperties(filter,query);
    Page<ProductSearch> oldPage = productBridgeService.pageIt(pageable, query,sm);
    return new PageImpl<Product>(oldPage.getContent().stream().map(pdtSrch -> new Product(pdtSrch)).collect(Collectors.toList()), pageable,
        oldPage.getTotalElements());
    
	}

	/**
	 * This Method will fetch one Product based on input productId.
	 * 
	 * @param product
	 * @return Product
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_PRODUCT)
	public Product getProduct(@GraphQLArgument(name = "productId") String data,
			@GraphQLRootContext AuthContext context) {
		Product product = new Product();
		product.setProductId(data);
		return productBridgeService.fetchOneProduct(product);
	}

	/**
	 * This Method will give the paginated products based on input productIds.
	 * 
	 * @param pageNumber
	 * @param pageLimit
	 * @param filter
	 * @param sort
	 * @param ids
	 * @param context
	 * @return Page<Product>
	 */
	@GraphQLQuery(name = GRAPHQL_QUERY_GET_MULTIPLE_PRODUCTS_BY_ID)
	public Page<Product> getPaginatedProducts(@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") Product filter,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"productId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLArgument(name = "products") List<String> ids, @GraphQLRootContext AuthContext context) {
		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());
		return productBridgeService.pageIt(pageable, "", ids);
	}
	
	/**
	 * This Method will fetch one Product by id.
	 * 
	 * @param id
	 * @param context
	 * @return Product
	 */
	@GraphQLQuery(name = GET_PRODUCT_DATA_BY_PRODUCT_ID)
	public Product getProductDataByProductId(@GraphQLArgument(name = "id") String id,
			@GraphQLRootContext AuthContext context) {
		validator.validateEmptyNullValue(id, ID);
		Product product = new Product();
		product.setProductId(id);
		return productBridgeService.fetchOneProduct(product);
	}
	
	/**
	 * This Method will give the paginated getProductSearch as response.
	 * 
	 * @param pageNumber
	 * @param pageLimit
	 * @param filter
	 * @param context
	 * @return Page<ProductSearch>
	 */
	@GraphQLQuery(name = PGE_GRAPHQL_QUERY_GET_PRODUCTSEARCH, description = "Search product in elastic search")
	public Page<ProductSearch> getProductSearch(@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") String query,
			@GraphQLRootContext AuthContext context) {
		Pageable pageable = PageRequest.of(pageNumber, pageLimit);
		return productBridgeService.search(pageable, query);
	}

	
}
