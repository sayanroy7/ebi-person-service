package com.ebi.app.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SignInDto {

	@NotNull
    private String username;

    @NotNull
    private String password;
}
