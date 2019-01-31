package com.pearson.sam.bridgeapi.serviceimpl;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.constants.ValidatorConstants;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.AccessCodeRepository;
import com.pearson.sam.bridgeapi.samclient.AccessCodeSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.serviceimpl.AccessCodeRepoService;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.leangen.graphql.execution.SortField.Direction;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//TODO: Null branch not done due to NPE, code fix needed
@RunWith(MockitoJUnitRunner.class)
public class AccessCodeRepoServiceTest {

	@Mock
	private AccessCodeSamClient accessCodeSamClient;

	@Mock
	private AccessCodeRepository accessCodeRepository;

	@Mock
	protected ISessionFacade sessionFacade;
	
	@Mock
	private ModelValidator modelValidator;
	
	@InjectMocks
	private AccessCodeRepoService underTest;
	
	
	//TODO: Null Branching not done.. NPE while initHashMap.. Code fix required
	@Test
	public void testcreate() {
		AccessCodes mongoaccesscodes = getMongoAccessCodes();
		when(accessCodeSamClient.create(any(JsonObject.class))).thenReturn(getAccessCodes());
		when(accessCodeRepository.save(any(AccessCodes.class))).thenReturn(mongoaccesscodes);
		AccessCodes accesscodes = underTest.create(getAccessCodes());
		assertNotNull(accesscodes);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testcreateGraphException() {
		when(accessCodeSamClient.create(any(JsonObject.class))).thenReturn(getAccessCodes());
		when(accessCodeRepository.save(any(AccessCodes.class))).thenThrow(new BridgeApiGeneralException("Exception in Creating access codes"));
		underTest.create(getAccessCodes());
	}
	
	@Test
	public void testupdate() {
		AccessCodes accesscodestoupdate = getAccessCodes();
		when(accessCodeRepository.findByCode(anyString())).thenReturn(accesscodestoupdate);
		when(accessCodeSamClient.update(any(),any(JsonObject.class))).thenReturn(accesscodestoupdate);
		when(accessCodeRepository.save(any(AccessCodes.class))).thenReturn(accesscodestoupdate);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		AccessCodes accesscodes = underTest.update(accesscodestoupdate);
		assertNotNull(accesscodes);
		
		accesscodestoupdate.setLastActivatedBy(null);
		accesscodestoupdate.setLastActivatedDate(null);
		when(accessCodeRepository.findByCode(anyString())).thenReturn(accesscodestoupdate);
		when(accessCodeSamClient.update(any(),any(JsonObject.class))).thenReturn(accesscodestoupdate);
		when(accessCodeRepository.save(any(AccessCodes.class))).thenReturn(accesscodestoupdate);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		accesscodes = underTest.update(accesscodestoupdate);
		assertNotNull(accesscodes);
	}
	
	@Test
	public void testupdateIsVoidNull() {
		AccessCodes accesscodestoupdate = getAccessCodes();
		accesscodestoupdate.setIsVoid(null);
		when(accessCodeRepository.findByCode("7HJ-HCG-GG3X")).thenReturn(accesscodestoupdate);
		when(accessCodeSamClient.update(any(),any(JsonObject.class))).thenReturn(accesscodestoupdate);
		when(accessCodeRepository.save(any(AccessCodes.class))).thenReturn(accesscodestoupdate);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		AccessCodes accesscodes = underTest.update(accesscodestoupdate);
		assertNotNull(accesscodes);
	}
	
	@Test
	public void testupdateTotalReactivationsNull() {
		AccessCodes accesscodestoupdate = getAccessCodes();
		AccessCodes dbaccesscodes = accesscodestoupdate;
		dbaccesscodes.setTotalReactivations(null);
		accesscodestoupdate.setIsVoid(true);
		when(accessCodeRepository.findByCode(anyString())).thenReturn(dbaccesscodes);
		when(accessCodeSamClient.update(any(),any(JsonObject.class))).thenReturn(accesscodestoupdate);
		when(accessCodeRepository.save(any(AccessCodes.class))).thenReturn(accesscodestoupdate);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		AccessCodes accesscodes = underTest.update(accesscodestoupdate);
		assertNotNull(accesscodes);
	}
	
	@Test
	public void testupdateTotalReactivationZero() {
		AccessCodes accesscodestoupdate = getAccessCodes();
		AccessCodes dbaccesscodes = accesscodestoupdate;
		dbaccesscodes.setTotalReactivations(0);
		accesscodestoupdate.setIsVoid(true);
		
		when(accessCodeRepository.findByCode(anyString())).thenReturn(dbaccesscodes);
		when(accessCodeSamClient.update(any(),any(JsonObject.class))).thenReturn(accesscodestoupdate);
		when(accessCodeRepository.save(any(AccessCodes.class))).thenReturn(accesscodestoupdate);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		AccessCodes accesscodes = underTest.update(accesscodestoupdate);
		assertNotNull(accesscodes);
	}
	
	@Test
	public void testupdateSaveGraphException() {
		AccessCodes accesscodestoupdate = getAccessCodes();
		when(accessCodeRepository.findByCode(anyString())).thenReturn(accesscodestoupdate);
		when(accessCodeSamClient.update(any(),any(JsonObject.class))).thenReturn(accesscodestoupdate);
		when(accessCodeRepository.save(any(AccessCodes.class))).thenThrow(new BridgeApiGeneralException("Exception in updating access codes"));
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		AccessCodes accesscodes = underTest.update(accesscodestoupdate);
		assertNotNull(accesscodes);
	}
	
	
	@Test(expected=BridgeApiGeneralException.class)
	public void testupdateIsVoid() {
		AccessCodes accesscodestoupdate = getAccessCodes();
		accesscodestoupdate.setIsVoid(true);
		accesscodestoupdate.setTotalReactivations(1);
		when(accessCodeRepository.findByCode(anyString())).thenReturn(accesscodestoupdate);
		underTest.update(accesscodestoupdate);
	}
	
	@Test(expected=BridgeApiGeneralException.class)
	public void testupdateDBAccessNull() {
		AccessCodes accesscodestoupdate = getAccessCodes();
		when(accessCodeRepository.findByCode(anyString())).thenReturn(null);
		underTest.update(accesscodestoupdate);
	}
	
	@Test
	public void testaddAccessCodes() {
		Map<String,Object> map = new HashMap<>();
		when(accessCodeSamClient.addAccessCodes(anyString())).thenReturn(map);
		map = underTest.addAccessCodes("addaccesscodes");
		assertNotNull(map);
	}
	
	@Test
	public void testfetchOne() {
		when(accessCodeRepository.findByCode("7HJ-HCG-GG3X")).thenReturn(getAccessCodes());
		when(accessCodeSamClient.fetchOne(any(JsonObject.class))).thenReturn(getAccessCodes());
		AccessCodes accesscodes = underTest.fetchOne(getAccessCodes());
		assertNotNull(accesscodes);
		
		accesscodes = getAccessCodes();
		accesscodes.setCode("");
		when(accessCodeRepository.findByCode("")).thenReturn(null);
		accesscodes = underTest.fetchOne(accesscodes);
		assertNull(accesscodes);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testfetchOneException() {
		when(accessCodeRepository.findByCode(anyString())).thenReturn(getAccessCodes());
		when(accessCodeSamClient.fetchOne(any(JsonObject.class))).thenThrow(new BridgeApiGeneralException("Exception in fetching record"));;
		AccessCodes accesscodes = underTest.fetchOne(getAccessCodes());
		assertNotNull(accesscodes);
	}
	
	@Test
	public void testfetchMultiple() {
		List<AccessCodes> accesslist = asList(getAccessCodes()); 
		List<String> ids = asList("1","2");
		when(accessCodeRepository.findAll()).thenReturn(accesslist);
		List<AccessCodes> accesscodeslist = underTest.fetchMultiple(KeyType.ACCESS_CODES, ids); 
		assertNotNull(accesscodeslist);
		
		accesscodeslist = underTest.fetchMultiple(KeyType.USER_ID, ids); 
		assertNull(accesscodeslist);
		
		accesscodeslist = underTest.fetchMultiple(KeyType.DEFAULT, ids); 
		assertNull(accesscodeslist);
		
		accesscodeslist = underTest.fetchMultiple(KeyType.SCHOOL_ID, ids); 
		assertNull(accesscodeslist);
	}
	
	
	
	@Test
	public void fetchOneNullAccessCode() {
		AccessCodes accesscode = new AccessCodes();
		accesscode = underTest.fetchOne(accesscode);
		assertNull(accesscode);
	}
	
	@Test
	public void testpageIt() {
		Sort sort = new Sort();
		sort.setField("accessCode");
		io.leangen.graphql.execution.SortField.Direction direction = Direction.ASC;
		sort.setOrder(direction);
		Pageable pageable = PageRequest.of(0, 1,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<AccessCodes> e = Example.of(new AccessCodes(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(accessCodeRepository.findAll(any(),any(Pageable.class))).thenReturn(getPageofAccessCodes());
		Page<AccessCodes> pageofaccesscodes = underTest.pageIt(pageable, e);
		assertNotNull(pageofaccesscodes);
	}
	
	@Test
	public void testgenerateAccessCodes() {
		List<AccessCodes> acceslist = asList(getAccessCodes());
		when(accessCodeRepository.findTopByOrderByIdDesc()).thenReturn(getAccessCodes());
		when(accessCodeSamClient.createMultiple(any(JsonObject.class))).thenReturn(acceslist);
		when(accessCodeRepository.insert(acceslist)).thenReturn(acceslist);
		List<AccessCodes> accesscodeslist = underTest.generateAccessCodes(getAccessCodes());
		assertNotNull(accesscodeslist);
	}
	
	@Test(expected=BridgeApiGraphqlException.class)
	public void testgenerateAccessCodesInsertException() {
		List<AccessCodes> acceslist = asList(getAccessCodes());
		when(accessCodeRepository.findTopByOrderByIdDesc()).thenReturn(getAccessCodes());
		when(accessCodeSamClient.createMultiple(any(JsonObject.class))).thenReturn(acceslist);
		when(accessCodeRepository.insert(acceslist)).thenThrow(new BridgeApiGeneralException("Exception in generating access codes"));;
		underTest.generateAccessCodes(getAccessCodes());
	}
	
	
	@Test(expected = BridgeApiGraphqlException.class)
	public void testgetAccessCodesFromBatchGraphqlException() {
		when(accessCodeRepository.findByBatch(anyString())).thenThrow(new BridgeApiGeneralException("Exception in findingbatch"));
		underTest.getAccessCodesFromBatch("testbatch");
	}
	
	@Test
	public void testgetAccessCodesFromBatch() {
		when(accessCodeRepository.findByBatch(anyString())).thenReturn(asList(getMongoAccessCodes()));
		List<AccessCodes> findByBatch = underTest.getAccessCodesFromBatch("testbatch");
		assertNotNull(findByBatch);
	}
	
	@Test(expected = BridgeApiGraphqlException.class)
	public void testgetAccessCodesFromIncorrectBatch() {
		when(accessCodeRepository.findByBatch(anyString())).thenThrow(new BridgeApiGraphqlException("Incorrect Batch..Not Found in Repository.."));
		underTest.getAccessCodesFromBatch("IncorrectBatch");
	}
	
	@Test
	public void testdelete() {
		AccessCodes accesscodes = underTest.delete(getAccessCodes());
		assertNull(accesscodes);
	}
	
	private AccessCodes getMongoAccessCodes() {
		AccessCodes accesscodes = new AccessCodes();
		Instant instanttime = Instant.now();
		ZonedDateTime zoneddateandtime = instanttime.atZone(ZoneId.of(ValidatorConstants.TIME_ZONE));
		String dateandtime = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy").format(zoneddateandtime);
		accesscodes.setAccessCode("7HJ-HCG-GG3X");
		accesscodes.setCode("7HJ-HCG-GG3X");
		accesscodes.setCreatedBy("PP");
		accesscodes.setDateCreated(dateandtime);
		return accesscodes;
	}
	private AccessCodes getAccessCodes() {
		AccessCodes accesscodes = new AccessCodes();
		Instant instanttime = Instant.now();
		ZonedDateTime zoneddateandtime = instanttime.atZone(ZoneId.of(ValidatorConstants.TIME_ZONE));
		String dateandtime = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy").format(zoneddateandtime);
		accesscodes.setAccessCode("7HJ-HCG-GG3X");
		accesscodes.setCode("7HJ-HCG-GG3X");
		accesscodes.setCreatedBy("PP");
		accesscodes.setDateCreated(dateandtime);
		accesscodes.setLastActivatedBy("PP");
		accesscodes.setLastActivatedDate(dateandtime);
		accesscodes.setIsVoid(false);
		accesscodes.setBatch("0");
		return accesscodes;
	}
	
	private final SessionUser getSessionUser() {
		SessionUser sessionuser = new SessionUser();
		sessionuser.setUid("chandraBoseCC1");
		return sessionuser;
	}
	
	private final Page<AccessCodes> getPageofAccessCodes(){
		List<AccessCodes> listaccessCodes = asList(getAccessCodes());
		Page<AccessCodes> pageaccessCodes = new PageImpl<AccessCodes>(listaccessCodes);
		return pageaccessCodes;
	}
	
}
