package pl.archala.dto.errorResponse;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.http.HttpStatus;
import pl.archala.httpStatus.HttpStatusDeserializer;
import pl.archala.httpStatus.HttpStatusSerializer;
import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(@JsonDeserialize(using = LocalDateTimeDeserializer.class)
                            @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime occurred,
                            List<String> reasons,
                            @JsonDeserialize(using = HttpStatusDeserializer.class)
                            @JsonSerialize(using = HttpStatusSerializer.class) HttpStatus status) {
}
