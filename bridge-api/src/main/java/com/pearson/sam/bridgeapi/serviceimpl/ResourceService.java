package com.pearson.sam.bridgeapi.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Resource;
import com.pearson.sam.bridgeapi.repository.ResourceRepository;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.IResourceService;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

@Component
public class ResourceService implements IResourceService{

	private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

	@Autowired
	ResourceRepository resourceRepository;

	
	public Resource create(Resource resource) {
		//logger.info("Creating Resource {}");
		// Setting Resource Values
		Resource mergedData = resourceRepository.save(resource);
		return mergedData;
	}

	public Resource update(Resource resource) {
		/*//logger.info("Updating Resource {}");
		resource.setUpdateBy(null != resource.getUpdateBy() ? resource.getUpdateBy() : "PP");
		resource.setUpdatedOn(null != resource.getUpdatedOn() ? resource.getUpdatedOn() : new Date().toString());
		Resource dbResource = resourceRepository.findByResourceId(resource.getResourceId());
		if (!Optional.ofNullable(dbResource).isPresent()) {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, "Resource doesn't exists");
		}
		Utils.copyNonNullProperties(resource, dbResource);
		dbResource = resourceRepository.save(dbResource);*/
		return null;
	}

	
	public Resource fetchOne(Resource resource) {
		/*//logger.info("Getting Resource {}");
		Resource dbResource = resourceRepository.findByResourceId(resource.getResourceId());*/
		return null;
	}

	public List<Resource> fetchMultiple(KeyType key, List<String> ids) {
		return null;
	}

	public Resource delete(Resource object) {
		return null;
	}

	public Page<Resource> pageIt(Pageable pageable, Example<Resource> e) {
		//logger.info("Getting paginated Resource {}");
		return resourceRepository.findAll(e, pageable);
	}

}