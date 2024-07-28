package cz.jan.common.error;

import cz.jan.common.error.exception.ResourceNotFoundException;
import cz.jan.order.exception.OrderInvalidActionException;
import cz.jan.order.exception.OrderNotFoundException;
import cz.jan.order.exception.OrderProductQuantityException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleConstraintViolation(MethodArgumentNotValidException ex) {
        var errors = Stream.concat(
                        ex.getBindingResult().getFieldErrors().stream()
                                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()),
                        ex.getBindingResult().getGlobalErrors().stream()
                                .map(objectError -> objectError.getObjectName() + ": " + objectError.getDefaultMessage()))
                .toList();
        return ApiError.builder()
                .message("Invalid request body with errors")
                .errors(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiError handleGlobalException(Exception ex) {
        log.error("GeneralError", ex);
        return ApiError.builder()
                .message(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class, OrderNotFoundException.class})
    public ApiError handleNotFoundException(RuntimeException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({OrderInvalidActionException.class})
    public ApiError handleInvalidOrderActionException(OrderInvalidActionException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({OrderProductQuantityException.class})
    public ApiError handleOrderProductQuantityException(OrderProductQuantityException ex) {
        return ApiError.builder()
                .message("Cannot create order, there is not enough quantity on product.")
                .errors(ex.getProductErrors())
                .build();
    }

}
