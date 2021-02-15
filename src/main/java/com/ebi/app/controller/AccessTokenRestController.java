package com.ebi.app.controller;

import com.ebi.app.model.web.response.ErrorResponse;
import com.ebi.app.model.web.response.WebApiResponse;
import com.ebi.app.security.JwtProvider;
import com.ebi.app.security.SignInDto;
import com.ebi.app.security.TokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
public class AccessTokenRestController {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebApiResponse<TokenDTO>> signIn(@RequestBody @Valid SignInDto signInDto) {
        try {
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getUsername(),
                    signInDto.getPassword()));
            var accessToken = jwtProvider.createToken(signInDto.getUsername(), authentication.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
            return new ResponseEntity<>(WebApiResponse.success(new TokenDTO(accessToken)), HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.error("Log in failed for user: " + signInDto.getUsername(), e);
        }

        return new ResponseEntity<>(WebApiResponse.error(ErrorResponse.builder()
                .message("Not Found")
                .statusCode(HttpStatus.UNAUTHORIZED)
                .build()), HttpStatus.UNAUTHORIZED);
    }

}
