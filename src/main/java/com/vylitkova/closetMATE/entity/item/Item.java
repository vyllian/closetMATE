package com.vylitkova.closetMATE.entity.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vylitkova.closetMATE.entity.item.enums.Color;
import com.vylitkova.closetMATE.entity.joinTables.Outfit_items;
import com.vylitkova.closetMATE.entity.joinTables.Profile_items;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
@Entity
public class Item {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String type;
    private String category;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    private Set<String> colors;
    private String color_warmness;
    private String color_darkness;

    @Column(nullable = false)
    private String pattern;
    private String material;

    @Column(nullable = false)
    private String season;
    private Integer temperature_min;
    private Integer temperature_max;

    @Column(nullable = false)
    private String formality;
    @Column(nullable = false)
    private String style;
    @Column(nullable = false)
    private String mood;
    @Column(nullable = false)
    private String purpose;
    @Column(columnDefinition = "text")
    private String publicUrl;
    @DateTimeFormat
    private LocalDateTime urlExpiryDate;

    @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Outfit_items> outfit_items;

    @JsonIgnore
    @OneToMany(mappedBy = "item" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profile_items> profile_items;


    public Item(String image) {
        this.image = image;
    }
}
