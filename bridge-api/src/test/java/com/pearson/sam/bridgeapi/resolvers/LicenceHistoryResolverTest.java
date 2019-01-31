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
import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceHistoryBridgeService;
import com.pearson.sam.bridgeapi.model.LicenceHistory;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;

@RunWith(MockitoJUnitRunner.class)
public class LicenceHistoryResolverTest {
	@Mock
	private ILicenceHistoryBridgeService licenceHistoryBridgeService;

	@Mock
	private AuthContext authContex;

	@InjectMocks
	private LicenceHistoryResolver underTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddLicenceHistory() {
		when(licenceHistoryBridgeService.create(any(LicenceHistory.class))).thenReturn(new LicenceHistory());
		LicenceHistory result = underTest.addLicenceHistory(new LicenceHistory());
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedLicenceHistoryTableData() {
		Sort sort = new Sort();
		sort.setField("uid");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when((licenceHistoryBridgeService.pageIt(any(Pageable.class), any(Example.class))))
				.thenReturn(getLicenceHistory());
		Page<LicenceHistory> licenceHistory = underTest.getPaginatedLicenceHistoryTableData(0, 15, new LicenceHistory(),
				StringMatcher.DEFAULT, sort, authContex);
		Assert.assertNotNull(licenceHistory);
	}

	public Page<LicenceHistory> getLicenceHistory() {
		List<LicenceHistory> licenceHistory = new ArrayList<LicenceHistory>();
		licenceHistory.add(new LicenceHistory());
		Page<LicenceHistory> pageAccessCodesHistory = new PageImpl<LicenceHistory>(licenceHistory);
		return pageAccessCodesHistory;
	}

}
