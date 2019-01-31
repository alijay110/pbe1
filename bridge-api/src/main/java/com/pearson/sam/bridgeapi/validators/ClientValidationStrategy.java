package com.pearson.sam.bridgeapi.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pearson.sam.bridgeapi.constants.ErrorMessageConstants;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.BaseModel;

public enum ClientValidationStrategy implements ValidationStrategy{

	TIMESTAMP_VALIDATION{
		@Override
		public <T extends BaseModel> boolean validate(T model,String inputfieldvalue) {
			if(!inputfieldvalue.matches("\\d{13}")) {
				throw new BridgeApiGraphqlException(ErrorMessageConstants.TIMESTAMP_INVALID);
			}
			return true;
		}
	},
	ALPHANUMERIC_VALIDATION{
		@Override
		public <T extends BaseModel> boolean validate(T model,String inputfieldvalue) {
			Pattern p = Pattern.compile("[A-Za-z0-9 @'-_]*");
		    Matcher match = p.matcher(inputfieldvalue);
		    if(!match.matches()) {
		    	throw new BridgeApiGraphqlException(ErrorMessageConstants.INVALID_SPECIAL_CHARACTERS_PRESENT);
		    }
			return true;
		}
		
	},
	NUMERIC_VALIDATION{
		@Override
		public <T extends BaseModel> boolean validate(T model,String inputfieldvalue) {
			Pattern p = Pattern.compile("[0-9]*");
		    Matcher match = p.matcher(inputfieldvalue);
		    if(!match.matches()) {
		    	throw new BridgeApiGraphqlException(ErrorMessageConstants.FIELD_NOT_NUMERIC);
		    }
			return true;
		}
		
	},
	MANDATORY_FIELD_VALIDATION{
		@Override
		public <T extends BaseModel> boolean validate(T model,String inputfieldvalue) {
			if("null".equals(inputfieldvalue) || inputfieldvalue.trim().isEmpty()) {
		    	throw new BridgeApiGraphqlException(ErrorMessageConstants.MANDATORY_FIELD_MISSING);
		    }
			return true;
		}
		
	},
	NOTNULL_VALIDATION{
		@Override
		public <T extends BaseModel> boolean validate(T model,String inputfieldname) {
			//TODO
			return true;
		}
		
	},
	NOTNULLANDNOTEMPTY_VALIDATION{
		@Override
		public <T extends BaseModel> boolean validate(T model,String inputfieldname) {
			//TODO
			return true;
		}
	},
	EMPTY_VALIDATION{
		@Override
		public <T extends BaseModel> boolean validate(T model,String inputfieldname) {
			//TODO
			return true;
		}
	};
	
	public abstract <T extends BaseModel> boolean validate(T model,String inputfieldname);

}
