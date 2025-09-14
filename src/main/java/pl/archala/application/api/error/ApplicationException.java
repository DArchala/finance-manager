package pl.archala.application.api.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationException extends RuntimeException {

    private final String message;
    private final ErrorCode errorCode;
    private final Instant timestamp;

    public static ApplicationException from(String message, ErrorCode errorCode) {
        return new ApplicationException(message, errorCode, Instant.now());
    }

    public static ApplicationException notFound(String message) {
        return new ApplicationException(message, ErrorCode.NOT_FOUND, Instant.now());
    }

    public static ApplicationException badRequest(String message) {
        return new ApplicationException(message, ErrorCode.BAD_REQUEST, Instant.now());
    }

    public static ApplicationException internalError() {
        return new ApplicationException("Internal error", ErrorCode.INTERNAL_SERVER_ERROR, Instant.now());
    }

}
