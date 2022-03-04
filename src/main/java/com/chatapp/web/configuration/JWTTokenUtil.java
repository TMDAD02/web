package com.chatapp.web.configuration;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class JWTTokenUtil implements Serializable {

    private static final String TOKEN_HEADER_PARAM = "Authorization";
    private static final String TOKEN_COOKIE_PARAM = "token";

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private String secret = "MyMasterKey1.";

    /**
     * Retrieve a username from a token
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retrieve the expiratoin date of a token
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // For retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Check if token is expired
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Generate a token for a user
     *
     * @param userDetails
     * @return
     */

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * While creating the token -
     *     1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
     *     2. Sign the JWT using the HS512 algorithm and secret key.
     *     3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     *     Compaction of the JWT to a URL-safe string
     *
     * @param claims
     * @param subject
     * @return
     */

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Validate a token
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Try to get the JWT from the header or for a cookie
     *
     * @param request
     * @return
     */

    public String getRequestToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER_PARAM);
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals(TOKEN_COOKIE_PARAM)) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null) {
            return token;
        }

        return null;
    }
}