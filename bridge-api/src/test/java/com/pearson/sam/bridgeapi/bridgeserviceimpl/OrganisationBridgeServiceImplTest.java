package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.OrganisationBridgeServiceImpl;
import com.pearson.sam.bridgeapi.iservice.IOrganisationService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Organisation;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class OrganisationBridgeServiceImplTest {
	
	@Mock
	private IOrganisationService organisationService;
	
	@Mock
	private ISessionFacade sessionFacade;
	
	@InjectMocks
	private OrganisationBridgeServiceImpl underTest = new OrganisationBridgeServiceImpl();
	
	@Test
	public void testCreate() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when(organisationService.create(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = underTest.create(getOrganisation());
		Assert.assertNotNull(organisation);
	}
	
	@Test
	public void testUpdate() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when(organisationService.update(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = underTest.update(getOrganisation());
		Assert.assertNotNull(organisation);
	}
	
	@Test
	public void testFetch() {
		when(organisationService.fetch(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = underTest.fetch(getOrganisation());
		Assert.assertNotNull(organisation);
	}
	
	@Test
	public void testPageIt( ) {
		Sort sort = new Sort();
		sort.setField("accessCode");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10, org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Organisation> e = Example.of(new Organisation(), ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(organisationService.pageIt(any(Pageable.class),any(Example.class))).thenReturn(getOrganisationList());
		Page<Organisation> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}
	
	@Test
	public void testPageIt1( ) {
		Sort sort = new Sort();
		sort.setField("accessCode");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10, org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Organisation> e = Example.of(new Organisation(), ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(organisationService.pageIt(any(Pageable.class),anyString(),anyList())).thenReturn(getOrganisationList());
		List<String> ids = Arrays.asList("id1","id2");
		Page<Organisation> page = underTest.pageIt(pageable,"name", ids);
		Assert.assertNotNull(page);
	}
	
	@Test
	public void testFindByOrganisationId() {
		when(organisationService.findByOrganisationId(anyString())).thenReturn(getOrganisation());
		Organisation organisation = underTest.findByOrganisationId(anyString());
		Assert.assertNotNull(organisation);
	}
	
	@Test
	public void testDelete() {
		when(organisationService.delete(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = underTest.delete(getOrganisation());
		Assert.assertNotNull(organisation);
	}
	
	@Test
	public void testFetchMultiple() {
		List<String> ids = Arrays.asList("id1","id2");
		List<Organisation> lstOrganisation = Arrays.asList(getOrganisation(),getOrganisation());
		when(organisationService.fetchMultiple(anyObject(),anyList())).thenReturn(lstOrganisation);	
		lstOrganisation = underTest.fetchMultiple(KeyType.SCHOOL_ID,ids);
		Assert.assertNotNull(lstOrganisation);
	}
	
	private Organisation getOrganisation() {
		Organisation organisation = new Organisation();
		organisation.setName("Mario");
		organisation.setOrganisationId("PP2ORG111");
		organisation.setLocation("BNG");
		List<String> schools = Arrays.asList("school1","school2");
		organisation.setSchools(schools);
		return organisation;
	}
	
	private SessionUser getSessionUser() {
		SessionUser sessionUser = new SessionUser();
		sessionUser.setUserName("mlsreekanth");
		sessionUser.setUid("chandraBoseCC1");
		return sessionUser;
	}
	
	private Page<Organisation> getOrganisationList(){
		Organisation organisation = new Organisation();
		organisation.setName("Mario");
		organisation.setOrganisationId("PP2ORG111");
		organisation.setLocation("BNG");
		List<String> schools = Arrays.asList("school1","school2");
		organisation.setSchools(schools);
		Organisation organisation1 = new Organisation();
		organisation1.setName("Mario");
		organisation1.setOrganisationId("PP2ORG111");
		organisation1.setLocation("BNG");
		organisation1.setSchools(schools);
		List<Organisation> listOrganisation = Arrays.asList(organisation,organisation1,getOrganisation());
		Page<Organisation> pageOrganisation = new PageImpl<Organisation>(listOrganisation);
		return pageOrganisation;
	}

}
