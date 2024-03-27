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

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlePropertyReferenceException(EntityNotFoundException e) {
        List<String> reasons = List.of(e.getMessage());
        ErrorResponse errorResponse = mapper.toErrorResponse(reasons, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> reasons = e.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).toList();
        ErrorResponse errorResponse = mapper.toErrorResponse(reasons, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }

    @ExceptionHandler(value = PropertyReferenceException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(PropertyReferenceException e) {
        List<String> reasons = List.of(e.getMessage());
        ErrorResponse errorResponse = mapper.toErrorResponse(reasons, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }
}