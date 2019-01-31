package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.ibridgeservice.IOrganisationBridgeService;
import com.pearson.sam.bridgeapi.iservice.IOrganisationService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Organisation;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

@Service
public class OrganisationBridgeServiceImpl implements IOrganisationBridgeService{
	private static final Logger logger = LoggerFactory.getLogger(OrganisationBridgeServiceImpl.class);

	@Autowired
	private IOrganisationService organisationService;
	
	@Autowired
	private ISessionFacade sessionFacade;
	
	@Override
	public Organisation create(Organisation organisation) {
		// TODO Auto-generated method stub
		User user = sessionFacade.getLoggedInUser(true);
		organisation.setCreatedBy(user.getUid());
		return organisationService.create(organisation);
	}

	@Override
	public Organisation update(Organisation organisation) {
		// TODO Auto-generated method stub
		User user = sessionFacade.getLoggedInUser(true);
		organisation.setUpdatedBy(user.getUid());
		return organisationService.update(organisation);
	}

	@Override
	public Organisation fetch(Organisation organisation) {
		// TODO Auto-generated method stub
		return organisationService.fetch(organisation);
	}

	@Override
	public Page<Organisation> pageIt(Pageable pageable, Example<Organisation> e) {
		// TODO Auto-generated method stub
		Page<Organisation> mongoOrganisations = organisationService.pageIt(pageable, e);
		return PageableExecutionUtils.getPage(getAllOrganisationsinList( mongoOrganisations.getContent()), pageable, () -> {return mongoOrganisations.getTotalElements();} );

	}

	@Override
	public Page<Organisation> pageIt(Pageable pageable, String name, List<String> organisationIds) {
		// TODO Auto-generated method stub
		Page<Organisation> mongoOrganisations = organisationService.pageIt(pageable, name, organisationIds);
		return PageableExecutionUtils.getPage(getAllOrganisationsinList( mongoOrganisations.getContent()), pageable, () -> {return mongoOrganisations.getTotalElements();} );
	}

	@Override
	public Organisation findByOrganisationId(String organisationId) {
		// TODO Auto-generated method stub
		return organisationService.findByOrganisationId(organisationId);
	}

	@Override
	public Organisation delete(Organisation organisation) {
		// TODO Auto-generated method stub
		return organisationService.delete(organisation);
	}

	@Override
	public List<Organisation> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub
		return organisationService.fetchMultiple(key, ids);
	}
	
	private List<Organisation> getAllOrganisationsinList(List<Organisation> organisationList) {
		DataLoaderOptions dlo = new DataLoaderOptions();
		dlo.setBatchingEnabled(true);
		dlo.setMaxBatchSize(Utils.batchSizeCount(organisationList.size()));

		BatchLoader<String, Organisation> batchLoader = (List<String> organisationIds) -> {
			//logger.info("calling batchLoader load...");
			List<Organisation> organisations = organisationIds.stream().map(organisationId -> {
				Organisation organisation = null;
					organisation = organisationService.findByOrganisationId(organisationId);
				return organisation;
			}).collect(Collectors.toList());

			return CompletableFuture.supplyAsync(() -> {
				return organisations;
			});
		};
		DataLoader<String, Organisation> organisationsDataLoader = new DataLoader<String, Organisation>(batchLoader, dlo);
		organisationList.stream().map(key -> organisationsDataLoader.load(key.getOrganisationId())).collect(Collectors.toList());
		return organisationsDataLoader.dispatchAndJoin();
	}

}
