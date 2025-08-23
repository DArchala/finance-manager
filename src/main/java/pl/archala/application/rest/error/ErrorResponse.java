package pl.archala.application.rest.error;

import org.springframework.http.HttpStatusCode;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(List<String> reasons, HttpStatusCode status, Instant occurred) {

    public static ErrorResponse of(List<String> reasons, HttpStatusCode status) {
        return new ErrorResponse(reasons, status, Instant.now());
    }

}
