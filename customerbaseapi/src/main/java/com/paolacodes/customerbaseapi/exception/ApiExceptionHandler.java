package com.paolacodes.customerbaseapi.exception;

import com.paolacodes.customerbaseapi.domain.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<Problem.Field> fields = new ArrayList<>();

        for(ObjectError error : ex.getBindingResult().getAllErrors()){
            String nameField = ((FieldError) error).getField();
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());

            fields.add(new Problem.Field(nameField, message));
        }

        Problem problem = new Problem();
        problem.setStatus(status.value());
        problem.setTitle("Um ou mais campos estão inválidos, faça o peenchimento correto");
        problem.setDateTime(LocalDateTime.now());
        problem.setFields(fields);

        return handleExceptionInternal(ex, problem ,headers, status, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException businessException, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Problem problem = new Problem();
        problem.setStatus(status.value());
        problem.setDateTime(LocalDateTime.now());
        problem.setTitle(businessException.getMessage());

        return handleExceptionInternal(businessException, problem, new HttpHeaders(), status, request);
    }
}
