package com.pearson.sam.bridgeapi.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.CLASSROOM_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_DATECREATED;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_DATEMODIFIED;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCT_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PROVIDE_VALID_EMAIL;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PROVIDE_VALID_FULLNAME;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PROVIDE_VALID_USERNAME;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ROLES_SHOULD_PRESENT;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SUBJECTPREFRENCES_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.USERNAME_SHOULD_NOT_EMPTY;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.USERNAME_SHOULD_PRESENT;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.validators.ValidateModelCollection;

import graphql.GraphQLException;
import io.leangen.graphql.annotations.GraphQLIgnore;

@Document(collection = "user")
public class User extends BaseModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -7010888538125520105L;

	public User() {

	}

	public User(String uid) {
		this.uid = uid;
	}

	/**
	 * Clone of input obj.
	 * 
	 * @param user u
	 */
	public User(User user) {
		super();
		if (user == null) {
			throw new GraphQLException("User details not found!");
		}

		this.uid = user.getUid();
		this.fullName = user.getFullName();
		this.roles = user.getRoles();
		this.avatar = user.getAvatar();
		this.tandc = user.getTandc();
		this.email = user.getEmail();
		this.userName = user.getUserName();
		this.preferredName = user.getPreferredName();
		this.passwd = user.getPasswd();
		this.passwdValidation = user.getPasswdValidation();
		this.year = user.getYear();
		this.product = user.getProduct();
		this.classroom = user.getClassroom();
		this.school = user.getSchool();
		this.organisation = user.getOrganisation();
		this.status = user.getStatus();
		this.portal = user.getPortal();
		this.sso = user.getSso();
		this.lastLogin = user.getLastLogin();
		this.dateCreated = user.getDateCreated();
		this.subjectPreference = user.getSubjectPreference();
		this.message = user.getMessage();
		this.countryCode = user.getCountryCode();
		this.pearsonCreatedBy = user.getPearsonCreatedBy();
		this.dateModified = user.getDateModified();
		this.userStatus = user.getUserStatus();
		this.identifier = user.getIdentifier();
		this.settings = user.getSettings();
	}

	 public User(UserSearch userSearch) {
	    super();
	    if (userSearch== null) {
	      throw new GraphQLException("user Search details not found!");
	    }

	    this.uid = userSearch.getUid();
	    this.fullName = userSearch.getFullName();
	    this.roles = userSearch.getRoles();
	    this.avatar = userSearch.getAvatar();
	    this.email = userSearch.getEmail();
	    this.userName = userSearch.getUserName();
	    this.year = userSearch.getYear();
	    this.product = userSearch.getProduct();
	    this.classroom = userSearch.getClassroom();
	    this.school = userSearch.getSchool();
	    this.organisation = userSearch.getOrganisation();
	    this.status = userSearch.getStatus();
	    this.dateCreated = userSearch.getDateCreated();
	    this.dateModified = userSearch.getDateModified();
	  }

	 
	/**
	 * replaceWith.
	 * 
	 * @param user u
	 */
	public void replaceWith(User user) {
		this.uid = user.getUid();
		this.fullName = user.getFullName();
		this.roles = user.getRoles();
		this.avatar = user.getAvatar();
		this.tandc = user.getTandc();
		this.email = user.getEmail();
		this.userName = user.getUserName();
		this.preferredName = user.getPreferredName();
		this.passwd = user.getPasswd();
		this.passwdValidation = user.getPasswdValidation();
		this.year = user.getYear();
		this.product = user.getProduct();
		this.classroom = user.getClassroom();
		this.school = user.getSchool();
		this.organisation = user.getOrganisation();
		this.status = user.getStatus();
		this.portal = user.getPortal();
		this.sso = user.getSso();
		this.lastLogin = user.getLastLogin();
		this.dateCreated = user.getDateCreated();
		this.subjectPreference = user.getSubjectPreference();
		this.message = user.getMessage();
		this.countryCode = user.getCountryCode();
		this.pearsonCreatedBy = user.getPearsonCreatedBy();
		this.dateModified = user.getDateModified();
		this.userStatus = user.getUserStatus();
		this.identifier = user.getIdentifier();
		this.settings = user.getSettings();
	}

	@Id
	private String id;

	@MongoSource
	private String uid;
	@SamSource
	@Pattern(regexp = "^[a-zA-Z+'_ ]*$", message = PROVIDE_VALID_FULLNAME)
	private String fullName;
	@SamSource
	@NotNull(message = ROLES_SHOULD_PRESENT)
	private Set<String> roles;
	@MongoSource
	private String avatar;
	@SamSource
	private TandC tandc;
	@MongoSource
	@SamSource
	@Pattern(regexp = "[^.\\s][A-Za-z0-9+._-]+@(.+)[^.\\s]$", message = PROVIDE_VALID_EMAIL)
	private String email;
	@SamSource
	@NotBlank(message = USERNAME_SHOULD_NOT_EMPTY)
	@NotNull(message = USERNAME_SHOULD_PRESENT)
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9@._-]*$", message = PROVIDE_VALID_USERNAME)
	private String userName;
	@MongoSource
	private String preferredName;
	@SamSource
	private String passwd;
	@MongoSource
	private String passwdValidation;
	@MongoSource
	private String year;
	@MongoSource
	@ValidateModelCollection(message = PRODUCT_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private Set<String> product;
	@MongoSource
	@ValidateModelCollection(message = CLASSROOM_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private List<String> classroom;
	@MongoSource
	@ValidateModelCollection(message = SCHOOL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private List<String> school;
	@MongoSource
	private String organisation;
	@SamSource
	private String status;
	@MongoSource
	private String portal;
	@MongoSource
	private Boolean sso;
	@MongoSource
	private String lastLogin;
	@MongoSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_DATECREATED)
	private String dateCreated;
	@MongoSource
	@ValidateModelCollection(message = SUBJECTPREFRENCES_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private List<String> subjectPreference;
	private String message;
	@SamSource
	private String countryCode;

	@SamSource
	private String pearsonCreatedBy;

	@MongoSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_DATEMODIFIED)
	private String dateModified;

	@MongoSource
	private String educationStage;

	@MongoSource
	private String userStatus;

	@MongoSource
	private String identifier;

	@MongoSource
	private Settings settings;

	public @GraphQLIgnore String getIdentifier() {
		return identifier;
	}

	@GraphQLIgnore
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public @GraphQLIgnore String getUserStatus() {
		return userStatus;
	}

	@GraphQLIgnore
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	@GraphQLIgnore
	public String getId() {
		return id;
	}

	@GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public TandC getTandc() {
		return tandc;
	}

	public void setTandc(TandC tandc) {
		this.tandc = tandc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPasswdValidation() {
		return passwdValidation;
	}

	public void setPasswdValidation(String passwdValidation) {
		this.passwdValidation = passwdValidation;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Set<String> getProduct() {
		return product;
	}

	public void setProduct(Set<String> product) {
		this.product = product;
	}

	public Boolean getSso() {
		return sso;
	}

	public List<String> getSchool() {
		return school;
	}

	public void setSchool(List<String> school) {
		this.school = school;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPortal() {
		return portal;
	}

	public void setPortal(String portal) {
		this.portal = portal;
	}

	public Boolean isSso() {
		return this.sso;
	}

	public void setSso(Boolean sso) {
		this.sso = sso;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPearsonCreatedBy() {
		return pearsonCreatedBy;
	}

	public void setPearsonCreatedBy(String pearsonCreatedBy) {
		this.pearsonCreatedBy = pearsonCreatedBy;
	}

	public List<String> getClassroom() {
		return classroom;
	}

	public void setClassroom(List<String> classroom) {
		this.classroom = classroom;
	}

	public String getDateModified() {
		return dateModified;
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	public List<String> getSubjectPreference() {
		return subjectPreference;
	}

	public void setSubjectPreference(List<String> subjectPreference) {
		this.subjectPreference = subjectPreference;
	}

	public String getEducationStage() {
		return educationStage;
	}

	public void setEducationStage(String educationStage) {
		this.educationStage = educationStage;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", uid=" + uid + ", fullName=" + fullName + ", roles=" + roles + ", avatar=" + avatar
				+ ", tandc=" + tandc + ", email=" + email + ", userName=" + userName + ", preferredName="
				+ preferredName + ", passwd=" + passwd + ", passwdValidation=" + passwdValidation + ", year=" + year
				+ ", product=" + product + ", classroom=" + classroom + ", school=" + school + ", organisation="
				+ organisation + ", status=" + status + ", portal=" + portal + ", sso=" + sso + ", lastLogin="
				+ lastLogin + ", dateCreated=" + dateCreated + ", subjectPreference=" + subjectPreference + ", message="
				+ message + ", countryCode=" + countryCode + ", pearsonCreatedBy=" + pearsonCreatedBy
				+ ", dateModified=" + dateModified + ", educationStage=" + educationStage + ", userStatus=" + userStatus
				+ ", settings=" + settings + "]";
	}

}
