package com.pearson.sam.bridgeapi.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.pearson.sam.bridgeapi.constants.ErrorMessageConstants;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;
import com.pearson.sam.bridgeapi.model.BaseModel;

@Component
public class ValidationContext{
	
	Map<String,ValidationStrategy> validationStrategies;
	
	public void setValidationContext(Map<String,ValidationStrategy> validationStrategies) {
		this.validationStrategies=validationStrategies;
	}
	
	public void executeValidationStrategy(BaseModel model) {
		List<String> errorMessages = new ArrayList<>();
		validationStrategies.forEach((field,strategy) ->  
		       {
		    	   String fieldname = null; //String "null" 
		    	   String fieldvalue = null;//String "null"
		       try{
		    	   	String[]  fields = field.split(strategy.toString()); 
		    	   	fieldname = fields[0];
		    	   	if(fields.length > 1) {
		    	   		fieldvalue =fields[1];
		       		}
					strategy.validate(model, fieldvalue);
		        }catch(BridgeApiGraphqlException e) {
		        	errorMessages.add(e.getMessage()+fieldname);		       
		        }		
		       });
		if(!errorMessages.isEmpty()) {
			throw new BridgeApiGraphqlException(errorMessages.toString());
		}
	}

	
}
