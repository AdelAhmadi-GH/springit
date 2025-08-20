package com.adelahmadi.springit.web.dto.validation;

import com.adelahmadi.springit.web.dto.RegisterRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest dto, ConstraintValidatorContext context) {
        if (dto == null)
            return true; // به نال حساس نیستیم
        String p1 = dto.getPassword();
        String p2 = dto.getConfirmPassword();
        if (p1 == null || p2 == null)
            return false;
        return p1.equals(p2);
    }
}
