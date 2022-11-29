package com.apu.example.springsecurityjwt.util;

import com.apu.example.springsecurityjwt.entity.UserCredential;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtHelper {

    private JwtParser jwtParser;

    private String SECRET_KEY="secret";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String createToken(UserDetails userDetails){
//        Map<String, Object> claims=new HashMap<>();
//        claims.put("authorities", userDetails.getAuthorities());
//        claims.put("roles", userDetails.getAuthorities());
//        return createToken(claims, userDetails.getUsername());
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("type", "access_token")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public Authentication getAuthentication(Claims claims, HttpServletRequest request, String token, UserDetails userDetails) {
        String username = getUsername(claims);
        String tokenType = getTokenType(token);
        List<String> authorities;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        authorities = getAuthorities(claims);
        authorities.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role)));

        request.getSession().setAttribute("userDetails", userDetails);
        request.getSession().setAttribute("tokenType", tokenType);
        return new UsernamePasswordAuthenticationToken(userDetails, "", grantedAuthorities);
    }
    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getAuthorities(Claims claims) {
        return (List<String>) claims.get("authorities");
    }

    public String getTokenType(String token) {
        if (token != null) {
            return (String) jwtParser.parse(token).getHeader().get("type");
        }
        return null;
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
    public Claims resolveClaims(HttpServletRequest req){

            String bearerToken = req.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return parseJwtClaims(bearerToken.substring("Bearer ".length()));
            }
            return null;
    }
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }
    public boolean validateClaims(Claims claims){
        return claims.getExpiration().after(new Date());
    }
}
