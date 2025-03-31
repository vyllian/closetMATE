package com.vylitkova.closetMATE.passwordReset;

import com.vylitkova.closetMATE.email.EmailSender;
import com.vylitkova.closetMATE.entity.user.User;
import com.vylitkova.closetMATE.entity.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetService {
    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder passwordEncoder;

    public void createPasswordResetToken(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(30));

        passwordResetTokenRepository.save(passwordResetToken);

        String link = "http://localhost:8080/api/password-reset/confirm?token=" + token;
        emailSender.send(email, "Click the link to reset your password: " + link);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);
        passwordResetTokenRepository.delete(resetToken);
    }
}
