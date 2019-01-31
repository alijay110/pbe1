package com.pearson.sam.bridgeapi.model;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.VOUCHER_CODE_SHOULD_NOT_EMPTY;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.types.GraphQLType;

@Document(collection = "voucherCodeHistory")
@GraphQLType(name = "VoucherCodeHistory")
public class VoucherCodeHistory extends BaseModel {

	@Id
	private String id;
	@MongoSource
	private String voucherCodeHistoryId;
	@MongoSource
	private Voucher alteration;
	@MongoSource
	private String alterDate;
	@MongoSource
	private String alterBy;
	@NotBlank(message = VOUCHER_CODE_SHOULD_NOT_EMPTY)
	@NotNull(message = VOUCHER_CODE_SHOULD_NOT_EMPTY)
	@MongoSource
	private String voucherCode;
	
	@GraphQLIgnore
	public String getId() {
		return id;
	}
	
	@GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}
	public String getVoucherCodeHistoryId() {
		return voucherCodeHistoryId;
	}
	public void setVoucherCodeHistoryId(String voucherCodeHistoryId) {
		this.voucherCodeHistoryId = voucherCodeHistoryId;
	}
	public String getAlterDate() {
		return alterDate;
	}
	public void setAlterDate(String alterDate) {
		this.alterDate = alterDate;
	}
	public String getAlterBy() {
		return alterBy;
	}
	public void setAlterBy(String alterBy) {
		this.alterBy = alterBy;
	}
	public Voucher getAlteration() {
		return alteration;
	}
	public void setAlteration(Voucher alteration) {
		this.alteration = alteration;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
	
}