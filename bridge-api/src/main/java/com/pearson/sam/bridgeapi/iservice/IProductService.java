package com.pearson.sam.bridgeapi.iservice;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.model.Product;

/**
 * 
 * @author VKu99Ma
 *
 */
public interface IProductService {

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
	public Product update(Product userProduct);

	/**
	 * This Method will fetch one Product.
	 * 
	 * @param product
	 * @return Product
	 */
	public Product fetchOne(Product product);

	/**
	 * This Method will fetch fetch Multiple products based on input ids.
	 * 
	 * @param key
	 * @param ids
	 * @return List<Product>
	 */
	public List<Product> fetchMultiple(List<String> ids);

	/**
	 * 
	 * @param productId
	 * @return
	 */
	List<Product> getMultipleProducts(Set<String> productId);

	/**
	 * This Method will delete the Product from DB.
	 * 
	 * @param product
	 * @return Product
	 */
	public Product delete(Product product);

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

	/**
	 * 
	 * @param productList
	 * @return
	 */
	public List<Product> getProductsList(List<Product> productList);
	
	/**
	 * 
	 * @param AssetsList
	 * @return
	 */
	
	public Map<String, Object> getLearningMaterial(String orgId,String courseId, String userId);

  /**
   * getTotalCount.
   * @return  
   */
  public Long getTotalCount();

  /**
   * findProductsToLoadIntoElastic.
   * @return  
   */
  List<Product> findProductsToLoadIntoElastic();

}