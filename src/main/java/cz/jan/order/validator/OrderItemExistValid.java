package cz.jan.order.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = OrderItemExistValidator.class)
@Target( {ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderItemExistValid {
    String message() default "Active product does not exist for productId";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
