package com.apu.example.springsecurityjwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public String createToken(UserDetails userDetails, String tokenType, List<String> authorities){
        Date tokenValidity;
        if(tokenType.equals("access_token")){
            tokenValidity = new Date(System.currentTimeMillis()+1000*60*60*10);
        }else{
            tokenValidity = new Date(System.currentTimeMillis()+1000*60*60*20);
        }

        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("authorities", authorities);


        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("type", tokenType)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(tokenValidity)
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
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            String header = new String(decoder.decode(chunks[0]));
            String payload = new String(decoder.decode(chunks[1]));
            System.out.println("Header: "+header);
            try {
                Map<String, String> result =
                        new ObjectMapper().readValue(header, HashMap.class);
                return result.get("type");
            }catch (Exception e){
                System.out.println("Exception occurred while converting into map");
            }

            return null;
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
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(bearerToken.substring("Bearer ".length()))
                        .getBody();
                return claims;
            }catch (Exception e){
                System.out.println("Exception occurred while parsing claims!");
            }
        }
        return null;
    }
    public boolean validateClaims(Claims claims){
        return claims.getExpiration().after(new Date());
    }
}
