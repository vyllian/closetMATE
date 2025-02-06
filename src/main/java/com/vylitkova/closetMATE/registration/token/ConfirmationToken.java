package com.vylitkova.closetMATE.registration.token;

import com.vylitkova.closetMATE.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column( updatable = false, nullable = false)
    private UUID id;

    @Column( nullable = false)
    private String token;
    @Column( nullable = false)
    private LocalDateTime expiresAt;
    @Column( nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn( nullable = false, name = "app_user_id")
    private User user;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, User user ) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.user = user;
    }
}
