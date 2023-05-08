package civilCapstone.contractB2B.user.service;

import civilCapstone.contractB2B.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class TokenProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;

    public String create(User user) {
        Date now = new Date();
        Date accessTokenValidity = new Date(now.getTime() + 1000 * 60 * 30);
        String accessToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setSubject(user.getUsername())
                .setIssuer("civilCapstone/contractB2B")
                .setIssuedAt(now)
                .setExpiration(accessTokenValidity)
                .compact();
        return accessToken;

    }

    public String validateAndGetUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateTimeToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Date expirationDate = claims.getExpiration();
        return expirationDate.after(new Date());
    }
}