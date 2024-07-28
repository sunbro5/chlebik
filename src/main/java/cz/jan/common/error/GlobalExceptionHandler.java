package cz.jan.common.error;

import cz.jan.common.error.exception.ResourceNotFoundException;
import cz.jan.order.exception.OrderInvalidActionException;
import cz.jan.order.exception.OrderNotFoundException;
import cz.jan.order.exception.OrderProductQuantityException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleConstraintViolation(ConstraintViolationException ex) {
        var errors = ex.getConstraintViolations().stream()
                .map(constraintViolation ->
                        constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage())
                .toList();
        return ApiError.builder()
                .message("Invalid request body with errors")
                .errors(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiError handleGlobalException(Exception ex) {
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
