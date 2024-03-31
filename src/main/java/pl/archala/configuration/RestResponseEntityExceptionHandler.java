package pl.archala.configuration;

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
import pl.archala.dto.errorResponse.ErrorResponse;
import pl.archala.exception.*;
import pl.archala.mapper.ErrorResponseMapper;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorResponseMapper mapper;

    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> reasons = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
        ErrorResponse errorResponse = mapper.toErrorResponse(reasons, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> reasons = e.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).toList();
        ErrorResponse errorResponse = mapper.toErrorResponse(reasons, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlePropertyReferenceException(EntityNotFoundException e) {
        return getErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UsersConflictException.class)
    protected ResponseEntity<ErrorResponse> handleUserException(UsersConflictException e) {
        return getErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = TransactionsLimitException.class)
    protected ResponseEntity<ErrorResponse> handleTransactionsLimitException(TransactionsLimitException e) {
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserException.class)
    protected ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InsufficientFundsException.class)
    protected ResponseEntity<ErrorResponse> handleInsufficientFundsException(InsufficientFundsException e) {
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserAlreadyContainsBalanceException.class)
    protected ResponseEntity<ErrorResponse> handleUserAlreadyContainsBalance(UserAlreadyContainsBalanceException e) {
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PropertyReferenceException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(PropertyReferenceException e) {
        return getErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> getErrorResponse(Exception e, HttpStatus status) {
        List<String> reasons = List.of(e.getMessage());
        ErrorResponse errorResponse = mapper.toErrorResponse(reasons, status);
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }
}