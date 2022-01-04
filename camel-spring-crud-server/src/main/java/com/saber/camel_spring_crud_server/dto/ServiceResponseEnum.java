package com.saber.camel_spring_crud_server.dto;

public enum ServiceResponseEnum {
	JSON_MAPPING_EXCEPTION(1,"json خطا در پردازش"),
	JSON_PARSE_EXCEPTION(2,"json خطا در پردازش"),
	RESOURCE_NOT_FOUND_EXCEPTION(3,"اطلاعات مورد نظر یافت نشد"),
	RESOURCE_DUPLICATION_EXCEPTION(4,"اطلاعات تکراری است"),
	TIMEOUT_EXCEPTION(5,"خطا در برقرای ارتباط"),
	BEAN_VALIDATION_EXCEPTION(6,"خطای اعتبارسنجی");
	
	int code;
	String message;
	
	ServiceResponseEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}
