package com.saber.camel_spring_crud_server.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class NationalCodeValidator implements ConstraintValidator<NationalCode, String> {
	
	@Autowired
	InputValidator validator;
	
	@Override
	public boolean isValid(String nationalCode, ConstraintValidatorContext constraintValidatorContext) {
		return validator.validateNationalCode(nationalCode);
	}
}
