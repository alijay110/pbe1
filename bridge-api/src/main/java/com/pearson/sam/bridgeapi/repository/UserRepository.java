package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.User;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  public User findByUid(String uid);

  public User findByEmail(String email);

  /**
   * existsByEmail.
   * 
   * @param email email
   * @return
   */
  public Boolean existsByEmail(String email);
  
  public Long countBySchool(String schoolId);
  
  public Boolean existsByUid(String uid);
  
  public User findByIdentifier(String uid);
}
