package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.BaseModel;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public class BaseRepositoryImpl<T extends BaseModel, ID extends Serializable>
    extends SimpleMongoRepository<T, ID> implements BaseRepository<T, ID> {

  private static final Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class);

  @Autowired
  protected ISessionFacade sessionFacade;

  /**
   * @param metadata
   * @param mongoOperations
   */
  public BaseRepositoryImpl(MongoEntityInformation<T, ID> metadata,
      MongoOperations mongoOperations) {
    super(metadata, mongoOperations);
  }

  protected SessionUser getLoggedInUser(boolean getLoggedInUser) {
    return sessionFacade.getLoggedInUser(getLoggedInUser);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#save(java.lang.
   * Object)
   */
  @Override
  public <S extends T> S save(S entity) {
    //logger.debug("save {} {}", entity, getLoggedInUser(false));
    return super.save(entity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.mongodb.repository.support.SimpleMongoRepository#saveAll(java.lang.
   * Iterable)
   */
  @Override
  public <S extends T> List<S> saveAll(Iterable<S> entities) {
    //logger.debug("saveAll {} {}", entities, getLoggedInUser(false));
    return super.saveAll(entities);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.mongodb.repository.support.SimpleMongoRepository#existsById(java.lang.
   * Object)
   */
  @Override
  public boolean existsById(ID id) {
    //logger.debug("existsById {} {}", id, getLoggedInUser(false));
    return super.existsById(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#count()
   */
  @Override
  public long count() {
    //logger.debug("count {}", getLoggedInUser(false));
    return super.count();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.mongodb.repository.support.SimpleMongoRepository#deleteById(java.lang.
   * Object)
   */
  @Override
  public void deleteById(ID id) {
    //logger.debug("deleteById {} {}", id, getLoggedInUser(false));
    throw new BridgeApiGraphqlException("operation not supported!");
    // super.deleteById(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.mongodb.repository.support.SimpleMongoRepository#delete(java.lang.
   * Object)
   */
  @Override
  public void delete(T entity) {
    //logger.debug(" delete {} {}", entity, getLoggedInUser(false));
    throw new BridgeApiGraphqlException("operation not supported!");
    // super.delete(entity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.mongodb.repository.support.SimpleMongoRepository#deleteAll(java.lang.
   * Iterable)
   */
  @Override
  public void deleteAll(Iterable<? extends T> entities) {
    //logger.debug("deleteAll {} {}", entities, getLoggedInUser(false));
    throw new BridgeApiGraphqlException("operation not supported!");
    // super.deleteAll(entities);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#deleteAll()
   */
  @Override
  public void deleteAll() {
    //logger.debug("deleteAll {}", getLoggedInUser(false));
    throw new BridgeApiGraphqlException("operation not supported!");
    // super.deleteAll();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findAll()
   */
  @Override
  public List<T> findAll() {
    //logger.debug("findAll() {}", getLoggedInUser(false));
    throw new BridgeApiGraphqlException("operation not supported!");
    // return super.findAll();
    // return findAll(filter);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findAllById(java.lang
   * .Iterable)
   */
  @Override
  public Iterable<T> findAllById(Iterable<ID> ids) {
    //logger.debug("findAllById {} {}", ids, getLoggedInUser(false));
    throw new BridgeApiGraphqlException("operation not supported!");
    // return super.findAllById(ids);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findAll(org.
   * springframework.data.domain.Pageable)
   */
  @Override
  public Page<T> findAll(Pageable pageable) {
    //logger.debug("findAll {} {}", pageable, getLoggedInUser(false));
    throw new BridgeApiGraphqlException("operation not supported!");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findAll(org.
   * springframework.data.domain.Sort)
   */
  @Override
  public List<T> findAll(Sort sort) {
    //logger.debug("findAll {} {}", sort, getLoggedInUser(false));
    throw new BridgeApiGraphqlException("operation not supported!");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.mongodb.repository.support.SimpleMongoRepository#insert(java.lang.
   * Object)
   */
  @Override
  public <S extends T> S insert(S entity) {
    //logger.debug("insert {} {}", entity, getLoggedInUser(false));
    return super.insert(entity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.mongodb.repository.support.SimpleMongoRepository#insert(java.lang.
   * Iterable)
   */
  @Override
  public <S extends T> List<S> insert(Iterable<S> entities) {
    //logger.debug("insert {} {}", entities, getLoggedInUser(false));
    return super.insert(entities);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findAll(org.
   * springframework.data.domain.Example, org.springframework.data.domain.Pageable)
   */
  @Override
  public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
    //logger.debug("findAll {} {} {}", example, pageable, getLoggedInUser(false));
    return super.findAll(example, pageable);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findAll(org.
   * springframework.data.domain.Example, org.springframework.data.domain.Sort)
   */
  @Override
  public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
    //logger.debug("findAll {} {} {}", example, sort, getLoggedInUser(false));
    return super.findAll(example, sort);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findAll(org.
   * springframework.data.domain.Example)
   */
  @Override
  public <S extends T> List<S> findAll(Example<S> example) {
    //logger.debug("findAll {} {}", example, getLoggedInUser(false));
    return super.findAll(example);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findOne(org.
   * springframework.data.domain.Example)
   */
  @Override
  public <S extends T> Optional<S> findOne(Example<S> example) {
    //logger.debug("findOne {} {}", example, getLoggedInUser(false));
    return super.findOne(example);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#count(org.
   * springframework.data.domain.Example)
   */
  @Override
  public <S extends T> long count(Example<S> example) {
    //logger.debug("count {} {}", example, getLoggedInUser(false));
    return super.count(example);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.data.mongodb.repository.support.SimpleMongoRepository#exists(org.
   * springframework.data.domain.Example)
   */
  @Override
  public <S extends T> boolean exists(Example<S> example) {
    //logger.debug("exists {} {}", example, getLoggedInUser(false));
    return super.exists(example);
  }

}