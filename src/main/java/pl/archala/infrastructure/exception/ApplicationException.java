package pl.archala.infrastructure.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationException extends RuntimeException {

    private final String message;
    private final HttpStatusCode status;
    private final Instant timestamp;

    public static ApplicationException from(String message, HttpStatus status) {
        return new ApplicationException(message, status, Instant.now());
    }

    public static ApplicationException notFound(String message) {
        return new ApplicationException(message, HttpStatus.NOT_FOUND, Instant.now());
    }

}
