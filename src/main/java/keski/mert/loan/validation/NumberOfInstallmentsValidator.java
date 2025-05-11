package keski.mert.loan.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class NumberOfInstallmentsValidator implements ConstraintValidator<ValidNumberOfInstallments, Integer> {
    private static final List<Integer> ALLOWED_NUMBER_OF_INSTALLMENTS = Arrays.asList(6, 9, 12, 24);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return ALLOWED_NUMBER_OF_INSTALLMENTS.contains(value);
    }
}
