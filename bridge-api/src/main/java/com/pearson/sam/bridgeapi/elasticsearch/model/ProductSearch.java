/**
 * 
 */
package com.pearson.sam.bridgeapi.elasticsearch.model;

import com.pearson.sam.bridgeapi.model.BaseModel;
import com.pearson.sam.bridgeapi.model.Details;

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
@Document(indexName = "pp2products", type = "product")
public class ProductSearch extends BaseModel {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;
  @Field(fielddata = true, type = FieldType.Text)
  private String series;

  @Field(fielddata = true, type = FieldType.Text)
  private String productId;
  @Field(fielddata = true, type = FieldType.Text)
  private String cartridgeId;
  private List<String> type;
  private Details details;
  @Field(fielddata = true, type = FieldType.Text)
  private String status;
  @Field(fielddata = true, type = FieldType.Text)
  private String productCode;

  private Boolean isTeacherProduct;
  @Field(fielddata = true, type = FieldType.Text)

  private String courseUrl;
  @Field(fielddata = true, type = FieldType.Text)

  private String template;
  private List<String> defaultYearLevel;
  @Field(fielddata = true, type = FieldType.Text)

  private String productTerm;
  @Field(fielddata = true, type = FieldType.Text)

  private String monthUntilReactivation;
  @Field(fielddata = true, type = FieldType.Text)

  private String reactivationVoucherCodeType;
  @Field(fielddata = true, type = FieldType.Text)

  private String coolingOffPeriod;
  @Field(fielddata = true, type = FieldType.Text)

  private String createdOn;
  @Field(fielddata = true, type = FieldType.Text)

  private String updatedBy;
  @Field(fielddata = true, type = FieldType.Text)

  private String updatedOn;
  
  @Field(fielddata = true, type = FieldType.Text)
  private String linkedProductCode;
  
  private Long resourcesCount;
}
