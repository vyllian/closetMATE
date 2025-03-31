package com.vylitkova.closetMATE.entity.joinTables;

import com.vylitkova.closetMATE.entity.item.Item;
import com.vylitkova.closetMATE.entity.profile.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "profile_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile_items {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn (name="profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn (name="item_id")
    private Item item;

    private boolean favourite = false;

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
