package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.LicenceHistoryBridgeServiceImpl;
import com.pearson.sam.bridgeapi.iservice.ILicenceHistoryService;
import com.pearson.sam.bridgeapi.model.LicenceHistory;
import com.pearson.sam.bridgeapi.model.Sort;

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

/**
 * LicenceHistory Bridge Service Unit Test Cases
 * 
 * @author VVaijPa
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LicenceHistoryBridgeServiceImplTest {

	@InjectMocks
	private LicenceHistoryBridgeServiceImpl underTest = new LicenceHistoryBridgeServiceImpl();

	@Mock
	private ILicenceHistoryService licenceHistoryService;

	@Test
	public void testCreate() {
		when(licenceHistoryService.create(any(LicenceHistory.class))).thenReturn(new LicenceHistory());
		LicenceHistory result = underTest.create(getLicenceHistory());
		Assert.assertNotNull(result);
	}

	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("licenceHistoryId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<LicenceHistory> e = Example.of(new LicenceHistory(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(licenceHistoryService.pageIt(pageable, e)).thenReturn(getLicenceHistoryPage());
		Page<LicenceHistory> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}

	private LicenceHistory getLicenceHistory() {
		LicenceHistory licenceHistory = new LicenceHistory();
		licenceHistory.setLicenceHistoryId("PP2LIC001");
		return licenceHistory;
	}

	private Page<LicenceHistory> getLicenceHistoryPage() {
		LicenceHistory licenceHistory = new LicenceHistory();
		licenceHistory.setLicenceHistoryId("PP2LIC001");
		List<LicenceHistory> listLicenceHistory = new ArrayList<LicenceHistory>();
		listLicenceHistory.add(licenceHistory);
		Page<LicenceHistory> result = new PageImpl<LicenceHistory>(listLicenceHistory);
		return result;
	}

}
