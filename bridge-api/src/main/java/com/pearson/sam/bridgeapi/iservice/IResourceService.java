package com.pearson.sam.bridgeapi.iservice;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Resource;

public interface IResourceService {

	Resource create(Resource resource);
	
	Resource update(Resource resource);
	
	Resource fetchOne(Resource resource);
	
	List<Resource> fetchMultiple(KeyType key, List<String> ids);
	
	Resource delete(Resource object);
	
	Page<Resource> pageIt(Pageable pageable, Example<Resource> e);
}
