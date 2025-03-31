package com.vylitkova.closetMATE.entity.outfit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vylitkova.closetMATE.entity.joinTables.Outfit_items;
import com.vylitkova.closetMATE.entity.joinTables.Profile_outfits;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outfits")
@Entity
public class Outfit {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String image;

    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private List<LocalDate> planningToWear;

    @JsonIgnore
    @OneToMany(mappedBy = "outfit" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Outfit_items> outfit_items;

    @JsonIgnore
    @OneToMany(mappedBy = "outfit" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profile_outfits> profile_outfits;

    public Outfit(String image, String description, List<LocalDate> planningToWear) {
        this.image = image;
        this.description = description;
        this.planningToWear = planningToWear;
    }
}
