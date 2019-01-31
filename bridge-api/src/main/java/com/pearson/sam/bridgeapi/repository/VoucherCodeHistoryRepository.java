package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.VoucherCodeHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherCodeHistoryRepository extends MongoRepository<VoucherCodeHistory, String> {

	VoucherCodeHistory findTopByOrderByIdDesc();
	
	public VoucherCodeHistory findByVoucherCodeHistoryId(String voucherCodeHistoryId);
}
