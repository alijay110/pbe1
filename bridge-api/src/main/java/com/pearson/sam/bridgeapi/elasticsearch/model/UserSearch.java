package com.pearson.sam.bridgeapi.elasticsearch.model;

import com.pearson.sam.bridgeapi.model.BaseModel;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(indexName = "pp2users", type = "user")
public class UserSearch extends BaseModel {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  private String id;
  @Field(fielddata=true,type=FieldType.Text)

  private String uid;
  @Field(fielddata=true,type=FieldType.Text)

  private String fullName;
  @Field(fielddata = true, type = FieldType.Text)
  private String email;
  @Field(fielddata = true, type = FieldType.Text)

  private String userName;
  @Field(fielddata=true,type=FieldType.Text)

  private String year;
  private List<String> school;
  @Field(fielddata=true,type=FieldType.Text)

  private String organisation;

  private List<String> classroom;
  @Field(fielddata=true,type=FieldType.Text)

  private String dateCreated;
  @Field(fielddata=true,type=FieldType.Text)

  private String dateModified;
  private Set<String> roles;
  @Field(fielddata=true,type=FieldType.Text)

  private String avatar;
  private Set<String> product;
  @Field(fielddata=true,type=FieldType.Text)

  private String status;

}