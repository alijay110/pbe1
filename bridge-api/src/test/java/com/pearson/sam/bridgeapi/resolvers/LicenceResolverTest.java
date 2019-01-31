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
import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceBridgeService;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;

@RunWith(MockitoJUnitRunner.class)
public class LicenceResolverTest {

	@Mock
	private ILicenceBridgeService licenceBridgeService;

	@Mock
	private AuthContext authContex;

	@InjectMocks
	private LicenceResolver underTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddLicence() {
		when(licenceBridgeService.create(any(Licence.class))).thenReturn(getLicence());
		Licence result = underTest.addLicence(getLicence());
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetLicenceById() {
		when(licenceBridgeService.fetch(any(Licence.class))).thenReturn(getLicence());
		Licence result = underTest.getLicenceById("licenceId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateAccessCode() {
		when(licenceBridgeService.update(any(Licence.class))).thenReturn(getLicence());
		Licence result = underTest.updateAccessCode("licenceId", new Licence());
		Assert.assertNotNull(result);
	}

	@Test
	public void testDeleteLicenceById() {
		when(licenceBridgeService.delete(any(Licence.class))).thenReturn(getLicence());
		Licence result = underTest.deleteLicenceById(new Licence());
		Assert.assertNotNull(result);
	}

	@Test
	public void testAllocateLicenceToUser() {
		when(licenceBridgeService.update(any(Licence.class))).thenReturn(getLicence());
		Licence result = underTest.allocateLicenceToUser(new Licence());
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedLicenceTableData() {
		Sort sort = new Sort();
		sort.setField("licenceId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when((licenceBridgeService.pageIt(any(Pageable.class), any(Example.class)))).thenReturn(getLicencePage());
		Page<Licence> licenceHistory = underTest.getPaginatedLicenceTableData(0, 15, new Licence(),
				StringMatcher.DEFAULT, sort, authContex);
		Assert.assertNotNull(licenceHistory);
	}

	private Page<Licence> getLicencePage() {
		List<Licence> licence = new ArrayList<Licence>();
		licence.add(new Licence());
		Page<Licence> pageLicence = new PageImpl<Licence>(licence);
		return pageLicence;
	}

	private Licence getLicence() {
		Licence licence = new Licence();
		licence.setLicenceId("PP2LIC001");
		return licence;
	}
}
