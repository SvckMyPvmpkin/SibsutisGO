package com.sibsutisgo.controller;

import com.sibsutisgo.dto.LoginRequest;
import com.sibsutisgo.model.*;
import com.sibsutisgo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(Map.of("accessToken", userService.login(req.email())));
    }

    @PostMapping("/passengers")
    public ResponseEntity<Map<String, String>> regPassenger(@RequestBody Passengers p) {
        return ResponseEntity.ok(Map.of("accessToken", userService.registerPassenger(p)));
    }

    @PostMapping("/drivers")
    public ResponseEntity<Map<String, String>> regDriver(@RequestBody Drivers d) {
        return ResponseEntity.ok(Map.of("accessToken", userService.registerDriver(d)));
    }

    @GetMapping("/passengers/{id}")
    public ResponseEntity<Passengers> getP(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getPassenger(id));
    }

    @GetMapping("/drivers/{id}")
    public ResponseEntity<Drivers> getD(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDriver(id));
    }

    @PatchMapping("/drivers/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> req) {
        userService.updateDriverStatus(id, req.get("status"));
        return ResponseEntity.ok().build();
    }
}
