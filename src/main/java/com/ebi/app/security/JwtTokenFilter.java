package com.ebi.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

public class JwtTokenFilter extends GenericFilterBean {

    private static final String BEARER = "Bearer";

    private final String issuer;

    private final String secret;

    private UserDetailsService userDetailsService;

    public JwtTokenFilter(UserDetailsService userDetailsService, String secret, String issuer) {
        this.userDetailsService = userDetailsService;
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.issuer = issuer;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        var headerValue = ((HttpServletRequest)req).getHeader("Authorization");
        getBearerToken(headerValue).ifPresent(token-> {
            if (!isJwtExpired(token)) {
                var username = getClaimFromToken(token, Claims::getSubject);
                var userDetails = userDetailsService.loadUserByUsername(username);

                if (username.equals(userDetails.getUsername())) {
                    var usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest)req));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        });

        filterChain.doFilter(req, res);
    }

    private Optional<String> getBearerToken(String headerVal) {
        if (headerVal != null && headerVal.startsWith(BEARER)) {
            return Optional.of(headerVal.replace(BEARER, "").trim());
        }
        return Optional.empty();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    	final var claims =  Jwts.parserBuilder()
                .requireIssuer(issuer)
                .setAllowedClockSkewSeconds(1000)
                .setSigningKey(secret)
                .build().parseClaimsJws(token).getBody();
    	return claimsResolver.apply(claims);
    }

    private boolean isJwtExpired(String token) {
        try {
            var expirationDate = getClaimFromToken(token, Claims::getExpiration);
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException e) {
            logger.warn("JWT toekn expired");
            return true;
        }
    }
}