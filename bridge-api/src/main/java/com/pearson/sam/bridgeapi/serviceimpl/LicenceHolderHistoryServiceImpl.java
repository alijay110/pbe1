package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.LICENCEHOLDERHISTORYID_NOT_FOUND_TO_UPDATE_LICENCEHOLDERHISTORY;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.LICENCEHOLDERID_NOT_FOUND_TO_UPDATE_LICENCEHOLDERHISTORY;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_LICENCEHOLDERID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_ORGANISATIONID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_PRODUCTID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_SCHOOLID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_VALID_ORGANISATIONID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_VALID_PRODUCTID;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_VALID_SCHOOLID;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.iservice.ILicenceHolderHistoryService;
import com.pearson.sam.bridgeapi.model.ChannelPartner;
import com.pearson.sam.bridgeapi.model.Holder;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.LicenceHolderHistory;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.School;
import com.pearson.sam.bridgeapi.repository.LicenceHolderHistoryRespository;
import com.pearson.sam.bridgeapi.repository.LicenceHolderRespository;
import com.pearson.sam.bridgeapi.repository.LicenceRespository;
import com.pearson.sam.bridgeapi.repository.OrganisationRepository;
import com.pearson.sam.bridgeapi.repository.ProductRepository;
import com.pearson.sam.bridgeapi.repository.SchoolRepository;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ErrorType;

@Service
public class LicenceHolderHistoryServiceImpl implements ILicenceHolderHistoryService {
	private static final Logger logger = LoggerFactory.getLogger(LicenceHolderHistoryServiceImpl.class);
	private static final String CREATE = "CREATE";
	private static final String UPDATE = "UPDATE";

	@Autowired
	LicenceHolderHistoryRespository licenceHolderHistoryRespository;
	
	@Autowired
	public LicenceRespository licenceRespository;

	@Autowired
	public SchoolRepository schoolRepository;

	@Autowired
	public OrganisationRepository organisationRepository;

	@Autowired
	public ProductRepository productRepository;
	
	@Autowired
	public LicenceHolderRespository licenceHolderRespository;
	
	@Autowired
	private ISessionFacade sessionFacade;

	@Override
	public LicenceHolderHistory create(LicenceHolderHistory licenceHolderHistory) {
		// TODO Auto-generated method stub
		//logger.info("Saving Licence holder History {}", licenceHolderHistory);
		String licenceHolderHistoryId = null;
		if(null!=licenceHolderHistory.getAlteration()) {
			validateLicenceHolder(licenceHolderHistory,CREATE);
		}
		LicenceHolderHistory licenceHolderHistory1 = licenceHolderHistoryRespository.findTopByOrderByIdDesc();

		if (null != licenceHolderHistory1 && null != licenceHolderHistory1.getLicenceHolderHistoryId()
				&& licenceHolderHistory1.getLicenceHolderHistoryId().contains("PP2LICENCEHOLDERHISTORY")) {
			String[] numbers = licenceHolderHistory1.getLicenceHolderHistoryId().split("Y");
			int id = Integer.parseInt(numbers[1]) + 1;
			licenceHolderHistoryId = "PP2LICENCEHOLDERHISTORY" + id;
		} else {
			licenceHolderHistoryId = "PP2LICENCEHOLDERHISTORY00";
		}
		licenceHolderHistory.setLicenceHolderHistoryId(licenceHolderHistoryId);
		licenceHolderHistory
		.setAlterDate(Utils.isNotEmpty(licenceHolderHistory.getAlterDate()) ? licenceHolderHistory.getAlterDate()
				: Utils.generateEpochDate());
		licenceHolderHistory.setAlteredBy(sessionFacade.getLoggedInUser(true).getUid());
		LicenceHolderHistory holderHistory = licenceHolderHistoryRespository.save(licenceHolderHistory);
		return holderHistory;
	}
	
	@Override
	public LicenceHolderHistory update(LicenceHolderHistory licenceHolderHistory) {
		// TODO Auto-generated method stub
		//logger.info("Updating Licence holder History {}", licenceHolderHistory);
		LicenceHolderHistory holderHistory = null;
		LicenceHolderHistory dbLicenceHolderHistory = null;
		dbLicenceHolderHistory = licenceHolderHistoryRespository
				.findByLicenceHolderHistoryId(licenceHolderHistory.getLicenceHolderHistoryId());
		if (null != dbLicenceHolderHistory) {
			if(null!=licenceHolderHistory.getAlteration()) {
				validateLicenceHolder(licenceHolderHistory,UPDATE);
			}
			dbLicenceHolderHistory  = updateLicenceHolderHistoryData(licenceHolderHistory,dbLicenceHolderHistory);
			holderHistory = licenceHolderHistoryRespository.save(dbLicenceHolderHistory);
		}else {
			throw new BridgeApiGraphqlException(ErrorType.ValidationError, LICENCEHOLDERHISTORYID_NOT_FOUND_TO_UPDATE_LICENCEHOLDERHISTORY);
		}
		return holderHistory;
	}
	
	private void validateLicenceHolder(LicenceHolderHistory licenceHolderHistory,String operation) {

		if(null != licenceHolderHistory.getAlteration()) {
			if (null != licenceHolderHistory.getAlteration().getLicenceHolderId()) {
				if (!licenceHolderRespository
						.existsByLicenceHolderId(licenceHolderHistory.getAlteration().getLicenceHolderId())) {
					throw new BridgeApiGraphqlException(ErrorType.ValidationError,
							LICENCEHOLDERID_NOT_FOUND_TO_UPDATE_LICENCEHOLDERHISTORY);
				}
			}else if(operation.equals(CREATE)){
				throw new BridgeApiGraphqlException(ErrorType.ValidationError, PLEASE_PROVIDE_LICENCEHOLDERID);
			}
		}
		if (null != licenceHolderHistory.getAlteration().getHolder()) {
			if (null != licenceHolderHistory.getAlteration().getHolder().getSchool()) {
				if (null != licenceHolderHistory.getAlteration().getHolder().getSchool().getSchoolId()) {
					if (!schoolRepository.existsBySchoolId(
							(licenceHolderHistory.getAlteration().getHolder().getSchool().getSchoolId()))) {
						throw new BridgeApiGraphqlException(ErrorType.ValidationError, PLEASE_PROVIDE_VALID_SCHOOLID);
					}
				} else if(operation.equals(CREATE)){
					throw new BridgeApiGraphqlException(ErrorType.ValidationError, PLEASE_PROVIDE_SCHOOLID);
				}
			}
			if (null != licenceHolderHistory.getAlteration().getHolder().getOrganisation()) {
				if (null != licenceHolderHistory.getAlteration().getHolder().getOrganisation().getOrganisationId()) {
					if (!organisationRepository.existsByOrganisationId(
							licenceHolderHistory.getAlteration().getHolder().getOrganisation().getOrganisationId())) {
						throw new BridgeApiGraphqlException(ErrorType.ValidationError,
								PLEASE_PROVIDE_VALID_ORGANISATIONID);
					}
				} else if(operation.equals(CREATE)){
					throw new BridgeApiGraphqlException(ErrorType.ValidationError, PLEASE_PROVIDE_ORGANISATIONID);
				}
			}
			if (null != licenceHolderHistory.getAlteration().getHolder().getProduct()) {
				if (null != licenceHolderHistory.getAlteration().getHolder().getProduct().getProductId()) {
					if (!productRepository.existsByProductId(
							licenceHolderHistory.getAlteration().getHolder().getProduct().getProductId())) {
						throw new BridgeApiGraphqlException(ErrorType.ValidationError, PLEASE_PROVIDE_VALID_PRODUCTID);
					}
				} else if(operation.equals(CREATE)){
					throw new BridgeApiGraphqlException(ErrorType.ValidationError, PLEASE_PROVIDE_PRODUCTID);
				}
			}
		}
	}
	
	
	private LicenceHolderHistory updateLicenceHolderHistoryData(LicenceHolderHistory licenceHolderHistory,
			LicenceHolderHistory dbLicenceHolderHistory) {
		licenceHolderHistory
				.setAlterDate(Utils.isNotEmpty(licenceHolderHistory.getAlterDate()) ? licenceHolderHistory.getAlterDate()
						: Utils.generateEpochDate());
		licenceHolderHistory.setAlteredBy(sessionFacade.getLoggedInUser(true).getUid());
		if (null != licenceHolderHistory) {
			if (null != licenceHolderHistory.getAlteration()) {
				licenceHolderHistory.getAlteration()
						.setCreatedDate(null != licenceHolderHistory.getAlteration().getCreatedDate()
								? licenceHolderHistory.getAlteration().getCreatedDate() : Utils.generateEpochDate());
				licenceHolderHistory.getAlteration().setCreatedBy(sessionFacade.getLoggedInUser(true).getUid());
				validateDbLicenceHolderHistoryData(dbLicenceHolderHistory);
				if (null != licenceHolderHistory.getAlteration().getHolder()) {
					if (null != licenceHolderHistory.getAlteration().getHolder().getOrganisation()) {
						Utils.copyNonNullProperties(licenceHolderHistory.getAlteration().getHolder().getOrganisation(),
								dbLicenceHolderHistory.getAlteration().getHolder().getOrganisation());
					}
					if (null != licenceHolderHistory.getAlteration().getHolder().getProduct()) {
						Utils.copyNonNullProperties(licenceHolderHistory.getAlteration().getHolder().getProduct(),
								dbLicenceHolderHistory.getAlteration().getHolder().getProduct());
					}
					if (null != licenceHolderHistory.getAlteration().getHolder().getSchool()) {
						Utils.copyNonNullProperties(licenceHolderHistory.getAlteration().getHolder().getSchool(),
								dbLicenceHolderHistory.getAlteration().getHolder().getSchool());
					}
					updateLicenceHolderHistoryRemainingData(licenceHolderHistory, dbLicenceHolderHistory);
				} else {
					if (null != licenceHolderHistory.getAlteration()) {
						Utils.copyNonNullProperties(licenceHolderHistory.getAlteration(),
								dbLicenceHolderHistory.getAlteration());
					}
				}
			} else {
				if (null != licenceHolderHistory) {
					Utils.copyNonNullProperties(licenceHolderHistory, dbLicenceHolderHistory);
				}
			}
		}
		return dbLicenceHolderHistory;
	}

	private void updateLicenceHolderHistoryRemainingData(LicenceHolderHistory licenceHolderHistory,
			LicenceHolderHistory dbLicenceHolderHistory) {
		if (null != licenceHolderHistory.getAlteration().getHolder()) {
			Holder tempDBHolder =  new Holder();
			tempDBHolder.setQuantity(null!=licenceHolderHistory.getAlteration().getHolder().getQuantity() ? licenceHolderHistory.getAlteration().getHolder().getQuantity():null);
			tempDBHolder.setTeacherAccess(null!=licenceHolderHistory.getAlteration().getHolder().getTeacherAccess() ? licenceHolderHistory.getAlteration().getHolder().getTeacherAccess():false);
			tempDBHolder.setTotalLicencesInUse(null!=licenceHolderHistory.getAlteration().getHolder().getTotalLicencesInUse() ? licenceHolderHistory.getAlteration().getHolder().getTotalLicencesInUse():null);
			tempDBHolder.setSchoolProductType(null!=licenceHolderHistory.getAlteration().getHolder().getSchoolProductType() ? licenceHolderHistory.getAlteration().getHolder().getSchoolProductType():null);
			Utils.copyNonNullProperties(tempDBHolder, dbLicenceHolderHistory.getAlteration().getHolder());
		}
		if (null != licenceHolderHistory.getAlteration()) {
			LicenceHolder tempLicenceHolder = new LicenceHolder();
			tempLicenceHolder.setLicenceHolderId(null!=licenceHolderHistory.getAlteration().getLicenceHolderId() ?licenceHolderHistory.getAlteration().getLicenceHolderId():null);
			tempLicenceHolder.setCreatedDate(licenceHolderHistory.getAlteration().getCreatedDate());
			tempLicenceHolder.setCreatedBy(licenceHolderHistory.getAlteration().getCreatedBy());
			Utils.copyNonNullProperties(tempLicenceHolder,dbLicenceHolderHistory.getAlteration());
		}
		if (null != licenceHolderHistory) {
			LicenceHolderHistory tempLicenceHolderHistory = new LicenceHolderHistory();
			tempLicenceHolderHistory.setAlterDate(licenceHolderHistory.getAlterDate());
			tempLicenceHolderHistory.setAlteredBy(licenceHolderHistory.getAlteredBy());
			Utils.copyNonNullProperties( tempLicenceHolderHistory,dbLicenceHolderHistory);
		}
	}
	
	private LicenceHolderHistory validateDbLicenceHolderHistoryData(LicenceHolderHistory dbLicenceHolderHistory) {
		if(null == dbLicenceHolderHistory.getAlteration()) {
			dbLicenceHolderHistory.setAlteration(new LicenceHolder());
			dbLicenceHolderHistory.getAlteration().setHolder(new Holder());
			dbLicenceHolderHistory.getAlteration().getHolder().setOrganisation(new ChannelPartner());
			dbLicenceHolderHistory.getAlteration().getHolder().setProduct(new Product());
			dbLicenceHolderHistory.getAlteration().getHolder().setSchool(new School());
		}else {
			if(null == dbLicenceHolderHistory.getAlteration().getHolder()) {
				dbLicenceHolderHistory.getAlteration().setHolder(new Holder());
			}else {
				if(null == dbLicenceHolderHistory.getAlteration().getHolder().getOrganisation()) {
					dbLicenceHolderHistory.getAlteration().getHolder().setOrganisation(new ChannelPartner());
				}
				if(null == dbLicenceHolderHistory.getAlteration().getHolder().getProduct()) {
					dbLicenceHolderHistory.getAlteration().getHolder().setProduct(new Product());
				}
				if(null == dbLicenceHolderHistory.getAlteration().getHolder().getSchool()) {
					dbLicenceHolderHistory.getAlteration().getHolder().setSchool(new School());
				}
			}
		}
		return dbLicenceHolderHistory;
	}

	@Override
	public LicenceHolderHistory fetch(LicenceHolderHistory licenceHolderHistory) {
		// TODO Auto-generated method stub
		//logger.info("Fetch One Licence holder History {}", licenceHolderHistory);
		licenceHolderHistory = licenceHolderHistoryRespository
				.findByLicenceHolderHistoryId(licenceHolderHistory.getLicenceHolderHistoryId());
		return licenceHolderHistory;
	}

	@Override
	public List<LicenceHolderHistory> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LicenceHolderHistory delete(LicenceHolderHistory licenceHolderHistory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<LicenceHolderHistory> pageIt(Pageable pageable, Example<LicenceHolderHistory> e) {
		// TODO Auto-generated method stub
		return licenceHolderHistoryRespository.findAll(e, pageable);
	}

}
