package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.util.Utils.getQueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.iservice.IUserSearchService;
import com.pearson.sam.bridgeapi.repository.UserElasticSearchRepository;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author VGUDLSA
 *
 */
@Service
public class UserSearchServiceImpl implements IUserSearchService {
	
	@Autowired
	private UserElasticSearchRepository userElasticSearchRepository;

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IUserSearchService#search(org.springframework.data.domain.Pageable, java.lang.String)
	 */
	@Override
	public Page<UserSearch> search(Pageable pageable,String searchable) {
		return userElasticSearchRepository.search(queryStringQuery(searchable),pageable);
	}

	@Override
	public UserSearch save(UserSearch userSearch) {
		return userElasticSearchRepository.save(userSearch);
	}

	@Override
	public UserSearch update(UserSearch userSearch) {
		UserSearch userSearch1 = userElasticSearchRepository.findByUid(userSearch.getId());
		if(Optional.ofNullable(userSearch1).isPresent()){
			userSearch.setUid(userSearch1.getId());
		}
		
		if(!Optional.ofNullable(userSearch.getId()).isPresent())	{
			throw new BridgeApiGeneralException("Id doesn't exists!");
		}
		return userElasticSearchRepository.save(userSearch);		
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.IUserSearchService#pageIt(org.springframework.data.domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch)
   */
  @Override
  public Page<UserSearch> pageIt(Pageable pageable, UserSearch searchable,StringMatcher sm) {
    return userElasticSearchRepository.search(getQueryBuilder(searchable,sm),pageable);
  }

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.IUserSearchService#insertIfNotFound(com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch)
   */
  
  public static AtomicInteger a =new AtomicInteger(0);
  public static AtomicInteger b =new AtomicInteger(0);
  
  @Override
  public void insertIfNotFound(UserSearch userSearch) {
    System.out.println("ENTERED: "+a.incrementAndGet());
    if(userSearch.getUid()!=null)
    {
    Boolean exists = userElasticSearchRepository.findByUid(userSearch.getUid())!=null;
    if(!exists)
    {
      userElasticSearchRepository.save(userSearch);
      System.out.println("SUCCESS: "+b.incrementAndGet());
    }
    }else {
      System.err.println("ERROR: "+userSearch);
    }
  }

}
