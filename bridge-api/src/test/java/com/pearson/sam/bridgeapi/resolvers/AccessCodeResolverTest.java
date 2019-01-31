package com.pearson.sam.bridgeapi.resolvers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.ibridgeservice.IAccessCodeBridgeService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.Sort;

import graphql.ErrorType;
import io.leangen.graphql.execution.SortField.Direction;

@RunWith(MockitoJUnitRunner.class)
public class AccessCodeResolverTest {

	@Mock
	private AuthContext authContex;
	
	@Mock
	IAccessCodeBridgeService accessCodeBridgeService;
	
	@InjectMocks
	private AccessCodeResolver underTest;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGenerateMultipleAccessCodes() {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("test");
		accessCodes.setQuantity(20);
		List<AccessCodes> lstAccessCodes = new ArrayList<AccessCodes>();
		when(accessCodeBridgeService.generateAccessCodes(any(AccessCodes.class))).thenReturn(getAccessCodesList());
		lstAccessCodes = underTest.generateMultipleAccessCodes(accessCodes);
		Assert.assertNotNull(lstAccessCodes);
	}
	
	@Test(expected = BridgeApiGraphqlException.class)
	public void testGenerateMultipleAccessCodesWithMoreQunatity() {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("test");
		accessCodes.setQuantity(26);
		List<AccessCodes> lstAccessCodes = new ArrayList<AccessCodes>();
		underTest.generateMultipleAccessCodes(accessCodes);
	}
	
	@Test(expected = BridgeApiGraphqlException.class)
	public void testGenerateLargeAccessCodesWithnoResponse() {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("test");
		List<AccessCodes> getAccessCodesList= new ArrayList<AccessCodes>();
		getAccessCodesList.add(null);
		when(accessCodeBridgeService.generateAccessCodes(accessCodes)).thenReturn(getAccessCodesList);
		accessCodes = underTest.generateLargeAccessCodes(accessCodes);
		Assert.assertNotNull(accessCodes);
	}
	
	@Test
	public void testGenerateLargeAccessCodes() {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("test");
		when(accessCodeBridgeService.generateAccessCodes(accessCodes)).thenReturn(getAccessCodesList());
		accessCodes = underTest.generateLargeAccessCodes(accessCodes);
		Assert.assertNotNull(accessCodes);
	}
	
	@Test
	public void testGetAccessCode() throws IOException, ProcessingException {
		when(accessCodeBridgeService.fetchOne(any(AccessCodes.class))).thenReturn(new AccessCodes());
		AccessCodes result = underTest.getAccessCode("accessCode");
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testUpdateAccessCode() throws IOException, ProcessingException {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("test");
		when(accessCodeBridgeService.update(any(AccessCodes.class))).thenReturn(new AccessCodes());
		AccessCodes result = underTest.updateAccessCode("accessCode",accessCodes);
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testGetProductsByAccessCode() throws IOException, ProcessingException {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("test");
		when(accessCodeBridgeService.fetchOne(any(AccessCodes.class))).thenReturn(new AccessCodes());
		AccessCodes result = underTest.getProductsByAccessCode("accessCode",authContex);
		Assert.assertNotNull(result);
	}
		
	@Test
	public <T> void testGetPaginatedAccessCodeTableData() {	
		Page<AccessCodes> pageAccessCodes1 = new PageImpl(getAccessCodesList());
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("test");
		Example<AccessCodes> e = Example.of(accessCodes, ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		Sort sort = new Sort();
		sort.setField("accessCode");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when(( accessCodeBridgeService.pageIt(any(Pageable.class),any(Example.class)))).thenReturn(getAccessCodes());
//		Page<AccessCodes> pageAccessCodes = underTest.getPaginatedAccessCodeTableData(0,15,accessCodes,StringMatcher.STARTING,sort,authContex);
//		Assert.assertNotNull(pageAccessCodes);
	}	
	
	public List<AccessCodes> getAccessCodesList() {
		AccessCodes accessCodes = new AccessCodes();
		accessCodes.setAccessCode("accessCodes");
		accessCodes.setQuantity(20);
		List<AccessCodes> lstAccessCodes = new ArrayList<AccessCodes>();
		lstAccessCodes.add(accessCodes);
		return lstAccessCodes;
	}
	
	public Page<AccessCodes> getAccessCodes(){
		Page<AccessCodes> pageAccessCodes = new PageImpl<AccessCodes>(getAccessCodesList());
		return pageAccessCodes;
	}

}
