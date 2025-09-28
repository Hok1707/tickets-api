package com.kimhok.tickets.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600000}") // 1 hour in milliseconds
    private long expiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7 days in milliseconds
    private long refreshExpiration;

    private String createToken(Map<String, Object> claims, String subject, long expiryMs) {
        Instant now = Instant.now();
        Instant expiryTime = now.plusMillis(expiryMs);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryTime))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof CustomUserDetails customUser) {
            claims.put("username", customUser.getUser().getUsername());
            claims.put("userId", customUser.getUser().getId().toString());
            claims.put("type", "access");
        }

        return createToken(claims, userDetails.getUsername(), expiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        if (userDetails instanceof CustomUserDetails customUser) {
            claims.put("username", customUser.getUser().getUsername());
            claims.put("userId", customUser.getUser().getId().toString());
        }

        return createToken(claims, userDetails.getUsername(), refreshExpiration);
    }

    public String generateTokenWithCustomExpiry(UserDetails userDetails, Map<String, Object> extraClaims, long customExpiryMs) {
        Map<String, Object> claims = new HashMap<>(extraClaims);

        if (userDetails instanceof CustomUserDetails customUser) {
            claims.put("username", customUser.getUser().getUsername());
            claims.put("userId", customUser.getUser().getId().toString());
        }

        return createToken(claims, userDetails.getUsername(), customExpiryMs);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims - Updated to use new API
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("JWT token is malformed: {}", e.getMessage());
            throw e;
        } catch (SecurityException | IllegalArgumentException e) {
            log.error("JWT token is invalid: {}", e.getMessage());
            throw e;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isUsernameValid = username.equals(userDetails.getUsername());
            boolean isTokenNotExpired = !isTokenExpired(token);

            log.debug("Token validation - Username valid: {}, Not expired: {}", isUsernameValid, isTokenNotExpired);
            return isUsernameValid && isTokenNotExpired;

        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String type = claims.get("type", String.class);
            return "refresh".equals(type);
        } catch (JwtException e) {
            log.error("Error checking if token is refresh token: {}", e.getMessage());
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String type = claims.get("type", String.class);
            return "access".equals(type) || type == null;
        } catch (JwtException e) {
            log.error("Error checking if token is access token: {}", e.getMessage());
            return false;
        }
    }

    private SecretKey getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Error creating JWT signing key: {}", e.getMessage());
            throw new RuntimeException("Failed to create JWT signing key", e);
        }
    }

    public String extractUsernameFromClaims(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    public String extractUserIdFromClaims(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public long getExpirationTimeMs(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.getTime() - System.currentTimeMillis();
        } catch (JwtException e) {
            log.error("Error getting expiration time: {}", e.getMessage());
            return 0;
        }
    }

    public long getExpirationTimeMinutes(String token) {
        return getExpirationTimeMs(token) / 60000;
    }

    public boolean willExpireWithin(String token, long minutes) {
        long expirationTimeMs = getExpirationTimeMs(token);
        return expirationTimeMs > 0 && expirationTimeMs <= (minutes * 60000);
    }

    public boolean verifyTokenSignature(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            log.error("Token signature verification failed: {}", e.getMessage());
            return false;
        }
    }
}