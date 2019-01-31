package com.pearson.sam.bridgeapi.validators;

import com.pearson.sam.bridgeapi.model.BaseModel;

public interface ValidationStrategy {
	 <T extends BaseModel> boolean validate(T model,String inputfieldname);
}
