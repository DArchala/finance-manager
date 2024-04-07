package pl.archala.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.HttpStatus;
import pl.archala.dto.errorResponse.ErrorResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ErrorResponseMapper {

    @Mapping(target = "occurred", expression = "java(LocalDateTime.now())")
    ErrorResponse toErrorResponse(List<String> reasons, HttpStatus status);

}
