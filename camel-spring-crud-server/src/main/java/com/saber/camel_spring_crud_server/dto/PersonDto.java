package com.saber.camel_spring_crud_server.dto;

import com.saber.camel_spring_crud_server.validations.NationalCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PersonDto {
	@NotBlank(message = "firstName is Required")
	@Size(min = 3, max = 70, message = "firstName must be > 3 and < 70 character")
	@ApiModelProperty(value = "firstName",example = "saber")
	private String firstname;
	@NotBlank(message = "lastname is Required")
	@Size(min = 3, max = 80, message = "lastname must be > 3 and < 80 character")
	@ApiModelProperty(value = "lastname",example = "Azizi")
	private String lastname;
	@NotBlank(message = "nationalCode is Required")
	@Size(min = 1, max = 10, message = "nationalCode must be < 10 digits")
	@Pattern(regexp = "\\d{10}",message = "nationalCode is not digits")
	@NationalCode(message = "nationalCode is not valid")
	@ApiModelProperty(value = "nationalCode",example = "0079028748")
	private String nationalCode;
	@NotNull(message = "age is Required")
	@Positive(message = "age must be > 0")
	@Max(value = 999, message = "age must be < 999")
	@ApiModelProperty(value = "age",example = "35")
	private Integer age;
	@NotBlank(message = "email is Required")
	@Email(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", message = "email is not valid")
	@ApiModelProperty(value = "email",example = "saberazizi66@yahoo.com")
	private String email;
	@NotBlank(message = "mobile is Required")
	@Size(min = 1, max = 11, message = "mobile must be > 1 and < 11 digits")
	@Pattern(regexp = "09[0-9]{9}", message = "mobile is not valid")
	@ApiModelProperty(value = "mobile",example = "09365627895")
	private String mobile;
}