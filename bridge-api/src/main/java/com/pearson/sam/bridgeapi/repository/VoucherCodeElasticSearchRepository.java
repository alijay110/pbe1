package com.pearson.sam.bridgeapi.repository;


import com.pearson.sam.bridgeapi.elasticsearch.model.VoucherCodeSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherCodeElasticSearchRepository extends ElasticsearchRepository<VoucherCodeSearch, String> {

	VoucherCodeSearch findByVoucherCode(String voucherCode);

}
