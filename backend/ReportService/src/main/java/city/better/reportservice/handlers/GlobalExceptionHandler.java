package city.better.reportservice.handlers;

import city.better.reportservice.dtos.ExceptionDto;
import city.better.reportservice.exceptions.ResourceNotFoundException;
import city.better.reportservice.mappers.ExceptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionMapper exceptionMapper;

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ExceptionDto> handleNotFound(ResourceNotFoundException exception) {
        var exceptionDto = exceptionMapper.toDto(exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exceptionDto);
    }

}
