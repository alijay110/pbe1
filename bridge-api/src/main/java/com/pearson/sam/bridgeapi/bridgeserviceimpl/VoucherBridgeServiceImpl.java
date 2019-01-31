/**
 * 
 */
package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.VoucherCodeSearch;
import com.pearson.sam.bridgeapi.ibridgeservice.IVoucherBridgeService;
import com.pearson.sam.bridgeapi.iservice.IVoucherCodeSearchService;
import com.pearson.sam.bridgeapi.iservice.IVoucherService;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.GraphQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author VGUDLSA
 *
 */
@Component
public class VoucherBridgeServiceImpl implements IVoucherBridgeService{
	
	@Autowired
	IVoucherService voucherService;
	
	@Autowired
	IVoucherCodeSearchService voucherCodeSearchService;

	
	 public void loadVouchersToMaster() {
	    initLoad(PageRequest.of(0, voucherService.getTotalCount().intValue()));
	  }

	  private static Example<Voucher> voucherEx = Example.of(new Voucher());

	  private void initLoad(Pageable pageable) {

	    Page<Voucher> pageVoucher = voucherService.pageIt(pageable, voucherEx);
	    
	    loadVoucherCodesinListToElastic(pageVoucher.getContent());    
	  }


	  private List<Voucher> loadVoucherCodesinListToElastic(List<Voucher> voucherCodeList) {
	    DataLoaderOptions dlo = new DataLoaderOptions();
	    dlo.setBatchingEnabled(true);
	    dlo.setCachingEnabled(true);
	    dlo.setMaxBatchSize(Utils.batchSizeCount(voucherCodeList.size()));

	      BatchLoader<Voucher, Voucher> batchLoader = (List<Voucher> voucherCodes) -> {
	        return CompletableFuture.supplyAsync(() -> {
	          this.loadVoucherCodeToElastic(voucherCodes);
	          return voucherCodes;
	        });
	      };
	      
	    DataLoader<Voucher,Voucher> usersDataLoader = new DataLoader<>(batchLoader, dlo);
	    CompletableFuture<List<Voucher>> cf = usersDataLoader.loadMany(voucherCodeList);
	    usersDataLoader.dispatchAndJoin();

	    List<Voucher> l = null;
	    try {
	      l = cf.get();
	    } catch (InterruptedException e) {
	      throw new GraphQLException(e.getMessage());
	    } catch (ExecutionException e) {
	      throw new GraphQLException(e.getMessage());
	    }

	    return l;
	  }

	  private void loadVoucherCodeToElastic(List<Voucher> voucherCodeList) {

	    List<VoucherCodeSearch> voucherSearchList= voucherCodeList.parallelStream().map(this::getVoucherCodeSearchObject).collect(Collectors.toList());

	    voucherCodeSearchService.saveAll(voucherSearchList);
	  }

	  
	@Override
	public Voucher create(Voucher voucher) {
		voucher = voucherService.create(voucher);
		voucherCodeSearchService.save(getVoucherCodeSearchObject(voucher));
		return voucher;
	}

	@Override
	public List<Voucher> createMultiple(Voucher voucher) {
		List<Voucher> vouchers = voucherService.createMultiple(voucher);
		voucherCodeSearchService.saveAll(getVoucherCodeSearchObjects(vouchers));
		return vouchers;
	}

	@Override
	public Voucher update(Voucher voucher) {
		return voucherService.update(voucher);
	}

	@Override
	public Voucher fetchOne(Voucher voucher) {
		return voucherService.fetchOne(voucher);
	}

	@Override
	public List<Voucher> getVoucherFromBatch(String batch) {
		return voucherService.getVoucherFromBatch(batch);
	}

	@Override
	public Page<Voucher> pageIt(Pageable pageable, Example<Voucher> e) {
		return voucherService.pageIt(pageable, e);
	}
	
	@Override
	public Page<VoucherCodeSearch> search(Pageable pageable, String searchable) {
		return voucherCodeSearchService.search(pageable, searchable);
	}
	
	private VoucherCodeSearch getVoucherCodeSearchObject(Voucher voucher) {
		VoucherCodeSearch voucherCodeSearch = new VoucherCodeSearch();
		Utils.copyNonNullProperties(voucher, voucherCodeSearch);
		return voucherCodeSearch;
	}
	
	private List<VoucherCodeSearch> getVoucherCodeSearchObjects(List<Voucher> vouchers) {
		List<VoucherCodeSearch> voucherCodeSearchs = new ArrayList<>();
		for(Voucher voucher:vouchers)
			voucherCodeSearchs.add(getVoucherCodeSearchObject(voucher));
		return voucherCodeSearchs;
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.ibridgeservice.IVoucherBridgeService#pageIt(org.springframework.data.domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch)
   */
  @Override
  public Page<VoucherCodeSearch> pageIt(Pageable pageable, UserSearch searchable,StringMatcher sm) {
    return voucherCodeSearchService.pageIt(pageable, searchable,sm);
  }

}
