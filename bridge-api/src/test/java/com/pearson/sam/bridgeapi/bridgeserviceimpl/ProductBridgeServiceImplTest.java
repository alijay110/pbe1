package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.ProductBridgeServiceImpl;
import com.pearson.sam.bridgeapi.iservice.IProductService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.samclient.AccessCodeSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Product Bridge Service Unit Test Cases
 * @author VVaijPa
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductBridgeServiceImplTest {
	
	@InjectMocks
	private ProductBridgeServiceImpl underTest = new ProductBridgeServiceImpl();
	
	@Mock
	private ISessionFacade sessionFacade;

	@Mock
	private AccessCodeSamClient accessCodeSamClient;

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Mock
	MongoOperations mongo;

	@Mock
	IProductService productService;
	
	@Test
	public void testGetUser() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		SessionUser result = underTest.getUser(true);
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testCreate() {
		when(productService.create(any(Product.class))).thenReturn(new Product());
		Product result = underTest.create(new Product());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testUpdateProduct() {
		when(productService.update(any(Product.class))).thenReturn(new Product());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		Product result = underTest.updateProduct(getProduct());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testFetchOneProduct() {
		when(productService.fetchOne(any(Product.class))).thenReturn(new Product());
		Product result = underTest.fetchOneProduct(getProduct());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testFetchMultipleProduct() {
		List<Product> resultProductList = new ArrayList<>();
		
		List<String> products = new ArrayList<>();
		products.add("productId");
		when(productService.fetchMultiple(products)).thenReturn(resultProductList);
		List<Product> result = underTest.fetchMultipleProduct(KeyType.PRODUCT_ID, products);
		Assert.assertNotNull(result);
		
		HashSet<String> t = new HashSet<>(products);
		when(productService.getMultipleProducts(t)).thenReturn(resultProductList);
		List<Product> resultMultiProduct = underTest.fetchMultipleProduct(KeyType.MULTIPLE_PRODUCTS, products);
		Assert.assertNotNull(resultMultiProduct);
		
		User user = new User();
		Set<String> product = new HashSet<String>();
		product.add("PRODUCT01");
		user.setProduct(product);
		when(productService.getMultipleProducts(user.getProduct())).thenReturn(resultProductList);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		List<Product> resultUserProduct = underTest.fetchMultipleProduct(KeyType.USER_ID, products);
		Assert.assertNotNull(resultUserProduct);
		
	}
	
	@Test
	public void testRemoveProduct() {
		when(productService.delete(any(Product.class))).thenReturn(new Product());
		Product result = underTest.removeProduct(getProduct());
		Assert.assertNotNull(result);
	}
	
	@Ignore
	//@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("productId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Product> e = Example.of(new Product(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		
		Set<String> roles = new HashSet<>();
		roles.add("teacher");
		roles.add("student");
		
		sessionFacade.getLoggedInUser(true).setRoles(roles);
		sessionFacade.getLoggedInUser(true).setIdentifier("identifier");
		
		List<Product> resultProductList = new ArrayList<>();
		
		when(productService.getProductsList(resultProductList)).thenReturn(resultProductList);
		
		when(productService.pageIt(pageable, e)).thenReturn(getProductPage());
		Page<Product> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}
	
	@Test
	public void testPageItList() {
		Sort sort = new Sort();
		sort.setField("schoolId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		String productName = "productName";
		List<String> productList = new ArrayList<>();

		when(productService.pageIt(any(Pageable.class), anyString(), anyList())).thenReturn(getProductPage());

		Page<Product> page = underTest.pageIt(pageable, productName, productList);
		Assert.assertNotNull(page);
	}
	
	private Product getProduct() {
		Product product = new Product();
		product.setUpdatedOn(new Date().toString());
		return product;
	}
	
	private Page<Product> getProductPage() {
		Product product = new Product();
		product.setProductId("productId");
		List<Product> listProduct = new ArrayList<Product>();
		listProduct.add(product);
		Page<Product> result = new PageImpl<Product>(listProduct);
		return result;
	}

}
