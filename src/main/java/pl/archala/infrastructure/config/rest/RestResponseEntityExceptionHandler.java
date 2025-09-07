package pl.archala.infrastructure.config.rest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.archala.application.api.error.ErrorCode;
import pl.archala.application.api.error.ErrorResponse;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var reasons = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
        var errorResponse = ErrorResponse.of(reasons, ErrorCode.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.BAD_REQUEST.name()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        var reasons = e.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).toList();
        var errorResponse = ErrorResponse.of(reasons, ErrorCode.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.BAD_REQUEST.name()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return getErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({PropertyReferenceException.class})
    protected ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception e) {
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> getErrorResponse(Exception e, HttpStatus status) {
        var errorResponse = ErrorResponse.of(List.of(e.getMessage()), ErrorCode.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, status);
    }
}