package com.pearson.sam.bridgeapi.serviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.ProductRepository;
import com.pearson.sam.bridgeapi.samclient.IProductSamClient;
import com.pearson.sam.bridgeapi.serviceimpl.ProductServiceImpl;
import com.pearson.sam.bridgeapi.util.Utils;

import io.leangen.graphql.execution.SortField.Direction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

//TODO: Possible NPE in create,update->fetchOne and branching condition to be changed

/**
 * 
 * @author VKu99Ma
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-application.properties")
public class ProductServiceImplTest {
	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");
		
	}
	
	@Value("${SPEC_FILE}")
	String specJsonFile;
	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductServiceImpl underTest = new ProductServiceImpl();

	@Mock
	private IProductSamClient productSamClient;

	@Mock
	Product product = new Product();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
	}

	/**
	 * This Test Case will test the create method in ProductServiceImpl
	 * 
	 */
	@Test
	public void testAddProduct() {
		when(productRepository.insert(any(Product.class))).thenReturn(getProduct());
		when(productSamClient.create(any())).thenReturn(getProduct());
		when(productRepository.findByProductId(any(String.class))).thenReturn(getProduct());
		Product response = underTest.create(getProduct());
		Assert.assertNotNull(response);
	}
	
	
	/**
	 * This Test Case will test the create method in ProductServiceImpl when we are
	 * not passing the productName.
	 * */
	@Test(expected = BridgeApiGeneralException.class)
	public void testAddProductWithoutProductName() {
		Product productLocal = new Product();
		productLocal.setProductName(null);
		when(Utils.isEmpty(product.getProductName()))
				.thenThrow(new BridgeApiGeneralException("Product name should not be empty"));
		underTest.create(productLocal);
	}
	@Test(expected=BridgeApiGeneralException.class)
	public void testAddProductEmptyCourseUrl() {
		Product productLocal = new Product();
		productLocal.setProductName("ProductName");
		productLocal.setCourseUrl(null);
		when(Utils.isEmpty(product.getCourseUrl())).thenThrow(new BridgeApiGeneralException("Course URL should not be empty"));
		underTest.create(productLocal);
		
	}
	
	@Test(expected=BridgeApiGeneralException.class)
	public void testAddProductEmptyProductModel() {
		Product productLocal = new Product();
		productLocal.setProductName("ProductName");
		productLocal.setCourseUrl("CourseUrl");
		productLocal.setProductModel(null);
		when(Utils.isEmpty(product.getProductModel())).thenThrow(new BridgeApiGeneralException("Product Model should not be empty"));
		underTest.create(productLocal);
	}
	@Test
	public void testAddProductEmptyProductId() {
		Product productLocal = new Product();
		productLocal.setProductName("ProductName");
		productLocal.setCourseUrl("CourseUrl");
		productLocal.setProductModel("ProductModel");
		productLocal.setProductId(null);
		when(Utils.isEmpty(product.getProductId())).thenThrow(new BridgeApiGeneralException("Product Model should not be empty"));
		when(productRepository.insert(any(Product.class))).thenReturn(getProduct());
		when(productSamClient.create(any())).thenReturn(getProduct());
		when(productRepository.findByProductId(any(String.class))).thenReturn(getProduct());

		Product response = underTest.create(productLocal);
		Assert.assertNotNull(response);
	}
	
	@Test
	public void testCreateProductEmptyProductId() {
		Product product = getProduct();
		product.setProductId("");
		product.setProductName(" Pearson Places learning ");
		product.setCreatedBy("PP");
		product.setCreatedOn("15-11-2018");
		when(productRepository.insert(any(Product.class))).thenReturn(product);
		when(productSamClient.create(any())).thenReturn(product);
		when(productRepository.findByProductId(any(String.class))).thenReturn(product);
		Product response = underTest.create(product);
		Assert.assertFalse(response.getProductId().isEmpty());
		Assert.assertNotNull(response);
		
	}
	
	@Test
	public void testCreateProductEmptyProductIdNameLengthOne() {
		Product product = getProduct();
		product.setProductId("");
		product.setProductName("PP");
		product.setCreatedBy("PP");
		product.setCreatedOn("15-11-2018");
		when(productRepository.insert(any(Product.class))).thenReturn(product);
		when(productSamClient.create(any())).thenReturn(product);
		when(productRepository.findByProductId(any(String.class))).thenReturn(product);
		Product response = underTest.create(product);
		Assert.assertFalse(response.getProductId().isEmpty());
		Assert.assertNotNull(response);
		
	}
	
	@Test
	public void testCreateProductEmptyProductIdSecondNameNotEmpty() {
		Product product = getProduct();
		product.setProductId("");
		product.setProductName("PP PP");
		product.setCreatedBy("PP");
		product.setCreatedOn("15-11-2018");
		when(productRepository.insert(any(Product.class))).thenReturn(product);
		when(productSamClient.create(any())).thenReturn(product);
		when(productRepository.findByProductId(any(String.class))).thenReturn(product);
		Product response = underTest.create(product);
		Assert.assertFalse(response.getProductId().isEmpty());
		Assert.assertNotNull(response);
		
	}
	
	@Test
	public void testCreateProductEmptyProductIdSecondNameEmpty() {
		Product product = getProduct();
		product.setProductId("");
		product.setProductName("PP    Places");
		product.setCreatedBy("PP");
		product.setCreatedOn("15-11-2018");
		when(productRepository.insert(any(Product.class))).thenReturn(product);
		when(productSamClient.create(any())).thenReturn(product);
		when(productRepository.findByProductId(any(String.class))).thenReturn(product);
		Product response = underTest.create(product);
		Assert.assertFalse(response.getProductId().isEmpty());
		Assert.assertNotNull(response);
		
	}
	
	@Test
	public void testCreateProductEmptyProductIdNameOne() {
		Product product = getProduct();
		product.setProductId("");
		product.setProductName("P");
		product.setCreatedBy("PP");
		product.setCreatedOn("15-11-2018");
		when(productRepository.insert(any(Product.class))).thenReturn(product);
		when(productSamClient.create(any())).thenReturn(product);
		when(productRepository.findByProductId(any(String.class))).thenReturn(product);
		Product response = underTest.create(product);
		Assert.assertFalse(response.getProductId().isEmpty());
		Assert.assertNotNull(response);
		
	}
	
	@Test
	public void testCreateProductTestCreatedBy() {
		Product product = getProduct();
		product.setCreatedBy("PP");
		when(productRepository.insert(any(Product.class))).thenReturn(product);
		when(productSamClient.create(any())).thenReturn(product);
		when(productRepository.findByProductId(any(String.class))).thenReturn(product);
		Product response = underTest.create(product);
		Assert.assertTrue(response.getCreatedBy().equals("PP"));
		Assert.assertNotNull(response);
		
	}
	
	@Test
	public void testCreateProductBlankTestCreatedBy() {
		Product product = getProduct();
		product.setCreatedBy(null);
		when(productRepository.insert(any(Product.class))).thenReturn(product);
		when(productSamClient.create(any())).thenReturn(product);
		when(productRepository.findByProductId(any(String.class))).thenReturn(product);
		Product response = underTest.create(product);
		Assert.assertTrue(response.getCreatedBy().equals("PP"));
		Assert.assertNotNull(response);
		
	}
	
	@Test
	public void testCreateProductTestCreatedOnNull() {
		Product product = getProduct();
		product.setCreatedOn(null);
		when(productRepository.insert(any(Product.class))).thenReturn(product);
		when(productSamClient.create(any())).thenReturn(product);
		when(productRepository.findByProductId(any(String.class))).thenReturn(product);
		Product response = underTest.create(product);
		Assert.assertNotNull(response.getCreatedOn());
		Assert.assertNotNull(response);
		
	}

	
	@Test(expected=BridgeApiGeneralException.class)
	public void testCreateProductEmptyProductIdNameEmpty() {
		Product product = getProduct();
		product.setProductId("");
		product.setProductName(" ");
		product.setCreatedBy("PP");
		product.setCreatedOn("15-11-2018");
		when(productRepository.insert(any(Product.class))).thenReturn(product);
		when(productSamClient.create(any())).thenReturn(product);
		when(productRepository.findByProductId(any(String.class))).thenReturn(product);
		underTest.create(product);
		
	}

	/**
	 * This Test Case will test the create method in ProductServiceImpl when SAM
	 * throwing some Exception while creating the Product.
	 * 
	 */
	@Test
	public void testAddProductWhenSAMThrowsException() {
		when(productRepository.insert(any(Product.class))).thenReturn(getProduct());
		when(productSamClient.create(any())).thenThrow(new BridgeApiGeneralException(null));
		doNothing().when(productRepository).deleteByProductId("MyProduct");
		underTest.create(getProduct());
	}

	@Test
	public void testUpdateProduct() {
		when(productRepository.findByProductId(any(String.class))).thenReturn(getProduct());
		when(productRepository.save(any(Product.class))).thenReturn(getProduct());
		when(productSamClient.update(any(), any())).thenReturn(getProduct());
		Product response = underTest.update(getProduct());
		Assert.assertNotNull(response);
	}

	@Test(expected=BridgeApiGraphqlException.class)
	public void testUpdateProductNotExists() {
		when(productRepository.findByProductId(any(String.class))).thenReturn(null);
		underTest.update(getProduct());
	}
	
	@Test(expected=BridgeApiGeneralException.class)
	public void testUpdateProductEmptyProductDB() {
		Product productLocal = new Product();
		productLocal.setProductName("ProductName");
		productLocal.setCourseUrl("CourseUrl");
		productLocal.setProductModel(null);
		when(Utils.isEmpty(product.getProductModel())).thenThrow(new BridgeApiGeneralException("Product Model should not be empty"));
		underTest.create(productLocal);
	}
	
	@Test
	public void testFetchOneProduct()
			throws JsonParseException, JsonMappingException, IOException, ProcessingException {
		when(productRepository.findByProductId(any(String.class))).thenReturn(getProduct());
		when(productSamClient.fetchOne(any())).thenReturn(getProduct());
		Product response = underTest.fetchOne(getProduct());
		Assert.assertNotNull(response);
	}

	@Test
	public void testFetchMultipleProducts()
			throws JsonParseException, JsonMappingException, IOException, ProcessingException {
		List<Product> products = new ArrayList<>();
		products.add(getProduct());
		List<String> ids = new ArrayList<>();
		ids.add("PP2PRODUCT43");
		ids.add("PP2PRODUCT44");
		when(productRepository.findByProductId(any(String.class))).thenReturn(getProduct());
		when(productSamClient.fetchMultiple(any())).thenReturn(products);
		List<Product> response = underTest.fetchMultiple(ids);
		Assert.assertNotNull(response);
	}
	
	
	@Test
	public void testFetchMultipleProductsFindProdIdNull(){
		List<Product> products = new ArrayList<>();
		products.add(getProduct());
		List<String> ids = new ArrayList<>();
		ids.add("PP2PRODUCT43");
		ids.add("PP2PRODUCT44");
		when(productRepository.findByProductId(any(String.class))).thenReturn(null);
		when(productSamClient.fetchMultiple(any())).thenReturn(products);
		List<Product> response = underTest.fetchMultiple(ids);
		Assert.assertNotNull(response);
	}
	

	@Test
	public void testdeleteProduct() throws JsonParseException, JsonMappingException, IOException, ProcessingException {
		when(productRepository.findByProductId(any(String.class))).thenReturn(getProduct());
		Product response = underTest.delete(getProduct());
		Assert.assertNotNull(response);
	}

	@Test
	public void testPageItList() {
		Sort sort = new Sort();
		sort.setField("productCode");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		List<String> productIds = Arrays.asList("PP2PRODUCT0", "PP2PRODUCT1");
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Product> e = Example.of(new Product(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when((productRepository.findAllByProductIdIn(anyList(), any(Pageable.class)))).thenReturn(getProductPage());
		Page<Product> page = underTest.pageIt(pageable, "productName", productIds);
		Assert.assertNotNull(page);
	}

	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("productCode");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Product> e = Example.of(new Product(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(productRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(getProductPage());
		Page<Product> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}
	
	@Test
	public void testGetMultipleProducts() {
		List<Product> resultProductList = new ArrayList<>();
		Set<String> products = new HashSet<>();
		products.add("Product01");
		when(productRepository.findAllByProductIdIn(products)).thenReturn(resultProductList);
		
		List<Product> response = underTest.getMultipleProducts(products);
		Assert.assertNotNull(response);
		
	}
	
	@Test
	public void testGetProductsList() {
		
		when(productSamClient.fetchOne(any())).thenReturn(getProduct());
		
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		
		product.setProductId("PP2PRODUCT43");
		product.setProductId("PP2PRODUCT44");
		productList.add(product);
		
		
		List<Product> response = underTest.getProductsList(productList);
		Assert.assertNotNull(response);
	}
	
	@Test
	public void testgetProductsListNull() {
		List<Product> productList = null;
		List<Product> response = underTest.getProductsList(productList);
		Assert.assertTrue(response.isEmpty());
	}
	
	@Test
	public void testgetProductsListFetchThrowsException() {
		when(productSamClient.fetchOne(any())).thenThrow(new BridgeApiGeneralException("Fetching one Throwing Exception"));
		List<Product> productList = new ArrayList<>();
		productList.add(getProduct());
		List<Product> response = underTest.getProductsList(productList);
		Assert.assertTrue(response.isEmpty());
	}
	
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testdeleteProdutNotAvailable() {
		when(productRepository.findByProductId(any(String.class))).thenReturn(null);
		underTest.delete(getProduct());
	}
	
	private final Product getProduct() {
		Product product = new Product();
		product.setProductId("PP");
		product.setProductName("MyProduct");
		product.setId("333333");
		product.setCourseUrl("google.com");
		product.setProductModel("PP");
		return product;
	}

	private Page<Product> getProductPage() {
		List<Product> listProduct = Arrays.asList(getProduct(), getProduct());
		Page<Product> pageProduct = new PageImpl<Product>(listProduct);
		return pageProduct;
	}
}
