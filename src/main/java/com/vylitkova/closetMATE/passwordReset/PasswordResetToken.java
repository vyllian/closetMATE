package com.vylitkova.closetMATE.passwordReset;

import com.vylitkova.closetMATE.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column( updatable = false, nullable = false)
    private UUID id;

    private String token;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiresAt;

    public PasswordResetToken(String token, User user, LocalDateTime expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }
}
