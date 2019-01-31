package com.pearson.sam.bridgeapi.validators;

import java.util.Collection;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

public class ValidateModelCollectionValidator
		implements ConstraintValidator<ValidateModelCollection, Collection<String>> {

	final private Pattern pattern = Pattern.compile("^[0-9a-zA-z]*");

	boolean mandatory;

	@Override
	public void initialize(final ValidateModelCollection constraintAnnotation) {
		mandatory = constraintAnnotation.mandatory();
	}

	@Override
	public boolean isValid(Collection<String> value, ConstraintValidatorContext context) {
		if (!mandatory && (value == null || value.isEmpty())) {
			return true;
		}
		return !(value == null || value.isEmpty()) && value.stream().filter(e -> !StringUtils.isBlank(e))
				.filter(e -> pattern.matcher(e).matches()).count() == value.size();
	}
}
