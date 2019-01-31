package com.pearson.sam.bridgeapi.serviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.model.LicenceHistory;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.LicenceHistoryRespository;
import com.pearson.sam.bridgeapi.serviceimpl.LicenceHistoryServiceImpl;

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
public class LicenceHistoryServiceImplTest {
	
	@Mock
	public LicenceHistoryRespository licenceHistoryRespository;
	
	@InjectMocks
	private LicenceHistoryServiceImpl underTest = new LicenceHistoryServiceImpl();
	
	@Test
	public void testCreate() {
	  when(licenceHistoryRespository.save(any(LicenceHistory.class))).thenReturn(getLicenceHistory());
	  LicenceHistory licenceHistory = underTest.create(getLicenceHistory());
	  Assert.assertNotNull(licenceHistory);		 
	}
	
	@Test
	  public void testPageIt( ) {
		Sort sort = new Sort();
		sort.setField("licenceHolderHistoryId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10, org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<LicenceHistory> e = Example.of(new LicenceHistory(), ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(licenceHistoryRespository.findAll(any(Example.class),any(Pageable.class))).thenReturn(getLicenceHistoryList());
		Page<LicenceHistory> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	  }
	 
	private final LicenceHistory getLicenceHistory() {
	  LicenceHistory licenceHistory = new LicenceHistory();
	  licenceHistory.setLicenceHistoryId("licence120");;
	  licenceHistory.setLicence("QQP-3VD-QVC3");
	  return licenceHistory;
	}
	
	private Page<LicenceHistory> getLicenceHistoryList(){
		LicenceHistory licenceHistory = new LicenceHistory();
		licenceHistory.setLicenceHistoryId("PP2LICENCEHISTORY00");
		List<LicenceHistory> listLicenceHistory = new ArrayList<LicenceHistory>();
		listLicenceHistory.add(licenceHistory);
		Page<LicenceHistory> result = new PageImpl<LicenceHistory>(listLicenceHistory);
		return result;
	}

}
