package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.Voucher;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends MongoRepository<Voucher, String> {

	public Voucher findByVoucherId(String voucherId);
	
	public Voucher findByVoucherCode(String voucherCode);
	
	public void deleteByVoucherCode(String voucherCode);

	public Voucher findTopByOrderByIdDesc();
	
	public List<Voucher> findByBatch(String batch);

}
