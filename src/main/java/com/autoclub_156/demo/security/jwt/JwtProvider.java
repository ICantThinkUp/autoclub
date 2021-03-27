package com.autoclub_156.demo.security.jwt;

import com.autoclub_156.demo.interfaces.CarRepository;
import com.autoclub_156.demo.interfaces.UserRepository;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Log
@Component
public class JwtProvider {

    @Value("&(jwt.secret)")
    private String jwtSecret;

    public String generateToken(String login, UserRepository userRepository) {
        Date date = Date.from(LocalDate.now().plusDays(14).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.severe("Expired");
        } catch (UnsupportedJwtException mjEx) {
            log.severe("Maloinformed jwt");
        } catch (SignatureException sigEx) {
            log.severe("Signature exeption");
        } catch (Exception ex) {
            log.severe("Invalid token");
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
