package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.Product;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

  void deleteByProductId(String productId);

  Product findByProductId(String productId);

  List<Product> findAllByType(String type);

  List<Product> findAllByProductIdIn(Set<String> productId);
  
  Page<Product> findAllByProductIdIn(List<String> productIds, Pageable pageable);

  Product findTopByOrderByIdDesc();
  
  public boolean existsByProductId(String productId);

}
