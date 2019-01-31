package com.pearson.sam.bridgeapi.serviceimpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.model.AccessCodesHistory;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.AccessCodeHistoryRepository;
import com.pearson.sam.bridgeapi.serviceimpl.AccessCodeHistoryRepoService;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.List;

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
public class AccessCodeHistoryRepoServiceTest {

	@Mock
	private AccessCodeHistoryRepository accessCodeHistoryRepository;
	
	@InjectMocks
	private AccessCodeHistoryRepoService underTest;
	
	@Test
	public void testcreate() {
		AccessCodesHistory accesscodehistory = getAccessCodesHistory();
		when(accessCodeHistoryRepository.findTopByOrderByIdDesc()).thenReturn(accesscodehistory);
		when(accessCodeHistoryRepository.save(accesscodehistory)).thenReturn(accesscodehistory);
		AccessCodesHistory accesscodehistoryresult = underTest.create(accesscodehistory);
		assertNotNull(accesscodehistoryresult);
		
		when(accessCodeHistoryRepository.findTopByOrderByIdDesc()).thenReturn(null);
		when(accessCodeHistoryRepository.save(accesscodehistory)).thenReturn(accesscodehistory);
		accesscodehistoryresult = underTest.create(accesscodehistory);
		assertNotNull(accesscodehistoryresult);
		
		accesscodehistory.setAccessCodeHistoryId("");
		when(accessCodeHistoryRepository.findTopByOrderByIdDesc()).thenReturn(accesscodehistory);
		when(accessCodeHistoryRepository.save(accesscodehistory)).thenReturn(accesscodehistory);
		accesscodehistoryresult = underTest.create(accesscodehistory);
		assertNotNull(accesscodehistoryresult);
		
		accesscodehistory.setAccessCodeHistoryId(null);
		when(accessCodeHistoryRepository.findTopByOrderByIdDesc()).thenReturn(accesscodehistory);
		when(accessCodeHistoryRepository.save(accesscodehistory)).thenReturn(accesscodehistory);
		accesscodehistoryresult = underTest.create(accesscodehistory);
		assertNotNull(accesscodehistoryresult);
		
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
		when(accessCodeHistoryRepository.findAll(any(),any(Pageable.class))).thenReturn(getPageofAccessCodesHistory());
		Page<AccessCodesHistory> pageofaccesscodeshistory = underTest.pageIt(pageable, e);
		assertNotNull(pageofaccesscodeshistory);
		
	}
	
	private Page<AccessCodesHistory> getPageofAccessCodesHistory() {
		List<AccessCodesHistory> listaccessCodeshistory = asList(getAccessCodesHistory());
		Page<AccessCodesHistory> pageaccessCodesHistory = new PageImpl<AccessCodesHistory>(listaccessCodeshistory);
		return pageaccessCodesHistory;
	}

	private AccessCodesHistory getAccessCodesHistory() {
		AccessCodesHistory accesscodeHistory = new AccessCodesHistory();
		accesscodeHistory.setAccessCodeHistoryId("PP2ACCESSCODEHISTORY07");
		return accesscodeHistory;
	}
}
