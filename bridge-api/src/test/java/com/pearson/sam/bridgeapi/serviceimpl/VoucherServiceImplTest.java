package com.pearson.sam.bridgeapi.serviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.iservice.IAccessCodeRepoService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.repository.VoucherRepository;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.serviceimpl.IndexService;
import com.pearson.sam.bridgeapi.serviceimpl.VoucherServiceImpl;
import com.pearson.sam.bridgeapi.validators.ModelValidator;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@SuppressWarnings({ "rawtypes", "unchecked" })
@RunWith(MockitoJUnitRunner.class)
public class VoucherServiceImplTest {

	@Mock
	private IndexService idxService;

	@Mock
	private IAccessCodeRepoService accessCodeServices;

	@Mock
	private VoucherRepository voucherRepository;

	@Mock
	protected ISessionFacade sessionFacade;
	
	@Mock
	private ModelValidator modelValidator;

	@InjectMocks
	private VoucherServiceImpl underTest = new VoucherServiceImpl();

	@Test
	public void testCreate() {
		doNothing().when(modelValidator).validateModel(any(AccessCodes.class),anyString());
		when(idxService.getNextId(anyString())).thenReturn(5);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when(voucherRepository.save(any(Voucher.class))).thenReturn(getVoucher());
		Voucher voucher = underTest.create(getVoucher());
		Assert.assertNotNull(voucher);
	}

	@Test
	public void testCreateMultiple() {
		when(idxService.getNextId(anyString())).thenReturn(5);
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when(accessCodeServices.addAccessCodes(anyString())).thenReturn(getVoucherMap());
		when(voucherRepository.insert(anyList())).thenReturn(getVoucherList());
		List<Voucher> voucherList = underTest.createMultiple(getVoucher());
		Assert.assertNotNull(voucherList);
	}

	@Test
	public void testUpdate() {
		when(voucherRepository.findByVoucherCode(anyString())).thenReturn(getVoucher());
		when(voucherRepository.save(any(Voucher.class))).thenReturn(getVoucher());
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		Voucher voucher = underTest.update(getVoucher());
		Assert.assertNotNull(voucher);
	}

	@Test
	public void testFetchOne() {
		when(voucherRepository.findByVoucherCode(anyString())).thenReturn(getVoucher());
		Voucher voucher = underTest.fetchOne(getVoucher());
		Assert.assertNotNull(voucher);
	}

	@Test
	public void testgetVoucherFromBatch() {
		when(voucherRepository.findByBatch(anyString())).thenReturn(getVoucherList());
		List<Voucher> voucherList = underTest.getVoucherFromBatch("batch1");
		Assert.assertNotNull(voucherList);
	}

	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("voucherCode");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Voucher> e = Example.of(new Voucher(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(voucherRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(getPageVoucherList());
		Page<Voucher> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}

	private SessionUser getSessionUser() {
		SessionUser sessionUser = new SessionUser();
		sessionUser.setUserName("mlsreekanth");
		sessionUser.setUid("chandraBoseCC1");
		return sessionUser;
	}

	private Voucher getVoucher() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Voucher voucherCode = new Voucher();
		voucherCode.setVoucherCode("REA3-G5M4-T7Q7");
		voucherCode.setVoucherId("PP2VOUCHER00");
		voucherCode.setQuantity(3);
		voucherCode.setType("VCE English");
		voucherCode.setDateCreated(cal.getTime().toString());
		voucherCode.setCreatedBy("PP");
		voucherCode.setUpdatedBy("chandraBoseCC1");
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -4);
		voucherCode.setUpdatedOn(cal.getTime().toString());
		return voucherCode;
	}

	private List<Voucher> getVoucherList() {
		List<Voucher> voucherList = Arrays.asList(getVoucher());
		return voucherList;
	}

	private Page<Voucher> getPageVoucherList() {
		Page<Voucher> pageVoucher = new PageImpl<Voucher>(getVoucherList());
		return pageVoucher;
	}
	
	private Map<String, Object> getVoucherMap() {	
		Map<String, Object> voucherMap = new HashMap<>();
		voucherMap.put("batchIdentifier", "111");
		voucherMap.put("createdOn", getVoucher().getDateCreated());
		voucherMap.put("accessCode", getVoucher().getVoucherCode());	
		List<Map<String, Object>> voucherCodeMapList = Arrays.asList(voucherMap);	
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("accessCodeList", voucherCodeMapList);
		return bodyMap;
	}
}
