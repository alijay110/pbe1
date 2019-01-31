package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.Indexes;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexRepository extends MongoRepository<Indexes, String> {

  Indexes findBySeqName(String seqname);

}
