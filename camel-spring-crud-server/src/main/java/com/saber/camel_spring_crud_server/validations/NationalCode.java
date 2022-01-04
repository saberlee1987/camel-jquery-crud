package com.saber.camel_spring_crud_server.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NationalCodeValidator.class)
public @interface NationalCode {
	String message() default "Please Enter valid nationalCode";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
