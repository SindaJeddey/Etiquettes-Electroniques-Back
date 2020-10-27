package project.ee.controllers.adviceControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.ee.exceptions.ApiError;
import project.ee.exceptions.ResourceNotFoundException;
import project.ee.exceptions.ResourceNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(ResourceNotFoundException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getMessage());

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message("404. Resource not found")
                .errors(details)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotValidException.class)
    public ResponseEntity<ApiError> handleNotValidException(ResourceNotFoundException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getMessage());

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message("400. Resource not valid")
                .status(HttpStatus.BAD_REQUEST)
                .errors(details)
                .build();
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException exception) {
        List<String> details = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : violations) {
            details.add(constraintViolation.getMessage());
        }

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message("400. Constraint violation")
                .status(HttpStatus.BAD_REQUEST)
                .errors(details)
                .build();
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getMessage());

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message("400. Malformed JSON")
                .status(HttpStatus.BAD_REQUEST)
                .errors(details)
                .build();

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiError> handleAll(Exception exception) {

        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message("400. Error occurred")
                .errors(details)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
