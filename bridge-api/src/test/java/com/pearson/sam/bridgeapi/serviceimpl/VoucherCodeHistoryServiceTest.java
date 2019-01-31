package com.pearson.sam.bridgeapi.serviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.VoucherCodeHistory;
import com.pearson.sam.bridgeapi.repository.VoucherCodeHistoryRepository;
import com.pearson.sam.bridgeapi.serviceimpl.VoucherCodeHistoryService;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.Arrays;
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
public class VoucherCodeHistoryServiceTest {
	@InjectMocks
	private VoucherCodeHistoryService underTest = new VoucherCodeHistoryService();
	
	@Mock
	private VoucherCodeHistoryRepository voucherCodeHistoryRepository;
	
	@Test
	public void testCreate() {
		when(voucherCodeHistoryRepository.findTopByOrderByIdDesc()).thenReturn(getVoucherCodeHistory());
		when(voucherCodeHistoryRepository.save(any(VoucherCodeHistory.class))).thenReturn(getVoucherCodeHistory());

		VoucherCodeHistory voucher = underTest.create(getVoucherCodeHistory());
		Assert.assertNotNull(voucher);
	}
	
	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("voucherCodeHistoryId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<VoucherCodeHistory> e = Example.of(new VoucherCodeHistory(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(voucherCodeHistoryRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(getPageVoucherCodeHistoryList());
		Page<VoucherCodeHistory> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}
	
	private List<VoucherCodeHistory> getVoucherCodeHistoryList() {
		List<VoucherCodeHistory> voucherCodeHistoryList = Arrays.asList(getVoucherCodeHistory());
		return voucherCodeHistoryList;
	}

	private Page<VoucherCodeHistory> getPageVoucherCodeHistoryList() {
		Page<VoucherCodeHistory> pageVoucherCodeHistory = new PageImpl<VoucherCodeHistory>(getVoucherCodeHistoryList());
		return pageVoucherCodeHistory;
	}
	
	private VoucherCodeHistory getVoucherCodeHistory() {
		VoucherCodeHistory voucherCodeHistory = new VoucherCodeHistory();
		voucherCodeHistory.setVoucherCodeHistoryId("PP2VOUCHERCODEHISTORY00");
		
		return voucherCodeHistory;
	}
}
