package cz.jan.order.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = OrderItemDuplicityValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderItemDuplicityValid {
    String message() default "ProductId duplicity";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
