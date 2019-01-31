package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.IOrganisationBridgeService;
import com.pearson.sam.bridgeapi.model.Organisation;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;

@RunWith(MockitoJUnitRunner.class)
public class OrganisationResolverTest {

	@Mock
	private IOrganisationBridgeService organisationBridgeService;

	@Mock
	private AuthContext authContex;
	
	@InjectMocks
	private OrganisationResolver underTest;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testCreateOrganisation() {
		when(organisationBridgeService.create(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation result = underTest.createOrganisation(new Organisation());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testUpdateOrganisation() {
		when(organisationBridgeService.update(any(Organisation.class))).thenReturn(new Organisation());
		Organisation result = underTest.updateOrganisation("organisationID", new Organisation());
		Assert.assertNotNull(result);
		}

	@Test
	public void testGetPaginatedOrganisationTableData() {		
		Sort sort = new Sort();
		sort.setField("organization");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10, org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Organisation> e = Example.of(new Organisation(), ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(organisationBridgeService.pageIt(any(Pageable.class),any(Example.class))).thenReturn(getOrganisationList());
		Page<Organisation> pageOrganisation = underTest.getPaginatedOrganisationTableData(0,15,new Organisation(),StringMatcher.DEFAULT,sort,authContex);
		Assert.assertNotNull(pageOrganisation);
	}
	
	@Test
	public void testGetMultipleOrganisationByIds() {
		Sort sort = new Sort();
		sort.setField("organizationId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10, org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Organisation> e = Example.of(new Organisation(), ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		List<String> ids = new ArrayList<String>();
		ids.add("code1");
		ids.add("code2");
		
		when(organisationBridgeService.pageIt(any(Pageable.class), anyString(), anyList())).thenReturn(getOrganisationList());
		Page<Organisation> pageOrganisation = underTest.getMultipleOrganisationByIds(0, 15, new Organisation(), sort, ids, authContex);
		Assert.assertNotNull(pageOrganisation);
	}
		
	private Page<Organisation> getOrganisationList(){
		List<Organisation> listOrganisation = new ArrayList<Organisation>();
		listOrganisation.add(getOrganisation());
		listOrganisation.add(getOrganisation());
		Page<Organisation> pageOrganisation = new PageImpl(listOrganisation);
		return pageOrganisation;
	}
	
	private Organisation getOrganisation() {
		Organisation organisation = new Organisation();
		organisation.setName("OrioSchool");
		organisation.setId("IBJK");
		return organisation;
	}
}
