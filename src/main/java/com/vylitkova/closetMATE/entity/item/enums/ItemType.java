package com.vylitkova.closetMATE.entity.item.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum ItemType {
    puffer("outer"),
    bomber("outer"),
    coat("outer"),
    trench_coat("outer"),
    jacket("outer"),
    blazer("outer"),
    anorak("outer"),
    dress("overall"),
    jumpsuit("overall"),
    cardigan("top"),
    hoodie("top"),
    sweater("top"),
    turtleneck("top"),
    jumper("top"),
    longsleeve("top"),
    shirt("top"),
    tshirt("top"),
    vest("top"),
    crop_top("top"),
    bra("top"),
    jeans("bottom"),
    pants("bottom"),
    sweatpants("bottom"),
    shorts("bottom"),
    skirt("bottom"),
    high_boots("shoes"),
    boots("shoes"),
    sandals("shoes"),
    maryjanes("shoes"),
    moccasins("shoes"),
    ballerina_flats("shoes"),
    sneakers("shoes"),
    loafers("shoes"),
    flip_flops("shoes"),
    stilettos("shoes"),
    slippers("shoes"),
    bag("accessory"),
    belt("accessory"),
    bodychain("accessory"),
    glasses("accessory"),
    gloves("accessory"),
    hat("accessory"),
    cap("accessory"),
    neckerchief("accessory"),
    scarf("accessory"),
    tie("accessory"),
    tights("accessory"),
    socks("accessory"),
    jewerly("accessory"),
    backpack("accessory"),
    purse("accessory"),
    beret( "accessory");

    private final String category;
    private static final Map<String, ItemType> TYPE_MAP = Stream.of(values())
            .collect(Collectors.toMap(Enum::name, e -> e));

    public static String getCategoryByType(String type) {
        ItemType itemType = TYPE_MAP.get(type.toLowerCase()); // Приведення до нижнього регістру
        return itemType.getCategory();
    }
}
