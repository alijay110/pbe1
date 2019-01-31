package com.pearson.sam.bridgeapi.ibridgeservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Product;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @author VKu99Ma
 *
 */
public interface IProductBridgeService {

	/**
	 * This Method will add the Product.
	 * 
	 * @param method
	 * @param product
	 * @return Product
	 */
	public Product create(Product product);

	/**
	 * This Method will update the Product.
	 * 
	 * @param method
	 * @param userProduct
	 * @return Product
	 */
	public Product updateProduct(Product userProduct);

	/**
	 * This Method will fetch one Product.
	 * 
	 * @param product
	 * @return Product
	 */
	public Product fetchOneProduct(Product product);

	/**
	 * This Method will fetch fetch Multiple products based on input ids.
	 * 
	 * @param key
	 * @param ids
	 * @return List<Product>
	 */
	public List<Product> fetchMultipleProduct(KeyType key, List<String> ids);

	/**
	 * This Method will remove the Product from DB.
	 * 
	 * @param product
	 * @return Product
	 */
	public Product removeProduct(Product product);

	/**
	 * This Method will give the paginated products
	 * 
	 * @param pageable
	 * @param e
	 * @return Page<Product>
	 */
	public Page<Product> pageIt(Pageable pageable, Example<Product> e);

	/**
	 * This Method will give the paginated products based on input productIds.
	 * 
	 * @param pageable
	 * @param name
	 * @param productIds
	 * @return Page<Product>
	 */
	public Page<Product> pageIt(Pageable pageable, String name, List<String> productIds);

	Page<ProductSearch> search(Pageable pageable, String searchable);

  /**
   * pageIt.
   * @param pageable
   * @param query
   * @return  
   */
  public Page<ProductSearch> pageIt(Pageable pageable, ProductSearch query,StringMatcher sm);

}