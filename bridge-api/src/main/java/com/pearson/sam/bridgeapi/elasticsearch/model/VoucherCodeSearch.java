/**
 * 
 */
package com.pearson.sam.bridgeapi.elasticsearch.model;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.model.BaseModel;

import javax.validation.constraints.Min;
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
@Document(indexName = "pp2vouchercodes", type = "vouchercode")
public class VoucherCodeSearch extends BaseModel {

  private static final long serialVersionUID = 1L;

  @Id

  private String id;
  @Field(fielddata = true, type = FieldType.Text)
  private String voucherCode;

  @Field(fielddata = true, type = FieldType.Text)

  private String voucherId;
  @Field(fielddata = true, type = FieldType.Text)

  private String createdDate;
  @Field(fielddata = true, type = FieldType.Text)

  private String startDate;
  @Field(fielddata = true, type = FieldType.Text)

  private String endDate;

  @Field(fielddata = true, type = FieldType.Text)
  private String type;

  private Boolean isVoid;
  @Field(fielddata = true, type = FieldType.Text)

  private String accessCode;

  private Long totalReactivations;

  @Field(fielddata = true, type = FieldType.Text)

  private String batch;
  private Integer quantity;
  @Field(fielddata = true, type = FieldType.Text)

  private String createdBy;
  @Field(fielddata = true, type = FieldType.Text)

  private String dateCreated;
  @Field(fielddata = true, type = FieldType.Text)

  private String updatedOn;
  @Field(fielddata = true, type = FieldType.Text)

  private String updatedBy;
  @Field(fielddata = true, type = FieldType.Text)

  private String lastActivatedBy;
  @Field(fielddata = true, type = FieldType.Text)

  private String lastActivatedDate;

}
