package com.pearson.sam.bridgeapi.constants;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.CLASSROOM_ID_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCT_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;

public final class ErrorMessageConstants {

	private ErrorMessageConstants() {
		throw new AssertionError();// restrict instantiation
	}

	public static final String ROLES_SHOULD_PRESENT = "Roles should be present!";
	public static final String USERNAME_SHOULD_PRESENT = "UserName should be present!";
	public static final String USERNAME_SHOULD_NOT_EMPTY = "UserName should not be Empty!";
	public static final String PRODUCTID_SHOULD_PRESENT = "Product identifier should be present!";
	public static final String PRODUCTIDS_SHOULD_PRESENT = "Product identifier list should not be empty!";
	public static final String PRODUCTID_SHOULD_CONTAIN = "Product identifier should contain alphanumric characters only!";
	public static final String PRODUCTNAME_SHOULD_PRESENT = "Product Name should be present!";
	public static final String PRODUCTNAME_SHOULD_NOT_EMPTY = "Product Name should not Empty!";
	public static final String PRODUCTNAME_SHOULD_CONTAIN = "Product Name must not contain special characters other than .'-";
	public static final String PRODUCTMODEL_SHOULD_PRESENT = "ProductModel should be present!";
	public static final String PRODUCTMODEL_SHOULD_NOT_EMPTY = "ProductModel should not Empty!";
	public static final String COURSEURL_SHOULD_PRESENT = "CourseUrl should be present!";
	public static final String COURSEURL_SHOULD_NOT_EMPTY = "CourseUrl should not Empty!";
	public static final String PRODUCTMODEL_SHOULD_CONTAIN = "ProductModel must not contain special characters other than .'-";
	public static final String EMAIL_SHOULD_PRESENT = "Email should be present!";
	public static final String EMAIL_SHOULD_NOT_EMPTY = "Email should not Empty!";
	public static final String PRODUCT_DOES_NOT_EXISTS = "Product doesn't exists";
	public static final String PRODUCTID_ALREADY_EXISTS = "ProductId already exists";
	public static final String PRODUCTNAME_SHOULD_NOT_BE_EMPTY = "Product name should not be empty";
	public static final String DATA_NOT_FOUND = "Data Not Found!";
	public static final String ROLE_NAME_SHOULD_NOT_BE_EMPTY = "Role name should not be empty";
	public static final String NO_PRODUCTS_HAS_BEEN_SUBSCRIBED_BY_THIS_USER = "No products has been subscribed by this user!";
	public static final String NOT_INCLUDING_SUBSCRIBED_PRODUCTS_CONDITION = "Not including subscribed products condition!";
	public static final String THIS_EMAIL_ALREADY_EXISTS = "This email already exists";
	public static final String USERNAME_ALREADY_EXISTS = "UserName already exists";
	public static final String UID_ALREADY_EXISTS = "This uid already exists";
	//public static final String USER_DOES_NOT_EXISTS = "USER_DOES_NOT_EXISTS";
	public static final String UID_SHOULD_PRESENT = "UID should be present!";
	public static final String SHOULD_NOT_BE_EMPTY = " should not be Empty or Null!";
	public static final String TIMESTAMP_INVALID = "Invalid Time Stamp for Field ";
	public static final String INVALID_SPECIAL_CHARACTERS_PRESENT = "Invalid Special Characters Present in field ";
	public static final String MANDATORY_FIELD_MISSING = "Mandatory Field is null or blank  ";
	public static final String JOB_AREADY_EXISTS = "Job Id Already exists";
	public static final String JOB_DOES_NOT_EXISTS = "Job Does Not exists";
	public static final String FIELD_NOT_NUMERIC = "Field should be Numeric ";
	public static final String PRODUCT_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "product should not contain special character or empty value!";
	public static final String CLASSROOM_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "classRoom should not contain special character or empty value!";
	public static final String SCHOOL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "school should not contain special character or empty value!!";
	public static final String SUBJECTPREFRENCES_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "subjectPreference should not contain special character or empty value!!";
	public static final String SUBJECT_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "subject should not contain special character or empty value!!";
	public static final String TYPE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "type should not contain special character or empty value!!";
	public static final String BUNDLE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "bundle should not contain special character or empty value!!";
	public static final String PROVIDE_VALID_EMAIL = "Please provide valid email!";
	public static final String PROVIDE_VALID_USERNAME = "Please provide valid userName!";
	public static final String PROVIDE_VALID_FULLNAME = "Please provide valid fullName!";
	public static final String VOUCHER_CODE_SHOULD_NOT_EMPTY = "Voucher Code is should not Empty!";
	
	/*VOUCHER*/
	public static final String VOUCHER_CODE_DOES_NOT_EXISTS = "Voucher Code doesn't exists";
	public static final String QUANTITY_CANNOT_BE_LESSTHAN = "quantity Cannot be less than";
	public static final String TOTATLREACTIVATIONS_CANNOT_BE_LESSTHAN = "totalReactivations Cannot be less than";
	public static final String TOTATLREACTIVATIONS_MUST_BE_LESSTHAN = "totalReactivations must be less than";
	
	/*ACCESS CODES*/
	public static final String ACCESS_CODE_SHOULD_NOT_EMPTY = "Access Code is should not Empty!";
	public static final String ACCESS_CODE_TYPE_SHOULD_NOT_EMPTY = "Type is should not Empty!";
	public static final String ACCESS_CODE_DOES_NOT_EXISTS = "Access Code doesn't exists";
	public static final String ACCESS_CODE_TYPES = "Access Code types are allowed:";
	public static final String ACCESS_CODE_QUANTITY_MIN ="Quantity should not be less than 1";
	public static final String ACCESS_CODE_QUANTITY_MAX ="Quantity should not be more than 24";
	public static final String ACCESS_CODE_TOTAL_REACTIVATIONS_MIN="Total Reactivations should not be less than ";
	public static final String ACCESS_CODE_TOTAL_REACTIVATIONS_MAX="Total Reactivations should not be more than ";
	public static final String ENTER_VALID_START_DATE = "Enter valid start date";
	public static final String ENTER_VALID_END_DATE = "Enter valid end date";
	public static final String ENTER_VALID_SUBSCRIPTION_DATE = "Enter valid subscription date";
	public static final String START_DATE_SHOULD_NOT_GREATER_THAN_END_DATE = "Start date should not greater than end date";

	/*USER*/
	public static final String USER_DOES_NOT_EXISTS = "User doesn't exists";
	public static final String PLEASE_PROVIDE_STATUS_CODE = "Please provide status code!";
	public static final String INSERT_VALID_STATUS_CODE = "Insert valid status code!";
	public static final String NO_SCHOOL_ASSOCIATED_WITH_TEACHER = "No Schools associated with this teacher!";
	public static final String ENTER_VALID_DATECREATED = "Enter Valid dateCreated";
	public static final String ENTER_VALID_DATEMODIFIED = "Enter Valid dateModified";
	
	/*ORGANISATION*/
	public static final String ORGANISATION_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "organisationId should not contain special character!";
	public static final String PLEASE_PROVIDE_VALID_NAME = "Please provide valid name!";
	public static final String ENTER_VALID_CREATEDON = "Enter Valid createdOn";
	public static final String ENTER_VALID_UPDATEDON = "Enter Valid updatedOn";
	public static final String NAME_SHOULD_BE_PRESENT = "Name should be present!";
	public static final String CAN_NOT_PASS_UPDATEDBY_UPDATEDON_WHILE_CREATE_ORGANISATION = "You can't passed updatedBy,updatedOn while createOrganisation!";
	public static final String CAN_NOT_PASS_CREATEDBY_CREATEDON_WHILE_UPDATE_ORGANISATION = "You can't passed createdBy,createdOn while updateOrganisation!";
	
	/*LICENCE*/
	public static final String ENTER_VALID_CREATION_DATE = "Enter Valid creationDate!";
	public static final String ENTER_VALID_DATE_CREATED = "Enter Valid dateCreated";
	public static final String ENTER_VALID_LAST_ACTIVATED_DATE = "Enter Valid lastActivatedDate";
	public static final String UNABLE_TO_FIND_THE_LICENCE_ID = "Unable to find the Licence Id:";
	
	/*LICENCEHOLDER*/
	public static final String ENTER_VALID_CREATED_DATE = "Enter Valid createdDate!";
	public static final String QUANTITY_SHOULD_CONTAIN_ONLY_NUMBER = "quantity should contain only number!";
	public static final String SCHOOLID_NOT_FOUND_TO_UPDATE_LICENCEHOLDER = "schoolId not found to update licenceHolder!";
	public static final String PLEASE_PROVIDE_VALID_SCHOOLID = "Please provide the valid schoolId!";
	public static final String ORGANISATIONID_NOT_FOUND_TO_UPDATE_LICENCEHOLDER = "organisationId not found to update licenceHolder!";
	public static final String PLEASE_PROVIDE_VALID_ORGANISATIONID = "Please provide the valid OrganisationId!";
	public static final String LICENCEHOLDER_NOT_FOUND = "licenceHolder not found!";
	public static final String PLEASE_PROVIDE_VALID_LICENCEHOLDERID = "Please provide the LicenceHolderId!";
	public static final String PLEASE_PROVIDE_VALID_PRODUCTID = "Please provide the valid productId!";
	public static final String NO_SCHOOL_ARE_ASSOCIATED_WITH_ANY_LICENCE_HOLDER = "No Schools are associated with any Licence Holder!";
	public static final String SCHOOL_IS_NOT_ASSOCIATED_WITH_USER = "School is not associated with User!";
	
	/*LICENCE HOLDER HISTORY*/
	public static final String LICENCEHOLDERID_NOT_FOUND_TO_UPDATE_LICENCEHOLDERHISTORY = "licenceHolderId not found to update licenceHolderHistory!";
	public static final String PLEASE_PROVIDE_SCHOOLID = "Please provide schoolId to add school!";
	public static final String PLEASE_PROVIDE_ORGANISATIONID = "Please provide organisationId to add organisation!";
	public static final String PLEASE_PROVIDE_PRODUCTID = "Please provide productId to add product!";
	public static final String PLEASE_PROVIDE_LICENCEHOLDERID = "Please provide LicenceHolderId to add licenceHolder!";
	public static final String LICENCEHOLDERHISTORYID_NOT_FOUND_TO_UPDATE_LICENCEHOLDERHISTORY = "licenceHolderHistroyId not found to update licenceHolderHistory!";
	public static final String ENTER_VALID_ALTER_DATE = "Enter Valid alterDate!";
	
	
	/* SCHOOL */
	public static final String SCHOOL_NAME_SHOULD_NOT_BE_EMPTY = "School name should not be empty";
	public static final String SCHOOL_NAME_SHOULD_NOT_ALLOW_SPECIAL_CHARACTERS = "School name should not allow special characters";
	public static final String SCHOOL_ID_ALREADY_EXISTED = "School ID Already Existed";
	public static final String SCHOOL_ID_SHOULD_NOT_BE_EMPTY = "School ID should not be empty";
	public static final String SCHOOL_IDS_LIST_SHOULD_NOT_BE_EMPTY = "School IDs list should not be empty";

	 
	/*CLASSROOM*/
	public static final String CLASSROOM_ID_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "classroomId should not contain special character!";
	public static final String YEAR_LEVEL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "yearLevel should not contain special character or empty value!";
	public static final String VALIDATION_CODE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "validationCode should not contain special character!";
	public static final String VALIDATION_CODE_SHOULD_NOT_BE_EMPTY = "validationCode should not be Empty or Null!";
	public static final String VALIDATION_CODE_SHOULD_BE_UNIQUE = "validationCode should be unique!";
	public static final String PROVIDE_VALID_NAME = "Please provide valid name!";
	public static final String GROUP_TYPE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER = "groupType should not contain special character!";
}
