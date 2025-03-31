package com.vylitkova.closetMATE.mapper;

import com.vylitkova.closetMATE.dto.ItemDetails;
import com.vylitkova.closetMATE.entity.item.Item;
import com.vylitkova.closetMATE.entity.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ItemDetailsMapper {
    private final ItemService itemService;
    public ItemDetails mapItemDetails(Map<String, String> map, Set<String> colors) {
        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setId(UUID.fromString(map.get("id")));
        itemDetails.setFavourite(Boolean.parseBoolean(map.get("favourite")));
        itemDetails.setImage(map.get("image"));
        itemDetails.setType(map.get("type"));
        itemDetails.setCategory(map.get("category"));
        itemDetails.setColors(colors);
        itemDetails.setColor_warmness(map.get("color_warmness"));
        itemDetails.setColor_darkness(map.get("color_darkness"));
        itemDetails.setPattern(map.get("pattern"));
        itemDetails.setMaterial(map.get("material"));
        itemDetails.setSeason(map.get("season"));
        itemDetails.setTemperature_min(Integer.parseInt(map.get("temperature_min")));
        itemDetails.setTemperature_max(Integer.parseInt(map.get("temperature_max")));
        itemDetails.setFormality(map.get("formality"));
        itemDetails.setStyle(map.get("style"));
        itemDetails.setMood(map.get("mood"));
        itemDetails.setPurpose(map.get("purpose"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String dateTimeString = map.get("url_expiry_date");
        if (dateTimeString.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{4,5}")) {
            dateTimeString = dateTimeString + "0".repeat(6 - dateTimeString.split("\\.")[1].length());
        }
        if (LocalDateTime.parse(dateTimeString, formatter).isBefore(LocalDateTime.now())){
            itemDetails.setPublic_url(itemService.getItemImgUrl(itemDetails.getId()));
            itemDetails.setUrl_expiry_date(LocalDateTime.now().plusDays(6));
        }
        else {
            itemDetails.setUrl_expiry_date(LocalDateTime.parse(dateTimeString, formatter));
            itemDetails.setPublic_url(map.get("public_url"));
        }
        return itemDetails;
    }
}
