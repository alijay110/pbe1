package com.pearson.sam.bridgeapi.serviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.model.Indexes;
import com.pearson.sam.bridgeapi.repository.IndexRepository;
import com.pearson.sam.bridgeapi.serviceimpl.IndexService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IndexServiceTest {

	@Mock
	private IndexRepository indexRepository;

	@InjectMocks
	private IndexService underTest = new IndexService();

	@Test
	public void testGetNextId() {
		Indexes index = new Indexes();
		index.setCounter(5);
		index.setSeqName("seq");
		when(indexRepository.findBySeqName(anyString())).thenReturn(index);
		when(indexRepository.save(any(Indexes.class))).thenReturn(index);
		int index1 = underTest.getNextId("seq");
		Assert.assertNotNull(index1);
	}
	
	@Test
	public void testGetNextIdNull() {
		Indexes index = new Indexes();
		index.setCounter(5);
		index.setSeqName("seq");
		
		when(indexRepository.findBySeqName(anyString())).thenReturn(null);
		when(indexRepository.save(any(Indexes.class))).thenReturn(index);
		int index1 = underTest.getNextId("seq");
		Assert.assertNotNull(index1);
	}
}
