package com.pearson.sam.bridgeapi.serviceimpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Organisation;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.repository.OrganisationRepository;
import com.pearson.sam.bridgeapi.samclient.OrganisationSamClient;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.serviceimpl.OrganisationServiceImpl;

import io.leangen.graphql.execution.SortField.Direction;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-application.properties")
public class OrganisationServiceImplTest {

	static {
		System.setProperty("product", "PP");
		System.setProperty("env", "Local");
		
	}
	
	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Mock
	private OrganisationSamClient<Organisation> organisationSamClient;

	@Mock
	private OrganisationRepository organisationRepository;
	
	@Mock
	private ISessionFacade sessionFacade;
	
	@InjectMocks
	private OrganisationServiceImpl underTest;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, "specJsonFile", specJsonFile);
		
	}
	
	
	@Test
	public void testcreate() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when (organisationSamClient.create(any(JsonObject.class))).thenReturn(getOrganisation());
		when(organisationRepository.save(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = underTest.create(getOrganisation());
		assertNotNull(organisation);
	}
	
	@Test
	public void testcreateWithNoMiddleName() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when (organisationSamClient.create(any(JsonObject.class))).thenReturn(getOrganisation());
		when(organisationRepository.save(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = getOrganisation();
		organisation.setName("Pearson    Places");
		organisation = underTest.create(organisation);
		assertNotNull(organisation);
	}
	
	@Test
	public void testcreateWithFirstNametwoLength() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when (organisationSamClient.create(any(JsonObject.class))).thenReturn(getOrganisation());
		when(organisationRepository.save(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = getOrganisation();
		organisation.setName("PP    Places");
		organisation = underTest.create(organisation);
		assertNotNull(organisation);
	}
	
	@Test
	public void testcreateWithFirstNameOneLength() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when (organisationSamClient.create(any(JsonObject.class))).thenReturn(getOrganisation());
		when(organisationRepository.save(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = getOrganisation();
		organisation.setName("O    Pearson");
		organisation = underTest.create(organisation);
		assertNotNull(organisation);
	}
	
	@Test
	public void testcreateWithFirstNameEmpty() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when (organisationSamClient.create(any(JsonObject.class))).thenReturn(getOrganisation());
		when(organisationRepository.save(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = getOrganisation();
		organisation.setName("    Pearson");
		organisation = underTest.create(organisation);
		assertNotNull(organisation);
	}
	
	@Test
	public void testcreateWithEmptyName() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when (organisationSamClient.create(any(JsonObject.class))).thenReturn(getOrganisation());
		when(organisationRepository.save(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = getOrganisation();
		organisation.setName("   ");
		organisation = underTest.create(organisation);
		assertNotNull(organisation);
	}
	
	@Test
	public void testcreateWithblankFirstName() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when (organisationSamClient.create(any(JsonObject.class))).thenReturn(getOrganisation());
		when(organisationRepository.save(any(Organisation.class))).thenReturn(getOrganisation());
		Organisation organisation = getOrganisation();
		organisation.setName("");
		organisation = underTest.create(organisation);
		assertNotNull(organisation);
	}
	
	
	@Test
	public void testupdate() {
		when(sessionFacade.getLoggedInUser(true)).thenReturn(getSessionUser());
		when (organisationSamClient.update(Matchers.<Map<String,String>>any(),any(JsonObject.class))).thenReturn(getOrganisation());
		when(organisationRepository.findByOrganisationId(anyString())).thenReturn(getOrganisation());
		when(organisationRepository.save(any(Organisation.class))).thenReturn(getOrganisation());
		when(organisationSamClient.fetchOne(any(JsonObject.class))).thenReturn(getOrganisation());
		Organisation organisation = underTest.update(getOrganisation());
		assertNotNull(organisation);
	}
	
	@Test
	public void testdelete() {
		Organisation organisation = underTest.delete(getOrganisation());
		assertNull(organisation);
	}
	
	@Test
	public void testfetchMultiple() {
		List<Organisation> organisation = underTest.fetchMultiple(KeyType.ACCESS_CODES,asList("1"));
		assertNull(organisation);
	}
	
	//return organisationRepository.findAll(e, pageable);
	@Test
	public void testpageIt() {
		Sort sort = new Sort();
		sort.setField("organisationId");
		io.leangen.graphql.execution.SortField.Direction direction = Direction.ASC;
		sort.setOrder(direction);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		Example<Organisation> e = Example.of(new Organisation(),
				ExampleMatcher.matching().withStringMatcher(StringMatcher.DEFAULT));
		when(organisationRepository.findAll(Matchers.<Example<Organisation>>any(), any(Pageable.class))).thenReturn(getPageofOrganisation());
		Page<Organisation> pageofaccesscodeshistory = underTest.pageIt(pageable, e);
		assertNotNull(pageofaccesscodeshistory);
	}
	
	@Test
	public void testfindByOrganisationId() {
		when(organisationRepository.findByOrganisationId(anyString())).thenReturn(getOrganisation());
		when(organisationSamClient.fetchOne(any(JsonObject.class))).thenReturn(getOrganisation());
		Organisation organisation = underTest.findByOrganisationId("PeO108199");
		assertNotNull(organisation);
	}
	
	@Test
	public void pageItFromids() {
		when(organisationRepository.findAllByOrganisationIdIn(Matchers.<List<String>>any(),any(Pageable.class))).thenReturn(getPageofOrganisation());
		Sort sort = new Sort();
		sort.setField("organisationId");
		io.leangen.graphql.execution.SortField.Direction direction = Direction.ASC;
		sort.setOrder(direction);
		Pageable pageable = PageRequest.of(0, 10,
				org.springframework.data.domain.Sort.Direction.valueOf(sort.getOrder().toString()), sort.getField());
		when(organisationRepository.findAll(Matchers.<Example<Organisation>>any(), any(Pageable.class))).thenReturn(getPageofOrganisation());
		Page<Organisation> pageofaccesscodeshistory = underTest.pageIt(pageable,"Pearson",asList("PeO108199"));
		assertNotNull(pageofaccesscodeshistory);
		
	}
	
	private SessionUser getSessionUser() {
		SessionUser sessionuser = new SessionUser();
		sessionuser.setUid("chandraBoseCC1");
		return sessionuser;
	}
	
	private Page<Organisation> getPageofOrganisation(){
		List<Organisation> organisationlist = asList(getOrganisation());
		Page<Organisation> pageoforganisationlist = new PageImpl<Organisation>(organisationlist);
		return pageoforganisationlist;
	}
	
	private Organisation getOrganisation() {
		Organisation organisation = new Organisation();
		organisation.setOrganisationId("PeO108199");
		organisation.setName("Pearson Places");
		organisation.setCreatedBy("PP");
		organisation.setCreatedOn(String.valueOf(System.currentTimeMillis()));
		return organisation;
	}

}
