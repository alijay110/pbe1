package com.pearson.sam.bridgeapi.serviceimpl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.model.LicenceHistory;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.LicenceHistoryRespository;
import com.pearson.sam.bridgeapi.repository.LicenceRespository;
import com.pearson.sam.bridgeapi.samclient.LicenceSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.serviceimpl.LicenceServiceImpl;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.ArrayList;
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
public class LicenceServiceImplTest {

	@Mock
	private LicenceRespository licenceRespository;

	@Mock
	private LicenceSamClient licenceSamClient;

	@Mock
	public LicenceHistoryRespository licenceHistoryRespository;

	@Mock
	private ISessionFacade sessionFacade;

	@InjectMocks
	private LicenceServiceImpl underTest = new LicenceServiceImpl();

	@Test
	public void testCreate() {
		when(licenceSamClient.create(any())).thenReturn(getLicence());
		when(licenceRespository.save(any(Licence.class))).thenReturn(getLicence());
		when(licenceHistoryRespository.findByLicence(any(String.class))).thenReturn(new LicenceHistory());
		when(licenceHistoryRespository.save(any(LicenceHistory.class))).thenReturn(new LicenceHistory());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		Licence licence = underTest.create(getLicence());
		Assert.assertNotNull(licence);
	}

	@Test(expected = BridgeApiGraphqlException.class)
	public void testcreateGraphException() {
		when(licenceSamClient.create(any(JsonObject.class))).thenReturn(getLicence());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		when(licenceRespository.save(any(Licence.class)))
				.thenThrow(new BridgeApiGeneralException("Exception in Creating access codes"));
		underTest.create(getLicence());
	}

	@Test
	public void testUpdate() {
		when(licenceRespository.findByLicenceId(any(String.class))).thenReturn(getLicence());
		when(licenceSamClient.update(anyMap(), any())).thenReturn(getLicence());
		Licence licence = underTest.update(getLicence());
		Assert.assertNotNull(licence);
	}

	@Test(expected = BridgeApiGeneralException.class)
	public void testUpdateDBAccessNull() {
		Licence licence = getLicence();
		when(licenceRespository.findByLicenceId(anyString())).thenReturn(null);
		underTest.update(licence);
	}

	@Test
	public void testUpdateSaveGraphException() {
		Licence licenceSingle = getLicence();
		when(licenceRespository.findByLicenceId(anyString())).thenReturn(licenceSingle);
		when(licenceSamClient.update(any(), any(JsonObject.class)))
				.thenThrow(new BridgeApiGeneralException("Exception in updating access codes"));
		when(sessionFacade.getLoggedInUser(true)).thenReturn(new SessionUser());
		Licence licence = underTest.update(licenceSingle);
		assertNotNull(licence);
	}

	@Test
	public void testFetch() {
		when(licenceSamClient.fetchOne(any())).thenReturn(getLicence());
		when(licenceRespository.findByLicenceId(any(String.class))).thenReturn(getLicence());
		Licence licence = underTest.fetch(getLicence());
		Assert.assertNotNull(licence);
	}

	@Test(expected = BridgeApiGraphqlException.class)
	public void testfetchOneException() {
		when(licenceRespository.findByLicenceId(anyString())).thenReturn(getLicence());
		when(licenceSamClient.fetchOne(any(JsonObject.class)))
				.thenThrow(new BridgeApiGeneralException("Exception in fetching record"));
		;
		Licence licence = underTest.fetch(getLicence());
		assertNotNull(licence);
	}

	@Test
	public void testDelete() {
		when(licenceRespository.findByLicenceId(any(String.class))).thenReturn(getLicence());
		doNothing().when(licenceRespository).delete(any(Licence.class));
		Licence licence = underTest.delete(getLicence());
		Assert.assertNotNull(licence);
	}

	@Test(expected = BridgeApiGraphqlException.class)
	public void testDeleteDBAccessNull() {
		Licence licence = getLicence();
		when(licenceRespository.findByLicenceId(anyString())).thenReturn(null);
		underTest.delete(licence);
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
		when(licenceRespository.findAll(any(Example.class), any(Pageable.class))).thenReturn(getLicenceList());
		Page<Licence> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}

	private Licence getLicence() {
		Licence licence = new Licence();
		licence.setLastActivatedBy("mlsreekanth");
		licence.setLicenceId("QQP-3VD-QVC3");
		return licence;
	}

	private Page<Licence> getLicenceList() {
		Licence licence = new Licence();
		licence.setLicenceId("PP2LICENCE00");
		List<Licence> listLicence = new ArrayList<Licence>();
		listLicence.add(licence);
		Page<Licence> result = new PageImpl<Licence>(listLicence);
		return result;
	}

}
