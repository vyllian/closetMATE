package com.vylitkova.closetMATE.entity.joinTables;

import com.vylitkova.closetMATE.entity.outfit.Outfit;
import com.vylitkova.closetMATE.entity.profile.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profile_outfits")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile_outfits {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn (name="profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn (name="outfit_id")
    private Outfit outfit;

    private boolean favourite = false;

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}