package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.User;

import io.jsonwebtoken.lang.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

@Qualifier("UserRepositoryImpl")
public class UserRepositoryImpl1 extends BaseRepositoryImpl<User, String>
    implements MongoRepository<User, String> {

  private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl1.class);

  /**
   * @param metadata
   * @param mongoOperations
   */
  public UserRepositoryImpl1(@Autowired MongoOperations mongoOperations) {
    super(new MongoRepositoryFactory(mongoOperations).getEntityInformation(User.class),
        mongoOperations);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.pearson.sam.bridgeapi.repository.UserRepository#findByUid(java.lang.String)
   */
  //
  public User findByUid(String uid) {
    logger.info("findByUid:{}", uid);
    return findByIdentifier(uid);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.pearson.sam.bridgeapi.repository.UserRepository#findByEmail(java.lang.String)
   */
  //
  public User findByEmail(String email) {
    logger.info("findByEmail:{}", email);

    User user = new User();
    user.setEmail(email);
    Example<User> example = Example.of(user);

    Optional<User> optionalUser = super.findOne(example);

    return optionalUser.isPresent() ? optionalUser.get() : null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.pearson.sam.bridgeapi.repository.UserRepository#existsByEmail(java.lang.String)
   */
  //
  public boolean existsByEmail(String email) {
    logger.info("existsByEmail:{}", email);

    User user = new User();
    user.setEmail(email);
    Example<User> example = Example.of(user);

    return super.exists(example);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.pearson.sam.bridgeapi.repository.UserRepository#countBySchool(java.lang.String)
   */
  //
  public Long countBySchool(String schoolId) {
    logger.info("countBySchool:{}", schoolId);

    User user = new User();
    List<String> school = new ArrayList<>();
    school.add(schoolId);
    user.setSchool(school);
    Example<User> example = Example.of(user);

    return super.count(example);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.pearson.sam.bridgeapi.repository.UserRepository#existsByUid(java.lang.String)
   */
  //
  public boolean existsByUid(String uid) {
    logger.info("existsByUid:{}", uid);

    User user = new User();
    user.setIdentifier(uid);
    Example<User> example = Example.of(user);

    return super.exists(example);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.pearson.sam.bridgeapi.repository.UserRepository#findByIdentifier(java.lang.String)
   */
  //
  public User findByIdentifier(String uid) {
    logger.info("findByIdentifier:{}", uid);

    User user = new User();
    user.setIdentifier(uid);
    Example<User> example = Example.of(user);

    logger.info("loggedin user: {} - trying to retrieve {}", getLoggedInUser(false).getUserId(),
        uid);

    // if (!uid.equals(getLoggedInUser(false).getUserId())) {
    // example = getFilter(example);
    // }

    Optional<User> optionalUser = super.findOne(example);
    return optionalUser.isPresent() ? optionalUser.get() : null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.data.mongodb.repository.support.SimpleMongoRepository#findById(java.lang.
   * Object)
   */
  @Override
  public Optional<User> findById(String id) {
    Example<User> ex = Example.of(new User());
    ex.getProbe().setId(id);
    return super.findOne(getFilter(ex));
  }

  @Override
  public List<User> findAll() {
    logger.info("findAll() {}", getLoggedInUser(true));
    return super.findAll(getFilter(null));
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    logger.info("findAll {} {}", pageable, getLoggedInUser(true));
    return super.findAll(getFilter(null), pageable);
  }

  @Override
  public List<User> findAll(Sort sort) {
    logger.info("findAll {} {}", sort, getLoggedInUser(true));
    return super.findAll(getFilter(null), sort);
  }

  @Override
  public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
    logger.info("findAll {} {} {}", example, pageable, getLoggedInUser(true));
    return super.findAll(getFilter(example), pageable);
  }

  @Override
  public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
    logger.info("findAll {} {} {}", example, sort, getLoggedInUser(true));
    return super.findAll(getFilter(example), sort);
  }

  @Override
  public <S extends User> List<S> findAll(Example<S> example) {
    logger.info("findAll {} {}", example, getLoggedInUser(true));
    return super.findAll(getFilter(example));
  }

  @Override
  public <S extends User> Optional<S> findOne(Example<S> example) {
    logger.info("findOne {} {}", example, getLoggedInUser(true));
    return super.findOne(getFilter(example));
  }

  private <S extends User> Example<S> getFilter(Example<S> e) {
    SessionUser user = getLoggedInUser(true);

    logger.info("logged in user: {}", user);

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
      e.getProbe().setSchool(user.getSchool());
      // }else if(roles.contains("channel-partner")) {
      // e.getProbe().setOrganisation(user.getOrganisation());
    } else {
      e.getProbe().setUid(user.getUid());
    }
    return e;
  }

  private <S extends User> Example<S> createNewFilterIfNull(Example<S> e) {
    if (e == null) {
      return (Example<S>) Example.of(new User());
    }
    return e;
  }

}