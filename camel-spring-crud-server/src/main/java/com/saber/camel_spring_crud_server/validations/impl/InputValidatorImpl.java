package com.saber.camel_spring_crud_server.validations.impl;

import com.saber.camel_spring_crud_server.validations.InputValidator;
import org.springframework.stereotype.Service;

@Service
public class InputValidatorImpl implements InputValidator {
	private static final Integer CONTROL_NUMBER = 11;
	private static final Integer NATIONAL_CODE_LENGTH = 10;
	private static final Integer CHECK_DIGIT = 2;
	@Override
	public boolean validateNationalCode(String nationalCode) {
		if (nationalCode == null || nationalCode.trim().equals(""))
			return false;
		
		if (nationalCode.trim().length() < NATIONAL_CODE_LENGTH)
			return false;
		
		if (!nationalCode.trim().matches("\\d{10}"))
			return false;
		
		int k = NATIONAL_CODE_LENGTH;
		int sum = 0;
		int n;
		int controlNumber = Integer.parseInt(String.valueOf(nationalCode.charAt(nationalCode.length() - 1)));
		for (int i = 0; i < nationalCode.length() - 1; i++) {
			n = Integer.parseInt(String.valueOf(nationalCode.charAt(i))) * k;
			sum += n;
			k--;
		}
		int r = sum % CONTROL_NUMBER;
		int checkControlNumber = CONTROL_NUMBER - r;
		if (r >= CHECK_DIGIT && checkControlNumber == controlNumber) {
			return true;
		} else return r == controlNumber;
	}
}

