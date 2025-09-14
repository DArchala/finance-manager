package pl.archala.application.api.error;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(List<String> reasons,
                            ErrorCode errorCode,
                            Instant occurred) {

    public static ErrorResponse of(List<String> reasons, ErrorCode errorCode) {
        return new ErrorResponse(reasons, errorCode, Instant.now());
    }

}
