package com.example.demoserver.validators;

import com.example.demoserver.http.RegistrationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        RegistrationRequest request = (RegistrationRequest) obj;
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
