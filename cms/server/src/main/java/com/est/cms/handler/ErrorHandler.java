package com.est.cms.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.est.core.common.rest.ApiError;
import com.est.core.common.rest.exception.ApiException;
import com.est.core.common.rest.exception.NotFoundException;

@ControllerAdvice
public class ErrorHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiError handleValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<String> errors = result.getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
		
		return new ApiError(HttpStatus.BAD_REQUEST.value(),"error.validation", errors);
	}
	
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ApiError handleNotFound(NotFoundException ex) {
		return new ApiError(HttpStatus.NOT_FOUND.value(),"error.404", ex.getMessage());
	}
	
	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiError handleApiException(ApiException ex) {
		return new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getTitle(), ex.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ApiError handleAllOtherExceptions(Exception ex) {
		ex.printStackTrace();
		return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.internal", ex.getMessage());
	}
}
