/**
 * 
 */
package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IAccessCodeBridgeService;
import com.pearson.sam.bridgeapi.iservice.IAccessCodeRepoService;
import com.pearson.sam.bridgeapi.iservice.IAccessCodeSearchService;
import com.pearson.sam.bridgeapi.iservice.IUserService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.GraphQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author VGUDLSA
 *
 */

@Component
public class AccessCodeBridgeServiceImpl implements IAccessCodeBridgeService {
	
	@Autowired
	IAccessCodeRepoService accessCodeRepoService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IAccessCodeSearchService accessCodeSearchService;

	
  public void loadAccessToMaster() {
    initLoad(PageRequest.of(0, accessCodeRepoService.getTotalCount().intValue()));
  }

  private static Example<AccessCodes> accessEx = Example.of(new AccessCodes());

  private void initLoad(Pageable pageable) {
    Page<AccessCodes> accessCodePage = accessCodeRepoService.pageIt(pageable, accessEx);
    loadAccessCodesinListToElastic(accessCodePage.getContent());    
  }



	
  
  private List<AccessCodes> loadAccessCodesinListToElastic(List<AccessCodes> accessCodeList) {
    DataLoaderOptions dlo = new DataLoaderOptions();
    dlo.setBatchingEnabled(true);
    dlo.setCachingEnabled(true);
    dlo.setMaxBatchSize(Utils.batchSizeCount(accessCodeList.size()));

    BatchLoader<AccessCodes, AccessCodes> batchLoader = (List<AccessCodes> accessCodes) -> {
      return CompletableFuture.supplyAsync(() -> {
        this.loadAccessCodeToElastic(accessCodes);
        return accessCodes;
      });
    };
    
    DataLoader<AccessCodes,AccessCodes> usersDataLoader = new DataLoader<>(batchLoader, dlo);
    CompletableFuture<List<AccessCodes>> cf = usersDataLoader.loadMany(accessCodeList);
    usersDataLoader.dispatchAndJoin();

    List<AccessCodes> l = null;
    try {
      l = cf.get();
    } catch (InterruptedException e) {
      throw new GraphQLException(e.getMessage());
    } catch (ExecutionException e) {
      throw new GraphQLException(e.getMessage());
    }

    return l;
  }

  private void loadAccessCodeToElastic(List<AccessCodes> accessCodes) {
    List<AccessCodeSearch> accessCodeSearchList = accessCodes.parallelStream().map(this::getAccessCodeSearchObject).collect(Collectors.toList());
    accessCodeSearchService.saveAll(accessCodeSearchList);
  }

  
	
	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IAccessCodeRepoBridgeService#create(com.pearson.sam.bridgeapi.model.AccessCodes)
	 */
	@Override
	public AccessCodes create(AccessCodes accesscode) {
		if(null != accesscode.getCreatedBy() && userService.checkAvailability(accesscode.getCreatedBy()))
			throw new BridgeApiGraphqlException("CreatedBy user not found");
		accesscode =  accessCodeRepoService.create(accesscode);
		accessCodeSearchService.save(getAccessCodeSearchObject(accesscode));
		return accesscode;
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IAccessCodeRepoBridgeService#update(com.pearson.sam.bridgeapi.model.AccessCodes)
	 */
	@Override
	public AccessCodes update(AccessCodes accesscode) {
		if(null != accesscode.getCreatedBy() && userService.checkAvailability(accesscode.getCreatedBy()))
				throw new BridgeApiGraphqlException("CreatedBy user not found");
		return accessCodeRepoService.update(accesscode);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IAccessCodeRepoBridgeService#fetchOne(com.pearson.sam.bridgeapi.model.AccessCodes)
	 */
	@Override
	public AccessCodes fetchOne(AccessCodes accesscode) {
		return accessCodeRepoService.fetchOne(accesscode);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IAccessCodeRepoBridgeService#fetchMultiple(com.pearson.sam.bridgeapi.model.KeyType, java.util.List)
	 */
	@Override
	public List<AccessCodes> fetchMultiple(KeyType key, List<String> ids) {
		return accessCodeRepoService.fetchMultiple(key, ids);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IAccessCodeRepoBridgeService#pageIt(org.springframework.data.domain.Pageable, org.springframework.data.domain.Example)
	 */
	@Override
	public Page<AccessCodes> pageIt(Pageable pageable, Example<AccessCodes> e) {
		return accessCodeRepoService.pageIt(pageable, e);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IAccessCodeRepoBridgeService#generateAccessCodes(com.pearson.sam.bridgeapi.model.AccessCodes)
	 */
	@Override
	public List<AccessCodes> generateAccessCodes(AccessCodes accessCodedata) {
		if(null != accessCodedata.getCreatedBy() && userService.checkAvailability(accessCodedata.getCreatedBy()))
			throw new BridgeApiGraphqlException("CreatedBy user not found");
		List<AccessCodes> accessCodes = accessCodeRepoService.generateAccessCodes(accessCodedata);
		accessCodeSearchService.saveAll(getAccessCodeSearchObject(accessCodes));
		return accessCodes;
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IAccessCodeRepoBridgeService#getAccessCodesFromBatch(java.lang.String)
	 */
	@Override
	public List<AccessCodes> getAccessCodesFromBatch(String batch) {
		return accessCodeRepoService.getAccessCodesFromBatch(batch);
	}
	
	@Override
	public Page<AccessCodeSearch> search(Pageable pageable, String searchable) {
		return accessCodeSearchService.search(pageable, searchable);
	}
	
	private AccessCodeSearch getAccessCodeSearchObject(AccessCodes accesscode) {
		AccessCodeSearch accessCodeSearch = new AccessCodeSearch();
		Utils.copyNonNullProperties(accesscode, accessCodeSearch);
		return accessCodeSearch;
	}
	
	private List<AccessCodeSearch> getAccessCodeSearchObject(List<AccessCodes> accesscodes) {
		List<AccessCodeSearch> accessCodeSearchs = new ArrayList<>();
		for(AccessCodes codes:accesscodes)
			accessCodeSearchs.add(getAccessCodeSearchObject(codes));
		return accessCodeSearchs;
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.ibridgeservice.IAccessCodeBridgeService#pageIt(org.springframework.data.domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch)
   */
  @Override
  public Page<AccessCodeSearch> pageIt(Pageable pageable, AccessCodeSearch searchable,StringMatcher sm) {
    return accessCodeSearchService.pageIt(pageable, searchable,sm);
  }

}
