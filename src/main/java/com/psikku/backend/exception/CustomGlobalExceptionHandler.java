package com.psikku.backend.exception;

import com.psikku.backend.entity.TestResult;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final String DATE_TIME_FORMAT = "yyyy-MM-dd HH-mm-ss";

    public DateTimeFormatter getDtf() {
        return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    }



    //==========================================================================================
    //==========================override response entity exception handler======================

    // validate the payload
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("exception","payload exception");
        body.put("timestamp", LocalDateTime.now().format(getDtf()));
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                                .map(x->x.getDefaultMessage())
                                .collect(Collectors.toList());
        body.put("errors",errors);
        return new ResponseEntity<>(body,headers,status);

    }

    // detect if all the required header included in the request
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status",status.value());
        body.put("exception","null header exception");
        body.put("timestamp",LocalDateTime.now().format(getDtf()));
        body.put("error",ex.getMessage());
        return new ResponseEntity<>(body,headers,status);
    }

    // handle different data type passed in request parameter
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("exception","path variable exception");
        body.put("timestamp", LocalDateTime.now().format(getDtf()));
        String endpoint = request.getDescription(false);
        String error = "'"+ex.getValue()+"' "+"is not a valid path variable at the endpoint: "+endpoint.substring(4 ,(endpoint.length()-ex.getValue().toString().length()));
        body.put("errors",error);
        return new ResponseEntity<>(body,headers,status);

    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("exception","validation exception");
        body.put("timestamp", LocalDateTime.now().format(getDtf()));
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.toList());

        body.put("errors",errors);
        return new ResponseEntity<>(body,headers,status);
    }

    //========================================================================================
    //==================================custom exception handler==============================


    @ExceptionHandler(TestException.class)
    public ResponseEntity<TestExceptionResponse> handleTestException (TestException e){
        TestExceptionResponse response = new TestExceptionResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PackageException.class)
    public ResponseEntity<PackageExceptionResponse> handlePackageException (PackageException e){
        PackageExceptionResponse response = new PackageExceptionResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(TestResultException.class)
    public ResponseEntity<TestResultExceptionResponse> handleTestResultException (TestResultException e){
        TestResultExceptionResponse response = new TestResultExceptionResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<PaymentExceptionResponse> handlePaymentException(PaymentException e){

        PaymentExceptionResponse response = new PaymentExceptionResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VoucherException.class)
    public ResponseEntity<VoucherExceptionResponse> handleVoucherException(VoucherException e){

        VoucherExceptionResponse response = new VoucherExceptionResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompanyException.class)
    public ResponseEntity<CompanyExceptionResponse> handleVoucherException(CompanyException e){

        CompanyExceptionResponse response = new CompanyExceptionResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnswerException.class)
    public ResponseEntity<AnswerExceptionResponse> handleVoucherException(AnswerException e){

        AnswerExceptionResponse response = new AnswerExceptionResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<UserExistExceptionResponse> handleUserExistException(UserExistException e){

        UserExistExceptionResponse response = new UserExistExceptionResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}
