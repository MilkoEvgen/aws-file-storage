package com.milko.awsfilestorage.security;


import com.milko.awsfilestorage.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

@Slf4j
public class JwtHandler {

    private final String secret;

    public JwtHandler(String secret) {
        this.secret = secret;
    }

    public Mono<VerificationResult> check(String accessToken){
        return  Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    private VerificationResult verify(String token){
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();
        if (expirationDate.before(new Date())){
            throw new RuntimeException("Token expired");
        }

        return new VerificationResult(claims, token);
    }

    public Claims getClaimsFromToken(String token){
        try {
            JwtParser parser = Jwts.parser();
            String encodedKey = Base64.getEncoder().encodeToString(secret.getBytes());
            parser.setSigningKey(encodedKey);
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (Exception e) {

            throw e;
        }
    }


    public static class VerificationResult{
        public Claims claims;
        public String token;
        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}
