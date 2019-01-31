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
import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceHolderHistoryBridgeService;
import com.pearson.sam.bridgeapi.model.LicenceHolderHistory;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;

@RunWith(MockitoJUnitRunner.class)
public class LicenceHolderHistoryResolverTest {
	@Mock
	private ILicenceHolderHistoryBridgeService licenceHolderHistoryBridgeService;

	@Mock
	private AuthContext authContex;

	@InjectMocks
	private LicenceHolderHistoryResolver underTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddLicenceHolderHistory() {
		when(licenceHolderHistoryBridgeService.create(any(LicenceHolderHistory.class)))
				.thenReturn(new LicenceHolderHistory());
		LicenceHolderHistory result = underTest.addLicenceHolderHistory(new LicenceHolderHistory());
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdateLicenceHolderHistory() {
		when(licenceHolderHistoryBridgeService.update(any(LicenceHolderHistory.class)))
				.thenReturn(new LicenceHolderHistory());
		LicenceHolderHistory result = underTest.updateLicenceHolderHistory("licenceHolderHistoryId",
				new LicenceHolderHistory());
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetLicenceHolderHistoryById() {
		when(licenceHolderHistoryBridgeService.fetch(any(LicenceHolderHistory.class)))
				.thenReturn(new LicenceHolderHistory());
		LicenceHolderHistory result = underTest.getLicenceHolderHistoryById("licenceHolderHistoryId");
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedLicenceHolderHistoryTableData() {

		Sort sort = new Sort();
		sort.setField("licenceHolderHistoryId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when((licenceHolderHistoryBridgeService.pageIt(any(Pageable.class), any(Example.class))))
				.thenReturn(getLicenceHolderHistory());
		Page<LicenceHolderHistory> licenceHolderHistory = underTest.getPaginatedLicenceHolderHistoryTableData(0, 15,
				new LicenceHolderHistory(), StringMatcher.DEFAULT, sort, authContex);
		Assert.assertNotNull(licenceHolderHistory);

	}

	public Page<LicenceHolderHistory> getLicenceHolderHistory() {
		List<LicenceHolderHistory> licenceHolderHistory = new ArrayList<LicenceHolderHistory>();
		licenceHolderHistory.add(new LicenceHolderHistory());
		Page<LicenceHolderHistory> pageAccessCodesHistory = new PageImpl<LicenceHolderHistory>(licenceHolderHistory);
		return pageAccessCodesHistory;
	}

}
