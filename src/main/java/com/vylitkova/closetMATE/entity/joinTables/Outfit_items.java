package com.vylitkova.closetMATE.entity.joinTables;

import com.vylitkova.closetMATE.entity.item.Item;
import com.vylitkova.closetMATE.entity.outfit.Outfit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "outfit_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Outfit_items {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn (name="outfit_id")
    private Outfit outfit;

    @ManyToOne
    @JoinColumn (name="item_id")
    private Item item;

    @Column(nullable = false)
    private int position;

}
