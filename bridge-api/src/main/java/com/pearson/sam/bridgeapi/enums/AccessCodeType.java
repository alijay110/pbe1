package com.pearson.sam.bridgeapi.enums;

public enum AccessCodeType {

	IN_BOOK("inBook"), SINGLE_USE("singleUse"), TEACHER("teacher");

	private AccessCodeType(String type) {
		this.type=type;
	}
	
	private String type;

	public String getType() {
		return type;
	}
}
