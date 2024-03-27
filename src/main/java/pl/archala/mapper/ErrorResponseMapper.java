package pl.archala.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.archala.dto.errorResponse.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ErrorResponseMapper {

    public ErrorResponse toErrorResponse(List<String> reasons, HttpStatus status) {
        return new ErrorResponse(LocalDateTime.now(), reasons, status);
    }

}
