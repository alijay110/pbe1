package com.pearson.sam.bridgeapi.serviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.model.LicenceHolderHistory;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.LicenceHolderHistoryRespository;
import com.pearson.sam.bridgeapi.serviceimpl.LicenceHolderHistoryServiceImpl;

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
public class LicenceHolderHistoryServiceImplTest {

	@Mock
	private LicenceHolderHistoryRespository licenceHolderHistoryRespository;
	
	@InjectMocks
	private LicenceHolderHistoryServiceImpl underTest = new LicenceHolderHistoryServiceImpl();
		
	 @Test
	 public void testCreate() {
		  when(licenceHolderHistoryRespository.findTopByOrderByIdDesc()).thenReturn(getLicenceHolderHistory());
		  when(licenceHolderHistoryRespository.save(any(LicenceHolderHistory.class))).thenReturn(getLicenceHolderHistory());	  
		  LicenceHolderHistory holderHistory = underTest.create(getLicenceHolderHistory());
	    Assert.assertNotNull(holderHistory);
	}
	 
	  @Test
	  public void testUpdate() {
	    when(licenceHolderHistoryRespository.findByLicenceHolderHistoryId(any(String.class))).thenReturn(getLicenceHolderHistory());
	    when(licenceHolderHistoryRespository.save(any(LicenceHolderHistory.class))).thenReturn(getLicenceHolderHistory());
	    
	    LicenceHolderHistory holderHistory = underTest.update(getLicenceHolderHistory());
	    Assert.assertNotNull(holderHistory);
	  }

	  @Test
	  public void testFetch() {
	    when(licenceHolderHistoryRespository.findByLicenceHolderHistoryId(any(String.class))).thenReturn(getLicenceHolderHistory());
	    LicenceHolderHistory holderHistory = underTest.fetch(getLicenceHolderHistory());
	    Assert.assertNotNull(holderHistory);
	  }
	  
	  @Test
	  public void testPageIt( ) {
		Sort sort = new Sort();
		sort.setField("licenceHolderHistoryId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10, org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<LicenceHolderHistory> e = Example.of(new LicenceHolderHistory(), ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(licenceHolderHistoryRespository.findAll(any(Example.class),any(Pageable.class))).thenReturn(getLicenceHolderHistoryList());
		Page<LicenceHolderHistory> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	  }
	 
	 private LicenceHolderHistory getLicenceHolderHistory() {
		 LicenceHolderHistory licenceHolderHistory = new LicenceHolderHistory();
		 licenceHolderHistory.setLicenceHolderHistoryId("PP2LICENCEHOLDERHISTORY00");
		    return licenceHolderHistory;
	}

	private Page<LicenceHolderHistory> getLicenceHolderHistoryList(){
		LicenceHolderHistory licenceHolderHistory = new LicenceHolderHistory();
		licenceHolderHistory.setLicenceHolderHistoryId("PP2LICENCEHOLDERHISTORY00");
		List<LicenceHolderHistory> listLicenceHolderHistory = new ArrayList<LicenceHolderHistory>();
		listLicenceHolderHistory.add(licenceHolderHistory);
		Page<LicenceHolderHistory> result = new PageImpl<LicenceHolderHistory>(listLicenceHolderHistory);
		return result;
	}
}
