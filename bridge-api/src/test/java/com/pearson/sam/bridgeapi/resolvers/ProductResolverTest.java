package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.IProductBridgeService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;
/**
 * ProductResolver unit tests
 * @author Param
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductResolverTest {

	@Mock
	private AuthContext authContex;

	@InjectMocks
	private ProductResolver underTest;

	@Mock
	private IProductBridgeService productBridgeService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddProduct() throws IOException, ProcessingException {
		when(productBridgeService.create(any(Product.class))).thenReturn(new Product());
		Product result = underTest.addProduct(new Product(), authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateProduct() throws IOException, ProcessingException {
		when(productBridgeService.updateProduct(any(Product.class))).thenReturn(new Product());
		Product result = underTest.updateProduct("productId", new Product(), authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testRemoveProduct() throws IOException, ProcessingException {
		when(productBridgeService.removeProduct(any(Product.class))).thenReturn(new Product());
		Product result = underTest.removeProduct("productId", authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testProduct() throws IOException, ProcessingException {
		when(productBridgeService.fetchOneProduct(any(Product.class))).thenReturn(new Product());
		Product result = underTest.product("productId", authContex);
		Assert.assertNotNull(result);

	}

	@Test
	public void testGetProductById() throws IOException, ProcessingException {
		when(productBridgeService.fetchOneProduct(any(Product.class))).thenReturn(new Product());
		Product result = underTest.getProductById("id", authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testProducts() throws IOException, ProcessingException {
		List<Product> products = new ArrayList<>();
		products.add(new Product());

		when(productBridgeService.fetchMultipleProduct(KeyType.PRODUCT_ID, null)).thenReturn(products);

		List<Product> result = underTest.products(authContex);
		Assert.assertNotNull(result);

	}

	@Test
	public void testGetAllProducts() throws IOException, ProcessingException {
		List<Product> products = new ArrayList<>();
		products.add(new Product());
		when(productBridgeService.fetchMultipleProduct(KeyType.PRODUCT_ID, null)).thenReturn(products);
		List<Product> result = underTest.getAllProducts(authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetMultipleProducts() throws IOException, ProcessingException {
		List<Product> products = new ArrayList<>();
		products.add(new Product());
		List<String> productList = new ArrayList<>();
		when(productBridgeService.fetchMultipleProduct(KeyType.MULTIPLE_PRODUCTS, productList)).thenReturn(products);

		List<Product> result = underTest.getMultipleProducts(productList, authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetProductsByIds() throws IOException, ProcessingException {
		List<Product> products = new ArrayList<>();
		products.add(new Product());
		List<String> productList = new ArrayList<>();
		when(productBridgeService.fetchMultipleProduct(KeyType.MULTIPLE_PRODUCTS, productList)).thenReturn(products);

		List<Product> result = underTest.getProductsByIds(productList, authContex);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedProductTableData() throws IOException, ProcessingException {
		Example<Product> e = Example.of(new Product(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		Sort sort = new Sort();
		sort.setField("productId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		when(productBridgeService.pageIt(any(Pageable.class), any(Example.class))).thenReturn(getProducts());
//		Page<Product> product = underTest.getPaginatedProductTableData(0, 15, new Product(), StringMatcher.DEFAULT,
//				sort, authContex);
//		Assert.assertNotNull(product);
	}

	@Test
	public void testGetProduct() throws IOException, ProcessingException {
		when(productBridgeService.fetchOneProduct(any(Product.class))).thenReturn(new Product());
		Product result = underTest.getProduct("productId", authContex);
		Assert.assertNotNull(result);

	}

	@Test
	public void testGetPaginatedProducts() throws IOException, ProcessingException {
		Example<Product> e = Example.of(new Product(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		Sort sort = new Sort();
		sort.setField("productId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());

		List<String> productList = new ArrayList<>();
		when(productBridgeService.pageIt(any(Pageable.class), anyString(), anyList())).thenReturn(getProducts());

		Page<Product> product = underTest.getPaginatedProducts(0, 15, new Product(), sort, productList, authContex);
		Assert.assertNotNull(product);
	}

	private final Page<Product> getProducts() {
		List<Product> product = new ArrayList<Product>();
		product.add(new Product());
		Page<Product> pageProduct = new PageImpl(product);
		return pageProduct;
	}

}