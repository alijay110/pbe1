package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.LicenceHolderHistoryBridgeServiceImpl;
import com.pearson.sam.bridgeapi.iservice.ILicenceHolderHistoryService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.LicenceHolderHistory;
import com.pearson.sam.bridgeapi.model.Sort;

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
 * Licence Holder History Bridge Service Unit Test Cases
 * 
 * @author VVaijPa
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LicenceHolderHistoryBridgeServiceImplTest {

	@InjectMocks
	private LicenceHolderHistoryBridgeServiceImpl underTest = new LicenceHolderHistoryBridgeServiceImpl();

	@Mock
	private ILicenceHolderHistoryService licenceHolderHistoryService;

	@Test
	public void testCreate() {
		when(licenceHolderHistoryService.create(any(LicenceHolderHistory.class)))
				.thenReturn(new LicenceHolderHistory());
		LicenceHolderHistory result = underTest.create(getLicenceHolderHistory());
		Assert.assertNotNull(result);
	}

	@Test
	public void testUpdate() {
		when(licenceHolderHistoryService.update(any(LicenceHolderHistory.class)))
				.thenReturn(new LicenceHolderHistory());
		LicenceHolderHistory result = underTest.update(new LicenceHolderHistory());
		Assert.assertNotNull(result);
	}

	@Test
	public void testFetch() {
		when(licenceHolderHistoryService.fetch(any(LicenceHolderHistory.class))).thenReturn(new LicenceHolderHistory());
		LicenceHolderHistory result = underTest.fetch(getLicenceHolderHistory());
		Assert.assertNotNull(result);
	}

	@Test
	public void testFetchMultiple() {
		List<String> ids = new ArrayList<>();
		ids.add("LICENCEHOLDER01");
		ids.add("LICENCEHOLDER02");

		when(licenceHolderHistoryService.fetchMultiple(KeyType.DEFAULT, ids)).thenReturn(getLicenceHolderHistoryList());
		List<LicenceHolderHistory> result = underTest.fetchMultiple(KeyType.DEFAULT, ids);
		Assert.assertNotNull(result);
	}

	@Test
	public void testDelete() {
		when(licenceHolderHistoryService.delete(any(LicenceHolderHistory.class)))
				.thenReturn(new LicenceHolderHistory());
		LicenceHolderHistory result = underTest.delete(getLicenceHolderHistory());
		Assert.assertNotNull(result);
	}

	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("licenceHolderHistoryId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<LicenceHolderHistory> e = Example.of(new LicenceHolderHistory(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(licenceHolderHistoryService.pageIt(pageable, e)).thenReturn(getLicenceHolderHistoryPage());
		Page<LicenceHolderHistory> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}

	private LicenceHolderHistory getLicenceHolderHistory() {
		LicenceHolderHistory licenceHolderHistory = new LicenceHolderHistory();
		licenceHolderHistory.setLicenceHolderHistoryId("licenceHolderHistoryId");

		return licenceHolderHistory;
	}

	private List<LicenceHolderHistory> getLicenceHolderHistoryList() {
		List<LicenceHolderHistory> licenceHolderHistoryList = Arrays.asList(getLicenceHolderHistory(),
				getLicenceHolderHistory());
		return licenceHolderHistoryList;
	}

	private Page<LicenceHolderHistory> getLicenceHolderHistoryPage() {
		LicenceHolderHistory licenceHolderHistory = new LicenceHolderHistory();
		licenceHolderHistory.setLicenceHolderHistoryId("licenceHolderHistoryId");
		List<LicenceHolderHistory> listLicenceHolderHistory = new ArrayList<LicenceHolderHistory>();
		listLicenceHolderHistory.add(licenceHolderHistory);
		Page<LicenceHolderHistory> result = new PageImpl<LicenceHolderHistory>(listLicenceHolderHistory);
		return result;
	}
}
