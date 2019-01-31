package com.pearson.sam.bridgeapi.repository;


import com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductElasticSearchRepository extends ElasticsearchRepository<ProductSearch, String> {

	ProductSearch findByProductId(String productId);

}
