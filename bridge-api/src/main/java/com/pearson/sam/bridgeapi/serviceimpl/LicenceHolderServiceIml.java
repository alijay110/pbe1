package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.LICENCEHOLDER_NOT_FOUND;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.NO_SCHOOL_ARE_ASSOCIATED_WITH_ANY_LICENCE_HOLDER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ORGANISATIONID_NOT_FOUND_TO_UPDATE_LICENCEHOLDER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_VALID_LICENCEHOLDERID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_VALID_ORGANISATIONID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_VALID_PRODUCTID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_VALID_SCHOOLID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOLID_NOT_FOUND_TO_UPDATE_LICENCEHOLDER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_IS_NOT_ASSOCIATED_WITH_USER;
import static com.pearson.sam.bridgeapi.constants.ValidatorConstants.TIME_ZONE;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.ILicenceHolderService;
import com.pearson.sam.bridgeapi.model.ChannelPartner;
import com.pearson.sam.bridgeapi.model.Holder;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.LicenceHolderRespository;
import com.pearson.sam.bridgeapi.repository.LicenceRespository;
import com.pearson.sam.bridgeapi.repository.OrganisationRepository;
import com.pearson.sam.bridgeapi.repository.ProductRepository;
import com.pearson.sam.bridgeapi.repository.SchoolRepository;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;
import graphql.GraphQLException;

@Service
public class LicenceHolderServiceIml implements ILicenceHolderService {

	  private static final Logger logger = LoggerFactory.getLogger(LicenceHolderServiceIml.class);
	  private static final String SCHOOL = "SCHOOL";
	  private static final String ORGANISATION = "ORGANISATION";
	  private static final String LICENCEHOLDER = "LICENCEHOLDER";

	  @Autowired
	  LicenceHolderRespository licenceHolderRespository;
	  
	  @Autowired
	  MongoOperations mongo;
	  
	  @Autowired
	  protected ISessionFacade sessionFacade;
	  
	  @Autowired
	  public LicenceRespository licenceRespository;
	  
	  @Autowired
	  public SchoolRepository schoolRepository;
	  
	  @Autowired
	  public OrganisationRepository organisationRepository;
	  
	  @Autowired
	  public ProductRepository productRepository;
	  
	@Override
	public LicenceHolder create(LicenceHolder licenceHolder) {
		// TODO Auto-generated method stub
		   //logger.info("Saving Licence holder {}", licenceHolder);
		    licenceHolder.setCreatedDate(!Utils.isEmpty(licenceHolder.getCreatedDate()) ? licenceHolder.getCreatedDate() : Utils.generateEpochDate());
			if(null!=licenceHolder.getHolder()) {
				validateHoler(licenceHolder);
			}
		    LicenceHolder holder = licenceHolderRespository.save(licenceHolder);
		    return holder;
	}

	@Override
	public LicenceHolder update(LicenceHolder licenceHolder, String operation) {
		//logger.info("Updating Licence holder {}", licenceHolder);
		LicenceHolder holder = null;
		LicenceHolder dbLicenceHolder = null;
		
		switch (operation) {
		case LICENCEHOLDER:
			dbLicenceHolder = validateLicenceHolderId(licenceHolder, dbLicenceHolder);
			break;
		case ORGANISATION:
			dbLicenceHolder = validateHolderOrganisationOrganisationId(licenceHolder, dbLicenceHolder);
			break;
		case SCHOOL:
			dbLicenceHolder = validateHolderSchoolSchoolId(licenceHolder, dbLicenceHolder);
			break;
		default:
		}
		if (null != licenceHolder) {

			if (null != licenceHolder.getHolder().getOrganisation()) {
				validateDbLicenceHolder(dbLicenceHolder);
				Utils.copyNonNullProperties(licenceHolder.getHolder().getOrganisation(),
						dbLicenceHolder.getHolder().getOrganisation());
			}
			if (null != licenceHolder.getHolder().getProduct()) {
				validateDbLicenceHolder(dbLicenceHolder);
				Utils.copyNonNullProperties(licenceHolder.getHolder().getProduct(),
						dbLicenceHolder.getHolder().getProduct());
			}
			if (null != licenceHolder.getHolder().getSchool()) {
				validateDbLicenceHolder(dbLicenceHolder);
				Utils.copyNonNullProperties(licenceHolder.getHolder().getSchool(),
						dbLicenceHolder.getHolder().getSchool());
			}
			if (null != licenceHolder.getHolder()) {
				Holder tempDBHolder =  new Holder();
				tempDBHolder.setQuantity(null!=licenceHolder.getHolder().getQuantity() ? licenceHolder.getHolder().getQuantity():null);
				tempDBHolder.setTeacherAccess(null!=licenceHolder.getHolder().getTeacherAccess() ? licenceHolder.getHolder().getTeacherAccess():false);
				tempDBHolder.setTotalLicencesInUse(null!=licenceHolder.getHolder().getTotalLicencesInUse() ? licenceHolder.getHolder().getTotalLicencesInUse():null);
				tempDBHolder.setSchoolProductType(null!=licenceHolder.getHolder().getSchoolProductType() ? licenceHolder.getHolder().getSchoolProductType():null);
				Utils.copyNonNullProperties(tempDBHolder, dbLicenceHolder.getHolder());
			}
			if(null!=licenceHolder) {
				LicenceHolder tempLicenceHolder = new LicenceHolder();
				tempLicenceHolder.setCreatedDate(!Utils.isEmpty(licenceHolder.getCreatedDate()) ? licenceHolder.getCreatedDate() : Utils.generateEpochDate());
				tempLicenceHolder.setCreatedBy(sessionFacade.getLoggedInUser(true).getUid());
				Utils.copyNonNullProperties(tempLicenceHolder, dbLicenceHolder);
			}
			holder = licenceHolderRespository.save(dbLicenceHolder);
		}
		return holder;
		
	}
	
	private LicenceHolder validateDbLicenceHolder(LicenceHolder dbLicenceHolder) {
		Holder holder = null;
		if(null == dbLicenceHolder.getHolder()) {
			holder = new Holder();
			dbLicenceHolder.setHolder(holder);
			dbLicenceHolder.getHolder().setOrganisation(new ChannelPartner());
			dbLicenceHolder.getHolder().setProduct(new Product());
			dbLicenceHolder.getHolder().setSchool(new School());
		}else {
			if(null == dbLicenceHolder.getHolder().getOrganisation()) {
				dbLicenceHolder.getHolder().setOrganisation(new ChannelPartner());
			}
			if(null == dbLicenceHolder.getHolder().getProduct()) {
				dbLicenceHolder.getHolder().setProduct(new Product());
			}
			if(null == dbLicenceHolder.getHolder().getSchool()) {
				dbLicenceHolder.getHolder().setSchool(new School());
			}
		}
		return dbLicenceHolder;
	}

	private LicenceHolder validateHolderSchoolSchoolId(LicenceHolder licenceHolder, LicenceHolder dbLicenceHolder) {
		if (null != licenceHolder.getHolder().getSchool().getSchoolId()) {
			dbLicenceHolder = licenceHolderRespository
					.findByHolderSchoolSchoolId(licenceHolder.getHolder().getSchool().getSchoolId());
			if (null == dbLicenceHolder) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError,
						SCHOOLID_NOT_FOUND_TO_UPDATE_LICENCEHOLDER);
			}
			validateHoler(licenceHolder);
		}else {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError,
					PLEASE_PROVIDE_VALID_SCHOOLID);
		}
		return dbLicenceHolder;
	}

	private LicenceHolder validateHolderOrganisationOrganisationId(LicenceHolder licenceHolder,
			LicenceHolder dbLicenceHolder) {
		if (null != licenceHolder.getHolder().getOrganisation().getOrganisationId()) {
			dbLicenceHolder = licenceHolderRespository.findByHolderOrganisationOrganisationId(
					licenceHolder.getHolder().getOrganisation().getOrganisationId());
			if (null == dbLicenceHolder) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError,
						ORGANISATIONID_NOT_FOUND_TO_UPDATE_LICENCEHOLDER);
			}
			validateHoler(licenceHolder);
		}else {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError,
					PLEASE_PROVIDE_VALID_ORGANISATIONID);
		}
		return dbLicenceHolder;
	}

	private LicenceHolder validateLicenceHolderId(LicenceHolder licenceHolder, LicenceHolder dbLicenceHolder) {
		if (null != licenceHolder.getLicenceHolderId()) {
			dbLicenceHolder = licenceHolderRespository.findByLicenceHolderId(licenceHolder.getLicenceHolderId());
			if (null == dbLicenceHolder) {
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, LICENCEHOLDER_NOT_FOUND);
			}
			validateHoler(licenceHolder);
		}else {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError,
					PLEASE_PROVIDE_VALID_LICENCEHOLDERID);
		}
		return dbLicenceHolder;
	}

	private void validateHoler(LicenceHolder licenceHolder) {
		if (null != licenceHolder.getHolder().getSchool()) {
			if (null != licenceHolder.getHolder().getSchool().getSchoolId()) {
				if (!schoolRepository.existsBySchoolId((licenceHolder.getHolder().getSchool().getSchoolId()))) {
					throw new BridgeApiGraphqlException(ErrorType.ValidationError, PLEASE_PROVIDE_VALID_SCHOOLID);
				}
			}
		}
		if (null != licenceHolder.getHolder().getOrganisation()) {
			if (null != licenceHolder.getHolder().getOrganisation().getOrganisationId()) {
				if (!organisationRepository
						.existsByOrganisationId(licenceHolder.getHolder().getOrganisation().getOrganisationId())) {
					throw new BridgeApiGraphqlException(ErrorType.ValidationError,
							PLEASE_PROVIDE_VALID_ORGANISATIONID);
				}
			}
		}
		if (null != licenceHolder.getHolder().getProduct()) {
			if (null != licenceHolder.getHolder().getProduct().getProductId()) {
				if (!productRepository.existsByProductId(licenceHolder.getHolder().getProduct().getProductId())) {
					throw new BridgeApiGraphqlException(ErrorType.ValidationError, PLEASE_PROVIDE_VALID_PRODUCTID);
				}
			}
		}
	}

	@Override
	public LicenceHolder fetch(LicenceHolder licenceHolder) {
		// TODO Auto-generated method stub
		//logger.info("Fetch One Licence holder {}", licenceHolder);
	    licenceHolder = licenceHolderRespository
	        .findByLicenceHolderId(licenceHolder.getLicenceHolderId());
	    return licenceHolder;
	}

	@Override
	public List<LicenceHolder> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LicenceHolder delete(LicenceHolder licenceHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<LicenceHolder> pageIt(Pageable pageable, Example<LicenceHolder> e) {
		User user = getUser(true);
		List<String> schoolIdsList = user.getSchool();
		Set<String> roles = user.getRoles();
		//logger.info("logged user school list:"+schoolIdsList);
		//logger.info("logged user roles:"+roles);
		boolean isCusomerCareRoleExisted = roles.contains("customer-care");
		if (!isCusomerCareRoleExisted && CollectionUtils.isEmpty(schoolIdsList)) {
			throw new GraphQLException(NO_SCHOOL_ARE_ASSOCIATED_WITH_ANY_LICENCE_HOLDER);
		}
		if(!isCusomerCareRoleExisted && !isSchoolIdsAssociatedWithLoggedUser(schoolIdsList, e)){
			throw new GraphQLException(SCHOOL_IS_NOT_ASSOCIATED_WITH_USER);			
		}
		Query query = new Query().with(pageable);
		query.addCriteria(Criteria.byExample(e));
		if(!isCusomerCareRoleExisted)
			query.addCriteria(Criteria.where("holder.school.schoolId").in(schoolIdsList));
		Page<LicenceHolder> page = PageableExecutionUtils.getPage(mongo.find(query, LicenceHolder.class), pageable,
				() -> mongo.count(query, LicenceHolder.class));
		// Used Licenses Count by School Id and Product.
		activeLicenseCountBySchoolIdAndProduct(page.getContent());
		return page;
	}
	private void activeLicenseCountBySchoolIdAndProduct(List<LicenceHolder> licenceHolders) {
		Map<String,Map<String,Integer>> schoolProductLicencesCount = new HashMap<String,Map<String,Integer>>();
		for(LicenceHolder licenceHolder:licenceHolders) {
			if(licenceHolder.getHolder()!=null && licenceHolder.getHolder().getSchool()!=null && licenceHolder.getHolder().getProduct()!=null) {
				List<Licence> lics = licenceRespository.findByAttachedSchool(licenceHolder.getHolder().getSchool().getSchoolId());
				for(Licence licence : lics){
					if((licence.getProducts()!=null && licence.getProducts().contains(licenceHolder.getHolder().getProduct().getProductId())) && !isExpired(licence.getExpirationDate())){
						if(schoolProductLicencesCount.get(licenceHolder.getHolder().getSchool().getSchoolId())==null){
							Map<String,Integer> prouctLiencesCount = new HashMap<String,Integer>();
							prouctLiencesCount.put(licenceHolder.getHolder().getProduct().getProductId(), 1);
							schoolProductLicencesCount.put(licenceHolder.getHolder().getSchool().getSchoolId(), prouctLiencesCount);
						}else {
							Map<String,Integer> prouctLiencesCount = schoolProductLicencesCount.get(licenceHolder.getHolder().getSchool().getSchoolId());
							if(prouctLiencesCount.containsKey(licenceHolder.getHolder().getProduct().getProductId())){
								prouctLiencesCount.put(licenceHolder.getHolder().getProduct().getProductId(), prouctLiencesCount.get(licenceHolder.getHolder().getProduct().getProductId())+1);
							}else {
								prouctLiencesCount.put(licenceHolder.getHolder().getProduct().getProductId(),1);
							}
						}
					}
				}
			}
			String schoolId = (licenceHolder.getHolder()!=null && licenceHolder.getHolder().getSchool()!=null)?licenceHolder.getHolder().getSchool().getSchoolId():"";
			String productId = (licenceHolder.getHolder()!=null && licenceHolder.getHolder().getProduct()!=null)?licenceHolder.getHolder().getProduct().getProductId():"";
			//logger.info("School Id:"+schoolId+" Product Id:"+productId);
			Map<String,Integer> prouctLiencesCount = schoolProductLicencesCount.get(schoolId);
			Integer usedLicencesCount = 0;
			if(prouctLiencesCount!=null)
				usedLicencesCount = prouctLiencesCount.get(productId)==null?0:prouctLiencesCount.get(productId);
			//logger.info("School Id:"+schoolId+" Product Id:"+productId+" count:"+usedLicencesCount);
			if(licenceHolder.getHolder()!=null)
				licenceHolder.getHolder().setTotalLicencesInUse(usedLicencesCount);
		}
	}
	
	private SessionUser getUser(boolean getLoggedInUser) {
		return sessionFacade.getLoggedInUser(getLoggedInUser);
	}
	
	private boolean isExpired(String expiryDate) {
		DateTime dt = new DateTime(DateTimeZone.forID(TIME_ZONE));
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeInMillis(dt.getMillis());
		Calendar licenceDate = Calendar.getInstance();
		licenceDate.setTimeInMillis(Long.parseLong(expiryDate));

		if (currentDate.compareTo(licenceDate) > 0) {
			return true;
		} else if (currentDate.compareTo(licenceDate) < 0) {
			return false;
		}
		return false;
	}
	
	private boolean isSchoolIdsAssociatedWithLoggedUser(List<String> schoolIdsList,Example<LicenceHolder> e) {
		boolean isSchoolNotAssociated = true;
		if((e.getProbe().getHolder()!=null && e.getProbe().getHolder().getSchool()!=null) && (Utils.isNotEmpty(e.getProbe().getHolder().getSchool().getSchoolId())) ) {
			isSchoolNotAssociated = schoolIdsList.contains(e.getProbe().getHolder().getSchool().getSchoolId());
		}
		return isSchoolNotAssociated;
	}

}
