package keski.mert.loan.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NumberOfInstallmentsValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNumberOfInstallments {
    String message() default "Number of installments must be one of the following: 6, 9, 12, 24";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
