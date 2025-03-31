package com.vylitkova.closetMATE.entity.outfit;

import com.vylitkova.closetMATE.entity.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutfitRequest {
    private Outfit outfit;
    private HashMap<Integer, UUID> items;
}
