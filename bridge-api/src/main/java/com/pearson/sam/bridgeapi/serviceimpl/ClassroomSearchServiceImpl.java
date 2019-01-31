/**
 * 
 */
package com.pearson.sam.bridgeapi.serviceimpl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import static com.pearson.sam.bridgeapi.util.Utils.getQueryBuilder;

import com.pearson.sam.bridgeapi.elasticsearch.model.ClassroomSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.iservice.IClassroomSearchService;
import com.pearson.sam.bridgeapi.repository.ClassRoomElasticSearchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

/**
 * @author VGUDLSA
 *
 */
@Service
public class ClassroomSearchServiceImpl implements IClassroomSearchService {
	
	@Autowired
	ClassRoomElasticSearchRepository classRoomElasticSearchRepository;

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IClassroomSearchService#search(org.springframework.data.domain.Pageable, java.lang.String)
	 */
	@Override
	public Page<ClassroomSearch> search(Pageable pageable, String searchable) {
		return classRoomElasticSearchRepository.search(queryStringQuery(searchable),pageable);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IClassroomSearchService#save(com.pearson.sam.bridgeapi.elasticsearch.model.ClassroomSearch)
	 */
	@Override
	public ClassroomSearch save(ClassroomSearch classroomSearch) {
		return classRoomElasticSearchRepository.save(classroomSearch);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IClassroomSearchService#update(com.pearson.sam.bridgeapi.elasticsearch.model.ClassroomSearch)
	 */
	@Override
	public ClassroomSearch update(ClassroomSearch classroomSearch) {
		ClassroomSearch classroomSearch1 = classRoomElasticSearchRepository.findByClassroomId(classroomSearch.getClassroomId());
		
		if(Optional.ofNullable(classroomSearch1).isPresent()){
			classroomSearch.setId(classroomSearch1.getId());
		}
		
		if(!Optional.ofNullable(classroomSearch.getId()).isPresent())	{
			throw new BridgeApiGeneralException("Id doesn't exists!");
		}
		return classRoomElasticSearchRepository.save(classroomSearch);	
	}

	@Override
	public void delete(ClassroomSearch classroomSearch) {
		ClassroomSearch classroomSearch1 = classRoomElasticSearchRepository.findByClassroomId(classroomSearch.getClassroomId());
		classroomSearch.setId(classroomSearch1.getClassroomId());
		classRoomElasticSearchRepository.delete(classroomSearch);
	}

	@Override
	public Page<ClassroomSearch> pageIt(Pageable pageable, ClassroomSearch searchable,StringMatcher sm) {
		return classRoomElasticSearchRepository.search(getQueryBuilder(searchable,sm),pageable);
	}

}
