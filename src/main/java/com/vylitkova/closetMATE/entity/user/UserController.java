package com.vylitkova.closetMATE.entity.user;

import com.vylitkova.closetMATE.entity.profile.Profile;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/user")
@AllArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userService.findByEmail(userDetails.getUsername());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    @GetMapping("/profiles")
    public List<Profile> getUserProfiles(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUsersProfiles(userDetails.getUsername());
    }
    @CrossOrigin("*")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> checkEmailStatus(@RequestParam String email) {
        boolean isConfirmed = userService.isEmailConfirmed(email);
        return ResponseEntity.ok(Map.of("confirmed", isConfirmed));
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") UUID id) {
        return userService.getUser(id).orElseThrow(EntityNotFoundException::new);
    }

    @PutMapping("/edit/{id}")
    public void editUser(@PathVariable("id") UUID id, @RequestBody User user) {
        userService.updateUser(id, user);
    }
    @PatchMapping("/{id}/add-profile")
    public UUID addUserProfile(@PathVariable("id") UUID id, @RequestBody Profile profile) {
        return userService.addProfile(id, profile);
    }
    @PatchMapping("/{id}/change-email")
    public void changeEmail(@PathVariable("id") UUID id, @RequestBody String email) {
        userService.updateEmail(id, email);
    }
    @PatchMapping("/{id}/change-password")
    public void updatePassword(@PathVariable UUID id, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        userService.updatePassword(id, passwordUpdateRequest.getOldPassword(), passwordUpdateRequest.getNewPassword());
    }
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
    }


}
