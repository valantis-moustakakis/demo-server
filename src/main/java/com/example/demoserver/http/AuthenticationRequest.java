package com.example.demoserver.http;

import com.example.demoserver.validators.ValidEmail;
import com.example.demoserver.validators.ValidPassword;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotNull
    @ValidEmail
    private String email;

    @NotNull
    @ValidPassword
    private String password;
}
