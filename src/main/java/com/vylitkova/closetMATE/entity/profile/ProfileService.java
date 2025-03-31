package com.vylitkova.closetMATE.entity.profile;


import com.vylitkova.closetMATE.api.AWSBucketService;
import com.vylitkova.closetMATE.dto.ItemDetails;
import com.vylitkova.closetMATE.entity.item.Item;
import com.vylitkova.closetMATE.entity.item.ItemRepository;
import com.vylitkova.closetMATE.entity.item.ItemService;
import com.vylitkova.closetMATE.entity.joinTables.Profile_items;
import com.vylitkova.closetMATE.entity.joinTables.Profile_outfits;
import com.vylitkova.closetMATE.entity.outfit.Outfit;
import com.vylitkova.closetMATE.entity.outfit.OutfitRepository;
import com.vylitkova.closetMATE.entity.utility.ImageInfo;
import com.vylitkova.closetMATE.mapper.ItemDetailsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ItemRepository itemRepository;
    private final OutfitRepository outfitRepository;
    private final AWSBucketService awsBucketService;
    private final ItemService itemService;
    private final ItemDetailsMapper itemDetailsMapper;

    public Optional<Profile> getProfile(UUID id) {
        return profileRepository.findById(id);
    }
    public Map<String, String> getFullProfile(UUID id) {
        return profileRepository.getProfileInfo(id);
    }
    public UUID saveProfile(Profile profile) {
        Profile savedProfile = profileRepository.save(profile);
        return savedProfile.getId();
    }
    public Profile updateProfile(UUID id, Profile new_profile) {
        Profile profile = profileRepository.findById(id).orElse(null);
        if (!Objects.equals(profile.getUsername(), new_profile.getUsername())) {
            profile.setUsername(new_profile.getUsername());
        }
        if (!Objects.equals(profile.getBio(), new_profile.getBio())) {
            profile.setBio(new_profile.getBio());
        }
        if (!Objects.equals(profile.getImage(), new_profile.getImage())) {
            profile.setImage(new_profile.getImage());
        }
        if (!Objects.equals(profile.getGender(), new_profile.getGender())) {
            profile.setGender(new_profile.getGender());
        }
        if (!Objects.equals(profile.getDateOfBirth(), new_profile.getDateOfBirth())) {
            profile.setDateOfBirth(new_profile.getDateOfBirth());
        }
        if (!Objects.equals(profile.getPublicUrl(), new_profile.getPublicUrl())){
            profile.setPublicUrl(new_profile.getPublicUrl());
        }
        if (!Objects.equals(profile.getUrlExpiryDate(), new_profile.getUrlExpiryDate())) {
            profile.setUrlExpiryDate(new_profile.getUrlExpiryDate());
        }
        return profileRepository.save(profile);
    }
    public void deleteProfile(UUID id) {
        profileRepository.deleteById(id);
    }
    public List<Item> getItems(UUID id) {
        Profile profile = profileRepository.findById(id).orElse(null);
        List<Profile_items> profile_items = profile.getProfile_items();
        List<Item> items = new ArrayList<>();
        for (Profile_items profile_item : profile_items){
            items.add(profile_item.getItem());
        }
        return items;
    }
    public List<Item> getFavItems(UUID id) {
        Profile profile = profileRepository.findById(id).orElse(null);
        List<Profile_items> profile_items = profile.getProfile_items();
        List<Item> items = new ArrayList<>();
        for (Profile_items profile_item : profile_items){
            if (profile_item.isFavourite())
                items.add(profile_item.getItem());
        }
        return items;
    }
    public Profile addItem(UUID id, Item item) {
        Profile profile = profileRepository.findById(id).orElse(null);
        Profile_items profileItem = new Profile_items();
        profileItem.setProfile(profile);
        Item newItem = new Item();
        newItem.setImage(item.getImage());
        newItem.setPurpose(item.getPurpose());
        newItem.setSeason(item.getSeason());
        newItem.setMood(item.getMood());
        newItem.setStyle(item.getStyle());
        newItem.setFormality(item.getFormality());
        newItem.setMaterial(item.getMaterial());
        newItem.setPattern(item.getPattern());
        newItem.setPublicUrl(item.getPublicUrl());
        newItem.setUrlExpiryDate(item.getUrlExpiryDate());
        newItem.setType(item.getType());
        newItem.setCategory(item.getCategory());
        newItem.setColors(item.getColors());
        newItem.setColor_warmness(item.getColor_warmness());
        newItem.setColor_darkness(item.getColor_darkness());
        newItem.setTemperature_min(item.getTemperature_min());
        newItem.setTemperature_max(item.getTemperature_max());

        profileItem.setItem(itemRepository.save(newItem));
        profile.getProfile_items().add(profileItem);
        return  profileRepository.save(profile);
    }
    public void setItemFavourite(UUID id, UUID item_id) {
        boolean current = profileRepository.getFavStatus(item_id);
        profileRepository.updateFavouriteItem(id, item_id, !current);
    }
    public List<Outfit> getOutfits(UUID id) {
        Profile profile = profileRepository.findById(id).orElse(null);
        List<Profile_outfits> profile_outfits= profile.getProfile_outfits();
        List<Outfit> outfits = new ArrayList<>();
        for (Profile_outfits profile_outfit : profile_outfits){
            outfits.add(profile_outfit.getOutfit());
        }
        return outfits;
    }
    public List<Outfit> getFavOutfits(UUID id) {
        Profile profile = profileRepository.findById(id).orElse(null);
        List<Profile_outfits> profile_outfits= profile.getProfile_outfits();
        List<Outfit> outfits = new ArrayList<>();
        for (Profile_outfits profile_outfit : profile_outfits){
            if(profile_outfit.isFavourite())
                outfits.add(profile_outfit.getOutfit());
        }
        return outfits;
    }
    public Profile addOutfit(UUID id, UUID outfit_id) {
        Profile profile = profileRepository.findById(id).orElse(null);
        Outfit outfit = outfitRepository.findById(outfit_id).orElse(null);
        Profile_outfits profile_outfit = new Profile_outfits();
        profile_outfit.setProfile(profile);
        profile_outfit.setOutfit(outfit);
        profile.getProfile_outfits().add(profile_outfit);
        return  profileRepository.save(profile);
    }
    public void setOutfitFavourite(UUID id, UUID outfit_id) {
        profileRepository.updateFavouriteOutfit(id, outfit_id);
    }


    public ImageInfo uploadImage(MultipartFile file) throws IOException {
        String filename=awsBucketService.uploadToS3(file.getBytes(), UUID.randomUUID() + ".png", "profile");
        ImageInfo imageInfo =awsBucketService.generatePresignedUrl("profile-images", filename);
        return imageInfo;
    }
    public String getProfileImgUrl(UUID id) {
        Profile profile = profileRepository.findById(id).orElse(null);
        ImageInfo imageInfo = awsBucketService.generatePresignedUrl("profile-images", profile.getImage());
        profile.setPublicUrl(imageInfo.getPublicUrl());
        profile.setUrlExpiryDate(imageInfo.getExpirationDate());
        profileRepository.save(profile);
        return imageInfo.getPublicUrl();
    }

    public List<ItemDetails> getItemsByCategory(UUID id, String category) {
        List<Object[]> results = profileRepository.getItemsByCategory(id, category);
        System.out.println(results);
        List<ItemDetails> items = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("favourite", String.valueOf(row[0]));
            itemMap.put("id", String.valueOf(row[1]));
            itemMap.put("image", String.valueOf(row[2]));
            itemMap.put("public_url", String.valueOf(row[3]));
            itemMap.put("url_expiry_date", String.valueOf(row[4]));
            itemMap.put("formality", String.valueOf(row[5]));
            itemMap.put("material", String.valueOf(row[6]));
            itemMap.put("mood", String.valueOf(row[7]));
            itemMap.put("pattern", String.valueOf(row[8]));
            itemMap.put("purpose", String.valueOf(row[9]));
            itemMap.put("season", String.valueOf(row[10]));
            itemMap.put("style", String.valueOf(row[11]));
            itemMap.put("type", String.valueOf(row[12]));
            itemMap.put("category", String.valueOf(row[13]));
            itemMap.put("color_warmness", String.valueOf(row[14]));
            itemMap.put("color_darkness", String.valueOf(row[15]));
            itemMap.put("temperature_min", String.valueOf(row[16]));
            itemMap.put("temperature_max", String.valueOf(row[17]));
            items.add(itemDetailsMapper.mapItemDetails(itemMap,itemService.getItemColors(UUID.fromString(itemMap.get("id"))) ));

        }
        return items;
    }

}
