package com.pearson.sam.bridgeapi.serviceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.pearson.sam.bridgeapi.model.ChannelPartner;
import com.pearson.sam.bridgeapi.model.Holder;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.LicenceHolderRespository;
import com.pearson.sam.bridgeapi.serviceimpl.IndexService;
import com.pearson.sam.bridgeapi.serviceimpl.LicenceHolderServiceIml;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.ArrayList;
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
public class LicenceHolderServiceImplTest {
	@Mock
	private LicenceHolderRespository licenceHolderRespository;

	@Mock
	private IndexService indexService;

	@InjectMocks
	private LicenceHolderServiceIml underTest = new LicenceHolderServiceIml();

	@Test
	public void testCreate() {
		when(licenceHolderRespository.save(any(LicenceHolder.class))).thenReturn(new LicenceHolder());
		LicenceHolder holder = underTest.create(getLicenceHolder());
		Assert.assertNotNull(holder);
	}

	@Test
	public void testUpdate() {
		LicenceHolder licenceHolder = new LicenceHolder();
		licenceHolder.setLicenceHolderId("PP2LICENCEHOLDER00");

		Holder holder = new Holder();
		School school = new School();
		school.setSchoolId("SCHID01");
		holder.setSchool(school);

		ChannelPartner organisation = new ChannelPartner();
		organisation.setOrganisationId("ORGANISATIONID01");
		holder.setOrganisation(organisation);

		Product product = new Product();
		product.setProductId("PP2PRODUCT40");
		holder.setProduct(product);

		licenceHolder.setHolder(holder);

		Assert.assertNotNull(licenceHolder.getLicenceHolderId());
		when(licenceHolderRespository.findByLicenceHolderId(any(String.class))).thenReturn(getLicenceHolder());

		Assert.assertNotNull(licenceHolder.getHolder().getSchool().getSchoolId());
		when(licenceHolderRespository.findByHolderSchoolSchoolId(any(String.class))).thenReturn(getLicenceHolder());

		Assert.assertNotNull(licenceHolder.getHolder().getOrganisation().getOrganisationId());
		when(licenceHolderRespository.findByHolderOrganisationOrganisationId(any(String.class)))
				.thenReturn(getLicenceHolder());

		when(licenceHolderRespository.save(any(LicenceHolder.class))).thenReturn(new LicenceHolder());
		licenceHolder = underTest.update(getLicenceHolder(),"SCHOOL");
		Assert.assertNotNull(licenceHolder);
	}

	@Test
	public void testUpdateWithEmptyLicenceHolderId() {
		LicenceHolder licenceHolder = new LicenceHolder();
		licenceHolder.setLicenceHolderId(null);

		Holder holder = new Holder();
		School school = new School();
		school.setSchoolId("SCHID01");
		holder.setSchool(school);

		ChannelPartner organisation = new ChannelPartner();
		organisation.setOrganisationId("ORGANISATIONID01");
		holder.setOrganisation(organisation);

		Product product = new Product();
		product.setProductId("PP2PRODUCT40");
		holder.setProduct(product);

		licenceHolder.setHolder(holder);

		Assert.assertNotNull(licenceHolder.getHolder().getSchool().getSchoolId());
		when(licenceHolderRespository.findByHolderSchoolSchoolId(any(String.class))).thenReturn(getLicenceHolder());

		Assert.assertNotNull(licenceHolder.getHolder().getOrganisation().getOrganisationId());
		when(licenceHolderRespository.findByHolderOrganisationOrganisationId(any(String.class)))
				.thenReturn(getLicenceHolder());

		when(licenceHolderRespository.save(any(LicenceHolder.class))).thenReturn(new LicenceHolder());
		licenceHolder = underTest.update(licenceHolder,"SCHOOL");
		Assert.assertNotNull(licenceHolder);
	}

	@Test
	public void testUpdateWithEmptyLicenceHolderIdAndSchoolId() {
		LicenceHolder licenceHolder = new LicenceHolder();
		licenceHolder.setLicenceHolderId(null);

		Holder holder = new Holder();
		School school = new School();
		school.setSchoolId(null);
		holder.setSchool(school);

		ChannelPartner organisation = new ChannelPartner();
		organisation.setOrganisationId("ORGANISATIONID01");
		holder.setOrganisation(organisation);

		Product product = new Product();
		product.setProductId("PP2PRODUCT40");
		holder.setProduct(product);

		licenceHolder.setHolder(holder);

		Assert.assertNotNull(licenceHolder.getHolder().getOrganisation().getOrganisationId());
		when(licenceHolderRespository.findByHolderOrganisationOrganisationId(any(String.class)))
				.thenReturn(getLicenceHolder());

		when(licenceHolderRespository.save(any(LicenceHolder.class))).thenReturn(new LicenceHolder());
		licenceHolder = underTest.update(licenceHolder,"SCHOOL");
		Assert.assertNotNull(licenceHolder);
	}

	@Test
	public void testFetch() {
		when(licenceHolderRespository.findByLicenceHolderId(any(String.class))).thenReturn(getLicenceHolder());
		LicenceHolder holder = underTest.fetch(getLicenceHolder());
		Assert.assertNotNull(holder);
	}

	@Test
	public void testPageIt() {
		Sort sort = new Sort();
		sort.setField("licenceHolderId");
		io.leangen.graphql.execution.SortField.Direction d = Direction.ASC;
		sort.setOrder(d);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<LicenceHolder> e = Example.of(new LicenceHolder(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(licenceHolderRespository.findAll(any(Example.class), any(Pageable.class)))
				.thenReturn(getLicenceHolderList());
		Page<LicenceHolder> page = underTest.pageIt(pageable, e);
		Assert.assertNotNull(page);
	}

	private LicenceHolder getLicenceHolder() {
		LicenceHolder licenceHolder = new LicenceHolder();
		licenceHolder.setLicenceHolderId("PP2LICENCEHOLDER00");

		Holder holder = new Holder();
		School school = new School();
		school.setSchoolId("SCHID01");
		holder.setSchool(school);

		ChannelPartner organisation = new ChannelPartner();
		organisation.setOrganisationId("ORGANISATIONID01");
		holder.setOrganisation(organisation);

		Product product = new Product();
		product.setProductId("PP2PRODUCT40");
		holder.setProduct(product);

		licenceHolder.setHolder(holder);

		licenceHolder.setCreatedDate(String.valueOf(System.currentTimeMillis()));

		return licenceHolder;
	}

	private Page<LicenceHolder> getLicenceHolderList() {
		LicenceHolder licenceHolder = new LicenceHolder();
		licenceHolder.setLicenceHolderId("PP2LICENCEHOLDER00");
		List<LicenceHolder> listLicenceHolder = new ArrayList<LicenceHolder>();
		listLicenceHolder.add(licenceHolder);
		Page<LicenceHolder> result = new PageImpl<LicenceHolder>(listLicenceHolder);
		return result;
	}

}
