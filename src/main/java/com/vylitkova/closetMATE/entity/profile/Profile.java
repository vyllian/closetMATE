package com.vylitkova.closetMATE.entity.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vylitkova.closetMATE.entity.joinTables.Outfit_items;
import com.vylitkova.closetMATE.entity.joinTables.Profile_items;
import com.vylitkova.closetMATE.entity.joinTables.Profile_outfits;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import com.vylitkova.closetMATE.entity.user.User;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
@Entity
public class Profile {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String image;
    @Column(nullable = false)
    private String username;
    @Column(columnDefinition = "text")
    private String bio;
    @Enumerated(EnumType.STRING)
    private ProfileGender gender = ProfileGender.UNKNOWN;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    @Column(columnDefinition = "text")
    private String publicUrl;
    @DateTimeFormat
    private LocalDateTime urlExpiryDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "profile" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profile_items> profile_items;

    @JsonIgnore
    @OneToMany(mappedBy = "profile" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profile_outfits> profile_outfits;

    public Profile(String image, String bio, ProfileGender gender, LocalDate dateOfBirth, User user) {
        this.image = image;
        this.bio = bio;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.user = user;
    }
    public Profile(String bio, ProfileGender gender, LocalDate dateOfBirth, User user) {
        this.bio = bio;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;;
        this.user = user;
    }
}
