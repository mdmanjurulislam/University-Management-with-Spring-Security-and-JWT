package com.security.SecurityExample.auth.service;

import com.security.SecurityExample.auth.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    String secrateKey = "";

    public JWTService(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            this.secrateKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

//    public String generateSecrateKey(){
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGenerator.generateKey();
//            secrateKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//            return secrateKey;
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public String generateToken(Users user){
        return generateToken(new HashMap<>(),user);
    }

    private String generateToken(Map<String, Object> extraClaims,Users user){

//        Map<String, Objects> claims = new HashMap<>();
        extraClaims.put("userName",user.getUserName());
        extraClaims.put("userPassword",user.getUserPassword());
        extraClaims.put("userId",user.getUserId());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUserName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secrateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //step : 3
//before extract username from the token you have to extract the claim
    public String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    //step : 2
// for extracting claim you have to extract all the claim by providing token so create a method for extract all the claim
    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    //step : 1
//    here you can see that this extractAllClaims method verifying with the secret key and parsing claims from the token
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()      // Create a parser builder
                .setSigningKey(getKey()) // Set the key for signature verification
                .build()                 // Build the parser
                .parseClaimsJws(token)   // Parse and verify the token (with signature verification)
                .getBody();
    }

    public boolean isValidateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
}
