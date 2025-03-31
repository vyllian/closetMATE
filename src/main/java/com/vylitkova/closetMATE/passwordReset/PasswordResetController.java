package com.vylitkova.closetMATE.passwordReset;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
@AllArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email) {
        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok("Password reset link sent");
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPasswordReset(@RequestBody Map<String, String> request) {
        passwordResetService.resetPassword(request.get("token"), request.get("newPassword"));
        return ResponseEntity.ok("Password reset successful");
    }
}

