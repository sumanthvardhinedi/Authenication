package dev.sumanth.manual_authentication.service;

import dev.sumanth.manual_authentication.Exception.PasswordMissmatch;
import dev.sumanth.manual_authentication.Exception.TokenExpiredException;
import dev.sumanth.manual_authentication.models.Token;
import dev.sumanth.manual_authentication.models.User;
import dev.sumanth.manual_authentication.repos.TokenRepo;
import dev.sumanth.manual_authentication.repos.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.ResourceTransactionManager;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UserServiceImpl implements UserServiceInf {


    private UserRepo userRepo;
    private TokenRepo tokenRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecretKey secretKey;


    public UserServiceImpl(UserRepo userRepo, TokenRepo tokenRepo, BCryptPasswordEncoder bCryptPasswordEncoder, SecretKey secretKey) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.secretKey=secretKey;

    }
    @Override
    public User signup(String name, String email, String password) {
//        System.out.println("RAW PASSWORD = " + password);

        Optional<User> optionalUser= userRepo.findByEmail(email);
         if(optionalUser.isPresent()){
             return optionalUser.get();
         }
         User user = new User();
         user.setEmail(email);
         user.setUsername(name);
         user.setPassword(bCryptPasswordEncoder.encode(password));
         return userRepo.save(user);
    }

    @Override
    public String login(String email, String password) throws PasswordMissmatch {

        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new PasswordMissmatch("Invalid password");
        }

//       ------------------- TOKEN GENERATION LOGIC with token return value in controller and serviceinf ------------------

//       Token token = new Token();
//       token.setUser(user);
//       token.setToken(RandomStringUtils.randomAlphanumeric(128));
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, 30);
//        Date expiryDate = calendar.getTime();
//        token.setExpiryDate(expiryDate);
//        tokenRepo.save(token);
//
//        --------------------Hardcoded token generation logic using JJwt without header and signature ------------------

//       String userData = "{\n" +
//                "   \"email\": \"deepak@gmail.com\",\n" +
//               "   \"roles\": [\n" +
//               "      \"instructor\",\n" +
//               "      \"ta\"\n" +
//               "   ],\n" +
//               "   \"expiryDate\": \"22ndSept2026\"\n" +
//             "}";

//        String tokenValue = Jwts.builder()
//                .setPayload(userData)
//                .compact();
//       return tokenValue;


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date expiryDate = calendar.getTime();

        Map<String, Object> claims = new HashMap<>();
        claims.put("user", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", List.of("instructor", "ta", "student"));
        claims.put("expiryDate", expiryDate);

//        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
//
//        //secret key is in config for reusability file, we are using it to generate the token signature
//
//        SecretKey secretKey = new SecretKeySpec("mysecretkeymysecretkeymysecretkey".getBytes(StandardCharsets.UTF_8),
//                algorithm.getJcaName());


        String tokenValue = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return tokenValue;


    }
    @Override
    public User validateToken(String tokenValue) throws TokenExpiredException {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(tokenValue)
                    .getPayload();

            // ✅ Use standard expiration
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                throw new TokenExpiredException("Token expired");
            }

            // ✅ Extract userId safely
            Long userId = claims.get("user", Long.class);
            if (userId == null) {
                throw new RuntimeException("Invalid token: user missing");
            }

            return userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new TokenExpiredException("Token expired");

        } catch (io.jsonwebtoken.JwtException e) {
            throw new RuntimeException("Invalid token");

        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
    }
}
