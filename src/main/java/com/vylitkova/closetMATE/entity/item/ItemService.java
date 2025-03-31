package com.vylitkova.closetMATE.entity.item;

import com.vylitkova.closetMATE.api.AWSBucketService;
import com.vylitkova.closetMATE.api.OpenAIService;
import com.vylitkova.closetMATE.entity.item.enums.ItemType;
import com.vylitkova.closetMATE.entity.profile.Profile;
import com.vylitkova.closetMATE.entity.utility.ImageInfo;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImageService itemImageService;
    private final AWSBucketService awsBucketService;
    private final OpenAIService openAIService;

    public Optional<Item> getItem(UUID id) {
        return itemRepository.findById(id);
    }
    public void saveItem(Item item) {
        itemRepository.save(item);
    }
    public Item categoriseItem(MultipartFile file) throws Exception {
        byte[] imageWithoutBG = itemImageService.removeBackground(file);
        String fileName = awsBucketService.uploadToS3(imageWithoutBG, UUID.randomUUID() + ".png", "item");

        Item item = new Item(fileName);
        ImageInfo imageInfo=awsBucketService.generatePresignedUrl("item-images",fileName);
        Map<String, Object> item_cat = openAIService.categorizeItem(imageInfo.getPublicUrl());

        item.setType((String)item_cat.get("Type"));
        item.setCategory(ItemType.getCategoryByType(item.getType()));
        item.setColors(new HashSet<>((ArrayList<String>)item_cat.get("Colors")));
        item.setColor_warmness((String)item_cat.get("Color_warmness"));
        item.setColor_darkness((String)item_cat.get("Color_darkness"));
        item.setPattern((String)item_cat.get("Pattern"));
        item.setMaterial((String)item_cat.get("Material"));
        item.setSeason((String)item_cat.get("Season"));
        item.setTemperature_min(Integer.parseInt(String.valueOf(item_cat.get("Minimum temperature outside"))));
        item.setTemperature_max(Integer.parseInt((String)item_cat.get("Maximum temperature outside")));
        item.setFormality((String)item_cat.get("Formality"));
        item.setStyle((String)item_cat.get("Style"));
        item.setMood((String)item_cat.get("Mood"));
        item.setPurpose((String)item_cat.get("Purpose"));
        item.setPublicUrl(imageInfo.getPublicUrl());
        item.setUrlExpiryDate(imageInfo.getExpirationDate());
        return item;
    }
    public Set<String> getItemColors(UUID id) {
        List<Object[]> rawInfo= itemRepository.getItemColors(id);
        Set<String> colors = new HashSet<>();
        for (Object[] row : rawInfo) {
            colors.add((String)row[0]);
        }
        return colors;
    }
    public String getItemImgUrl(UUID id) {
        Item item = itemRepository.findById(id).orElse(null);
        ImageInfo imageInfo = awsBucketService.generatePresignedUrl("item-images", item.getImage());
        item.setPublicUrl(imageInfo.getPublicUrl());
        item.setUrlExpiryDate(imageInfo.getExpirationDate());
        itemRepository.save(item);
        return imageInfo.getPublicUrl();
    }

    public void deleteItem(UUID id) {
        Item item = itemRepository.findById(id).orElse(null);
        awsBucketService.deleteFromS3("item",item.getImage());
        itemRepository.deleteById(id);
    }

    public void deleteImage(String image) {
        awsBucketService.deleteFromS3("item", image);
    }

    public void updateItem(UUID id, @Valid Item newItem) {
        Item item = itemRepository.findById(id).orElse(null);
        item.setCategory(newItem.getCategory());
        item.setType(newItem.getType());
        item.setImage(newItem.getImage());
        item.setColors(newItem.getColors());
        item.setColor_warmness(newItem.getColor_warmness());
        item.setColor_darkness(newItem.getColor_darkness());
        item.setPattern(newItem.getPattern());
        item.setMaterial(newItem.getMaterial());
        item.setSeason(newItem.getSeason());
        item.setTemperature_min(newItem.getTemperature_min());
        item.setTemperature_max(newItem.getTemperature_max());
        item.setFormality(newItem.getFormality());
        item.setStyle(newItem.getStyle());
        item.setMood(newItem.getMood());
        item.setPurpose(newItem.getPurpose());
        item.setPublicUrl(newItem.getPublicUrl());
        item.setUrlExpiryDate(newItem.getUrlExpiryDate());
        itemRepository.save(item);
    }
}
