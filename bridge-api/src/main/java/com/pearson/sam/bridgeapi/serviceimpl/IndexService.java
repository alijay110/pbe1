package com.pearson.sam.bridgeapi.serviceimpl;

import com.pearson.sam.bridgeapi.model.Indexes;
import com.pearson.sam.bridgeapi.repository.IndexRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndexService {

  @Autowired
  IndexRepository repo;

  /**
   * getNext.
   * 
   * @return
   */
  public int getNextId(String seqName) {
    Indexes index = repo.findBySeqName(seqName);

    if (index == null) {
      index = new Indexes();
      index.setSeqName(seqName);
      repo.save(index);
    }

    index.setCounter(index.getCounter() + 1);
    return repo.save(index).getCounter();
  }
}
