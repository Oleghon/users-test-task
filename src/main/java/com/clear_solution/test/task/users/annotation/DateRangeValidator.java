package com.clear_solution.test.task.users.annotation;

import com.clear_solution.test.task.users.domain.Range;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<DateRange, Range> {
    @Override
    public boolean isValid(Range range, ConstraintValidatorContext constraintValidatorContext) {
        return range.from().isBefore(range.to());
    }
}
