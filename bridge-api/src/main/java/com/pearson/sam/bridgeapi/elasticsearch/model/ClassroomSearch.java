/**
 * 
 */
package com.pearson.sam.bridgeapi.elasticsearch.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.CLASSROOM_ID_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.model.BaseModel;
import com.pearson.sam.bridgeapi.model.Members;
import com.pearson.sam.bridgeapi.model.Subject;

import io.leangen.graphql.annotations.GraphQLIgnore;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author VGUDLSA
 *
 */
@Getter
@Setter
@Document(indexName = "pp2classrooms", type = "classroom")
public class ClassroomSearch extends BaseModel {

  @Id
	private String id;
	@Field(fielddata = true, type = FieldType.Text)
	private String classroomId;
	@Field(fielddata = true, type = FieldType.Text)
	private String name;

	@Field(fielddata = true, type = FieldType.Text)
	private String dateCreated;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String validationCode;

	@Field(fielddata = true, type = FieldType.Text)
	private String schoolId;

	@Field(fielddata = true, type = FieldType.Text)
	private String groupOwner;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String groupType;

	@Field(fielddata = true, type = FieldType.Text)
	private String inactiveTimestamp;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String orgIdentifier;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String productModelIdentifier;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String updatedBy;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String updatedOn;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String activeTimestamp;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String createdBy;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String createdOn;
	
	@Field(fielddata = true, type = FieldType.Text)
	private String owner;

}
