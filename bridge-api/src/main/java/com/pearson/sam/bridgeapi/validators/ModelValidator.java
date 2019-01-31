package com.pearson.sam.bridgeapi.validators;

import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

import com.pearson.sam.bridgeapi.constants.ErrorMessageConstants;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGraphqlException;

/**
 * 
 * @author VKu99Ma
 *
 */
@Component
public class ModelValidator {

	private static final String CREATE = "CREATE";

	@Autowired
	SmartValidator validator;

	/**
	 * 
	 * @param data
	 * @param operation
	 */
	public void validateModel(Object data, String operation) {

		String objectName = data != null ? data.getClass().getSimpleName() : "User";

		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(data, objectName);

		if (StringUtils.isNotEmpty(operation) && operation.equalsIgnoreCase(CREATE)) {
			validator.validate(data, bindingResult);
		}

		if (bindingResult.hasErrors()) {
			throw new BridgeApiGraphqlException(bindingResult.getAllErrors().stream().map(s -> s.getDefaultMessage())
					.collect(Collectors.toList()).toString());
		}
	}

	/**
	 * 
	 * @param value
	 * @param key
	 */
	public void validateEmptyNullValue(String value, String key) {

		String objectName = value != null ? value.getClass().getSimpleName() : "User";

		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(value, objectName);

		if ((!Optional.ofNullable(value).isPresent()) || (StringUtils.isBlank(value))) {
			ObjectError error = new ObjectError(key, key + ErrorMessageConstants.SHOULD_NOT_BE_EMPTY);
			bindingResult.addError(error);
		}

		if (bindingResult.hasErrors()) {
			throw new BridgeApiGraphqlException(bindingResult.getAllErrors().stream().map(s -> s.getDefaultMessage())
					.collect(Collectors.toList()).toString());
		}
	}
}
