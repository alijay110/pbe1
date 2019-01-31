package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.AccessCodeHistoryBridgeServiceImpl;
import com.pearson.sam.bridgeapi.iservice.IAccessCodeHistoryRepoService;
import com.pearson.sam.bridgeapi.model.AccessCodesHistory;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
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
public class AccessCodeHistoryBridgeServiceImplTest {

	@Mock
	IAccessCodeHistoryRepoService accessCodeHistoryRepoService;
	
	@InjectMocks
	private AccessCodeHistoryBridgeServiceImpl underTest;
	
	@Test
	public void create() {
		AccessCodesHistory accesscodeshistorycreate = getAccessCodeHistory();
		when(accessCodeHistoryRepoService.create(accesscodeshistorycreate)).thenReturn(accesscodeshistorycreate);
		accesscodeshistorycreate = underTest.create(accesscodeshistorycreate);
		assertNotNull(accesscodeshistorycreate);
	}
	
	
	@Test
	public void testpageIt() {		
		Sort sort = new Sort();
		sort.setField("accessCodeHistoryId");
		io.leangen.graphql.execution.SortField.Direction direction = Direction.ASC;
		sort.setOrder(direction);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<AccessCodesHistory> e = Example.of(new AccessCodesHistory(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(accessCodeHistoryRepoService.pageIt(any(Pageable.class), Matchers.<Example<AccessCodesHistory>>any())).thenReturn(getPageofAccessCodesHistory());
		Page<AccessCodesHistory> pageofaccesscodeshistory = underTest.pageIt(pageable, e);
		assertNotNull(pageofaccesscodeshistory);
		
	}
	
	private Page<AccessCodesHistory> getPageofAccessCodesHistory() {
		List<AccessCodesHistory> listaccessCodeshistory = asList(getAccessCodeHistory());
		Page<AccessCodesHistory> pageaccessCodesHistory = new PageImpl<AccessCodesHistory>(listaccessCodeshistory);
		return pageaccessCodesHistory;
	}
	
	
	private AccessCodesHistory getAccessCodeHistory() {
		AccessCodesHistory accesscodehistory = new AccessCodesHistory();
		accesscodehistory.setAccessCode("W4Z7-T4T4-Y6Z5");
		return accesscodehistory;
	}
	
}
