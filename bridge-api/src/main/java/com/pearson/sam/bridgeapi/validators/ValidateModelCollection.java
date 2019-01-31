package com.pearson.sam.bridgeapi.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { ValidateModelCollectionValidator.class })
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateModelCollection {

	boolean mandatory() default false;

	String message() default "Invalid Model List";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
