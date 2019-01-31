package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import com.pearson.sam.bridgeapi.ibridgeservice.IAccessCodeHistoryBridgeService;
import com.pearson.sam.bridgeapi.model.AccessCodesHistory;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.execution.SortField.Direction;

@RunWith(MockitoJUnitRunner.class)
public class AccessCodeHistoryResolverTest {
	
	@Mock
	IAccessCodeHistoryBridgeService accessCodeHistoryBridgeService;

	@Mock
	private AuthContext authContex;
	
	@InjectMocks
	private AccessCodeHistoryResolver underTest;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddAccessCodeHistory() {
		when(accessCodeHistoryBridgeService.create(any(AccessCodesHistory.class))).thenReturn(new AccessCodesHistory());
		AccessCodesHistory result = underTest.addAccessCodeHistory(new AccessCodesHistory());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testGetPaginatedAccessCodeTableData() {		
		Sort sort = new Sort();
		sort.setField("accessCode");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when(( accessCodeHistoryBridgeService.pageIt(any(Pageable.class),any(Example.class)))).thenReturn(getAccessCodesHistory());
		Page<AccessCodesHistory> pageAccessCodesHistory = underTest.getPaginatedAccessCodeTableData(0,15,new AccessCodesHistory(),StringMatcher.DEFAULT,sort,authContex);
		Assert.assertNotNull(pageAccessCodesHistory);
	}
	
	public Page<AccessCodesHistory> getAccessCodesHistory(){
		AccessCodesHistory accessCodesHistory = new AccessCodesHistory();
		accessCodesHistory.setAccessCode("22233444KKLL");
		List<AccessCodesHistory> listAccessCodesHistory = Arrays.asList(accessCodesHistory);
		Page<AccessCodesHistory> pageAccessCodesHistory = new PageImpl<AccessCodesHistory>(listAccessCodesHistory);
		return pageAccessCodesHistory;
	}
}
