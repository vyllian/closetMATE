package com.vylitkova.closetMATE.registration;

import com.vylitkova.closetMATE.email.EmailSender;
import com.vylitkova.closetMATE.email.EmailValidator;
import com.vylitkova.closetMATE.registration.token.ConfirmationToken;
import com.vylitkova.closetMATE.registration.token.ConfirmationTokenService;
import com.vylitkova.closetMATE.entity.user.User;
import com.vylitkova.closetMATE.entity.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    @Autowired
    public RegistrationService(EmailValidator emailValidator, UserService userService, ConfirmationTokenService confirmationTokenService, EmailSender emailSender) {
        this.emailValidator = emailValidator;
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
    }
    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        System.out.println(request);
        System.out.println(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalArgumentException("Invalid email");
        }
        String token = userService.signUpUser(new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword()));
        String link = "http://localhost:8080/api/registration/confirm?token="+token;
        emailSender.send(request.getEmail(), emailValidator.buildEmail(request.getFirstName(), link));
        return token ;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("Sorry, token not found"));

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return "Token expired, try again";
        }

        confirmationTokenService.setConfirmedAt(token);
        User user = confirmationToken.getUser();

        if (user.getNewEmail() != null) {
            user.setEmail(user.getNewEmail());
            user.setNewEmail(null);
            userService.saveUser(user);
        } else {
            userService.enableUser(user.getEmail());
        }

        return "Thank you, email is confirmed";
    }
}
