package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.iservice.ILicenceHolderService;
import com.pearson.sam.bridgeapi.model.ChannelPartner;
import com.pearson.sam.bridgeapi.model.Holder;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.serviceimpl.IndexService;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.ArrayList;
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

/**
 * Licence Holder Bridge Service Unit test cases
 * 
 * @author VVaijPa
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LicenceHolderBridgeServiceImplTest {

	@InjectMocks
	private LicenceHolderBridgeServiceImpl underTest = new LicenceHolderBridgeServiceImpl();

	@Mock
	private ILicenceHolderService licenceHolderService;

	@Mock
	private IndexService indexService;

	@Mock
	private ISessionFacade sessionFacade;

	@Test
	public void testCreate() {
		when(licenceHolderService.create(any(LicenceHolder.class))).thenReturn(new LicenceHolder());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		LicenceHolder result = underTest.create(getLicenceHolder());
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdate() {
		when(licenceHolderService.update(any(LicenceHolder.class),"SCHOOL")).thenReturn(getLicenceHolder());
		LicenceHolder result = underTest.update(new LicenceHolder(),"SCHOOL");
		Assert.assertNotNull(result);
	}

	@Test
	public void testFetch() {
		when(licenceHolderService.fetch(any(LicenceHolder.class))).thenReturn(new LicenceHolder());
		LicenceHolder result = underTest.fetch(getLicenceHolder());
		Assert.assertNotNull(result);
	}

	@Test
	public void testFetchMultiple() {
		List<String> ids = new ArrayList<>();
		ids.add("LICENCEHOLDER01");
		ids.add("LICENCEHOLDER02");

		when(licenceHolderService.fetchMultiple(KeyType.DEFAULT, ids)).thenReturn(getLicenceHolderList());
		List<LicenceHolder> result = underTest.fetchMultiple(KeyType.DEFAULT, ids);
		Assert.assertNotNull(result);
	}

	@Test
	public void testDelete() {
		when(licenceHolderService.delete(any(LicenceHolder.class))).thenReturn(new LicenceHolder());
		LicenceHolder result = underTest.delete(getLicenceHolder());
		Assert.assertNotNull(result);
	}

	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("licenceHolderId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<LicenceHolder> e = Example.of(new LicenceHolder(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(licenceHolderService.pageIt(pageable, e)).thenReturn(getLicenceHolderPage());
		Page<LicenceHolder> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}

	private LicenceHolder getLicenceHolder() {
		LicenceHolder licenceHolder = new LicenceHolder();
		Holder holder = new Holder();
		holder.setOrganisation(new ChannelPartner());
		licenceHolder.setHolder(holder);
		holder.setSchool(new School());
		return licenceHolder;
	}

	private List<LicenceHolder> getLicenceHolderList() {
		List<LicenceHolder> licenceHolderList = Arrays.asList(getLicenceHolder(), getLicenceHolder());
		return licenceHolderList;
	}

	private Page<LicenceHolder> getLicenceHolderPage() {
		LicenceHolder licenceHolder = new LicenceHolder();
		licenceHolder.setLicenceHolderId("PP2LICENCE00");
		List<LicenceHolder> listLicenceHolder = new ArrayList<LicenceHolder>();
		listLicenceHolder.add(licenceHolder);
		Page<LicenceHolder> result = new PageImpl<LicenceHolder>(listLicenceHolder);
		return result;
	}
}
