package com.example.demoserver.http;

import com.example.demoserver.validators.PasswordMatches;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class RegistrationRequest extends AuthenticationRequest {

    @NotNull
    private String confirmPassword;
}
