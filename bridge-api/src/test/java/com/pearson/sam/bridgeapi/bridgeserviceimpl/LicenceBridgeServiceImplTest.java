package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.LicenceBridgeServiceImpl;
import com.pearson.sam.bridgeapi.iservice.ILicenceService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.LicenceRespository;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

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
 * Licence Bridge Layer Service unit test cases
 * 
 * @author VVaijPa
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LicenceBridgeServiceImplTest {
	@Mock
	private ILicenceService licenceService;

	@Mock
	public LicenceRespository licenceRespository;

	@Mock
	private ISessionFacade sessionFacade;

	@InjectMocks
	private LicenceBridgeServiceImpl underTest = new LicenceBridgeServiceImpl();

	@Test
	public void testCreate() {
		when(licenceService.create(any(Licence.class))).thenReturn(new Licence());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		Licence result = underTest.create(getLicence());
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdate() {
		when(licenceService.update(any(Licence.class))).thenReturn(new Licence());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		Licence licence = underTest.update(getLicence());
		Assert.assertNotNull(licence);
	}

	@Test
	public void testFetch() {
		when(licenceService.fetch(any(Licence.class))).thenReturn(new Licence());
		Licence licence = underTest.fetch(getLicence());
		Assert.assertNotNull(licence);
	}

	@Test
	public void testDelete() {
		when(licenceService.delete(any(Licence.class))).thenReturn(new Licence());
		Licence licence = underTest.delete(getLicence());
		Assert.assertNotNull(licence);
	}

	@Test
	public void testFetchMultiple() {
		List<String> ids = new ArrayList<>();
		ids.add("LICENCE01");
		ids.add("LICENCE02");

		when(licenceService.fetchMultiple(KeyType.DEFAULT, ids)).thenReturn(getLicenceList());
		List<Licence> licence = underTest.fetchMultiple(KeyType.DEFAULT, ids);
		Assert.assertNotNull(licence);
	}

	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("licenceId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Licence> e = Example.of(new Licence(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(licenceService.pageIt(pageable, e)).thenReturn(getLicencePage());
		Page<Licence> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}

	private Licence getLicence() {
		Licence licence = new Licence();
		licence.setLicenceId("PP2LIC001");
		return licence;
	}

	private Page<Licence> getLicencePage() {
		Licence licence = new Licence();
		licence.setLicenceId("PP2LICENCE00");
		List<Licence> listLicence = new ArrayList<Licence>();
		listLicence.add(licence);
		Page<Licence> result = new PageImpl<Licence>(listLicence);
		return result;
	}

	private List<Licence> getLicenceList() {
		List<Licence> licenceList = Arrays.asList(getLicence(), getLicence());
		return licenceList;
	}
}
