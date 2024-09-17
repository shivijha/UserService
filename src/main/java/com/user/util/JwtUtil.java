package com.user.util;

@Component
public class JwtUtil {
    private String secretKey = "your-secret-key"; // Use a more secure key in production

    public String generateToken(User user) {
        return Jwts.builder()
                   .setSubject(user.getUsername())
                   .claim("roles", user.getRole())
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                   .signWith(SignatureAlgorithm.HS256, secretKey)
                   .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                   .setSigningKey(secretKey)
                   .parseClaimsJws(token)
                   .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, User user) {
        return (extractUsername(token).equals(user.getUsername()) && !isTokenExpired(token));
    }
}

