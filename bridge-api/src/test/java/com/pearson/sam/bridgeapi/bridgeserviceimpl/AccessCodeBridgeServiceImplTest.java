package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.AccessCodeBridgeServiceImpl;
import com.pearson.sam.bridgeapi.iservice.IAccessCodeRepoService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;
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
public class AccessCodeBridgeServiceImplTest {

	@Mock
	IAccessCodeRepoService accessCodeRepoService;
	
	@InjectMocks
	private AccessCodeBridgeServiceImpl underTest;
	
	@Test
	public void testcreate() {
		AccessCodes accesscodetoCreate = getAccessCodes();
		when(accessCodeRepoService.create(any(AccessCodes.class))).thenReturn(accesscodetoCreate);
		accesscodetoCreate = underTest.create(accesscodetoCreate);
		assertNotNull(accesscodetoCreate);
	}

	
	@Test
	public void testupdate() {
		AccessCodes accesscodetoupdate = getAccessCodes();
		when(accessCodeRepoService.update(any(AccessCodes.class))).thenReturn(accesscodetoupdate);
		accesscodetoupdate = underTest.update(accesscodetoupdate);
		assertNotNull(accesscodetoupdate);
	}

	
	@Test
	public void testfetchOne() {
		AccessCodes accesscodetofetch = getAccessCodes();
		when(accessCodeRepoService.fetchOne(any(AccessCodes.class))).thenReturn(accesscodetofetch);
		accesscodetofetch = underTest.fetchOne(accesscodetofetch);
		assertNotNull(accesscodetofetch);
	}

	@Test
	public void fetchMultiple() {
		List<AccessCodes> listofAccesscodes = asList(getAccessCodes());
		when(accessCodeRepoService.fetchMultiple(any(KeyType.class),Matchers.<List<String>>any())).thenReturn(listofAccesscodes);
		listofAccesscodes = underTest.fetchMultiple(KeyType.ACCESS_CODES, asList("1"));
	    assertNotNull(listofAccesscodes);
	}

	@Test
	public void testpageIt() {		
		Sort sort = new Sort();
		sort.setField("accessCode");
		io.leangen.graphql.execution.SortField.Direction direction = Direction.ASC;
		sort.setOrder(direction);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<AccessCodes> e = Example.of(new AccessCodes(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(accessCodeRepoService.pageIt(any(Pageable.class),Matchers.<Example<AccessCodes>>any())).thenReturn(getPageofAccessCodes());
		Page<AccessCodes> pageofaccesscodes = underTest.pageIt(pageable, e);
		assertNotNull(pageofaccesscodes);
		
	}

	@Test
	public void testgenerateAccessCodes() {
		AccessCodes accesscodes = getAccessCodes();
		List<AccessCodes> accesscodeslist = asList(accesscodes);
		when(accessCodeRepoService.generateAccessCodes(any(AccessCodes.class))).thenReturn(accesscodeslist);
		accesscodeslist = underTest.generateAccessCodes(accesscodes);
		assertNotNull(accesscodeslist);
	}
	
	@Test
	public void testgetAccessCodesFromBatch() {
		AccessCodes accesscodes = getAccessCodes();
		List<AccessCodes> accesscodeslist = asList(accesscodes);
		when(accessCodeRepoService.getAccessCodesFromBatch(anyString())).thenReturn(accesscodeslist);
		accesscodeslist = underTest.getAccessCodesFromBatch("batch");
		assertNotNull(accesscodeslist);
	}
	
	private Page<AccessCodes> getPageofAccessCodes() {
		List<AccessCodes> listaccessCodes = asList(getAccessCodes());
		Page<AccessCodes> pageaccessCodes = new PageImpl<AccessCodes>(listaccessCodes);
		return pageaccessCodes;
	}

	
	private AccessCodes getAccessCodes() {
		AccessCodes accesscodes = new AccessCodes();
		accesscodes.setAccessCode("HPB-6JF-RGJT");
		return accesscodes;
	}

}
