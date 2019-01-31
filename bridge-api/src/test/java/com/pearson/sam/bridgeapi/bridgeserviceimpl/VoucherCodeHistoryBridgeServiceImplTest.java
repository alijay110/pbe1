package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.bridgeserviceimpl.VoucherCodeHistoryBridgeServiceImpl;
import com.pearson.sam.bridgeapi.iservice.IVoucherCodeHistoryService;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.VoucherCodeHistory;

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
public class VoucherCodeHistoryBridgeServiceImplTest {

	@Mock
	IVoucherCodeHistoryService voucherCodeHistoryService;
	
	@InjectMocks
	private VoucherCodeHistoryBridgeServiceImpl underTest;
	
	
	@Test
	public void testcreate() {
		VoucherCodeHistory createvouchercodehistory = getVoucherCodeHistory();
		when(voucherCodeHistoryService.create(any())).thenReturn(createvouchercodehistory);
		VoucherCodeHistory vouchercodehistory = underTest.create(createvouchercodehistory);
		assertNotNull(vouchercodehistory);
	}

	@Test
	public void testpageIt() {		
		Sort sort = new Sort();
		sort.setField("voucherCodeHistoryId");
		io.leangen.graphql.execution.SortField.Direction direction = Direction.ASC;
		sort.setOrder(direction);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<VoucherCodeHistory> e = Example.of(new VoucherCodeHistory(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(voucherCodeHistoryService.pageIt(any(),any())).thenReturn(getPageofVoucher());
		Page<VoucherCodeHistory> pageOfVoucherHistory = underTest.pageIt(pageable, e);
		assertNotNull(pageOfVoucherHistory);
		
	}
	
	private Page<VoucherCodeHistory> getPageofVoucher() {
		List<VoucherCodeHistory> listvoucherhistory = asList(getVoucherCodeHistory());
		Page<VoucherCodeHistory> pagelistVoucherhistory = new PageImpl<VoucherCodeHistory>(listvoucherhistory);
		return pagelistVoucherhistory;
	}
	
	private VoucherCodeHistory getVoucherCodeHistory() {
		VoucherCodeHistory vouchercodehistory = new VoucherCodeHistory();
		vouchercodehistory.setVoucherCodeHistoryId("PP2VOUCHERCODEHISTORY1");
		vouchercodehistory.setAlterBy("chandraBoseCC1");
		return vouchercodehistory;
	}

}
