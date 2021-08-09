package com.jack.ssoserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JwtUtil {
    /**创建JWT*/
    public static String createJwt() throws IllegalArgumentException, UnsupportedEncodingException {
        Algorithm al = Algorithm.HMAC256("secretkey");
        String token = JWT.create()
                .withIssuer("Jack")
                .withSubject("SSO")
                .withClaim("userid", 1234)
                .withExpiresAt(new Date(System.currentTimeMillis()+360000))
                .sign(al);
        return token;
    }
    /**验证jwt*/
    public static boolean verifyJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secretkey");
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            System.out.println("校验失败");
        }
        return false;
    }
}