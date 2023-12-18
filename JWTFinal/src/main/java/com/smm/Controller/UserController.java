package com.smm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smm.Entity.User;
import com.smm.Service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.ServletException;
import java.security.Key;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService us;

    @Autowired
    private Key secretKey; // Inject secretKey from JwtConfig

    @PostMapping("/login")
    public String validate(@RequestBody User user) throws ServletException {
        String jwt_token = "";
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new ServletException("Please enter user and password");
        }
        String uname = user.getUsername();
        String upass = user.getPassword();

        User u1 = us.login(uname, upass);

        if (u1 == null) {
            throw new ServletException("Invalid credentials");
        }

        jwt_token = Jwts.builder()
                .setSubject(uname)
                .setIssuedAt(new Date())
                .signWith(secretKey) // Use injected secretKey
                .compact();
        return jwt_token;
    }
}

