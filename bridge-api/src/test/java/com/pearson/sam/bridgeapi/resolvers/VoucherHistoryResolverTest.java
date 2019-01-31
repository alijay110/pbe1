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
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.IVoucherCodeHistoryBridgeService;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.model.VoucherCodeHistory;

import io.leangen.graphql.execution.SortField.Direction;

/**
 * Voucher History Resolver unit test cases
 * 
 * @author Param
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class VoucherHistoryResolverTest {

	@Mock
	private AuthContext authContext;

	@InjectMocks
	private VoucherHistoryResolver underTest;

	@Mock
	private IVoucherCodeHistoryBridgeService voucherCodeHistoryBridgeService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testUpdateVoucherHistory() throws IOException, ProcessingException {
		when(voucherCodeHistoryBridgeService.create(any(VoucherCodeHistory.class)))
				.thenReturn(new VoucherCodeHistory());
		VoucherCodeHistory result = underTest.updateVoucheristory(new VoucherCodeHistory());
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetPaginatedVoucherCodeTableData() throws IOException, ProcessingException {
		Sort sort = new Sort();
		sort.setField("voucherId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		when((voucherCodeHistoryBridgeService.pageIt(any(Pageable.class), any(Example.class))))
				.thenReturn(getVoucherCodeHistory());
		Page<VoucherCodeHistory> voucherCodeHistory = underTest.getPaginatedVoucherCodeTableData(0, 15,
				new VoucherCodeHistory(), StringMatcher.DEFAULT, sort, authContext);
		Assert.assertNotNull(voucherCodeHistory);
	}

	public Page<VoucherCodeHistory> getVoucherCodeHistory() {
		List<VoucherCodeHistory> voucherCodeHistory = new ArrayList<VoucherCodeHistory>();
		voucherCodeHistory.add(new VoucherCodeHistory());
		Page<VoucherCodeHistory> pageVoucherCodeHistory = new PageImpl<VoucherCodeHistory>(voucherCodeHistory);
		return pageVoucherCodeHistory;
	}
}