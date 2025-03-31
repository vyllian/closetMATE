package com.vylitkova.closetMATE.entity.user;

import com.vylitkova.closetMATE.email.EmailSender;
import com.vylitkova.closetMATE.email.EmailValidator;
import com.vylitkova.closetMATE.entity.profile.Profile;
import com.vylitkova.closetMATE.entity.profile.ProfileRepository;
import com.vylitkova.closetMATE.entity.profile.ProfileService;
import com.vylitkova.closetMATE.registration.RegistrationService;
import com.vylitkova.closetMATE.registration.token.ConfirmationToken;
import com.vylitkova.closetMATE.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND_MESSAGE = "User with email %s not found";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ProfileRepository profileRepository;
    private final EmailSender emailSender;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public String signUpUser(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (userExists) {
            User foundUser = userRepository.findByEmail(user.getEmail()).get();
            if (!foundUser.isEnabled()) {
                String token = UUID.randomUUID().toString();
                ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), foundUser);
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                return token;
            }else throw new IllegalStateException("Email is already confirmed");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(3), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }
    public Optional<User> getUser(UUID id) {
        return userRepository.findById(id);
    }
    public List<Profile> getUsersProfiles(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user.getProfiles();
    }
    public void addProfile(UUID id, UUID profile_id) {
        User user = userRepository.findById(id).orElse(null);
        Profile profile = profileRepository.findById(profile_id).orElse(null);
        user.getProfiles().add(profile);
        userRepository.save(user);
    }
    @Transactional
    public void updatePassword(UUID userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }
        String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        EmailValidator emailValidator = new EmailValidator();
        emailSender.send(user.getEmail(), emailValidator.buildEmailPasswordChange(user.getFirstName()));

    }

    @Transactional
    public void updateEmail(UUID userId, String newEmail) {
        EmailValidator emailValidator = new EmailValidator();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }
        user.setNewEmail(newEmail);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(3), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:8080/api/registration/confirm?token=" + token;
        emailSender.send(newEmail, emailValidator.buildEmail(user.getFirstName(), link));
    }


    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean isEmailConfirmed(String email) {
        return userRepository.isConfirmed(email);
    }

    public void updateUser(UUID id, User new_user) {
        User user = userRepository.findById(id).orElse(null);
        if (!Objects.equals(user.getFirstName(), new_user.getFirstName())) {
            user.setFirstName(new_user.getFirstName());
        }
        if (!Objects.equals(user.getLastName(), new_user.getLastName())) {
            user.setLastName(new_user.getLastName());
        }
        userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteToken(id);
        userRepository.deleteById(id);
    }

    public UUID addProfile(UUID id, Profile prof) {
        User user = userRepository.findById(id).orElse(null);
        Profile profile = new Profile();
        profile.setUsername(prof.getUsername());
        profile.setImage(prof.getImage());
        profile.setBio(prof.getBio());
        profile.setGender(prof.getGender());
        profile.setDateOfBirth(prof.getDateOfBirth());
        profile.setPublicUrl(prof.getPublicUrl());
        profile.setUrlExpiryDate(prof.getUrlExpiryDate());
        profile.setUser(user);
        Profile savedProfile = profileRepository.save(profile);

        user.getProfiles().add(savedProfile);
        System.out.println(user.getProfiles());
        userRepository.save(user);
        return savedProfile.getId();
    }
}
