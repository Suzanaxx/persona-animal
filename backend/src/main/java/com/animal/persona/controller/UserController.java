package com.animal.persona.controller;

import com.animal.persona.model.Users;
import com.animal.persona.repository.UsersRepository;
import com.animal.persona.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UsersRepository usersRepository;
    private final UserService userService;

    public UserController(UsersRepository usersRepository, UserService userService) {
        this.usersRepository = usersRepository;
        this.userService = userService;
    }

    // 🔁 Obstoječa endpointa
    @GetMapping
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @PostMapping
    public Users createUser(@RequestBody Users users) {
        return usersRepository.save(users);
    }

    // ✅ Novo: vrni trenutno prijavljenega uporabnika (Firebase)
    @GetMapping("/me")
    public Users getCurrentFirebaseUser(@RequestHeader("Authorization") String authorizationHeader) {
        String idToken = extractBearerToken(authorizationHeader);
        return userService.getOrCreateFirebaseUser(idToken);
    }

    // ✅ Novo: vrni anonimnega uporabnika (session)
    @GetMapping("/anonymous")
    public Users getAnonymousUser(HttpServletRequest request) {
        return userService.getCurrentUser(request);
    }

    // 🔒 Utility za obrezat "Bearer <token>"
    private String extractBearerToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }
}
