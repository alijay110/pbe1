package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.User;

import io.jsonwebtoken.lang.Collections;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

public class AccessCodeRepositoryImpl1 extends BaseRepositoryImpl<AccessCodes, String>
implements MongoRepository<AccessCodes, String> {


  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#findAll()
   */
  @Override
  public List<AccessCodes> findAll() {
    // TODO Auto-generated method stub
    return super.findAll();
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#findAllById(java.lang.Iterable)
   */
  @Override
  public Iterable<AccessCodes> findAllById(Iterable<String> ids) {
    // TODO Auto-generated method stub
    return super.findAllById(ids);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#findAll(org.springframework.data.domain.Pageable)
   */
  @Override
  public Page<AccessCodes> findAll(Pageable pageable) {
    // TODO Auto-generated method stub
    return super.findAll(pageable);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#findAll(org.springframework.data.domain.Sort)
   */
  @Override
  public List<AccessCodes> findAll(Sort sort) {
    // TODO Auto-generated method stub
    return super.findAll(sort);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#insert(com.pearson.sam.bridgeapi.model.BaseModel)
   */
  @Override
  public <S extends AccessCodes> S insert(S entity) {
    // TODO Auto-generated method stub
    return super.insert(entity);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#insert(java.lang.Iterable)
   */
  @Override
  public <S extends AccessCodes> List<S> insert(Iterable<S> entities) {
    // TODO Auto-generated method stub
    return super.insert(entities);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#findAll(org.springframework.data.domain.Example, org.springframework.data.domain.Pageable)
   */
  @Override
  public <S extends AccessCodes> Page<S> findAll(Example<S> example, Pageable pageable) {
    // TODO Auto-generated method stub
    return super.findAll(example, pageable);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#findAll(org.springframework.data.domain.Example, org.springframework.data.domain.Sort)
   */
  @Override
  public <S extends AccessCodes> List<S> findAll(Example<S> example, Sort sort) {
    // TODO Auto-generated method stub
    return super.findAll(example, sort);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#findAll(org.springframework.data.domain.Example)
   */
  @Override
  public <S extends AccessCodes> List<S> findAll(Example<S> example) {
    // TODO Auto-generated method stub
    return super.findAll(example);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#findOne(org.springframework.data.domain.Example)
   */
  @Override
  public <S extends AccessCodes> Optional<S> findOne(Example<S> example) {
    // TODO Auto-generated method stub
    return super.findOne(example);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#count(org.springframework.data.domain.Example)
   */
  @Override
  public <S extends AccessCodes> long count(Example<S> example) {
    // TODO Auto-generated method stub
    return super.count(example);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.BaseRepositoryImpl#exists(org.springframework.data.domain.Example)
   */
  @Override
  public <S extends AccessCodes> boolean exists(Example<S> example) {
    // TODO Auto-generated method stub
    return super.exists(example);
  }

  /* (non-Javadoc)
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findById(java.lang.Object)
   */
  @Override
  public Optional<AccessCodes> findById(String id) {
    // TODO Auto-generated method stub
    return super.findById(id);
  }

  private static final Logger logger = LoggerFactory.getLogger(AccessCodeRepositoryImpl1.class);

  /**
   * @param metadata
   * @param mongoOperations
   */
  public AccessCodeRepositoryImpl1(@Autowired MongoOperations mongoOperations) {
    super(new MongoRepositoryFactory(mongoOperations).getEntityInformation(AccessCodes.class),
        mongoOperations);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.AccessCodeRepository#findByCode(java.lang.String)
   */
  public AccessCodes findByCode(String code) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.AccessCodeRepository#deleteByCode(java.lang.String)
   */
  public void deleteByCode(String code) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.AccessCodeRepository#findTopByOrderByIdDesc()
   */
  public AccessCodes findTopByOrderByIdDesc() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.AccessCodeRepository#findByBatch(java.lang.String)
   */
  public List<AccessCodes> findByBatch(String batch) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.AccessCodeRepository#existsByCode(java.lang.String)
   */
  public Boolean existsByCode(String code) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.AccessCodeRepository#findByAccessCode(java.lang.String)
   */
  public AccessCodes findByAccessCode(String accessCode) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.repository.AccessCodeRepository#findByLastActivatedBy(java.lang.String)
   */
  public List<AccessCodes> findByLastActivatedBy(String userName) {
    // TODO Auto-generated method stub
    return null;
  }
  
  
  private <S extends AccessCodes> Example<S> getFilter(Example<S> e) {
    SessionUser user = getLoggedInUser(true);

    //logger.info("logged in user: {}", user);

    e = createNewFilterIfNull(e);

    Set<String> roles = user.getRoles();

    if (roles.isEmpty()) {
      throw new BridgeApiGraphqlException("No roles associated with logged in user!");
    }

    if (roles.contains("customer-care")) {
      // }else if(roles.contains("teacher")) {
      // List<String> classes= user.getClassroom();
      // if(Collections.isEmpty(classes))
      // {
      // throw new BridgeApiGraphqlException("No class associated with logged in user!");
      // }
      // e.getProbe().setClassroom(classes);
    } else if (roles.contains("teacher") || roles.contains("channel-partner")
        || roles.contains("school-admin")) {
      List<String> schools = user.getSchool();
      if (Collections.isEmpty(schools)) {
        throw new BridgeApiGraphqlException("No school associated with logged in user!");
      }
//      e.getProbe().setSchool(user.getSchool());
      // }else if(roles.contains("channel-partner")) {
      // e.getProbe().setOrganisation(user.getOrganisation());
    } else {
//      e.getProbe().setUid(user.getUid());
//      e.getProbe().setUid(user.getUserId());
    }
    return e;
  }

  private <S extends AccessCodes> Example<S> createNewFilterIfNull(Example<S> e) {
    if (e == null) {
      return (Example<S>) Example.of(new AccessCodes());
    }
    return e;
  }

}