/**
 * 
 */
package com.pearson.sam.bridgeapi.elasticsearch.model;

import com.pearson.sam.bridgeapi.model.BaseModel;
import com.pearson.sam.bridgeapi.model.Location;

import java.util.List;

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
@Document(indexName = "pp2schools", type = "school")
public class SchoolSearch extends BaseModel {
  private static final long serialVersionUID = 1L;

  @Id
  private String id;
  @Field(fielddata = true, type = FieldType.Text)

  private String schoolId;
  @Field(fielddata = true, type = FieldType.Text)

  private String name;

  private Location location;
  private String organisation;
  @Field(fielddata = true, type = FieldType.Text)

  private String teacherCode;
  @Field(fielddata = true, type = FieldType.Text)

  private String studentCode;

  @Field(fielddata = true, type = FieldType.Text)

  private String team;
  private List<String> tags;
  @Field(fielddata = true, type = FieldType.Text)

  private String schoolType;
  @Field(fielddata = true, type = FieldType.Text)

  private String schoolCode;
  @Field(fielddata = true, type = FieldType.Text)

  private String createdBy;
  @Field(fielddata = true, type = FieldType.Text)

  private String createdOn;
  @Field(fielddata = true, type = FieldType.Text)

  private String updatedBy;
  @Field(fielddata = true, type = FieldType.Text)

  private String updatedOn;
  @Field(fielddata = true, type = FieldType.Text)

  private String productModelIdentifier;
  @Field(fielddata = true, type = FieldType.Text)

  private String schoolURL;
  @Field(fielddata = true, type = FieldType.Text)

  private String schoolStatus;
  private List<String> product;
  @Field(fielddata = true, type = FieldType.Text)

  private String educationStage;
  @Field(fielddata = true, type = FieldType.Text)

  private String activeTimestamp;

  private Long usersCount;
  private Long classRoomsCount;
}
