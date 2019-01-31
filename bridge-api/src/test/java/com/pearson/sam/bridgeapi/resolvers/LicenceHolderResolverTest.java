package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceHolderBridgeService;
import com.pearson.sam.bridgeapi.model.ChannelPartner;
import com.pearson.sam.bridgeapi.model.Holder;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;

@RunWith(MockitoJUnitRunner.class)
public class LicenceHolderResolverTest {
	@Mock
	private ILicenceHolderBridgeService licenceHolderBridgeService;

	@Mock
	private AuthContext authContex;

	@InjectMocks
	private LicenceHolderResolver underTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddLicenceHolder() {
		when(licenceHolderBridgeService.create(any(LicenceHolder.class))).thenReturn(getLicenceHolder());
		LicenceHolder result = underTest.addLicenceHolder(new LicenceHolder());
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetLicenceHolderById() {
		when(licenceHolderBridgeService.fetch(any(LicenceHolder.class))).thenReturn(getLicenceHolder());
		LicenceHolder result = underTest.getLicenceHolderById("licenceHolderId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateLicenceHolder() {
		when(licenceHolderBridgeService.update(any(LicenceHolder.class),"SCHOOL")).thenReturn(getLicenceHolder());
		LicenceHolder result = underTest.updateLicenceHolder("licenceHolderId", new LicenceHolder());
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateLicenceHolderByOrganisationId() {
		when(licenceHolderBridgeService.update(any(LicenceHolder.class),"SCHOOL")).thenReturn(getLicenceHolder());
		LicenceHolder result = underTest.updateLicenceHolderByOrganisationId("organisationId111", getLicenceHolder());
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateLicenceHolderBySchoolId() {
		when(licenceHolderBridgeService.update(any(LicenceHolder.class),"SCHOOL")).thenReturn(getLicenceHolder());
		LicenceHolder result = underTest.updateLicenceHolderBySchoolId("schoolId", getLicenceHolder());
		Assert.assertNotNull(result);
	}

	@Test
	public void testAllocateLicenceToLicenceHolderForSchool() {
		when(licenceHolderBridgeService.update(any(LicenceHolder.class),"SCHOOL")).thenReturn(getLicenceHolder());
		LicenceHolder result = underTest.allocateLicenceToLicenceHolderForSchool(new LicenceHolder());
		Assert.assertNotNull(result);
	}

	@Test
	public void testAllocateLicenceToLicenceHolderForOrganisation() {
		when(licenceHolderBridgeService.update(any(LicenceHolder.class),"SCHOOL")).thenReturn(getLicenceHolder());
		LicenceHolder result = underTest.allocateLicenceToLicenceHolderForOrganisation(new LicenceHolder());
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedLicenceHistoryTableData() {
		Sort sort = new Sort();
		sort.setField("licenceHolderId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when((licenceHolderBridgeService.pageIt(any(Pageable.class), any(Example.class))))
				.thenReturn(getLicenceHolderList());
		Page<LicenceHolder> licenceHistory = underTest.getPaginatedHolderLicenceTableData(0, 15, new LicenceHolder(),
				StringMatcher.DEFAULT, sort, authContex);
		Assert.assertNotNull(licenceHistory);
	}

	private Page<LicenceHolder> getLicenceHolderList() {
		List<LicenceHolder> licenceHolder = new ArrayList<LicenceHolder>();
		licenceHolder.add(new LicenceHolder());
		Page<LicenceHolder> pageLicenceHolder = new PageImpl<LicenceHolder>(licenceHolder);
		return pageLicenceHolder;
	}

	private LicenceHolder getLicenceHolder() {
		LicenceHolder licenceHolder = new LicenceHolder();
		Holder holder = new Holder();
		holder.setOrganisation(new ChannelPartner());
		licenceHolder.setHolder(holder);
		holder.setSchool(new School());
		return licenceHolder;
	}

}
