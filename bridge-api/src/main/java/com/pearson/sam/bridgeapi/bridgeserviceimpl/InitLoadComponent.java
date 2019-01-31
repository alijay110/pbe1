/**
 * 
 */
package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import com.pearson.sam.bridgeapi.serviceimpl.CacheService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author VKuma09
 *
 */
@Component
public class InitLoadComponent implements ApplicationRunner, Ordered {

  private static final Logger logger = LoggerFactory.getLogger(InitLoadComponent.class);

  @Autowired
  UserBridgeServiceImpl userBService;

  @Autowired
  ProductBridgeServiceImpl pdtBService;

  
  @Autowired
  SchoolBridgeServiceImpl schBService;

  
  @Autowired
  ClassRoomBridgeServiceImpl clsBService;

  
  @Autowired
  AccessCodeBridgeServiceImpl acsBService;

  
  @Autowired
  VoucherBridgeServiceImpl vchrBService;

  
//  @Autowired
//  CacheService cacheService;

  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
   */
  @Override
  public void run(ApplicationArguments args) throws Exception {/*
    //logger.info("*****TOMCAT STARTED*****");
    //logger.info("*****USERS LOADING*****");
//    cacheService.clearAll();
    userBService.loadUsersToMaster();
    //logger.info("*****USERS LOADED*****");
    
    //logger.info("*****PRODUCTS LOADING*****");
    pdtBService.loadProductsToMaster();
    //logger.info("*****PRODUCTS LOADED*****");

    
    
    
    //logger.info("*****SCHOOLS LOADING*****");
    schBService.loadSchoolToMaster();;
    //logger.info("*****SCHOOLS LOADED*****");

    
    //logger.info("*****CLASSROOM LOADING*****");
    clsBService.loadClassroomsToMaster();
    //logger.info("*****CLASSROOM LOADED*****");

    
    //logger.info("*****ACCESSCODE LOADING*****");
    acsBService.loadAccessToMaster();
    //logger.info("*****ACCESSCODE LOADED*****");

    
    //logger.info("*****VOUCHER LOADING*****");
    vchrBService.loadVouchersToMaster();
    //logger.info("*****VOUCHER LOADED*****");

  */}
}
