package pl.archala.application.api.error;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(List<String> reasons, String status, Instant occurred) {

    public static ErrorResponse of(List<String> reasons, String status) {
        return new ErrorResponse(reasons, status, Instant.now());
    }

}
